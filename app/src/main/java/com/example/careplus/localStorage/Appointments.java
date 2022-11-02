package com.example.careplus.localStorage;

public class Appointments {
    public String clinicName;
    public String dayOfBooking;
    public String dateOfBooking;
    public String patientEmail;

    public Appointments( String clinicName, String dayOfBooking, String dateOfBooking, String patientEmail) {
        this.clinicName = clinicName;
        this.dayOfBooking = dayOfBooking;
        this.dateOfBooking = dateOfBooking;
        this.patientEmail = patientEmail;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getDayOfBooking() {
        return dayOfBooking;
    }

    public void setDayOfBooking(String dayOfBooking) {
        this.dayOfBooking = dayOfBooking;
    }

    public String getDateOfBooking() {
        return dateOfBooking;
    }

    public void setDateOfBooking(String dateOfBooking) {
        this.dateOfBooking = dateOfBooking;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }
}
