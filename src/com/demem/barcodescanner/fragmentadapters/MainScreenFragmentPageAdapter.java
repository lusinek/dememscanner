package com.demem.barcodescanner.fragmentadapters;

import com.demem.barcodescanner.base.BaseFragmentPageAdapter;
import com.demem.barcodescanner.fragments.CategoryListFragment;
import com.demem.barcodescanner.fragments.NewsFeedFragment;
import com.demem.barcodescanner.fragments.ShopMapFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class MainScreenFragmentPageAdapter extends BaseFragmentPageAdapter {

	public MainScreenFragmentPageAdapter(FragmentManager fm) {
		super(fm);
		PAGE_COUNT = 3;
	}

	@Override
	public Fragment getItem(int arg0) {
		Bundle data = new Bundle();
		switch(arg0){
			case 0:
				NewsFeedFragment newsFeedFragment = new NewsFeedFragment();
				newsFeedFragment.setContext(_context);
				data.putInt("current_page", arg0+1);
				newsFeedFragment.setArguments(data);
				return newsFeedFragment;
			case 1:
				CategoryListFragment categoryList = new CategoryListFragment();
				categoryList.setContext(_context);
				data.putInt("current_page", arg0+1);
				categoryList.setArguments(data);
				return categoryList;
			case 2:
				ShopMapFragment shopMap = new ShopMapFragment();
                shopMap.setContext(_context);
                data.putInt("current_page", arg0+1);
                shopMap.setArguments(data);
                return shopMap;
		}
		return null;
	}
}
