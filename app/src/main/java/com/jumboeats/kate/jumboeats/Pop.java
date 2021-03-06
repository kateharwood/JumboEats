package com.jumboeats.kate.jumboeats;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.os.AsyncTask;
import android.widget.TimePicker;

import com.crashlytics.android.Crashlytics;

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
import android.app.AlertDialog;
import android.content.DialogInterface;



/**
 * Pop up window to post free food events
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

        getWindow().setLayout((int) (width * 1), (int) (height * 1));


        //****** SAVE USER INPUT *******//

        Button postEvent = (Button) findViewById(R.id.post_event);
        Button cancelEvent = (Button) findViewById(R.id.cancel_event);
        assert cancelEvent != null;
        cancelEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        assert postEvent != null;
        postEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                String halfOfDay = "am";

                final EditText eventName = (EditText) findViewById(R.id.edit_event);
                final EditText food = (EditText) findViewById(R.id.edit_food);
                final EditText location = (EditText) findViewById(R.id.edit_place);
                final TimePicker time = (TimePicker) findViewById(R.id.edit_time);
                final DatePicker date = (DatePicker) findViewById(R.id.edit_date);

                final String theFood = food.getText().toString();
                int theHour;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    theHour = time.getHour();
                }
                else {
                    //noinspection deprecation
                    theHour = time.getCurrentHour(); // for APIs less than 23
                }

                final int theMinute;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    theMinute = time.getMinute();
                }
                else {
                    //noinspection deprecation
                    theMinute = time.getCurrentMinute(); // for APIs less than 23
                }
                final String theLocation = location.getText().toString();
                final String theEvent = eventName.getText().toString();
                final int theMonth = date.getMonth();
                final int theDay = date.getDayOfMonth();

                if (theHour > 12) {
                    theHour = theHour - 12;
                    halfOfDay = "pm";
                }

                if (theHour == 0) {
                    theHour = 12;
                }

                String theNewMinute = String.valueOf(theMinute);
                if (theMinute < 10) {
                    theNewMinute = "0" + String.valueOf(theMinute);
                }

                final String theDate = MONTHS[theMonth] + " " + String.valueOf(theDay);
                final String theTime = String.valueOf(theHour) + ":" + theNewMinute + halfOfDay;


                //****** POST INPUT TO SERVER ********//
                 class sendPostRequest extends AsyncTask<String, Void, String> {


                    protected String doInBackground(String... arg0) {

                        try {
                            URL url = new URL("http://jumboeats.herokuapp.com/post");

                            JSONObject postDataParams = new JSONObject();
                            postDataParams.put("date", theDate);
                            postDataParams.put("time", theTime);
                            postDataParams.put("food", theFood);
                            postDataParams.put("location", theLocation);
                            postDataParams.put("sponsor", theEvent);
                            postDataParams.put("other", "other");


                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setReadTimeout(1500);
                            conn.setConnectTimeout(1500);
                            conn.setRequestMethod("POST");
                            conn.setDoInput(true);
                            conn.setDoOutput(true);

                            OutputStream os = conn.getOutputStream();
                            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(os, "UTF-8"));
                            writer.write(getPostDataString(postDataParams));

                            writer.flush();
                            writer.close();
                            os.close();

                            int responseCode=conn.getResponseCode();

                            if (responseCode == HttpURLConnection.HTTP_OK) {

                                BufferedReader in=new BufferedReader(
                                        new InputStreamReader(
                                                conn.getInputStream()));
                                StringBuilder sb = new StringBuilder("");
                                String line;

                                if((line = in.readLine()) != null) {

                                    sb.append(line);
                                }

                                in.close();
                                return sb.toString();

                            }
                            else {
                                return "false : " + responseCode;
                            }
                        }


                         catch(Exception e){
                             Crashlytics.logException(e);
                             return "Exception: " + e.getMessage();
                         }
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
                finish();
            }
        });
    }
}
