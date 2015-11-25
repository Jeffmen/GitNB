package com.example.gitnb.module.repos;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.gitnb.R;
import com.example.gitnb.api.retrofit.RepoClient;
import com.example.gitnb.api.retrofit.RetrofitNetworkAbs;
import com.example.gitnb.app.BaseActivity;
import com.example.gitnb.model.Content;
import com.example.gitnb.model.Repository;
import com.example.gitnb.module.viewholder.HorizontalDividerItemDecoration;
import com.example.gitnb.utils.MessageUtils;

public class ReposContentsListActivity extends BaseActivity {
	public static String CONTENT = "content";
	private static int UPDATE_CONTENT = 100;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private ReposContentsAdapter adapter;
    private RecyclerView recyclerView;
	private Repository repos;
    private String path = "";
    
	public enum TYPE{
		REPOS_CONTENT,
		USER_FOLLOWER,
		USER_FOLLOWING,
		USER_REPOSITORY
	}    
	
	private Handler mHandler = new Handler(){ 
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == UPDATE_CONTENT)
            {
            	mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    };

	@Override
	protected void setTitle(TextView view) {
        if(repos != null && !repos.getName().isEmpty()){
        	view.setText(repos.getName());
        }else{
        	view.setText("NULL");
        }
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        repos = (Repository) intent.getParcelableExtra(HotReposFragment.REPOS);
        setContentView(R.layout.activity_list_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        adapter = new ReposContentsAdapter(this);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        adapter.SetOnItemClickListener(new ReposContentsAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Content content = adapter.getItem(position);
				if(content.isDir()){
					requestContents(content.name);
				}
				if(content.isFile()){
					showContent(content);
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
            	requestContents("");
            }
            
        });
        requestContents("");
    }

    private void showContent(Content content){
		Intent intent = new Intent(ReposContentsListActivity.this, ReposContentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable(CONTENT, content);
		intent.putExtras(bundle);
		startActivity(intent);
    }
    
    private void requestContents(final String name){
    	mSwipeRefreshLayout.setRefreshing(true);
    	RepoClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
				path += name + "/";
				adapter.update((ArrayList<Content>) ts);
				mHandler.sendEmptyMessage(UPDATE_CONTENT);
			}

			@Override
			public void onError(String Message) {
				MessageUtils.showErrorMessage(ReposContentsListActivity.this, Message);
				mHandler.sendEmptyMessage(UPDATE_CONTENT);
			}
			
    	}).contents(repos.getOwner().getLogin(), repos.getName(), path + name);
    }
    
}
