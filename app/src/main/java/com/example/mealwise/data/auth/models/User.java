package com.example.mealwise.data.auth.models;

public class User {
    String userId;
    String userName;
    String email;

    public User(){}

    public User(String userId,String userName, String email) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
    }

    public String getUserId() {return userId;}

    public String getUserName() {return userName;}

    public String getEmail() {return email;}
}
