package com.codelite.kr4k3rz.samachar.ui.settings;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.feed.EntriesItem;
import com.codelite.kr4k3rz.samachar.model.feed.ResponseFeed;
import com.codelite.kr4k3rz.samachar.ui.allnews.RvAdapter;
import com.codelite.kr4k3rz.samachar.util.Parse;
import com.codelite.kr4k3rz.samachar.util.ToastMsg;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Newspaper extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler;

    public Newspaper() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_newspaper, container, false);
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_newspaper);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh_newspaper);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        handler = new Handler(Looper.getMainLooper());
        final int position = getArguments().getInt("POSITION");
        final String link = getArguments().getString("LINK");
        Log.i("TAG", "link : " + link);
        final String lang = Paper.book().read("language");
        if (lang.equalsIgnoreCase("NP")) {
            List<EntriesItem> list = Paper.book().read(lang + "newspaper" + position);
            recyclerView.setAdapter(new RvAdapter(getContext(), list));
        } else {
            List<EntriesItem> list = Paper.book().read(lang + "newspaper" + position);
            recyclerView.setAdapter(new RvAdapter(getContext(), list));
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFeed(link, position, recyclerView, lang);
            }
        });


        return rootView;
    }

    private void loadFeed(String link, final int position, final RecyclerView recyclerView, final String lang) {
        OkHttpClient okHttpClient = new OkHttpClient();
        assert link != null;
        Request request = new Request.Builder().url(link).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastMsg.shortMsg(getContext(), "failed to load");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();    //loads JSON String feeds by Okhttp
                Log.i("TAG", " : " + jsonStr);
                ResponseFeed responseFeed = new Gson().fromJson(jsonStr, ResponseFeed.class);
                List<EntriesItem> posts = responseFeed.getResponseData().getFeed().getEntries();
                List<EntriesItem> list = Paper.book().read(lang + "newspaper" + position);
                List<EntriesItem> temp;

                if (list == null) {
                    temp = Parse.deleteDuplicate(posts);

                } else {
                    list.addAll(posts);
                    temp = Parse.deleteDuplicate(list);
                }
                temp = Parse.deleteEnglishFeeds(temp);
                temp = Parse.sortByTime(temp);
                Paper.book().write(lang + "newspaper" + position, temp);
                final List<EntriesItem> finalTemp = temp;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(new RvAdapter(getContext(), finalTemp));
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });


            }
        });
    }


}
