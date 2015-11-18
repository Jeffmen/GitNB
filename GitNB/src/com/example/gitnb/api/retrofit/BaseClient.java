package com.example.gitnb.api.retrofit;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import android.util.Pair;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.Response;
import retrofit.converter.Converter;

public abstract class BaseClient<K> implements RequestInterceptor, RestAdapter.Log {

    protected StoreCredentials storeCredentials;

    protected Context context;
    private ApiClient client;

    public BaseClient(Context context, ApiClient client) {
        this.client = client;
        if (context != null) {
            this.context = context.getApplicationContext();
        }
        storeCredentials = new StoreCredentials(context);
    }

    protected RestAdapter getRestAdapter() {
        RestAdapter.Builder restAdapterBuilder =
            new RestAdapter.Builder().setEndpoint(client.getApiEndpoint())
                .setRequestInterceptor(this)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(this);

        if (customConverter() != null) {
            restAdapterBuilder.setConverter(customConverter());
        }

        if (getInterceptor() != null) {
            restAdapterBuilder.setClient(getInterceptor());
        }

        return restAdapterBuilder.build();
    }

    @Nullable
    protected Client getInterceptor() {
        return null;
    }

    public Observable<K> observable() {
        return getApiObservable(getRestAdapter());
    }

    protected abstract Observable<K> getApiObservable(RestAdapter restAdapter);

    protected Converter customConverter() {
        return null;
    }

    protected String getToken() {
        return storeCredentials.token();
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
}
