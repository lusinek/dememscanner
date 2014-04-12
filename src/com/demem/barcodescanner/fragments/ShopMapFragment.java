package com.demem.barcodescanner.fragments;

import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demem.barcodescanner.JsonLocationParser;
import com.demem.barcodescanner.R;
import com.demem.barcodescanner.activities.ShopPageActivity;
import com.demem.barcodescanner.base.BaseFragment;
import com.demem.barcodescanner.base.JsonParserBase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShopMapFragment extends BaseFragment {

    private GoogleMap map;
    private final String GOOGLE_MAPS_API_URL = "http://maps.google.com/maps/api/geocode/json?address=";
    private final String GOOGLE_MAPS_API_DEFAULT_PARAMS = "&region=hy&sensor=false";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.map_layout, null);
        map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        ((ShopPageActivity) getActivity()).setOnDataPassedListener(new ShopPageActivity.OnDataPassedListener() {
            @Override
            public void onDataPassed(Vector<String> items) {
                map.clear();
                int zoom = items.size() > 1 ? 12 : 14;
                for(int i = 0; i < items.size(); ++i) {
                    createMarker(items.get(i), i == 0 ? zoom : 0);
                }
            }
        });
        return v;
    }

    public void createMarker(String address, final int zoom)
    {
        final JsonLocationParser jlp = new JsonLocationParser();
        jlp.setOnJsonItemListParserListener(new JsonParserBase.OnJsonParserListener() {
            @Override
            public void onJSONSet() {
                String latLng = jlp.getLatLng();
                final double lat = Double.parseDouble(latLng.substring(0, latLng.lastIndexOf(",")));
                final double lng = Double.parseDouble(latLng.substring(latLng.lastIndexOf(",") + 1, latLng.length()));
                ((ShopPageActivity) getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShopMapFragment.this.map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
                        if(zoom > 0) {
                            ShopMapFragment.this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
                            ShopMapFragment.this.map.animateCamera(CameraUpdateFactory.zoomTo(zoom), 1000, null);
                        }
                    }
                });
            }
        });
        String requestUrl = GOOGLE_MAPS_API_URL + address + GOOGLE_MAPS_API_DEFAULT_PARAMS;
        jlp.init(_context, requestUrl);
        jlp.update();
    }
}
