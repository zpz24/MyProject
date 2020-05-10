package com.example.newsapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private longcardAdapter longcardAdapter;
    private Context context;
    private ProgressBar progressBar;
    private TextView fetchText;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String query;
//    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;

        toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
//        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressBar = findViewById(R.id.search_progressBar);
        fetchText = findViewById(R.id.search_fetch_text);

        progressBar.setVisibility(View.VISIBLE);
        fetchText.setVisibility(View.VISIBLE);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            actionBar.setTitle("Search Results for " + query);
            fetch(query);
        }

        //refresh
        mSwipeRefreshLayout = findViewById(R.id.search_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetch(query);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });

    }

    private void fetch(String keyword){
        String url = "http://newsapp-zpzzpz.us-east-1.elasticbeanstalk.com/search/?id=" + keyword;
        JsonObjectRequest homeNewsRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            longcardAdapter = new longcardAdapter(response.getJSONArray("search_result"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RecyclerView homeRecyclerView = findViewById(R.id.search_recycle);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                        homeRecyclerView.setLayoutManager(layoutManager);
                        homeRecyclerView.setAdapter(longcardAdapter);
                        homeRecyclerView.setItemAnimator(new DefaultItemAnimator());

                        progressBar.setVisibility(View.GONE);
                        fetchText.setVisibility(View.GONE);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("searchNews", error.toString());
                    }
                });
        MySingleton.getInstance(context).addToRequestQueue(homeNewsRequest);
    }



//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == android.R.id.home)
//        {
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//
//    }

    @Override
    public void onResume() {
        super.onResume();

        if (longcardAdapter != null) {
            longcardAdapter.notifyDataSetChanged();
        }


    }
}
