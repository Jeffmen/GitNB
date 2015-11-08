package com.example.gitnb.api;

import java.util.ArrayList;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.gitnb.api.RequestManager.WebRequest;
import com.example.gitnb.model.User;
import com.example.gitnb.model.PersistenceHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

public class ReposContributorsRequest implements WebRequest {
    //https://api.github.com/repos/Trinea/android-auto-scroll-view-pager/contributors
    private static final String BASE_URL = "https://api.github.com/repos/";
    private HandlerInterface<ArrayList<User>> handler;
    private Condition searchCondition;
    private JsonObjectRequest request;
    private Context mContext;
    
    public class Condition{
        private String login;  
        public void SetLogin(String value){
        	login = value;
        }
        
        private String reposName;  
        public void SetReposName(String value){
        	reposName = value;
        }
        
        private boolean refresh = false;
        public void SetRefresh(boolean value){
        	refresh = value;
        }
    }
    
    public ReposContributorsRequest(Context context){
       mContext = context;
    }
    
    public void SetHandler(HandlerInterface<ArrayList<User>> value){
    	handler = value;
    }
    
    public void SetSearchCondition(Condition value){
    	searchCondition = value;
    }	
    
    private String getUrl(){
		return searchCondition.login+"/"+searchCondition.reposName+"/contributors";
    }
    
    
	@Override
	public StringRequest getRequest() {
		if(searchCondition.login == null || searchCondition.login.isEmpty()
				|| searchCondition.reposName == null || searchCondition.reposName.isEmpty())
		{
			return null;
		}
        if (!searchCondition.refresh || !RequestManager.isNetworkAvailable(mContext)) {
            ArrayList<User> topics = PersistenceHelper.loadModelList(mContext, getUrl());            
            //if (topics != null && topics.size() > 0) {
            	DefaulHandlerImp.onSuccess(handler, topics);
            //}
            return null;
        }
		try {
	        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + getUrl(), new Response.Listener<String>() {  
	           @Override  
	           public void onResponse(String response) {  
	               try {  
		            	ArrayList<User> data = null;
						try {
							data = (ArrayList<User>) JSON.parseArray(response, User.class);
						} catch (Exception e) {
							DefaulHandlerImp.onFailure(handler, e.getMessage());
						}
				        PersistenceHelper.saveModelList(mContext, data, getUrl());
		            	DefaulHandlerImp.onSuccess(handler, data);
	               } catch (JSONException e) {  
	                   e.printStackTrace();  
	               }  
	           }  
	        }, new Response.ErrorListener() {  
	           @Override  
	           public void onErrorResponse(VolleyError error) {  
					DefaulHandlerImp.onFailure(handler, error.getMessage());
	           }  
	        } 
	        );  
			return stringRequest;
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
