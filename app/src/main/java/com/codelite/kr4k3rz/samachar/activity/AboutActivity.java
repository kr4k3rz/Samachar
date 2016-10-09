package com.codelite.kr4k3rz.samachar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.codelite.kr4k3rz.samachar.BuildConfig;
import com.codelite.kr4k3rz.samachar.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mAboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher)
                .setDescription(R.string.app_name + "app is all in one app optimised for network performance and look & feel for latest news feeds.")
                .addItem(new Element().setTitle(BuildConfig.VERSION_NAME+"v"))
                .addEmail("xitize@gmail.com")
                .addFacebook("kr4k3rz")
                .addPlayStore("com.codelite.kr4k3rz.samachar")
                .create();

        setContentView(mAboutPage);
    }

}

