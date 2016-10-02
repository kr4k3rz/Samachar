package com.codelite.kr4k3rz.samachar.adapter;

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

/**
 * Created by kr4k3rz on 10/1/16.
 */

public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int CATEGORY_NAME = 0, ENTRY = 1;
    private String TAG = ComplexRecyclerViewAdapter.class.getSimpleName();
    private Context context;
    private List<Object> items;

    public ComplexRecyclerViewAdapter(Context context, List<Object> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case CATEGORY_NAME:
                View v1 = inflater.inflate(R.layout.category_name, parent, false);

                viewHolder = new ViewHolder1(v1);
                break;
            case ENTRY:
                View v2 = inflater.inflate(R.layout.item_cardview, parent, false);
                viewHolder = new ViewHolder2(v2);
                break;
            default:
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case CATEGORY_NAME:
                ViewHolder1 vh1 = (ViewHolder1) holder;
                configureViewHolder1(vh1, position);
                break;
            case ENTRY:
                ViewHolder2 vh2 = (ViewHolder2) holder;
                configureViewHolder2(vh2, position);
                break;
            default:
                break;
        }

    }

    private void configureViewHolder2(final ViewHolder2 vh2, final int position) {
        Entry entry = (Entry) items.get(position);

        /*Bind only data here*/
        String url = Parse.parseImg(entry.getContent());
        vh2.title.setText(entry.getTitle());
        vh2.contentSnippet.setText(Html.fromHtml(entry.getContentSnippet().replace("...", "")).toString());
        String s = String.valueOf(DateUtils.getRelativeTimeSpanString(Date.parse(entry.getDate()),
                System.currentTimeMillis(), DateUtils.FORMAT_ABBREV_ALL));
        try {
            vh2.source.setText(String.format("%s", Parse.getSource(entry.getLink())));
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        vh2.date.setText(String.format("%s", s)); //to set date time in '3 minutes ago' like
        boolean enableImage = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("enableImage", true);
        if (enableImage) {
            Glide.with(context)
                    .load(url)
                    .error(R.drawable.ic_newspaper)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.placeholderimg)
                    .into(vh2.imageView);
        }
        vh2.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailFeed.class);
                intent.putExtra("title", ((Entry) items.get(position)).getTitle());
                intent.putExtra("date", ((Entry) items.get(position)).getDate());
                intent.putExtra("content", ((Entry) items.get(position)).getContent().replace("...", "").replace("[…]", ""));
                intent.putExtra("author", ((Entry) items.get(position)).getAuthor());
                intent.putExtra("link", ((Entry) items.get(position)).getLink());
                intent.putStringArrayListExtra("categories", (ArrayList<String>) ((Entry) items.get(position)).getCategories());
                context.startActivity(intent);

            }
        });


    }

    private void configureViewHolder1(ViewHolder1 vh1, int position) {
        String s = (String) items.get(position);
        vh1.textView.setText(s);

    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        else return items.size();

    }


    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof String) {
            return CATEGORY_NAME;
        } else if (items.get(position) instanceof Entry) {
            return ENTRY;
        }
        return -1;
    }


    private class ViewHolder1 extends RecyclerView.ViewHolder {

        TextView textView;

        ViewHolder1(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text1);
        }

    }

    private class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView title;
        final TextView date;
        final CardView cardView;
        final ImageView imageView;
        final TextView source;
        final TextView contentSnippet;

        ViewHolder2(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            contentSnippet = (TextView) itemView.findViewById(R.id.content_snippet);
            date = (TextView) itemView.findViewById(R.id.date);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            source = (TextView) itemView.findViewById(R.id.source);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

        }
    }


}
