package com.example.gitnb.api.retrofit;

import java.io.IOException;

import android.content.Context;

import com.example.gitnb.model.RequestTokenDTO;
import com.example.gitnb.model.Token;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

public class LoginClient extends RetrofitNetworkAbs{
	private static String CLIENT_ID = "a4220ecd856ed8c01689";
	private static String CLIENT_SECRET = "32d70cc4f19255a98c015c42437a9bef8017593f";
	private static String REDIRECT_URI = "https://github.com/Jeffmen/GitNB";
	

	public LoginClient(Context context) {
		super();
	}
    
	public void requestTokenAsync(Callback<Token> callback){

        LoginService loginService = ApiRetrofit.getRetrofit().create(LoginService.class);
        RequestTokenDTO tokenDTO = new RequestTokenDTO();
        tokenDTO.client_id = "";
        tokenDTO.client_secret = "";
        tokenDTO.redirect_uri = "";
        tokenDTO.code = "";
        loginService.requestToken(tokenDTO).enqueue(new Callback<Token>() {

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }

			@Override
			public void onResponse(retrofit.Response<Token> response, Retrofit retrofit) {
                myOnResponse(response);
			}
        });

	}
	
	public retrofit.Response<Token> requestTokenSync() throws IOException{
		LoginService loginService = ApiRetrofit.getRetrofit().create(LoginService.class);
        RequestTokenDTO tokenDTO = new RequestTokenDTO();
        tokenDTO.client_id = CLIENT_ID;
        tokenDTO.client_secret = CLIENT_SECRET;
        tokenDTO.redirect_uri = REDIRECT_URI;
        tokenDTO.code = "";

        Call<Token> call = loginService.requestToken(tokenDTO);
        return call.execute();
	}


	@Override
	public LoginClient setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
	}
}
