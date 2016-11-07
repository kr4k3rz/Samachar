package com.codelite.kr4k3rz.samachar.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.Subscribe;
import com.codelite.kr4k3rz.samachar.model.search.EntriesItem;
import com.codelite.kr4k3rz.samachar.model.search.ResponseSearch;
import com.codelite.kr4k3rz.samachar.util.SnackMsg;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_searchResult);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchItem.expandActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //  swipeRefreshLayout.setRefreshing(true);
                progressBar.setVisibility(View.VISIBLE);
                queryFeeds(query);
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void queryFeeds(String query) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("https://ajax.googleapis.com/ajax/services/feed/find?v=1.0&q=" + query).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            final Handler handler = new Handler(SearchActivity.this.getMainLooper());

            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SnackMsg.showMsgShort(toolbar, e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String jsonStr = null;
                        try {
                            jsonStr = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.i("TAG", " : " + jsonStr);
                        ResponseSearch responseSearch = new Gson().fromJson(jsonStr, ResponseSearch.class);
                        Log.i("TAG", "response : " + responseSearch.getResponseData().getEntries().size());
                        recyclerView.setAdapter(new SearchQueryAdapter(getBaseContext(), responseSearch.getResponseData().getEntries()));
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                Subscribe subscribe;
                subscribe = (Subscribe) data.getSerializableExtra("SubscribeItem");
                intent.putExtra("SubscribedItem", subscribe);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    class SearchQueryAdapter extends RecyclerView.Adapter<SearchQueryAdapter.MyViewHolder> {
        private final Context context;
        private final List<EntriesItem> entries;

        SearchQueryAdapter(Context context, List<EntriesItem> entries) {
            this.context = context;
            this.entries = entries;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.textView_title.setText(Html.fromHtml(entries.get(position).getTitle()));
            holder.textView_code_snippet.setText(Html.fromHtml(entries.get(position).getContentSnippet()));
            holder.button_subscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SubscribeActivity.class);
                    intent.putExtra("QUERY", entries.get(holder.getAdapterPosition()));
                    startActivityForResult(intent, 3);

                }
            });
            holder.textView_link.setText(entries.get(position).getLink());

        }

        @Override
        public int getItemCount() {
            return entries.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            final TextView textView_title;
            final TextView textView_code_snippet;
            final TextView textView_link;
            final LinearLayout button_subscribe;

            MyViewHolder(View itemView) {
                super(itemView);
                textView_title = (TextView) itemView.findViewById(R.id.query_title);
                textView_code_snippet = (TextView) itemView.findViewById(R.id.query_codeSnippet);
                button_subscribe = (LinearLayout) itemView.findViewById(R.id.ll_search);
                textView_link = (TextView) itemView.findViewById(R.id.query_link);
            }
        }
    }
}
