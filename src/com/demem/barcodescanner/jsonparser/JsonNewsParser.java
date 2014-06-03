package com.demem.barcodescanner.jsonparser;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.demem.barcodescanner.base.JsonParserBase;
import com.demem.barcodescanner.utils.ImageManager;


public class JsonNewsParser extends JsonParserBase {

    public static final JsonNewsParser instance = new JsonNewsParser();

    public final String NEWS_ARRAY_KEY = "newsFeed";
    public final String NEWS_TITLE_KEY = "title";
    public final String NEWS_CONTENT_KEY = "content";
    public final String NEWS_IMAGE_HASH_KEY = "imageHash";
    public final String NEWS_IMAGE_URL_KEY = "imageUrl";

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
            checkImages();
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

    public String getNewsImageUrl(String newsTitle)
    {
    	String result = null;
        if(jsonSet) {
            for(int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String nTitle = object.getString(NEWS_TITLE_KEY);
                    if(nTitle.compareTo(newsTitle) == 0 || newsTitle.compareTo("") == 0) {
                    	result = getPathFromUrl(object.getString(NEWS_IMAGE_URL_KEY));           
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

    public void checkImages()
    {
        final HashMap<String, String> imageInfoMap = new HashMap<String, String>();
        if(jsonSet) {
            for(int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                	imageInfoMap.put(object.getString(NEWS_IMAGE_URL_KEY), object.getString(NEWS_IMAGE_HASH_KEY));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            final ImageManager im = new ImageManager(_context);
            Thread imageCheckThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    im.checkImages(imageInfoMap);
                }
            });
            imageCheckThread.start();
        }
    }
    
    public String getPathFromUrl(String url)
    {
        return _context.getFilesDir() + "/" + ImageManager.IMAGE_FOLDER_NAME + "/" + (new ImageManager(_context)).getFileNameFromUrl(url);
    }
    
}
