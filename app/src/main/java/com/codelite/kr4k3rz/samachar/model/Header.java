package com.codelite.kr4k3rz.samachar.model;

import java.io.Serializable;


public class Header implements Serializable {
    private String firstName;
    private String secondName;

    public Header(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getSecondName() {
        return secondName;
    }

}
