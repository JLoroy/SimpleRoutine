package com.example.simpleroutine;

public class History {
    private String datetime;
    private int stepID;

    public History(String datetime, int stepID) {
        this.datetime = datetime;
        this.stepID = stepID;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getStepID() {
        return stepID;
    }

    public void setStepID(int stepID) {
        this.stepID = stepID;
    }
}