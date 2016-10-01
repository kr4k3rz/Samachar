package com.codelite.kr4k3rz.samachar.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.adapter.RvAdapter;
import com.codelite.kr4k3rz.samachar.handler.AsyncHelper;
import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.model.WhichCategory;
import com.codelite.kr4k3rz.samachar.util.CheckInternet;
import com.codelite.kr4k3rz.samachar.util.SnackMsg;
import com.orhanobut.hawk.Hawk;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HealthFrag extends Fragment {

    private static final String TAG = HealthFrag.class.getSimpleName();
    private static final String CACHE_NAME = WhichCategory.HEALTH.name();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View rootView;
    private String mSpecialFeed[] = {"https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.nepalihealth.com/feed/&num=-1",
            "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://nepalhealthnews.com/feed/&num=-1"};

    public HealthFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_health, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_health);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setScrollbarFadingEnabled(true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout_health);
        initSwipeRefresh();
        return rootView;
    }

    private void initSwipeRefresh() {
        mSwipeRefreshLayout.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         Log.i(TAG, "SwipeRefresh post()");
                                         if (!Hawk.contains(CACHE_NAME) && CheckInternet.isNetworkAvailable(getContext())) {
                                             new AsyncHelper(rootView, mSwipeRefreshLayout, getContext(), CACHE_NAME, recyclerView, WhichCategory.HEALTH.ordinal()).execute(mSpecialFeed);
                                         } else {
                                             mSwipeRefreshLayout.setRefreshing(true);
                                             List<Entry> list = Hawk.get(CACHE_NAME);
                                             if (!Hawk.contains(CACHE_NAME)) {
                                                 SnackMsg.showMsgShort(rootView, "connect to internet");
                                             } else
                                                 recyclerView.setAdapter(new RvAdapter(getContext(), list));
                                             mSwipeRefreshLayout.setRefreshing(false);
                                         }

                                     }
                                 }
        );

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "SwipeRefresh()  ");
                if (CheckInternet.isNetworkAvailable(getContext())) {
                    new AsyncHelper(rootView, mSwipeRefreshLayout, getContext(), CACHE_NAME, recyclerView, WhichCategory.HEALTH.ordinal()).execute(mSpecialFeed);
                } else {
                    SnackMsg.showMsgShort(rootView, "couldn't connect to internet");
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }

}
