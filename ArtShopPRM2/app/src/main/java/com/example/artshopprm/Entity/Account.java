package com.example.artshopprm.Entity;

public class Account {
    private String id;
    private String email;
    private String password;  // Note: Storing passwords directly like this is not recommended for real applications
    private String phoneNumber;
    private String userName;
    private String role;
    private String createdDate;
    private String updateDate;

    // Constructors
    public Account() {
        // Default constructor required for Firebase Realtime Database
    }

    public Account(String id, String email, String password, String phoneNumber, String userName, String role, String createdDate, String updateDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.role = role;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
    }

    // Getters and Setters (omitting for brevity, but you should include getters and setters for all fields)

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    // toString() method for debugging or logging purposes
    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userName='" + userName + '\'' +
                ", role='" + role + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                '}';
    }
}
