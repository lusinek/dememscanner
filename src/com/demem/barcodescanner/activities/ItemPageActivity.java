package com.demem.barcodescanner.activities;

import com.demem.barcodescanner.R;
import com.demem.barcodescanner.R.id;
import com.demem.barcodescanner.R.layout;
import com.demem.barcodescanner.fragments.CategoryListFragment;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
	}
}
