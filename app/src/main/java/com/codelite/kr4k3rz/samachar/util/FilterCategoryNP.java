package com.codelite.kr4k3rz.samachar.util;

import android.content.Context;
import android.util.Log;

import com.codelite.kr4k3rz.samachar.model.Header;
import com.codelite.kr4k3rz.samachar.model.WhichCategoryNP;
import com.codelite.kr4k3rz.samachar.model.feed.EntriesItem;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;


/**
 * The type Filter category.
 */
public class FilterCategoryNP {
    private static final String TAG = Parse.class.getSimpleName();
    private List<EntriesItem> entryList = new ArrayList<>();


    /**
     * Instantiates a new Filter category.
     *
     * @param entryList the entry list
     * @param context   the context
     */
    public FilterCategoryNP(List<EntriesItem> entryList, Context context) {
        this.entryList = entryList;
    }


    /**
     * Filter the List of Entry into category and saved it into DB(Paper database).
     * <p>
     * the array list of integer that contains the category feeds loaded
     */
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
        for (WhichCategoryNP whichCategory :
                WhichCategoryNP.values()) {
            categories.add(new Header(whichCategory.getFirstName(), whichCategory.getSecondName()));
        }


        List<EntriesItem> breaking = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryNP.BREAKING.getSecondName() + "NP")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryNP.BREAKING.getSecondName() + "NP");
            breaking.addAll(entries1);
        }

        List<EntriesItem> newspaper = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryNP.NATIONAL.getSecondName() + "NP")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryNP.NATIONAL.getSecondName() + "NP");
            newspaper.addAll(entries1);
        }
        List<EntriesItem> world = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryNP.WORLD.getSecondName() + "NP")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryNP.WORLD.getSecondName() + "NP");
            world.addAll(entries1);
        }

        List<EntriesItem> business = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryNP.BUSINESS.getSecondName() + "NP")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryNP.BUSINESS.getSecondName() + "NP");
            business.addAll(entries1);
        }

        List<EntriesItem> technology = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryNP.TECHNOLOGY.getSecondName() + "NP")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryNP.TECHNOLOGY.getSecondName() + "NP");
            technology.addAll(entries1);
        }

        List<EntriesItem> entertain = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryNP.ENTERTAINMENT.getSecondName() + "NP")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryNP.ENTERTAINMENT.getSecondName() + "NP");
            entertain.addAll(entries1);
        }

        List<EntriesItem> health = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryNP.HEALTH.getSecondName() + "NP")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryNP.HEALTH.getSecondName() + "NP");
            health.addAll(entries1);
        }

        List<EntriesItem> sport = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryNP.SPORT.getSecondName() + "NP")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryNP.SPORT.getSecondName() + "NP");
            sport.addAll(entries1);
        }

        List<EntriesItem> imgVid = new ArrayList<>();
        if (Paper.book().exist(WhichCategoryNP.IMGVID.getSecondName() + "NP")) {
            List<EntriesItem> entries1 = Paper.book().read(WhichCategoryNP.IMGVID.getSecondName() + "NP");
            imgVid.addAll(entries1);
        }

        //for special category
        for (EntriesItem entry : entryList) {
            total_feeds++;
            for (String s : entry.getCategories()) {
                //BreakingNews
                if (s.equalsIgnoreCase("News")
                        || s.equalsIgnoreCase("Breaking News")
                        || s.equalsIgnoreCase("CurrentNews")
                        || s.equalsIgnoreCase("मुलुक प्रमुख")
                        || s.equalsIgnoreCase("HEADLINES")
                        || s.equalsIgnoreCase("Feature")
                        || s.equalsIgnoreCase("साप्ताहिक परिषिष्टांक")
                        || s.equalsIgnoreCase("नेपाल चर्चा")
                        || s.equalsIgnoreCase("Banner News")
                        || s.equalsIgnoreCase("highlight")
                        || s.equalsIgnoreCase("English")
                        || s.equalsIgnoreCase("हेडर न्युज")
                        || s.equalsIgnoreCase("Top News")
                        || s.equalsIgnoreCase("कभर समाचार")
                        || s.equalsIgnoreCase("टप न्युज")
                        || s.equalsIgnoreCase("बिशेष")
                        || s.equalsIgnoreCase("CRIME")
                        || s.equalsIgnoreCase("सुरक्षा गतिविधि")
                        || s.equalsIgnoreCase("BreakingFrag")
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
                } else //National
                    if (s.equalsIgnoreCase("सैाजन्य")
                            || s.equalsIgnoreCase("पत्रपत्रिका प्रमुख")
                            || s.equalsIgnoreCase("बिशेष स्टोरी")
                            || s.equalsIgnoreCase("saujanya")
                            || s.equalsIgnoreCase("एभरेष्ट दैनिक")
                            || s.equalsIgnoreCase("सम्पादकीय")
                            || s.equalsIgnoreCase("पत्रपत्रिकाबाट")
                            || s.equalsIgnoreCase("समाचार फिचर")
                            || s.equalsIgnoreCase("आजको पत्रिका बाट")
                            || s.equalsIgnoreCase("छापामा")
                            || s.equalsIgnoreCase("पत्रपत्रिका")
                            || s.equalsIgnoreCase("Nepal News")
                            || s.equalsIgnoreCase("समाचार")
                            || s.equalsIgnoreCase("खोजखबर")
                            || s.equalsIgnoreCase("मुलुक")
                            || s.equalsIgnoreCase("Community News")
                            || s.equalsIgnoreCase("आजको पत्रपत्रिकाबाट")
                            || s.equalsIgnoreCase("Night only")
                            || s.equalsIgnoreCase("राष्ट्रिय समाचार")
                            || s.equalsIgnoreCase("News of the day")
                            || s.equalsIgnoreCase("मेन स्टोरी")
                            || s.equalsIgnoreCase("राष्ट्रिय")
                            || s.equalsIgnoreCase("देश")
                            || s.equalsIgnoreCase("समाज")
                            || s.equalsIgnoreCase("देश/समाज")
                            || s.equalsIgnoreCase("थारु समाचार")
                            || s.equalsIgnoreCase("राजनीति")
                            || s.equalsIgnoreCase("राजनीति")
                            || s.equalsIgnoreCase("राजनिति")
                            || s.equalsIgnoreCase("फिचर")
                            || s.equalsIgnoreCase("LOCAL")
                            || s.equalsIgnoreCase("Uncategorized")
                            || s.equalsIgnoreCase("DIASPORA")
                            || s.equalsIgnoreCase("DIASPORA/LOCAL")
                            || s.equalsIgnoreCase("Nepali Diaspora")
                            || s.equalsIgnoreCase("प्रबास/स्थानिय")
                            || s.equalsIgnoreCase(" बैदेशिक रोजगार")
                            || s.equalsIgnoreCase("प्रवास समाचार")
                            || s.equalsIgnoreCase("रोजगार")
                            || s.equalsIgnoreCase("प्रवास प्रमुख")
                            || s.equalsIgnoreCase("प्रबास")
                            || s.equalsIgnoreCase("समाज")
                            || s.equalsIgnoreCase("अपराध सुरक्षा")
                            || s.equalsIgnoreCase("प्रवास | बैदेशिक रोजगार")
                            || s.equalsIgnoreCase("प्रवास")
                            || s.equalsIgnoreCase("स्थानिय")
                            || s.equalsIgnoreCase("OPINION")
                            || s.equalsIgnoreCase("Uncategorized")
                            || s.equalsIgnoreCase("विचार")
                            || s.equalsIgnoreCase("अग्रलेख")
                            || s.equalsIgnoreCase("लेख")
                            || s.equalsIgnoreCase("अन्तरवार्ता")
                            || s.equalsIgnoreCase("लेख / रचना")
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
                        newspaper.add(entry);
                        feeds_filtered++;
                        break;
                    } //world
                    else if (s.equalsIgnoreCase("अन्तराष्ट्रिय समाचार")
                            || s.equalsIgnoreCase("अन्तर्राष्ट्रिय समाचार")
                            || s.equalsIgnoreCase("बिविध")
                            || s.equalsIgnoreCase("विदेश")
                            || s.equalsIgnoreCase("यूरोप")
                            || s.equalsIgnoreCase("विविध")
                            || s.equalsIgnoreCase("पत्याउनै गाह्रो")
                            || s.equalsIgnoreCase("Offbeat")
                            || s.equalsIgnoreCase("सम–सामयिक")
                            || s.equalsIgnoreCase("बिश्व")
                            || s.equalsIgnoreCase("विश्व/प्रवास")
                            || s.equalsIgnoreCase("वर्ल्ड")
                            || s.equalsIgnoreCase("बिबिध")
                            || s.equalsIgnoreCase("World News")
                            || s.equalsIgnoreCase("अचम्म...")
                            || s.equalsIgnoreCase("Odd World")
                            || s.equalsIgnoreCase("रोचक")
                            || s.equalsIgnoreCase("रोचक / विचित्र")
                            || s.equalsIgnoreCase("विचित्र संसार")
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
                            || s.equalsIgnoreCase("पर्यटन") || s.equalsIgnoreCase("market")
                            || s.equalsIgnoreCase("Expo")
                            || s.equalsIgnoreCase("Employment")
                            || s.equalsIgnoreCase("Tourism")
                            || s.equalsIgnoreCase("कलेज")
                            || s.equalsIgnoreCase("Bank")
                            || s.equalsIgnoreCase("Share")
                            || s.equalsIgnoreCase("Finance")
                            || s.equalsIgnoreCase("Market")
                            || s.equalsIgnoreCase("निजी क्षेत्र")
                            || s.equalsIgnoreCase("परियोजना क्षेत्र")
                            || s.equalsIgnoreCase("Corporate")
                            || s.equalsIgnoreCase("व्यापार")
                            || s.equalsIgnoreCase("Infrastructure")
                            || s.equalsIgnoreCase("Industry")
                            || s.contains("बिजनेस")
                            || s.equalsIgnoreCase("कर्पेारेट")
                            || s.equalsIgnoreCase("शिक्षा")
                            || s.equalsIgnoreCase("गन्तब्य")
                            || s.equalsIgnoreCase("बैंक-वित्त प्रमुख")
                            || s.equalsIgnoreCase("अभिलेख")
                            || s.equalsIgnoreCase("अर्थतन्त्र फिचर")
                            || s.equalsIgnoreCase("अर्थ | वाणिज्य | बजार")
                            || s.equalsIgnoreCase("शेयर")
                            || s.contains("अर्थ")
                            || s.equalsIgnoreCase("कृषि")
                            || s.equalsIgnoreCase("बातावरण-कृषि")
                            || s.equalsIgnoreCase("अर्थ")
                            || s.equalsIgnoreCase("बैंक/शेयर")
                            || s.equalsIgnoreCase("अर्थ बजार")
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
                            || s.equalsIgnoreCase("बिज्ञान-प्रबिधि")
                            || s.equalsIgnoreCase("Technology")
                            || s.equalsIgnoreCase("Auto")
                            || s.equalsIgnoreCase("अटो")
                            || s.equalsIgnoreCase("सूचना प्रविधि -समाचार")
                            || s.equalsIgnoreCase("Telecom")
                            || s.equalsIgnoreCase("ओटोमोबाइल")
                            || s.equalsIgnoreCase("Sci-Tech")
                            || s.equalsIgnoreCase("3G mobile service")
                            || s.equalsIgnoreCase("nepal telecom")
                            || s.equalsIgnoreCase("Internet")
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
                            || s.equalsIgnoreCase("संगीत")
                            || s.equalsIgnoreCase("संस्कृति")
                            || s.equalsIgnoreCase("कला समिक्षा")
                            || s.equalsIgnoreCase("Music")
                            || s.equalsIgnoreCase("Movie")
                            || s.equalsIgnoreCase("Events")
                            || s.equalsIgnoreCase("Exclusive")
                            || s.equalsIgnoreCase("इतिहासमा आज")
                            || s.equalsIgnoreCase("चर्चामा")
                            || s.equalsIgnoreCase("हलिउड")
                            || s.equalsIgnoreCase("रंग समाचार")
                            || s.equalsIgnoreCase("रंग प्रमुख")
                            || s.equalsIgnoreCase("फोटो फिचर")
                            || s.equalsIgnoreCase("कला साहित्य")
                            || s.equalsIgnoreCase("मनोरञ्जन / कला")
                            || s.equalsIgnoreCase("सामाजिक सञ्जाल")
                            || s.equalsIgnoreCase("सिनेमा")
                            || s.equalsIgnoreCase("मनोरञ्जन")
                            || s.equalsIgnoreCase("म्युजिक अपडेट")
                            || s.equalsIgnoreCase("साहित्य आज")
                            || s.equalsIgnoreCase("प्रोफाइल")
                            || s.equalsIgnoreCase("कलिवुड")
                            || s.equalsIgnoreCase("बलिवुड")
                            || s.equalsIgnoreCase("चलिरहेको फ्लिम")
                            || s.equalsIgnoreCase("Entertainment News")
                            || s.equalsIgnoreCase("चलचित्र")
                            || s.equalsIgnoreCase("मनोरञ्जन एप")
                            || s.equalsIgnoreCase("मनोरन्जन")
                            || s.equalsIgnoreCase("मनोरन्जन")
                            || s.equalsIgnoreCase("मनोरञ्जन")
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
                            || s.contains("शारीरिक")
                            || s.equalsIgnoreCase("जानकारी")
                            || s.equalsIgnoreCase("पोषण")
                            || s.equalsIgnoreCase("स्पेशल स्टोरी")
                            || s.equalsIgnoreCase("राशिफल")
                            || s.equalsIgnoreCase("अवसर")
                            || s.equalsIgnoreCase("Health")
                            || s.equalsIgnoreCase("Lifestyle")
                            || s.equalsIgnoreCase("जीवन शैली")
                            || s.equalsIgnoreCase("जिज्ञासा र जवाफ")
                            || s.equalsIgnoreCase("सौन्दर्य")
                            || s.equalsIgnoreCase("डाक्टर आर्टिकल")
                            || s.equalsIgnoreCase("थायरोइड समस्या")
                            || s.equalsIgnoreCase("यौन स्वास्थ्य")
                            || s.equalsIgnoreCase("मोफसल")
                            || s.equalsIgnoreCase("उपभाेक्ता")
                            || s.equalsIgnoreCase("छुटाउनु भयो कि ?")
                            || s.equalsIgnoreCase("श्वास्थ्य")
                            || s.equalsIgnoreCase("हेल्थ न्यूज")
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
                            || s.equalsIgnoreCase("फुड")
                            || s.equalsIgnoreCase("स्वास्थ्य खबर")
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

        for (int i = 0; i < CATEGORY_NUMBER; i++) {
            List<EntriesItem> processedFeeds;//new
            processedFeeds = main.get(i);
            processedFeeds = Parse.deleteDuplicate(processedFeeds); //delete duplicate feeds
            processedFeeds = Parse.deleteEnglishFeeds(processedFeeds);  //delete english feeds
            processedFeeds = Parse.sortByTime(processedFeeds);  //sort by time feeds feeds*//*
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
            clearFeedsByPref(processedFeeds);
            Paper.book().write(header.getSecondName() + "NP", processedFeeds);

        }
        Paper.book().write("AllFeedsNP", objects);
    }

    private void clearFeedsByPref(List<EntriesItem> feeds) {
        int limitSize = 50;
        if (feeds.size() > limitSize) {
            feeds.subList(limitSize, feeds.size()).clear();
        }

    }

}
