package com.example.artshopprm.Entity;

import java.util.Date;

public class OrderDetail {
    private String id;
    private Date createdDate;
    private Date updateDate;
    private String orderId;
    private String artId;
    private int quantity;
    private double actualPrice;
    private boolean isActive;

    public OrderDetail(){

    }
    public OrderDetail(String id, Date createdDate, Date updateDate, String orderId, String artId, int quantity, double actualPrice, boolean isActive) {
        this.id = id;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
        this.orderId = orderId;
        this.artId = artId;
        this.quantity = quantity;
        this.actualPrice = actualPrice;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getArtId() {
        return artId;
    }

    public void setArtId(String artId) {
        this.artId = artId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
