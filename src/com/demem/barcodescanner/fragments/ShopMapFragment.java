package com.demem.barcodescanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demem.barcodescanner.R;
import com.demem.barcodescanner.base.BaseFragment;

public class ShopMapFragment extends BaseFragment {

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

    	View v = inflater.inflate(R.layout.map_layout, null);
		return v;
	}
}
