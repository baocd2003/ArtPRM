package com.example.artshopprm.Entity;

public class MessageModel {
    private String messId;
    private String SenderId;
    private String messages;

    public MessageModel() {
    }

    public MessageModel(String messId) {
        this.messId = messId;
    }

    public MessageModel(String messId, String senderId, String messages) {
        this.messId = messId;
        SenderId = senderId;
        this.messages = messages;
    }

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
