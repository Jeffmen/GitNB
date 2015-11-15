package com.example.gitnb.api.retrofit;

import com.example.gitnb.model.RequestTokenDTO;
import com.example.gitnb.model.Token;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface LoginInterface {
    //Async
    @POST("/login/oauth/access_token")
    void requestToken(@Body RequestTokenDTO requestTokenDTO, Callback<Token> callback);

    //Sync
    Token requestToken(@Body RequestTokenDTO requestTokenDTO);
}
