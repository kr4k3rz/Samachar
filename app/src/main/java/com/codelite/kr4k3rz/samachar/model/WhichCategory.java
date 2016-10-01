package com.codelite.kr4k3rz.samachar.model;

/**
 * Created by kr4k3rz on 9/28/16.
 */

public enum WhichCategory {

    BREAKING("breaking"),
    NEWSPAPER("newspaper"),
    NATIONAL("national"),
    LOCAL("local"),
    OPINION("opinion"),
    WORLD("world"),
    BUSINESS("business"),
    TECHNOLOGY("technology"),
    ENTERTAINMENT("entertainment"),
    HEALTH("health"),
    SPORT("sport");


    String mNmae;

    WhichCategory(String mNmae) {
        this.mNmae = mNmae;
    }

    String getName() {
        return mNmae;
    }
}
