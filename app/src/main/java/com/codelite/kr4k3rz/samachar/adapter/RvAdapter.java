package com.codelite.kr4k3rz.samachar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
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
    public void onBindViewHolder(final CustomViewHolder customViewHolder, final int position) {

        /*Bind only data here*/
        Entry e = entries.get(position);
        String url = Parse.parseImg(e.getContent());
        customViewHolder.title.setText(e.getTitle());
        customViewHolder.contentSnippet.setText(Html.fromHtml(e.getContentSnippet().replace("...", "")).toString());
        String s = String.valueOf(DateUtils.getRelativeTimeSpanString(Date.parse(e.getDate()),
                System.currentTimeMillis(), DateUtils.FORMAT_ABBREV_ALL));
        try {
            customViewHolder.source.setText(String.format("%s", Parse.getSource(e.getLink())));
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        customViewHolder.date.setText(String.format("%s", s)); //to set date time in '3 minutes ago' like
        boolean enableImage = PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("enableImage", true);
        if (enableImage) {
            Glide.with(mContext)
                    .load(url)
                    .error(R.drawable.ic_newspaper)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.placeholderimg)
                    .into(customViewHolder.imageView);
        }
        customViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailFeed.class);
                intent.putExtra("title", entries.get(position).getTitle());
                intent.putExtra("date", entries.get(position).getDate());
                intent.putExtra("content", entries.get(position).getContent().replace("...", "").replace("[…]", ""));
                intent.putExtra("author", entries.get(position).getAuthor());
                intent.putExtra("link", entries.get(position).getLink());
                intent.putStringArrayListExtra("categories", (ArrayList<String>) entries.get(position).getCategories());
                mContext.startActivity(intent);
            }
        });

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

