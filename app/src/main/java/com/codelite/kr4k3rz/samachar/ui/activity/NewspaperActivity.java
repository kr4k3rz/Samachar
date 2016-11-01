package com.codelite.kr4k3rz.samachar.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.ui.fragments.Newspaper;

public class NewspaperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newspaper);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newspaper);
        setSupportActionBar(toolbar);
        String name = getIntent().getExtras().getString("NEWSPAPER_NAME");
        int position = getIntent().getExtras().getInt("POSITION");
        String link = getIntent().getExtras().getString("LINK");
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        bundle.putString("LINK", link);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(name);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Newspaper newspaper = new Newspaper();
        newspaper.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer_newspaper, newspaper).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
