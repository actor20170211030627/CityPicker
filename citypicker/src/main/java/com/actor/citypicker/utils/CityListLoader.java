package com.actor.citypicker.utils;

import android.content.Context;

import com.actor.citypicker.bean.CityInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 作者：liji on 2017/5/21 15:35
 * 邮箱：lijiwork@sina.com
 * QQ ：275137657
 */
public class CityListLoader {

    public static final     String         CITY_JSON = "china_city_data.json";
    private volatile static CityListLoader instance;
    private                 List<CityInfo> provinces;//所有城市

    private CityListLoader() {
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static CityListLoader getInstance() {
        if (instance == null) {
            synchronized (CityListLoader.class) {
                if (instance == null) {
                    instance = new CityListLoader();
                }
            }
        }
        return instance;
    }

    /**
     * 解析所有城市
     */
    public void init(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String cityJson = AssetsUtils.getString(context, CITY_JSON);
                Type type = new TypeToken<List<CityInfo>>() {
                }.getType();
                provinces = new Gson().fromJson(cityJson, type);
            }
        }).start();
    }

    public List<CityInfo> getProvinces() {
        return provinces;
    }
}
