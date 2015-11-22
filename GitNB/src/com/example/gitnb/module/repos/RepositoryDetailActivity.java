package com.example.gitnb.module.repos;

import com.example.gitnb.R;
import com.example.gitnb.api.retrofit.RepoActionsClient;
import com.example.gitnb.api.retrofit.RepoClient;
import com.example.gitnb.api.retrofit.RetrofitNetworkAbs;
import com.example.gitnb.app.BaseActivity;
import com.example.gitnb.model.Content;
import com.example.gitnb.model.Repository;
import com.example.gitnb.module.viewholder.HorizontalDividerItemDecoration;
import com.example.gitnb.utils.MessageUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class RepositoryDetailActivity extends BaseActivity{

	private String TAG = "UserDetailActivity";
	public static String CONTENT = "content";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private ReposContentsAdapter adapter;
    private MaterialAnimatedSwitch swithBt;
    private RecyclerView recyclerView;
	private boolean isFirst = true;
	private Repository repos;
    private String path;
	
    protected void setTitle(TextView view){
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
        repos = (Repository) intent.getParcelableExtra(HotReposFragment.REPOS_KEY);
        setContentView(R.layout.activity_repo_detail);
        this.setRepository();
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);  
        adapter = new ReposContentsAdapter(this);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        adapter.SetOnItemClickListener(new ReposContentsAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Content content = adapter.getItem(position);
				if(content.isDir()){
					path += content.name + "/";
					requestContents();
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
            	requestContents();
            }
        });
        swithBt = (MaterialAnimatedSwitch) findViewById(R.id.switch_bt);  
        swithBt.setVisibility(View.VISIBLE);
        swithBt.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
        
           @Override 
           public void onCheckedChanged(boolean isChecked) {
        	   if(!isFirst){
	        	   if(isChecked){
	        		   starRepo();
	        	   }
	        	   else{
	        		   unstarRepo();
	        	   }
        	   }
           }
        
        });
        checkIfRepoIsStarred();
        requestContents();
    }
    
    private void setRepository(){
    	TextView repos_name = (TextView) findViewById(R.id.repos_name);
    	TextView repos_owner = (TextView) findViewById(R.id.repos_owner);
    	TextView repos_created = (TextView) findViewById(R.id.repos_created);
    	TextView repos_homepage = (TextView) findViewById(R.id.repos_homepage);
    	TextView repos_discription = (TextView) findViewById(R.id.repos_description);
    	SimpleDraweeView user_avatar = (SimpleDraweeView) findViewById(R.id.user_avatar);
		if(this.repos != null){
			repos_name.setText(repos.getName());				
			String date = repos.getCreated_at();
			if(date != null && !date.isEmpty()){
				date = date.substring(0, date.indexOf('T'));
			}
			repos_created.setText(date);
			repos_homepage.setText(repos.getHomepage());
			repos_discription.setText(repos.getDescription());
		}
		if(repos.getOwner() != null){
			repos_owner.setText(repos.getOwner().getLogin());
			user_avatar.setImageURI(Uri.parse(repos.getOwner().getAvatar_url()));
		}
    }
	
    private void showContent(Content content){
		Intent intent = new Intent(RepositoryDetailActivity.this, ReposContentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable(CONTENT, content);
		intent.putExtras(bundle);
		startActivity(intent);
    }
    
	private void checkIfRepoIsStarred(){
		RepoActionsClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
				swithBt.toggle();
				isFirst = false;
			}

			@Override
			public void onError(String Message) {
				isFirst = false;
			}
			
    	}).checkIfRepoIsStarred(repos.getOwner().getLogin(), repos.getName());
	}
	
	private void starRepo(){
		RepoActionsClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
				Snackbar.make(recyclerView, "Already Stared", Snackbar.LENGTH_LONG).show();
			}

			@Override
			public void onError(String Message) {
				MessageUtils.showErrorMessage(RepositoryDetailActivity.this, Message);
			}
			
    	}).starRepo(repos.getOwner().getLogin(), repos.getName());
	}	
	
	private void unstarRepo(){
		RepoActionsClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
				Snackbar.make(recyclerView, "Already unStared", Snackbar.LENGTH_LONG).show();
			}

			@Override
			public void onError(String Message) {
				MessageUtils.showErrorMessage(RepositoryDetailActivity.this, Message);
			}
			
    	}).unstarRepo(repos.getOwner().getLogin(), repos.getName());
	}
	
    private void requestContents(){
    	RepoClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
				Snackbar.make(recyclerView, "Already unStared", Snackbar.LENGTH_LONG).show();
			}

			@Override
			public void onError(String Message) {
				MessageUtils.showErrorMessage(RepositoryDetailActivity.this, Message);
			}
			
    	}).contents(repos.getOwner().getLogin(), repos.getName(), path);
    }
}
