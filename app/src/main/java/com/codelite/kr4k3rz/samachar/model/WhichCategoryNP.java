package com.codelite.kr4k3rz.samachar.model;


public enum WhichCategoryNP {

    BREAKING("ताजा खबर", "BreakingFrag"),
    NATIONAL("राष्ट्रिय", "NationalFrag"),
    WORLD("विश्व", "WorldFrag"),
    BUSINESS("अर्थ", "BusinessFrag"),
    TECHNOLOGY("सूचना प्रविधि", "TechnologyFrag"),
    ENTERTAINMENT("मनोरञ्जन", "EntertainmentFrag"),
    HEALTH("स्वास्थ्य", "HealthFrag"),
    SPORT("खेलकुद", "SportFrag"),
    IMGVID("ImageVideo", "ImgVidFrag");


    final String mFirstName;
    final String mSecondName;

    WhichCategoryNP(String mFirstName, String mSecondName) {
        this.mFirstName = mFirstName;
        this.mSecondName = mSecondName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getSecondName() {
        return mSecondName;
    }
}
