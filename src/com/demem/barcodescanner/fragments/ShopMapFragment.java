package com.demem.barcodescanner.fragments;

import java.util.Vector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demem.barcodescanner.R;
import com.demem.barcodescanner.ShopContainer;
import com.demem.barcodescanner.activities.MainScreenActivity;
import com.demem.barcodescanner.base.BaseFragment;
import com.demem.barcodescanner.base.JsonParserBase;
import com.demem.barcodescanner.jsonparser.JsonItemListParser;
import com.demem.barcodescanner.jsonparser.JsonLocationParser;
import com.demem.barcodescanner.jsonparser.JsonShopListParser;
import com.demem.barcodescanner.utils.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShopMapFragment extends BaseFragment {

    protected JsonShopListParser jsonShopListParser = JsonShopListParser.getInstance();

    private GoogleMap map;
    private MapView mMapView;

    private final String GOOGLE_MAPS_API_URL = "http://maps.google.com/maps/api/geocode/json?address=";
    private final String GOOGLE_MAPS_API_DEFAULT_PARAMS = "&region=hy&sensor=false";

    private ProgressDialog mDialog;
    private GPSTracker gps;

    public ShopMapFragment() {
		// TODO Auto-generated constructor stub
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.map_layout, null);
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        map = mMapView.getMap();

        mDialog = new ProgressDialog(_context);
        mDialog.setMessage("Downloading data ...");
        mDialog.setCancelable(false);
        mDialog.show();

        jsonShopListParser.setOnJsonItemListParserListener(new JsonItemListParser.OnJsonParserListener() {

            @Override
            public void onJSONSet() {
                ((MainScreenActivity) getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                while(!jsonShopListParser.update()){
                    ((MainScreenActivity) getActivity()).runOnUiThread(new Runnable() {
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
        ((MainScreenActivity) getActivity()).setOnTabChangedListener(new MainScreenActivity.OnTabChangedListener() {
            @Override
            public void onTabChanged(int currentTab) {
                ((MainScreenActivity) getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addMarkers();
                    }
                });
            }
        });
        return v;
    }
    
    public void addMarkers()
    {
    	if(map != null) {
    		map.clear();
	        gps = new GPSTracker((Activity) _context);
	        if(gps.canGetLocation()){
	            double lat = gps.getLatitude();
	            double lng = gps.getLongitude();
	            map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))).setTitle("Me");

	            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
	            map.animateCamera(CameraUpdateFactory.zoomTo(14), 1000, null);
	        } else {
	            gps.showSettingsAlert();
	        }
	        Vector<ShopContainer> shopList = jsonShopListParser.getShopAddresses();
	        for(int i = 0; i < shopList.size(); ++i) {
	            createMarker(shopList.get(i).getAddress(), shopList.get(i).getShopName(), 0);
	        }
    	}
    }

    public void createMarker(final String address, final String title, final int zoom)
    {
        final JsonLocationParser jlp = new JsonLocationParser();
        jlp.setOnJsonItemListParserListener(new JsonParserBase.OnJsonParserListener() {
            @Override
            public void onJSONSet() {
                String latLng = jlp.getLatLng();
                final double lat = Double.parseDouble(latLng.substring(0, latLng.lastIndexOf(",")));
                final double lng = Double.parseDouble(latLng.substring(latLng.lastIndexOf(",") + 1, latLng.length()));
                ((MainScreenActivity) getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	if(map != null) {
	                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.dont_buy_it);
	                        bm = Bitmap.createScaledBitmap(bm, 100, 100, true);
	                        ShopMapFragment.this.map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
	                                .icon(BitmapDescriptorFactory.fromBitmap(bm))
	                                .anchor(0.5f, 0.5f)
	                                .title(title + " : " + address)
	                        );
	                        if(zoom > 0) {
	                            ShopMapFragment.this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
	                            ShopMapFragment.this.map.animateCamera(CameraUpdateFactory.zoomTo(zoom), 1000, null);
	                        }
                    	}
                    }
                });
            }
        });
        String requestUrl = GOOGLE_MAPS_API_URL + address + GOOGLE_MAPS_API_DEFAULT_PARAMS;
        requestUrl = requestUrl.replaceAll(" ", "%20");
        jlp.init(_context, requestUrl);
        jlp.update();
    }
}
