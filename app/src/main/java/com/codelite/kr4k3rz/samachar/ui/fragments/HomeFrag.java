package com.codelite.kr4k3rz.samachar.ui.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
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
import com.codelite.kr4k3rz.samachar.model.NewspaperEN;
import com.codelite.kr4k3rz.samachar.model.NewspaperNP;
import com.codelite.kr4k3rz.samachar.ui.adapter.ComplexRecyclerViewAdapter;
import com.codelite.kr4k3rz.samachar.ui.adapter.SimpleDividerItemDecoration;
import com.codelite.kr4k3rz.samachar.util.CacheLang;
import com.codelite.kr4k3rz.samachar.util.FilterCategoryEN;
import com.codelite.kr4k3rz.samachar.util.FilterCategoryNP;
import com.codelite.kr4k3rz.samachar.util.Parse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import angtrim.com.fivestarslibrary.FiveStarsDialog;
import angtrim.com.fivestarslibrary.NegativeReviewListener;
import angtrim.com.fivestarslibrary.ReviewListener;
import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFrag extends Fragment {
    private static final String CACHE_NAME = "AllFeeds";
    private static final String TAG = HomeFrag.class.getSimpleName();
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private boolean b;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Handler mHandler;


    public HomeFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_feed, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_All_Feeds);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh_allFeeds);
        recyclerView.setHasFixedSize(false);
        mHandler = new Handler(Looper.getMainLooper());
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        Log.i(TAG, CacheLang.findLang(CACHE_NAME));
        List<Object> objects = Paper.book().read(CacheLang.findLang(CACHE_NAME));
        if (objects == null) {
            swipeRefreshLayout.setRefreshing(true);
            loadENFeeds(okHttpClient);
        }
        recyclerView.setAdapter(new ComplexRecyclerViewAdapter(getContext(), objects));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final OkHttpClient okHttpClient = new OkHttpClient();
                String lang = Paper.book().read("language");
                Log.i(TAG, "lang : " + lang);

                switch (lang) {
                    case "NP":
                        loadNPFeeds(okHttpClient);
                        break;
                    case "EN":
                        loadENFeeds(okHttpClient);
                        break;
                }

            }


        });

        ratingApp();
    }

    private void ratingApp() {
        FiveStarsDialog fiveStarsDialog = new FiveStarsDialog(getContext(), "xitize@gmail.com");
        fiveStarsDialog.setRateText("How would you rate this app?")
                .setTitle("Rate your experience")
                .setForceMode(false)
                .setUpperBound(2) // Market opened if a rating >= 2 is selected
                .setNegativeReviewListener(new NegativeReviewListener() {
                    @Override
                    public void onNegativeReview(int i) {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "xitize@gmail.com", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Samachar FeedBack");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi,i would like to ");
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    }
                }) // OVERRIDE mail intent for negative review
                .setReviewListener(new ReviewListener() {
                    @Override
                    public void onReview(int i) {
                    }
                }) // Used to listen for reviews (if you want to track them )
                .showAfter(10);
    }

    private void loadOnRefresh() {
        b = true;
        Paper.book().write("RefreshCheck", b);
        Log.i("TAG", "boolean set");
    }

    private Collection<? extends Entry> setTitleLink(String feedTitle, String feedLink, List<Entry> posts) {
        List<Entry> entries = new ArrayList<>();
        entries.clear();
        for (int i = 0; i < posts.size(); i++) {
            Entry entry = posts.get(i);
            entry.setTitleFeed(feedTitle);
            entry.setLinkFeed(feedLink);
            entries.add(entry);
        }
        return entries;
    }

    private void loadNPFeeds(final OkHttpClient okHttpClient) {
        final List<Entry> list_nepali = new ArrayList<>();
        final ArrayList<String> rss = new NewspaperNP().getLinksList();
        for (int i = 0; i < rss.size(); i++) {
            Request request = new Request.Builder().url(rss.get(i)).build();
            final int failureCount = i;
            final float responseCount = i;
            final int finalI = i;
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    okHttpClient.dispatcher().cancelAll();
                    Log.i("TAG", "" + e);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (failureCount == rss.size() - 1) {
                                FilterCategoryNP filterCategory = new FilterCategoryNP(list_nepali, getContext());
                                filterCategory.filter();
                                List<Object> objects = Paper.book().read(CacheLang.findLang(CACHE_NAME));
                                if (objects != null)
                                    recyclerView.setAdapter(new ComplexRecyclerViewAdapter(getContext(), objects));
                                swipeRefreshLayout.setRefreshing(false);
                                loadOnRefresh();

                            }
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
                        String feedTitle = feeds.getString("title");
                        String feedLink = feeds.getString("link");
                        Log.i(TAG, "FeedLink : " + feedLink + "\n Feed title : " + feedTitle);
                        JSONArray entries = feeds.getJSONArray("entries");
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Entry>>() {
                        }.getType();
                        List<Entry> posts = gson.fromJson(String.valueOf(entries), listType);
                        ArrayList<Entry> entryArrayList = new ArrayList<>();
                        entryArrayList.clear();
                        entryArrayList = Paper.book().read("NP" + "newspaper" + finalI);
                        entryArrayList.addAll(posts);
                        List<Entry> tempList;
                        tempList = Parse.deleteDuplicate(entryArrayList);
                        tempList = Parse.deleteEnglishFeeds(tempList);
                        tempList = Parse.sortByTime(tempList);
                        Paper.book().write("NP" + "newspaper" + finalI, tempList);
                        list_nepali.addAll(setTitleLink(feedTitle, feedLink, posts));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (responseCount == rss.size() - 1) {
                                Log.i("TAG", "on completed: ");
                                FilterCategoryNP filterCategory = new FilterCategoryNP(list_nepali, getContext());
                                filterCategory.filter();
                                List<Object> objects = Paper.book().read(CacheLang.findLang(CACHE_NAME));
                                if (objects != null)
                                    recyclerView.setAdapter(new ComplexRecyclerViewAdapter(getContext(), objects));
                                swipeRefreshLayout.setRefreshing(false);
                                loadOnRefresh();
                            }
                        }
                    });

                }
            });

        }
    }

    private void loadENFeeds(OkHttpClient okHttpClient) {
        final List<Entry> list_English = new ArrayList<>();
        final List<String> rss_english;
        rss_english = new NewspaperEN().getLinksList();

            /*ENGLISH*/
        for (int i = 0; i < rss_english.size(); i++) {
            Request request = new Request.Builder().url(rss_english.get(i)).build();
            final int failureCount = i;
            final float responseCount = i;
            final int finalI = i;
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("TAG", "" + e);
                    if (failureCount == rss_english.size() - 1) {
                        FilterCategoryEN filterCategory = new FilterCategoryEN(list_English, getContext());
                        filterCategory.filter();
                        List<Object> objects = Paper.book().read(CacheLang.findLang(CACHE_NAME));
                        if (objects != null)
                            recyclerView.setAdapter(new ComplexRecyclerViewAdapter(getContext(), objects));
                        swipeRefreshLayout.setRefreshing(false);
                        loadOnRefresh();


                    }

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    String jsonStr = response.body().string();
                    Log.i("TAG", " : " + jsonStr);
                    try {
                        JSONObject mainNode = new JSONObject(jsonStr);
                        JSONObject responseData = mainNode.getJSONObject("responseData");
                        JSONObject feeds = responseData.getJSONObject("feed");
                        String feedTitle = feeds.getString("title");
                        String feedLink = feeds.getString("link");
                        Log.i(TAG, "FeedLink : " + feedLink + "\n Feed title : " + feedTitle);
                        JSONArray entries = feeds.getJSONArray("entries");
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Entry>>() {
                        }.getType();
                        List<Entry> posts = gson.fromJson(String.valueOf(entries), listType);
                        if (Paper.book().exist("EN" + "newspaper" + finalI)) {
                            List<Entry> entryList;
                            entryList = Paper.book().read("EN" + "newspaper" + finalI);
                            entryList.addAll(posts);
                            List<Entry> temp;
                            temp = Parse.deleteDuplicate(entryList);
                            temp = Parse.deleteNonEngFeeds(temp);
                            temp = Parse.sortByTime(temp);
                            Paper.book().write("EN" + "newspaper" + finalI, temp);
                        } else {
                            List<Entry> tempList;
                            tempList = Parse.deleteDuplicate(posts);
                            tempList = Parse.deleteNonEngFeeds(tempList);
                            tempList = Parse.sortByTime(tempList);
                            Paper.book().write("EN" + "newspaper" + finalI, tempList);

                        }
                        list_English.addAll(setTitleLink(feedTitle, feedLink, posts));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (responseCount == rss_english.size() - 1) {
                                Log.i("IN FINAL", "OK");
                                FilterCategoryEN filterCategoryEN = new FilterCategoryEN(list_English, getContext());
                                filterCategoryEN.filter();
                                List<Object> objects = Paper.book().read(CacheLang.findLang(CACHE_NAME));
                                if (objects != null)
                                    recyclerView.setAdapter(new ComplexRecyclerViewAdapter(getContext(), objects));
                                swipeRefreshLayout.setRefreshing(false);
                                loadOnRefresh();
                            }
                        }
                    });
                }


            });


        }
    }


}
