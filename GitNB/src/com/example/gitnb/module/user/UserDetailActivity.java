package com.example.gitnb.module.user;

import java.util.ArrayList;

import com.example.gitnb.R;
import com.example.gitnb.api.HandlerInterface;
import com.example.gitnb.api.RequestManager;
import com.example.gitnb.api.UserReposRequest;
import com.example.gitnb.api.UserReposRequest.Condition;
import com.example.gitnb.api.RequestManager.WebRequest;
import com.example.gitnb.model.HotUser;
import com.example.gitnb.model.Repository;
import com.example.gitnb.utils.MessageUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class UserDetailActivity extends AppCompatActivity implements HandlerInterface<ArrayList<Repository>>{

	private String TAG = "UserDetailActivity";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private WebRequest currentRequest;
    private UserReposAdapter adapter;
	private boolean isLoadingMore;
	private Toolbar toolbar;
	private HotUser user;
	private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = (HotUser) intent.getParcelableExtra(HotUserFragment.USER_KEY);
        setContentView(R.layout.activity_user_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        if(user != null && !user.getLogin().isEmpty()){
            title.setText(user.getLogin());
        }else{
            title.setText("NULL");
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);        
        adapter = new UserReposAdapter(this);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        //adapter.SetSearchTextWatcher(this);
        adapter.SetOnItemClickListener(new UserReposAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
				Toast.makeText(UserDetailActivity.this, "item:"+position, Toast.LENGTH_SHORT).show();
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
	             	requestRepository(true, null);
	            }
			}
		}); 
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
            	requestRepository(true, null);
            }
        });
        requestRepository(true, null);
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
    
    private void requestRepository(boolean refresh, String key){
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
