package com.dbapp.bean;

import org.apache.commons.lang3.StringUtils;

public class AutoConfig {
    private static final String TAB = "\t";
    private static final String LINE = System.lineSeparator();
    private String protocol;
    private String ip;
    private int port;
    private int speed;
    private long total;
    private String file;
    private String fileEncoding;
    private String logEncoding;
    // http
    private String apiKey;
    // kafka
    private String server;
    private String topic;
    // aes
    private boolean useAes;
    private String aesKey;
    private String aesInitVector;
    private String aesMode;
    private String aesPadding;
    // logHeader
    private boolean useLogHeader;
    private String logHeader;
    // locale
    private String locale;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileEncoding() {
        return fileEncoding;
    }

    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    public String getLogEncoding() {
        return logEncoding;
    }

    public void setLogEncoding(String logEncoding) {
        this.logEncoding = logEncoding;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean isUseAes() {
        return useAes;
    }

    public void setUseAes(boolean useAes) {
        this.useAes = useAes;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getAesInitVector() {
        return aesInitVector;
    }

    public void setAesInitVector(String aesInitVector) {
        this.aesInitVector = aesInitVector;
    }

    public String getAesMode() {
        return aesMode;
    }

    public void setAesMode(String aesMode) {
        this.aesMode = aesMode;
    }

    public String getAesPadding() {
        return aesPadding;
    }

    public void setAesPadding(String aesPadding) {
        this.aesPadding = aesPadding;
    }

    public boolean isUseLogHeader() {
        return useLogHeader;
    }

    public void setUseLogHeader(boolean useLogHeader) {
        this.useLogHeader = useLogHeader;
    }

    public String getLogHeader() {
        return logHeader;
    }

    public void setLogHeader(String logHeader) {
        this.logHeader = logHeader;
    }

    private String formatStr(String s) {
        return s == null ? StringUtils.EMPTY : s;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String format() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{").append(LINE);
        stringBuilder.append(TAB).append("\"useAes\": ").append(useAes).append(",").append(LINE);
        stringBuilder.append(TAB).append("\"aesKey\": ").append("\"").append(formatStr(aesKey)).append("\",").append(LINE);
        stringBuilder.append(TAB).append("\"aesInitVector\": ").append("\"").append(formatStr(aesInitVector)).append("\",").append(LINE);
        stringBuilder.append(TAB).append("\"aesMode\": ").append("\"").append(formatStr(aesMode)).append("\",").append(LINE);
        stringBuilder.append(TAB).append("\"aesPadding\": ").append("\"").append(formatStr(aesPadding)).append("\",").append(LINE);
        stringBuilder.append(TAB).append("\"useLogHeader\": ").append(useLogHeader).append(",").append(LINE);
        stringBuilder.append(TAB).append("\"logHeader\": ").append("\"").append(formatStr(logHeader)).append("\",").append(LINE);
        stringBuilder.append(TAB).append("\"protocol\": ").append("\"").append(formatStr(protocol)).append("\",").append(LINE);
        stringBuilder.append(TAB).append("\"ip\": ").append("\"").append(formatStr(ip)).append("\",").append(LINE);
        stringBuilder.append(TAB).append("\"port\": ").append(port).append(",").append(LINE);
        stringBuilder.append(TAB).append("\"speed\": ").append(speed).append(",").append(LINE);
        stringBuilder.append(TAB).append("\"total\": ").append(total).append(",").append(LINE);
        stringBuilder.append(TAB).append("\"file\": ").append("\"").append(formatStr(file)).append("\",").append(LINE);
        stringBuilder.append(TAB).append("\"apiKey\": ").append("\"").append(formatStr(apiKey)).append("\",").append(LINE);
        stringBuilder.append(TAB).append("\"server\": ").append("\"").append(formatStr(server)).append("\",").append(LINE);
        stringBuilder.append(TAB).append("\"topic\": ").append("\"").append(formatStr(topic)).append("\",").append(LINE);
        stringBuilder.append(TAB).append("\"fileEncoding\": ").append("\"").append(formatStr(fileEncoding)).append("\",").append(LINE);
        stringBuilder.append(TAB).append("\"logEncoding\": ").append("\"").append(formatStr(logEncoding)).append("\",").append(LINE);
        stringBuilder.append(TAB).append("\"locale\": ").append("\"").append(formatStr(locale)).append("\"").append(LINE);
        stringBuilder.append("}").append(LINE);
        return stringBuilder.toString();
    }

}
