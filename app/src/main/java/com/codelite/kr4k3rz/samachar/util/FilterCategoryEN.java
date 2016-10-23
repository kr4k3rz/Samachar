package com.codelite.kr4k3rz.samachar.util;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.model.Header;
import com.codelite.kr4k3rz.samachar.model.WhichCategoryEN;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class FilterCategoryEN {
    private static final String TAG = Parse.class.getSimpleName();
    private List<Entry> entryList = new ArrayList<>();
    private Context context;

    /**
     * Instantiates a new Filter category.
     *
     * @param entryList the entry list
     * @param context   the context
     */
    public FilterCategoryEN(List<Entry> entryList, Context context) {
        this.entryList = entryList;
        this.context = context;
    }

    public void filter() {
        List<Header> categories = new ArrayList<>();
        List<List<Entry>> main = new ArrayList<>();
        int total_feeds = 0;
        int feeds_filtered = 0;
        int feeds_notFiltered = 0;
        int CATEGORY_NUMBER = 9;
        List<Integer> mPriorSize = new ArrayList<>();
        List<Integer> mFeedSize = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        // List<Entry> filteredWithImg;
        // filteredWithImg = feedsWithImg();
        /*Initialization of Header by First and Second Name*/
        for (WhichCategoryEN whichCategory :
                WhichCategoryEN.values()) {
            categories.add(new Header(whichCategory.getFirstName(), whichCategory.getSecondName()));
        }


        List<Entry> breaking = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.BREAKING.getSecondName() + "EN")) {
            List<Entry> entries1 = Paper.book().read(WhichCategoryEN.BREAKING.getSecondName() + "EN");
            breaking.addAll(entries1);
            mPriorSize.add(breaking.size());

        } else mPriorSize.add(0);

        List<Entry> newspaper = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.NATIONAL.getSecondName() + "EN")) {
            List<Entry> entries1 = Paper.book().read(WhichCategoryEN.NATIONAL.getSecondName() + "EN");
            newspaper.addAll(entries1);
            mPriorSize.add(newspaper.size());
        } else mPriorSize.add(0);

        List<Entry> world = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.WORLD.getSecondName() + "EN")) {
            List<Entry> entries1 = Paper.book().read(WhichCategoryEN.WORLD.getSecondName() + "EN");
            world.addAll(entries1);
            mPriorSize.add(world.size());
        } else mPriorSize.add(0);

        List<Entry> business = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.BUSINESS.getSecondName() + "EN")) {
            List<Entry> entries1 = Paper.book().read(WhichCategoryEN.BUSINESS.getSecondName() + "EN");
            business.addAll(entries1);
            mPriorSize.add(business.size());

        } else mPriorSize.add(0);

        List<Entry> technology = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.TECHNOLOGY.getSecondName() + "EN")) {
            List<Entry> entries1 = Paper.book().read(WhichCategoryEN.TECHNOLOGY.getSecondName() + "EN");
            technology.addAll(entries1);
            mPriorSize.add(technology.size());

        } else mPriorSize.add(0);

        List<Entry> entertain = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.ENTERTAINMENT.getSecondName() + "EN")) {
            List<Entry> entries1 = Paper.book().read(WhichCategoryEN.ENTERTAINMENT.getSecondName() + "EN");
            entertain.addAll(entries1);
            mPriorSize.add(entertain.size());

        } else mPriorSize.add(0);

        List<Entry> health = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.HEALTH.getSecondName() + "EN")) {
            List<Entry> entries1 = Paper.book().read(WhichCategoryEN.HEALTH.getSecondName() + "EN");
            health.addAll(entries1);
            mPriorSize.add(health.size());
        } else mPriorSize.add(0);

        List<Entry> sport = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.SPORT.getSecondName() + "EN")) {
            List<Entry> entries1 = Paper.book().read(WhichCategoryEN.SPORT.getSecondName() + "EN");
            sport.addAll(entries1);
            mPriorSize.add(sport.size());
        } else mPriorSize.add(0);

        List<Entry> imgVid = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.IMGVID.getSecondName() + "EN")) {
            List<Entry> entries1 = Paper.book().read(WhichCategoryEN.IMGVID.getSecondName() + "EN");
            imgVid.addAll(entries1);
            mPriorSize.add(imgVid.size());
        } else mPriorSize.add(0);


        for (Entry entry : entryList) {
            total_feeds++;
//for category
            for (String s : entry.getCategories()) {
                //BreakingFrag
                if (s.equalsIgnoreCase("BreakingFrag")
                        || s.equalsIgnoreCase("News")
                        || s.equalsIgnoreCase("BreakingFrag News")
                        || s.equalsIgnoreCase("BreakingFrag News")
                        || s.equalsIgnoreCase("currentnews")
                        || s.equalsIgnoreCase("HEADLINES")
                        || s.equalsIgnoreCase("Feature")
                        || s.equalsIgnoreCase("highlight")
                        || s.equalsIgnoreCase("English")
                        || s.equalsIgnoreCase("Top News")
                        || s.equalsIgnoreCase("CRIME")
                        || s.equalsIgnoreCase("Just In")
                        || s.equalsIgnoreCase("News")
                        || s.equalsIgnoreCase("BreakingFrag News")
                        || s.equalsIgnoreCase("BreakingFrag News")
                        || s.equalsIgnoreCase("currentnews")
                        || s.equalsIgnoreCase("HEADLINES")
                        || s.equalsIgnoreCase("Feature")
                        || s.equalsIgnoreCase("Cover Story")
                        || s.equalsIgnoreCase("highlight")
                        || s.equalsIgnoreCase("English")
                        || s.equalsIgnoreCase("CRIME")
                        || s.equalsIgnoreCase("Top News")
                        ) {
                    breaking.add(entry);
                    feeds_filtered++;
                    break;
                } else //National
                    if (s.equalsIgnoreCase("Nepal")
                            || s.equalsIgnoreCase("Political News")
                            || s.equalsIgnoreCase("OPINION")
                            || s.equalsIgnoreCase("Uncategorized")
                            || s.equalsIgnoreCase("Interview")
                            || s.equalsIgnoreCase("LOCAL")
                            || s.equalsIgnoreCase("Society News")
                            || s.equalsIgnoreCase("Uncategorized")
                            || s.equalsIgnoreCase("DIASPORA")
                            || s.equalsIgnoreCase("DIASPORA/LOCAL")
                            || s.equalsIgnoreCase("Nepali Diaspora")
                            || s.equalsIgnoreCase("Nepal News")
                            || s.equalsIgnoreCase("Community News")
                            || s.equalsIgnoreCase("Kathmandu")
                            || s.equalsIgnoreCase("Opinion")) {
                        newspaper.add(entry);
                        feeds_filtered++;
                        break;
                    } //world
                    else if (s.equalsIgnoreCase("World")
                            || s.equalsIgnoreCase("World News")
                            || s.equalsIgnoreCase("Odd World")
                            ) {
                        world.add(entry);
                        feeds_filtered++;
                        break;
                    }//Business
                    else if (s.equalsIgnoreCase("Business")
                            || s.equalsIgnoreCase("Finance")
                            || s.equalsIgnoreCase("Business News")
                            ) {
                        business.add(entry);
                        feeds_filtered++;
                        break;
                    }//Technology
                    else if (s.equalsIgnoreCase("Mobile &amp; Apps")
                            || s.equalsIgnoreCase("TECHNOLOGY NEWS")
                            || s.equalsIgnoreCase("3G mobile service")
                            || s.equalsIgnoreCase("nepal telecom")
                            || s.equalsIgnoreCase("Science &amp; Technology")
                            || s.equalsIgnoreCase("Internet")
                            || s.equalsIgnoreCase("Samsung")) {
                        technology.add(entry);
                        feeds_filtered++;
                        break;
                    }//Entertain
                    else if (s.equalsIgnoreCase("Entertainment")
                            || s.equalsIgnoreCase("Book Review")
                            || s.equalsIgnoreCase("Bollywood")
                            || s.equalsIgnoreCase("Entertainment Main")
                            || s.equalsIgnoreCase("LITERATURE")
                            || s.equalsIgnoreCase("Entertainment News")
                            || s.equalsIgnoreCase("Music")
                            || s.equalsIgnoreCase("Movie")
                            || s.equalsIgnoreCase("Events")
                            || s.equalsIgnoreCase("Culture ")
                            || s.equalsIgnoreCase("Art &amp; Culture")) {
                        entertain.add(entry);
                        feeds_filtered++;
                        break;
                    }//Health
                    else if (s.equalsIgnoreCase("Environment")
                            || s.equalsIgnoreCase("Temp")
                            || s.equalsIgnoreCase("Health")
                            || s.equalsIgnoreCase("LIFE STYLE")
                            || s.equalsIgnoreCase("Lifestyle News")
                            || s.equalsIgnoreCase("Travel")
                            || s.equalsIgnoreCase("Lifestyle")) {
                        health.add(entry);
                        feeds_filtered++;
                        break;
                    }//sport
                    else if (s.equalsIgnoreCase("Sports")
                            || s.equalsIgnoreCase("SPORT")) {
                        sport.add(entry);
                        feeds_filtered++;
                        break;
                    } else {
                        Log.i(TAG, "unfiltered category : " + s);
                        imgVid.add(entry);
                        feeds_notFiltered++;
                    }

            }
        }

        main.add(breaking);  //0
        main.add(newspaper);  //1
        main.add(world);  //2
        main.add(business);   //3
        main.add(technology);  //4
        main.add(entertain);  //5
        main.add(health); //6
        main.add(sport);  //7
        main.add(imgVid); //8
        Log.i(TAG, "Total Feeds Size : " + total_feeds);
        Log.i(TAG, "feeds categorized : " + feeds_filtered);
        Log.i(TAG, "feeds not filtered : " + feeds_notFiltered);
        main.add(breaking);  //0
        main.add(newspaper);  //1
        main.add(world);  //2
        main.add(business);   //3
        main.add(technology);  //4
        main.add(entertain);  //5
        main.add(health); //6
        main.add(sport);  //7
        main.add(imgVid); //8
        Log.i(TAG, "Total Feeds Size : " + total_feeds);
        Log.i(TAG, "feeds categorized : " + feeds_filtered);
        Log.i(TAG, "feeds not filtered : " + feeds_notFiltered);

        for (int i = 0; i < CATEGORY_NUMBER; i++) {
            List<Entry> processedFeeds;//new
            processedFeeds = main.get(i);
            processedFeeds = Parse.deleteDuplicate(processedFeeds); //delete duplicate feeds
            processedFeeds = Parse.sortByTime(processedFeeds);  //sort by time feeds feeds*//*
            int LIMIT_FEED = 5;
            if (processedFeeds.size() >= LIMIT_FEED && i != 0) {  //leaving breaking news
                Header header;
                header = categories.get(i);
                objects.add(header);

                for (int ii = 0; ii < LIMIT_FEED; ii++) {
                    Entry entry = processedFeeds.get(ii);
                    objects.add(entry);
                }
            }
             /*calculate feed size by  processedFeeds.size()-mPriorSize.size() */
            Header header = categories.get(i);
            clearFeedsByPref(processedFeeds, context);
            Paper.book().write(header.getSecondName() + "EN", processedFeeds);

        }
        Paper.book().write("AllFeedsEN", objects);
    }

    private void clearFeedsByPref(List<Entry> feeds, Context context) {
        int limitSize = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("feedsToStore", String.valueOf(40)));
        if (feeds.size() > limitSize) {
            feeds.subList(limitSize, feeds.size()).clear();
        }

    }


}


