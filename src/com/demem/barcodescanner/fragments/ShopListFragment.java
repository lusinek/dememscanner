package com.demem.barcodescanner.fragments;

import java.util.ArrayList;

import com.demem.barcodescanner.JsonItemListParser;
import com.demem.barcodescanner.JsonShopListParser;
import com.demem.barcodescanner.R;
import com.demem.barcodescanner.activities.ShopPageActivity;
import com.demem.barcodescanner.base.BaseFragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ShopListFragment extends BaseFragment {

	protected JsonShopListParser jsonShopListParser = JsonShopListParser.getInstance();

	public static final String SHOP_NAME_FLAG = "SHOP_NAME";

	private ListView listView;
	private ProgressDialog mDialog;
	private ArrayAdapter<String> adapter;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

    	View v = inflater.inflate(R.layout.list_view_layout, null);
		mDialog = new ProgressDialog(_context);
        mDialog.setMessage("Downloading data ...");
        mDialog.setCancelable(false);
        mDialog.show();

        listView = (ListView) v.findViewById(R.id.categoryListView);

        jsonShopListParser.setOnJsonItemListParserListener(new JsonItemListParser.OnJsonParserListener() {

            @Override
            public void onJSONSet() {
                ArrayList<String> list = jsonShopListParser.getShops();
                adapter = new ArrayAdapter<String>(_context,
                        android.R.layout.simple_list_item_1, list);;
                        ((Activity) _context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	ShopListFragment.this.listView.setAdapter(ShopListFragment.this.adapter);
                        if(mDialog.isShowing()) {
                            mDialog.hide();
                        }
                    }
                });
            }
        });
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while(!jsonShopListParser.update()){
                	((Activity) _context).runOnUiThread(new Runnable() {
                        public void run() {
                            mDialog.setMessage("Downloading data ... \nConnect the internet");
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {

                Intent intent = new Intent(_context, ShopPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(SHOP_NAME_FLAG, (String)listView.getItemAtPosition(arg2));
                ShopListFragment.this.startActivity(intent);
            }
        });
		return v;
	}
}
