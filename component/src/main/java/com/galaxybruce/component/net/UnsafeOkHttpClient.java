package com.galaxybruce.component.net;

import android.annotation.SuppressLint;
import android.os.Build;


import com.galaxybruce.component.util.log.AppLogUtils;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * @date 2021/12/13 15:50
 * @author bruce.zhang
 * @description 不安全的OkHttpClient
 * <p>
 * modification history:
 */
public class UnsafeOkHttpClient {

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        //Redmi K30 Android 10 部分手机会挂机 所以等于30也推荐使用 X509TrustManager
        //sslSocketFactory is class com.android.org.conscrypt.OpenSSLSocketFactoryImpl
        if(Build.VERSION.SDK_INT < 29) {
            return new OkHttpClient.Builder()
                    .sslSocketFactory(createSSLSocketFactory(new TrustAllManager()))
                    .hostnameVerifier(new TrustAllHostnameVerifier());
//                .addInterceptor(new BasicDataInterceptor());
        } else {
            X509TrustManager trustManager = new TrustAllManager();
            return new OkHttpClient.Builder()
                    .sslSocketFactory(createSSLSocketFactory(trustManager), trustManager)
                    .hostnameVerifier(new TrustAllHostnameVerifier());
        }
    }

    /**
     * 默认信任所有的证书
     *
     * @return SSLSocketFactory
     */
    @SuppressLint("TrulyRandom")
    private static SSLSocketFactory createSSLSocketFactory(X509TrustManager trustManager) {
        SSLSocketFactory sSLSocketFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{trustManager},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
            AppLogUtils.e(e.getMessage(), e);
        }
        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}
