package com.vaiyee.myweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/11/12.
 */

public class Suggestion {
     @SerializedName("cw")
    public  Carwash carwash;
    @SerializedName("comf")
    public Comfort comfort;
    @SerializedName("sport")
    public Sport sport;
    public class Carwash
    {
        @SerializedName("txt")
        public String info;
    }
    public class Comfort
    {
        @SerializedName("txt")
        public String info;
    }
    public class Sport{
        @SerializedName("txt")
        public String info;
    }
}
