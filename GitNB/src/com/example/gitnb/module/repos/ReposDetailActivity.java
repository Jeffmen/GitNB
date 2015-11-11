package com.example.gitnb.module.repos;

import java.util.ArrayList;

import com.example.gitnb.R;
import com.example.gitnb.api.HandlerInterface;
import com.example.gitnb.api.ReposContributorsRequest;
import com.example.gitnb.api.ReposContributorsRequest.Condition;
import com.example.gitnb.api.RequestManager;
import com.example.gitnb.api.RequestManager.WebRequest;
import com.example.gitnb.app.BaseActivity;
import com.example.gitnb.model.User;
import com.example.gitnb.model.Repository;
import com.example.gitnb.module.user.HorizontalDividerItemDecoration;
import com.example.gitnb.module.user.HotUserFragment;
import com.example.gitnb.module.user.UserDetailActivity;
import com.example.gitnb.utils.MessageUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ReposDetailActivity extends BaseActivity implements HandlerInterface<ArrayList<User>>{

	private String TAG = "UserDetailActivity";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private ReposContributorAdapter adapter;
    private RecyclerView recyclerView;
    private WebRequest currentRequest;
	private boolean isLoadingMore;
	private Repository repos;
	private int page = 1;
	
    protected void setTitle(TextView view){
        if(repos != null && !repos.getName().isEmpty()){
        	view.setText(repos.getName());
        }else{
        	view.setText("NULL");
        }
    }
    
    protected int getNavigationIcon(){
    	return R.drawable.ic_back_white_60;
    }
    
    protected View.OnClickListener getNavigationOnClickListener(){
    	return new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		};
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        repos = (Repository) intent.getParcelableExtra(HotReposFragment.REPOS_KEY);
        setContentView(R.layout.activity_user_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);  
        adapter = new ReposContributorAdapter(this, repos);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        adapter.SetOnItemClickListener(new ReposContributorAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(ReposDetailActivity.this, UserDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(HotUserFragment.USER_KEY, adapter.getItem(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
        adapter.SetOnLoadMoreClickListener(new ReposContributorAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
                if(isLoadingMore){
	                Log.d(TAG,"ignore manually update!");
	            } else{
	             	page++;
	                isLoadingMore = true;
	                requestContributors(true);
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
            	requestContributors(true);
            }
        });
        requestContributors(true);
    }
    
	@Override
    public void onSuccess(ArrayList<User> data){
		onSuccess(data, 0, 1);
    }

	@Override
    public void onSuccess(ArrayList<User> data, int totalPages, int currentPage){
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
	
    private void requestContributors(boolean refresh){
    	mSwipeRefreshLayout.setRefreshing(false);
    	if(currentRequest != null) currentRequest.cancelRequest();
    	ReposContributorsRequest request = new ReposContributorsRequest(this);
    	Condition condition = request.new Condition();
    	condition.SetLogin(repos.getOwner().getLogin());
    	condition.SetReposName(repos.getName());
    	condition.SetRefresh(refresh);
    	request.SetHandler(this);
    	request.SetSearchCondition(condition);
    	RequestManager.getInstance(this).addRequest(request);
    	currentRequest = request;
    }
}
