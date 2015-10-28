package com.example.gitnb.module;

import java.util.ArrayList;
import java.util.List;

import com.astuetz.PagerSlidingTabStrip;
import com.example.gitnb.R;
import com.example.gitnb.module.user.HotUserFragment;

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
import android.view.View;
import android.widget.TextView;


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
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setText("GitNB");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_github_white_70);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
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
		tabs.setIndicatorColor(Color.parseColor("#4876FF"));
		tabs.setSelectedTextColor(Color.parseColor("#4876FF"));
		//tabs.setBackgroundColor(ContextCompat.getColor(this,R.color.contacts_theme_color));
		tabs.setTextColor(Color.BLACK);
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
