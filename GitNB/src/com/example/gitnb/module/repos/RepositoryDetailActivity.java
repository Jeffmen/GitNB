package com.example.gitnb.module.repos;

import com.example.gitnb.R;
import com.example.gitnb.api.retrofit.RepoActionsClient;
import com.example.gitnb.api.retrofit.RetrofitNetworkAbs;
import com.example.gitnb.app.BaseActivity;
import com.example.gitnb.model.Repository;
import com.example.gitnb.utils.MessageUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RepositoryDetailActivity extends BaseActivity{

	private String TAG = "UserDetailActivity";
	public static String CONTENT_URL = "content_url";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MaterialAnimatedSwitch swithBt;
	private boolean isFirst = true;
    private LinearLayout main;
	private Repository repos;
	
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
        repos = (Repository) intent.getParcelableExtra(HotReposFragment.REPOS);
        setContentView(R.layout.activity_repo_detail);
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
            	setRepository();
            	mSwipeRefreshLayout.setRefreshing(false);
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
    }
    
    private void setRepository(){
    	TextView repos_name = (TextView) findViewById(R.id.repos_name);
    	TextView repos_owner = (TextView) findViewById(R.id.repos_owner);
    	TextView repos_updated = (TextView) findViewById(R.id.repos_updated);
    	TextView repos_homepage = (TextView) findViewById(R.id.repos_homepage);
    	TextView repos_discription = (TextView) findViewById(R.id.repos_description);
    	SimpleDraweeView user_avatar = (SimpleDraweeView) findViewById(R.id.user_avatar);

    	TextView type = (TextView) findViewById(R.id.type);
    	TextView issues = (TextView) findViewById(R.id.issues);
    	TextView created_date = (TextView) findViewById(R.id.created_date);
    	TextView language = (TextView) findViewById(R.id.language);
    	TextView forks = (TextView) findViewById(R.id.forks);
    	TextView size = (TextView) findViewById(R.id.size);
    	
		if(this.repos != null){
			repos_name.setText(repos.getName());	
			repos_updated.setText(repos.getUpdated_at());
			repos_homepage.setText(repos.getHomepage());
			repos_discription.setText(repos.getDescription());

			type.setText(repos.is_private() ? "Private" : "Public");
			issues.setText(repos.getOpen_issues()+" Issues");			
			String date = repos.getCreated_at();
			if(date != null && !date.isEmpty()){
				date = date.substring(0, date.indexOf('T'));
			}
			created_date.setText(date);
			language.setText(repos.getLanguage());
			forks.setText(repos.getForks_count()+" Forks");
			size.setText((float)((repos.getSize()/1024*100))/100+"M");
		}
		if(repos.getOwner() != null){
			repos_owner.setText(repos.getOwner().getLogin());
			user_avatar.setImageURI(Uri.parse(repos.getOwner().getAvatar_url()));
		}
		
    	TextView stargazers = (TextView) findViewById(R.id.stargazers);
    	TextView readme = (TextView) findViewById(R.id.readme);
    	TextView contributor = (TextView) findViewById(R.id.contributor);
    	TextView source = (TextView) findViewById(R.id.source);
    	stargazers.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
    	readme.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RepositoryDetailActivity.this, ReposContentActivity.class);
				intent.putExtra(CONTENT_URL, repos.getUrl());
				startActivity(intent);
			}
		});
    	contributor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RepositoryDetailActivity.this, ReposContentActivity.class);
				intent.putExtra(CONTENT_URL, repos.getUrl());
				startActivity(intent);
			}
		});
    	source.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RepositoryDetailActivity.this, ReposContentsListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(HotReposFragment.REPOS, repos);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
        refreshHandler.sendEmptyMessage(END_UPDATE);
    }
    
    @Override
    protected void startRefresh(){
        mSwipeRefreshLayout.setRefreshing(true);
    	setRepository();
        checkIfRepoIsStarred();
    }

    @Override
    protected void endRefresh(){
        main.setVisibility(View.VISIBLE);
    	mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void endError(){
    	mSwipeRefreshLayout.setRefreshing(false);
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
				Snackbar.make(mSwipeRefreshLayout, "Already Stared", Snackbar.LENGTH_LONG).show();
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
				Snackbar.make(mSwipeRefreshLayout, "Already unStared", Snackbar.LENGTH_LONG).show();
			}

			@Override
			public void onError(String Message) {
				MessageUtils.showErrorMessage(RepositoryDetailActivity.this, Message);
			}
			
    	}).unstarRepo(repos.getOwner().getLogin(), repos.getName());
	}
}