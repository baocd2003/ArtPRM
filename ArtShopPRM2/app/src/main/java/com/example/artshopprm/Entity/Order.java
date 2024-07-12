package com.example.artshopprm.Entity;

import java.util.Date;

public class Order {
    private String id;
    private String createdDate;
    private String updateDate;
<<<<<<< Updated upstream

=======
    private String bankName;
    private String bankAccount;
>>>>>>> Stashed changes
    private String deliveryAddress;
    private String accountId;
    private String status;
    private boolean isActive;

    private double totalPrice;

    public Order() {

    }
<<<<<<< Updated upstream

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Order(String id, String createdDate, String updateDate, String deliveryAddress, String accountId, String status, boolean isActive, double totalPrice) {
=======
    public Order(String id, String createdDate, String updateDate, String bankName, String bankAccount, String deliveryAddress, String accountId, String status, boolean isActive) {
>>>>>>> Stashed changes
        this.id = id;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
        this.deliveryAddress = deliveryAddress;
        this.accountId = accountId;
        this.status = status;
        this.isActive = isActive;
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
