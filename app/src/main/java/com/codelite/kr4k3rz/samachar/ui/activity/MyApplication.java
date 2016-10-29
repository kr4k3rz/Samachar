package com.codelite.kr4k3rz.samachar.ui.activity;

import android.app.Application;

import io.paperdb.Paper;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(getBaseContext());
        String lang = "NP";
        Paper.book().write("language", lang);
    }
}
