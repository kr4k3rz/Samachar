package com.codelite.kr4k3rz.samachar.activity;

import android.content.Intent;
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
                .setDescription("समाचार app is all in one app optimised for network performance and look & feel for latest news feeds.")
                .addItem(new Element().setTitle("Version " + BuildConfig.VERSION_NAME))
                .addEmail("xitize@gmail.com")
                .addFacebook("kr4k3rz")
                .addPlayStore("com.codelite.kr4k3rz.samachar").
                        addItem(new Element("Share", "share", R.drawable.share).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2 = new Intent();
                                intent2.setAction(Intent.ACTION_SEND);
                                intent2.setType("text/plain");
                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                intent2.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);
                                startActivity(Intent.createChooser(intent2, "Share via"));

                            }
                        }))
                .create();

        setContentView(mAboutPage);
    }

}

