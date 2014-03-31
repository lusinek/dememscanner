package com.demem.barcodescanner;

import java.util.ArrayList;

import com.demem.barcodescanner.R;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends BaseActivity {

    public static final String CATEGORY_NAME_FLAG = "CATEGORY_NAME";
    public static final String ITEM_NAME_FLAG = "ITEM_NAME";
    public static final String ITEM_IMAGE_FLAG = "ITEM_IMAGE";

    private ArrayAdapter<String> adapter;
    private ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        soundPlayer.loadSounds(this);

        jsonParser.init(MainActivity.this);
        listView = (ListView)findViewById(R.id.categoryListView);

        jsonParser.setOnJsonParserListener(new JsonParser.OnJsonParserListener() {

            @Override
            public void onJSONSet() {
                ArrayList<String> list = jsonParser.getCategories();
                list.add(0, "All");
                adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1, list);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.listView.setAdapter(MainActivity.this.adapter);
                    }
                });
            }
        });
        jsonParser.update();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {

                Intent intent = new Intent(MainActivity.this, CategoryPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(CATEGORY_NAME_FLAG, (String)listView.getItemAtPosition(arg2));
                MainActivity.this.startActivity(intent);
            }
        });
//        ProgressDialog mDialog = new ProgressDialog(this);
//        mDialog.setMessage("Please wait...");
//        mDialog.setCancelable(true);
//        mDialog.show();
    }
}