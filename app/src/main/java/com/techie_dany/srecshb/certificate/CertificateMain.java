package com.techie_dany.srecshb.certificate;


import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class CertificateMain {
    SSLContext sc = null;
            public CertificateMain(){}


    public HostnameVerifier all_hosted = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    public SSLSocketFactory getMyCertificate() {
        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;}
            public void checkClientTrusted(X509Certificate[] certs, String authTypr) {}
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        }};
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return sc.getSocketFactory();
    }
}