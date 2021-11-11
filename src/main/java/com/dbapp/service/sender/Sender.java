package com.dbapp.service.sender;

import com.dbapp.bean.EffectiveConfig;
import com.dbapp.bean.aes.AesEncryption;

import java.nio.charset.Charset;
import java.util.List;

public interface Sender {

    String getName();

    void init(EffectiveConfig config) throws Exception;

    void send(List<byte[]> msg);

    void send(List<String> msg, Charset logEncoding);

    void stop();
}
