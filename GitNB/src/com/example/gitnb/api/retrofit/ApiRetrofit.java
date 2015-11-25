package com.example.gitnb.api.retrofit;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import retrofit.Retrofit;

import com.example.gitnb.api.FakeX509TrustManager;
import com.example.gitnb.api.retrofit.converter.GsonConverterFactory;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.content.Context;

public class ApiRetrofit{
//https://api.github.com/user?access_token=51ee597f1442fdc47df77121d05b343cc249a74e

    static final int CONNECT_TIMEOUT_MILLIS = 30 * 1000;
    static final int READ_TIMEOUT_MILLIS = 30 * 1000;
    public static String API_URL = "https://api.github.com/";
	
    public static OkHttpClient getOkClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        //ignore https certificate validation
        SSLContext context = null;
        TrustManager[] trustManagers = new TrustManager[] { new FakeX509TrustManager() };
        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        client.setSslSocketFactory(context.getSocketFactory());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        client.setHostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
        });
        client.interceptors().add(new Interceptor(){

			@Override
			public Response intercept(Chain chain) throws IOException {
		        Request request = chain.request().newBuilder()
		        		.header("Accept", "application/vnd.github.v3+json")
				        .addHeader("User-Agent", "GtiNB")
				        .addHeader("Authorization", "token " + getToken())
		                .build();
		        return chain.proceed(request);
			}
        });
        return client;
    }
    
    public static Retrofit getRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
         	.baseUrl(getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkClient()).build();
        return retrofit;
    }
    
    protected static String getToken() {
		return "cb094040d2f01ee6fae66fa0b26d781e441cac46";
    }

	public static String getBaseUrl() {
		return API_URL;
	}
}
