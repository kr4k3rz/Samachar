package com.codelite.kr4k3rz.samachar.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.ui.fragments.others.NewspaperList;

public class NewspapersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newspapers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newspapers_list);
        toolbar.setTitle("Newspapers");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_newspaper_list, new NewspaperList()).commit();
    }
}
