package com.example.gitnb.app;

import com.example.gitnb.api.FakeX509TrustManager;
import com.example.gitnb.api.RequestManager;
import com.example.gitnb.api.retrofit.GitHub;
import com.facebook.drawee.backends.pipeline.Fresco;

import android.content.Context;

public class Application extends android.app.Application {

    private static Application mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        FakeX509TrustManager.allowAllSSL();
        Fresco.initialize(mContext);
        RequestManager.initialize(mContext);
        GitHub.initialize(mContext);
    }
    
    public static Context getContext() {
        return mContext;
    }
}