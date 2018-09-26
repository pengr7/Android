package com.example.myapplication9;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    TextView city;
    Button search;
    LinearLayout details;
    TextView city_name;
    TextView update_time;
    TextView temperature;
    TextView temperature_range;
    TextView humidity;
    TextView air_quality;
    TextView wind;
    TextView ultraviolet_index;
    TextView cold_index;
    TextView dressing_index;
    TextView car_wash_index;
    TextView exercise_index;

    void findViews() {
        city = (TextView) findViewById(R.id.city);
        search = (Button) findViewById(R.id.search);
        details = (LinearLayout) findViewById(R.id.details);
        city_name = (TextView) findViewById(R.id.city_name);
        update_time = (TextView) findViewById(R.id.update_time);
        temperature = (TextView) findViewById(R.id.temperature);
        temperature_range = (TextView) findViewById(R.id.temperature_range);
        humidity = (TextView) findViewById(R.id.humidity);
        air_quality = (TextView) findViewById(R.id.air_quality);
        wind = (TextView) findViewById(R.id.wind);
        ultraviolet_index = (TextView) findViewById(R.id.ultraviolet_index);
        cold_index = (TextView) findViewById(R.id.cold_index);
        dressing_index = (TextView) findViewById(R.id.dressing_index);
        car_wash_index = (TextView) findViewById(R.id.car_wash_index);
        exercise_index = (TextView) findViewById(R.id.exercise_index);
    }

    boolean checkNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netWorkInfo = connectivityManager.getActiveNetworkInfo();
        return netWorkInfo != null && netWorkInfo.isAvailable();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNetwork())
                    (new MyAsyncTask()).execute();
                else
                    Toast.makeText(MainActivity.this, "No available network",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return startHTTPConnection();
        }
        @Override
        protected void onPostExecute(String s) {
            parseXMLWithPull(s);
        }
    }

    // start a connection and get the response
    String startHTTPConnection() {
        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL
                    ("http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather")
                    .openConnection());
            connection.setRequestMethod("POST");
            connection.setReadTimeout(8000);
            connection.setConnectTimeout(8000);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            String request = city.getText().toString();
            request = URLEncoder.encode(request, "utf-8");
            out.writeBytes("theCityCode=" + request
                    + "&theUserID=66475d7d45ab413181a1ef18213bae3e");
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                response.append(line);
            connection.disconnect();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("SetTextI18n")
    void parseXMLWithPull(String s) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(s));
            int count = 0;
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("string".equals(parser.getName())) {
                            count++;
                            switch (count) {
                                case 2:
                                    city_name.setText(parser.nextText());
                                    break;
                                case 4:
                                    update_time.setText(parser.nextText().substring(11)
                                            + " 更新");
                                    break;
                                case 5:
                                    String[] string5 = parser.nextText().split("；");
                                    temperature.setText(string5[0].split("：")[2]);
                                    wind.setText(string5[1].split("：")[1]);
                                    humidity.setText(string5[2]);
                                    break;
                                case 6:
                                    String[] string6 = parser.nextText().split("。");
                                    air_quality.setText(string6[1]);
                                    break;
                                case 7:
                                    String[] string7 = parser.nextText().split("。");
                                    ultraviolet_index.setText(string7[0].split("：")[1]);
                                    cold_index.setText(string7[1].split("：")[1]);
                                    dressing_index.setText(string7[2].split("：")[1]);
                                    car_wash_index.setText(string7[3].split("：")[1]);
                                    exercise_index.setText(string7[4].split("：")[1]);
                                    break;
                                case 9:
                                    temperature_range.setText(parser.nextText());
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
                details.setVisibility(LinearLayout.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
