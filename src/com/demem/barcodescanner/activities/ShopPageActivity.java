package com.demem.barcodescanner.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.demem.barcodescanner.R;
import com.demem.barcodescanner.fragmentadapters.ShopScreenFragmentPageAdapter;
import com.demem.barcodescanner.fragments.ShopListFragment;

@SuppressLint("NewApi")
public class ShopPageActivity extends FragmentActivity {

	private static final String ADDRESSES_TAB = "Addresses";
	private static final String MAP_TAB = "Map";

    List<Fragment> fragList = new ArrayList<Fragment>();

    private ActionBar bar;
    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);

        Bundle bundle = getIntent().getExtras();
        setTitle(bundle.getString(ShopListFragment.SHOP_NAME_FLAG));
        viewpager = (ViewPager) findViewById(R.id.pager);

        bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        FragmentManager fm = getSupportFragmentManager();
        final ShopScreenFragmentPageAdapter fragmentPagerAdapter = new ShopScreenFragmentPageAdapter(fm);
        fragmentPagerAdapter.setContext(ShopPageActivity.this);

        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bar.setSelectedNavigationItem(position);                
            }            
        };

        viewpager.setOnPageChangeListener(pageChangeListener);

        viewpager.setAdapter(fragmentPagerAdapter);
        bar.setDisplayShowTitleEnabled(true);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabReselected(Tab arg0, android.app.FragmentTransaction arg1) {
            }

            @Override
            public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
            	viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
            }
        };

        Tab categoryTab = bar.newTab();
        categoryTab.setText(ADDRESSES_TAB);
        categoryTab.setTabListener(tabListener);
        bar.addTab(categoryTab);
        Tab shopTab = bar.newTab();
        shopTab.setText(MAP_TAB);
        shopTab.setTabListener(tabListener);
        bar.addTab(shopTab);
    }
}