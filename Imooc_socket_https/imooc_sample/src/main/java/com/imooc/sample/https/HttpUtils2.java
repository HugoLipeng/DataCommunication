package com.imooc.sample.https;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;



public class HttpUtils2 {

    public interface HttpListener {
        void onSuccess(String content);

        void onFail(Exception ex);
    }

    private static Handler mUIHandler = new Handler(Looper.getMainLooper());

    public static void doGet(final Context context, final String urlStr, final HttpListener listener) {
        new Thread() {
            @Override
            public void run() {

                InputStream is = null;
                try {
                    URL url = new URL(urlStr);

                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                    X509Certificate serverCert = getCert(context);

                    String keyStoreType = KeyStore.getDefaultType();
                    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                    keyStore.load(null);
                    keyStore.setCertificateEntry("srca",serverCert);

                    TrustManagerFactory trustManagerFactory =
                            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    trustManagerFactory.init(keyStore);

                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                    sslContext.init(null, trustManagers, new SecureRandom());
                    conn.setSSLSocketFactory(sslContext.getSocketFactory());

                    conn.setHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            HostnameVerifier defaultHostnameVerifier
                                    = HttpsURLConnection.getDefaultHostnameVerifier();
                            return defaultHostnameVerifier.verify("kyfw.12306.cn", session);
                        }
                    });

                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    conn.connect();

                    is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    char[] buf = new char[2048];
                    int len = -1;
                    final StringBuilder content = new StringBuilder();
                    while ((len = isr.read(buf)) != -1) {
                        content.append(new String(buf, 0, len));
                    }

                    mUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(content.toString());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFail(e);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }
        }.start();
    }

    private static X509Certificate getCert(Context context) {

        try {
            InputStream is = context.getAssets().open("srca.cer");
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) certificateFactory.generateCertificate(is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }

        return null;
    }

}
