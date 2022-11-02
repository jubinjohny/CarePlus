package com.example.careplus.localStorage;

public class NewClinic {
    public String clinicID;
    public String name;
    public String type;
    public String email;
    public String phone;
    public String address;
    public String city;
    public String state;
    public String zipCode;
    public int weekdayStartHour;
    public int weekdayStartMin;
    public int weekdayEndHour;
    public int weekdayEndMin;
    public int weekendStartHour;
    public int weekendStartMin;
    public int weekendEndHour;
    public int weekendEndMin;
    public String webSite;
    public String password;
    public String approvedList;
    public String currentSchedule;
    public String nextSchedule;
    public String pendingRequest;

    public NewClinic(String clinicID, String name, String type, String email, String phone, String address, String city, String state, String zipCode, int weekdayStartHour, int weekdayStartMin, int weekdayEndHour, int weekdayEndMin, int weekendStartHour, int weekendStartMin, int weekendEndHour, int weekendEndMin, String webSite, String password, String approvedList, String currentSchedule, String nextSchedule, String pendingRequest) {
        this.clinicID = clinicID;
        this.name = name;
        this.type = type;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.weekdayStartHour = weekdayStartHour;
        this.weekdayStartMin = weekdayStartMin;
        this.weekdayEndHour = weekdayEndHour;
        this.weekdayEndMin = weekdayEndMin;
        this.weekendStartHour = weekendStartHour;
        this.weekendStartMin = weekendStartMin;
        this.weekendEndHour = weekendEndHour;
        this.weekendEndMin = weekendEndMin;
        this.webSite = webSite;
        this.password = password;
        this.approvedList = approvedList;
        this.currentSchedule = currentSchedule;
        this.nextSchedule = nextSchedule;
        this.pendingRequest = pendingRequest;
    }

    public NewClinic() {
    }

    public String getClinicID() {
        return clinicID;
    }

    public void setClinicID(String clinicID) {
        this.clinicID = clinicID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public int getWeekdayStartHour() {
        return weekdayStartHour;
    }

    public void setWeekdayStartHour(int weekdayStartHour) {
        this.weekdayStartHour = weekdayStartHour;
    }

    public int getWeekdayStartMin() {
        return weekdayStartMin;
    }

    public void setWeekdayStartMin(int weekdayStartMin) {
        this.weekdayStartMin = weekdayStartMin;
    }

    public int getWeekdayEndHour() {
        return weekdayEndHour;
    }

    public void setWeekdayEndHour(int weekdayEndHour) {
        this.weekdayEndHour = weekdayEndHour;
    }

    public int getWeekdayEndMin() {
        return weekdayEndMin;
    }

    public void setWeekdayEndMin(int weekdayEndMin) {
        this.weekdayEndMin = weekdayEndMin;
    }

    public int getWeekendStartHour() {
        return weekendStartHour;
    }

    public void setWeekendStartHour(int weekendStartHour) {
        this.weekendStartHour = weekendStartHour;
    }

    public int getWeekendStartMin() {
        return weekendStartMin;
    }

    public void setWeekendStartMin(int weekendStartMin) {
        this.weekendStartMin = weekendStartMin;
    }

    public int getWeekendEndHour() {
        return weekendEndHour;
    }

    public void setWeekendEndHour(int weekendEndHour) {
        this.weekendEndHour = weekendEndHour;
    }

    public int getWeekendEndMin() {
        return weekendEndMin;
    }

    public void setWeekendEndMin(int weekendEndMin) {
        this.weekendEndMin = weekendEndMin;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApprovedList() {
        return approvedList;
    }

    public void setApprovedList(String approvedList) {
        this.approvedList = approvedList;
    }

    public String getCurrentSchedule() {
        return currentSchedule;
    }

    public void setCurrentSchedule(String currentSchedule) {
        this.currentSchedule = currentSchedule;
    }

    public String getNextSchedule() {
        return nextSchedule;
    }

    public void setNextSchedule(String nextSchedule) {
        this.nextSchedule = nextSchedule;
    }

    public String getPendingRequest() {
        return pendingRequest;
    }

    public void setPendingRequest(String pendingRequest) {
        this.pendingRequest = pendingRequest;
    }
}
