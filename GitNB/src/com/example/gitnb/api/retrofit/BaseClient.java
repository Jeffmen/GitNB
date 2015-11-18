package com.example.gitnb.api.retrofit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Interceptor.Chain;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import android.util.Pair;

import retrofit.Callback;
import retrofit.Retrofit;

public abstract class BaseClient implements Interceptor{

    private static final String BASE_URL = "https://github.com/";
    static final int CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s
    static final int READ_TIMEOUT_MILLIS = 20 * 1000; // 20s
    protected StoreCredentials storeCredentials;
    protected Context context;
    private ApiClient client;

    public BaseClient(Context context) {
        this.client = getApiClient(context);
        if (context != null) {
            this.context = context.getApplicationContext();
        }
        storeCredentials = new StoreCredentials(context);
    }

	private static ApiClient getApiClient(Context context) {
		String url = new StoreCredentials(context).getUrl();
		return new GitHub(url);
	}
	
    protected String getToken() {
		return "8e8b316209579af22f74fea7bf2e7369068bd518";
        //return storeCredentials.token();
    }

    public Context getContext() {
        return context;
    }

    public ApiClient getClient() {
        return client;
    }

    public void setStoreCredentials(StoreCredentials storeCredentials) {
        this.storeCredentials = storeCredentials;
    }
    
	public static String getAcceptHeader() {
		return "application/vnd.github.v3.json";
	}
	
    @Override
	public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
        		.header("Accept", getAcceptHeader())
		        .addHeader("User-Agent", "Gitskarios")
		        .addHeader("Authorization", "token " + getToken())
                .build();
        return chain.proceed(request);
    }
    
    public OkHttpClient getOkClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.interceptors().add(this);
        return client;
    }
    
    public Retrofit getRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
         	.baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkClient()).build();
        return retrofit;
    }
}
