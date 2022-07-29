package com.dbapp.bean;

import java.nio.charset.Charset;

public class EffectiveConfig {
    private final String ip;
    private final int port;
    private final String apiKey;
    private final String server;
    private final String topic;
    private final Charset logEncoding;
    private final boolean ssl;

    public EffectiveConfig() {
        this.ip = SenderConfig.getIp();
        this.port = SenderConfig.getPort();
        this.apiKey = SenderConfig.getApiKey();
        this.server = SenderConfig.getServer();
        this.topic = SenderConfig.getTopic();
        this.logEncoding = SenderConfig.getLogEncoding();
        this.ssl = SenderConfig.isSsl();
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getServer() {
        return server;
    }

    public String getTopic() {
        return topic;
    }

    public Charset getLogEncoding() {
        return logEncoding;
    }

    public boolean isSsl() {
        return ssl;
    }
}
