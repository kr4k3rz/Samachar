package com.codelite.kr4k3rz.samachar.ui.fragments.hotnewstab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.ui.adapter.ComplexRecyclerViewAdapter;
import com.codelite.kr4k3rz.samachar.ui.adapter.SimpleDividerItemDecoration;

import java.util.List;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllFeedFrag extends Fragment {

    private static final String CACHE_NAME = "AllFeeds";
    private static final String TAG = AllFeedFrag.class.getSimpleName();


    public AllFeedFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_all_feed, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_All_Feeds);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());/**/
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        List<Object> objects = Paper.book().read(CACHE_NAME);
        recyclerView.setAdapter(new ComplexRecyclerViewAdapter(getContext(), objects));
        return rootView;
    }


}
