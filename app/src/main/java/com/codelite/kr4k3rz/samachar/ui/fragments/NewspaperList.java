package com.codelite.kr4k3rz.samachar.ui.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.NewspaperEN;
import com.codelite.kr4k3rz.samachar.model.NewspaperNP;
import com.codelite.kr4k3rz.samachar.ui.activity.NewspaperActivity;
import com.codelite.kr4k3rz.samachar.ui.activity.SearchActivity;

import java.util.ArrayList;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewspaperList extends Fragment {
    FloatingActionButton floatingActionButton;

    public NewspaperList() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_newspaper_list, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_newspaper_list);
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fabBtn);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        String lang = Paper.book().read("language");
        switch (lang) {
            case "NP":
                ArrayList<NewspaperNP> newspapers;
                NewspaperNP newspaper = new NewspaperNP();
                newspapers = newspaper.getNewspapersList();
                recyclerView.setAdapter(new NewsPaperNPAdapter(getContext(), newspapers));
                break;
            case "EN":
                ArrayList<NewspaperEN> newspaperENss;
                NewspaperEN newspaperEN = new NewspaperEN();
                newspaperENss = newspaperEN.getNewspapersList();
                recyclerView.setAdapter(new NewsPaperENAdapter(getContext(), newspaperENss));
                break;

        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view1 = layoutInflater.inflate(R.layout.dialog_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(view1, 30, 30, 30, 30);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getContext(), SearchActivity.class));
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();


            }
        });
        return rootView;
    }


    private class NewsPaperNPAdapter extends RecyclerView.Adapter<NewsPaperNPAdapter.MyViewHolder> {
        final Context context;
        final ArrayList<NewspaperNP> newspapers;

        NewsPaperNPAdapter(Context context, ArrayList<NewspaperNP> newspapers) {
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
            final NewspaperNP n = newspapers.get(position);
            holder.newspaper_name.setText(n.name);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, NewspaperActivity.class);
                    intent.putExtra("NEWSPAPER_NAME", n.name);
                    intent.putExtra("POSITION", holder.getAdapterPosition());
                    intent.putExtra("LINK", n.link);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return newspapers.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            final TextView newspaper_name;
            final LinearLayout linearLayout;

            MyViewHolder(View itemView) {
                super(itemView);
                newspaper_name = (TextView) itemView.findViewById(R.id.newspaper_name);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.ll_newspaper);
            }
        }
    }

    private class NewsPaperENAdapter extends RecyclerView.Adapter<NewsPaperENAdapter.MyViewHolder> {
        final Context context;
        final ArrayList<NewspaperEN> newspapers;

        NewsPaperENAdapter(Context context, ArrayList<NewspaperEN> newspapers) {
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
            final NewspaperEN n = newspapers.get(position);
            holder.newspaper_name.setText(n.name);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, NewspaperActivity.class);
                    intent.putExtra("NEWSPAPER_NAME", n.name);
                    intent.putExtra("POSITION", holder.getAdapterPosition());
                    intent.putExtra("LINK", n.link);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return newspapers.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            final TextView newspaper_name;
            final LinearLayout linearLayout;

            MyViewHolder(View itemView) {
                super(itemView);
                newspaper_name = (TextView) itemView.findViewById(R.id.newspaper_name);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.ll_newspaper);
            }
        }
    }


}
