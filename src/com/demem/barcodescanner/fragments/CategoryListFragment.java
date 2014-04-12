package com.demem.barcodescanner.fragments;

import java.util.ArrayList;

import com.demem.barcodescanner.R;
import com.demem.barcodescanner.SoundPlayer;
import com.demem.barcodescanner.R.id;
import com.demem.barcodescanner.R.layout;
import com.demem.barcodescanner.activities.CategoryPageActivity;
import com.demem.barcodescanner.base.BaseFragment;
import com.demem.barcodescanner.jsonparser.JsonItemListParser;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryListFragment extends BaseFragment {

	protected JsonItemListParser jsonItemListParser = JsonItemListParser.getInstance();
	protected SoundPlayer soundPlayer = SoundPlayer.getInstance();

    public static final String CATEGORY_NAME_FLAG = "CATEGORY_NAME";
    public static final String ITEM_NAME_FLAG = "ITEM_NAME";
    public static final String ITEM_IMAGE_FLAG = "ITEM_IMAGE";

    private ArrayAdapter<String> adapter;
    private ListView listView = null;
    private ProgressDialog mDialog;

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

        listView = (ListView) v.findViewById(R.id.sListView);;

        jsonItemListParser.setOnJsonItemListParserListener(new JsonItemListParser.OnJsonParserListener() {

            @Override
            public void onJSONSet() {
                ArrayList<String> list = jsonItemListParser.getCategories();
                list.add(0, "All");
                adapter = new ArrayAdapter<String>(_context,
                        android.R.layout.simple_list_item_1, list);;
                        ((Activity) _context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	CategoryListFragment.this.listView.setAdapter(CategoryListFragment.this.adapter);
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
                while(!jsonItemListParser.update()){
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

                Intent intent = new Intent(_context, CategoryPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(CATEGORY_NAME_FLAG, (String)listView.getItemAtPosition(arg2));
                CategoryListFragment.this.startActivity(intent);
            }
        });
		return v;
	}
}