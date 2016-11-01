package com.codelite.kr4k3rz.samachar.model;

import java.util.ArrayList;

public class NewspaperNP {
    private static final String POSTFIX = "&num=-1";  //cached and max feeds
    public String name;
    public String link;
    private final ArrayList<NewspaperNP> newspapers = new ArrayList<>();

    private NewspaperNP(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public NewspaperNP() {
        newspapers.add(new NewspaperNP("OnlineKhabar", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.onlinekhabar.com/feed" + POSTFIX));
        newspapers.add(new NewspaperNP("NepaliHeadlines", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://nepaliheadlines.com/feed" + POSTFIX));
        newspapers.add(new NewspaperNP("NayaSamachar", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://nayasamachar.com/?feed=rss2" + POSTFIX));
        newspapers.add(new NewspaperNP("SambadMedia", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.sambadmedia.com/?feed=rss2" + POSTFIX));
        newspapers.add(new NewspaperNP("TajaOnlinekhabar", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.tajaonlinekhabar.com/feed" + POSTFIX));
        newspapers.add(new NewspaperNP("Lokaantar", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://lokaantar.com/feed" + POSTFIX));
        newspapers.add(new NewspaperNP("MediaNp", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://medianp.com/feed" + POSTFIX));
        newspapers.add(new NewspaperNP("eNepaliKhabar", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.enepalikhabar.com/feed" + POSTFIX));
        newspapers.add(new NewspaperNP("NepalAaja", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://nepalaaja.com/feed" + POSTFIX));
        newspapers.add(new NewspaperNP("ONSNepal", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.onsnews.com/feed" + POSTFIX));
        newspapers.add(new NewspaperNP("NayaPage", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.nayapage.com/feed" + POSTFIX));
        newspapers.add(new NewspaperNP("SouryaDaily", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.souryadaily.com/feed" + POSTFIX));
        newspapers.add(new NewspaperNP("RajdhaniDaily", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://rajdhanidaily.com/feed" + POSTFIX));
        newspapers.add(new NewspaperNP("Chardisa", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://chardisha.com/feed" + POSTFIX));
        newspapers.add(new NewspaperNP("NepaliHealth", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.nepalihealth.com/feed" + POSTFIX));
        newspapers.add(new NewspaperNP("BizKhabar", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.bizkhabar.com/feed" + POSTFIX));
        newspapers.add(new NewspaperNP("EverestDainik", "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.everestdainik.com/feed" + POSTFIX));
    }


    public ArrayList<NewspaperNP> getNewspapersList() {
        return newspapers;
    }

    public ArrayList<String> getLinksList() {
        ArrayList<String> linksList = new ArrayList<>();
        for (int i = 0; i < newspapers.size(); i++) {
            NewspaperNP newspaper = newspapers.get(i);
            linksList.add(newspaper.link);
        }
        return linksList;
    }


}
