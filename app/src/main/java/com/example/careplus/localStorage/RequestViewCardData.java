package com.example.careplus.localStorage;

public class RequestViewCardData {
    public String clinicName;
    public String address;
    public String email;
    public String ID;
    public int image;

    public RequestViewCardData(String clinicName, String address, String email, String ID, int image) {
        this.clinicName = clinicName;
        this.address = address;
        this.email = email;
        this.ID = ID;
        this.image = image;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
