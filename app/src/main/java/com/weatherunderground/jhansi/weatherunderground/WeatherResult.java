package com.weatherunderground.jhansi.weatherunderground;

import android.graphics.drawable.Drawable;

/**
 * Created by Jhansi Tavva on 5/11/16.
 * Copyright (c) 2016 Jhansi Tavva. All rights reserved.
 */
public class WeatherResult {


    private String full_name;
    private String cityName;
    private String country;
    private String temp_f;


    private String temp_c;
    private String icon_url;

    private String provider_url;

    private Drawable drawableProvider;
    private Drawable drawableWeather;


    public String getProvider_url() {
        return provider_url;
    }

    public void setProvider_url(String provider_url) {
        this.provider_url = provider_url;
    }

    public Drawable getDrawableProviderIcon() {
        return drawableProviderIcon;
    }

    public void setDrawableProviderIcon(Drawable drawableProviderIcon) {
        this.drawableProviderIcon = drawableProviderIcon;
    }

    private Drawable drawableProviderIcon;

    public Drawable getDrawableWeather() {
        return drawableWeather;
    }

    public void setDrawableWeather(Drawable drawableWeather) {
        this.drawableWeather = drawableWeather;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTemp_f() {
        return temp_f;
    }

    public void setTemp_f(String temp_f) {
        this.temp_f = temp_f;
    }

    public String getTemp_c() {
        return temp_c;
    }

    public void setTemp_c(String temp_c) {
        this.temp_c = temp_c;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }


}
