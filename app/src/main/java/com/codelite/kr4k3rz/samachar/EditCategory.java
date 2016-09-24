package com.codelite.kr4k3rz.samachar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codelite.kr4k3rz.samachar.adapter.CustomAdapter;
import com.codelite.kr4k3rz.samachar.model.Category;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

public class EditCategory extends AppCompatActivity {
    private ArrayList<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("NewsPapers");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_category);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        categories = Hawk.get("updatedData");
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), categories);
        recyclerView.setAdapter(customAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Hawk.put("updatedData", categories);


    }
}
