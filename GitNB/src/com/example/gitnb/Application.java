package com.example.gitnb;

import com.example.gitnb.api.FakeX509TrustManager;
import com.example.gitnb.api.RequestManager;
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
    }
    
    public static Context getContext() {
        return mContext;
    }
}