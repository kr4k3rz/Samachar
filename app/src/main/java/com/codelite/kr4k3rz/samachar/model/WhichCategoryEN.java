package com.codelite.kr4k3rz.samachar.model;
public enum WhichCategoryEN {

    BREAKING("Breaking", "BreakingFrag"),
    NATIONAL("National", "NationalFrag"),
    WORLD("World", "WorldFrag"),
    BUSINESS("Business", "BusinessFrag"),
    TECHNOLOGY("Technology", "TechnologyFrag"),
    ENTERTAINMENT("Entertainment", "EntertainmentFrag"),
    HEALTH("Health", "HealthFrag"),
    SPORT("Sport", "SportFrag"),
    IMGVID("ImageVideo", "ImgVidFrag");


    final String mFirstName;
    final String mSecondName;

    WhichCategoryEN(String mFirstName, String mSecondName) {
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


