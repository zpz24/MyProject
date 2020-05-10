package com.example.newsapp;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class shortcardAdapter extends RecyclerView.Adapter<shortcardAdapter.MyViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<newsList> news;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int len;
    private ViewGroup container;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public shortcardAdapter( List<newsList> news) {
//        this.mContext = context;
//        inflater = LayoutInflater.from(mContext);

//        pref = mContext.getSharedPreferences("MyPref", 0);
//        editor = pref.edit();
        this.news = news;


    }

    @Override
    public int getItemCount() {

        return news.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d("count2", String.valueOf(news.size()));
        mContext = parent.getContext();
        inflater = LayoutInflater.from(mContext);
        pref = mContext.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        container = parent;
        View view = inflater.inflate(R.layout.shortcard, parent, false);
        View mainView = inflater.inflate(R.layout.fragment_bookmarks, parent, false);
        MyViewHolder holder = new MyViewHolder(mainView, view);

        return holder;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final newsList data = news.get(position);
        final String keyTitle = data.getTitle();
        final String id = data.getId();
        String date = data.getDate();
        DateTimeFormatter f = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault());
        ZonedDateTime zdt = ZonedDateTime.parse(date, f);
        LocalDateTime ldt = zdt.toLocalDateTime();
        date = String.valueOf(ldt);

        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        if (month.equals("01")) {
            month = "Jan";
        } else if (month.equals("02")) {
            month = "Feb";
        } else if (month.equals("03")) {
            month = "Mar";
        } else if (month.equals("04")) {
            month = "Apr";
        } else if (month.equals("05")) {
            month = "May";
        } else if (month.equals("06")) {
            month = "Jun";
        } else if (month.equals("07")) {
            month = "Jul";
        } else if (month.equals("08")) {
            month = "Aug";
        } else if (month.equals("09")) {
            month = "Sep";
        } else if (month.equals("10")) {
            month = "Oct";
        } else if (month.equals("11")) {
            month = "Nov";
        } else {
            month = "Dec";
        }
        String temp = day + " " + month + " | " + data.getSection();
        final String rmvBookmark = "\"" + keyTitle + "\" was removed from favorites";


        holder.title.setText(keyTitle);
        holder.info.setText(temp);
        Glide.with(mContext).load(data.getImg()).into(holder.img);



        holder.savedBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext, rmvBookmark, Toast.LENGTH_LONG).show();
                editor.remove(id);
                editor.commit();
                news.remove(data);
                if(news.size() == 0){
                    container.setVisibility(View.GONE);
                }
                notifyDataSetChanged();

            }
        });

        holder.shortcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mContext, DetailArticle.class);
                myIntent.putExtra("id",data.getId());
                mContext.startActivity(myIntent);
            }
        });

        holder.shortcard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.dialog);

                TextView dialog_title = dialog.findViewById(R.id.dialog_text);
                dialog_title.setText(keyTitle);

                ImageView dialog_img = dialog.findViewById(R.id.dialog_image);
                Glide.with(mContext).load(data.getImg()).into(dialog_img);

                ImageButton dialog_twitter = dialog.findViewById(R.id.dialog_twitter);
                dialog_twitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Uri share = Uri.parse("https://twitter.com/intent/tweet?text=Check out this Link:" + data.getShareUrl() +"&hashtags=CSCI571NewsSearch");
                        Intent shareIntent = new Intent(Intent.ACTION_VIEW, share);
                        mContext.startActivity(shareIntent);
                    }
                });

                final ImageButton dialog_empty = dialog.findViewById(R.id.dialog_empty_bookmark);
                final ImageButton dialog_saved = dialog.findViewById(R.id.dialog_saved_bookmark);
                dialog_saved.setVisibility(View.VISIBLE);
                dialog_empty.setVisibility(View.GONE);



                dialog_saved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, rmvBookmark, Toast.LENGTH_LONG).show();
//                        holder.savedBookmark.setVisibility(View.GONE);
//                        dialog_saved.setVisibility(View.GONE);
                        editor.remove(id);
                        editor.commit();
                        news.remove(data);
                        if(news.size() == 0){
                            container.setVisibility(View.GONE);
                        }
                        notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });

                dialog.show();
                return false;
            }
        });

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView info;
//        TextView noBookmark;
        ImageView img;
        ImageButton savedBookmark;
        CardView shortcard;
//        RecyclerView recyclerView;


        public MyViewHolder(View mainView, View view) {
            super(view);
//            super(mainView);
//            Log.d("count3", String.valueOf(news.size()));
            title = view.findViewById(R.id.shortCard_title);
            info = view.findViewById(R.id.shortCard_section);
            img = view.findViewById(R.id.shortCard_img);
//            recyclerView = mainView.findViewById(R.id.bookmarks);
            savedBookmark = view.findViewById(R.id.short_savedBookmark);
            shortcard = view.findViewById(R.id.shortCard);
//            noBookmark = view.findViewById(R.id.noBookmarks);

        }


    }
}
