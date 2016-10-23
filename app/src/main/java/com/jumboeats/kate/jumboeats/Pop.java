package com.jumboeats.kate.jumboeats;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.EditText;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.net.URLEncoder;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;



/**
 * Created by kate on 10/2/16.
 */

public class Pop extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .6), (int) (height * .8));

//        EditText eventName;
//        EditText food;
//        EditText location;
//        EditText time;


        //****** SAVE USER INPUT *******//

        Button postEvent = (Button) findViewById(R.id.postEvent);
        assert postEvent != null;
        postEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final EditText eventName = (EditText) findViewById(R.id.edit_event);
                final EditText food = (EditText) findViewById(R.id.edit_food);
                final EditText location = (EditText) findViewById(R.id.edit_place);
                final EditText time = (EditText) findViewById(R.id.edit_time);
                String date = "location";

                Log.v("blah", food.getText().toString());
                final String theFood = food.getText().toString();
                final String theTime = time.getText().toString();
                final String theLocation = location.getText().toString();
                final String theEvent = eventName.getText().toString();


//            }
//        });
//    }}




                //****** POST INPUT TO SERVER ********//
                 class sendPostRequest extends AsyncTask<String, Void, String> {

                   // protected void onPreExecute()

                    protected String doInBackground(String... arg0) {

                        try {
                            URL url = new URL("http://jumboeats.herokuapp.com/post");

                            JSONObject postDataParams = new JSONObject();
                            postDataParams.put("date", "now");
                            postDataParams.put("time", theTime);
                            postDataParams.put("food", theFood);
                            postDataParams.put("location", theLocation);
                            postDataParams.put("sponsor", theEvent);
                            postDataParams.put("other", "other");

                            Log.e("params", postDataParams.toString());

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setReadTimeout(1500);
                            conn.setConnectTimeout(1500);
                            conn.setRequestMethod("POST");
                            conn.setDoInput(true);
                            conn.setDoOutput(true);

                            Log.e("params for url", getPostDataString(postDataParams));
                            OutputStream os = conn.getOutputStream();
                            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(os, "UTF-8"));
                            writer.write(getPostDataString(postDataParams));

                            writer.flush();
                            writer.close();
                            os.close();
                            Log.e("here", "here1");

                            Log.e("help", String.valueOf(conn.getResponseCode()));
                            int responseCode=conn.getResponseCode();
                            Log.e("response code", String.valueOf(responseCode));

                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                Log.e("here", "here2");

                                BufferedReader in=new BufferedReader(
                                        new InputStreamReader(
                                                conn.getInputStream()));
                                StringBuffer sb = new StringBuffer("");
                                String line="";

                                while((line = in.readLine()) != null) {

                                    sb.append(line);
                                    break;
                                }

                                in.close();
                                return sb.toString();

                            }
                            else {
                                return new String("false : "+responseCode);
                            }
                        }


                         catch(Exception e){

                             return new String("Exception: " + e.getMessage());
                         }
                    }


                     @Override
                     protected void onPostExecute(String result) {
                         Log.e("result", result);

                     }


                    public String getPostDataString(JSONObject params) throws Exception {
                        StringBuilder result = new StringBuilder();
                        boolean first = true;

                        Iterator<String> itr = params.keys();

                        while (itr.hasNext()) {
                            String key = itr.next();
                            Object value = params.get(key);

                            if (first) {
                                first  = false;
                            }
                            else {
                                result.append("&");
                            }

                            result.append(URLEncoder.encode(key, "UTF-8"));
                            result.append("=");
                            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
                        }

                        return result.toString();

                    }
                 }
                new sendPostRequest().execute();
            }
        });
    }
}
