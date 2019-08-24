package com.pkg.tin_tin;

public class UserData {
    private String name;
    private String address;
    private String emailid;

    public UserData(String name, String address, String emailid) {
        this.name = name;
        this.address = address;
        this.emailid = emailid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }
}
