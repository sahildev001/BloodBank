package com.example.bloodbank.Model;

public class Donors {
    String name,email,phoneNo,bloodGroup,address;
    Double lat,lng;

    public Donors(String name, String email, String phoneNo, String bloodGroup, String address, Double lat, Double lng) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public Donors() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
