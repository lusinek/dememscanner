package com.demem.barcodescanner.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.demem.barcodescanner.ExtendedMapView;
import com.demem.barcodescanner.R;
import com.demem.barcodescanner.ExtendedMapView.OnDataPassedListener;
import com.demem.barcodescanner.fragmentadapters.ShopScreenFragmentPageAdapter;
import com.demem.barcodescanner.fragments.ShopListFragment;
import com.demem.barcodescanner.fragments.ShopMapFragment;

@SuppressLint("NewApi")
public class ShopPageActivity extends FragmentActivity {

    private static final String ADDRESSES_TAB = "Addresses";
    private static final String MAP_TAB = "Map";

    private ActionBar bar;
    private ViewPager viewpager;

    public interface OnDataPassedListener {
        public abstract void onDataPassed(Vector<String> items);
    }

    OnDataPassedListener onDataPassedListener = null;

    public void setOnDataPassedListener(OnDataPassedListener listener)
    {
        onDataPassedListener = listener;
    }

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
        fragmentPagerAdapter.setOnItemClickListener(new ShopScreenFragmentPageAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(String item) {
                viewpager.setCurrentItem(1);
                if(onDataPassedListener != null)
                {
                    Vector<String> data = new Vector<String>();
                    data.add(item);
                    onDataPassedListener.onDataPassed(data);
                }
            }
        });

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