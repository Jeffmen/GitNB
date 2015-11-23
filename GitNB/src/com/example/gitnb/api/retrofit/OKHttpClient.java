package com.example.gitnb.api.retrofit;

import java.io.IOException;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import retrofit.Call;
import retrofit.Retrofit;
import retrofit.Response;


public class OKHttpClient extends RetrofitNetworkAbs{
	
	private OKHttpClient(){
	}
	
    public static OKHttpClient getNewInstance() {
        return new OKHttpClient();
    }	
	
    public void request(String url){
    	final Request request = new Request.Builder().url(url).build();

    	ApiRetrofit.getRetrofit().client().newCall(request)
    	.enqueue(new Callback() {

			@Override
			public void onFailure(Request arg0, IOException arg1) {
				
			}

			@Override
			public void onResponse(com.squareup.okhttp.Response arg0)
					throws IOException {
				
			}
		});
	}
    
	@Override
	public OKHttpClient setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
	}

}
