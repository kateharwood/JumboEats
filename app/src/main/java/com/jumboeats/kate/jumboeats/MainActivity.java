package com.jumboeats.kate.jumboeats;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

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
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class MainActivity extends AppCompatActivity {
    private static final String TAG_DATE = "Date";
    private static final String TAG_TIME = "Time";
    private static final String TAG_FOOD = "Food";
    private static final String TAG_SPONSOR = "Sponsor";
    private static final String TAG_LOCATION = "Location";
    private static final String TAG_OTHER = "Other";


    private ListView list;

    private ArrayList<HashMap<String, String>> eventList = new ArrayList<>();

    public class GetData extends AsyncTask<Void, Integer, JSONArray> {

        public GetData() {
            super();
        }


        //    @Override
        protected JSONArray doInBackground(Void... params) {
            try {
                URL api = new URL("https://jumboeats.herokuapp.com/");
                HttpURLConnection conn = (HttpURLConnection)api.openConnection();

                InputStream is = conn.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                r.close();
                is.close();
                return new JSONArray(total.toString());
            }
            catch (MalformedURLException e) {
                Crashlytics.logException(e);
                return null;
            }
            catch (IOException e) {
                Crashlytics.logException(e);
                return null;
            }
            catch (JSONException e) {
                Crashlytics.logException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray jArray) {
            if (jArray != null) {
                JSONObject entry;

                try {
                    int numMessages = jArray.length();
                    for (int i = 0; i < numMessages; i++) {
                        entry = jArray.getJSONObject(i);

                        String date = entry.getString(TAG_DATE);
                        String time = entry.getString(TAG_TIME);
                        String food = entry.getString(TAG_FOOD);
                        String sponsor = entry.getString(TAG_SPONSOR);
                        String location = entry.getString(TAG_LOCATION);

                        HashMap<String, String> map = new HashMap<>();

                        // parse date passed in from database
                        Date newDate = new Date();
                        try {
                            newDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                            String dateString = newDate.toString();
                            String[] sp = dateString.split(" ");
                            // System.out.println(sp[1] + " " + sp[2]);
                            dateString = sp[1] + " " + sp[2];
                            map.put(TAG_DATE, dateString);
                        } catch (ParseException e) {
                            map.put(TAG_DATE, date);
                        }

                        // parse time passed in from database
                        Date newTime = new Date();
                        try {
                            newTime = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss+SSSS").parse(time);
                            String timeString = newTime.toString();
                            String[] sp = timeString.split(" ");
                            timeString = sp[3];
                            timeString = timeString.substring(0, 5);

                            Integer hour = 0;
                            String addOn = "am";
                            if (!(Integer.parseInt(Character.toString(timeString.charAt(0))) == 0)) {

                              if (Integer.parseInt(Character.toString(timeString.charAt(0))) == 1 ||
                                        Integer.parseInt(Character.toString(timeString.charAt(0))) == 2) {
                                hour = Integer.parseInt(timeString.substring(0, 2));
                                if (hour >= 12) {
                                    addOn = "pm";
                                    hour = Integer.parseInt(timeString.substring(0, 2)) - 12;
                                }
                              }
                              timeString = Integer.toString(hour) + timeString.substring(2, timeString.length()) + addOn;


                            }
                            else  {
                                if (Integer.parseInt(Character.toString(timeString.charAt(1))) == 0) {
                                    hour = 12;
                                    timeString = Integer.toString(hour) + timeString.substring(2, timeString.length()) + addOn;
                                }
                                timeString = timeString.substring(1, 5) + addOn;
                            }
                            map.put(TAG_TIME, timeString);
                        } catch (ParseException e) {
                            map.put(TAG_TIME, time);
                        }


                        map.put(TAG_FOOD, food);
                        map.put(TAG_SPONSOR, sponsor);
                        map.put(TAG_LOCATION, location);

                        eventList.add(0, map);
                        list = (ListView) findViewById(R.id.list);

                        ListAdapter adapter = new SimpleAdapter(MainActivity.this, eventList,
                                R.layout.list_item, new String[]{TAG_DATE, TAG_TIME, TAG_FOOD, TAG_SPONSOR, TAG_LOCATION, TAG_OTHER},
                                new int[]{R.id.date, R.id.time, R.id.food, R.id.sponsor, R.id.location});

                        assert list != null;
                        list.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    Crashlytics.logException(e);
                }

            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        Button createNewEvent = (Button) findViewById(R.id.createNewEvent);

        assert createNewEvent != null;
        createNewEvent.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Pop.class));
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        eventList.clear();
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(null);
        new GetData().execute();

    }

}
