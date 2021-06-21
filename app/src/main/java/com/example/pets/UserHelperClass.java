package com.example.pets;

public class UserHelperClass {
    String petname,ownername,username,breed,email,password,conpassword;

    public UserHelperClass() {



    }

    public UserHelperClass(String petname, String ownername, String username, String breed, String email, String password, String conpassword) {
        this.petname = petname;
        this.ownername = ownername;
        this.username = username;
        this.breed = breed;
        this.email = email;
        this.password = password;
        this.conpassword = conpassword;
    }

    public String getPetname() {
        return petname;
    }

    public void setPetname(String petname) {
        this.petname = petname;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConpassword() {
        return conpassword;
    }

    public void setConpassword(String conpassword) {
        this.conpassword = conpassword;
    }
}
