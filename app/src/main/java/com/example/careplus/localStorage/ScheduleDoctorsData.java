package com.example.careplus.localStorage;

public class ScheduleDoctorsData {
    public String doctorName;
    public String startTime;
    public String endTime;
    public int image;
    public int setCheck;
    public String clinicName;
    public String day;
    public String clinicEmail;
    public String date;
    public String patientName;
    public String insuranceProvider;

    public ScheduleDoctorsData(String doctorName, String startTime, String endTime, int image, int setCheck, String clinicName, String day) {
        this.doctorName = doctorName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.image = image;
        this.setCheck = setCheck;
        this.clinicName = clinicName;
        this.day = day;
    }

    public ScheduleDoctorsData(String doctorName, String startTime, String endTime, int image, int setCheck, String clinicName, String day, String clinicEmail, String date, String patientName, String insuranceProvider) {
        this.doctorName = doctorName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.image = image;
        this.setCheck = setCheck;
        this.clinicName = clinicName;
        this.day = day;
        this.clinicEmail = clinicEmail;
        this.date = date;
        this.patientName = patientName;
        this.insuranceProvider = insuranceProvider;
    }

    public ScheduleDoctorsData(String doctorName, String startTime, String endTime, int image) {
        this.doctorName = doctorName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.image = image;
    }

    public ScheduleDoctorsData(String doctorName, String startTime, String endTime) {
        this.doctorName = doctorName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClinicEmail() {
        return clinicEmail;
    }

    public void setClinicEmail(String clinicEmail) {
        this.clinicEmail = clinicEmail;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public int getSetCheck() {
        return setCheck;
    }

    public void setSetCheck(int setCheck) {
        this.setCheck = setCheck;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
