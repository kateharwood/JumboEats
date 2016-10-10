package com.jumboeats.kate.jumboeats;

/**
 * Created by kate on 10/6/16.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
//import com.crashlytics.android.Crashlytics;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;



import android.util.Log;


//public class GetData extends AsyncTask<Void, Integer, JSONArray> {
//    private Context copyOfContext;
//
//    public GetData (Context context) {
//        super();
//        copyOfContext = context;
//    }
//
//
////    @Override
//    protected JSONArray doInBackground(Void... params) {
//        try {
//            URL api = new URL("http://jumboeats.herokuapp.com/");
//            HttpURLConnection conn = (HttpURLConnection)api.openConnection();
//
//            InputStream is = conn.getInputStream();
//            BufferedReader r = new BufferedReader(new InputStreamReader(is));
//            StringBuilder total = new StringBuilder();
//            String line;
//            while ((line = r.readLine()) != null) {
//                total.append(line);
//            }
//            r.close();
//            is.close();
////            Log.v("data", total.toString());
//            return new JSONArray(total.toString());
//        }
//        catch (MalformedURLException e) {
//            Log.e("doInBackground(): ", e.toString());
//            return null;
//        }
//        catch (IOException e) {
//            Log.e("doInBackground(): ", e.toString());
//            return null;
//        }
//        catch (JSONException e) {
//            Log.e("doInBackground(): ", e.toString());
//            return null;
//        }
//    }
//
//    @Override
//    protected void onPostExecute(JSONArray jArray) {
//        if (jArray != null) {
////            Log.v("tag", jArray.toString());
//
//
//            JSONObject entry;
//            int numMessages = 0, count = 0;
//
//            try {
//                numMessages = jArray.length();
//                if (numMessages > 0){
//                    while (count < 1) {
//                        entry = jArray.getJSONObject(count);
//                        Log.v("please", entry.toString());
//
//                        count++;
//                    }
//                }
//            }
//
//            catch (JSONException e) {
//                Log.e("onPostExecute", e.toString());
//            }
//
//
//
//        }
//        else {
//            Log.v("uh oh", "null");
//        }
//    }
//
//}