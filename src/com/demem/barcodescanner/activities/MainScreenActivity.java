package com.demem.barcodescanner.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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

    private static final String NEWS_FEED_TAB = "News Feed";
    private static final String CATEGORIES_TAB = "Item Categories";
    private static final String SHOPS_TAB = "Shops";

    private final String PRODUCTS_JSON_FILENAME = "products.json";
    private final String PRODUCTS_JSON_URL = "http://hakob.info/auto/_products.json";
    private final String SHOPS_JSON_FILENAME = "shops.json";
    private final String SHOPS_JSON_URL = "http://hakob.info/auto/shops.json";
    private final String NEWS_JSON_FILENAME = "news.json";
    private final String NEWS_JSON_URL = "http://hakob.info/auto/news.json";

    private ActionBar bar;
    private ViewPager viewpager;

    public interface OnTabChangedListener {
        public abstract void onTabChanged(int currentTab);
    }

    OnTabChangedListener onTabChangedListener = null;

    public void setOnTabChangedListener(OnTabChangedListener listener)
    {
        onTabChangedListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_layout);

        viewpager = (ViewPager) findViewById(R.id.pager);

        bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        jsonItemListParser.init(MainScreenActivity.this, PRODUCTS_JSON_URL, PRODUCTS_JSON_FILENAME);
        jsonShopListParser.init(MainScreenActivity.this, SHOPS_JSON_URL, SHOPS_JSON_FILENAME);
        jsonNewsParser.init(MainScreenActivity.this, NEWS_JSON_URL, NEWS_JSON_FILENAME);
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
                int position = tab.getPosition();
                currentTabIndex = position;
                viewpager.setCurrentItem(tab.getPosition());
                if(onTabChangedListener != null) {
                    onTabChangedListener.onTabChanged(position);
                }
            }

            @Override
            public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
            }
        };

        // Add News Feed tab
        Tab newsFeedTab = bar.newTab();
        newsFeedTab.setText(NEWS_FEED_TAB);
        newsFeedTab.setTabListener(tabListener);
        bar.addTab(newsFeedTab);
        // Add Categories tab
        Tab categoryTab = bar.newTab();
        categoryTab.setText(CATEGORIES_TAB);
        categoryTab.setTabListener(tabListener);
        bar.addTab(categoryTab);
        // Add Shops tab
        Tab shopTab = bar.newTab();
        shopTab.setText(SHOPS_TAB);
        shopTab.setTabListener(tabListener);
        bar.addTab(shopTab);
    }
}
