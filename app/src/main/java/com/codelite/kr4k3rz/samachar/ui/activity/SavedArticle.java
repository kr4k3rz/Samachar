package com.codelite.kr4k3rz.samachar.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.ui.fragments.FavouriteFrag;

public class SavedArticle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_saved_article);
        toolbar.setTitle("Saved Article");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fContent_saved_article, new FavouriteFrag()).commit();

    }
}