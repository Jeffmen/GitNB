package com.example.gitnb.app;

import java.util.ArrayList;
import java.util.List;

import com.astuetz.PagerSlidingTabStrip;
import com.example.gitnb.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;


public class MainActivity extends AppCompatActivity {

	private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private TabPagerAdapter pagerAdapter;
	private DisplayMetrics dm;
	private Toolbar toolbar;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("GitNB");
        setSupportActionBar(toolbar);
		dm = getResources().getDisplayMetrics();
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		
		pagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
		pagerAdapter.addFragment(new HotUserFragment(), "User");
		pager.setAdapter(pagerAdapter);
		tabs.setViewPager(pager);
		//tabs.setOnPageChangeListener(new PageListener());
		setTabsValue();
		pager.setCurrentItem(0);
    }
    
	private void setTabsValue() {
		tabs.setShouldExpand(true);
		tabs.setDividerColor(ContextCompat.getColor(this,R.color.contacts_theme_color));
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, dm));
		tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));
		tabs.setIndicatorColor(Color.WHITE);
		tabs.setSelectedTextColor(Color.WHITE);
		tabs.setBackgroundColor(ContextCompat.getColor(this,R.color.contacts_theme_color));
		tabs.setTextColor(ContextCompat.getColor(this,R.color.contacts_theme_text_color));
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
