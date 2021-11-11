package com.dbapp.util;

import com.dbapp.bean.SenderConfig;
import com.dbapp.exception.SocException;

import java.io.*;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 文件工具类，用来处理与文件的交互
 *
 * @author xutao
 */
public class FileUtils {

    public static String readFileAsString(String filePath) throws SocException {
        File file = new File(filePath);
        if (!file.exists() || !file.canRead() || !file.isFile()) {
            String msg = SenderConfig.isEn() ? "file not exists(%s),or cannot be access!" : "文件不存在(%s)，或无法访问!";
            throw new SocException(format(msg, filePath));
        }

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, UTF_8)) {
            char[] buf = new char[1024];
            StringBuilder builder = new StringBuilder();

            int count;
            while ((count = inputStreamReader.read(buf, 0, buf.length)) != -1) {
                builder.append(buf, 0, count);
            }
            return builder.toString();
        } catch (Exception e) {
            String msg = SenderConfig.isEn() ? "(%s) data load failed." : "（%s）数据读取失败. %s";
            throw new SocException(format(msg, filePath, e));
        }
    }

    public static void writeContentToFile(String content, String filePath) throws SocException {
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.delete()) {
                throw new SocException(SenderConfig.isEn() ? "origin file cannot be delete." : "原文件无法删除");
            }
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, UTF_8)) {
            outputStreamWriter.write(content);
            outputStreamWriter.flush();
        } catch (Exception e) {
            throw new SocException(e);
        }
    }

}
