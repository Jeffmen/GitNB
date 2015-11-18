package com.example.gitnb.api.retrofit;

import android.content.Context;

import com.example.gitnb.model.RequestTokenDTO;
import com.example.gitnb.model.Token;
import com.example.gitnb.api.retrofit.UserInterface;

import retrofit.Callback;

public class LoginClient extends BaseClient{

    
	public LoginClient(Context context) {
		super(context);
	}

	public void requestTokenAsync(Callback<Token> callback){

        UserInterface loginService = getRetrofit().create(UserInterface.class);
        RequestTokenDTO tokenDTO = new RequestTokenDTO();
        tokenDTO.client_id = "";
        tokenDTO.client_secret = "";
        tokenDTO.redirect_uri = "";
        tokenDTO.code = "";

        loginService.requestToken(tokenDTO, callback);
	}
	
	public Token requestTokenSync(){
        UserInterface loginService = getRetrofit().create(UserInterface.class);
        RequestTokenDTO tokenDTO = new RequestTokenDTO();
        tokenDTO.client_id = "";
        tokenDTO.client_secret = "";
        tokenDTO.redirect_uri = "";
        tokenDTO.code = "";

        return loginService.requestToken(tokenDTO);
	}
}
