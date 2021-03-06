package com.codelite.kr4k3rz.samachar.util;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.codelite.kr4k3rz.samachar.model.Header;
import com.codelite.kr4k3rz.samachar.model.WhichCategoryEN;
import com.codelite.kr4k3rz.samachar.model.feed.EntriesItem;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class FilterCategoryEN {
    private static final String TAG = Parse.class.getSimpleName();
    private final Context context;
    private List<EntriesItem> entryList = new ArrayList<>();

    /**
     * Instantiates a new Filter category.
     *
     * @param entryList the entry list
     * @param context   the context
     */
    public FilterCategoryEN(List<EntriesItem> entryList, Context context) {
        this.entryList = entryList;
        this.context = context;
    }

    public void filter() {
        List<Header> categories = new ArrayList<>();
        List<List<EntriesItem>> main = new ArrayList<>();
        int total_feeds = 0;
        int feeds_filtered = 0;
        int feeds_notFiltered = 0;
        int CATEGORY_NUMBER = 9;
        List<Object> objects = new ArrayList<>();
        // List<Entry> filteredWithImg;
        // filteredWithImg = feedsWithImg();
        /*Initialization of Header by First and Second Name*/
        for (WhichCategoryEN whichCategory :
                WhichCategoryEN.values()) {
            categories.add(new Header(whichCategory.getFirstName(), whichCategory.getSecondName()));
        }


        List<EntriesItem> breaking = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.BREAKING.getSecondName() + "EN")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryEN.BREAKING.getSecondName() + "EN");
            breaking.addAll(entries1);
        }

        List<EntriesItem> newspaper = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.NATIONAL.getSecondName() + "EN")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryEN.NATIONAL.getSecondName() + "EN");
            newspaper.addAll(entries1);
        }

        List<EntriesItem> world = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.WORLD.getSecondName() + "EN")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryEN.WORLD.getSecondName() + "EN");
            world.addAll(entries1);
        }

        List<EntriesItem> business = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.BUSINESS.getSecondName() + "EN")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryEN.BUSINESS.getSecondName() + "EN");
            business.addAll(entries1);
        }

        List<EntriesItem> technology = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.TECHNOLOGY.getSecondName() + "EN")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryEN.TECHNOLOGY.getSecondName() + "EN");
            technology.addAll(entries1);
        }

        List<EntriesItem> entertain = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.ENTERTAINMENT.getSecondName() + "EN")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryEN.ENTERTAINMENT.getSecondName() + "EN");
            entertain.addAll(entries1);
        }

        List<EntriesItem> health = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.HEALTH.getSecondName() + "EN")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryEN.HEALTH.getSecondName() + "EN");
            health.addAll(entries1);
        }

        List<EntriesItem> sport = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.SPORT.getSecondName() + "EN")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryEN.SPORT.getSecondName() + "EN");
            sport.addAll(entries1);
        }

        List<EntriesItem> imgVid = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryEN.IMGVID.getSecondName() + "EN")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryEN.IMGVID.getSecondName() + "EN");
            imgVid.addAll(entries1);
        }

        for (EntriesItem entry : entryList) {
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
                            || s.equalsIgnoreCase("Political")
                            || s.equalsIgnoreCase("Political News")
                            || s.equalsIgnoreCase("OPINION")
                            || s.equalsIgnoreCase("Uncategorized")
                            || s.equalsIgnoreCase("Interview")
                            ||s.equalsIgnoreCase("LITERARY")
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
                            || s.equalsIgnoreCase("nepal travel")
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
                            || s.equalsIgnoreCase("SPORT") || s.equalsIgnoreCase("Sports News")) {
                        sport.add(entry);
                        feeds_filtered++;
                        break;
                    } else {
                        if (!s.equalsIgnoreCase("Photo Feature")) {
                            Log.i(TAG, "unfiltered category : " + s);
                            imgVid.add(entry);
                            feeds_notFiltered++;
                        }

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
        for (int i = 0; i < CATEGORY_NUMBER; i++) {
            List<EntriesItem> processedFeeds;//new
            processedFeeds = main.get(i);
            processedFeeds = Parse.deleteDuplicate(processedFeeds); //delete duplicate feeds
            processedFeeds = Parse.deleteNonEngFeeds(processedFeeds);  //delete non english feeds
            processedFeeds = Parse.sortByTime(processedFeeds);  //sort by time feeds feeds

            int LIMIT_FEED = 3;
            if (processedFeeds.size() >= LIMIT_FEED && i != 0) {  //leaving breaking news
                Header header;
                header = categories.get(i);
                objects.add(header);
                for (int ii = 0; ii < LIMIT_FEED; ii++) {
                    EntriesItem entry = processedFeeds.get(ii);
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

    private void clearFeedsByPref(List<EntriesItem> feeds, Context context) {
        int limitSize = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("feedsToStore", String.valueOf(40)));
        if (feeds.size() > limitSize) {
            feeds.subList(limitSize, feeds.size()).clear();
        }

    }


}


