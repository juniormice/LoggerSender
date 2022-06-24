package com.dbapp.util;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util {

    public final static String ENCODING_BASE64 = "base64";
    public final static String ENCODING_BASE64_DISPLAY = "BASE-64";

    private static Charset fileEncoding;
    private static Charset logEncoding;

    static {
        String fileEncodingEnv = System.getenv("FILE_ENCODING");
        if (StringUtils.isNotEmpty(fileEncodingEnv)) {
            try {
                fileEncoding = Charset.forName(fileEncodingEnv);
            } catch (Exception e) {
                System.out.println("read file encoding [" + fileEncodingEnv + "] failed. using default.");
            }
        }
        if (fileEncoding == null) {
            fileEncoding = StandardCharsets.UTF_8;
        }

        String logEncodingEnv = System.getenv("LOG_ENCODING");
        if (StringUtils.isNotEmpty(logEncodingEnv)) {
            try {
                logEncoding = Charset.forName(logEncodingEnv);
            } catch (Exception e) {
                System.out.println("read log encoding [" + logEncodingEnv + "] failed. using default.");
            }
        }
        if (logEncoding == null) {
            logEncoding = StandardCharsets.UTF_8;
        }
    }

    public static String decode(String str) {
        byte[] data = str.getBytes(StandardCharsets.UTF_8);
        byte[] decode = Base64.getDecoder().decode(data);
        return new String(decode, fileEncoding);
    }

    public static String encode(String str) {
        byte[] data = str.getBytes(logEncoding);
        byte[] result = Base64.getEncoder().encode(data);
        return new String(result, StandardCharsets.UTF_8);
    }
}
