package com.codelite.kr4k3rz.samachar.adapter;

import com.codelite.kr4k3rz.samachar.model.Entry;

import java.util.List;

/**
 * Created by kr4k3rz on 10/2/16.
 */

public class DataModel {
    String category_name;
    List<Entry> entry;

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
