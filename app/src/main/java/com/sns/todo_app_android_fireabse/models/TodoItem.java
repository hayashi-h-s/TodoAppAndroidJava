package com.sns.todo_app_android_fireabse.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class TodoItem implements Serializable {

    private String id;
    private String name;
    private Timestamp timestamp;

    public TodoItem() {
    }

    public TodoItem(String id, String name, Timestamp timestamp) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setTodoId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return name;
    }

    public void setItemName(String name) {
        this.name = name;
    }

    public Timestamp getItemTimestamp() {
        return timestamp;
    }

    public void setItemTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}