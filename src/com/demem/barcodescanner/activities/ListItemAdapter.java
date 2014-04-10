package com.demem.barcodescanner.activities;

import java.util.Vector;

import com.demem.barcodescanner.ItemConteiner;
import com.demem.barcodescanner.R;
import com.demem.barcodescanner.R.id;
import com.demem.barcodescanner.R.layout;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class ListItemAdapter extends ArrayAdapter<ItemConteiner> implements Filterable {

	private Context _context;
    private Vector<ItemConteiner> values = null;
    private Vector<ItemConteiner> mOriginalValues = null;

    public ListItemAdapter(Context context, Vector<ItemConteiner> values) {
        super(context, R.layout.list_item, values);
        this._context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.listItemText);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listItemIcon);
        ItemConteiner item = values.get(position);

        textView.setText(item.getItemName());
    	imageView.setImageBitmap(BitmapFactory.decodeFile(item.getImagePath()));
        return rowView;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public ItemConteiner getItem(int position) {
        return values.elementAt(position);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                values = (Vector<ItemConteiner>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                Vector<ItemConteiner> FilteredArrList = new Vector<ItemConteiner>();
                if (mOriginalValues == null) {
                    mOriginalValues = new Vector<ItemConteiner>(values);
                }
                if (constraint == null || constraint.length() == 0) {
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getItemName();
                        if (data.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(mOriginalValues.get(i));
                        }
                    }
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
