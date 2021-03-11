package com.sns.todo_app_android_fireabse.models;

import com.google.firebase.Timestamp;

public class Todo {

    private String id;
    private String name;
    private Timestamp timestamp;

    public Todo() {
    }

    public Todo(String id, String name, Timestamp timestamp) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}