package com.example.careplus.localStorage;

public class UpcomingScheduleData {
    public String day;
    public String date;
    public String startTime;
    public String endTime;

    public UpcomingScheduleData(String day, String date, String startTime, String endTime) {
        this.day = day;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
