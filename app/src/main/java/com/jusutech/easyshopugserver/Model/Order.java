package com.jusutech.easyshopugserver.Model;

/**
 * Created by Jafix Technologies on 2/13/2019.
 */


public class Order {
    private String ProductId, Name, Quantity, Price, Image;

    public Order() {
    }

    public Order(String productId, String name, String quantity, String price, String image) {
        ProductId = productId;
        Name = name;
        Quantity = quantity;
        Price = price;
        Image = image;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

}
