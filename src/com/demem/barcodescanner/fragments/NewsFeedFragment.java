package com.demem.barcodescanner.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demem.barcodescanner.R;
import com.demem.barcodescanner.SoundPlayer;
import com.demem.barcodescanner.base.BaseFragment;
import com.demem.barcodescanner.jsonparser.JsonItemListParser;
import com.demem.barcodescanner.jsonparser.JsonNewsParser;
import com.demem.barcodescanner.utils.NewsContainer;

public class NewsFeedFragment extends BaseFragment{
	 
	private static String TAG = "com.demem.barcodescanner.fragments.NewsFeedFragment";

	protected JsonNewsParser jsonNewsParser = JsonNewsParser.getInstance();
	protected SoundPlayer soundPlayer = SoundPlayer.getInstance();

    private MyExpandableListAdapter mExpandableAdapter;
    private ExpandableListView mExpandableListView = null;
    private ProgressDialog mDialog;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.news_feed_layout, null);
		
		final Activity activity = getActivity();
		
		mDialog = new ProgressDialog(_context);
		mDialog.setMessage("Downloading data ...");
		mDialog.setCancelable(false);
		mDialog.show();
	
		mExpandableListView = (ExpandableListView) v.findViewById(R.id.newsListView);
        
        jsonNewsParser.setOnJsonItemListParserListener(new JsonItemListParser.OnJsonParserListener() {

            @Override
            public void onJSONSet() {
            	SparseArray<Group> groups = new SparseArray<Group>();
            	// Get news titles from the appropriate json file
            	ArrayList<String> listOfTitles = jsonNewsParser.getNews();
            	for(int i = 0; i < listOfTitles.size(); ++i)
            	{
                    String imagePath = "";
            		String title = listOfTitles.get(i);
            		Group group = new Group(title);
            		// Get news detail corresponding to the title and image path if present
            		try {
            			imagePath = jsonNewsParser.getNewsImageUrl(title);
            		}
            		catch (Exception e) {
            			Log.e(TAG, title + " does not have image.");
            		}
            		group.children.add(new NewsContainer(jsonNewsParser.getNewsContent(title), imagePath));
            		groups.append(i, group);
            	}
            	
            	mExpandableAdapter = new MyExpandableListAdapter(activity, groups);
                
                ((Activity) _context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	mExpandableListView.setAdapter(mExpandableAdapter);
                        if(mDialog.isShowing()) {
                            mDialog.dismiss();
                       }
                    }
                });
            }
        });
        
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while(!jsonNewsParser.update()){
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
        
        return v;
    }
    
	public class Group {
		public String string;
		public final List<NewsContainer> children = new ArrayList<NewsContainer>();

		public Group(String string) {
			this.string = string;
			}
		} 
	
	public class MyExpandableListAdapter extends BaseExpandableListAdapter {

		  private final SparseArray<Group> groups;
		  public LayoutInflater inflater;
		  public Activity activity;

		  public MyExpandableListAdapter(Activity act, SparseArray<Group> groups) {
		    activity = act;
		    this.groups = groups;
		    inflater = act.getLayoutInflater();
		  }

		  @Override
		  public Object getChild(int groupPosition, int childPosition) {
		    return groups.get(groupPosition).children.get(childPosition);
		  }

		  @Override
		  public long getChildId(int groupPosition, int childPosition) {
		    return 0;
		  }

		  @Override
		  public View getChildView(int groupPosition, final int childPosition,
		      boolean isLastChild, View convertView, ViewGroup parent) {
		    final NewsContainer child = (NewsContainer) getChild(groupPosition, childPosition);
		    TextView text = null;
		    if (convertView == null) {
		      convertView = inflater.inflate(R.layout.news_feed_item_details, null);
		    }
		    text = (TextView) convertView.findViewById(R.id.newsFeedChildContent);
		    text.setText(child.getNewsContent());
		    
		    String newsImageUrl = child.getImagePath();
		    if(newsImageUrl != "") {
		    	ImageView imageView = (ImageView) convertView.findViewById(R.id.newsFeedChildImage);
		    	Drawable d = Drawable.createFromPath(newsImageUrl);
		    	imageView.setImageDrawable(d);
		    }
		    
		    convertView.setOnClickListener(new OnClickListener() {
		      @Override
		      public void onClick(View v) {
		        Toast.makeText(activity, child.getNewsContent(),
		            Toast.LENGTH_SHORT).show();
		      }
		    });
		    return convertView;
		  }

		  @Override
		  public int getChildrenCount(int groupPosition) {
		    return groups.get(groupPosition).children.size();
		  }

		  @Override
		  public Object getGroup(int groupPosition) {
		    return groups.get(groupPosition);
		  }

		  @Override
		  public int getGroupCount() {
		    return groups.size();
		  }

		  @Override
		  public void onGroupCollapsed(int groupPosition) {
		    super.onGroupCollapsed(groupPosition);
		  }

		  @Override
		  public void onGroupExpanded(int groupPosition) {
		    super.onGroupExpanded(groupPosition);
		  }

		  @Override
		  public long getGroupId(int groupPosition) {
		    return 0;
		  }

		  @Override
		  public View getGroupView(int groupPosition, boolean isExpanded,
		      View convertView, ViewGroup parent) {
		    if (convertView == null) {
		      convertView = inflater.inflate(R.layout.news_feed_item_group, null);
		    }
		    Group group = (Group) getGroup(groupPosition);
		    ((CheckedTextView) convertView).setText(group.string);
		    ((CheckedTextView) convertView).setChecked(isExpanded);
		    return convertView;
		  }

		  @Override
		  public boolean hasStableIds() {
		    return false;
		  }

		  @Override
		  public boolean isChildSelectable(int groupPosition, int childPosition) {
		    return false;
		  }
		} 
}