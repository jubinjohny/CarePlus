package com.example.careplus.localStorage;

public class ScheduleViewCardData {
    public String day;
    public String date;
    public String FNshift;
    public String ANshift;

    public ScheduleViewCardData(String day, String date, String FNshift, String ANshift) {
        this.day = day;
        this.date = date;
        this.FNshift = FNshift;
        this.ANshift = ANshift;
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

    public String getFNshift() {
        return FNshift;
    }

    public void setFNshift(String FNshift) {
        this.FNshift = FNshift;
    }

    public String getANshift() {
        return ANshift;
    }

    public void setANshift(String ANshift) {
        this.ANshift = ANshift;
    }
}
