package com.i5i58.live.common.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Lee on 2017/4/12.
 */

@Table(name = "user")
public class User {

    @Column(name = "id")
    private int id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "accId", isId = true)
    private String accId;

    @Column(name = "token")
    private String token;

    @Column(name = "icon")
    private String icon = "no icon";

    public User() {
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getAccId() {
        return accId;
    }

    public User setAccId(String accId) {
        this.accId = accId;
        return this;
    }

    public String getToken() {
        return token;
    }

    public User setToken(String token) {
        this.token = token;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public User setIcon(String icon) {
        this.icon = icon;
        return this;
    }

}
