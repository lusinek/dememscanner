package com.demem.barcodescanner;

import com.demem.barcodescanner.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CategoryPageActivity extends BaseActivity {

    private ListView listView;
    private String categoryName;
    private ListItemAdapter adapter;
    private JsonParser jsonParser = JsonParser.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_layout);
        Bundle bundle = getIntent().getExtras();
        categoryName = bundle.getString(MainActivity.CATEGORY_NAME_FLAG);
        setTitle(categoryName);

        listView = (ListView)findViewById(R.id.itemsListView);

        adapter = new ListItemAdapter(this,
                categoryName.compareTo("All") == 0 ? jsonParser.getAllItems() : jsonParser.getCategoryItems(categoryName));

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {

                Intent intent = new Intent(CategoryPageActivity.this, ItemPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(MainActivity.ITEM_NAME_FLAG, ((ItemConteiner)listView.getItemAtPosition(arg2)).getItemName());
				intent.putExtra(MainActivity.ITEM_IMAGE_FLAG, ((ItemConteiner)listView.getItemAtPosition(arg2)).getImagePath());
				getApplicationContext().startActivity(intent);
            }
        });
    }
}
