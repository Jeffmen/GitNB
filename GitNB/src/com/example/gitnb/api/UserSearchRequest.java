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
import com.example.gitnb.model.User;
import com.alibaba.fastjson.JSON;

public class UserSearchRequest implements WebRequest {

//	https://api.github.com/search/users?q=location:china&page=1
//	https://api.github.com/search/users?q=language:Java+followers:>500&page=1
//	https://api.github.com/search/users?q=language:C+followers:>500&page=1
//  https://api.github.com/search/users?q=jake+language:java&sort=followers&order=desc&page=1
		
    private static final String BASE_URL = "https://api.github.com/search/";
    private HandlerInterface<ArrayList<User>> handler;
    private Condition searchCondition;
    private JsonObjectRequest request;
    private Context mContext;
    
    public class Condition{
        private String key;  
        public void SetKey(String value){
        	key = value;
        }

        private String location;
        public void SetLocation(String value){
        	location = value;
        }

        private String language;
        public void SetLanguage(String value){
        	language = value;
        }

        private int page = 1;  
        public void SetPage(int value){
        	page = value;
        }
        
        //The sort field. Can be followers, repositories, or joined. Default: results are sorted by best match.
        private String sort = "followers";
        public void SetSort(String value){
        	sort = value;
        }

        private String order = "desc";
        public void SetOrder(String value){
        	order = value;
        }

        private boolean refresh = false;
        public void SetRefresh(boolean value){
        	refresh = value;
        }
    }
    
    public UserSearchRequest(Context context){
       mContext = context;
    }
    
    public void SetHandler(HandlerInterface<ArrayList<User>> value){
    	handler = value;
    }
    
    public void SetSearchCondition(Condition value){
    	searchCondition = value;
    }	
    
    private String getUrl(){
    	String query = "users?q=";
		if(searchCondition.key != null && !searchCondition.key.isEmpty())
		{
			query += searchCondition.key;
		}
		if(searchCondition.location != null && !searchCondition.location.isEmpty())
		{
			query += "+location:" + searchCondition.location;
		}
		if(searchCondition.language != null && !searchCondition.language.isEmpty())
		{
			query += "+language:" + searchCondition.language;
		}
		if(searchCondition.sort != null && !searchCondition.sort.isEmpty())
		{
			query += "&sort=" + searchCondition.sort;
		}
		if(searchCondition.order != null && searchCondition.order.isEmpty())
		{
			query += "&order=" + searchCondition.order;
		}
		if(searchCondition.page > 0)
		{
			query += "&page=" + searchCondition.page;
		}
		Log.i("user_request", "query="+query);
		return query;
    }
    
    
	@Override
	public JsonObjectRequest getRequest() {
		
        if (!searchCondition.refresh || !RequestManager.isNetworkAvailable(mContext)) {
            ArrayList<User> topics = PersistenceHelper.loadModelList(mContext, getUrl());            
            //if (topics != null && topics.size() > 0) {
            	DefaulHandlerImp.onSuccess(handler, topics);
            //}
            return null;
        }
		try {
			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(BASE_URL + getUrl().replace("/", "."), null,  
			        new Response.Listener<JSONObject>() {  
			            @Override  
			            public void onResponse(JSONObject response) {  
			            	ArrayList<User> data = null;
							try {
								data = (ArrayList<User>) JSON.parseArray(response.getString("items"), User.class);
							} catch (Exception e) {
								DefaulHandlerImp.onFailure(handler, e.getMessage());
							}
					        PersistenceHelper.saveModelList(mContext, data, getUrl().replace("/", "."));
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
