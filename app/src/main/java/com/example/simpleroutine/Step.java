package com.example.simpleroutine;

public class Step {
    private int id;
    private String text;
    private int order;

    public Step(int id, String text, int order) {
        this.id = id;
        this.text = text;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}