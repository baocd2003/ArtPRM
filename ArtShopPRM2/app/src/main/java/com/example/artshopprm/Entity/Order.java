package com.example.artshopprm.Entity;

import java.util.Date;

public class Order {
    private String id;
    private Date createdDate;
    private Date updateDate;
    private String bankName;
    private String bankAccount;
    private String deliveryAddress;
    private String accountId;
    private String status;
    private boolean isActive;

    public Order() {

    }
    public Order(String id, Date createdDate, Date updateDate, String bankName, String bankAccount, String deliveryAddress, String accountId, String status, boolean isActive) {
        this.id = id;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.deliveryAddress = deliveryAddress;
        this.accountId = accountId;
        this.status = status;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
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
