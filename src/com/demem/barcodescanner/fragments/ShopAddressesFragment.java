package com.demem.barcodescanner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.demem.barcodescanner.R;
import com.demem.barcodescanner.activities.ShopPageActivity;
import com.demem.barcodescanner.base.BaseFragment;
import com.demem.barcodescanner.jsonparser.JsonShopListParser;
import com.demem.barcodescanner.utils.JsonManager.OnJsonManagerListener;

public class ShopAddressesFragment extends BaseFragment {

    protected JsonShopListParser jsonShopListParser = JsonShopListParser.getInstance();

    private ListView listView;
    private ArrayAdapter<String> adapter;

    public interface OnItemClickListener {
        public abstract void onItemClicked(String item);
    }

    OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        onItemClickListener = listener;
    }

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClicked((String)listView.getItemAtPosition(arg2));
                }
            }
        });
        return v;
    }
}
