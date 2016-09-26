package com.codelite.kr4k3rz.samachar.util;

import android.annotation.SuppressLint;
import android.util.Log;

import com.codelite.kr4k3rz.samachar.model.Entry;
import com.orhanobut.hawk.Hawk;

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
        if (entries != null)
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


    public static ArrayList<Integer> filterCategories(List<Entry> entries) {
        ArrayList<String> categories = new ArrayList<>();
        List<List<Entry>> main = new ArrayList<>();
        ArrayList<Integer> oldSizeFeed = new ArrayList<>();
        ArrayList<Integer> newFeedsLoaded = new ArrayList<>();

        int CATEGORY_NUMBER = 11;

        categories.add("Breaking");
        categories.add("Newspaper");
        categories.add("Headlines");
        categories.add("Local");
        categories.add("Opinion");
        categories.add("World");
        categories.add("Business");
        categories.add("Technology");
        categories.add("Entertainment");
        categories.add("Health");
        categories.add("Sport");

        List<Entry> breaking = new ArrayList<>();
        if (Hawk.contains("Breaking")) {
            List<Entry> entries1 = Hawk.get("Breaking");
            breaking.addAll(entries1);
            oldSizeFeed.add(breaking.size());

        }

        List<Entry> newspaper = new ArrayList<>();
        if (Hawk.contains("Newspaper")) {
            List<Entry> entries1 = Hawk.get("Newspaper");
            newspaper.addAll(entries1);
            oldSizeFeed.add(newspaper.size());
        }

        List<Entry> national = new ArrayList<>();
        if (Hawk.contains("Headlines")) {
            List<Entry> entries1 = Hawk.get("Headlines");
            national.addAll(entries1);
            oldSizeFeed.add(national.size());
        }
        List<Entry> local = new ArrayList<>();
        if (Hawk.contains("Local")) {
            List<Entry> entries1 = Hawk.get("Local");
            local.addAll(entries1);
            oldSizeFeed.add(local.size());
        }

        List<Entry> opinion = new ArrayList<>();
        if (Hawk.contains("Opinion")) {
            List<Entry> entries1 = Hawk.get("Opinion");
            opinion.addAll(entries1);
            oldSizeFeed.add(opinion.size());

        }

        List<Entry> world = new ArrayList<>();
        if (Hawk.contains("World")) {
            List<Entry> entries1 = Hawk.get("World");
            world.addAll(entries1);
            oldSizeFeed.add(world.size());

        }
        List<Entry> business = new ArrayList<>();
        if (Hawk.contains("Business")) {
            List<Entry> entries1 = Hawk.get("Business");
            business.addAll(entries1);
            oldSizeFeed.add(business.size());

        }
        List<Entry> technology = new ArrayList<>();
        if (Hawk.contains("Technology")) {
            List<Entry> entries1 = Hawk.get("Technology");
            technology.addAll(entries1);
            oldSizeFeed.add(technology.size());

        }
        List<Entry> entertain = new ArrayList<>();
        if (Hawk.contains("Entertainment")) {
            List<Entry> entries1 = Hawk.get("Entertainment");
            entertain.addAll(entries1);
            oldSizeFeed.add(entertain.size());

        }
        List<Entry> health = new ArrayList<>();
        if (Hawk.contains("Health")) {
            List<Entry> entries1 = Hawk.get("Health");
            health.addAll(entries1);
            oldSizeFeed.add(health.size());

        }
        List<Entry> sport = new ArrayList<>();
        if (Hawk.contains("Sport")) {
            List<Entry> entries1 = Hawk.get("Sport");
            sport.addAll(entries1);
            oldSizeFeed.add(sport.size());

        }

        int total_feeds = 0;
        int feeds_filtered = 0;
        /*
        TODO 1.all are not categorized check the percentage and make use of  unfiltered by showing
        filtered by category*/
        for (Entry entry : entries) {
            for (String s : entry.getCategories()) {
                //Breaking
                total_feeds++;
                if (s.equalsIgnoreCase("Breaking")
                        || s.equalsIgnoreCase("Breaking News")
                        || s.equalsIgnoreCase("Breaking News")
                        || s.equalsIgnoreCase("currentnews")
                        || s.equalsIgnoreCase("HEADLINES")
                        || s.equalsIgnoreCase("Feature")
                        || s.equalsIgnoreCase("highlight")
                        || s.equalsIgnoreCase("Top News")
                        || s.equalsIgnoreCase("CRIME")
                        || s.equalsIgnoreCase("BreakingNews")
                        || s.equalsIgnoreCase("ताजा अपडेट")
                        || s.equalsIgnoreCase("प्रमुख समाचार")
                        || s.equalsIgnoreCase("ताजा खबर")
                        || s.equalsIgnoreCase("मुख्य समाचार")
                        || s.equalsIgnoreCase("मुख्य खबर")
                        || s.equalsIgnoreCase("मेन स्टोरी")
                        || s.equalsIgnoreCase("सम–सामयिक")
                        || s.equalsIgnoreCase("आजको विशेष")
                        || s.equalsIgnoreCase("प्रमुख")
                        || s.equalsIgnoreCase("मुख्य")
                        || s.equalsIgnoreCase("विशेष")
                        || s.equalsIgnoreCase("बिशेष")
                        ) {
                    breaking.add(entry);
                    feeds_filtered++;
                    break;
                }//newspaper
                if (s.equalsIgnoreCase("newspaper")
                        || s.equalsIgnoreCase("पत्रपत्रिकाबाट")
                        || s.equalsIgnoreCase("आजको पत्रिका बाट")
                        || s.equalsIgnoreCase("छापामा")
                        || s.equalsIgnoreCase("पत्रपत्रिका")) {
                    newspaper.add(entry);
                    feeds_filtered++;
                    break;
                }//national
                if (s.equalsIgnoreCase("Nepal News")
                        || s.equalsIgnoreCase("समाचार")
                        || s.equalsIgnoreCase("Community News")
                        || s.equalsIgnoreCase("Night only")
                        || s.equalsIgnoreCase("राष्ट्रिय समाचार")
                        || s.equalsIgnoreCase("News of the day")
                        || s.equalsIgnoreCase("मेन स्टोरी")
                        || s.equalsIgnoreCase("राष्ट्रिय")
                        || s.equalsIgnoreCase("देश")
                        || s.equalsIgnoreCase("समाज")
                        || s.equalsIgnoreCase("थारु समाचार")
                        || s.equalsIgnoreCase("राजनीति")
                        || s.equalsIgnoreCase("फिचर")
                        ) {
                    national.add(entry);
                    feeds_filtered++;
                    break;
                }//local
                if (s.equalsIgnoreCase("LOCAL")
                        || s.equalsIgnoreCase("Uncategorized")
                        || s.equalsIgnoreCase("DIASPORA")
                        || s.equalsIgnoreCase("DIASPORA/LOCAL")
                        || s.equalsIgnoreCase("प्रबास/स्थानिय")
                        || s.equalsIgnoreCase(" बैदेशिक रोजगार")
                        || s.equalsIgnoreCase("प्रबास")
                        || s.equalsIgnoreCase("समाज")
                        || s.equalsIgnoreCase("अपराध सुरक्षा")
                        || s.equalsIgnoreCase("प्रवास | बैदेशिक रोजगार")
                        || s.equalsIgnoreCase("विश्व/प्रवास")
                        || s.equalsIgnoreCase("प्रवास")
                        || s.equalsIgnoreCase("स्थानिय")) {
                    local.add(entry);
                    feeds_filtered++;
                    break;
                }//opinion
                if (s.equalsIgnoreCase("OPINION")
                        || s.equalsIgnoreCase("Uncategorized")
                        || s.equalsIgnoreCase("विचार")
                        || s.equalsIgnoreCase("लेख")
                        || s.equalsIgnoreCase("बिचार")
                        || s.equalsIgnoreCase("फिचर")
                        || s.equalsIgnoreCase("अन्तर्वार्ता")
                        || s.equalsIgnoreCase("विचार/दृष्टिकोण")
                        || s.equalsIgnoreCase("अनुभूति")
                        || s.equalsIgnoreCase("बिचार / ब्लग")
                        || s.equalsIgnoreCase("बिचार | ब्लग")
                        || s.equalsIgnoreCase("बिचार / ब्लग")
                        || s.equalsIgnoreCase("ब्लग")
                        || s.equalsIgnoreCase("सम्यक संवाद")
                        || s.equalsIgnoreCase("घुम्दा घुम्दै")
                        || s.equalsIgnoreCase("विचार/अनुभूति")) {
                    opinion.add(entry);
                    feeds_filtered++;
                    break;
                }//world
                if (s.equalsIgnoreCase("अन्तराष्ट्रिय समाचार")
                        || s.equalsIgnoreCase("अन्तर्राष्ट्रिय समाचार")
                        || s.equalsIgnoreCase("बिश्व")
                        || s.equalsIgnoreCase("वर्ल्ड")
                        || s.equalsIgnoreCase("Odd World")
                        || s.equalsIgnoreCase("रोचक")
                        || s.equalsIgnoreCase("रोचक / विचित्र")
                        || s.equalsIgnoreCase("विचित्र संसार")                           //TODO break into rochak and world
                        || s.equalsIgnoreCase("फरक दुनिया")
                        || s.equalsIgnoreCase("आश्चर्यजनक")
                        || s.equalsIgnoreCase("अनौठो संसार")
                        || s.equalsIgnoreCase("बिचित्र संसार")
                        || s.equalsIgnoreCase("रोचक संसार")
                        || s.equalsIgnoreCase("रोचक | आश्चर्यजनक")
                        || s.equalsIgnoreCase("अन्तर्राष्ट्रिय")
                        || s.equalsIgnoreCase("अन्तराष्ट्रिय")
                        || s.equalsIgnoreCase("अन्तराष्ट्रीय")
                        || s.equalsIgnoreCase("अन्तर्राष्ट्रिय समाचार")
                        || s.equalsIgnoreCase("विश्व")) {
                    world.add(entry);
                    feeds_filtered++;
                    break;
                }//Business
                if (s.equalsIgnoreCase("अर्थनीति")
                        || s.equalsIgnoreCase("कृषि")
                        || s.equalsIgnoreCase("बातावरण-कृषि")
                        || s.equalsIgnoreCase("अर्थ")
                        || s.equalsIgnoreCase("अर्थ बजार")
                        || s.equalsIgnoreCase("अर्थ/बजार")
                        || s.equalsIgnoreCase("बजार")
                        || s.equalsIgnoreCase("ट्रेन्डिङ")
                        || s.equalsIgnoreCase("बिजनेस")
                        || s.equalsIgnoreCase("विकास")
                        || s.equalsIgnoreCase("पर्यटन")
                        || s.equalsIgnoreCase("अर्थ वाणिज्य")
                        || s.equalsIgnoreCase("अर्थ विशेष")
                        || s.equalsIgnoreCase("अर्थ खबर")
                        || s.equalsIgnoreCase("वाणिज्य")
                        || s.equalsIgnoreCase("अर्थतन्त्र")
                        || s.equalsIgnoreCase("बिजनेस समाचार")
                        || s.equalsIgnoreCase("बिजनेस प्रमुख होम")
                        || s.equalsIgnoreCase("बिजनेस प्रमुख")) {
                    business.add(entry);
                    feeds_filtered++;
                    break;
                }//Technology
                if (s.equalsIgnoreCase("सूचना प्रविधि-प्रमुख")
                        || s.equalsIgnoreCase("विज्ञान  प्रविधि")
                        || s.equalsIgnoreCase("सूचना प्रविधि")
                        || s.equalsIgnoreCase("विज्ञान")
                        || s.equalsIgnoreCase("प्रविधि")
                        || s.equalsIgnoreCase("विज्ञान तथा प्रविधी")
                        || s.equalsIgnoreCase("प्रविधि")
                        || s.equalsIgnoreCase("सूचना प्रविधि")) {
                    technology.add(entry);
                    feeds_filtered++;
                    break;
                }//Entertain
                if (s.equalsIgnoreCase("बलिउड/हलिउड")
                        || s.equalsIgnoreCase("बलिउड / हलिउड")
                        || s.equalsIgnoreCase("बलिउड")
                        || s.equalsIgnoreCase("हलिउड")
                        || s.equalsIgnoreCase("मनोरञ्जन")
                        || s.equalsIgnoreCase("म्युजिक अपडेट")
                        || s.equalsIgnoreCase("साहित्य आज")
                        || s.equalsIgnoreCase("प्रोफाइल")
                        || s.equalsIgnoreCase("बलिवुड")
                        || s.equalsIgnoreCase("चलचित्र")
                        || s.equalsIgnoreCase("मनोरञ्जन एप")
                        || s.equalsIgnoreCase("मनोरन्जन")
                        || s.equalsIgnoreCase("मनोरञ्जन")
                        || s.equalsIgnoreCase("विविध")
                        || s.equalsIgnoreCase("LITERATURE")
                        || s.equalsIgnoreCase("मनोरन्जन प्रमुख होम")
                        || s.equalsIgnoreCase("गशिप")
                        || s.equalsIgnoreCase("साहित्य")
                        || s.equalsIgnoreCase("ENT")
                        || s.equalsIgnoreCase("Entertainment Main")
                        || s.equalsIgnoreCase("नेपाली चलचित्र")
                        || s.equalsIgnoreCase("कला")
                        || s.equalsIgnoreCase("कला/ साहित्य")
                        || s.equalsIgnoreCase("Bollywood")
                        || s.equalsIgnoreCase("मनोरञ्जन प्रमुख")) {
                    entertain.add(entry);
                    feeds_filtered++;
                    break;
                }//Health
                if (s.equalsIgnoreCase("स्वास्थ्य")
                        || s.equalsIgnoreCase("श्वास्थ्य")
                        || s.equalsIgnoreCase("जीवन-दर्शन")
                        || s.equalsIgnoreCase("बातावरण")
                        || s.equalsIgnoreCase("यौन-शिक्षा")
                        || s.equalsIgnoreCase("जीवनशैली")
                        || s.equalsIgnoreCase("आजको राशिफल")
                        || s.equalsIgnoreCase("यौन")
                        || s.equalsIgnoreCase("स्वास्थ्य/जीवनशैली")) {
                    health.add(entry);
                    feeds_filtered++;
                    break;
                }//sport
                if (s.equalsIgnoreCase("SPORT")
                        || s.equalsIgnoreCase("Sports")
                        || s.equalsIgnoreCase("खेलकुद समाचार")
                        || s.equalsIgnoreCase("खेलकुद")
                        || s.equalsIgnoreCase("खेलकुद प्रमुख")
                        || s.equalsIgnoreCase("खेल")
                        || s.equalsIgnoreCase("खेलकुद फिचर")
                        ) {
                    sport.add(entry);
                    feeds_filtered++;
                    break;
                }

            }
        }

        main.add(breaking);
        main.add(newspaper);
        main.add(national);
        main.add(local);
        main.add(opinion);
        main.add(world);
        main.add(business);
        main.add(technology);
        main.add(entertain);
        main.add(health);
        main.add(sport);
        Log.i("TAG", "feeds categorized : " + feeds_filtered);
        Log.i("TAG", "Total Feeds Size : " + total_feeds);

        for (int i = 0; i < CATEGORY_NUMBER; i++) {
            List<Entry> processedFeeds;//new
            int oldSize = 0, newSize = 0;
            processedFeeds = main.get(i);
            processedFeeds = deleteDuplicate(processedFeeds); //delete duplicate feeds
            processedFeeds = deleteEnglishFeeds(processedFeeds);  //delete english feeds
            processedFeeds = sortByTime(processedFeeds);  //sort by time feeds feeds*//*
            Hawk.put(categories.get(i), processedFeeds);  //Hawk.put(nameFeed,List);
            if (processedFeeds.size() != 0)
                newSize = processedFeeds.size();
            if (oldSizeFeed.size() != 0)
                oldSize = oldSizeFeed.get(i);
            Log.i(Parse.class.getSimpleName(), "New Size: " + newSize + "\n Old Size" + oldSize);
            newFeedsLoaded.add(newSize - oldSize);  //new feeds loaded size

        }
        return newFeedsLoaded;
    }


}
