package com.codelite.kr4k3rz.samachar.ui.allnews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.feed.EntriesItem;
import com.codelite.kr4k3rz.samachar.ui.activity.DetailFeed;
import com.codelite.kr4k3rz.samachar.util.Parse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.paperdb.Paper;


public class RvAdapter extends RecyclerView.Adapter<RvAdapter.CustomViewHolder> {
    private final Context mContext;
    private final String TAG = RvAdapter.class.getSimpleName();
  //  private final SparseBooleanArray selectedItems = new SparseBooleanArray();
    private List<EntriesItem> entries = new ArrayList<>();

    public RvAdapter(Context context, List<EntriesItem> entries) {
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
    public void onBindViewHolder(final CustomViewHolder customViewHolder, @SuppressLint("RecyclerView") final int _position) {
        final EntriesItem entry = entries.get(customViewHolder.getAdapterPosition());

       /* if (selectedItems.get(customViewHolder.getAdapterPosition())) {
            //if already selected set the same color
            customViewHolder.title.setTextColor(Color.LTGRAY);
        } else {
            //if not selected set the default color
            customViewHolder.title.setTextColor();  //make here
        }*/

        String actualUrl = null;
        String url = Parse.parseImg(entry.getContent());
        try {
            actualUrl = convertImgUrl(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        customViewHolder.title.setText(entry.getTitle());
        try {
            customViewHolder.source.setText(Parse.capitalize(String.format("%s", Parse.getSource(entry.getLink()))));
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        customViewHolder.date.setText(String.format("%s", Parse.convertLongDateToAgoString(Date.parse(entry.getPublishedDate()), System.currentTimeMillis()))); //to set date time in '3 minutes ago' like
        customViewHolder.date.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


        boolean enableImage = true;
        if (Paper.book().exist("Image")) {
            enableImage = Paper.book().read("Image");
        }
        if (enableImage) {
            Glide.with(mContext)
                    .load(actualUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(customViewHolder.imageView);
        } else {
            Log.d("TAG", "Image disabled : " + false);
        }
        customViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  customViewHolder.title.setTextColor(Color.LTGRAY);
             //   selectedItems.put(customViewHolder.getAdapterPosition(), true);
                Log.i(TAG, "Inside on Click\n" + "_position :  " + _position + "\n getadapter position : " + customViewHolder.getAdapterPosition());
                Intent intent = new Intent(mContext, DetailFeed.class);
                intent.putExtra("ENTRY", entry);
                ((Activity) mContext).startActivityForResult(intent, 2);

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

        CustomViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            source = (TextView) itemView.findViewById(R.id.source);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }


    }


}

