package com.codelite.kr4k3rz.samachar.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.Category;
import com.codelite.kr4k3rz.samachar.ui.adapter.CustomAdapter;

import java.util.ArrayList;

import io.paperdb.Paper;

public class EditCategory extends AppCompatActivity {
    private ArrayList<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit_category);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Newspaper");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_category);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        categories = Paper.book().read("updatedData");
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), categories);
        recyclerView.setAdapter(customAdapter);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Paper.book().write("updatedData", categories);
    }
}
