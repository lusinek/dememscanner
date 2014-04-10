package com.demem.barcodescanner.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.demem.barcodescanner.JsonShopListParser;
import com.demem.barcodescanner.R;
import com.demem.barcodescanner.base.BaseFragment;

public class ShopAddressesFragment extends BaseFragment {

    protected JsonShopListParser jsonShopListParser = JsonShopListParser.getInstance();

    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.list_view_layout, null);
        listView = (ListView) v.findViewById(R.id.sListView);

        adapter = new ArrayAdapter<String>(_context, android.R.layout.simple_list_item_1,
                jsonShopListParser.getShopAddresses((String)getActivity().getTitle()));

        listView.setAdapter(adapter);
        return v;
    }
}
