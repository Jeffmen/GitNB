package com.example.gitnb.api;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.gitnb.api.RequestManager.WebRequest;
import com.example.gitnb.model.PersistenceHelper;
import com.example.gitnb.model.Repository;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

public class TrendingReposRequest implements WebRequest {

	//http://trending.codehub-app.com/v2/trending?language=c&since=weekly
		
    private static final String BASE_URL = "http://trending.codehub-app.com/v2/";
    private HandlerInterface<ArrayList<Repository>> handler;
    private Condition searchCondition;
    private JsonObjectRequest request;
    private Context mContext;
    
    public class Condition{
    	//daily,weekly,monthly
        private String since = "weekly";  
        public void SetSince(String value){
        	since = value;
        }

        private String language;
        public void SetLanguage(String value){
        	language = value;
        }
        
        private boolean refresh = false;
        public void SetRefresh(boolean value){
        	refresh = value;
        }
    }
    
    public TrendingReposRequest(Context context){
       mContext = context;
    }
    
    public void SetHandler(HandlerInterface<ArrayList<Repository>> value){
    	handler = value;
    }
    
    public void SetSearchCondition(Condition value){
    	searchCondition = value;
    }	
    
    private String getUrl(){
    	String query = "trending?";
		if(searchCondition.language != null && !searchCondition.language.isEmpty())
		{
			query += "language=" + searchCondition.language;
		}
		if(searchCondition.since != null && !searchCondition.since.isEmpty())
		{
			query += "&since=" + searchCondition.since;
		}
		Log.i("user_request", "query="+query);
		return query;
    }
    
    
    @Override
	public StringRequest getRequest() {
		if(searchCondition.since == null || searchCondition.since.isEmpty())
		{
			return null;
		}
        if (!searchCondition.refresh || !RequestManager.isNetworkAvailable(mContext)) {
            ArrayList<Repository> topics = PersistenceHelper.loadModelList(mContext, getUrl().replace("/", "."));            
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
				        PersistenceHelper.saveModelList(mContext, data, getUrl().replace("/", "."));
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
