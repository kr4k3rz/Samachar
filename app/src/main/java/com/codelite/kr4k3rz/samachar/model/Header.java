package com.codelite.kr4k3rz.samachar.model;

import java.io.Serializable;


public class Header implements Serializable {
    private final String firstName;
    private final String secondName;
    private int numFeeds;

    public Header(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public int getNumFeeds() {
        return numFeeds;
    }

    public void setNumFeeds(int numFeeds) {
        this.numFeeds = numFeeds;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getSecondName() {
        return secondName;
    }

}
