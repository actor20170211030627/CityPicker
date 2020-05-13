package com.actor.parsehtml;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 省/直辖市/市
 *
 * @author : 李大发
 * date       : 2020/5/12 on 21:16
 * @version 1.0
 */
public class ProvinceBean {

    public long id;
    public String name;

    //城市列表
    public List<CityBean> citys;

    public ProvinceBean(long id) {
        this.id = id;
        citys = new ArrayList<>();
    }
}
