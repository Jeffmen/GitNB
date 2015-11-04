package com.example.gitnb.api;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.gitnb.api.RequestManager.WebRequest;
import com.example.gitnb.model.PersistenceHelper;
import com.example.gitnb.model.Repository;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

public class UserReposRequest implements WebRequest {

//	https://api.github.com/users/peachananr/repos?visibility=public&sort=updated
		
    private static final String BASE_URL = "https://api.github.com/users/";
    private HandlerInterface<ArrayList<Repository>> handler;
    private Condition searchCondition;
    private StringRequest request;
    private Context mContext;
    
    public class Condition{
        private String login;  
        public void SetLogin(String value){
        	login = value;
        }

        private String visibility = "public";
        public void SetVisibility(String value){
        	visibility = value;
        }

        private String sort = "updated";
        public void SetSort(String value){
        	sort = value;
        }

        private String order = "asc";
        public void SetOrder(String value){
        	order = value;
        }

        private boolean refresh = false;
        public void SetRefresh(boolean value){
        	refresh = value;
        }
    }
    
    public UserReposRequest(Context context){
       mContext = context;
    }
    
    public void SetHandler(HandlerInterface<ArrayList<Repository>> value){
    	handler = value;
    }
    
    public void SetSearchCondition(Condition value){
    	searchCondition = value;
    }	
    
    private String getUrl(){
    	String query = searchCondition.login + "/repos?";
		if(searchCondition.visibility != null && !searchCondition.visibility.isEmpty())
		{
			query += "visibility:" + searchCondition.visibility;
		}
		if(searchCondition.sort != null && !searchCondition.sort.isEmpty())
		{
			query += "&sort=" + searchCondition.sort;
		}
		
		if(searchCondition.order != null && searchCondition.order.isEmpty())
		{
			query += "&order=" + searchCondition.order;
		}
		Log.i("user_request", "query="+query);
		return query;
    }
    
    
	@Override
	public StringRequest getJsonObjectRequest() {
		if(searchCondition.login == null || searchCondition.login.isEmpty())
		{
			return null;
		}
        if (!searchCondition.refresh || !RequestManager.isNetworkAvailable(mContext)) {
            ArrayList<Repository> topics = PersistenceHelper.loadModelList(mContext, getUrl());            
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
		            	ArrayList<Repository> data = null;
						try {
							data = (ArrayList<Repository>) JSON.parseArray(response, Repository.class);
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
