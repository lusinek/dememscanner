package com.demem.barcodescanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.demem.barcodescanner.base.JsonParserBase;
import com.demem.barcodescanner.utils.ImageManager;

public class JsonLocationParser extends JsonParserBase {

    public final String RESULTS_KEY = "results";
    public final String ADDRESS_COMPONENTS_KEY = "address_components";
    public final String FORMATTED_ADDRESS_KEY = "formatted_address";
    public final String GEOMETRY_KEY = "geometry";
    public final String LOCATION_KEY = "location";
    public final String VIEWPORT_KEY = "viewport";
    public final String NORTHEAST_KEY = "northeast";
    public final String SOUTHWEST_KEY = "southwest";
    public final String LAT_KEY = "lat";
    public final String LNG_KEY = "lng";
    public final String STATUS_KEY = "status";

    private JSONArray jsonArray;

    @Override
    protected void jsonDataRead(String json) {
    }

    @Override
    protected void jsonDataDownloaded(String json) {
    	Log.d("json downloaded", "@@@@@@");
        try {
        	Log.d("json downloaded 1", "@@@@@@");
            JSONObject jsonObject = new JSONObject(json);
            Log.d("json downloaded 2", "@@@@@@");
            String status = jsonObject.getString(STATUS_KEY);
            Log.d("json downloaded 3", "@@@@@@");
            if(status.compareTo("OK") == 0) {
            	Log.d("json downloaded 4", "@@@@@@");
                jsonArray = jsonObject.getJSONArray(RESULTS_KEY);
                Log.d("json downloaded 5", "@@@@@@");
                jsonSet = true;
                Log.d("json downloaded 6", "@@@@@@");
                if(onJsonParserListener != null) {
                	Log.d("json downloaded 7", "@@@@@@");
                    onJsonParserListener.onJSONSet();
                }
            }
            Log.d("json downloaded 8", "@@@@@@");
        } catch (JSONException e) {
        	Log.d("json downloaded 9", "@@@@@@");
            e.printStackTrace();
        }
    }

    public String getLatLng()
    {
        String result = "";
        if(jsonSet) {
            try {
                JSONObject location = jsonArray.getJSONObject(0).getJSONObject(GEOMETRY_KEY).getJSONObject(LOCATION_KEY);
                result = location.getString(LAT_KEY) + "," + location.getString(LNG_KEY); 
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
