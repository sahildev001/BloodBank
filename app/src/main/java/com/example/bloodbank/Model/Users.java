package com.example.bloodbank.Model;

public class Users {

    String name,email,phoneno,password,profilepic,UserId;

    public Users(String name, String email, String phoneno, String password, String profilepic,String UserId) {
        this.name = name;
        this.email = email;
        this.phoneno = phoneno;
        this.password = password;
        this.profilepic = profilepic;
        this.UserId=UserId;
    }
        //sign up
    public Users(String name, String email, String phoneno, String password) {
        this.name = name;
        this.email = email;
        this.phoneno = phoneno;
        this.password = password;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public Users() {
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

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
