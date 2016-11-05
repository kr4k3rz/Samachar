package com.codelite.kr4k3rz.samachar.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.Header;
import com.codelite.kr4k3rz.samachar.model.feed.EntriesItem;
import com.codelite.kr4k3rz.samachar.ui.activity.CategoryActivity;
import com.codelite.kr4k3rz.samachar.ui.activity.DetailFeed;
import com.codelite.kr4k3rz.samachar.util.Parse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import io.paperdb.Paper;


public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int HEADER = 0, ENTRY = 1;
    private final Context context;
    private final List<Object> items;


    public ComplexRecyclerViewAdapter(Context context, List<Object> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case HEADER:
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

        // Here you apply the animation when the view is bound
        // setAnimation(holder.itemView, position);
        switch (holder.getItemViewType()) {
            case HEADER:
                ViewHolder1 vh1 = (ViewHolder1) holder;
                configureViewHolder1(vh1, position);
                break;
            case ENTRY:
                ViewHolder2 vh2 = (ViewHolder2) holder;
                try {
                    configureViewHolder2(vh2, position);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }


    }

    private void configureViewHolder2(final ViewHolder2 vh2, final int position) throws MalformedURLException {
        final EntriesItem entry = (EntriesItem) items.get(position);
        /*Bind only data here*/
        String actualUrl;
        String url = Parse.parseImg(entry.getContent());
        actualUrl = convertImgUrl(url);
        vh2.title.setText(entry.getTitle());

        try {
            vh2.source.setText(Parse.capitalize(String.format("%s", Parse.getSource(entry.getLink()))));
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        displayDate(vh2, entry);

        displayImage(vh2, entry, actualUrl);


    }

    private void displayDate(ViewHolder2 vh2, EntriesItem entry) {
        vh2.date.setText(String.format("%s", Parse.convertLongDateToAgoString(Date.parse(entry.getPublishedDate()), System.currentTimeMillis()))); //to set date time in '3 minutes ago' like
        vh2.date.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    private void displayImage(ViewHolder2 vh2, final EntriesItem entry, String actualUrl) {
        boolean enableImage = true;
        if (Paper.book().exist("Image")) {
            enableImage = Paper.book().read("Image");
        }
        if (enableImage) {
            Glide.with(context)
                    .load(actualUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(vh2.imageView);
        }
        vh2.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailFeed.class);
                intent.putExtra("ENTRY", entry);
                context.startActivity(intent);


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


    private void configureViewHolder1(final ViewHolder1 vh1, int position) {

        final Header header = (Header) items.get(position);
        vh1.tv_category_name.setText(header.getFirstName());
        vh1.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("HEADER", header);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        else return items.size();

    }


    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Header) {
            return HEADER;
        } else if (items.get(position) instanceof EntriesItem) {
            return ENTRY;
        }
        return -1;
    }


    private class ViewHolder1 extends RecyclerView.ViewHolder {

        final TextView tv_category_name;
        final LinearLayout linearLayout;

        ViewHolder1(View itemView) {
            super(itemView);
            tv_category_name = (TextView) itemView.findViewById(R.id.category_name);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.ll_item);
        }

    }

    private class ViewHolder2 extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView date;
        final CardView cardView;
        final ImageView imageView;
        final TextView source;

        ViewHolder2(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            source = (TextView) itemView.findViewById(R.id.source);
            cardView = (CardView) itemView.findViewById(R.id.cardView);

        }

    }


}
