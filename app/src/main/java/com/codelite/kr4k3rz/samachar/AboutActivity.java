package com.codelite.kr4k3rz.samachar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About");
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher)
                .setDescription("all newspaper in one app optimised for network performance")
                .addItem(new Element().setTitle("Version 6.2"))
                .addEmail("xitize@gmail.com")
                .addFacebook("kr4k3rz")
                .addPlayStore("com.codelite.kr4k3rz.samachar")
                .create();

        setContentView(aboutPage);
    }

}

