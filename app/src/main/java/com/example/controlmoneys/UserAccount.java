package com.example.controlmoneys;

public class UserAccount {
    public String id, email;
    public int period, money;

    public UserAccount() {
        // конструктор класса
    }

    public UserAccount(String id, String email, int period, int money) {
        // конструктор класса
        this.id = id;
        this.email = email;
        this.period = period;
        this.money = money;
    }
}
