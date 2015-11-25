package com.example.gitnb.module.user;

import com.example.gitnb.R;
import com.example.gitnb.api.retrofit.RetrofitNetworkAbs;
import com.example.gitnb.api.retrofit.UsersClient;
import com.example.gitnb.app.BaseActivity;
import com.example.gitnb.model.User;
import com.example.gitnb.utils.MessageUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

public class UserDetailActivity extends BaseActivity{

	private String TAG = "UserDetailActivity";
	public static String AVATAR_URL = "avatar_url";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MaterialAnimatedSwitch swithBt;
	private boolean isFirst = true;
	private User user;
	
    protected void setTitle(TextView view){
        if(user != null && !user.getLogin().isEmpty()){
        	view.setText(user.getLogin());
        }else{
        	view.setText("NULL");
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = (User) intent.getParcelableExtra(HotUserFragment.USER_KEY);
        setContentView(R.layout.activity_user_detail);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(
        		android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        	
            @Override
            public void onRefresh() {
            	getSingleUser();
            }
            
        });        
        swithBt = (MaterialAnimatedSwitch) findViewById(R.id.switch_bt);  
        swithBt.setVisibility(View.VISIBLE);
        swithBt.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
        
           @Override 
           public void onCheckedChanged(boolean isChecked) {
        	   if(!isFirst){
	        	   if(isChecked){
	        		   followUser();
	        	   }
	        	   else{
	        		   unfollowUser();
	        	   }
        	   }
           }
        
        });
        getSingleUser();
        checkFollowing();
        mSwipeRefreshLayout.setRefreshing(true);
    }
	
    private void setUserInfo(){
		TextView user_name = (TextView) findViewById(R.id.user_name);
		TextView user_company = (TextView) findViewById(R.id.user_company);
		TextView user_location = (TextView) findViewById(R.id.user_location);
		TextView user_created_date = (TextView) findViewById(R.id.user_created_date);
		TextView user_blog = (TextView) findViewById(R.id.user_blog);
		TextView user_email = (TextView) findViewById(R.id.user_email);
		SimpleDraweeView user_avatar = (SimpleDraweeView) findViewById(R.id.user_avatar);
		
		if(user!=null){
			user_name.setText(user.getName());
			user_location.setText(user.getLocation());
			String date = user.getCreated_at();
			if(date != null && !date.isEmpty()){
				date = date.substring(0, date.indexOf('T'));
			}
			user_avatar.setImageURI(Uri.parse(user.getAvatar_url()));
			user_email.setText(user.getEmail());
			user_created_date.setText(date);
			user_blog.setText(user.getBlog());
			user_company.setText(user.getCompany());
			if(user.getCompany() == null || user.getCompany().isEmpty()){
				user_company.setVisibility(View.VISIBLE);
			}
		}
    }
	private void getSingleUser(){
    	UsersClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
				user = (User) ts;
                setUserInfo();
                mSwipeRefreshLayout.setRefreshing(false);
			}

			@Override
			public void onError(String Message) {
				MessageUtils.showErrorMessage(UserDetailActivity.this, Message);
			}
			
    	}).getSingleUser(user.getLogin());
	} 
	
	private void checkFollowing(){
    	UsersClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
				swithBt.toggle();
				isFirst = false;
			}

			@Override
			public void onError(String Message) {
				isFirst = false;
			}
			
    	}).checkFollowing(user.getLogin());
	}
	
	private void followUser(){
    	UsersClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
				Snackbar.make(mSwipeRefreshLayout, "Already Followed", Snackbar.LENGTH_LONG).show();
			}

			@Override
			public void onError(String Message) {
				MessageUtils.showErrorMessage(UserDetailActivity.this, Message);
			}
			
    	}).followUser(user.getLogin());
	}	
	
	private void unfollowUser(){
    	UsersClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
				Snackbar.make(mSwipeRefreshLayout, "Already unFollowed", Snackbar.LENGTH_LONG).show();
			}

			@Override
			public void onError(String Message) {
				MessageUtils.showErrorMessage(UserDetailActivity.this, Message);
			}
			
    	}).unfollowUser(user.getLogin());
	}
    
}
