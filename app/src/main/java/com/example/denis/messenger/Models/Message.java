package com.example.denis.messenger.Models;


import java.util.Date;

import static java.text.DateFormat.getDateInstance;

/**
 * Created by Denis on 07.03.2018.
 */

public class Message {
    private String message;
    private String user;
    private String id;
    private long time;

    public Message(String message, String user, String id) {
        this.message = message;
        this.user = user;
        this.id = id;
        time = new Date().getTime();
    }

    public Message() {
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMessage() {

        return message;
    }

    public String getUser() {
        return user;
    }

    public long getTime() {
        return time;
    }

    public String getId() {
        return id;
    }
}
