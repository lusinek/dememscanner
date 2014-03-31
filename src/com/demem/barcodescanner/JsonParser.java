package com.demem.barcodescanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class JsonParser {

    public static final JsonParser instance = new JsonParser();

    public final String CATEGORY_ARRAY_KEY = "categoryList";
    public final String CATEGORY_NAME_KEY = "categoryName";
    public final String ITEM_LIST_KEY = "itemsList";
    public final String ITEM_NAME_KEY = "itemName";
    public final String ITEM_BAR_CODE_ARRAY_KEY = "itemBarCodes";
    public final String ITEM_IMAGE_HASH_KEY = "imageHash";
    public final String ITEM_IMAGE_URL_KEY = "imageUrl";

    private Context _context;
    private JsonManager jsonManager;
    private JSONArray jsonArray;
    private boolean jsonSet = false;

    public interface OnJsonParserListener {
        public void onJSONSet();
    }

    OnJsonParserListener onJsonParserListener = null;

    public void setOnJsonParserListener(OnJsonParserListener listener)
    {
        onJsonParserListener = listener;
    }

    public void init(Context context)
    {
        this._context = context;
        jsonManager = new JsonManager(this._context);
        jsonManager.setOnJsonManagerListener(new JsonManager.OnJsonManagerListener() {
            @Override
            public void onJsonDataRead(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    jsonArray = jsonObject.getJSONArray(CATEGORY_ARRAY_KEY);
                    jsonSet = true;
                    chaecImages();
                    if(onJsonParserListener != null) {
                        onJsonParserListener.onJSONSet();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean update()
    {
        return jsonManager.update();
    }

    public static JsonParser getInstance()
    {
        return instance;
    }

    private JsonParser() {
    }

    public boolean itemInList(String itemCode)
    {
        if(jsonSet) {
            for(int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONArray itemArray = jsonArray.getJSONObject(i).getJSONArray(ITEM_LIST_KEY);
                    for(int j = 0; j < itemArray.length(); ++j) {
                        JSONObject itemObject = itemArray.getJSONObject(j);
                        JSONArray barCodeArray = itemObject.getJSONArray(ITEM_BAR_CODE_ARRAY_KEY);
                        for(int k = 0; k < barCodeArray.length(); ++k) {
                            String code = barCodeArray.getString(k);
                            if(code.compareTo(itemCode) == 0) {
                                return true;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public ArrayList<String> getCategories()
    {
        ArrayList<String> result = new ArrayList<String>();
        if(jsonSet) {
            for(int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject item = jsonArray.getJSONObject(i);
                    result.add(item.getString(CATEGORY_NAME_KEY));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public Vector<ItemConteiner> getCategoryItems(String category)
    {
        Vector<ItemConteiner> result = new Vector<ItemConteiner>();
        if(jsonSet) {
            for(int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if(object.getString(CATEGORY_NAME_KEY).compareTo(category) == 0) {
                        JSONArray itemsArray = object.getJSONArray(ITEM_LIST_KEY);
                        for(int j = 0; j < itemsArray.length(); ++j) {
                            result.add(new ItemConteiner(itemsArray.getJSONObject(j).getString(ITEM_NAME_KEY),
                                    getPathFromUrl(itemsArray.getJSONObject(j).getString(ITEM_IMAGE_URL_KEY))));
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

    public Vector<ItemConteiner> getAllItems()
    {
        Vector<ItemConteiner> result = new Vector<ItemConteiner>();
        if(jsonSet) {
            for(int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    JSONArray itemsArray = object.getJSONArray(ITEM_LIST_KEY);
                    for(int j = 0; j < itemsArray.length(); ++j) {
                        result.add(new ItemConteiner(itemsArray.getJSONObject(j).getString(ITEM_NAME_KEY),
                                getPathFromUrl(itemsArray.getJSONObject(j).getString(ITEM_IMAGE_URL_KEY))));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public void chaecImages()
    {
        final HashMap<String, String> imageInfoMap = new HashMap<String, String>();
        if(jsonSet) {
            for(int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    JSONArray itemsArray = object.getJSONArray(ITEM_LIST_KEY);
                    for(int j = 0; j < itemsArray.length(); ++j) {
                        imageInfoMap.put(itemsArray.getJSONObject(j).getString(ITEM_IMAGE_URL_KEY),
                                itemsArray.getJSONObject(j).getString(ITEM_IMAGE_HASH_KEY));
                    }
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
