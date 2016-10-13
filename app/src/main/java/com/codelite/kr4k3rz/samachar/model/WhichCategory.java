package com.codelite.kr4k3rz.samachar.model;


public enum WhichCategory {

    BREAKING("ताजा खबर", "BreakingNews"),
    NEWSPAPER("पत्रपत्रिका", "NewsPaperFrag"),
    NATIONAL("राष्ट्रिय समाचार", "NationalFrag"),
    LOCAL("समाज", "LocalFrag"),
    OPINION("विचार", "OpinionFrag"),
    WORLD("विश्व", "WorldFrag"),
    BUSINESS("अर्थ", "BusinessFrag"),
    TECHNOLOGY("सूचना प्रविधि", "TechnologyFrag"),
    ENTERTAINMENT("मनोरञ्जन", "EntertainmentFrag"),
    HEALTH("स्वास्थ्य", "HealthFrag"),
    SPORT("खेलकुद", "SportFrag"),
    IMGVID("ImageVideo", "ImgVid");


    final String mFirstName;
    final String mSecondName;

    WhichCategory(String mFirstName, String mSecondName) {
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
