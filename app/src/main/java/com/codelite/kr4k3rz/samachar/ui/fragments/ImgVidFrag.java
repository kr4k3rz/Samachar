package com.codelite.kr4k3rz.samachar.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.model.WhichCategory;
import com.codelite.kr4k3rz.samachar.ui.adapter.RvAdapter;
import com.codelite.kr4k3rz.samachar.ui.adapter.SimpleDividerItemDecoration;

import java.util.List;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImgVidFrag extends Fragment {
    private static final String CACHE_NAME = WhichCategory.IMGVID.getSecondName();
    private static final String TAG = ImgVidFrag.class.getSimpleName();

    public ImgVidFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_img_vid, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_ImgVid);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());/**/
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        List<Entry> list = Paper.book().read(CACHE_NAME);
        recyclerView.setAdapter(new RvAdapter(getContext(), list));
        return rootView;
    }

}
