package com.example.denis.messenger.Models;

/**
 * Created by Denis on 13.03.2018.
 */

public class User {
    private String name;

    public User() {

    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
