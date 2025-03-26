package com.splitwise.dto;

public class UserBalanceDTO {
    private String name;
    private String email;
    private double balance;

    public UserBalanceDTO() {}

    public UserBalanceDTO(String name, String email, double balance) {
        this.name = name;
        this.email = email;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
