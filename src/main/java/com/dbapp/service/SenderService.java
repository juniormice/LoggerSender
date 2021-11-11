package com.dbapp.service;

import com.dbapp.bean.EffectiveConfig;
import com.dbapp.bean.ProtocolType;
import com.dbapp.bean.SenderConfig;
import com.dbapp.bean.aes.AesEncryption;
import com.dbapp.exception.SocException;
import com.dbapp.service.sender.Sender;
import com.dbapp.service.sender.http.HttpSender;
import com.dbapp.service.sender.kafka.KafkaSender;
import com.dbapp.service.sender.snmptrap.SnmpTrapSender;
import com.dbapp.service.sender.tcp.TcpSender;
import com.dbapp.service.sender.udp.UdpSender;
import com.dbapp.util.Counter;
import com.dbapp.util.CounterManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SenderService {

    private static boolean running = false;
    private static Thread run;
    private static Sender sender;

    private static Sender getSender(ProtocolType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case UDP:
                return new UdpSender();
            case TCP:
                return new TcpSender();
            case SNMPTRAP:
                return new SnmpTrapSender();
            case HTTP:
                return new HttpSender();
            case KAFKA:
                return new KafkaSender();
            default:
                return null;
        }
    }


    public static void startSend() throws SocException {
        if (run != null) {
            run.interrupt();
            run = null;
        }
        running = true;
        run = new Thread() {
            @Override
            public void run() {
                if (sender != null) {
                    sender.stop();
                    sender = null;
                }
                DisplayService.directPrint(SenderConfig.isEn() ? "Prepare To Send Log" : "准备发送日志!");
                Sender sender = getSender(SenderConfig.getProtocol());
                if (sender == null) {
                    DisplayService.directPrint(SenderConfig.isEn() ? "This type is not support: " : "该协议日志暂时不可以发送：" + SenderConfig.getProtocol().name());
                    running = false;
                    return;
                }
                AesEncryption aesEncryption = null;
                if (SenderConfig.isUseAes()) {
                    try {
                        aesEncryption = new AesEncryption(SenderConfig.getAesKey(), SenderConfig.getAesInitVector(),
                                SenderConfig.getAesMode(), SenderConfig.getAesPadding());
                    } catch (Exception e) {
                        DisplayService.directPrint(SenderConfig.isEn() ? "AES Config With Error" : "AES配置有误!", e);
                        running = false;
                        return;
                    }
                }
                try {
                    sender.init(new EffectiveConfig());
                } catch (Exception e) {
                    sender.stop();
                    DisplayService.directPrint("Sender Init Failed. ", e);
                }
                List<String> data = SenderConfig.getData().parallelStream()
                        .filter(StringUtils::isNotEmpty)
                        .collect(Collectors.toList());
                int speed = SenderConfig.getSpeed();
                int batch;
                long shouldTime;
                if (speed < 10) {
                    batch = speed;
                    shouldTime = 1000L;
                } else {
                    batch = speed / 10;
                    shouldTime = 100;
                }
                if (data.size() == 0) {
                    running = false;
                    System.out.println(SenderConfig.isEn() ? "No Data" : "无数据");
                    return;
                }
                Charset logEncoding = SenderConfig.getLogEncoding();
                List<List<String>> dataSList = prepareData(data, batch);
                if (SenderConfig.isUseAes()) {
                    dataSList = prepareAes(dataSList, aesEncryption, logEncoding);
                }
                if (SenderConfig.isUseLogHeader()) {
                    dataSList = prepareLogHeader(dataSList, SenderConfig.getLogHeader());
                }

                List<List<byte[]>> dataBList = new ArrayList<>(dataSList.size());
                try {
                    for (List<String> listS : dataSList) {
                        List<byte[]> lb = new ArrayList<>();
                        for (String s : listS) {
                            byte[] sb = s.getBytes(logEncoding);
                            lb.add(sb);
                        }
                        dataBList.add(lb);
                    }
                } catch (Exception e) {
                    DisplayService.directPrint(SenderConfig.isEn() ? "Data Prepare Failed!" : "数据准备失败！", e);
                    running = false;
                    sender.stop();
                    return;
                }
                int dataSize = dataBList.size();
                long goal = SenderConfig.getTotal();
                int cnt = 0;
                Counter counter = CounterManager.createCounter(sender.getName());
                DisplayService.directPrint(StringUtils.EMPTY);
                DisplayService.directPrint(SenderConfig.isEn() ? "Start To Send Log" : "开始发送日志");
                CounterManager.getInstance().start();
                while (running) {
                    try {
                        long start = System.currentTimeMillis();
                        if (cnt < dataSize) {
                            sender.send(dataBList.get(cnt));
                            sender.send(dataSList.get(cnt), logEncoding);
                        } else {
                            sender.send(dataBList.get(0));
                            sender.send(dataSList.get(0), logEncoding);
                        }
                        cnt++;
                        counter.countSent(batch);
                        long cost = System.currentTimeMillis() - start;
                        long diff = shouldTime - cost;
                        if (goal != -1 && goal <= counter.getSentTotal()) {
                            break;
                        }
                        if (diff > 0) {
                            Thread.sleep(diff);
                        }
                    } catch (InterruptedException i) {
                        System.out.println("Interrupted Sender!");
                        return;
                    }
                }
            }
        };
        run.start();
    }

    private static List<List<String>> prepareLogHeader(List<List<String>> dataSList, String logHeader) {
        if (CollectionUtils.isEmpty(dataSList)) {
            return dataSList;
        }
        String header = logHeader == null ? StringUtils.EMPTY : logHeader;
        List<List<String>> listList = new ArrayList<>(dataSList.size());
        for (List<String> list : dataSList) {
            listList.add(list.stream()
                    .map(s -> header + " " + s)
                    .collect(Collectors.toList()));
        }
        return listList;
    }

    private static List<List<String>> prepareAes(List<List<String>> dataSList, AesEncryption encryption, Charset logEncoding) {
        if (CollectionUtils.isEmpty(dataSList) || encryption == null) {
            return dataSList;
        }
        List<List<String>> listList = new ArrayList<>(dataSList.size());
        for (List<String> list : dataSList) {
            listList.add(list.stream()
                    .map(s -> encryption.encryptToString(s.getBytes(logEncoding)))
                    .collect(Collectors.toList()));
        }
        return listList;
    }

    private static List<List<String>> prepareData(List<String> data, int batch) {
        List<List<String>> dataSList = new ArrayList<>();
        if (data.size() < batch) {
            List<String> listS = new ArrayList<>();
            int i = 0;
            for (int j = 0; j < batch; j++) {
                if (i < data.size()) {
                    listS.add(data.get(i));
                } else {
                    listS.add(data.get(0));
                    i = 1;
                }
                i++;
            }
            dataSList.add(listS);
        } else {
            int j = 0;
            while (j < data.size()) {
                if (batch + j < data.size()) {
                    dataSList.add(data.subList(j, j + batch));
                } else {
                    List<String> listS = new ArrayList<>(data.subList(j, data.size()));
                    listS.addAll(data.subList(0, batch - listS.size()));
                    dataSList.add(listS);
                }
                j = j + batch;
            }
        }
        return dataSList;
    }

    public static void stopSend() throws SocException {
        running = false;
        if (run != null) {
            run.interrupt();
            run = null;
        }
        if (sender != null) {
            sender.stop();
            sender = null;
        }
        CounterManager.getInstance().stop();
        System.out.println("Stop Sending!");
    }
}
