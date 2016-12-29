package com.codelite.kr4k3rz.samachar.ui.trending;


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
import com.codelite.kr4k3rz.samachar.model.NewspaperEN;
import com.codelite.kr4k3rz.samachar.model.NewspaperNP;
import com.codelite.kr4k3rz.samachar.model.feed.EntriesItem;
import com.codelite.kr4k3rz.samachar.model.feed.ResponseFeed;
import com.codelite.kr4k3rz.samachar.util.CacheLang;
import com.codelite.kr4k3rz.samachar.util.CheckInternet;
import com.codelite.kr4k3rz.samachar.util.FilterCategoryEN;
import com.codelite.kr4k3rz.samachar.util.FilterCategoryNP;
import com.codelite.kr4k3rz.samachar.util.Parse;
import com.codelite.kr4k3rz.samachar.util.SnackMsg;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
public class HomeFrag extends Fragment {
    private static final String CACHE_NAME = "AllFeeds";
    private static final String TAG = HomeFrag.class.getSimpleName();
    private final OkHttpClient okHttpClient = new OkHttpClient();
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
                if (CheckInternet.isNetworkAvailable(getContext()))
                    loadFeedsOnRefresh();
                else {
                    SnackMsg.showMsgLong(recyclerView, "connect to internet");
                    swipeRefreshLayout.setRefreshing(false);
                }

            }


        });


    }

    private void loadFeedsOnRefresh() {
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


    private void loadOnRefresh() {
        Paper.book().write("RefreshCheck", true);
        Log.i("TAG", "boolean set");
    }

    private Collection<? extends EntriesItem> setTitleLink(String feedTitle, String feedLink, List<EntriesItem> posts) {
        List<EntriesItem> entries = new ArrayList<>();
        entries.clear();
        for (int i = 0; i < posts.size(); i++) {
            EntriesItem entry = posts.get(i);
            entry.setTitleFeed(feedTitle);
            entry.setLinkFeed(feedLink);
            entries.add(entry);
        }
        return entries;
    }

    private void loadNPFeeds(final OkHttpClient okHttpClient) {
        final List<EntriesItem> list_nepali = new ArrayList<>();
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
                    ResponseFeed responseFeed = new Gson().fromJson(jsonStr, ResponseFeed.class);
                    List<EntriesItem> posts = responseFeed.getResponseData().getFeed().getEntries();
                    ArrayList<EntriesItem> entryArrayList = new ArrayList<>();
                    entryArrayList.clear();
                    entryArrayList = Paper.book().read("NP" + "newspaper" + finalI);
                    List<EntriesItem> tempList;
                    if (entryArrayList == null) {
                        tempList = Parse.deleteDuplicate(posts);
                    } else {
                        entryArrayList.addAll(posts);
                        tempList = Parse.deleteDuplicate(entryArrayList);
                    }
                    tempList = Parse.deleteEnglishFeeds(tempList);
                    tempList = Parse.sortByTime(tempList);
                    Paper.book().write("NP" + "newspaper" + finalI, tempList);
                    list_nepali.addAll(setTitleLink(responseFeed.getResponseData().getFeed().getTitle(), responseFeed.getResponseData().getFeed().getLink(), posts));

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
        final List<EntriesItem> list_English = new ArrayList<>();
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
                        final List<Object> objects = Paper.book().read(CacheLang.findLang(CACHE_NAME));
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (objects != null)
                                    recyclerView.setAdapter(new ComplexRecyclerViewAdapter(getContext(), objects));
                                swipeRefreshLayout.setRefreshing(false);
                                loadOnRefresh();
                            }
                        });


                    }

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    String jsonStr = response.body().string();
                    Log.i("TAG", " : " + jsonStr);
                    ResponseFeed responseFeed = new Gson().fromJson(jsonStr, ResponseFeed.class);
                    List<EntriesItem> posts = responseFeed.getResponseData().getFeed().getEntries();
                    if (Paper.book().exist("EN" + "newspaper" + finalI)) {
                        List<EntriesItem> entryList;
                        entryList = Paper.book().read("EN" + "newspaper" + finalI);
                        entryList.addAll(posts);
                        List<EntriesItem> temp;
                        temp = Parse.deleteDuplicate(entryList);
                        temp = Parse.deleteNonEngFeeds(temp);
                        temp = Parse.sortByTime(temp);
                        Paper.book().write("EN" + "newspaper" + finalI, temp);
                    } else {
                        List<EntriesItem> tempList;
                        tempList = Parse.deleteDuplicate(posts);
                        tempList = Parse.deleteNonEngFeeds(tempList);
                        tempList = Parse.sortByTime(tempList);
                        Paper.book().write("EN" + "newspaper" + finalI, tempList);

                    }
                    list_English.addAll(setTitleLink(responseFeed.getResponseData().getFeed().getTitle(), responseFeed.getResponseData().getFeed().getLink(), posts));
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
