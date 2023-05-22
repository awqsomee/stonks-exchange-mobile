package com.example.stonksexchange.models;

public class User {
    String id;
    String username;
    String name;
    String email;
    Number balance;
    String avatar;

    public User() {
    }

    public User(String id, String username, String name, String email, Number balance, String avatar) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public Number getBalance() {
        return balance;
    }

    public void setBalance(Number balance) {
        this.balance = balance;
    }
}
