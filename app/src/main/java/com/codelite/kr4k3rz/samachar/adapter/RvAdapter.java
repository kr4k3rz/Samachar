package com.codelite.kr4k3rz.samachar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.activity.DetailFeed;
import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.util.Parse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RvAdapter extends RecyclerView.Adapter<RvAdapter.CustomViewHolder> {
    private final Context mContext;
    private List<Entry> entries = new ArrayList<>();

    public RvAdapter(Context context, List<Entry> entries) {
        this.entries = entries;
        mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardview, viewGroup, false);
        return new CustomViewHolder(itemView);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, final int _position) {

        /*Bind only data here*/
        String actualUrl = null;
        final Entry entry = entries.get(customViewHolder.getAdapterPosition());
        String url = Parse.parseImg(entry.getContent());

        try {
            actualUrl = convertImgUrl(actualUrl, url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        customViewHolder.title.setText(entry.getTitle());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            customViewHolder.contentSnippet.setText(Html.fromHtml(entry.getContentSnippet().replace("...", ""), Html.FROM_HTML_MODE_LEGACY).toString());
        } else {
            customViewHolder.contentSnippet.setText(Html.fromHtml(entry.getContentSnippet().replace("...", "")).toString());
        }

        String s = String.valueOf(DateUtils.getRelativeTimeSpanString(Date.parse(entry.getDate()),
                System.currentTimeMillis(), DateUtils.FORMAT_ABBREV_ALL));
        try {
            customViewHolder.source.setText(String.format("%s", Parse.getSource(entry.getLink())));
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        customViewHolder.date.setText(String.format("%s", s)); //to set date time in '3 minutes ago' like
        boolean enableImage = PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("enableImage", true);
        if (enableImage) {
            Glide.with(mContext)
                    .load(actualUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(customViewHolder.imageView);
        } else {
            Log.d("TAG", "Image disabled : " + enableImage);
        }
        customViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailFeed.class);
                intent.putExtra("ENTRY", entry);
                mContext.startActivity(intent);
            }
        });

    }

    private String convertImgUrl(String actualUrl, String url) throws MalformedURLException {
        if (url != null && url.startsWith("http://")) {
            if (url.toLowerCase().contains(".png".toLowerCase())) {
                URL url1 = new URL(url);
                String tempUrl = url1.getHost() + ".rsz.io" + url1.getPath() + "?format=jpg";
                actualUrl = "http://images.weserv.nl/?url=" + tempUrl + "&w=300&h=300&q=10";
                Log.i("PNG TAG", "" + actualUrl);
            } else {
                actualUrl = "http://images.weserv.nl/?url=" + url.replace("http://", "") + "&w=300&h=300&q=10";
                Log.i("TAG", " String to be shows : " + actualUrl);
            }
        } else Log.i("TAG", " String is null");
        return actualUrl;
    }


    @Override
    public int getItemCount() {
        if (entries != null)
            return entries.size();
        else return 0;
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView date;
        final CardView cardView;
        final ImageView imageView;
        final TextView source;
        final TextView contentSnippet;

        CustomViewHolder(View itemView) {
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

