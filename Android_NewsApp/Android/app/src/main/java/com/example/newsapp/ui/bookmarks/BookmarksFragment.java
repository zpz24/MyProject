package com.example.newsapp.ui.bookmarks;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.longcardAdapter;
import com.example.newsapp.newsList;
import com.example.newsapp.shortcardAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookmarksFragment extends Fragment{
    private BookmarksViewModel bookmarksViewModel;
    private RecyclerView bookmarksRecyclerView;
    private com.example.newsapp.shortcardAdapter shortcardAdapter;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private RecyclerView.ItemDecoration itemDecoration;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bookmarksViewModel =
                ViewModelProviders.of(this).get(BookmarksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        final TextView noBookmark = root.findViewById(R.id.noBookmarks);
        bookmarksRecyclerView = root.findViewById(R.id.bookmarks);


        try {

            pref = getContext().getSharedPreferences("MyPref", 0);
            editor = pref.edit();

            if( pref.getAll().size() > 0){
//                noBookmark.setVisibility(View.GONE);


                List<newsList> news = new ArrayList<>();
                Map<String, ?> allBookmark = pref.getAll();
                Gson gson = new Gson();
                for(String key: allBookmark.keySet()){
//                    Log.d("gson",pref.getString(key, null));
                    newsList oneNews = gson.fromJson(pref.getString(key, null), newsList.class);
                    news.add(oneNews);
                }
                shortcardAdapter = new shortcardAdapter(news);
                bookmarksRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                itemDecoration = new
                        DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
                bookmarksRecyclerView.addItemDecoration(itemDecoration);
                bookmarksRecyclerView.setAdapter(shortcardAdapter);
                bookmarksRecyclerView.setItemAnimator(new DefaultItemAnimator());
//                shortcardAdapter.notifyDataSetChanged();
//                Log.d("adapter", String.valueOf(shortcardAdapter.getItemCount()));
            }
            else{
                bookmarksRecyclerView.setVisibility(View.GONE);
            }
        }
        catch (NullPointerException e){
            bookmarksRecyclerView.setVisibility(View.GONE);
            noBookmark.setVisibility(View.VISIBLE);
        }


        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();

        if (shortcardAdapter != null) {
            if( pref.getAll().size() > 0){

                List<newsList> news = new ArrayList<>();
                Map<String, ?> allBookmark = pref.getAll();
                Gson gson = new Gson();
                for(String key: allBookmark.keySet()){
                    newsList oneNews = gson.fromJson(pref.getString(key, null), newsList.class);
                    news.add(oneNews);
                }
                shortcardAdapter = new shortcardAdapter(news);
                bookmarksRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                itemDecoration = new
                        DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
                bookmarksRecyclerView.addItemDecoration(itemDecoration);
                bookmarksRecyclerView.setAdapter(shortcardAdapter);
                bookmarksRecyclerView.setItemAnimator(new DefaultItemAnimator());
            }
            else{
                bookmarksRecyclerView.setVisibility(View.GONE);
            }
        }


    }
}
