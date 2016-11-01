package com.codelite.kr4k3rz.samachar.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.feed.ResponseFeed;
import com.codelite.kr4k3rz.samachar.model.search.EntriesItem;
import com.codelite.kr4k3rz.samachar.ui.adapter.SimpleDividerItemDecoration;
import com.codelite.kr4k3rz.samachar.util.Parse;
import com.codelite.kr4k3rz.samachar.util.SnackMsg;
import com.codelite.kr4k3rz.samachar.util.ToastMsg;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SubscribeActivity extends AppCompatActivity {
    private final SparseBooleanArray selectedItems = new SparseBooleanArray();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_subscribe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EntriesItem entriesItem = (EntriesItem) getIntent().getSerializableExtra("QUERY");
        getSupportActionBar().setTitle(entriesItem.getTitle());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_subscribe);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getBaseContext()));
        String url = entriesItem.getUrl();
        ToastMsg.shortMsg(getBaseContext(), "" + url);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=" + url + "&num=-1").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            Handler handler = new Handler(SubscribeActivity.this.getMainLooper());

            @Override
            public void onFailure(Call call, IOException e) {
                SnackMsg.showMsgShort(recyclerView, e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                ResponseFeed responseFeed = new Gson().fromJson(jsonStr, ResponseFeed.class);
                final List<com.codelite.kr4k3rz.samachar.model.feed.EntriesItem> entriesItems = responseFeed.getResponseData().getFeed().getEntries();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(new SubscribeAdapter(getBaseContext(), entriesItems));
                    }
                });

            }
        });


    }

    private String convertImgUrl(String url) throws MalformedURLException {
        if (url != null && url.startsWith("http://")) {
            if (url.toLowerCase().contains(".png".toLowerCase())) {
                URL url1 = new URL(url);
                String tempUrl = url1.getHost() + ".rsz.io" + url1.getPath() + "?format=jpg";
                url = "http://images.weserv.nl/?url=" + tempUrl + "&w=300&h=300&q=10";
                Log.i("PNG TAG", "" + url);
            } else {
                url = "http://images.weserv.nl/?url=" + url.replace("http://", "") + "&w=300&h=300&q=10";
                Log.i("TAG", " String to be shows : " + url);
            }
        } else Log.i("TAG", " String is null");
        return url;
    }

    private class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.MyViewHolder> {
        Context context;
        List<com.codelite.kr4k3rz.samachar.model.feed.EntriesItem> entriesItem;

        SubscribeAdapter(Context context, List<com.codelite.kr4k3rz.samachar.model.feed.EntriesItem> entriesItemList) {
            this.context = context;
            this.entriesItem = entriesItemList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final com.codelite.kr4k3rz.samachar.model.feed.EntriesItem entry = entriesItem.get(position);
            holder.title.setText(entry.getTitle());

            if (selectedItems.get(holder.getAdapterPosition())) {
                //if already selected set the same color
                holder.title.setTextColor(Color.LTGRAY);
                holder.contentSnippet.setTextColor(Color.LTGRAY);
            } else {
                //if not selected set the default color
                holder.title.setTextColor(ContextCompat.getColor(context, R.color.primary_text));
                holder.contentSnippet.setTextColor(ContextCompat.getColor(context, R.color.secondary_text));
            }

            String actualUrl = null;
            String url = Parse.parseImg(entry.getContent());
            try {
                actualUrl = convertImgUrl(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            holder.title.setText(entry.getTitle());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holder.contentSnippet.setText(Html.fromHtml(entry.getContentSnippet().replace("...", ""), Html.FROM_HTML_MODE_LEGACY).toString());
            } else {
                holder.contentSnippet.setText(Html.fromHtml(entry.getContentSnippet().replace("...", "")).toString());
            }

            try {
                holder.source.setText(String.format("%s", Parse.getSource(entry.getLink())));
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }

            holder.date.setText(String.format("%s", Parse.convertLongDateToAgoString(Date.parse(entry.getPublishedDate()), System.currentTimeMillis()))); //to set date time in '3 minutes ago' like
            holder.date.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            boolean enableImage = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("enableImage", true);

            if (enableImage) {
                Glide.with(context)
                        .load(actualUrl)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(holder.imageView);
            } else {
                Log.d("TAG", "Image disabled : " + false);
            }
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.title.setTextColor(Color.LTGRAY);
                    holder.contentSnippet.setTextColor(Color.LTGRAY);
                    selectedItems.put(holder.getAdapterPosition(), true);
                    Intent intent = new Intent(context, DetailFeed.class);
                    intent.putExtra("ENTRY", entry);
                    startActivity(intent);

                }
            });


        }

        @Override
        public int getItemCount() {
            return entriesItem.size();
        }


        class MyViewHolder extends RecyclerView.ViewHolder {
            final TextView title;
            final TextView date;
            final CardView cardView;
            final ImageView imageView;
            final TextView source;
            final TextView contentSnippet;

            MyViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                contentSnippet = (TextView) itemView.findViewById(R.id.content_snippet);
                date = (TextView) itemView.findViewById(R.id.date);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
                source = (TextView) itemView.findViewById(R.id.source);
                cardView = (CardView) itemView.findViewById(R.id.cardView);
            }
        }
    }


}
