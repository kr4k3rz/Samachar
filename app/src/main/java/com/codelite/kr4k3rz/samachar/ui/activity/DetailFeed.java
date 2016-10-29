package com.codelite.kr4k3rz.samachar.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.util.Parse;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.apmem.tools.layouts.FlowLayout;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;


/*
* shows the details of the  every feeds clicked*/
public class DetailFeed extends AppCompatActivity {
    private static final String TAG = DetailFeed.class.getSimpleName();
    private DiscreteSeekBar discreteSeekBar1;
    private Entry entry;
    private boolean checked = false;
    private TextView content;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        discreteSeekBar1 = (DiscreteSeekBar) findViewById(R.id.discrete1);
        discreteSeekBar1.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                return value;
            }
        });
        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flowLayout);
        TextView title = (TextView) findViewById(R.id.title_detail);
        TextView date = (TextView) findViewById(R.id.date_detail);
        content = (TextView) findViewById(R.id.content_detail);
        ImageView imageView = (ImageView) findViewById(R.id.detailImageView);
        CardView cardView = (CardView) findViewById(R.id.myCardview);
        TextView author = (TextView) findViewById(R.id.authorTv);

        entry = (Entry) getIntent().getSerializableExtra("ENTRY");

        Log.d("DETAIL FEEDS", "Entry : " + entry.getTitle());
        content.setTextSize(getSharedPreferences("setting", MODE_PRIVATE).getFloat("textsize", 16));
        title.setText(entry.getTitle());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM ''yy, HH:mm ", Locale.ENGLISH);
        date.setText(simpleDateFormat.format(Date.parse(entry.getDate())));
        Log.i(TAG, entry.getContent());
       /*use to remove the TAG */
        String check = "<p>The post <a rel=\"nofollow\" href=\"" + entry.getLink() + "\">" + entry.getTitle() + "</a> appeared first on <a rel=\"nofollow\" href=\"" + entry.getLinkFeed() + "\">" + entry.getTitleFeed() + "</a>.</p>";
        Log.i(TAG, "" + check);
        String contentHtml = entry.getContent().replace(check, "");
        content.setText(Html.fromHtml(contentHtml, Parse.EMPTY_IMAGE_GETTER, null));

        author.setText(entry.getAuthor());
        String url = Parse.parseImg(entry.getContent());
        String actualUrl = null;
        try {
            actualUrl = convertImgUrl(null, url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        boolean enableImage = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean("enableImage", true);
        if (enableImage) {
            cardView.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext())
                    .load(actualUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        }

        ArrayList<String> fetchList;
        fetchList = (ArrayList<String>) entry.getCategories();
        if (flowLayout != null) {
            if (!fetchList.isEmpty()) {
                Log.e("TEXT", "setTags: " + flowLayout.getChildCount());
                flowLayout.removeAllViews();
                for (String s : fetchList) {
                    FlowLayout.LayoutParams lparams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
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

    private String convertImgUrl(String actualUrl, String url) throws MalformedURLException {
        if (url != null && url.startsWith("http://")) {
            if (url.toLowerCase().contains(".png".toLowerCase())) {
                URL url1 = new URL(url);
                String tempUrl = url1.getHost() + ".rsz.io" + url1.getPath() + "?format=jpg";
                actualUrl = "http://images.weserv.nl/?url=" + tempUrl + "&w=300&h=300&q=50";
                Log.i("PNG TAG", "" + actualUrl);
            } else {
                actualUrl = "http://images.weserv.nl/?url=" + url.replace("http://", "") + "&w=300&h=300&q=50";
                Log.i("TAG", " String to be shows : " + actualUrl);
            }
        } else Log.i("TAG", " String is null");
        return actualUrl;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.action_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getIntent().getExtras().getString("title"));
                shareIntent.putExtra(Intent.EXTRA_TEXT, getIntent().getExtras().getString("link"));
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Share this article with..."));
                return true;
            case R.id.action_textSize:
                if (discreteSeekBar1.getVisibility() == View.VISIBLE) {
                    discreteSeekBar1.setVisibility(View.GONE);
                    break;
                }
                discreteSeekBar1.setVisibility(View.VISIBLE);
                discreteSeekBar1.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
                    float _value;

                    @Override
                    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                        content.setTextSize(value);
                        _value = value;
                    }

                    @Override
                    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                        _value = getSharedPreferences("setting", MODE_PRIVATE).getFloat("textsize", 12);
                        seekBar.setProgress((int) _value);

                    }

                    @Override
                    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                        seekBar.setVisibility(View.GONE);
                        SharedPreferences preferences = getSharedPreferences("setting", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putFloat("textsize", _value);
                        editor.apply();
                    }
                });
                return true;
            case R.id.action_bookmark:
                if (item.isChecked()) {
                    //  ToastMsg.shortMsg(getBaseContext(), "inside checked");
                    item.setIcon(R.drawable.ic_star_selected);
                    if (Paper.book().exist("BookMark")) {
                        List<Entry> entries;
                        entries = Paper.book().read("BookMark");
                        entries.add(entry);
                        Paper.book().write("BookMark", entries);
                    } else {
                        List<Entry> entries = new ArrayList<>();
                        entries.clear();
                        entries.add(entry);
                        Paper.book().write("BookMark", entries);
                    }
                    checked = true;
                    item.setChecked(false);
                } else {
                    //  ToastMsg.shortMsg(getBaseContext(), "inside Unchecked");
                    item.setIcon(R.drawable.ic_star_unselected);
                    item.setChecked(true);
                    if (Paper.book().exist("BookMark")) {
                        List<Entry> entries;
                        entries = Paper.book().read("BookMark");
                        entries.remove(entry);
                        Paper.book().write("BookMark", entries);
                    }
                    checked = false;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("RESULT", checked);
        setResult(Activity.RESULT_OK, intent);
    }
}
