package com.dbapp.service.sender.http;

import com.alibaba.fastjson.JSON;
import com.dbapp.bean.EffectiveConfig;
import com.dbapp.service.DisplayService;
import com.dbapp.service.sender.AbstractSender;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
        this.client = createHttpClient();
        this.fullUrl = (config.isSsl() ? "https" : "http") + "://" + config.getIp() + ":" + config.getPort() + "/c/externalHttpReceiver2?apiKey=" + config.getApiKey();
    }

    private HttpClient createHttpClient() throws Exception {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, ((x509Certificates, s) -> true));
        String protocols = "TLSv1,TLSv1.1,TLSv1.2";
        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(builder.build(), protocols.split(","), null, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", factory).build();
        PoolingHttpClientConnectionManager gcm = new PoolingHttpClientConnectionManager(registry);
        gcm.setMaxTotal(200);
        gcm.setDefaultMaxPerRoute(200);

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10 * 1000).setSocketTimeout(200000 * 1000)
                .setConnectionRequestTimeout(10 * 1000).build();
        return HttpClientBuilder.create().setConnectionManager(gcm).setDefaultRequestConfig(requestConfig).build();
    }

    private void config(HttpRequestBase httpPost) {
        httpPost.setHeader("User-Agent", "Mozilla/5.0");
        httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*,q=0.8");
        httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        httpPost.setHeader("Accept-Charset", "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("dataType", "unstructured");
    }

    @Override
    public void send(List<String> msg, Charset logEncoding) {
        HttpPost httpPost = new HttpPost(this.fullUrl);
        try {
            config(httpPost);
            byte[] data = JSON.toJSONString(msg).getBytes(logEncoding);
            ByteArrayEntity entity = new ByteArrayEntity(data, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            HttpResponse response = this.client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code != HttpStatus.SC_OK && code != HttpStatus.SC_CREATED) {
                DisplayService.directPrint("send http failed: " + EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            DisplayService.directPrint("send http failed", e);
        } finally {
            httpPost.releaseConnection();
        }
    }

    @Override
    public void stop() {
        if (client != null) {
            client = null;
        }
    }
}
