package com.codelite.kr4k3rz.samachar.model;

import java.util.ArrayList;

import io.paperdb.Paper;

public class FeedLists {
    private static final String PREFIX_AJAX = "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=";
    private static final String POSTFIX_CACHED = "&num=-1";  //cached and max feeds
    private static final String POSTFIX_LATEST = "";  //latest and max feeds

    /*&scoring=h*/
    public static ArrayList<Category> feedsListSetup() {

        ArrayList<Category> categories = new ArrayList<>();//<-- its collects all the Category
        Category category_headlines = new Category();
        //<-- Headlines --> 0
        category_headlines.setName_category("मुख्य समाचार");   //<--it for Category name eg. Headline
        ArrayList<SubCategory> subCategories_headlines;  // <-- it for subcategory eg. Online Khabar , its cache_link and latest_link
        subCategories_headlines = new ArrayList<>();
        String[][] headlines_feed = new String[][]{
                {"Online Khabar", "http://www.onlinekhabar.com/feed/"}, //best
                {"Online Patrika", "http://onlinepatrika.com/feed/"},   //best
                {"Nepali Headlines", "http://nepaliheadlines.com/feed/"},
                {"Nepali Samachar", "http://nepalisamachar.com/?feed=rss2"},  //best
                {"Naya Samachar", "http://nayasamachar.com/?feed=rss2"},                  /*Not daily posters*/
                {"Sambad Media", "http://www.sambadmedia.com/?feed=rss2"},    //best
                {"Taja Onlinekhabar", "http://www.tajaonlinekhabar.com/feed"},  //best
                {"Lokaantar", "http://lokaantar.com/feed"},     //ok
                {"MediaNp", "http://medianp.com/feed"},                          //best
                {"eNepali Khabar", "http://www.enepalikhabar.com/feed"},   //ok and low
                {"Nepal Aaja", "http://nepalaaja.com/feed/"},
                {"ONS Nepal", "http://www.onsnews.com/feed/"},
                {"Naya Samachar", "http://nayasamachar.com/?feed=rss2"},//ok
                {"Naya Page", "http://www.nayapage.com/feed"},
                {"Sourya Daily", "http://www.souryadaily.com/feed"},
                {"Rajdhani Daily", "http://rajdhanidaily.com/feed/"},
                {"Char disa", "http://chardisha.com/feed/"},
                {"Nepali Health", "http://www.nepalihealth.com/feed/"},


        };
        for (String[] aHeadlines_feed : headlines_feed) {
            SubCategory subCategory = new SubCategory();
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    subCategory.setSub_name(aHeadlines_feed[0]);
                }
                if (j == 1) {
                    subCategory.setSub_link_cached(PREFIX_AJAX + aHeadlines_feed[1] + POSTFIX_CACHED);
                    subCategory.setSub_link_latest(PREFIX_AJAX + aHeadlines_feed[1] + POSTFIX_LATEST);
                    subCategory.setUpdated_sub_link_cached(PREFIX_AJAX + aHeadlines_feed[1] + POSTFIX_CACHED);
                    subCategory.setUpdated_sub_link_latest(PREFIX_AJAX + aHeadlines_feed[1] + POSTFIX_LATEST);
                    subCategories_headlines.add(subCategory);
                }


            }
        }
        category_headlines.setSub_category(subCategories_headlines);
        categories.add(category_headlines);


        //<--Business--> 1
        Category category_business = new Category();
        category_business.setName_category("अर्थ");
        ArrayList<SubCategory> subCategories_businesses = new ArrayList<>();

        String[][] business_feed = new String[][]{
                {"Karobar Daily", "http://www.karobardaily.com/rss"},

        };

        for (String[] aBusiness_feed : business_feed) {
            SubCategory subCategory = new SubCategory();
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    subCategory.setSub_name(aBusiness_feed[0]);
                }
                if (j == 1) {
                    subCategory.setSub_link_cached(PREFIX_AJAX + aBusiness_feed[1] + POSTFIX_CACHED);
                    subCategory.setSub_link_latest(PREFIX_AJAX + aBusiness_feed[1] + POSTFIX_LATEST);
                    subCategory.setUpdated_sub_link_cached(PREFIX_AJAX + aBusiness_feed[1] + POSTFIX_CACHED);
                    subCategory.setUpdated_sub_link_latest(PREFIX_AJAX + aBusiness_feed[1] + POSTFIX_LATEST);
                    subCategories_businesses.add(subCategory);
                }


            }
        }
        category_business.setSub_category(subCategories_businesses);
        categories.add(category_business);

        //<--Entertainment--> 2
        Category category_entertainment = new Category();
        category_entertainment.setName_category("कला");

        ArrayList<SubCategory> subCategories_entertainments = new ArrayList<>();

        String[][] entertainment_feed = new String[][]{
                {"Screen Nepal", "http://screennepal.com/feed"},

        };

        for (String[] aEntertainment_feed : entertainment_feed) {
            SubCategory subCategory = new SubCategory();
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    subCategory.setSub_name(aEntertainment_feed[0]);
                }
                if (j == 1) {
                    subCategory.setSub_link_cached(PREFIX_AJAX + aEntertainment_feed[1] + POSTFIX_CACHED);
                    subCategory.setSub_link_latest(PREFIX_AJAX + aEntertainment_feed[1] + POSTFIX_LATEST);
                    subCategory.setUpdated_sub_link_cached(PREFIX_AJAX + aEntertainment_feed[1] + POSTFIX_CACHED);
                    subCategory.setUpdated_sub_link_latest(PREFIX_AJAX + aEntertainment_feed[1] + POSTFIX_LATEST);
                    subCategories_entertainments.add(subCategory);
                }


            }
        }
        category_entertainment.setSub_category(subCategories_entertainments);
        categories.add(category_entertainment);


        //<--Health--> 3
        Category category_health;
        category_health = new Category();
        category_health.setName_category("स्वास्थ्य");
        ArrayList<SubCategory> subCategories_health = new ArrayList<>();

        String[][] health_feed = new String[][]{
                {"Nepali Health", "http://www.nepalihealth.com/feed/"},
                {"Nepal Health New", "http://nepalhealthnews.com/feed/"}
        };

        for (String[] aHealth_feed : health_feed) {
            SubCategory subCategory = new SubCategory();
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    subCategory.setSub_name(aHealth_feed[0]);
                }
                if (j == 1) {
                    subCategory.setSub_link_cached(PREFIX_AJAX + aHealth_feed[1] + POSTFIX_CACHED);
                    subCategory.setSub_link_latest(PREFIX_AJAX + aHealth_feed[1] + POSTFIX_LATEST);
                    subCategory.setUpdated_sub_link_cached(PREFIX_AJAX + aHealth_feed[1] + POSTFIX_CACHED);
                    subCategory.setUpdated_sub_link_latest(PREFIX_AJAX + aHealth_feed[1] + POSTFIX_LATEST);
                    subCategories_health.add(subCategory);
                }


            }
        }
        category_health.setSub_category(subCategories_health);
        categories.add(category_health);


        //<--Technology 4
        Category category_technology;
        category_technology = new Category();
        category_technology.setName_category("प्रविधि");
        ArrayList<SubCategory> subCategories_technology = new ArrayList<>();


        String[][] technology_feed = new String[][]{
                {"Aaakar Post", "http://feeds.feedburner.com/Aakar"}
        };

        for (String[] aTechnology_feed : technology_feed) {
            SubCategory subCategory = new SubCategory();
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    subCategory.setSub_name(aTechnology_feed[0]);
                }
                if (j == 1) {
                    subCategory.setSub_link_cached(PREFIX_AJAX + aTechnology_feed[1] + POSTFIX_CACHED);
                    subCategory.setSub_link_latest(PREFIX_AJAX + aTechnology_feed[1] + POSTFIX_LATEST);
                    subCategory.setUpdated_sub_link_cached(PREFIX_AJAX + aTechnology_feed[1] + POSTFIX_CACHED);
                    subCategory.setUpdated_sub_link_latest(PREFIX_AJAX + aTechnology_feed[1] + POSTFIX_LATEST);
                    subCategories_technology.add(subCategory);
                }
            }
        }
        category_technology.setSub_category(subCategories_technology);
        categories.add(category_technology);

        return categories;
    }


    public static String[] getFeedListCached(int numCategory) {
        ArrayList<String> strings = new ArrayList<>();
        ArrayList<Category> categories = Paper.book().read("updatedData");
        ArrayList<SubCategory> subCategory = categories.get(numCategory).getSub_category();
        for (SubCategory subCategory1 : subCategory) {
            strings.add(subCategory1.getUpdated_sub_link_cached());
        }
        return FeedLists.convertArrayListToStringArray(strings);
    }

    public static String[] getFeedListLatest(int numCategory) {
        ArrayList<String> strings = new ArrayList<>();
        ArrayList<Category> categories = Paper.book().read("updatedData");
        ArrayList<SubCategory> subCategory = categories.get(numCategory).getSub_category();
        for (SubCategory subCategory1 : subCategory) {
            strings.add(subCategory1.getUpdated_sub_link_latest());
        }

        return FeedLists.convertArrayListToStringArray(strings);

    }

    private static String[] convertArrayListToStringArray(ArrayList<String> strings) {
        String[] stockArr = new String[strings.size()];
        stockArr = strings.toArray(stockArr);
        return stockArr;

    }

}

