package com.demem.barcodescanner.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class JsonManager {

    // default values
    private String jsonFileName = null;
    private String jsonUrl = null;

    private Context _context;

    public interface OnJsonManagerListener {
        public abstract void onJsonDataRead(String json);
        public abstract void onJsonDownloaded(String json);
    }

    OnJsonManagerListener onJsonManagerListener = null;

    public void setOnJsonManagerListener(OnJsonManagerListener listener)
    {
        onJsonManagerListener = listener;
    }

    public JsonManager(Context context)
    {
        this._context = context;
    }

    public boolean update()
    {
        ConnectionDetector cd = new ConnectionDetector(this._context);
        String json = "";
        if(jsonFileName != null) {
            json = readJsonFromFile();
        }
        if(cd.isConnectingToInternet()) {
            updateJson();
            return true;
        } else if(json.length() == 0) {
//            AlertDialog alertDialog = new AlertDialog.Builder(_context).create();;
//            alertDialog.setTitle("Please Connect the Internet and Restart the Application");
//            alertDialog.setButton("OK", new AlertDialog.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            });
//            alertDialog.show();
        }
        return json.length() == 0 ? false : true;
    }

    private void writeJsonToFile(String json)
    {
        try {
            FileWriter out = new FileWriter(new File(this._context.getFilesDir(), jsonFileName));
            out.write(json);
            out.close();
        } catch (IOException e) {
            Log.d("File writing error", e.getMessage());
        }
    }

    private String readJsonFromFile()
    {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File(this._context.getFilesDir(), jsonFileName)));
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
            }
            in.close();
        } catch (FileNotFoundException e) {
            Log.d("File not found", e.getMessage());
        } catch (IOException e) {
            Log.d("IO Error", e.getMessage());
        }
        if(onJsonManagerListener != null) {
            onJsonManagerListener.onJsonDataRead(stringBuilder.toString());
        }
        return stringBuilder.toString();
    }

    public void updateJson()
    {
        NetworkManager nm = new NetworkManager();
        nm.setOnNetworkDownloaderListener(new NetworkManager.OnNetworkDownloaderListener() {
            @Override
            public void onFileDownloaded(String url, byte[] buffer) {
                String data = new String(buffer);
                if(onJsonManagerListener != null) {
                    onJsonManagerListener.onJsonDownloaded(data);
                }
                if(jsonFileName != null) {
                    writeJsonToFile(data);
                    readJsonFromFile();
                }
            }
        });
        nm.execute(jsonUrl);
    }

    public void setFilename(String fileName) {
        jsonFileName = fileName;
    }

    public void setUrl(String url) {
        jsonUrl = url;
    }

    public String getFilename(String fileName) {
        return jsonFileName;
    }

    public String getUrl(String url) {
        return jsonUrl;
    }
}
