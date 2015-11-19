package com.example.gitnb.api.retrofit;

import java.io.IOException;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.content.Context;

public class OauthUrlRetrofit extends BaseRetrofit{

    public static String API_OAUTH_URL = "https://github.com/";
    
    public OauthUrlRetrofit() {
    }

	@Override
	protected String getBaseUrl() {
		return API_OAUTH_URL;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
        		.header("Accept", "application/json")
                .build();
        return chain.proceed(request);
	}
}
