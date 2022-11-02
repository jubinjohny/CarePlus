package com.example.careplus.localStorage;

public class ClinicsViewData {
    public String ID;
    public String name;
    public String city;
    public String email;
    public int image;

    public ClinicsViewData(String ID, String name, String city, String email, int image) {
        this.ID = ID;
        this.name = name;
        this.city = city;
        this.email = email;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
