package com.codelite.kr4k3rz.samachar.fragments.allnewstab;


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
import com.codelite.kr4k3rz.samachar.adapter.SimpleDividerItemDecoration;
import com.codelite.kr4k3rz.samachar.handler.AsyncHelper;
import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.model.WhichCategory;
import com.codelite.kr4k3rz.samachar.util.CheckInternet;
import com.codelite.kr4k3rz.samachar.util.SnackMsg;

import java.util.List;

import io.paperdb.Paper;


/**
 * A simple {@link Fragment} subclass.
 */
public class EntertainmentFrag extends Fragment {

    private static final String TAG = EntertainmentFrag.class.getSimpleName();
    private static final String CACHE_NAME = WhichCategory.ENTERTAINMENT.getSecondName();
    private final String[] mSpecialFeed = {"https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://screennepal.com/feed&num=-1"};
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View rootView;

    public EntertainmentFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_entertainment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_entertainment);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout_entertainment);
        initSwipeRefresh();
        return rootView;
    }

    private void initSwipeRefresh() {
        mSwipeRefreshLayout.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         Log.i(TAG, "SwipeRefresh post()");
                                         if (!Paper.book().exist(CACHE_NAME) && CheckInternet.isNetworkAvailable(getContext())) {
                                             new AsyncHelper(rootView, mSwipeRefreshLayout, getContext(), CACHE_NAME, recyclerView, WhichCategory.ENTERTAINMENT.ordinal()).execute(mSpecialFeed);
                                         } else {
                                             mSwipeRefreshLayout.setRefreshing(true);
                                             List<Entry> list = Paper.book().read(CACHE_NAME);
                                             if (!Paper.book().exist(CACHE_NAME)) {
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
                    new AsyncHelper(rootView, mSwipeRefreshLayout, getContext(), CACHE_NAME, recyclerView, WhichCategory.ENTERTAINMENT.ordinal()).execute(mSpecialFeed);
                } else {
                    SnackMsg.showMsgShort(rootView, "couldn't connect to internet");
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }


}
