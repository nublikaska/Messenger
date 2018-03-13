package com.example.denis.messenger.Models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Denis on 12.03.2018.
 */

public class Chats {
    private Map<String, Message> map;

    public Chats() {}

    public Chats(Map<String, Message> map) {
        this.map= map;
    }

    public Map<String, Message> getMap() {
        return map;
    }

}
