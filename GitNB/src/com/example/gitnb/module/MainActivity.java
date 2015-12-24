package com.example.gitnb.module;

import java.util.ArrayList;
import java.util.List;

import com.example.gitnb.R;
import com.example.gitnb.app.BaseActivity;
import com.example.gitnb.model.User;
import com.example.gitnb.module.repos.EventListActivity;
import com.example.gitnb.module.repos.HotReposFragment;
import com.example.gitnb.module.repos.ReposListActivity;
import com.example.gitnb.module.trending.ShowCaseFragment;
import com.example.gitnb.module.trending.TrendingReposFragment;
import com.example.gitnb.module.user.HotUserFragment;
import com.example.gitnb.module.user.ImageShowerActivity;
import com.example.gitnb.module.user.ReceivedEventsFragment;
import com.example.gitnb.module.user.UserDetailActivity;
import com.example.gitnb.module.user.UserListActivity;
import com.example.gitnb.utils.CurrentUser;
import com.facebook.drawee.view.SimpleDraweeView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends BaseActivity {
    private static int FOR_LANGUAGE = 200;
	private FloatingActionButton faButton;
    private TabPagerAdapter pagerAdapter;
    private DrawerLayout drawerlayout;
    private CoordinatorLayout layout;
	private DisplayMetrics dm;
    private ViewPager pager;
	private TabLayout tabs;
    private User me;
	
    public interface UpdateLanguageListener{
    	Void updateLanguage(String language);
    }
    
    protected void setTitle(TextView view){
    	view.setText("GitNB");
    }

    @Override
    protected int getNavigationIcon(){
    	return R.drawable.ic_git_white_50;
    }

    @Override
    protected View.OnClickListener getNavigationOnClickListener(){
    	return new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				drawerlayout.openDrawer(Gravity.LEFT);
			}
		};
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		dm = getResources().getDisplayMetrics();
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (TabLayout) findViewById(R.id.tabs);
		me = CurrentUser.get(MainActivity.this);
		drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		pagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
		pagerAdapter.addFragment(new ShowCaseFragment(), "ShowCase");
		//pagerAdapter.addFragment(new HotUserFragment(), "User");
		if(me != null){
			pagerAdapter.addFragment(new ReceivedEventsFragment(me), "News");
		}
		pagerAdapter.addFragment(new TrendingReposFragment(), "Trending");
		//pagerAdapter.addFragment(new HotReposFragment(), "Repos");
		pager.setAdapter(pagerAdapter);
		tabs.setSelectedTabIndicatorColor(Color.WHITE);
		tabs.setTabTextColors(getResources().getColor(R.color.transparent_dark_gray), Color.WHITE);
		tabs.setupWithViewPager(pager);
		//tabs.setOnPageChangeListener(new PageListener());
		//setTabsValue();
		pager.setCurrentItem(1);
		pager.setOffscreenPageLimit(2);
		layout = (CoordinatorLayout) findViewById(R.id.layout);
		faButton = (FloatingActionButton) findViewById(R.id.faButton);
		faButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, LanguageActivity.class);
				startActivityForResult(intent, FOR_LANGUAGE);
			}
		});
		setMeDetail();
    }

    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 

        if (requestCode == FOR_LANGUAGE && resultCode == RESULT_OK) { 
        	String language = data.getStringExtra(LanguageActivity.LANGUAGE_KEY);
        	UpdateLanguageListener languageListener = (UpdateLanguageListener) pagerAdapter.getItem(pager.getCurrentItem());
        	languageListener.updateLanguage(language);
        }
    }
    
    private void setMeDetail(){
		SimpleDraweeView me_avatar = (SimpleDraweeView) findViewById(R.id.me_avatar);
		TextView me_login = (TextView) findViewById(R.id.me_login);
		if(me == null){
			me_login.setText("Login...");
			me_avatar.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(MainActivity.this, GitHubAnthorizeActivity.class);
					startActivity(intent);
				}
	        	
	        });
		}
		else{
			me_login.setText(me.getLogin());
			me_avatar.setImageURI(Uri.parse(me.getAvatar_url()));
			me_avatar.setOnClickListener(null);    	
			
			TextView events = (TextView) findViewById(R.id.events);
	    	TextView organizations = (TextView) findViewById(R.id.organizations);
	    	TextView followers = (TextView) findViewById(R.id.followers);
	    	TextView following = (TextView) findViewById(R.id.following);
	    	TextView repositorys = (TextView) findViewById(R.id.repositorys);

	    	events.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this, EventListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putParcelable(HotUserFragment.USER, me);
					intent.putExtras(bundle);
					intent.putExtra(EventListActivity.EVENT_TYPE, EventListActivity.EVENT_TYPE_USER);
					startActivity(intent);
				}
			});
	    	organizations.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				}
			});
	    	followers.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this, UserListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putParcelable(HotUserFragment.USER, me);
					intent.putExtras(bundle);
					intent.putExtra(UserListActivity.USER_TYPE, UserListActivity.USER_TYPE_FOLLOWER);
					startActivity(intent);
				}
			});
	    	following.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this, UserListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putParcelable(HotUserFragment.USER, me);
					intent.putExtras(bundle);
					intent.putExtra(UserListActivity.USER_TYPE, UserListActivity.USER_TYPE_FOLLOWING);
					startActivity(intent);
				}
			});
	    	repositorys.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this, ReposListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putParcelable(HotUserFragment.USER, me);
					intent.putExtras(bundle);
					intent.putExtra(ReposListActivity.REPOS_TYPE, ReposListActivity.REPOS_TYPE_USER);
					startActivity(intent);
				}
			});
		}
    }
    
    public class TabPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<Fragment>();
        private final List<String> mFragmentTitles = new ArrayList<String>();

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

	}
}
