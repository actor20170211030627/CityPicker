package com.actor.parsehtml;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 直辖市/市
 *
 * @author : 李大发
 * date       : 2020/5/12 on 21:16
 * @version 1.0
 */
public class CityBean {

    public long id;
    public String name;

    //区县列表
    public List<CountyBean> countys;

    public CityBean(long id, String name) {
        this.id = id;
        this.name = name;
        countys = new ArrayList<>();
    }
}
