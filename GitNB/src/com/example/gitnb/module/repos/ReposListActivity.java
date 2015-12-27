package com.example.gitnb.module.repos;

import java.util.ArrayList;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.util.Log;

import com.example.gitnb.R;
import com.example.gitnb.api.retrofit.RetrofitNetworkAbs;
import com.example.gitnb.api.retrofit.TrendingClient;
import com.example.gitnb.api.retrofit.UsersClient;
import com.example.gitnb.app.BaseSwipeActivity;
import com.example.gitnb.model.Repository;
import com.example.gitnb.model.ShowCase;
import com.example.gitnb.model.User;
import com.example.gitnb.model.search.ShowCaseSearch;
import com.example.gitnb.module.trending.ShowCaseFragment;
import com.example.gitnb.module.user.HotUserFragment;
import com.example.gitnb.module.viewholder.HorizontalDividerItemDecoration;
import com.example.gitnb.utils.MessageUtils;

public class ReposListActivity  extends BaseSwipeActivity implements RetrofitNetworkAbs.NetworkListener<ArrayList<Repository>>{
	private String TAG = "ReposListActivity";
	public static final String REPOS_TYPE = "repos_type";
	public static final String REPOS_TYPE_USER = "user_repository";
	public static final String REPOS_TYPE_SHOWCASE = "showcase_epository";
    private ReposListAdapter adapter;
    private RecyclerView recyclerView;
	private boolean isLoadingMore;
	private ShowCase showCase;
	private User user;
	private String type;
	private int page = 1;

	
	@Override
	protected void setTitle(TextView view) {
        if(user != null && !user.getLogin().isEmpty()){    
        	switch(type){
	        case REPOS_TYPE_USER:
	        	view.setText(user.getLogin()+" / Repositorys");    
	        	break;
            }
        }
        else if(showCase != null && !showCase.name.isEmpty()){    
        	switch(type){
	        case REPOS_TYPE_SHOWCASE:
	        	view.setText(showCase.name +" / Repositorys");    
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
		type = intent.getStringExtra(REPOS_TYPE);
        switch(type){
        case REPOS_TYPE_USER:
    		user = (User) intent.getParcelableExtra(HotUserFragment.USER);
        	break;
        case REPOS_TYPE_SHOWCASE:
    		showCase = (ShowCase) intent.getParcelableExtra(ShowCaseFragment.SHOWCASE);
        	break;
        }
		this.setContentView(R.layout.activity_list_layout);
		
        adapter = new ReposListAdapter(this);
        adapter.setOnItemClickListener(new ReposListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(ReposListActivity.this, ReposDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(HotReposFragment.REPOS, adapter.getItem(position));
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
	                getRefreshdler().sendEmptyMessage(START_UPDATE);
	            }
			}
		}); 
        
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);  
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
	}
	 
    @Override
    protected void startRefresh(){
    	super.startRefresh();
    	page = 1;
        switch(type){
	        case REPOS_TYPE_USER:
	        	userReposList();
	        	break;
	        case REPOS_TYPE_SHOWCASE:
	        	showCaseReposList();
	        	break;
        }
    }

    @Override
    protected void endRefresh(){
    	super.endRefresh();
        isLoadingMore = false;
    }

    @Override
    protected void endError(){
    	super.endError();
        isLoadingMore = false;
    }
    
	@Override
	public void onOK(ArrayList<Repository> ts) {   	
		if(page == 1){
        	adapter.update(ts);
    	}
    	else{
            isLoadingMore = false;
        	adapter.insertAtBack(ts);
    	}
		getRefreshdler().sendEmptyMessage(END_UPDATE);
	}

	@Override
	public void onError(String Message) {
		MessageUtils.showErrorMessage(ReposListActivity.this, Message);
		getRefreshdler().sendEmptyMessage(END_ERROR);
	}
	
	private void userReposList(){
		UsersClient.getNewInstance().setNetworkListener(this)
		  .userReposList(user.getLogin(), "updated", page);
	}

	private void showCaseReposList(){
		TrendingClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener<ShowCaseSearch>(){

			@Override
			public void onOK(ShowCaseSearch ts) {   	
				if(page == 1){
		        	adapter.update((ArrayList<Repository>)ts.repositories);
		    	}
		    	else{
		            isLoadingMore = false;
		        	adapter.insertAtBack((ArrayList<Repository>)ts.repositories);
		    	}
				getRefreshdler().sendEmptyMessage(END_UPDATE);
			}

			@Override
			public void onError(String Message) {
				MessageUtils.showErrorMessage(ReposListActivity.this, Message);
				getRefreshdler().sendEmptyMessage(END_ERROR);
			}
		}).trendingShowCase(showCase.slug);
	}
}
