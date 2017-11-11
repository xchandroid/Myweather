package com.vaiyee.myweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/11/11.
 */

public class Sheng extends DataSupport {
    private int id;
    private String shengName;
    private int shengdaihao;

    public int getShengdaihao() {
        return shengdaihao;
    }

    public void setShengdaihao(int shengdaihao) {
        this.shengdaihao = shengdaihao;
    }

    public String getShengName() {

        return shengName;
    }

    public void setShengName(String shengName) {
        this.shengName = shengName;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
