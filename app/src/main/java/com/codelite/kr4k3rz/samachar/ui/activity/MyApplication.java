package com.codelite.kr4k3rz.samachar.ui.activity;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import io.paperdb.Paper;


public class MyApplication extends Application {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Paper.init(getBaseContext());
        String lang = "NP";
        Paper.book().write("language", lang);
    }


}
