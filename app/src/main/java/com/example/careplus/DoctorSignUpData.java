package com.example.careplus;

public class DoctorSignUpData {
    public String name, email, licence, major, password;

    public DoctorSignUpData() {

    }

    public DoctorSignUpData(String name, String email, String licence, String major, String password) {
        this.name = name;
        this.email = email;
        this.licence = licence;
        this.major = major;
        this.password = password;
    }
}
