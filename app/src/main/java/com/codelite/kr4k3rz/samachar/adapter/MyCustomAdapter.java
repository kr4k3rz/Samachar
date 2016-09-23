package com.codelite.kr4k3rz.samachar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.SubCategory;

import java.util.ArrayList;


class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.CustomViewHolder> {
    private final ArrayList<SubCategory> subCategories;

    MyCustomAdapter(ArrayList<SubCategory> categories) {
        this.subCategories = categories;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        final SubCategory subCategory = subCategories.get(position);
        holder.textView_sub.setText(subCategory.getSub_name());
        holder.checkBox_sub.setChecked(subCategory.isChecked());
        holder.checkBox_sub.setOnCheckedChangeListener(null);
        holder.checkBox_sub.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    subCategory.setUpdated_sub_link_cached(subCategory.getSub_link_cached());
                    subCategory.setUpdated_sub_link_latest(subCategory.getSub_link_latest());
                    subCategory.setChecked(true);
                } else {
                    subCategory.setUpdated_sub_link_cached("");
                    subCategory.setUpdated_sub_link_latest("");
                    subCategory.setChecked(false);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        if (subCategories != null)
            return subCategories.size();
        else return 0;
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView textView_sub;
        final CheckBox checkBox_sub;

        CustomViewHolder(View itemView) {
            super(itemView);
            textView_sub = (TextView) itemView.findViewById(R.id.sub_category_itemName);
            checkBox_sub = (CheckBox) itemView.findViewById(R.id.sub_category_checkBox);

        }
    }

}
