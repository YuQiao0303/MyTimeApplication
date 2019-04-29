package com.example.mytimeapplication.bean;



public class Record {
    private int id;
    private String title;
    private long startTime;
    private long stopTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Record(int id, String title, long startTime, long stopTime) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }
}
