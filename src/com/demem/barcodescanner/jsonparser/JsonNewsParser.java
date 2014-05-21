package com.demem.barcodescanner.jsonparser;

import java.util.ArrayList;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.demem.barcodescanner.ShopContainer;
import com.demem.barcodescanner.base.JsonParserBase;

public class JsonNewsParser extends JsonParserBase {

    public static final JsonNewsParser instance = new JsonNewsParser();

    public final String NEWS_ARRAY_KEY = "newsFeed";
    public final String NEWS_TITLE_KEY = "title";
    public final String NEWS_CONTENT_KEY = "content";

    private JSONArray jsonArray;

    public static JsonNewsParser getInstance()
    {
        return instance;
    }

    private JsonNewsParser() {
    }

    @Override
    protected void jsonDataRead(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray(NEWS_ARRAY_KEY);
            jsonSet = true;
            if(onJsonParserListener != null) {
                onJsonParserListener.onJSONSet();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getNews()
    {
        ArrayList<String> result = new ArrayList<String>();
        if(jsonSet) {
            for(int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject item = jsonArray.getJSONObject(i);
                    result.add(item.getString(NEWS_TITLE_KEY));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public String getNewsContents()
    {
        return getNewsContent("");
    }

    public String getNewsContent(String newsTitle)
    {
    	String result = "";
        if(jsonSet) {
            for(int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String nTitle = object.getString(NEWS_TITLE_KEY);
                    if(nTitle.compareTo(newsTitle) == 0 || newsTitle.compareTo("") == 0) {
                    		result = object.getString(NEWS_CONTENT_KEY);
                        }
                    }
                catch (JSONException e) {
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
