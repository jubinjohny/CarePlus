package com.example.careplus;

public class ClinicSignUpData {
    public String name, email, id, location, password;

    public ClinicSignUpData() {

    }

    public ClinicSignUpData(String name, String email, String id, String location, String password) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.location = location;
        this.password = password;
    }
}
