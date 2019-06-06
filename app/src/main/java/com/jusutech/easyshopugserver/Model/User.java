package com.jusutech.easyshopugserver.Model;

/**
 * Created by Junior Joseph on 1/8/2019.
 */

public class User {
    private String name, email, phone, password;
    private String admin;

    public User() {
    }

    public User(String name,  String phone, String password, String admin) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
