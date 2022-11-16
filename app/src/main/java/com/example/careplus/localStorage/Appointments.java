package com.example.careplus.localStorage;

public class Appointments {
    public String clinicName;
    public String clinicEmail;
    public String patientName;
    public String appointmentTime;
    public String appointmentDate;
    public String patientEmail;

    public Appointments() {
    }

    public Appointments(String clinicName, String clinicEmail, String patientName, String appointmentTime, String appointmentDate, String patientEmail) {
        this.clinicName = clinicName;
        this.clinicEmail = clinicEmail;
        this.patientName = patientName;
        this.appointmentTime = appointmentTime;
        this.appointmentDate = appointmentDate;
        this.patientEmail = patientEmail;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getClinicEmail() {
        return clinicEmail;
    }

    public void setClinicEmail(String clinicEmail) {
        this.clinicEmail = clinicEmail;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }
}
