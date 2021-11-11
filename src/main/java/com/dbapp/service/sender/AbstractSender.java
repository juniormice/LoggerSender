package com.dbapp.service.sender;

import java.nio.charset.Charset;
import java.util.List;

public abstract class AbstractSender implements Sender {

    @Override
    public void send(List<byte[]> msg) {
    }

    @Override
    public void send(List<String> msg, Charset logEncoding) {
    }
}
