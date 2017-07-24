package com.asuscomm.yangyinetwork.todos.models;

/**
 * Created by jaeyoung on 7/24/17.
 */

public class Todo {

    public String description;
    public boolean isCompleted;

    public Todo(String s, boolean b) {
        description = s;
        isCompleted = b;
    }
}
