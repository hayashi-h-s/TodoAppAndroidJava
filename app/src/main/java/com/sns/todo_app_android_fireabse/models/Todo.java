package com.sns.todo_app_android_fireabse.models;

import com.google.firebase.Timestamp;

public class Todo {

    private String id;
    private String name;
    private String test;
    private Timestamp createdAt;

    public Todo() {
    }

    public Todo(String id, String name, String test,Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.test = test;
        this.createdAt = createdAt;
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

    public String getTest() {
        return test;
    }

    public void getTest(String test) {
        this.test = test;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}