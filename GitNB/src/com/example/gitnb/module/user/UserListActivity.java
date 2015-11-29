package com.example.gitnb.module.user;

import java.util.ArrayList;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.util.Log;

import com.example.gitnb.R;
import com.example.gitnb.api.retrofit.RepoClient;
import com.example.gitnb.api.retrofit.RetrofitNetworkAbs;
import com.example.gitnb.api.retrofit.UsersClient;
import com.example.gitnb.app.BaseActivity;
import com.example.gitnb.model.Repository;
import com.example.gitnb.model.User;
import com.example.gitnb.module.repos.HotReposFragment;
import com.example.gitnb.module.viewholder.HorizontalDividerItemDecoration;
import com.example.gitnb.utils.MessageUtils;

public class UserListActivity  extends BaseActivity implements RetrofitNetworkAbs.NetworkListener{
	private String TAG = "ReposStargzersActivity";
	public static final String USER_TYPE = "user_type";
	public static final String USER_TYPE_STARGZER = "Stargzer";
	public static final String USER_TYPE_CONTRIBUTOR = "Contributor";
	public static final String USER_TYPE_FOLLOWER = "Follower";
	public static final String USER_TYPE_FOLLOWING = "Following";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private UserListAdapter adapter;
    private RecyclerView recyclerView;
	private boolean isLoadingMore;
	private Repository repos;
	private User user;
	private String type;
	private int page = 1;

	
	@Override
	protected void setTitle(TextView view) {
        if(repos != null && !repos.getName().isEmpty()){
            switch(type){
		        case USER_TYPE_STARGZER:
		        	view.setText(repos.getName() + " / " + USER_TYPE_STARGZER);
		        	break;
		        case USER_TYPE_CONTRIBUTOR:
		        	view.setText(repos.getName() + " / " + USER_TYPE_CONTRIBUTOR);
		        	break;
            }
        }
        else if(user != null && !user.getName().isEmpty()){
            switch(type){
		        case USER_TYPE_FOLLOWER:
		        	view.setText(user.getLogin() + " / " + USER_TYPE_FOLLOWER);
		        	break;
		        case USER_TYPE_FOLLOWING:
		        	view.setText(user.getLogin() + " / " + USER_TYPE_FOLLOWING);
		        	break;
            }
        }else{
        	view.setText("NULL");
        }
	}
	
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		type = intent.getStringExtra(USER_TYPE);
        switch(type){
	        case USER_TYPE_STARGZER:
	        case USER_TYPE_CONTRIBUTOR:
	    		repos = (Repository) intent.getParcelableExtra(HotReposFragment.REPOS);
	        	break;
	        case USER_TYPE_FOLLOWER:
	        case USER_TYPE_FOLLOWING:
	    		user = (User) intent.getParcelableExtra(HotUserFragment.USER);
	        	break;
	    }
        
		this.setContentView(R.layout.activity_list_layout);
		
        adapter = new UserListAdapter(this);
        adapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(UserListActivity.this, UserDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(HotUserFragment.USER, adapter.getItem(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
        adapter.setOnLoadMoreClickListener(new UserListAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
                if(isLoadingMore){
	                Log.d(TAG,"ignore manually update!");
	            } else{
	             	page++;
	                isLoadingMore = true;
	                refreshHandler.sendEmptyMessage(START_UPDATE);
	            }
			}
		}); 
        
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);  
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                refreshHandler.sendEmptyMessage(START_UPDATE);
            }
        });
	}
	 
    @Override
    protected void startRefresh(){
        mSwipeRefreshLayout.setRefreshing(true);
        switch(type){
	        case USER_TYPE_STARGZER:
	        	getStargzers();
	        	break;
	        case USER_TYPE_CONTRIBUTOR:
	            getContributors();
	        	break;
	        case USER_TYPE_FOLLOWER:
	        	getFollowers();
	        	break;
	        case USER_TYPE_FOLLOWING:
	        	getFollowing();
	        	break;
        }
    }

    @Override
    protected void endRefresh(){
        isLoadingMore = false;
    	mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void endError(){
        isLoadingMore = false;
    	mSwipeRefreshLayout.setRefreshing(false);
    }
    
	@Override
	public void onOK(Object ts) {   	
		if(page == 1){
        	adapter.update((ArrayList<User>) ts);
    	}
    	else{
            isLoadingMore = false;
        	adapter.insertAtBack((ArrayList<User>) ts);
    	}
		refreshHandler.sendEmptyMessage(END_UPDATE);
	}

	@Override
	public void onError(String Message) {
		MessageUtils.showErrorMessage(UserListActivity.this, Message);
		refreshHandler.sendEmptyMessage(END_ERROR);
	}
	
	private void getContributors(){
		RepoClient.getNewInstance().setNetworkListener(this)
		  .contributors(repos.getOwner().getLogin(), repos.getName(), page);
	}
	
	private void getStargzers(){
		RepoClient.getNewInstance().setNetworkListener(this)
		  .stargazers(repos.getOwner().getLogin(), repos.getName(), page);
	}

	private void getFollowers(){
		UsersClient.getNewInstance().setNetworkListener(this)
		  .followers(user.getLogin(), page);
	}
	
	private void getFollowing(){
		UsersClient.getNewInstance().setNetworkListener(this)
		  .following(user.getLogin(), page);
	}
}
