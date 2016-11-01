package com.codelite.kr4k3rz.samachar.ui.fragments;


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
import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.ui.adapter.RvAdapter;
import com.codelite.kr4k3rz.samachar.ui.adapter.SimpleDividerItemDecoration;
import com.codelite.kr4k3rz.samachar.util.Parse;
import com.codelite.kr4k3rz.samachar.util.ToastMsg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
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
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        handler = new Handler(Looper.getMainLooper());
        final int position = getArguments().getInt("POSITION");
        final String link = getArguments().getString("LINK");
        Log.i("TAG", "link : " + link);

        final String lang = Paper.book().read("language");
        if (lang.equalsIgnoreCase("NP")) {
            List<Entry> list = Paper.book().read(lang + "newspaper" + position);
            recyclerView.setAdapter(new RvAdapter(getContext(), list));
        } else {
            List<Entry> list = Paper.book().read(lang + "newspaper" + position);
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
                try {
                    JSONObject mainNode = new JSONObject(jsonStr);
                    JSONObject responseData = mainNode.getJSONObject("responseData");
                    JSONObject feeds = responseData.getJSONObject("feed");
                    JSONArray entries = feeds.getJSONArray("entries");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Entry>>() {
                    }.getType();
                    List<Entry> posts = gson.fromJson(String.valueOf(entries), listType);
                    List<Entry> list = Paper.book().read(lang + "newspaper" + position);
                    list.addAll(posts);
                    List<Entry> temp;
                    temp = Parse.deleteDuplicate(list);
                    temp = Parse.deleteEnglishFeeds(temp);
                    temp = Parse.sortByTime(temp);
                    Paper.book().write(lang + "newspaper" + position, temp);
                    final List<Entry> finalTemp = temp;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(new RvAdapter(getContext(), finalTemp));
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


}
