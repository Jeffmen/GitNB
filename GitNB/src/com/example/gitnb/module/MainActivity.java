package com.example.gitnb.module;

import java.util.ArrayList;
import java.util.List;

import com.astuetz.PagerSlidingTabStrip;
import com.example.gitnb.R;
import com.example.gitnb.app.BaseActivity;
import com.example.gitnb.module.repos.HotReposFragment;
import com.example.gitnb.module.trending.TrendingReposFragment;
import com.example.gitnb.module.user.HotUserFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
    private static int FOR_LANGUAGE = 200;
    private TabPagerAdapter pagerAdapter;
	private TabLayout tabs;
	private FloatingActionButton faButton;
    private CoordinatorLayout layout;
	private DisplayMetrics dm;
    private ViewPager pager;
	
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		dm = getResources().getDisplayMetrics();
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (TabLayout) findViewById(R.id.tabs);
		pagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
		pagerAdapter.addFragment(new HotUserFragment(), "User");
		pagerAdapter.addFragment(new TrendingReposFragment(), "Trending");
		pagerAdapter.addFragment(new HotReposFragment(), "Repos");
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
    }

    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 

        if (requestCode == FOR_LANGUAGE && resultCode == RESULT_OK) { 
        	String language = data.getStringExtra(LanguageActivity.LANGUAGE_KEY);
        	UpdateLanguageListener languageListener = (UpdateLanguageListener) pagerAdapter.getItem(pager.getCurrentItem());
        	languageListener.updateLanguage(language);
//				Snackbar.make(layout, "connection error", Snackbar.LENGTH_LONG).setAction("retry", new View.OnClickListener() {
//		              
//					@Override
//		              public void onClick(View v) {
//		                  Toast.makeText(MainActivity.this, "aleady click snackbar", Toast.LENGTH_SHORT).show();
//		              }
//					
//		         }).show();
        }
    }
    
//	private void setTabsValue() {
//		tabs.setShouldExpand(true);
//		//tabs.setDividerColor(ContextCompat.getColor(this,R.color.contacts_theme_color));
//		tabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm));
//		tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, dm));
//		tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, dm));
//		tabs.setTabPaddingLeftRight(0);
//		tabs.setIndicatorColor(Color.WHITE);
//		tabs.setSelectedTextColor(Color.WHITE);
//		//tabs.setBackgroundColor(ContextCompat.getColor(this,R.color.contacts_theme_color));
//		tabs.setTextColor(getResources().getColor(R.color.transparent_dark_gray));
//		tabs.setTabBackground(0);
//	}
    
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
