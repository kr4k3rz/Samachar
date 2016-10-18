package com.codelite.kr4k3rz.samachar.handler;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.ui.adapter.RvAdapter;
import com.codelite.kr4k3rz.samachar.util.FilterCategoryNP;
import com.codelite.kr4k3rz.samachar.util.SnackMsg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;


public class AsyncHelper extends AsyncTask<String, Void, Void> {
    private final String TAG = AsyncHelper.class.getName();
    private final SwipeRefreshLayout refreshLayout;
    private final String cacheName;
    private final Context context;
    private final RecyclerView recyclerView;
    private final View rootView;
    private final int categoryNum;
    private int feedSize;

    public AsyncHelper(View rootView, SwipeRefreshLayout refreshLayout, Context context, String cacheName, RecyclerView recyclerView, int categoryNum) {
        this.refreshLayout = refreshLayout;
        this.context = context;
        this.cacheName = cacheName;
        this.recyclerView = recyclerView;
        this.rootView = rootView;
        this.categoryNum = categoryNum;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        refreshLayout.setRefreshing(true);
    }

    @Override
    protected Void doInBackground(String... rss) {
        List<Entry> list = new ArrayList<>();
        try {
            for (String rs : rss) {
                if (!rs.equalsIgnoreCase("")) {
                    String jsonStr = new PullData().run(rs);    //loads JSON String feeds by Okhttp
                    Log.i(TAG, " : " + jsonStr);
                    JSONObject mainNode = new JSONObject(jsonStr);
                    JSONObject responseData = mainNode.getJSONObject("responseData");
                    JSONObject feeds = responseData.getJSONObject("feed");
                    JSONArray entries = feeds.getJSONArray("entries");

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Entry>>() {
                    }.getType();
                    List<Entry> posts = gson.fromJson(String.valueOf(entries), listType);
                    list.addAll(posts);
                }

            }


        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "Internet not working or failed to download / 200 error");
        }

        FilterCategoryNP filterCategory = new FilterCategoryNP(list, context);
        try {
            filterCategory.filter();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        List<Entry> feeds = Paper.book().read(cacheName);
        recyclerView.setAdapter(new RvAdapter(context, feeds));
        refreshLayout.setRefreshing(false);
        if (feedSize == 0)
            SnackMsg.showMsgLong(rootView, "zero feeds loaded");
        else
            SnackMsg.showMsgShort(rootView, feedSize + " feeds loaded");


    }


}