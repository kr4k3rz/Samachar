package com.codelite.kr4k3rz.samachar.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.Subscribe;
import com.codelite.kr4k3rz.samachar.model.feed.ResponseFeed;
import com.codelite.kr4k3rz.samachar.model.search.EntriesItem;
import com.codelite.kr4k3rz.samachar.util.Parse;
import com.codelite.kr4k3rz.samachar.util.SnackMsg;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.codelite.kr4k3rz.samachar.util.Parse.convertImgUrl;

public class SubscribeActivity extends AppCompatActivity {
    private final String TAG = SubscribeActivity.class.getSimpleName();
    private final SparseBooleanArray selectedItems = new SparseBooleanArray();
    private ProgressBar progressBar;
    private List<com.codelite.kr4k3rz.samachar.model.feed.EntriesItem> entriesItems;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_subscribe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_subscribe);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_subscribe);
        final EntriesItem entriesItem = (EntriesItem) getIntent().getSerializableExtra("QUERY");
        getSupportActionBar().setTitle(Html.fromHtml(entriesItem.getTitle()));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_subscribe);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        String url = entriesItem.getUrl();
        Log.i(TAG, "" + url);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=" + url + "&num=-1").build();
        progressBar.setVisibility(View.VISIBLE);
        okHttpClient.newCall(request).enqueue(new Callback() {
            final Handler handler = new Handler(SubscribeActivity.this.getMainLooper());

            @Override
            public void onFailure(Call call, IOException e) {
                SnackMsg.showMsgShort(recyclerView, e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                final ResponseFeed responseFeed = new Gson().fromJson(jsonStr, ResponseFeed.class);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (responseFeed.getResponseStatus() == 200 && responseFeed.getResponseData().getFeed().getEntries().size() != 0) {
                            entriesItems = responseFeed.getResponseData().getFeed().getEntries();
                            recyclerView.setAdapter(new SubscribeAdapter(getApplicationContext(), entriesItems));
                            progressBar.setVisibility(View.GONE);

                        } else {
                            if (responseFeed.getResponseData().getFeed().getEntries().size() == 0)
                                SnackMsg.showMsgLong(toolbar, "No Feeds");
                            else
                                SnackMsg.showMsgLong(toolbar, "Error: " + responseFeed.getResponseDetails().toString());
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });


            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SnackMsg.showMsgShort(toolbar, "subscribed");
                Subscribe subscribe = new Subscribe();
                subscribe.setEntriesItem(entriesItem);
                subscribe.setEntriesItemList(entriesItems);
                Intent intent = new Intent();
                intent.putExtra("SubscribeItem", subscribe);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.MyViewHolder> {
        final Context context;
        final List<com.codelite.kr4k3rz.samachar.model.feed.EntriesItem> entriesItem;

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
                //  holder.contentSnippet.setTextColor(Color.LTGRAY);
            } else {
                //if not selected set the default color
                holder.title.setTextColor(ContextCompat.getColor(context, R.color.primary_text));
                //  holder.contentSnippet.setTextColor(ContextCompat.getColor(context, R.color.secondary_text));
            }

            String actualUrl = null;
            String url = Parse.parseImg(entry.getContent());
            try {
                actualUrl = convertImgUrl(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            holder.title.setText(entry.getTitle());
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

            MyViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                date = (TextView) itemView.findViewById(R.id.date);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
                source = (TextView) itemView.findViewById(R.id.source);
                cardView = (CardView) itemView.findViewById(R.id.cardView);
            }
        }
    }
}
