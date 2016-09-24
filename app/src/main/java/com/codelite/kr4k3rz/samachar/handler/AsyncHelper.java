package com.codelite.kr4k3rz.samachar.handler;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.codelite.kr4k3rz.samachar.adapter.RvAdapter;
import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.util.Parse;
import com.codelite.kr4k3rz.samachar.util.SnackMsg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class AsyncHelper extends AsyncTask<String, Void, List<Entry>> {

    private final SwipeRefreshLayout refreshLayout;
    private final String cacheName;
    private final Context context;
    private final RecyclerView recyclerView;
    private final View rootView;

    public AsyncHelper(View rootView, SwipeRefreshLayout refreshLayout, Context context, String cacheName, RecyclerView recyclerView) {
        this.refreshLayout = refreshLayout;
        this.context = context;
        this.cacheName = cacheName;
        this.recyclerView = recyclerView;
        this.rootView = rootView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        refreshLayout.setRefreshing(true);
    }

    @Override
    protected List<Entry> doInBackground(String... rss) {
        List<Entry> list = new ArrayList<>();
        try {
            for (String rs : rss) {
                if (!rs.equalsIgnoreCase("")) {
                    String jsonStr = new PullData().run(rs);    //loads JSON String feeds by Okhttp
                    Log.i("TAG", " : " + jsonStr);
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
            Log.i("TAG", "Internet not working or failed to download / 200 error");
        }
        return list;

    }

    @Override
    protected void onPostExecute(List<Entry> entries) {
        super.onPostExecute(entries);
        int oldFeedsSize, newFeedsSize;

        List<Entry> oldFeeds = Hawk.get(cacheName);
        if (oldFeeds != null) {
            //have old feeds
            oldFeedsSize = oldFeeds.size(); //number of old feeds
            Log.i("TAG", "old feeds  size:" + oldFeedsSize);
            entries.addAll(oldFeeds);
        } else oldFeedsSize = 0;
        List<Entry> newFeeds;
        newFeeds = entries;
        newFeeds = Parse.deleteDuplicate(newFeeds); //delete duplicate feeds
        newFeeds = Parse.deleteEnglishFeeds(newFeeds);  //delete english feeds
        newFeeds = Parse.sortByTime(newFeeds);  //sort by time feeds feeds

        newFeedsSize = newFeeds.size();
        Log.i("TAG", "new feeds size : " + newFeedsSize);

        recyclerView.setAdapter(new RvAdapter(context, newFeeds));


        int numStore = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("feedsToStore", String.valueOf(300)));
        Log.i("TAG", "Pref Value : " + numStore);

        if (newFeedsSize > numStore) {
            entries.subList(numStore, newFeedsSize).clear();
        }


        Hawk.put(cacheName, newFeeds);
        if (newFeedsSize - oldFeedsSize == 0)
            SnackMsg.showMsgShort(rootView, "zero feeds");
        else
            SnackMsg.showMsgShort(rootView, newFeedsSize - oldFeedsSize + " feeds loaded ");
        refreshLayout.setRefreshing(false);
    }


}

