package com.iakstudios.app.nazam;


import java.util.ArrayList;
import java.util.List;

public class userInfo {
    public String name,role;
    public int mobile;
    public ArrayList<locate> locations;

    public userInfo(String name, String role, int mobile,ArrayList<locate> locations) {
        this.name = name;
        this.role = role;
        this.mobile = mobile;
        this. locations= locations;
    }
    public userInfo(ArrayList<locate> locations) {

        this. locations= locations;
    }
}

//class UserInformaions {
//    String name;
//    String mobile;
//    String role;
//
//    public UserInformaions(String name, String mobile, String role){
//        this.name = name;
//        this.mobile = mobile;
//        this.role = role;
//    }
//
//}
//
//class UserLocation {
//    String latitude;
//    String longitude;
//    UserLocation(String latitude, String longitude){
//        this.latitude = latitude;
//        this.longitude = longitude;
//    }
//}
//
//class Locations {
//    UserLocation[] x= new UserLocation[1];
//    Locations(UserLocation x){
//        this.x[0] = x;
//    }

