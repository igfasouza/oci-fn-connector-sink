package com.fn.sink.kafka.connect.http;

import static com.fn.sink.kafka.connect.http.FnClientCommon.loadPrivateKey;
import java.io.IOException;

import java.security.PrivateKey;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;

public class FnHTTPGet {

    private String apiKey = null;
    private String privateKeyFilename = null;

    public FnHTTPGet(String tenancyOCID, String usrOCID, String publicKeyFingerprint, String privateKeyFilename) throws Exception {

        this.privateKeyFilename = privateKeyFilename;

        this.apiKey = (tenancyOCID + "/"
                + usrOCID + "/"
                + publicKeyFingerprint);

    }

    public <T> T invoke(String endpoint, ResponseHandler<? extends T> rh) {
        PrivateKey privateKey = loadPrivateKey(privateKeyFilename);
        FnClientCommon.RequestSigner signer = new FnClientCommon.RequestSigner(apiKey, privateKey);

        HttpRequestBase request = new HttpGet(endpoint);
        signer.signRequest(request);

        HttpClient client = HttpClients.createDefault();
        T result = null;
        try {
            result = client.execute(request, rh);
        } catch (IOException ex) {
            Logger.getLogger(FnHTTPGet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            HttpClientUtils.closeQuietly(client);
        }
        return result;
    }

    public String invoke(String endpoint) throws Exception {
        return invoke(endpoint, new BasicResponseHandler());
    }

}