package com.example.andrew.secureyou;

public class CreateUser {

    public CreateUser()
    {}
        //displaying user data into the database together with the location
    public String name;
    public String email;

    public CreateUser(String name, String email, String password, String code, String isSharing, String lat, String lng, String imageUri) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.code = code;
        this.isSharing = isSharing;
        this.lat = lat;
        this.lng = lng;
        this.imageUri = imageUri;
    }

    public String password;
    public String code;
    public String isSharing;
    public String lat;
    public String lng;
    public String imageUri;
}
