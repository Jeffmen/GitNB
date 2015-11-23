package com.example.gitnb.api.retrofit;

import java.io.IOException;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


public class OKHttpClient extends RetrofitNetworkAbs{
	
	private OKHttpClient(){
	}
	
    public static OKHttpClient getNewInstance() {
        return new OKHttpClient();
    }	
	
    public void request(String url){
    	final Request request = new Request.Builder().url(url).build();
//    	ApiRetrofit.getInstance().getRetrofit().
//    	.newCall(request).enqueue(new Callback() {
//			@Override
//			public void onFailure(final Request request, final IOException e) {
//				
//			}
//
//			@Override
//			public void onResponse(final Response response) {
//
//			}
//		});
	}
    
	@Override
	public OKHttpClient setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
	}

}
