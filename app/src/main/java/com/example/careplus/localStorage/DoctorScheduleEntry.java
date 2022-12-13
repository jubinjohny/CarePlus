package com.example.careplus.localStorage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class DoctorScheduleEntry {
    public String startTime;
    public String endTime;
    public Map<String, ArrayList<String>> timeslots;

    public DoctorScheduleEntry(String startTime, String endTime, Map<String, ArrayList<String>> timeslots) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeslots = timeslots;
    }

    public Map<String, ArrayList<String>> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(Map<String, ArrayList<String>> timeslots) {
        this.timeslots = timeslots;
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
