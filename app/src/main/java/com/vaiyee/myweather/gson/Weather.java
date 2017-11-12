package com.vaiyee.myweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/11/12.
 */

public class Weather {
    public String status;
    public AQI aqi;
    public Basic basic;
    public Suggestion suggestion;
    public Now now;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
