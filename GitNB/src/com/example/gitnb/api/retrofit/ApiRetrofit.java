package com.example.gitnb.api.retrofit;

import java.io.IOException;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.content.Context;

public class ApiRetrofit extends BaseRetrofit{
//https://api.github.com/user?access_token=51ee597f1442fdc47df77121d05b343cc249a74e
    public static String API_URL = "https://api.github.com/";
    private static ApiRetrofit me;
    

    public ApiRetrofit() {
        super();
    }
    
	public static ApiRetrofit getInstance(){
		if(me == null){
			synchronized(BaseRetrofit.class){
				if(me == null){
					me = new ApiRetrofit();
				}
			}
		}
		return me;
	}
	
    protected static String getToken() {
		return "2f2cf9515d82dcd04f08ab330864f72e702a55f7";
    }

	@Override
	protected String getBaseUrl() {
		return API_URL;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
        		.header("Accept", "application/vnd.github.v3+json")
		        .addHeader("User-Agent", "GtiNB")
		        .addHeader("Authorization", "token " + getToken())
                .build();
        return chain.proceed(request);
	}
}
