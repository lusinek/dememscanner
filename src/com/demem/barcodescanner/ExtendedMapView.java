package com.demem.barcodescanner;

import java.util.Vector;

import android.content.Context;
import android.view.View;

public abstract class ExtendedMapView extends View {

    public interface OnDataPassedListener {
        public abstract void onDataPassed(Vector<String> items);
    }

    OnDataPassedListener onDataPassedListener = null;

    public void setOnDataPassedListener(OnDataPassedListener listener)
    {
        onDataPassedListener = listener;
    }

    public ExtendedMapView(Context context) {
        super(context);
    }

	abstract public void addMarkers(Vector<String> items);
}
