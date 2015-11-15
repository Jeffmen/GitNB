package com.example.gitnb.module.user;

import java.util.ArrayList;

import com.example.gitnb.R;
import com.example.gitnb.api.HandlerInterface;
import com.example.gitnb.api.RequestManager;
import com.example.gitnb.api.UserInfoRequest;
import com.example.gitnb.api.UserReposRequest;
import com.example.gitnb.api.UserReposRequest.Condition;
import com.example.gitnb.api.RequestManager.WebRequest;
import com.example.gitnb.app.BaseActivity;
import com.example.gitnb.model.User;
import com.example.gitnb.model.Repository;
import com.example.gitnb.module.repos.HotReposFragment;
import com.example.gitnb.module.repos.ReposDetailActivity;
import com.example.gitnb.module.viewholder.HorizontalDividerItemDecoration;
import com.example.gitnb.utils.MessageUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class UserDetailActivity extends BaseActivity implements HandlerInterface<ArrayList<Repository>>{

	private String TAG = "UserDetailActivity";
	public static String AVATAR_URL = "avatar_url";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private WebRequest currentRequest;
    private UserReposAdapter adapter;
	private boolean isLoadingMore;
	private User user;
	private int page = 1;
	
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
        setContentView(R.layout.activity_user_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);  
        User userInfo = new User();
        userInfo.setAvatar_url(user.getAvatar_url());
        adapter = new UserReposAdapter(this, userInfo);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        adapter.SetOnItemClickListener(new UserReposAdapter.OnItemClickListener() {
        	
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(UserDetailActivity.this, ReposDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(HotReposFragment.REPOS_KEY, adapter.getItem(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
		});
        adapter.SetOnLoadMoreClickListener(new UserReposAdapter.OnItemClickListener() {
        	
			@Override
			public void onItemClick(View view, int position) {
                if(isLoadingMore){
	                Log.d(TAG,"ignore manually update!");
	            } else{
	             	page++;
	                isLoadingMore = true;
	             	requestRepository(true);
	            }
			}
			
		}); 
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(
        		android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        	
            @Override
            public void onRefresh() {
            	page = 1;
            	requestRepository(true);
            }
            
        });
        requestUserInfo(true);
        requestRepository(true);
    }
    
	@Override
    public void onSuccess(ArrayList<Repository> data){
		onSuccess(data, 0, 1);
    }

	@Override
    public void onSuccess(ArrayList<Repository> data, int totalPages, int currentPage){
    	mSwipeRefreshLayout.setRefreshing(false);
    	if(page == 1){
        	adapter.update(data);
    	}
    	else{
            isLoadingMore = false;
        	adapter.insertAtBack(data);
    	}
    }

	@Override
    public void onFailure(String error){
    	mSwipeRefreshLayout.setRefreshing(false);
    	adapter.reset();
        MessageUtils.showErrorMessage(this, error);
    }
	
    private void requestUserInfo(boolean refresh){
    	if(currentRequest != null) currentRequest.cancelRequest();
    	UserInfoRequest request = new UserInfoRequest(this);
    	UserInfoRequest.Condition condition = request.new Condition();
    	condition.SetLogin(user.getLogin());
    	condition.SetRefresh(refresh);
    	request.SetHandler(new HandlerInterface<User>(){

			@Override
			public void onSuccess(User data) {
				adapter.UpdateUserInfo(data);
			}

			@Override
			public void onSuccess(User data, int totalPages, int currentPage) {
				
			}

			@Override
			public void onFailure(String error) {
		        MessageUtils.showErrorMessage(UserDetailActivity.this, error);
			}
    		
    	});
    	request.SetSearchCondition(condition);
    	RequestManager.getInstance(this).addRequest(request);
    	currentRequest = request;
    }
    
    private void requestRepository(boolean refresh){
    	mSwipeRefreshLayout.setRefreshing(false);
    	if(currentRequest != null) currentRequest.cancelRequest();
    	UserReposRequest request = new UserReposRequest(this);
    	Condition condition = request.new Condition();
    	condition.SetLogin(user.getLogin());
    	condition.SetRefresh(refresh);
    	request.SetHandler(this);
    	request.SetSearchCondition(condition);
    	RequestManager.getInstance(this).addRequest(request);
    	currentRequest = request;
    }
}
