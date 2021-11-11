package com.dbapp.service;

import com.dbapp.bean.FileModel;
import com.dbapp.bean.SenderConfig;
import com.dbapp.bean.table.TextTreeTable;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

/**
 * 展示服务
 * 1.进行数据格式的排版
 * 2.进行数据的显示
 *
 * @author xutao
 */
public class DisplayService {

    public static final String TAB_FORMAT_CHAR = "\t";
    public static final String FORMAT_HEADER = StringUtils.EMPTY;
    public static final String LINE_BREAK = System.lineSeparator();
    private static final long ONE_KB = 1024;
    private static final String B = "B";
    private static final String KB = "KB";
    private static final String MB = "MB";
    private static final String GB = "GB";
    private static final String TB = "TB";
    private static final String PB = "PB";
    private static final long ONE_MB = 1048576L;
    private static final long ONE_GB = 1073741824L;
    private static final long ONE_TB = 1099511627776L;
    private static final long ONE_PB = 1125899906842624L;
    private static final int DEFAULT_SCALE = 2;


    private DisplayService() {
    }

    public static void error(Throwable e) {
        if (Objects.isNull(e)) {
            return;
        }
        System.out.println(e.getMessage());
    }

    public static void welcome() {
        String banner = "";
        banner += LINE_BREAK;
        banner += "SOC LOG SENDER 2.0                                     " + LINE_BREAK;
        banner += "Java version: " + System.getProperty("java.version") + LINE_BREAK;
        banner += "Java Home: " + System.getProperty("java.home") + LINE_BREAK;
        banner += "OS: " + System.getProperty("os.name") + ", Version: " + System.getProperty("os.version");
        banner += LINE_BREAK;
        banner += LINE_BREAK;
        System.out.print(banner);
    }

    public static void exit() {
        System.out.println(SenderConfig.isEn() ? "\nsee you next time.\n" : "\n欢迎下次使用.\n");
    }

    static void cmdNotExists(String cmd) {
        String msg = SenderConfig.isEn() ? "command <%s> not exists" : "命令「 %s 」不存在";
        System.out.println(format(msg, cmd));
    }

    public static void directPrint(String word) {
        System.out.println(word);
    }

    public static void directPrint(String msg, Throwable e) {
        System.out.println(msg);
        if (e != null) {
            e.printStackTrace();
        }
    }

    public static void socException(String message) {
        System.out.println(message);
    }

    public static void updateSuccess() {
        System.out.println(SenderConfig.isEn() ? "update success!" : "更新成功！");
    }


    public static void llResult(List<FileModel> catalogList) {
        String[] names = new String[3];
        if (SenderConfig.isEn()) {
            names[0] = "FileName";
            names[1] = "FileType";
            names[2] = "FileSize";
        } else {
            names[0] = "文件名";
            names[1] = "文件类型";
            names[2] = "文件大小";
        }
        String[][] data = null;
        if (CollectionUtils.isNotEmpty(catalogList)) {
            data = new String[catalogList.size()][names.length];
            for (int i = 0; i < catalogList.size(); i++) {
                data[i][0] = catalogList.get(i).getFileName();
                if (SenderConfig.isEn()) {
                    data[i][1] = catalogList.get(i).getType().getDisplayNameEn();
                } else {
                    data[i][1] = catalogList.get(i).getType().getDisplayNameCn();
                }
                data[i][2] = human(catalogList.get(i).getLength());
            }
        }
        TextTreeTable textTreeTable = new TextTreeTable(names, data);
        textTreeTable.printTable();
    }

    private static String human(long size) {
        if (size < ONE_KB) {
            return size + B;
        }
        if (size < ONE_MB) {
            return BigDecimal.valueOf((double) size / ONE_KB)
                    .setScale(DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP) + KB;
        }
        if (size < ONE_GB) {
            return BigDecimal.valueOf((double) size / ONE_MB)
                    .setScale(DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP) + MB;
        }
        if (size < ONE_TB) {
            return BigDecimal.valueOf((double) size / ONE_GB)
                    .setScale(DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP) + GB;
        }
        if (size < ONE_PB) {
            return BigDecimal.valueOf((double) size / ONE_TB)
                    .setScale(DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP) + TB;
        }
        return BigDecimal.valueOf((double) size / ONE_PB)
                .setScale(DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP) + PB;
    }

    public static void lsResult(List<FileModel> catalogList) {
        if (CollectionUtils.isEmpty(catalogList)) {
            return;
        }
        StringBuilder catalogListDisplay = new StringBuilder(FORMAT_HEADER);
        for (FileModel catalogModel : catalogList) {
            catalogListDisplay.append(catalogModel.getFileName()).append(TAB_FORMAT_CHAR);
        }
        catalogListDisplay.append(LINE_BREAK);
        System.out.println(catalogListDisplay.toString());
    }

    public static void pwdResult(String nameList) {
        System.out.println(nameList);
    }
}
