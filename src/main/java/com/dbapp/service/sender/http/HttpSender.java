package com.dbapp.service.sender.http;

import com.alibaba.fastjson.JSON;
import com.dbapp.bean.EffectiveConfig;
import com.dbapp.bean.aes.AesEncryption;
import com.dbapp.service.DisplayService;
import com.dbapp.service.sender.AbstractSender;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.entity.ContentType;

import java.nio.charset.Charset;
import java.util.List;

public class HttpSender extends AbstractSender {

    private HttpClient client;
    private String fullUrl;

    @Override
    public String getName() {
        return "HttpSender";
    }

    @Override
    public void init(EffectiveConfig config) throws Exception {
        this.client = new HttpClient(new MultiThreadedHttpConnectionManager());
        this.client.getHttpConnectionManager().getParams().setConnectionTimeout(10 * 60 * 1000);
        this.fullUrl = "http://" + config.getIp() + ":" + config.getPort() + "/c/externalHttpReceiver2?apiKey=" + config.getApiKey();
    }

    @Override
    public void send(List<String> msg, Charset logEncoding) {
        PostMethod method = new PostMethod(fullUrl);
        try {
            method.setRequestHeader("Content-Type", "application/json");
            method.setRequestHeader("dataType", "unstructured");
            byte[] data = JSON.toJSONString(msg).getBytes(logEncoding);
            ByteArrayRequestEntity entity = new ByteArrayRequestEntity(data, ContentType.APPLICATION_JSON.getMimeType());
            method.setRequestEntity(entity);
            int result = this.client.executeMethod(method);
            if (result != HttpStatus.SC_OK) {
                DisplayService.directPrint("send http failed: " + method.getResponseBodyAsString());
            }
        } catch (Exception e) {
            DisplayService.directPrint("send http failed", e);
        } finally {
            method.releaseConnection();
        }
    }

    @Override
    public void stop() {
        if (client != null) {
            client = null;
        }
    }
}
