package com.demem.barcodescanner.activities;

import com.demem.barcodescanner.ItemConteiner;
import com.demem.barcodescanner.R;
import com.demem.barcodescanner.R.id;
import com.demem.barcodescanner.R.layout;
import com.demem.barcodescanner.base.BaseActivity;
import com.demem.barcodescanner.fragments.CategoryListFragment;
import com.demem.barcodescanner.jsonparser.JsonItemListParser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CategoryPageActivity extends BaseActivity {

    private ListView listView;
    private String categoryName;
    private ListItemAdapter adapter;
    private JsonItemListParser jsonItemListParser = JsonItemListParser.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_layout);
        Bundle bundle = getIntent().getExtras();
        categoryName = bundle.getString(CategoryListFragment.CATEGORY_NAME_FLAG);
        setTitle(categoryName);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.itemsListView);

        adapter = new ListItemAdapter(this,
                categoryName.compareTo("All") == 0 ? jsonItemListParser.getAllItems() : jsonItemListParser.getCategoryItems(categoryName));

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {

                Intent intent = new Intent(CategoryPageActivity.this, ItemPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(CategoryListFragment.ITEM_NAME_FLAG, ((ItemConteiner)listView.getItemAtPosition(arg2)).getItemName());
				intent.putExtra(CategoryListFragment.ITEM_IMAGE_FLAG, ((ItemConteiner)listView.getItemAtPosition(arg2)).getImagePath());
				getApplicationContext().startActivity(intent);
            }
        });
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
