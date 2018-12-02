package com.gc.materialdesigndemo.ui;

public class MyContacts {

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String email = "";
    public String name = "";

    public MyContacts(String mail, String name){
        this.email = mail;
        this.name = name;
    }
}
