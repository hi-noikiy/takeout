package com.baidu.iov.dueros.waimai.net.entity.request;

import com.baidu.iov.dueros.waimai.net.entity.base.LatLongRequestBase;

public class AddressEditReq extends LatLongRequestBase {
    private Long address_id;
    private Long mt_address_id;
    private String type;
    private String user_name;
    private int sex;
    private String user_phone;
    private String address;
    private String house;

    public Long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Long address_id) {
        this.address_id = address_id;
    }

    public Long getMt_address_id() {
        return mt_address_id;
    }

    public void setMt_address_id(Long mt_address_id) {
        this.mt_address_id = mt_address_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }
    
}
