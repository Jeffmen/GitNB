package com.example.gitnb.api.retrofit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import android.content.Context;

import retrofit.Retrofit;

public abstract class BaseRetrofit implements Interceptor {

    static final int CONNECT_TIMEOUT_MILLIS = 30 * 1000;
    static final int READ_TIMEOUT_MILLIS = 30 * 1000;
    

    public BaseRetrofit() {
    }
	
    protected abstract String getBaseUrl();
    
    public OkHttpClient getOkClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.interceptors().add(this);
        return client;
    }
    
    public Retrofit getRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
         	.baseUrl(getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkClient()).build();
        return retrofit;
    }
    
    @Override
	public abstract Response intercept(Chain chain) throws IOException;
}
