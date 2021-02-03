package android.cnam.bookypocket.API;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.cnam.bookypocket.R;
import android.os.Bundle;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

public class API_MySQL {

    public void test() {

        Thread thread = new Thread(new Runnable() {

            HttpURLConnection urlConnection = null;

            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.1.10/biblio/api/author/");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                        JSONArray test = new JSONArray(readStream(in));
                        String res = "";
                        for (int i = 0; i < test.length(); i++) {
                            JSONObject jsonObject = test.getJSONObject(i);
                            Iterator<String> keys = jsonObject.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                res += key + ": " + jsonObject.getString(key) + "\n";
                            }
                            res += "\n";
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
            }
        });
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.start();
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }
}