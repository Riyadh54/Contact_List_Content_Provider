package com.example.contactslist.Model;

public class ContactsModel {
    private String id,name,number,email,otherDetails;
    private  byte[] image;

    public ContactsModel(String id, String name, String number, String email, String otherDetails, byte[] image) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.email = email;
        this.otherDetails = otherDetails;
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }
}
