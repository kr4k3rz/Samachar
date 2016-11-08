package com.codelite.kr4k3rz.samachar.ui.settings;


import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.codelite.kr4k3rz.samachar.BuildConfig;
import com.codelite.kr4k3rz.samachar.MainActivity;
import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.feed.EntriesItem;
import com.codelite.kr4k3rz.samachar.ui.activity.SplashActivity;
import com.codelite.kr4k3rz.samachar.util.SnackMsg;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

import static android.content.Context.ACTIVITY_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFrag extends Fragment {
    private final String TAG = SettingsFrag.class.getSimpleName();
    private Switch switch_notifications;
    private Switch switch_downloadImage;
    private Switch switch_nightMode;
    private CoordinatorLayout coordinatorLayout;


    public SettingsFrag() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_setting);
        saveArticles(view);
        showNewspapersList(view);
        changeLanguage(view);
        setNotifications(view, coordinatorLayout);
        setDownloadImage(view);
        switchNightMode(view);
        clearCache(view);
        rateUsOnPlaystore(view);
        shareThisApp(view);
        sendFeedback(view);
        return view;
    }

    private void sendFeedback(View view) {
        LinearLayout ll_feedback = (LinearLayout) view.findViewById(R.id.ll_feedback);
        ll_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for " + R.string.app_name + " " + BuildConfig.VERSION_NAME);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"xitize@gmailcom"});
                intent.putExtra(Intent.EXTRA_TEXT, "\n\n\nBelow information is required tp address your issue in more detail. Please do not delete/modify this information.\n App Version - " + BuildConfig.VERSION_NAME + "\n Device Model - " + Build.MODEL + "\n OS Version - " + Build.VERSION.RELEASE);
                Intent mailer = Intent.createChooser(intent, "Send through...");
                startActivity(mailer);
            }
        });
    }

    private void shareThisApp(View view) {
        LinearLayout ll_shareThisApp = (LinearLayout) view.findViewById(R.id.ll_shareThisApp);
        ll_shareThisApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out " + R.string.app_name + " app for Android");
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hi, I am using " + R.string.app_name + " Android app." + "Why don't you check it out on your Android Phone at: https://play.google.com/store/apps/details?id=" + getContext().getPackageName());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share post via"));
            }

        });
    }

    private void rateUsOnPlaystore(View view) {
        LinearLayout ll_rateUsOnPlayStore = (LinearLayout) view.findViewById(R.id.ll_rateUsOnPlaySote);
        ll_rateUsOnPlayStore.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
                }
            }
        });
    }

    private void clearCache(View view) {
        LinearLayout ll_clearCache = (LinearLayout) view.findViewById(R.id.ll_clearCache);
        ll_clearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Delete cache story and images");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {


                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((ActivityManager) getContext().getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!

                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    private void switchNightMode(View view) {
        switch_nightMode = (Switch) view.findViewById(R.id.switch_nightMode);
        if (Paper.book().exist("NightMode")) {
            boolean bb = Paper.book().read("NightMode");
            Log.i(TAG, "NightMode : " + bb);
            switch_nightMode.setChecked(bb);
        }
        switch_nightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Paper.book().write("NightMode", true);
                    switch_nightMode.setChecked(true);
                    Intent intent = new Intent(getContext(), SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Paper.book().write("NightMode", false);
                    switch_nightMode.setChecked(false);
                    Intent intent = new Intent(getContext(), SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    private void setDownloadImage(View view) {
        switch_downloadImage = (Switch) view.findViewById(R.id.switch_downloadImage);
        if (Paper.book().exist("Image")) {
            boolean bb = Paper.book().read("Image");
            Log.i(TAG, "image : " + bb);
            switch_downloadImage.setChecked(bb);
        }
        switch_downloadImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Paper.book().write("Image", true);
                    switch_downloadImage.setChecked(true);
                    SnackMsg.showMsgShort(coordinatorLayout, "image on");

                } else {
                    Paper.book().write("Image", false);
                    switch_downloadImage.setChecked(false);
                    SnackMsg.showMsgShort(coordinatorLayout, "image off");

                }
            }
        });
    }

    private void setNotifications(final View view, final CoordinatorLayout coordinatorLayout) {
        switch_notifications = (Switch) view.findViewById(R.id.switch_notifications);
        if (Paper.book().exist("notifications")) {
            boolean bb = Paper.book().read("notifications");
            switch_notifications.setChecked(bb);
        }
        switch_notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Paper.book().write("notifications", true);
                    switch_notifications.setChecked(true);
                    SnackMsg.showMsgShort(coordinatorLayout, "notification on");

                } else {
                    Paper.book().write("notifications", false);
                    switch_notifications.setChecked(false);
                    SnackMsg.showMsgShort(coordinatorLayout, "notification off");


                }
            }
        });
    }

    private void changeLanguage(View view) {
        LinearLayout ll_language = (LinearLayout) view.findViewById(R.id.ll_language);
        TextView textView_language = (TextView) view.findViewById(R.id.language_textview);
        String lang = null;
        if (Paper.book().exist("language")) {
            lang = Paper.book().read("language");
        }
        assert lang != null;
        switch (lang) {
            case "EN":
                textView_language.setText("English");
                break;
            case "NP":
                textView_language.setText("नेपाली");
                break;
        }
        ll_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("FLAG", true);
                startActivity(intent);
            }
        });
    }

    private void showNewspapersList(View view) {
        LinearLayout ll_newspapers_list = (LinearLayout) view.findViewById(R.id.ll_newspapers_list);
        ll_newspapers_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewspapersActivity.class));
            }
        });
    }

    private void saveArticles(View view) {
        LinearLayout ll_saved_articles = (LinearLayout) view.findViewById(R.id.ll_saved_articles);
        TextView saved_articles_numbers = (TextView) view.findViewById(R.id.saved_articles_number);
        List<EntriesItem> list = Paper.book().read("BookMark");
        int num;
        if (list == null) {
            num = 0;
        } else {
            num = list.size();
        }
        saved_articles_numbers.setText(String.format(Locale.ENGLISH, "%d", num));

        ll_saved_articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SavedArticle.class));
            }
        });
    }

}
