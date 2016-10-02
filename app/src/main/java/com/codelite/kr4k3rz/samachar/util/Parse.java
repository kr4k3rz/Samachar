package com.codelite.kr4k3rz.samachar.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.model.WhichCategory;
import com.orhanobut.hawk.Hawk;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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


/* a class that handles parsing*/
public class Parse {

    public static final EmptyImageGetter EMPTY_IMAGE_GETTER = new EmptyImageGetter();
    private static final String TAG = Parse.class.getSimpleName();

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

    /*
    * filters the feeds categories (eg. World) passed
    * @param entries feed entries passed*/

    public static ArrayList<Integer> filterCategories(List<Entry> entries, Context context) throws UnsupportedEncodingException {
        List<String> categories = new ArrayList<>();
        List<List<Entry>> main = new ArrayList<>();
        float total_feeds = 0;
        float feeds_filtered = 0;
        int CATEGORY_NUMBER = 11;
        List<Integer> mPriorSize = new ArrayList<>();
        List<Integer> mFeedSize = new ArrayList<>();
        List<Object> objects = new ArrayList<>();

        /*initialization for Storage DB*/
        categories.add(WhichCategory.BREAKING.name());
        categories.add(WhichCategory.NEWSPAPER.name());
        categories.add(WhichCategory.NATIONAL.name());
        categories.add(WhichCategory.LOCAL.name());
        categories.add(WhichCategory.OPINION.name());
        categories.add(WhichCategory.WORLD.name());
        categories.add(WhichCategory.BUSINESS.name());
        categories.add(WhichCategory.TECHNOLOGY.name());
        categories.add(WhichCategory.ENTERTAINMENT.name());
        categories.add(WhichCategory.HEALTH.name());
        categories.add(WhichCategory.SPORT.name());

        List<Entry> breaking = new ArrayList<>();
        if (Hawk.contains(WhichCategory.BREAKING.name())) {
            List<Entry> entries1 = Hawk.get(WhichCategory.BREAKING.name());
            breaking.addAll(entries1);
            mPriorSize.add(breaking.size());

        } else mPriorSize.add(0);

        List<Entry> newspaper = new ArrayList<>();
        if (Hawk.contains(WhichCategory.NEWSPAPER.name())) {
            List<Entry> entries1 = Hawk.get(WhichCategory.NEWSPAPER.name());
            newspaper.addAll(entries1);
            mPriorSize.add(newspaper.size());
        } else mPriorSize.add(0);

        List<Entry> national = new ArrayList<>();
        if (Hawk.contains(WhichCategory.NATIONAL.name())) {
            List<Entry> entries1 = Hawk.get(WhichCategory.NATIONAL.name());
            national.addAll(entries1);
            mPriorSize.add(national.size());
        } else mPriorSize.add(0);

        List<Entry> local = new ArrayList<>();
        if (Hawk.contains(WhichCategory.LOCAL.name())) {
            List<Entry> entries1 = Hawk.get(WhichCategory.LOCAL.name());
            local.addAll(entries1);
            mPriorSize.add(local.size());
        } else mPriorSize.add(0);

        List<Entry> opinion = new ArrayList<>();
        if (Hawk.contains(WhichCategory.OPINION.name())) {
            List<Entry> entries1 = Hawk.get(WhichCategory.OPINION.name());
            opinion.addAll(entries1);
            mPriorSize.add(opinion.size());
        } else mPriorSize.add(0);

        List<Entry> world = new ArrayList<>();
        if (Hawk.contains(WhichCategory.WORLD.name())) {
            List<Entry> entries1 = Hawk.get(WhichCategory.WORLD.name());
            world.addAll(entries1);
            mPriorSize.add(world.size());
        } else mPriorSize.add(0);

        List<Entry> business = new ArrayList<>();
        if (Hawk.contains(WhichCategory.BUSINESS.name())) {
            List<Entry> entries1 = Hawk.get(WhichCategory.BUSINESS.name());
            business.addAll(entries1);
            mPriorSize.add(business.size());

        } else mPriorSize.add(0);

        List<Entry> technology = new ArrayList<>();
        if (Hawk.contains(WhichCategory.TECHNOLOGY.name())) {
            List<Entry> entries1 = Hawk.get(WhichCategory.TECHNOLOGY.name());
            technology.addAll(entries1);
            mPriorSize.add(technology.size());

        } else mPriorSize.add(0);

        List<Entry> entertain = new ArrayList<>();
        if (Hawk.contains(WhichCategory.ENTERTAINMENT.name())) {
            List<Entry> entries1 = Hawk.get(WhichCategory.ENTERTAINMENT.name());
            entertain.addAll(entries1);
            mPriorSize.add(entertain.size());

        } else mPriorSize.add(0);

        List<Entry> health = new ArrayList<>();
        if (Hawk.contains(WhichCategory.HEALTH.name())) {
            List<Entry> entries1 = Hawk.get(WhichCategory.HEALTH.name());
            health.addAll(entries1);
            mPriorSize.add(health.size());
        } else mPriorSize.add(0);

        List<Entry> sport = new ArrayList<>();
        if (Hawk.contains(WhichCategory.SPORT.name())) {
            List<Entry> entries1 = Hawk.get(WhichCategory.SPORT.name());
            sport.addAll(entries1);
            mPriorSize.add(sport.size());
        } else mPriorSize.add(0);



        /*
        TODO 1.all are not categorized check the percentage and make use of  unfiltered by showing
        filtered by category*/

        //for special category
        for (Entry entry : entries) {

            if (entry.getLink().contains("http://www.nepalihealth.com/") || entry.getLink().contains("http://nepalhealthnews.com")) {
                health.add(entry);
                feeds_filtered++;
            }

            if (entry.getLink().contains("http://screennepal.com/")) {
                entertain.add(entry);
                feeds_filtered++;
            }

            if (entry.getLink().contains("http://www.karobardaily.com")) {
                business.add(entry);
                feeds_filtered++;
            }

            if (entry.getLink().contains("http://feedproxy.google.com/~r/Aakar/")) {
                technology.add(entry);
                feeds_filtered++;
            }
//for category with zero size or no category

//for category
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
                        || s.equalsIgnoreCase("सुरक्षा गतिविधि")
                        || s.equalsIgnoreCase("BreakingNews")
                        || s.equalsIgnoreCase("समाचार")
                        || s.equalsIgnoreCase("भर्खरै")
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
                        ) {
                    breaking.add(entry);
                    feeds_filtered++;
                    break;
                } else//newspaper
                    if (s.equalsIgnoreCase("newspaper")
                            || s.equalsIgnoreCase("पत्रपत्रिकाबाट")
                            || s.equalsIgnoreCase("आजको पत्रिका बाट")
                            || s.equalsIgnoreCase("छापामा")
                            || s.equalsIgnoreCase("पत्रपत्रिका")) {
                        newspaper.add(entry);
                        feeds_filtered++;
                        break;
                    }//national
                    else if (s.equalsIgnoreCase("Nepal News")
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
                            || s.equalsIgnoreCase("राजनीति")
                            || s.equalsIgnoreCase("फिचर")
                            ) {
                        national.add(entry);
                        feeds_filtered++;
                        break;
                    }//local
                    else if (s.equalsIgnoreCase("LOCAL")
                            || s.equalsIgnoreCase("Uncategorized")
                            || s.equalsIgnoreCase("DIASPORA")
                            || s.equalsIgnoreCase("DIASPORA/LOCAL")
                            || s.equalsIgnoreCase("प्रबास/स्थानिय")
                            || s.equalsIgnoreCase(" बैदेशिक रोजगार")
                            || s.equalsIgnoreCase("रोजगार")
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
                    else if (s.equalsIgnoreCase("OPINION")
                            || s.equalsIgnoreCase("Uncategorized")
                            || s.equalsIgnoreCase("विचार")
                            || s.equalsIgnoreCase("लेख")
                            || s.equalsIgnoreCase("लेख")
                            || s.equalsIgnoreCase("आलेख")
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
                    else if (s.equalsIgnoreCase("अन्तराष्ट्रिय समाचार")
                            || s.equalsIgnoreCase("अन्तर्राष्ट्रिय समाचार")
                            || s.equalsIgnoreCase("बिविध")
                            || s.equalsIgnoreCase("विविध")
                            || s.equalsIgnoreCase("सम–सामयिक")
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
                    else if (s.equalsIgnoreCase("अर्थनीति")
                            || s.equalsIgnoreCase("अर्थतन्त्र फिचर")
                            || s.equalsIgnoreCase("अर्थतन्त्र फिचर")
                            || s.equalsIgnoreCase("अर्थ | वाणिज्य | बजार")
                            || s.contains("अर्थ")
                            || s.equalsIgnoreCase("कृषि")
                            || s.equalsIgnoreCase("बातावरण-कृषि")
                            || s.equalsIgnoreCase("अर्थ")
                            || s.equalsIgnoreCase("अर्थ बजार")
                            || s.equalsIgnoreCase("अर्थ/बजार")
                            || s.equalsIgnoreCase("कर्पोरेट")
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
                    else if (s.equalsIgnoreCase("सूचना प्रविधि-प्रमुख")
                            || s.equalsIgnoreCase("विज्ञान  प्रविधि")
                            || s.equalsIgnoreCase("सूचना प्रविधि")
                            || s.equalsIgnoreCase("बिज्ञान प्रबिधि")
                            || s.equalsIgnoreCase("विज्ञान प्रविधि")
                            || s.equalsIgnoreCase("विज्ञान")
                            || s.equalsIgnoreCase("प्रविधि")
                            || s.equalsIgnoreCase("Nepal Telecme")
                            || s.equalsIgnoreCase("विज्ञान तथा प्रविधी")
                            || s.equalsIgnoreCase("प्रविधि")
                            || s.equalsIgnoreCase("सूचना प्रविधि")) {
                        technology.add(entry);
                        feeds_filtered++;
                        break;
                    }//Entertain
                    else if (s.equalsIgnoreCase("बलिउड/हलिउड")
                            || s.equalsIgnoreCase("बलिउड / हलिउड")
                            || s.equalsIgnoreCase("बलिउड")
                            || s.equalsIgnoreCase("इतिहासमा आज")
                            || s.equalsIgnoreCase("हलिउड")
                            || s.equalsIgnoreCase("फोटो फिचर")
                            || s.equalsIgnoreCase("मनोरञ्जन")
                            || s.equalsIgnoreCase("म्युजिक अपडेट")
                            || s.equalsIgnoreCase("साहित्य आज")
                            || s.equalsIgnoreCase("प्रोफाइल")
                            || s.equalsIgnoreCase("बलिवुड")
                            || s.equalsIgnoreCase("Entertainment News")
                            || s.equalsIgnoreCase("चलचित्र")
                            || s.equalsIgnoreCase("मनोरञ्जन एप")
                            || s.equalsIgnoreCase("मनोरन्जन")
                            || s.equalsIgnoreCase("मनोरन्जन")
                            || s.equalsIgnoreCase("मनोरञ्जन")
                            || s.equalsIgnoreCase("बिशेष")
                            || s.equalsIgnoreCase("विविध")
                            || s.equalsIgnoreCase("LITERATURE")
                            || s.equalsIgnoreCase("मनोरन्जन प्रमुख होम")
                            || s.equalsIgnoreCase("गशिप")
                            || s.equalsIgnoreCase("साहित्य")
                            || s.equalsIgnoreCase("साहित्य")
                            || s.equalsIgnoreCase("ENT")
                            || s.equalsIgnoreCase("Entertainment Main")
                            || s.equalsIgnoreCase("नेपाली चलचित्र")
                            || s.equalsIgnoreCase("कला")
                            || s.equalsIgnoreCase("कला/ साहित्य")
                            || s.equalsIgnoreCase("साहित्य/कला/संस्कृति")
                            || s.equalsIgnoreCase("Bollywood")
                            || s.equalsIgnoreCase("मनोरञ्जन प्रमुख")) {
                        entertain.add(entry);
                        feeds_filtered++;
                        break;
                    }//Health
                    else if (s.equalsIgnoreCase("स्वास्थ्य")
                            || s.equalsIgnoreCase("मोफसल")
                            || s.equalsIgnoreCase("उपभाेक्ता")
                            || s.equalsIgnoreCase("श्वास्थ्य")
                            || s.equalsIgnoreCase("खन")
                            || s.equalsIgnoreCase("धर्म / संस्कृति")
                            || s.equalsIgnoreCase("स्वास्थ्य जानकारी")
                            || s.equalsIgnoreCase("श्वास्थ्य")
                            || s.equalsIgnoreCase("जीवन-दर्शन")
                            || s.equalsIgnoreCase("बातावरण")
                            || s.equalsIgnoreCase("यौन-शिक्षा")
                            || s.equalsIgnoreCase("जीवनशैली")
                            || s.equalsIgnoreCase("आजको राशिफल")
                            || s.equalsIgnoreCase("घरेलु स्वास्थ्य")
                            || s.equalsIgnoreCase("पत्रपत्रिकामा स्वास्थ्य")
                            || s.equalsIgnoreCase("टिप्स")
                            || s.equalsIgnoreCase("यौन")
                            || s.equalsIgnoreCase("स्वास्थ्य/यौन")
                            || s.equalsIgnoreCase("स्वास्थ्य/जीवनशैली")) {
                        health.add(entry);
                        feeds_filtered++;
                        break;
                    }//sport
                    else if (s.equalsIgnoreCase("SPORT")
                            || s.equalsIgnoreCase("Sports")
                            || s.contains("खेल")
                            || s.equalsIgnoreCase("खेलकुद समाचार")
                            || s.equalsIgnoreCase("खेलकुद")
                            || s.equalsIgnoreCase("खेलकूद")
                            || s.equalsIgnoreCase("खेलकुद प्रमुख")
                            || s.equalsIgnoreCase("खेल")
                            || s.equalsIgnoreCase("खेलकुद फिचर")
                            ) {
                        sport.add(entry);
                        feeds_filtered++;
                        break;
                    } else {
                        for (String s1 : entry.getCategories()) {
                            Log.i(TAG, "Categories not filtered " + s1);
                        }

                    }

            }
        }

        main.add(breaking);  //0
        main.add(newspaper);  //1
        main.add(national); //2
        main.add(local);  //3
        main.add(opinion);  //4
        main.add(world);  //5
        main.add(business);   //6
        main.add(technology);  //7
        main.add(entertain);  //8
        main.add(health); //9
        main.add(sport);  //10
        Log.i("TAG", "feeds categorized : " + feeds_filtered);
        Log.i("TAG", "Total Feeds Size : " + total_feeds);
        if (total_feeds != 0)
            Log.i(TAG, (total_feeds - feeds_filtered) / total_feeds + "% feeds not filtered ");

        for (int i = 0; i < CATEGORY_NUMBER; i++) {
            List<Entry> processedFeeds;//new
            processedFeeds = main.get(i);
            processedFeeds = deleteDuplicate(processedFeeds); //delete duplicate feeds
            processedFeeds = deleteEnglishFeeds(processedFeeds);  //delete english feeds
            processedFeeds = sortByTime(processedFeeds);  //sort by time feeds feeds*//*

            if (processedFeeds.size() >= 10 && i != 0) {
                String s = categories.get(i);
                objects.add(s);
                for (int ii = 0; ii < 10; ii++) {
                    Entry entry = processedFeeds.get(ii);
                    objects.add(entry);
                }

            }


            clearFeedsByPref(processedFeeds, context);
            Hawk.put(categories.get(i), processedFeeds);  //Hawk.put(nameFeed,List);
            mFeedSize.add(processedFeeds.size() - mPriorSize.get(i));
        }

        Parse.saveSharedPreferencesLogList(context, objects, "AllFeeds");
        return (ArrayList<Integer>) mFeedSize;
    }

    private static void clearFeedsByPref(List<Entry> feeds, Context context) {
        int limitSize = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("feedsToStore", String.valueOf(40)));
        Log.i(TAG, "limitSize : " + limitSize);

        if (feeds.size() > limitSize) {
            feeds.subList(limitSize, feeds.size()).clear();
        }
    }


    private static void saveSharedPreferencesLogList(Context context, List<Object> list, String cacheName) {
        SharedPreferences prefs = context.getSharedPreferences("User", Context.MODE_PRIVATE);
//save the user list to preference
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString(cacheName, ObjectSerializer.serialize((Serializable) list));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();

    }

    public static boolean checkPref(Context context, String cacheName) {
        SharedPreferences prefs = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        boolean b = prefs.contains(cacheName);
        return b;
    }

    public static List<Object> loadSharedPreferencesLogList(Context context, String cacheName) {

        List<Object> objects = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        try {
            objects = (List<Object>) ObjectSerializer.deserialize(prefs.getString(cacheName, ObjectSerializer.serialize(new ArrayList())));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return objects;
    }


}
