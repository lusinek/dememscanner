package com.demem.barcodescanner.base;

import org.json.JSONException;
import org.json.JSONObject;

import com.demem.barcodescanner.utils.JsonManager;
import com.demem.barcodescanner.utils.JsonManager.OnJsonManagerListener;

import android.content.Context;


public abstract class JsonParserBase {

    protected String fileName;
    protected String fileUrl;

    protected Context _context;
    protected JsonManager jsonManager;
    protected boolean jsonSet = false;

    public interface OnJsonParserListener {
        public void onJSONSet();
    }

    protected OnJsonParserListener onJsonParserListener = null;

    public void init(Context context, String fileName, String fileUrl)
    {
    	this._context = context;
    	this.fileName = fileName;
        this.fileUrl = fileUrl;
        jsonManager = new JsonManager(this._context);
        jsonManager.setFilename(fileName);
        jsonManager.setUrl(fileUrl);
        jsonManager.setOnJsonManagerListener(new JsonManager.OnJsonManagerListener() {
            @Override
            public void onJsonDataRead(String json) {
            	jsonDataRead(json);
            }
        });
    }

    public boolean update()
    {
        return jsonManager.update();
    }

    public void setOnJsonItemListParserListener(OnJsonParserListener listener)
    {
        onJsonParserListener = listener;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    protected abstract void jsonDataRead(String json); 
}
