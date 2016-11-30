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
import com.codelite.kr4k3rz.samachar.model.NewspaperNP;
import com.codelite.kr4k3rz.samachar.model.feed.EntriesItem;
import com.codelite.kr4k3rz.samachar.model.feed.ResponseFeed;
import com.codelite.kr4k3rz.samachar.ui.activity.DetailFeed;
import com.codelite.kr4k3rz.samachar.util.CheckInternet;
import com.codelite.kr4k3rz.samachar.util.Parse;
import com.codelite.kr4k3rz.samachar.util.PullData;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.paperdb.Paper;


public class MyIntentService extends IntentService {

    /*
    * download all newsFeeds show only the BreakingFrag news
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
            try {
                updateDataNotify();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private void updateDataNotify() throws IOException {
        ArrayList<String> rss = new NewspaperNP().getLinksList();
        List<EntriesItem> list = new ArrayList<>();

        for (String rs : rss) {
            if (!rs.equalsIgnoreCase("")) {
                String jsonStr = new PullData().run(rs);    //loads JSON String feeds by Okhttp
                Log.i("TAG", ":>  " + jsonStr);
                ResponseFeed responseFeed = new Gson().fromJson(jsonStr, ResponseFeed.class);
                List<EntriesItem> posts = responseFeed.getResponseData().getFeed().getEntries();
                list.addAll(posts);
            }

        }


        if (list.size() != 0) {

            List<EntriesItem> oldFeeds = Paper.book().read("BreakingFrag");
            if (oldFeeds != null) {
                list.addAll(oldFeeds);
            }
            List<EntriesItem> newFeeds;
            newFeeds = Parse.deleteDuplicate(list); //delete duplicate
            newFeeds = Parse.deleteEnglishFeeds(newFeeds);  //delete english feed if present
            Parse.sortByTime(newFeeds);
            Paper.book().write("BreakingFrag", newFeeds);
            EntriesItem entry;
            Random random = new Random();
            int random_num = random.nextInt(5);
            entry = newFeeds.get(random_num);
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
