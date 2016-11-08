package com.codelite.kr4k3rz.samachar.ui.trending;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.WhichCategoryNP;
import com.codelite.kr4k3rz.samachar.model.feed.EntriesItem;
import com.codelite.kr4k3rz.samachar.ui.allnews.RvAdapter;
import com.codelite.kr4k3rz.samachar.util.CacheLang;

import java.util.List;

import io.paperdb.Paper;


/**
 * A simple {@link Fragment} subclass.
 */
public class BreakingFrag extends Fragment {
    private static final String CACHE_NAME = WhichCategoryNP.BREAKING.getSecondName();
    private MyReceiver r;
    private RecyclerView recyclerView;

    public BreakingFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_breaking_news, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_Breaking_News);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        List<EntriesItem> list = Paper.book().read(CacheLang.findLang(CACHE_NAME));
        recyclerView.setAdapter(new RvAdapter(getContext(), list));
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(r);
    }

    @Override
    public void onResume() {
        super.onResume();
        r = new MyReceiver();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(r,
                new IntentFilter("TAG_REFRESH"));
    }

    private void refresh() {
        //yout code in refresh.
        Log.i("Refresh", "YES");
        if (Paper.book().exist("RefreshCheck")) {
            boolean b = Paper.book().read("RefreshCheck");
            Log.i("Refresh", "boolean" + b);

            if (b) {
                List<EntriesItem> list = Paper.book().read(CacheLang.findLang(CACHE_NAME));
                recyclerView.setAdapter(new RvAdapter(getContext(), list));
                Log.i("Refresh", "YES");
                Paper.book().write("RefreshCheck", false);
            }

        }

    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BreakingFrag.this.refresh();
        }
    }
}