package com.example.gitnb.api.retrofit;

import com.example.gitnb.model.RequestTokenDTO;
import com.example.gitnb.model.Token;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Bernat on 13/07/2014.
 */
public interface LoginService {

    @POST("login/oauth/access_token")
    Call<Token> requestToken(@Body RequestTokenDTO requestTokenDTO);

}
