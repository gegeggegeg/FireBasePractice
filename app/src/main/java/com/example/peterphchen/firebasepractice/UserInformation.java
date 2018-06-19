package com.example.peterphchen.firebasepractice;

public class UserInformation {
    private String emailAddress = null;
    private String name = null;
    private Long phonenumber = null;

    public UserInformation() {
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getName() {
        return name;
    }

    public Long getPhonenumber() {
        return phonenumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhonenumber(Long phonenumber) {
        this.phonenumber = phonenumber;
    }
}
