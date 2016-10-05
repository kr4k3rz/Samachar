package com.codelite.kr4k3rz.samachar.worker;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.Html;
import android.util.Log;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.activity.DetailFeed;
import com.codelite.kr4k3rz.samachar.handler.PullData;
import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.model.FeedLists;
import com.codelite.kr4k3rz.samachar.util.CheckInternet;
import com.codelite.kr4k3rz.samachar.util.Parse;
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


public class MyIntentService extends IntentService {

    /*
    * download all newsFeeds show only the Breaking news
    * save the feeds into file show that later it used*/
    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
        Log.i("TAG", "IntentService fired");

        if (CheckInternet.isNetworkAvailable(getApplicationContext()))
            updateDataNotify();

    }

    private void updateDataNotify() {
        String[] rss = FeedLists.getFeedListLatest(0);
        List<Entry> list = new ArrayList<>();

        try {
            for (String rs : rss) {
                if (!rs.equalsIgnoreCase("")) {
                    String jsonStr = new PullData().run(rs);    //loads JSON String feeds by Okhttp
                    Log.i("TAG", ":>  " + jsonStr);
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


        if (list.size() != 0) {
            List<Entry> oldFeeds = Paper.book().read("Headlines");
            if (oldFeeds != null) {
                list.addAll(oldFeeds);
            }
            List<Entry> newFeeds;
            newFeeds = Parse.deleteDuplicate(list); //delete duplicate
            newFeeds = Parse.deleteEnglishFeeds(newFeeds);  //delete english feed if present
            Parse.sortByTime(newFeeds).size();
            Paper.book().write("Headlines", newFeeds);
            Entry entry;
            entry = newFeeds.get(0);
            Intent intent = new Intent(getApplicationContext(), DetailFeed.class);
            intent.putExtra("ENTRY", entry);
            Log.i("TAG", " Title : " + entry.getTitle() + "\n Content : " + Html.fromHtml(entry.getContentSnippet().replace("...", "").replace("[…]", "")));
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(entry.getTitle())
                            .setContentIntent(pIntent)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentText(Html.fromHtml(entry.getContentSnippet().replace("...", "").replace("[…]", "")))
                            .setAutoCancel(true);
            int NOTIFICATION_ID = 12345;
            NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.notify(NOTIFICATION_ID, builder.build());
/*TODO
 * when notification is clicked make back button clicked on DetailFeeds to go in MainActivity loading all from start data */

        }


    }
}
