package com.demem.barcodescanner.activities;

import java.util.Vector;

import com.demem.barcodescanner.ItemConteiner;
import com.demem.barcodescanner.R;
import com.demem.barcodescanner.R.id;
import com.demem.barcodescanner.R.layout;
import com.demem.barcodescanner.ShopContainer;
import com.demem.barcodescanner.base.BaseActivity;
import com.demem.barcodescanner.base.BaseFragment;
import com.demem.barcodescanner.fragments.CategoryListFragment;
import com.demem.barcodescanner.jsonparser.JsonItemListParser;
import com.demem.barcodescanner.jsonparser.JsonShopListParser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends Activity {

    private JsonItemListParser jsonItemListParser = JsonItemListParser.getInstance();
    private JsonShopListParser jsonShopListParser = JsonShopListParser.getInstance();

    private TextView textView;
    private ListView listView;
    private int tabIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        setTitle("Search");
        Intent intent = getIntent();
        tabIndex = (Integer) intent.getIntExtra(BaseActivity.TAB_INDEX_KEY, 0);

        textView = (TextView) findViewById(R.id.inputSearch);
        listView = (ListView) findViewById(R.id.itemsListView);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        if(tabIndex == 0) {
            final ListItemAdapter adapter = new ListItemAdapter(this, jsonItemListParser.getAllItems());
            listView.setAdapter(adapter);

            textView.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
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
        } else if(tabIndex == 1) {
            Vector<ShopContainer> sc = jsonShopListParser.getShopAddresses();
            Vector<String> sh = new Vector<String>();
            for(int i = 0; i < sc.size(); ++i) {
                sh.add(sc.get(i).getShopName() + " : " + sc.get(i).getAddress());
            }
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sh);
            listView.setAdapter(adapter);

            textView.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
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
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case android.R.id.home:
            this.finish();
            return true;
     }
        return super.onOptionsItemSelected(item);
    }
}
