package com.demem.barcodescanner;

import com.demem.barcodescanner.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class BaseActivity extends Activity {

	protected JsonParser jsonParser = JsonParser.getInstance();
	protected SoundPlayer soundPlayer = SoundPlayer.getInstance();

	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.bar_scan_item:
        {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, 0);
            return true;
        }
        case R.id.qr_scan_item:
        {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
            return true;
        }
        case R.id.action_search:
        {
        	Intent intent = new Intent(this, SearchActivity.class);
        	startActivityForResult(intent, 0);
        	return true;
        }
        case R.id.share_item:
        {
        	Intent shareIntent = new Intent();
        	shareIntent.setAction(Intent.ACTION_SEND);
        	shareIntent.setType("text/plain");
        	shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this cool app");
        	startActivity(Intent.createChooser(shareIntent, "Share with friends"));
        }
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                String contents = intent.getStringExtra("SCAN_RESULT");
                //String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                boolean res = jsonParser.itemInList(contents);
                if(res) {
                	//Toast.makeText(this, "Don't buy this", Toast.LENGTH_SHORT).show();
                	soundPlayer.playSound(SoundPlayer.DECLINE, 1f);

                	final Dialog dialog = new Dialog(this);
                	dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
                	dialog.setContentView(R.layout.dialog_layout);
                	dialog.setTitle("Don't buy this");

                	Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);
                	dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
                	ImageView imageView = (ImageView) dialog.findViewById(R.id.arImageView);
                	imageView.setImageResource(R.drawable.dont_buy_it);
                	dialog.show();
                } else {
                	//Toast.makeText(this, "You can buy this", Toast.LENGTH_SHORT).show();
                	soundPlayer.playSound(SoundPlayer.ACCEPT, 1f);
                	final Dialog dialog = new Dialog(this);
                	dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
                	dialog.setContentView(R.layout.dialog_layout);
                	dialog.setTitle("You can buy this");

                	Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);
                	dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
                	ImageView imageView = (ImageView) dialog.findViewById(R.id.arImageView);
                	imageView.setImageResource(R.drawable.buy_it);
                	dialog.show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("App","Scan unsuccessful");
            }
        }
    }
}
