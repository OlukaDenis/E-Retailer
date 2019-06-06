package com.jusutech.easyshopugserver.Model;

import java.io.Serializable;

/**
 * Created by Junior Joseph on 1/14/2019.
 */

public class Product {
    private String Name, Image, Description, Price, addedBy, bargained, available, Menuid;
    public Product() {
    }

    public Product(String name, String image, String description, String price, String addedBy, String bargained, String available, String menuid) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        this.addedBy = addedBy;
        this.bargained = bargained;
        this.available = available;
        Menuid = menuid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getBargained() {
        return bargained;
    }

    public void setBargained(String bargained) {
        this.bargained = bargained;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getMenuid() {
        return Menuid;
    }

    public void setMenuid(String menuid) {
        Menuid = menuid;
    }
}

