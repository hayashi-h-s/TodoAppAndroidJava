package com.sns.todo_app_android_fireabse.models;

import android.util.Log;

public class Todo {

    private String id;
    private String name;

    public Todo() {

    }

    public Todo(String id, String name) {
        this.id = id;
        this.name = name;
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

    /**
     * JsonObjectからCommunityBoardMessageを作成して返します
     *
     * @return
     */
    public static Todo toObject(String name, String id) {

        Log.d("TAG" ,"public static Todo toObject(String name, String id) {");

        Todo todo = new Todo();
        todo.name = name;
        todo.id = id;

        Log.d("TAG" ,"todo.name =" +todo.name  + " todo.id = " +todo.id );

        return todo;
    }
}