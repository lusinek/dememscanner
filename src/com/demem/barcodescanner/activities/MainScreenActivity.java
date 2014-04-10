package com.demem.barcodescanner.activities;

import java.util.ArrayList;
import java.util.List;

import com.demem.barcodescanner.R;
import com.demem.barcodescanner.R.id;
import com.demem.barcodescanner.R.layout;
import com.demem.barcodescanner.base.BaseActivity;
import com.demem.barcodescanner.fragmentadapters.MainScreenFragmentPageAdapter;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

@SuppressLint("NewApi")
public class MainScreenActivity extends BaseActivity {

	private static final String CATEGORIES_TAB = "Item Categories";
	private static final String SHOPS_TAB = "Shops";

	private final String PRODUCTS_JSON_FILENAME = "products.json";
	private final String PRODUCTS_JSON_URL = "http://hakob.info/auto/_products.json";
	private final String SHOPS_JSON_FILENAME = "shops.json";
	private final String SHOPS_JSON_URL = "http://hakob.info/auto/shops.json";

    List<Fragment> fragList = new ArrayList<Fragment>();

    private ActionBar bar;
    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);

        viewpager = (ViewPager) findViewById(R.id.pager);

        bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        jsonItemListParser.init(MainScreenActivity.this, PRODUCTS_JSON_FILENAME, PRODUCTS_JSON_URL);
        jsonShopListParser.init(MainScreenActivity.this, SHOPS_JSON_FILENAME, SHOPS_JSON_URL);
        soundPlayer.loadSounds(MainScreenActivity.this);

        FragmentManager fm = getSupportFragmentManager();
        final MainScreenFragmentPageAdapter fragmentPagerAdapter = new MainScreenFragmentPageAdapter(fm);
        fragmentPagerAdapter.setContext(MainScreenActivity.this);

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
        categoryTab.setText(CATEGORIES_TAB);
        categoryTab.setTabListener(tabListener);
        bar.addTab(categoryTab);
        Tab shopTab = bar.newTab();
        shopTab.setText(SHOPS_TAB);
        shopTab.setTabListener(tabListener);
        bar.addTab(shopTab);
    }
}
