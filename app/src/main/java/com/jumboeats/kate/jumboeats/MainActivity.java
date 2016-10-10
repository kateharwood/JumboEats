package com.jumboeats.kate.jumboeats;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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


public class MainActivity extends AppCompatActivity {
    private static final String TAG_DATE = "Date";
    private static final String TAG_TIME = "Time";
    private static final String TAG_FOOD = "Food";
    private static final String TAG_SPONSOR = "Sponsor";
    private static final String TAG_LOCATION = "Location";
    private static final String TAG_OTHER = "Other";


    ListView list;
    TextView date;
    TextView time;
    TextView food;
    TextView sponsor;
    TextView location;
    TextView other;

    ArrayList<HashMap<String, String>> eventList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         class GetData extends AsyncTask<Void, Integer, JSONArray> {
//            private Context copyOfContext;

            public GetData (Context context) {
                super();
//                copyOfContext = context;
            }

             protected void onPreExecute() {
                 super.onPreExecute();
                 date = (TextView)findViewById(R.id.date);
                 time = (TextView)findViewById(R.id.time);
                 food = (TextView)findViewById(R.id.food);
                 sponsor = (TextView)findViewById(R.id.sponsor);
                 location = (TextView)findViewById(R.id.location);
                 other = (TextView)findViewById(R.id.other);

             }


            //    @Override
            protected JSONArray doInBackground(Void... params) {
                try {
                    URL api = new URL("http://jumboeats.herokuapp.com/");
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
                    Log.e("doInBackground(): ", e.toString());
                    return null;
                }
                catch (IOException e) {
                    Log.e("doInBackground(): ", e.toString());
                    return null;
                }
                catch (JSONException e) {
                    Log.e("doInBackground(): ", e.toString());
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
                            String other = entry.getString(TAG_OTHER);


                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(TAG_DATE, date);
                            map.put(TAG_TIME, time);
                            map.put(TAG_FOOD, food);
                            map.put(TAG_SPONSOR, sponsor);
                            map.put(TAG_LOCATION, location);
                            map.put(TAG_OTHER, other);


                            eventList.add(map);
                            list = (ListView) findViewById(R.id.list);

                            ListAdapter adapter = new SimpleAdapter(MainActivity.this, eventList,
                                    R.layout.list_item, new String[]{TAG_DATE, TAG_TIME, TAG_FOOD, TAG_SPONSOR, TAG_LOCATION, TAG_OTHER},
                                    new int[]{R.id.date, R.id.time, R.id.food, R.id.sponsor, R.id.location, R.id.other});

                            list.setAdapter(adapter);
                        }

                    } catch (JSONException e) {
                        Log.e("onPostExecute", e.toString());
                    }

                }
            }

        }



        AsyncTask<Void, Integer, JSONArray> json = new GetData(getApplicationContext()).execute();

        Button b = (Button) findViewById(R.id.button);

        assert b != null;
        b.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Pop.class));
            }
        });

    }
}
