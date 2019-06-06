package com.jusutech.easyshopugserver.Model;

import java.util.List;

/**
 * Created by Jafix Technologies on 2/13/2019.
 */


public class Request {
    private String name;
    private String contact;
    private String address;
    private long total;
    private String status;
    private String transporter, recieved, cancelled;
    private List<Order> orders;//List of orders


    public Request() {
    }

    public Request(String name, String contact, String address, long total, String status, String transporter, String recieved, String cancelled, List<Order> orders) {
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.total = total;
        this.status = status;
        this.transporter = transporter;
        this.recieved = recieved;
        this.cancelled = cancelled;
        this.orders = orders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransporter() {
        return transporter;
    }

    public void setTransporter(String transporter) {
        this.transporter = transporter;
    }

    public String getRecieved() {
        return recieved;
    }

    public void setRecieved(String recieved) {
        this.recieved = recieved;
    }

    public String getCancelled() {
        return cancelled;
    }

    public void setCancelled(String cancelled) {
        this.cancelled = cancelled;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}