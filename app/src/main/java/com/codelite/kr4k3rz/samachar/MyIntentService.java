package com.codelite.kr4k3rz.samachar;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.codelite.kr4k3rz.samachar.handler.PullData;
import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.util.CheckInternet;
import com.codelite.kr4k3rz.samachar.util.FeedLists;
import com.codelite.kr4k3rz.samachar.util.Parse;
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


public class MyIntentService extends IntentService {


    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
        if (CheckInternet.isNetworkAvailable(getApplicationContext())) {
            Log.i("MyIntentService", "Intent Service fired");
            if (CheckInternet.isNetworkAvailable(getApplicationContext()))
                updateDataNotify();
        }

    }

    private void updateDataNotify() {
        String[] rss = FeedLists.getFeedListLatest(0);
        List<Entry> list = new ArrayList<>();

        try {
            for (String rs : rss) {
                if (!rs.equalsIgnoreCase("")) {
                    String jsonStr = new PullData().run(rs);    //loads JSON String feeds by Okhttp
                    Log.i("TAG", ":>  " + jsonStr);
                /*parsing the pulled JSON string into ArrayList<Entry> */
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
            // checkError = true;
            Log.i("TAG", "Internet not working or failed to download / 200 error");
        }


        if (list.size() != 0) {
            List<Entry> oldFeeds = Hawk.get("Headlines");
            if (oldFeeds != null) {
                list.addAll(oldFeeds);
            }
            List<Entry> newFeeds;
            newFeeds = Parse.deleteDuplicate(list); //delete duplicate
            newFeeds = Parse.deleteEnglishFeeds(newFeeds);  //delete english feed if present
            Parse.sortByTime(newFeeds).size();
            Hawk.put("Headlines", newFeeds);
            Entry entry;
            entry = newFeeds.get(0);
            Intent intent4 = new Intent(getApplicationContext(), DetailFeed.class);
            intent4.putExtra("title", entry.getTitle());
            intent4.putExtra("date", entry.getDate());
            intent4.putExtra("content", entry.getContent());
            intent4.putExtra("author", entry.getAuthor());
            intent4.putExtra("link", entry.getLink());
            intent4.putStringArrayListExtra("categories", (ArrayList<String>) entry.getCategories());
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent4,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            android.support.v4.app.NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setContentTitle(entry.getTitle())
                            .setContentText(Html.fromHtml(entry.getContentSnippet().replace("...", "").replace("[…]", "")))
                            .setContentIntent(pIntent);
            Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(notificationSound);
            Notification notify = mBuilder.build();
            notify.flags = Notification.FLAG_AUTO_CANCEL;
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notify);

        }


    }
}