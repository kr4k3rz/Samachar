package com.codelite.kr4k3rz.samachar.model;

import com.codelite.kr4k3rz.samachar.model.search.EntriesItem;

import java.io.Serializable;
import java.util.List;


public class Subscribe implements Serializable {
    private EntriesItem entriesItem;
    private List<com.codelite.kr4k3rz.samachar.model.feed.EntriesItem> entriesItemList;

    public EntriesItem getEntriesItem() {
        return entriesItem;
    }

    public void setEntriesItem(EntriesItem entriesItem) {
        this.entriesItem = entriesItem;
    }

    public List<com.codelite.kr4k3rz.samachar.model.feed.EntriesItem> getEntriesItemList() {
        return entriesItemList;
    }

    public void setEntriesItemList(List<com.codelite.kr4k3rz.samachar.model.feed.EntriesItem> entriesItemList) {
        this.entriesItemList = entriesItemList;
    }
}
