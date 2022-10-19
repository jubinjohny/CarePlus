package com.example.careplus.localStorage;

import com.example.careplus.R;

public class DoctorViewCardData {
    public String doctorName;
    public String specialization;
    public String email;
    public String ID;
    public int image;
    public String requestPending;

    public DoctorViewCardData(String doctorName, String specialization, String email, String ID, int image, String requestPending) {
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.email = email;
        this.ID = ID;
        this.image = image;
        this.requestPending = requestPending;
    }

    public String getRequestPending() {
        return requestPending;
    }

    public void setRequestPending(String requestPending) {
        this.requestPending = requestPending;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
