package com.demem.barcodescanner.base;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public abstract class BaseFragmentPageAdapter extends FragmentPagerAdapter {

	protected int PAGE_COUNT;
	protected Context _context;

	public BaseFragmentPageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

	public void setContext(Context context) {
		_context = context;
	}
}
