package com.example.stonksexchange.models;

public class User {
    String id;
    String username;
    String name;
    String email;
    Number balance;
    String avatar;
    String birthday;
    String phoneNumber;
    String passportNumber;

    public User() {
    }

    public User(String id, String username, String name, String email, Number balance, String avatar, String birthday, String phoneNumber, String passportNumber) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.avatar = avatar;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.passportNumber = passportNumber;
    }

    public String getUsername() {
        return username;
    }

    public Float getBalance() {
        return balance.floatValue();
    }

    public void setBalance(Number balance) {
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }
}
