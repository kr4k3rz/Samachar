package com.codelite.kr4k3rz.samachar.ui.fragments.allnewstab;


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
import com.codelite.kr4k3rz.samachar.handler.AsyncHelper;
import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.model.FeedLists;
import com.codelite.kr4k3rz.samachar.model.WhichCategory;
import com.codelite.kr4k3rz.samachar.ui.adapter.RvAdapter;
import com.codelite.kr4k3rz.samachar.ui.adapter.SimpleDividerItemDecoration;
import com.codelite.kr4k3rz.samachar.util.CheckInternet;
import com.codelite.kr4k3rz.samachar.util.SnackMsg;

import java.util.List;

import io.paperdb.Paper;


/**
 * A simple {@link Fragment} subclass.
 */
public class HealthFrag extends Fragment {

    private static final String TAG = HealthFrag.class.getSimpleName();
    private static final String CACHE_NAME = WhichCategory.HEALTH.getSecondName();
    private final String[] mSpecialFeed = FeedLists.getFeedListCached(3);
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View rootView;

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
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout_health);
        List<Entry> list = Paper.book().read(CACHE_NAME);
        recyclerView.setAdapter(new RvAdapter(getContext(), list));
        initSwipeRefresh();
        return rootView;
    }

    private void initSwipeRefresh() {
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