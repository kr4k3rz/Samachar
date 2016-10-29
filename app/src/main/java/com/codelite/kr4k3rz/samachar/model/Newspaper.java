package com.codelite.kr4k3rz.samachar.model;

import java.util.ArrayList;

public class Newspaper {
    private static final String POSTFIX = "&num=30";  //cached and max feeds
    private ArrayList<Newspaper> newspapers = new ArrayList<>();
    public String name;
    public String link;

    public Newspaper(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public Newspaper() {
        newspapers.add(new Newspaper("OnlineKhabar", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.onlinekhabar.com/feed" + POSTFIX));
        newspapers.add(new Newspaper("NepaliHeadlines", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://nepaliheadlines.com/feed" + POSTFIX));
        newspapers.add(new Newspaper("NayaSamachar", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://nayasamachar.com/?feed=rss2" + POSTFIX));
        newspapers.add(new Newspaper("SambadMedia", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.sambadmedia.com/?feed=rss2" + POSTFIX));
        newspapers.add(new Newspaper("TajaOnlinekhabar", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.tajaonlinekhabar.com/feed" + POSTFIX));
        newspapers.add(new Newspaper("Lokaantar", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://lokaantar.com/feed" + POSTFIX));
        newspapers.add(new Newspaper("MediaNp", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://medianp.com/feed" + POSTFIX));
        newspapers.add(new Newspaper("eNepaliKhabar", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.enepalikhabar.com/feed" + POSTFIX));
        newspapers.add(new Newspaper("NepalAaja", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://nepalaaja.com/feed" + POSTFIX));
        newspapers.add(new Newspaper("ONSNepal", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.onsnews.com/feed" + POSTFIX));
        newspapers.add(new Newspaper("NayaPage", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.nayapage.com/feed" + POSTFIX));
        newspapers.add(new Newspaper("SouryaDaily", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.souryadaily.com/feed" + POSTFIX));
        newspapers.add(new Newspaper("RajdhaniDaily", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://rajdhanidaily.com/feed" + POSTFIX));
        newspapers.add(new Newspaper("Chardisa", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://chardisha.com/feed" + POSTFIX));
        newspapers.add(new Newspaper("NepaliHealth", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.nepalihealth.com/feed" + POSTFIX));
        newspapers.add(new Newspaper("BizKhabar", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.bizkhabar.com/feed" + POSTFIX));
        newspapers.add(new Newspaper("EverestDainik", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.everestdainik.com/feed" + POSTFIX));
    }



    public ArrayList<Newspaper> getNewspapersList() {
        return newspapers;
    }

    public ArrayList<String> getLinksList() {
        ArrayList<String> linksList = new ArrayList<>();
        for (int i = 0; i < newspapers.size(); i++) {
            Newspaper newspaper = newspapers.get(i);
            linksList.add(newspaper.link);
        }
        return linksList;
    }


}
