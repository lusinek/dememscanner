package com.demem.barcodescanner.activities;

import com.demem.barcodescanner.R;
import com.demem.barcodescanner.R.id;
import com.demem.barcodescanner.R.layout;
import com.demem.barcodescanner.fragments.CategoryListFragment;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ImageView;

public class ItemPageActivity extends Activity {

	private String itemName;
	private String itemImageUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_layout);
		Bundle bundle = getIntent().getExtras();
		itemName = bundle.getString(CategoryListFragment.ITEM_NAME_FLAG);
		itemImageUrl = bundle.getString(CategoryListFragment.ITEM_IMAGE_FLAG);
		ImageView itemImageView = (ImageView) findViewById(R.id.itemImageView);
		itemImageView.setImageBitmap(BitmapFactory.decodeFile(itemImageUrl));
		setTitle(itemName);
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
