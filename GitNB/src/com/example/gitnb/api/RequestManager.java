package com.example.gitnb.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gitnb.api.UserSearchRequest.UserCondition;

public class RequestManager {	
	private Context mContext;
	private static RequestManager manager;
	private RequestQueue mQueue;
	
	public interface WebRequest {
		public JsonObjectRequest getJsonObjectRequest();
		public void cancelRequest();
	}
	
	private RequestManager(Context context) {
		mContext = context;
		mQueue = Volley.newRequestQueue(mContext);
	}
	
	public static RequestManager getInstance(Context context){
		if(manager == null){
			synchronized(RequestManager.class){
				if(manager == null){
					manager = new RequestManager(context);
				}
			}
		}
		return manager;
	}
	 
	public static void initialize(Context context) {
		getInstance(context);
	}
	
	public void addRequest(WebRequest request){
		JsonObjectRequest jsonObjectRequest = request.getJsonObjectRequest();
		if(jsonObjectRequest != null){
			mQueue.add(jsonObjectRequest);
		}
	}
	
	public void getHotUsers(UserCondition searchCondition){
		UserSearchRequest request = new UserSearchRequest(mContext);
		request.SetSearchCondition(searchCondition);
	}
	
	public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
