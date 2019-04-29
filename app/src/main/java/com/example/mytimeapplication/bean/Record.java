package com.example.mytimeapplication.bean;



public class Record {
    private String title;
    private long startTime;
    private long stopTime;



    public Record(String title,long startTime, long stopTime) {
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
