package com.demem.barcodescanner.activities;

import java.util.Vector;

import com.demem.barcodescanner.ItemConteiner;
import com.demem.barcodescanner.R;
import com.demem.barcodescanner.R.id;
import com.demem.barcodescanner.R.layout;
import com.demem.barcodescanner.fragments.CategoryListFragment;
import com.demem.barcodescanner.jsonparser.JsonItemListParser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends Activity {

    private TextView textView;
    private ListView listView;
    ListItemAdapter adapter;
    private JsonItemListParser jsonItemListParser = JsonItemListParser.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        setTitle("Search");
        textView = (TextView)findViewById(R.id.inputSearch);
        listView = (ListView)findViewById(R.id.itemsListView);

        adapter = new ListItemAdapter(this, jsonItemListParser.getAllItems());
        listView.setAdapter(adapter);

        textView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchActivity.this.adapter.getFilter().filter(s);
                listView.setAdapter(adapter);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {

                Intent intent = new Intent(SearchActivity.this, ItemPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(CategoryListFragment.ITEM_NAME_FLAG, ((ItemConteiner)adapter.getItem(arg2)).getItemName());
                intent.putExtra(CategoryListFragment.ITEM_IMAGE_FLAG, ((ItemConteiner)adapter.getItem(arg2)).getImagePath());
                getApplicationContext().startActivity(intent);
            }
        });
    }
}
