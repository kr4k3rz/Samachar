package com.codelite.kr4k3rz.samachar.ui.activity;

import android.app.Application;

import io.paperdb.Paper;


public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Paper.init(getBaseContext());
        String lang = "NP";
        Paper.book().write("language", lang);
    }


}
