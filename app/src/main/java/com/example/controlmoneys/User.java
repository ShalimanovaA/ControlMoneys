package com.example.controlmoneys;

public class User {
    public String id, email, password;

    public User() {
        // конструктор класса
    }

    public User(String id, String email, String password) {
        // конструктор класса
        this.id = id;
        this.email = email;
        this.password = password;
    }
    public String getUserEmail(){
        return this.email;
    }
    public String getUserId(){
        return this.id;
    }
    public String getUserPassword(){
        return this.password;
    }
}
