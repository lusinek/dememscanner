package com.demem.barcodescanner;

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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class JsonManager {

    private final String jsonFileName = "myJsonFile.json";
    //private final String jsonUrl = "http://askimlan.110mb.com/mydir/products.txt";
    private final String jsonUrl = "http://hakob.info/auto/_products.json";
    private Context _context;

    public interface OnJsonManagerListener {
        public abstract void onJsonDataRead(String json);
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

    public void update()
    {
        ConnectionDetector cd = new ConnectionDetector(this._context);
        readJsonFromFile();
        if(cd.isConnectingToInternet()) {
            updateJson();
        }
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
                writeJsonToFile(data);
                readJsonFromFile();
            }
        });
        nm.execute(jsonUrl);
    }
}
