package com.example.careplus.localStorage;

public class DoctorViewSchedule {
    public String day;
    public String startTime;
    public String endTime;
    public String clinicName;

    public DoctorViewSchedule(String day, String startTime, String endTime, String clinicName) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.clinicName = clinicName;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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
