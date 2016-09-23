package com.codelite.kr4k3rz.samachar.util;

import android.annotation.SuppressLint;
import android.util.Log;

import com.codelite.kr4k3rz.samachar.model.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Parse {


    public static final EmptyImageGetter EMPTY_IMAGE_GETTER = new EmptyImageGetter();

    public static String parseImg(String html) {
        String url = null;
        Document doc = Jsoup.parse(html);

        Element imageElement = doc.select("img").first();

        if (!(imageElement == null)) {
            url = imageElement.attr("src");
        }

        return url;
    }

    public static String parseStr(String str) {
        String[] bits = str.split(" ");
        Log.i("TIME", String.valueOf(bits.length));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(bits[0]).append(bits[1].charAt(0));
        return String.valueOf(stringBuilder);

    }

    public static String getSource(String link) throws MalformedURLException {
        URL myUrl = new URL(link);
        String check = myUrl.getHost();
        int dot1, dot2;
        dot1 = check.indexOf(".");
        if (dot1 == -1) {
            return "";
        }
        dot2 = check.indexOf(".", dot1 + 1);
        if (dot2 == -1) {
            return check.substring(0, dot1);
        }
        return "" + check.substring(dot1 + 1, dot2);

    }

    public static List<Entry> sortByTime(List<Entry> entries) {

        Collections.sort(entries, new Comparator<Entry>() {
            @Override
            public int compare(Entry entry, Entry t1) {
                long d1 = 0;
                long d2 = 0;
                @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                try {
                    d1 = formatter.parse(entry.getDate()).getTime();
                    d2 = formatter.parse(t1.getDate()).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return (d1 > d2) ? -1 : 1;
            }
        });
        return entries;
    }

    public static List<Entry> deleteDuplicate(List<Entry> entries) {
        ArrayList<Entry> newFeeds = new ArrayList<>();
        Set<Entry> hs = new HashSet<>();
        hs.addAll(entries);
        newFeeds.clear();
        newFeeds.addAll(hs);
        return newFeeds;
    }


    public static List<Entry> deleteEnglishFeeds(List<Entry> entries) {
        ArrayList<Entry> entries1 = new ArrayList<>();
        for (Entry e : entries) {
            String s = e.getTitle();
            boolean atLeastOneAlpha = s.matches(".*[a-zA-Z]+.*");
            if (!atLeastOneAlpha) {
                entries1.add(e);
            }
        }
        return entries1;
    }



}
