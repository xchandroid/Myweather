package com.vaiyee.myweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/11/11.
 */

public class County extends DataSupport {
    private int id;
    private String countyName;
    private int cityId;
    private String weatherId;

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {

        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCountyName() {

        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
