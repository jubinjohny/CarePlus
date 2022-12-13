package com.example.careplus.localStorage;

public class NewAppointmentRequest {
    public String patientEmail;
    public String patientName;
    public String appointmentDate;
    public String appointmentTime;
    public String insuranceProvider;
    public String ID;
    public int image;
    public String clinicEmail;
    public String clinicName;
    public String doctorName;
    public String day;

    public NewAppointmentRequest(String patientEmail, String patientName, String appointmentDate,String appointmentTime, String insuranceProvider, String ID, int image) {
        this.patientEmail = patientEmail;
        this.patientName = patientName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.insuranceProvider = insuranceProvider;
        this.ID = ID;
        this.image = image;
    }

    public NewAppointmentRequest(String patientEmail, String patientName, String appointmentDate, String appointmentTime, String insuranceProvider, String ID, int image, String clinicName, String doctorName) {
        this.patientEmail = patientEmail;
        this.patientName = patientName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.insuranceProvider = insuranceProvider;
        this.ID = ID;
        this.image = image;
        this.clinicName = clinicName;
        this.doctorName = doctorName;
    }

    public NewAppointmentRequest(String patientEmail, String patientName, String appointmentDate, String appointmentTime, String insuranceProvider, String ID, int image, String clinicName, String doctorName, String day) {
        this.patientEmail = patientEmail;
        this.patientName = patientName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.insuranceProvider = insuranceProvider;
        this.ID = ID;
        this.image = image;
        this.clinicName = clinicName;
        this.doctorName = doctorName;
        this.day = day;
    }

    public NewAppointmentRequest(String clinicEmail, String clinicName, String appointmentDate, String appointmentTime, String insuranceProvider, String ID, int image, String doctorName) {
        this.clinicEmail = clinicEmail;
        this.patientName = clinicName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.insuranceProvider = insuranceProvider;
        this.ID = ID;
        this.image = image;
        this.clinicName = clinicName;
        this.doctorName = doctorName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getClinicEmail() {
        return clinicEmail;
    }

    public void setClinicEmail(String clinicEmail) {
        this.clinicEmail = clinicEmail;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
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
}
