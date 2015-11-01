package com.example.gitnb.api;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gitnb.api.RequestManager.WebRequest;
import com.example.gitnb.model.PersistenceHelper;
import com.example.gitnb.model.HotUser;
import com.alibaba.fastjson.JSON;

public class UserRequest implements WebRequest {

    private static final String BASE_URL = "https://api.github.com/users/";
    private HandlerInterface<ArrayList<HotUser>> handler;
    private UserCondition searchCondition;
    private JsonObjectRequest request;
    private Context mContext;
    
    public class UserCondition{
        private String login;  
        public void SetLogin(String value){
        	login = value;
        }

        private boolean refresh = false;
        public void SetRefresh(boolean value){
        	refresh = value;
        }
    }
    
    public UserRequest(Context context){
       mContext = context;
    }
    
    public void SetHandler(HandlerInterface<ArrayList<HotUser>> value){
    	handler = value;
    }
    
    public void SetSearchCondition(UserCondition value){
    	searchCondition = value;
    }	
    
    private String getUrl(){
		return searchCondition.login;
    }
    
    
	@Override
	public JsonObjectRequest getJsonObjectRequest() {
        if (!searchCondition.refresh) {
            ArrayList<HotUser> topics = PersistenceHelper.loadModelList(mContext, getUrl());            
            if (topics != null && topics.size() > 0) {
            	DefaulHandlerImp.onSuccess(handler, topics);
                return null;
            }
        }
		try {
			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(BASE_URL + getUrl(), null,  
			        new Response.Listener<JSONObject>() {  
			            @Override  
			            public void onResponse(JSONObject response) {  
			            	ArrayList<HotUser> data = null;
							try {
								data = (ArrayList<HotUser>) JSON.parseArray(response.toString(), HotUser.class);
							} catch (Exception e) {
								DefaulHandlerImp.onFailure(handler, e.getMessage());
							}
					        PersistenceHelper.saveModelList(mContext, data, getUrl());
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
