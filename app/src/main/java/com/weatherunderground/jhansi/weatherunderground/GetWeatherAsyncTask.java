package com.weatherunderground.jhansi.weatherunderground;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Jhansi Tavva on 5/11/16.
 * Copyright (c) 2016 Jhansi Tavva. All rights reserved.
 */
public class GetWeatherAsyncTask extends AsyncTask<String, Void, WeatherResult> {


    public static final String TAG = GetWeatherAsyncTask.class.getSimpleName();

    String tempInF = "NA";

    Listener listeningActivity;

    GetWeatherAsyncTask(Listener activity) {

        this.listeningActivity = activity;
    }

    @Override
    protected WeatherResult doInBackground(String... params) {


        WeatherResult weatherResult = new WeatherResult();
        try {
            URL obj = new URL(params[0].toString());

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();


            // optional default is GET
            con.setRequestMethod("GET");


            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonObject = new JSONObject(response.toString());

                JSONObject current = jsonObject.getJSONObject(CommonUtils.JSON_KEY_CURRENT_OBSERVATION);
                weatherResult.setTemp_f(current.getString(CommonUtils.JSON_KEY_TEMP_F));

                weatherResult.setIcon_url(current.getString(CommonUtils.JSON_KEY_ICON_URL));

                JSONObject display_location = current.getJSONObject(CommonUtils.JSON_KEY_DISPLAY_LOCATION);
                weatherResult.setFull_name(display_location.getString(CommonUtils.JSON_KEY_FULL));

                JSONObject image = current.getJSONObject(CommonUtils.JSON_KEY_IMAGE);
                weatherResult.setProvider_url(image.getString(CommonUtils.JSON_KEY_URL));


                weatherResult.setDrawableProviderIcon(LoadImageFromURL(weatherResult.getProvider_url()));
                weatherResult.setDrawableWeather(LoadImageFromURL(weatherResult.getIcon_url()));


            }


        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }

        return weatherResult;
    }

    @Override
    protected void onPostExecute(WeatherResult weatherResult) {

        listeningActivity.onReceived(weatherResult);
        super.onPostExecute(weatherResult);
    }

    public static Drawable LoadImageFromURL(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable drawable = Drawable.createFromStream(is, "src name");
            return drawable;
        } catch (Exception e) {
            return null;
        }
    }

}