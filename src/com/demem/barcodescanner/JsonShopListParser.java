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
    public final String SHOP_NAME_KYE = "shopName";

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
                    result.add(item.getString(SHOP_NAME_KYE));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
