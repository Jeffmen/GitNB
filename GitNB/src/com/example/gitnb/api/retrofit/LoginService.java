package com.example.gitnb.api.retrofit;

import com.example.gitnb.model.RequestTokenDTO;
import com.example.gitnb.model.Token;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Bernat on 13/07/2014.
 */
public interface LoginService {

    //Async
    @POST("login/oauth/access_token")
    void requestToken(@Body RequestTokenDTO requestTokenDTO, Callback<Token> callback);

    //Sync
    Token requestToken(@Body RequestTokenDTO requestTokenDTO);
}
