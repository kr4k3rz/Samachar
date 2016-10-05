package com.codelite.kr4k3rz.samachar.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.Header;

public class CategoryActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_category);
        setSupportActionBar(toolbar);
        Header header = (Header) getIntent().getSerializableExtra("HEADER");
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(header.getFirstName());
        if (getSupportActionBar() != null)
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        Fragment fragment;
        try {
            fragment = (Fragment) Class.forName("com.codelite.kr4k3rz.samachar.fragments." + header.getSecondName()).newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
