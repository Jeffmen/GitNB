package com.example.gitnb.api.retrofit;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;

import com.example.gitnb.model.Token;
import com.example.gitnb.model.User;

public interface SearchInterface {
    //Async
    @GET("/search/users")
    List<User> users(@Header("token") String token, @Query("q") String query, Callback<Token> callback);
    
    @GET("/search/users")
    List<User> users(@Header("token") String token, @Query("q") String query);

}
