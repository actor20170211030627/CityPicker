package com.actor.parsehtml;

/**
 * description: 区县
 *
 * @author : 李大发
 * date       : 2020/5/12 on 21:17
 * @version 1.0
 */
public class CountyBean {

    public long id;
    public String name;

    public CountyBean(long id) {
        this.id = id;
    }

    public CountyBean(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
