package com.dbapp.bean;

import com.dbapp.bean.table.TextTreeTable;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class SenderConfig {
    private static ProtocolType protocol = ProtocolType.UDP;
    private static String ip = "127.0.0.1";
    private static int port = 514;
    private static int speed = 1;
    private static long total = -1;
    private static List<File> files = new ArrayList<>();
    private static String originFilePath = null;
    private static Charset fileEncoding = Charset.defaultCharset();
    private static Charset logEncoding = Charset.defaultCharset();
    private static List<String> data = null;
    private static boolean dataLoaded = false;
    private static String apiKey = null;
    private static String server = null;
    private static String topic = null;
    // aes
    private static boolean useAes;
    private static String aesKey;
    private static String aesInitVector;
    private static String aesMode;
    private static String aesPadding;
    // logHeader
    private static boolean useLogHeader;
    private static String logHeader;
    // locale
    private static String locale;
    private static boolean en;

    public static void update(AutoConfig autoConfig) {
        if (autoConfig == null) {
            return;
        }
        updateProtocol(autoConfig.getProtocol());
        updateIp(autoConfig.getIp());
        updatePort(autoConfig.getPort());
        updateSpeed(autoConfig.getSpeed());
        updateTotal(autoConfig.getTotal());
        updateFiles(autoConfig.getFile());
        updateFileEncoding(autoConfig.getFileEncoding());
        updateLogEncoding(autoConfig.getLogEncoding());
        updateApiKey(autoConfig.getApiKey());
        updateServer(autoConfig.getServer());
        updateTopic(autoConfig.getTopic());
        updateUseAes(autoConfig.isUseAes());
        updateAesKey(autoConfig.getAesKey());
        updateAesInitVector(autoConfig.getAesInitVector());
        updateAesMode(autoConfig.getAesMode());
        updateAesPadding(autoConfig.getAesPadding());
        updateUseLogHeader(autoConfig.isUseLogHeader());
        updateLogHeader(autoConfig.getLogHeader());
        updateLocale(autoConfig.getLocale());
    }

    public synchronized static void updateLogEncoding(String encoding) {
        try {
            logEncoding = Charset.forName(encoding);
        } catch (Exception ignored) {
        }
    }

    public synchronized static void updateFileEncoding(String encoding) {
        try {
            fileEncoding = Charset.forName(encoding);
            dataLoaded = false;
        } catch (Exception ignored) {
        }
    }

    public synchronized static void updateFiles(String path) {
        List<File> fileList = new ArrayList<>();
        if (StringUtils.isNotEmpty(path)) {
            recursiveForFile(new File(path), fileList);
        }
        files = fileList;
        originFilePath = path;
        dataLoaded = false;
    }

    public synchronized static void loadData() {
        List<String> list = new ArrayList<>();
        for (File file : files) {
            try (FileInputStream inputStream = new FileInputStream(file);
                 InputStreamReader reader = new InputStreamReader(inputStream, fileEncoding);
                 BufferedReader bufferedReader = new BufferedReader(reader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    list.add(line);
                }
            } catch (Exception e) {
                System.out.println("load data from file: " + file.getAbsolutePath() + "  failed.");
            }
        }
        data = list;
        dataLoaded = true;
    }

    public synchronized static List<String> getData() {
        if (!dataLoaded) {
            loadData();
        }
        return data;
    }

    public synchronized static void updateTotal(long totalIn) {
        total = totalIn;
    }

    public synchronized static void updateSpeed(int speedIn) {
        speed = speedIn;
    }

    public synchronized static void updatePort(int portIn) {
        port = portIn;
    }

    public synchronized static void updateProtocol(String protocolIn) {
        protocol = ProtocolType.of(protocolIn);
    }

    public synchronized static void updateIp(String ipIn) {
        ip = ipIn;
    }

    public synchronized static void updateApiKey(String apiKeyIn) {
        apiKey = apiKeyIn;
    }

    public synchronized static void updateServer(String serverIn) {
        server = serverIn;
    }

    public synchronized static void updateTopic(String topicIn) {
        topic = topicIn;
    }

    public synchronized static void updateUseAes(boolean useAesIn) {
        useAes = useAesIn;
    }

    public synchronized static void updateUseLogHeader(boolean useLogHeaderIn) {
        useLogHeader = useLogHeaderIn;
    }

    public synchronized static void updateLogHeader(String logHeaderIn) {
        logHeader = logHeaderIn;
    }

    public synchronized static void updateLocale(String localeIn) {
        locale = localeIn;
        if ("en".equals(StringUtils.lowerCase(localeIn))) {
            en = true;
        }
    }

    public synchronized static void updateAesKey(String aesKeyIn) {
        aesKey = aesKeyIn;
    }

    public synchronized static void updateAesInitVector(String aesInitVectorIn) {
        aesInitVector = aesInitVectorIn;
    }

    public synchronized static void updateAesMode(String aesModeIn) {
        aesMode = aesModeIn;
    }

    public synchronized static void updateAesPadding(String aesPaddingIn) {
        aesPadding = aesPaddingIn;
    }

    private static void recursiveForFile(File file, List<File> files) {
        if (!file.exists() || !file.canRead()) {
            return;
        }
        if (file.isFile()) {
            files.add(file);
            return;
        }
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles == null) {
                return;
            }
            for (File child : subFiles) {
                recursiveForFile(child, files);
            }
        }
    }

    public static AutoConfig toConfig() {
        AutoConfig config = new AutoConfig();
        config.setProtocol(protocol.name());
        config.setIp(ip);
        config.setPort(port);
        config.setSpeed(speed);
        config.setTotal(total);
        if(!originFilePath.contains("\\\\") && originFilePath.contains("\\")) {
            config.setFile(originFilePath.replaceAll("\\\\", "\\\\\\\\"));
        }else {
            config.setFile(originFilePath);
        }
        config.setFileEncoding(fileEncoding.displayName());
        config.setLogEncoding(logEncoding.displayName());
        config.setApiKey(apiKey);
        config.setServer(server);
        config.setTopic(topic);
        config.setUseAes(useAes);
        config.setAesKey(aesKey);
        config.setAesInitVector(aesInitVector);
        config.setAesMode(aesMode);
        config.setAesPadding(aesPadding);
        config.setUseLogHeader(useLogHeader);
        config.setLogHeader(logHeader);
        config.setLocale(locale);
        return config;
    }

    private static String formatStr(String s) {
        return s == null ? "" : s;
    }

    public static String description() {
        String[] names = {en ? "Msg Send Type: " : "发送方式：", protocol.name()};
        List<String[]> values = new ArrayList<>();
        if (en) {
            values.add(new String[]{"Use Aes Decrypt: ", useAes ? "Yes" : "No"});
        } else {
            values.add(new String[]{" 使用AES加密：", useAes ? "是" : "否"});
        }
        if (useAes) {
            values.add(new String[]{en ? "AES Key: " : "  AES 密钥： ", formatStr(aesKey)});
            values.add(new String[]{en ? "AES IV: " : "  AES IV偏移量： ", formatStr(aesInitVector)});
            values.add(new String[]{en ? "AES Mode: " : "  AES 解密模式： ", StringUtils.upperCase(formatStr(aesMode))});
            values.add(new String[]{en ? "AES Padding: " : "  AES 补码方式： ", StringUtils.upperCase(formatStr(aesPadding))});
        }
        if (en) {
            values.add(new String[]{"Append Log Header: ", useLogHeader ? "Yes" : "No"});
        } else {
            values.add(new String[]{"添加日志头：", useLogHeader ? "是" : "否"});
        }
        if (useLogHeader) {
            values.add(new String[]{en ? "Log Header: " : "  日志头： ", formatStr(logHeader)});
        }
        if (!ProtocolType.KAFKA.equals(protocol)) {
            if (ProtocolType.HTTP.equals(protocol)) {
                values.add(new String[]{en ? "Send Address: " : "发送地址: ", "http://" + ip + ":" + port + "/c/externalHttpReceiver2"});
            } else {
                values.add(new String[]{en ? "Send Address: Port" : "发送地址：端口", formatStr(ip) + ":" + port});
            }
        }
        values.add(new String[]{en ? "Send Speed(EPS): " : "发送速度(EPS)：", String.valueOf(speed)});
        values.add(new String[]{en ? "Send Total: " : "发送总数：", (total == -1) ? en ? "unlimited" : "无限制" : String.valueOf(total)});
        if (ProtocolType.HTTP.equals(protocol)) {
            values.add(new String[]{en ? "Http AuthKey(apiKey)" : "Http授权码(apiKey)：", formatStr(apiKey)});
        }
        if (ProtocolType.KAFKA.equals(protocol)) {
            values.add(new String[]{en ? "Kafka Address(server): " : "Kafka服务地址(Server)：", formatStr(server)});
            values.add(new String[]{en ? "Kafka Topic" : "Kafka主题(Topic)：", formatStr(topic)});
        }
        values.add(new String[]{en ? "logFileEncoding / sendLogEncoding" : "文件编码 / 发送编码：", fileEncoding.displayName() + " / " + logEncoding.displayName()});
        int size = files.size();
        if (size < 1) {
            values.add(new String[]{en ? "logFile" : "日志文件：", "-"});
        } else if (size == 1) {
            values.add(new String[]{en ? "logFile" : "日志文件：", files.get(0).getAbsolutePath()});
        } else {
            values.add(new String[]{en ? "logFile" : "日志文件：", ""});
        }
        TextTreeTable textTreeTable = new TextTreeTable(names, values.toArray(new String[0][]));
        textTreeTable.printTable();
        if (size > 1) {
            for (File file : files) {
                System.out.println("  <file> " + file.getAbsolutePath());
            }
        }
        System.out.println();
        return "";
    }

    public static ProtocolType getProtocol() {
        return protocol;
    }

    public static String getIp() {
        return ip;
    }

    public static int getPort() {
        return port;
    }

    public static int getSpeed() {
        return speed;
    }

    public static long getTotal() {
        return total;
    }

    public static List<File> getFiles() {
        return files;
    }

    public static Charset getFileEncoding() {
        return fileEncoding;
    }

    public static Charset getLogEncoding() {
        return logEncoding;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getServer() {
        return server;
    }

    public static String getTopic() {
        return topic;
    }

    public static boolean isUseAes() {
        return useAes;
    }

    public static String getAesKey() {
        return aesKey;
    }

    public static String getAesInitVector() {
        return aesInitVector;
    }

    public static String getAesMode() {
        return aesMode;
    }

    public static String getAesPadding() {
        return aesPadding;
    }

    public static boolean isUseLogHeader() {
        return useLogHeader;
    }

    public static String getLogHeader() {
        return logHeader;
    }

    public static boolean isEn() {
        return en;
    }
}
