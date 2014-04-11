package com.demem.barcodescanner.fragmentadapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.demem.barcodescanner.base.BaseFragmentPageAdapter;
import com.demem.barcodescanner.base.JsonParserBase.OnJsonParserListener;
import com.demem.barcodescanner.fragments.ShopAddressesFragment;
import com.demem.barcodescanner.fragments.ShopMapFragment;
import com.demem.barcodescanner.fragments.ShopAddressesFragment.OnItemClickListener;

public class ShopScreenFragmentPageAdapter extends BaseFragmentPageAdapter {

    public ShopScreenFragmentPageAdapter(FragmentManager fm) {
        super(fm);
        PAGE_COUNT = 2;
    }

    public interface OnItemClickListener {
        public abstract void onItemClicked(String item);
    }

    OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        onItemClickListener = listener;
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
                shopAddresses.setOnItemClickListener(new ShopAddressesFragment.OnItemClickListener() {
                    @Override
                    public void onItemClicked(String item) {
                        if(onItemClickListener != null) {
                            onItemClickListener.onItemClicked(item);
                        }
                    }
                });
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
