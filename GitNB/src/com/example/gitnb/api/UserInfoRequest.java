package com.example.gitnb.api;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gitnb.api.RequestManager.WebRequest;
import com.example.gitnb.model.PersistenceHelper;
import com.example.gitnb.model.UserInfo;
import com.alibaba.fastjson.JSON;

public class UserInfoRequest implements WebRequest {

    private static final String BASE_URL = "https://api.github.com/users/";
    private HandlerInterface<UserInfo> handler;
    private Condition searchCondition;
    private JsonObjectRequest request;
    private Context mContext;
    
    public class Condition{
        private String login;  
        public void SetLogin(String value){
        	login = value;
        }

        private boolean refresh = false;
        public void SetRefresh(boolean value){
        	refresh = value;
        }
    }
    
    public UserInfoRequest(Context context){
       mContext = context;
    }
    
    public void SetHandler(HandlerInterface<UserInfo> value){
    	handler = value;
    }
    
    public void SetSearchCondition(Condition value){
    	searchCondition = value;
    }	
    
    private String getUrl(){
		return searchCondition.login;
    }
    
    
	@Override
	public JsonObjectRequest getJsonObjectRequest() {
        if (!searchCondition.refresh) {
            UserInfo topics = PersistenceHelper.loadModel(mContext, getUrl());            
            if (topics != null) {
            	DefaulHandlerImp.onSuccess(handler, topics);
                return null;
            }
        }
		try {
			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(BASE_URL + getUrl(), null,  
			        new Response.Listener<JSONObject>() {  
			            @Override  
			            public void onResponse(JSONObject response) {  
			            	UserInfo data = null;
							try {
								data = (UserInfo) JSON.parseObject(response.toString(), UserInfo.class);
							} catch (Exception e) {
								DefaulHandlerImp.onFailure(handler, e.getMessage());
							}
					        PersistenceHelper.saveModel(mContext, data, getUrl());
			            	DefaulHandlerImp.onSuccess(handler, data);
			            }  
			        }, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.i("zy", "startParseXml:thought an Exception");
							DefaulHandlerImp.onFailure(handler, error.getMessage());
						}  
			        });

			return jsonObjectRequest;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void cancelRequest() {
		if(request != null)
		request.cancel();
	}
}
