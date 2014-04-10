package com.demem.barcodescanner.fragmentadapters;

import com.demem.barcodescanner.base.BaseFragmentPageAdapter;
import com.demem.barcodescanner.fragments.CategoryListFragment;
import com.demem.barcodescanner.fragments.ShopListFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class MainScreenFragmentPageAdapter extends BaseFragmentPageAdapter {

	public MainScreenFragmentPageAdapter(FragmentManager fm) {
		super(fm);
		PAGE_COUNT = 2;
	}

	@Override
	public Fragment getItem(int arg0) {
		Bundle data = new Bundle();
		switch(arg0){
			case 0:
				CategoryListFragment categoryList = new CategoryListFragment();
				categoryList.setContext(_context);
				data.putInt("current_page", arg0+1);
				categoryList.setArguments(data);
				return categoryList;
			case 1:
				ShopListFragment shopList = new ShopListFragment();
				shopList.setContext(_context);
				data.putInt("current_page", arg0+1);
				shopList.setArguments(data);
				return shopList;
		}
		return null;
	}
}
