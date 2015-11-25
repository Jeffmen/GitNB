package com.example.gitnb.api.retrofit;

import java.io.IOException;
import com.example.gitnb.api.retrofit.RetrofitNetworkAbs.NetworkListener;
import com.example.gitnb.model.Content;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;


public class OKHttpClient{
	private static String TAG = "OKHttpClient";
    protected NetworkListener networkListener;
    private Gson gson = new Gson();
	
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
			public void onFailure(Request request, IOException exception) {
		        if (networkListener != null) {
		            networkListener.onError(exception.getMessage());
		        }
			}

			@Override
			public void onResponse(com.squareup.okhttp.Response response)
					throws IOException {
		        if (response.isSuccessful()) {
		            if (networkListener != null) {	
		            	String reponse = response.body().string();
		            	Object o = gson.fromJson(reponse, Content.class);
		                networkListener.onOK(o);
		            }
		        } else {
		            String mess = response.message();
		            if (networkListener != null) {
		                networkListener.onError(mess);
		            }
		        }
			}
		});
	}
    
	public OKHttpClient setNetworkListener(NetworkListener networkListener) {
        this.networkListener = networkListener;
        return this;
	}
}