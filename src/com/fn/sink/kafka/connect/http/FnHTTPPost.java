package com.fn.sink.kafka.connect.http;

import static com.fn.sink.kafka.connect.http.FnClientCommon.loadPrivateKey;
import java.security.PrivateKey;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;

public class FnHTTPPost {

    private String apiKey = null;
    private String privateKeyFilename = null;

    public FnHTTPPost(String tenancyOCID, String usrOCID, String publicKeyFingerprint, String privateKeyFilename) throws Exception {

        this.privateKeyFilename = privateKeyFilename;

        this.apiKey = (tenancyOCID + "/"
                + usrOCID + "/"
                + publicKeyFingerprint);

    }

    public <T> T invoke(String endpoint, HttpEntity payload, ResponseHandler<? extends T> rh) {
        PrivateKey privateKey = loadPrivateKey(privateKeyFilename);
        FnClientCommon.RequestSigner signer = new FnClientCommon.RequestSigner(apiKey, privateKey);

        HttpRequestBase request = new HttpPost(endpoint);
        ((HttpPost) request).setEntity(payload);
        signer.signRequest(request);

        HttpRequestInterceptor interceptor = (hr, hc) -> {
            hr.removeHeaders(HTTP.CONTENT_LEN);
        };

        HttpClient client = HttpClients.custom()
                .addInterceptorFirst(interceptor)
                .build();

        T result = null;
        try {
            result = client.execute(request, rh);
        } catch (Exception ex) {
            Logger.getLogger(FnHTTPPost.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            HttpClientUtils.closeQuietly(client);
        }
        return result;
    }

    public <T> T invoke(String endpoint, String payload, ResponseHandler<? extends T> rh) throws Exception {
        return invoke(endpoint, new StringEntity(payload), rh);
    }

    public String invoke(String endpoint, String payload) throws Exception {
        return invoke(endpoint, new StringEntity(payload), new BasicResponseHandler());
    }

}