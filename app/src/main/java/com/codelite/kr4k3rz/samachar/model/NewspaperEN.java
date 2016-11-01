package com.codelite.kr4k3rz.samachar.model;


import java.util.ArrayList;

public class NewspaperEN {
    private static final String POSTFIX = "&num=-1";
    public String name;
    public String link;
    private final ArrayList<NewspaperEN> newspapers = new ArrayList<>();

    private NewspaperEN(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public NewspaperEN() {
        newspapers.add(new NewspaperEN("TheHimalayanTimes", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://thehimalayantimes.com/feed" + POSTFIX));
        newspapers.add(new NewspaperEN("OnlineKhabar", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://english.onlinekhabar.com/feed" + POSTFIX));
        newspapers.add(new NewspaperEN("DainikPost", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://dainikpost.com/feed" + POSTFIX));
    }


    public ArrayList<NewspaperEN> getNewspapersList() {
        return newspapers;
    }

    public ArrayList<String> getLinksList() {
        ArrayList<String> linksList = new ArrayList<>();
        for (int i = 0; i < newspapers.size(); i++) {
            NewspaperEN newspaper = newspapers.get(i);
            linksList.add(newspaper.link);
        }
        return linksList;
    }




}
