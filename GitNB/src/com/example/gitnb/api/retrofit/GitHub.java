package com.example.gitnb.api.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

public class GitHub implements ApiClient { 
    public static String NAME = "Github";
	public static String CLIENT_ID = "a4220ecd856ed8c01689";
	public static String CLIENT_SECRET = "32d70cc4f19255a98c015c42437a9bef8017593f";
	public static String REDIRECT_URI = "https://github.com/Jeffmen/GitNB";
	public static String API_AUTHORIZE_URL = "https://github.com/login/oauth/authorize/";
    public static String API_OAUTH_URL = "https://github.com/";
    public static String API_URL = "https://api.github.com/";
    public static String TOKEN_KEY = "token";
    public static String CODE_KEY = "code";
    public static String STATE = "2015";
    public static String SCOPE = "user,public_repo";
    private Context context;
    private String token;
    private String code;
    private static GitHub me;

    private GitHub(Context context) { 
    	this.context = context;
        SharedPreferences read = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        token = read.getString(TOKEN_KEY, "1f5d67aed88429ae5d655eabba8193fc260c5477");
        code = read.getString(CODE_KEY, "b5a76a4a14ae09c059fa");
    }
    
	public static GitHub getInstance(){
		return me;
	}
	 
	public static void initialize(Context context) {
		me = new GitHub(context);
	}
    
    @Override
    public String getApiOauthUrlEndpoint() {
        return API_OAUTH_URL;
    }

    @Override
    public String getApiEndpoint() {
        return API_URL;
    }

    @Override
    public String getType() {
        return "github";
    }

    public String getCode() {
		return code;
    }
    
    public void setCode(String value){
    	code = value;
        SharedPreferences.Editor editor = context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putString(CODE_KEY, value);
        editor.commit();
    }
    
    public String getToken() {
		return token;
    }
    
    public void setToken(String value){
    	token = value;
        SharedPreferences.Editor editor = context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putString(TOKEN_KEY, value);
        editor.commit();
    }
}
