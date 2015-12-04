package com.example.gitnb.module.user;

import com.example.gitnb.R;
import com.example.gitnb.api.retrofit.RetrofitNetworkAbs;
import com.example.gitnb.api.retrofit.UsersClient;
import com.example.gitnb.app.BaseActivity;
import com.example.gitnb.model.User;
import com.example.gitnb.module.repos.ReposListActivity;
import com.example.gitnb.utils.MessageUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class UserDetailActivity extends BaseActivity{

	private String TAG = "UserDetailActivity";
	public static String AVATAR_URL = "avatar_url";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout main;
    private Switch swithBt;
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
        user = (User) intent.getParcelableExtra(HotUserFragment.USER);
        setContentView(R.layout.activity_user_detail);
        main = (LinearLayout) findViewById(R.id.main);
        main.setVisibility(View.GONE);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(
        		android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        	
            @Override
            public void onRefresh() {
                refreshHandler.sendEmptyMessage(START_UPDATE);
            }
            
        });        
        swithBt = (Switch) findViewById(R.id.switch_bt); 
    }
	
    private void setSwitchClicker(){
        swithBt.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        	   if(isChecked){
        		   followUser();
        	   }
        	   else{
        		   unfollowUser();
        	   }
            }
         
         });
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
		
		user_avatar.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UserDetailActivity.this, ImageShowerActivity.class);
				intent.putExtra(UserDetailActivity.AVATAR_URL, user.getAvatar_url());
				startActivity(intent);
			}
        	
        });
		
    	TextView events = (TextView) findViewById(R.id.events);
    	TextView organizations = (TextView) findViewById(R.id.organizations);
    	TextView followers = (TextView) findViewById(R.id.followers);
    	TextView following = (TextView) findViewById(R.id.following);
    	TextView repositorys = (TextView) findViewById(R.id.repositorys);
    	
    	events.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
    	organizations.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
    	followers.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserDetailActivity.this, UserListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(HotUserFragment.USER, user);
				intent.putExtras(bundle);
				intent.putExtra(UserListActivity.USER_TYPE, UserListActivity.USER_TYPE_FOLLOWER);
				startActivity(intent);
			}
		});
    	following.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserDetailActivity.this, UserListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(HotUserFragment.USER, user);
				intent.putExtras(bundle);
				intent.putExtra(UserListActivity.USER_TYPE, UserListActivity.USER_TYPE_FOLLOWING);
				startActivity(intent);
			}
		});
    	repositorys.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserDetailActivity.this, ReposListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(HotUserFragment.USER, user);
				intent.putExtras(bundle);
				intent.putExtra(ReposListActivity.REPOS_TYPE, ReposListActivity.REPOS_TYPE_USER);
				startActivity(intent);
			}
		});
    }
    
	@Override
    protected void startRefresh(){
        mSwipeRefreshLayout.setRefreshing(true);
        getSingleUser();
        checkFollowing();
    }

	@Override
    protected void endRefresh(){
        setUserInfo();
        main.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

	@Override
    protected void endError(){
        mSwipeRefreshLayout.setRefreshing(false);
    }
    
	private void getSingleUser(){
    	UsersClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
				user = (User) ts;
		        refreshHandler.sendEmptyMessage(END_UPDATE);
			}

			@Override
			public void onError(String Message) {
				MessageUtils.showErrorMessage(UserDetailActivity.this, Message);
		        refreshHandler.sendEmptyMessage(END_ERROR);
			}
			
    	}).getSingleUser(user.getLogin());
	} 
	
	private void checkFollowing(){
    	UsersClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
		        swithBt.setVisibility(View.VISIBLE);
				swithBt.setChecked(true);
				setSwitchClicker();
			}

			@Override
			public void onError(String Message) {
		        swithBt.setVisibility(View.VISIBLE);
				setSwitchClicker();
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
