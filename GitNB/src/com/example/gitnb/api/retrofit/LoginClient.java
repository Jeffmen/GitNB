package com.example.gitnb.api.retrofit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.example.gitnb.model.RequestTokenDTO;
import com.example.gitnb.model.Token;
import com.example.gitnb.api.retrofit.UserInterface;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import retrofit.Retrofit;
import retrofit.Callback;

public class LoginClient{

    private static final String BASE_URL = "https://github.com/";
    static final int CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s
    static final int READ_TIMEOUT_MILLIS = 20 * 1000; // 20s

	public static String getAcceptHeader() {
		return "application/vnd.github.v3.json";
	}
	
	private static String getToken(){
		return "8e8b316209579af22f74fea7bf2e7369068bd518";
	}
	
    public static OkHttpClient getOkClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                		.header("Accept", getAcceptHeader())
        		        .addHeader("User-Agent", "Gitskarios")
        		        .addHeader("Authorization", "token " + getToken())
                        .build();
                
                return chain.proceed(request);
            }
        });
        return client;
    }
    
	public void requestTokenAsync(Callback<Token> callback){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkClient()).build();
        UserInterface loginService = retrofit.create(UserInterface.class);
        RequestTokenDTO tokenDTO = new RequestTokenDTO();
        tokenDTO.client_id = "";
        tokenDTO.client_secret = "";
        tokenDTO.redirect_uri = "";
        tokenDTO.code = "";

        loginService.requestToken(tokenDTO, callback);
	}
	
	public Token requestTokenSync(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).build();
        UserInterface loginService = retrofit.create(UserInterface.class);
        RequestTokenDTO tokenDTO = new RequestTokenDTO();
        tokenDTO.client_id = "";
        tokenDTO.client_secret = "";
        tokenDTO.redirect_uri = "";
        tokenDTO.code = "";

        return loginService.requestToken(tokenDTO);
	}
}
