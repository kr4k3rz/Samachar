package com.codelite.kr4k3rz.samachar.model;

import java.util.ArrayList;

/**
 * Created by kr4k3rz on 9/4/16.
 */

public class Category {
    private String name_category;
    private ArrayList<SubCategory> sub_category;

    public ArrayList<SubCategory> getSub_category() {
        return sub_category;
    }

    public void setSub_category(ArrayList<SubCategory> sub_category) {
        this.sub_category = sub_category;
    }

    public String getName_category() {
        return name_category;
    }

    public void setName_category(String name_category) {
        this.name_category = name_category;
    }


}
