package com.demem.barcodescanner.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import android.os.AsyncTask;
import android.util.Log;

public class NetworkManager extends AsyncTask<String, Integer, Void> {

    interface OnNetworkDownloaderListener {
        public void onFileDownloaded(String url, byte[] data);
    }

    OnNetworkDownloaderListener onNetworkDownloaderListener = null;

    public void setOnNetworkDownloaderListener(OnNetworkDownloaderListener listener) {
        onNetworkDownloaderListener = listener;
    }

    @Override
    protected Void doInBackground(String... urls) {
        int count = urls.length;
        for (int i = 0; i < count; i++) {
            DefaultHttpClient defaultClient = new DefaultHttpClient(new BasicHttpParams());
            HttpGet httpGetRequest = new HttpGet(urls[i]);
            try {
                HttpResponse httpResponse = defaultClient.execute(httpGetRequest);
                InputStream inputStream = httpResponse.getEntity().getContent();
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();;
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];

                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                  byteBuffer.write(buffer, 0, len);
                }
                if(onNetworkDownloaderListener != null) {
                    onNetworkDownloaderListener.onFileDownloaded(urls[i], byteBuffer.toByteArray());
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (isCancelled()) {
                break;
            }
        }
        return null;
    }
}
