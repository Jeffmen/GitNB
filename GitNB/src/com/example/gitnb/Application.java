package com.example.gitnb;

import android.content.Context;

public class Application extends android.app.Application {

    private static Application mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }
    
    public static Context getContext() {
        return mContext;
    }
}