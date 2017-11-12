package com.vaiyee.myweather.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.vaiyee.myweather.db.City;
import com.vaiyee.myweather.db.County;
import com.vaiyee.myweather.db.Sheng;
import com.vaiyee.myweather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/11/11.
 */

public class Utility {
    /*
    *
    *
    解析和处理服务器返回的省级数据
    */
    public static boolean handleProvinResponse(String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                JSONArray allProvince = new JSONArray(response);
                for (int i = 0;i<allProvince.length();i++)
                {
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    Sheng sheng = new Sheng();
                    sheng.setShengName(provinceObject.getString("name"));
                    sheng.setShengdaihao(provinceObject.getInt("id"));
                    sheng.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //解析服务器返回的市级数据
    public static boolean handleCityResponse(String response,int provinceID) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCitys = new JSONArray(response);
                for (int i = 0; i < allCitys.length(); i++) {
                    JSONObject cityObject = allCitys.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceID);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
            return false;
        }
    //解析服务器返回的县级数据
    public static boolean handleCountyResponse(String response,int cityID) {
        if (!TextUtils.isEmpty(response))
        {
            try {
                JSONArray allCountys = new JSONArray(response);
                for (int i = 0; i < allCountys.length(); i++)
                {
                    JSONObject countyObject = allCountys.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityID);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static Weather handleWeatherResponse(String response)
    {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
