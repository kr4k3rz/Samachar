package com.codelite.kr4k3rz.samachar.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.Category;

import java.util.ArrayList;

import static android.view.View.GONE;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private static final String TAG = "Custom Adapter";
    private final Context context;
    private ArrayList<Category> categories = new ArrayList<>();

    public CustomAdapter(Context baseContext, ArrayList<Category> categories) {
        this.context = baseContext;
        this.categories = categories;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CustomViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        final Category category = categories.get(position);
        Log.i(TAG, "" + category.getName_category());
        holder.category_name.setText(category.getName_category());
        MyCustomAdapter adapter = new MyCustomAdapter(categories.get(position).getSub_category());
        holder.recyclerView.setAdapter(adapter);
        holder.cardView_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.recyclerView.getVisibility() == GONE) {
                    holder.recyclerView.setVisibility(View.VISIBLE);
                } else {
                    holder.recyclerView.setVisibility(GONE);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        if (categories != null)
            return categories.size();
        else return 0;
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView category_name;
        final CardView cardView_category;
        final RecyclerView recyclerView;

        CustomViewHolder(View itemView) {
            super(itemView);
            category_name = (TextView) itemView.findViewById(R.id.textView_category);
            cardView_category = (CardView) itemView.findViewById(R.id.cardview_category);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_sub_category);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(context);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
        }


    }
}
