package com.example.newsapp.ui.headlines;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.newsapp.MySingleton;
import com.example.newsapp.R;
import com.example.newsapp.longcardAdapter;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class HeadlinesFragment extends Fragment {

    private HeadlinesViewModel headlinesViewModel;
    private com.example.newsapp.longcardAdapter longcardAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar spinner;
    private TextView fetchText;
    private View root;
    private RecyclerView headlinesRecyclerView;
//    private LayoutInflater inflater;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        headlinesViewModel =
                ViewModelProviders.of(this).get(HeadlinesViewModel.class);
//        this.inflater = inflater;
        root = inflater.inflate(R.layout.fragment_headlines, container, false);
        fetchText = root.findViewById(R.id.fetchTextHeadlines);
        spinner = root.findViewById(R.id.headlinesProgressBar);
        headlinesRecyclerView =  root.findViewById(R.id.headlines_news);

        //first fetch
        fetchText.setVisibility(View.VISIBLE);
        spinner.setVisibility((View.VISIBLE));
        headlinesRecyclerView.setVisibility(View.GONE);


        //refresh
        mSwipeRefreshLayout = root.findViewById(R.id.headlines_swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                fetch();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });


        //fetch when certain tab is selected
        final String[] tabText = {"world", "business", "politics", "sport", "technology", "science"};
        TabLayout myTab = root.findViewById(R.id.tabLayout);


        myTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
//                Log.d("pod", String.valueOf(pos));
                String keyword = tabText[pos];
                fetchText.setVisibility(View.VISIBLE);
                spinner.setVisibility((View.VISIBLE));
                headlinesRecyclerView.setVisibility(View.GONE);
                fetch(keyword);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        myTab.getTabAt(0).select();
        fetch("world");

        return root;
    }

    private void fetch(String tabWord){

//        String url = "http://10.0.2.2:4000/headlines/?id=" + tabWord;
        String url = "http://newsapp-zpzzpz.us-east-1.elasticbeanstalk.com/headlines/?id=" + tabWord;

        JsonObjectRequest headlinesNewsRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            longcardAdapter = new longcardAdapter(response.getJSONArray("savedNews"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        headlinesRecyclerView.setLayoutManager(layoutManager);
                        headlinesRecyclerView.setAdapter(longcardAdapter);
                        headlinesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        spinner.setVisibility(View.GONE);
                        fetchText.setVisibility(View.GONE);
                        headlinesRecyclerView.setVisibility(View.VISIBLE);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("headlinesNews",error.toString());
                    }
                });
        MySingleton.getInstance(getActivity()).addToRequestQueue(headlinesNewsRequest);


    }

    @Override
    public void onResume() {
        super.onResume();


        if (longcardAdapter != null) {
            longcardAdapter.notifyDataSetChanged();
        }


    }
}
