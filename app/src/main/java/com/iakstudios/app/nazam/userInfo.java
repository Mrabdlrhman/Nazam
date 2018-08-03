package com.iakstudios.app.nazam;


import java.util.ArrayList;
import java.util.List;

public class userInfo {
    public String name,role;
    public String mobile;
    public ArrayList<locate> locations;

    public userInfo() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ArrayList<locate> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<locate> locations) {
        this.locations = locations;
    }

    public userInfo(String name, String role, String mobile, ArrayList<locate> locations) {
        this.name = name;
        this.role = role;
        this.mobile = mobile;
        this. locations= locations;
    }

}
