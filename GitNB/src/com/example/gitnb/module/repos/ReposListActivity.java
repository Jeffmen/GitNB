package com.example.gitnb.module.repos;

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
import com.example.gitnb.module.user.UserListAdapter;
import com.example.gitnb.module.user.HotUserFragment;
import com.example.gitnb.module.user.UserDetailActivity;
import com.example.gitnb.module.user.UserListAdapter.OnItemClickListener;
import com.example.gitnb.module.viewholder.HorizontalDividerItemDecoration;
import com.example.gitnb.utils.MessageUtils;

public class ReposListActivity  extends BaseActivity implements RetrofitNetworkAbs.NetworkListener{
	private String TAG = "ReposStargzersActivity";
	public static final String REPOS_TYPE = "repos_type";
	public static final String REPOS_TYPE_USER = "Repositorys";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ReposListAdapter adapter;
    private RecyclerView recyclerView;
	private boolean isLoadingMore;
	private User user;
	private String type;
	private int page = 1;

	
	@Override
	protected void setTitle(TextView view) {
        if(user != null && !user.getName().isEmpty()){    
        	switch(type){
	        case REPOS_TYPE_USER:
	        	view.setText(user.getLogin()+" / " + REPOS_TYPE_USER);    
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
		user = (User) intent.getParcelableExtra(HotUserFragment.USER);
		type = intent.getStringExtra(REPOS_TYPE);
		this.setContentView(R.layout.activity_list_layout);
		
        adapter = new ReposListAdapter(this);
        adapter.setOnItemClickListener(new ReposListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(ReposListActivity.this, UserDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(HotUserFragment.USER, adapter.getItem(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
        adapter.setOnLoadMoreClickListener(new ReposListAdapter.OnItemClickListener() {
			
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
	        case REPOS_TYPE_USER:
	        	userReposList();
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
        	adapter.update((ArrayList<Repository>) ts);
    	}
    	else{
            isLoadingMore = false;
        	adapter.insertAtBack((ArrayList<Repository>) ts);
    	}
		refreshHandler.sendEmptyMessage(END_UPDATE);
	}

	@Override
	public void onError(String Message) {
		MessageUtils.showErrorMessage(ReposListActivity.this, Message);
		refreshHandler.sendEmptyMessage(END_ERROR);
	}
	
	private void userReposList(){
		UsersClient.getNewInstance().setNetworkListener(this)
		  .userReposList(user.getLogin(), "updated", page);
	}
}
