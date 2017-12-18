package com.example.android.sunshine;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;


public class WeatherLoader<T> extends AsyncTaskLoader<String[]> {

    private String location;
    private String cachedResult[];

    public WeatherLoader(Context context, String location) {
        super(context);
        this.location = location;
    }

    @Override
    public String[] loadInBackground() {
        /* If there's no zip code, there's nothing to look up. */
        if (location.length() == 0) {
            return null;
        }

        URL weatherRequestUrl = NetworkUtils.buildUrl(location);

        try {
            String jsonWeatherResponse = NetworkUtils
                    .getResponseFromHttpUrl(weatherRequestUrl);

            String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                    .getSimpleWeatherStringsFromJson(getContext(), jsonWeatherResponse);

            return simpleJsonWeatherData;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onStartLoading() {
        if (cachedResult != null) {
            deliverResult(cachedResult);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(String[] data) {
        cachedResult = data;
        super.deliverResult(data);
    }
}
