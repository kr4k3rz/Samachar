package com.codelite.kr4k3rz.samachar.model;
public class SubCategory {
    private boolean checked = true;
    private String sub_name;
    private String sub_link_cached;
    private String sub_link_latest;
    private String updated_sub_link_cached;
    private String updated_sub_link_latest;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

   public String getUpdated_sub_link_cached() {
        return updated_sub_link_cached;
    }

    public void setUpdated_sub_link_cached(String updated_sub_link_cached) {
        this.updated_sub_link_cached = updated_sub_link_cached;
    }

   public String getUpdated_sub_link_latest() {
        return updated_sub_link_latest;
    }

    public void setUpdated_sub_link_latest(String updated_sub_link_latest) {
        this.updated_sub_link_latest = updated_sub_link_latest;
    }

    public String getSub_link_latest() {
        return sub_link_latest;
    }

    public void setSub_link_latest(String sub_link_latest) {
        this.sub_link_latest = sub_link_latest;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_link_cached() {
        return sub_link_cached;
    }

    public void setSub_link_cached(String sub_link_cached) {
        this.sub_link_cached = sub_link_cached;
    }
}
