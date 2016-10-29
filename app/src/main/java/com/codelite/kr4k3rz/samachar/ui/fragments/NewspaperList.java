package com.codelite.kr4k3rz.samachar.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.Newspaper;
import com.codelite.kr4k3rz.samachar.ui.activity.NewspaperActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewspaperList extends Fragment {


    public NewspaperList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_newspaper_list, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_newspaper_list);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ArrayList<Newspaper> newspapers;
        Newspaper newspaper = new Newspaper();
        newspapers = newspaper.getNewspapersList();
        recyclerView.setAdapter(new NewsPaperAdapter(getContext(), newspapers));
        return rootView;
    }

    private class NewsPaperAdapter extends RecyclerView.Adapter<MyViewHolder> {
        Context context;
        ArrayList<Newspaper> newspapers;

        NewsPaperAdapter(Context context, ArrayList<Newspaper> newspapers) {
            this.context = context;
            this.newspapers = newspapers;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_newspaper, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final Newspaper n = newspapers.get(position);
            holder.newspaper_name.setText(n.name);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, NewspaperActivity.class);
                    intent.putExtra("NEWSPAPER_NAME", n.name);
                    intent.putExtra("POSITION", holder.getAdapterPosition());
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return newspapers.size();
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView newspaper_name;
        LinearLayout linearLayout;

        MyViewHolder(View itemView) {
            super(itemView);
            newspaper_name = (TextView) itemView.findViewById(R.id.newspaper_name);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.ll_newspaper);
        }
    }
}
