package com.demem.barcodescanner.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.util.Log;

public class ImageManager {

    public static final String IMAGE_FOLDER_NAME = "images";
    private Context _context;

    public ImageManager(Context context) {
        _context = context;
        File f = new File(this._context.getFilesDir(), "/" + IMAGE_FOLDER_NAME);
        if(!f.exists()) {
            f.mkdir();
        }
    }

    public void checkImages(HashMap<String, String> imageInfoMap) {
        ArrayList<String> urlList = new ArrayList<String>();
        for(String key: imageInfoMap.keySet()) {
            if(!isCorrectImage(this._context.getFilesDir() + "/" + IMAGE_FOLDER_NAME + "/" + getFileNameFromUrl(key),
                    imageInfoMap.get(key).toLowerCase())) {
                urlList.add(key);
            }
        }
        String[] urls = new String[urlList.size()];
        urls = urlList.toArray(urls);
        downloadImages(urls);
    }

    public boolean isCorrectImage(String imagePath, String hash) {
        return hash.compareTo(computeFileMD5(imagePath)) == 0 ? true : false;
    }

    public void downloadImages(String[] urls) {
        NetworkManager nm = new NetworkManager();
        nm.setOnNetworkDownloaderListener(new NetworkManager.OnNetworkDownloaderListener() {
            @Override
            public void onFileDownloaded(String url, byte[] buffer) {
                saveImage(getFileNameFromUrl(url), buffer);
            }
        });
        nm.execute(urls);
    }

    public void saveImage(String fileName, byte[] buffer) {
        try {
            File file = new File(this._context.getFilesDir(), "/" + IMAGE_FOLDER_NAME + "/" + fileName);
            if (!file.exists()) {
                  file.createNewFile();
                }
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(buffer);
            stream.close();
        } catch (IOException e) {
            Log.d("File writing error", e.getMessage());
        }
    }

    public String getFileNameFromUrl(String urlName) {
        int index = urlName.lastIndexOf("/");
        return urlName.substring(index + 1, urlName.length());
    }

    public String computeFileMD5(String path) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Log.d("Exception while getting digest", e.getMessage());
            return "";
        }

        InputStream is;
        try {
            is = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            Log.d("Exception while getting FileInputStream", e.getMessage());
            return "";
        }

        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            Log.d("Unable to process file for MD5", e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.d("Exception on closing MD5 input stream", e.getMessage());
            }
        }
        return "";
    }
}
