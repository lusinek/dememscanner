package com.demem.barcodescanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.demem.barcodescanner.base.JsonParserBase;

import android.content.Context;

public class JsonShopListParser extends JsonParserBase {

    public static final JsonShopListParser instance = new JsonShopListParser();

    public final String SHOP_ARRAY_KEY = "shopList";
    public final String SHOP_NAME_KEY = "shopName";
    public final String ADDRESS_LIST_KEY = "addressList";

    private JSONArray jsonArray;

    public static JsonShopListParser getInstance()
    {
        return instance;
    }

    private JsonShopListParser() {
    }

    @Override
	protected void jsonDataRead(String json) {
    	try {
            JSONObject jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray(SHOP_ARRAY_KEY);
            jsonSet = true;
            if(onJsonParserListener != null) {
                onJsonParserListener.onJSONSet();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
	}

    public ArrayList<String> getShops()
    {
        ArrayList<String> result = new ArrayList<String>();
        if(jsonSet) {
            for(int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject item = jsonArray.getJSONObject(i);
                    result.add(item.getString(SHOP_NAME_KEY));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public Vector<String> getShopAddresses(String shopName)
    {
        Vector<String> result = new Vector<String>();
        if(jsonSet) {
            for(int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if(object.getString(SHOP_NAME_KEY).compareTo(shopName) == 0) {
                        JSONArray itemsArray = object.getJSONArray(ADDRESS_LIST_KEY);
                        for(int j = 0; j < itemsArray.length(); ++j) {
                            result.add(itemsArray.getString(j));
                        }
                        return result;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

	@Override
	protected void jsonDataDownloaded(String json) {
	}
}
