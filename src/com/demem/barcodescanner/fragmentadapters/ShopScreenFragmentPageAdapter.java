package com.demem.barcodescanner.fragmentadapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.demem.barcodescanner.base.BaseFragmentPageAdapter;
import com.demem.barcodescanner.fragments.ShopAddressesFragment;
import com.demem.barcodescanner.fragments.ShopMapFragment;

public class ShopScreenFragmentPageAdapter extends BaseFragmentPageAdapter {

    public ShopScreenFragmentPageAdapter(FragmentManager fm) {
        super(fm);
        PAGE_COUNT = 2;
    }

    @Override
    public Fragment getItem(int arg0) {
        Bundle data = new Bundle();
        switch(arg0){
            case 0:
            	ShopAddressesFragment shopAddresses = new ShopAddressesFragment();
            	shopAddresses.setContext(_context);
                data.putInt("current_page", arg0+1);
                shopAddresses.setArguments(data);
                return shopAddresses;
            case 1:
            	ShopMapFragment shopMap = new ShopMapFragment();
            	shopMap.setContext(_context);
                data.putInt("current_page", arg0+1);
                shopMap.setArguments(data);
                return shopMap;
        }
        return null;
    }
}
