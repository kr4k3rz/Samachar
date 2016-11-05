package com.codelite.kr4k3rz.samachar.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.ui.fragments.others.FavouriteFrag;

public class SavedArticle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_saved_article);
        toolbar.setTitle("Saved Articles");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fContent_saved_article, new FavouriteFrag()).commit();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}