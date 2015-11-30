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
import com.example.gitnb.app.BaseActivity;
import com.example.gitnb.model.Repository;
import com.example.gitnb.module.user.HotUserFragment;
import com.example.gitnb.module.user.UserDetailActivity;
import com.example.gitnb.module.viewholder.HorizontalDividerItemDecoration;
import com.example.gitnb.utils.MessageUtils;

public class ReposEventsActivity  extends BaseActivity implements RetrofitNetworkAbs.NetworkListener{
	private String TAG = "ReposEventsActivity";
	public static final String EVENT_TYPE = "event_type";
	public static final String EVENT_TYPE_REPOS = "Events";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ReposListAdapter adapter;
    private RecyclerView recyclerView;
	private boolean isLoadingMore;
	private Repository repos;
	private String type;
	private int page = 1;

	
	@Override
	protected void setTitle(TextView view) {
        if(repos != null && !repos.getName().isEmpty()){    
        	switch(type){
	        case EVENT_TYPE_REPOS:
	        	view.setText(repos.getName()+" / "+EVENT_TYPE_REPOS);    
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
		repos = (Repository) intent.getParcelableExtra(HotReposFragment.REPOS);
		type = intent.getStringExtra(EVENT_TYPE);
		this.setContentView(R.layout.activity_list_layout);
		
        adapter = new ReposListAdapter(this);
        adapter.setOnItemClickListener(new ReposListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(ReposEventsActivity.this, UserDetailActivity.class);
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
	        case EVENT_TYPE_REPOS:
	        	getEvents();
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
		MessageUtils.showErrorMessage(ReposEventsActivity.this, Message);
		refreshHandler.sendEmptyMessage(END_ERROR);
	}
	
	private void getEvents(){
		RepoClient.getNewInstance().setNetworkListener(this)
		  .events(repos.getOwner().getLogin(), repos.getName(), page);
	}
}
