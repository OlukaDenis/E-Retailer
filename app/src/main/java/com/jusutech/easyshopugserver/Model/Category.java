package com.jusutech.easyshopugserver.Model;

/**
 * Created by Junior Joseph on 1/8/2019.
 */

public class Category {
    private String Name;
    private String Link;

    public Category() {

    }

    public Category(String name, String imageLink) {
        this.Name = name;
        this.Link = imageLink;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
