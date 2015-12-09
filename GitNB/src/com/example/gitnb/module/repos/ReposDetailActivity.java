package com.example.gitnb.module.repos;

import com.example.gitnb.R;
import com.example.gitnb.api.retrofit.OKHttpClient;
import com.example.gitnb.api.retrofit.RepoActionsClient;
import com.example.gitnb.api.retrofit.RetrofitNetworkAbs;
import com.example.gitnb.app.BaseActivity;
import com.example.gitnb.model.Repository;
import com.example.gitnb.module.user.HotUserFragment;
import com.example.gitnb.module.user.UserDetailActivity;
import com.example.gitnb.module.user.UserListActivity;
import com.example.gitnb.utils.MessageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class ReposDetailActivity extends BaseActivity{

	private String TAG = "ReposDetailActivity";
	public static String CONTENT_URL = "content_url";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout main;
	private Repository repos;
    private Switch swithBt;
	
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
                refreshHandler.sendEmptyMessage(START_UPDATE);
            }
            
        });
        swithBt = (Switch) findViewById(R.id.switch_bt);  
    }

    private void setSwitchClicker(){
        swithBt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
        	   if(swithBt.isChecked()){
        		   starRepo();
        	   }
        	   else{
        		   unstarRepo();
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
    	
		user_avatar.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReposDetailActivity.this, UserDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(HotUserFragment.USER, repos.getOwner());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
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
    	TextView events = (TextView) findViewById(R.id.events);
    	stargazers.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReposDetailActivity.this, UserListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(HotReposFragment.REPOS, repos);
				intent.putExtras(bundle);
				intent.putExtra(UserListActivity.USER_TYPE, UserListActivity.USER_TYPE_STARGZER);
				startActivity(intent);
				
			}
		});
    	readme.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReposDetailActivity.this, ReposContentActivity.class);
				intent.putExtra(CONTENT_URL, repos.getUrl());
				startActivity(intent);
			}
		});
    	contributor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReposDetailActivity.this, UserListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(HotReposFragment.REPOS, repos);
				intent.putExtras(bundle);
				intent.putExtra(UserListActivity.USER_TYPE, UserListActivity.USER_TYPE_CONTRIBUTOR);
				startActivity(intent);
			}
		});
    	source.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReposDetailActivity.this, ReposContentsListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(HotReposFragment.REPOS, repos);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
    	events.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReposDetailActivity.this, ReposEventsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(HotReposFragment.REPOS, repos);
				intent.putExtras(bundle);
				intent.putExtra(ReposEventsActivity.EVENT_TYPE, ReposEventsActivity.EVENT_TYPE_REPOS);
				startActivity(intent);
			}
		});
    }
    
    @Override
    protected void startRefresh(){
        mSwipeRefreshLayout.setRefreshing(true);
        getRepositoryInfo();
    }

    @Override
    protected void endRefresh(){
        main.setVisibility(View.VISIBLE);
    	setRepository();
    	mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void endError(){
    	mSwipeRefreshLayout.setRefreshing(false);
    }
    
    private void getRepositoryInfo(){
    	if(repos.getOwner() != null){
            checkIfRepoIsStarred();
			refreshHandler.sendEmptyMessage(END_UPDATE);
			return;
    	}
    	OKHttpClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
				repos = (Repository) ts;
		        checkIfRepoIsStarred();
				refreshHandler.sendEmptyMessage(END_UPDATE);
			}

			@Override
			public void onError(String Message) {
				MessageUtils.showErrorMessage(ReposDetailActivity.this, Message);
				refreshHandler.sendEmptyMessage(END_ERROR);
			}
			
    	}).request(repos.getUrl(), Repository.class);
    }
    
	private void checkIfRepoIsStarred(){
		RepoActionsClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
		        swithBt.setVisibility(View.VISIBLE);
				swithBt.setChecked(true);
				setSwitchClicker();
			}

			@Override
			public void onError(String Message) {
		        swithBt.setVisibility(View.VISIBLE);
				setSwitchClicker();
			}
			
    	}).checkIfRepoIsStarred(repos.getOwner().getLogin(), repos.getName());
	}
	
	private void starRepo(){
		final Snackbar snackbar = Snackbar.make(mSwipeRefreshLayout, "UnStaring ...", Snackbar.LENGTH_INDEFINITE);
		snackbar.show();
		RepoActionsClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
				swithBt.setChecked(true);
				snackbar.dismiss();
			}

			@Override
			public void onError(String Message) {
				snackbar.dismiss();
				MessageUtils.showErrorMessage(ReposDetailActivity.this, Message);
			}
			
    	}).starRepo(repos.getOwner().getLogin(), repos.getName());
	}	
	
	private void unstarRepo(){
		final Snackbar snackbar = Snackbar.make(mSwipeRefreshLayout, "Staring ...", Snackbar.LENGTH_INDEFINITE);
		snackbar.show();
		RepoActionsClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
				swithBt.setChecked(false);
				snackbar.dismiss();
			}

			@Override
			public void onError(String Message) {
				snackbar.dismiss();
				MessageUtils.showErrorMessage(ReposDetailActivity.this, Message);
			}
			
    	}).unstarRepo(repos.getOwner().getLogin(), repos.getName());
	}
}
