package com.codelite.kr4k3rz.samachar.ui.fragments;


import android.content.Intent;
import android.net.Uri;
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

import angtrim.com.fivestarslibrary.FiveStarsDialog;
import angtrim.com.fivestarslibrary.NegativeReviewListener;
import angtrim.com.fivestarslibrary.ReviewListener;
import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFrag extends Fragment {


    private static final String CACHE_NAME = "AllFeeds";
    private static final String TAG = HomeFrag.class.getSimpleName();
    private RecyclerView recyclerView;

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
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        Log.i(TAG, CacheLang.findLang(CACHE_NAME));
        List<Object> objects = Paper.book().read(CacheLang.findLang(CACHE_NAME));
        recyclerView.setAdapter(new ComplexRecyclerViewAdapter(getContext(), objects));

        FiveStarsDialog fiveStarsDialog = new FiveStarsDialog(getContext(), "xitize@gmail.com");
        fiveStarsDialog.setRateText("How would you rate this app?")
                .setTitle("Rate your experience")
                .setForceMode(false)
                .setUpperBound(2) // Market opened if a rating >= 2 is selected
                .setNegativeReviewListener(new NegativeReviewListener() {
                    @Override
                    public void onNegativeReview(int i) {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "xitize@gmail.com", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Samachar FeedBack");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi,i would like to ");
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    }
                }) // OVERRIDE mail intent for negative review
                .setReviewListener(new ReviewListener() {
                    @Override
                    public void onReview(int i) {
                       // ToastMsg.shortMsg(getContext(), "Show store");
                    }
                }) // Used to listen for reviews (if you want to track them )
                .showAfter(10);
    }


}
