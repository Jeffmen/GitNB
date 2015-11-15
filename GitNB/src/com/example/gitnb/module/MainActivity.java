package com.example.gitnb.module;

import java.util.ArrayList;
import java.util.List;

import com.astuetz.PagerSlidingTabStrip;
import com.example.gitnb.R;
import com.example.gitnb.app.BaseActivity;
import com.example.gitnb.module.repos.HotReposFragment;
import com.example.gitnb.module.trending.TrendingReposFragment;
import com.example.gitnb.module.user.HotUserFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

    private TabPagerAdapter pagerAdapter;
	private PagerSlidingTabStrip tabs;
	private DisplayMetrics dm;
    private ViewPager pager;
	
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
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
		pagerAdapter.addFragment(new HotUserFragment(), "User");
		pagerAdapter.addFragment(new TrendingReposFragment(), "Trending");
		pagerAdapter.addFragment(new HotReposFragment(), "Repository");
		pager.setAdapter(pagerAdapter);
		tabs.setViewPager(pager);
		//tabs.setOnPageChangeListener(new PageListener());
		setTabsValue();
		pager.setCurrentItem(1);
		pager.setOffscreenPageLimit(2);
    }
    
	private void setTabsValue() {
		tabs.setShouldExpand(true);
		//tabs.setDividerColor(ContextCompat.getColor(this,R.color.contacts_theme_color));
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, dm));
		tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, dm));
		tabs.setTabPaddingLeftRight(0);
		tabs.setIndicatorColor(Color.WHITE);
		tabs.setSelectedTextColor(Color.WHITE);
		//tabs.setBackgroundColor(ContextCompat.getColor(this,R.color.contacts_theme_color));
		tabs.setTextColor(getResources().getColor(R.color.transparent_dark_gray));
		tabs.setTabBackground(0);
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
