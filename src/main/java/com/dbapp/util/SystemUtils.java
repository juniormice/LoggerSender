package com.dbapp.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 系统工具类
 *
 * @author xutao
 */
public class SystemUtils {
    private static final Pattern WINDOWS_OS_NAME_PATTERN = Pattern.compile("(?i)window.*");
    private static final Pattern LINUX_OS_NAME_PATTERN = Pattern.compile("(?i)linux.*");
    private static final String OS_NAME_PROPERTY_KEY = "os.name";
    private static final String WINDOWS_FILE_PREFIX = ":";
    private static final String LINUX_FILE_PREFIX = "/";

    private static final boolean WINDOWS_OS = isWindowOS();
    private static final boolean LINUX_OS = isLinuxOS();

    public static boolean isAbsolutePath(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }
        if (LINUX_OS) {
            return filePath.startsWith(LINUX_FILE_PREFIX);
        }
        if (WINDOWS_OS) {
            return filePath.indexOf(WINDOWS_FILE_PREFIX) > 0;
        }
        return false;
    }

    private static boolean isWindowOS() {
        String osName = System.getProperty(OS_NAME_PROPERTY_KEY);
        return WINDOWS_OS_NAME_PATTERN.matcher(osName).matches();
    }

    private static boolean isLinuxOS() {
        String osName = System.getProperty(OS_NAME_PROPERTY_KEY);
        return LINUX_OS_NAME_PATTERN.matcher(osName).matches();
    }
}
