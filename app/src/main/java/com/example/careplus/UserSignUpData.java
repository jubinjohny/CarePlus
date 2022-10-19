package com.example.careplus;

public class UserSignUpData {
    public String name, email, dob, password;

    public UserSignUpData() {

    }

    public UserSignUpData(String name, String email, String dob, String password) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.password = password;
    }
}
