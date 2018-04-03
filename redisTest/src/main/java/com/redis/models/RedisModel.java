package com.redis.models;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable
public class RedisModel implements Serializable {

    private static final long serialVersionUID = -820116588318764312L;
    @Id
    private String redisKey;//redis中的key
    private String name;//姓名
    private String tel;//电话
    private String address;//住址

    public RedisModel() {
    }

    public RedisModel(String redisKey, String name, String tel, String address) {
        this.redisKey = redisKey;
        this.name = name;
        this.tel = tel;
        this.address = address;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
