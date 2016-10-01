package com.codelite.kr4k3rz.samachar.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.util.Parse;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.Date;


/*
* shows the details of the  every feeds clicked*/
public class DetailFeed extends AppCompatActivity {
    private TextView content;
    private SeekBar seekBar;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flowLayout);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        TextView title = (TextView) findViewById(R.id.title_detail);
        TextView date = (TextView) findViewById(R.id.date_detail);
        content = (TextView) findViewById(R.id.content_detail);
        ImageView imageView = (ImageView) findViewById(R.id.detailImageView);
        CardView cardView = (CardView) findViewById(R.id.myCardview);
        TextView author = (TextView) findViewById(R.id.authorTv);
        content.setTextSize(getSharedPreferences("setting", MODE_PRIVATE).getFloat("textsize", 16));
        title.setText(getIntent().getExtras().getString("title"));

        date.setText(" " + DateUtils.getRelativeTimeSpanString(Date.parse(getIntent().getExtras().getString("date")),
                System.currentTimeMillis(), DateUtils.FORMAT_ABBREV_RELATIVE));


        content.setText(Html.fromHtml(getIntent().getExtras().getString("content"), Parse.EMPTY_IMAGE_GETTER, null));
        author.setText(getIntent().getExtras().getString("author"));
        boolean enableImage = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean("enableImage", true);
        //   Log.i("TAG", "enableImage: " + enableImage);
        if (enableImage) {
            cardView.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext())
                    .load(Parse.parseImg(getIntent()
                            .getExtras().getString("content")))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        }

        ArrayList<String> fetchList;
        fetchList = getIntent().getStringArrayListExtra("categories");

        if (flowLayout != null) {
            if (!fetchList.isEmpty()) {
                Log.e("TEXT", "setTags: " + flowLayout.getChildCount());
                flowLayout.removeAllViews();
                for (String s : fetchList) {
                    FlowLayout.LayoutParams lparams = new FlowLayout.LayoutParams(org.apmem.tools.layouts.FlowLayout.LayoutParams.WRAP_CONTENT, org.apmem.tools.layouts.FlowLayout.LayoutParams.WRAP_CONTENT);
                    lparams.setMargins(5, 5, 5, 5);
                    @SuppressLint("InflateParams") TextView rowTextView = (TextView) getLayoutInflater().inflate(R.layout.custom_tag_textview, null);
                    rowTextView.setText(s);
                    rowTextView.setLayoutParams(lparams);
                    flowLayout.addView(rowTextView);
                }
            }
            Log.e("TAG", "setTags: after " + flowLayout.getChildCount());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getIntent().getExtras().getString("title"));
                shareIntent.putExtra(Intent.EXTRA_TEXT, getIntent().getExtras().getString("link"));
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Share this article with..."));

                break;
            case R.id.action_textSize:
                if (seekBar.getVisibility() == View.VISIBLE) {
                    seekBar.setVisibility(View.GONE);
                    break;
                }
                seekBar.setVisibility(View.VISIBLE);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    float value;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        content.setTextSize(i);
                        value = i;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        seekBar.setProgress((int) value);
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        seekBar.setVisibility(View.GONE);
                        SharedPreferences preferences = getSharedPreferences("setting", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putFloat("textsize", value);
                        editor.apply();
                    }

                });
                break;
        }
        return super.onOptionsItemSelected(item);
        /*seekbar issue TODO
        * 1. size increase*/

    }
}
