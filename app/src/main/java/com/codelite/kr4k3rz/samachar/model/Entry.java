package com.codelite.kr4k3rz.samachar.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Entry implements Serializable {
    @SerializedName("title")
    private String title;
    @SerializedName("publishedDate")
    private String date;
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("contentSnippet")
    private String contentSnippet;
    @SerializedName("link")
    private String link;
    @SerializedName("categories")
    private List<String> categories;
    private String linkFeed;
    private String titleFeed;
    private boolean clicked = false;

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }


    public String getTitleFeed() {
        return titleFeed;
    }

    public void setTitleFeed(String titleFeed) {
        this.titleFeed = titleFeed;
    }

    public String getLinkFeed() {
        return linkFeed;
    }

    public void setLinkFeed(String linkFeed) {
        this.linkFeed = linkFeed;
    }

    public String getContentSnippet() {
        return contentSnippet;
    }

    public List<String> getCategories() {
        return categories;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Entry other = (Entry) obj;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

}
