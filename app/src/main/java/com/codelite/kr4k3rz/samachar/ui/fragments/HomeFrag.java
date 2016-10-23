package com.codelite.kr4k3rz.samachar.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.ui.adapter.ComplexRecyclerViewAdapter;
import com.codelite.kr4k3rz.samachar.ui.adapter.SimpleDividerItemDecoration;
import com.codelite.kr4k3rz.samachar.util.CacheLang;

import java.util.List;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFrag extends Fragment {


    private static final String CACHE_NAME = "AllFeeds";
    private static final String TAG = HomeFrag.class.getSimpleName();
    RecyclerView recyclerView;


    public HomeFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_feed, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_All_Feeds);
        recyclerView.setHasFixedSize(false);
       /* TextView textView = (TextView) view.findViewById(R.id.textViewheader);
        textView.setText("m chaned");*/
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());/**/
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        Log.i(TAG, CacheLang.findLang(CACHE_NAME));
        List<Object> objects = Paper.book().read(CacheLang.findLang(CACHE_NAME));
        recyclerView.setAdapter(new ComplexRecyclerViewAdapter(getContext(), objects));
    }

}
