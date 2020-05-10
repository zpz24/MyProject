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

import com.google.gson.Gson;


import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class longcardAdapter extends RecyclerView.Adapter <longcardAdapter.MyViewHolder>{

    private Context mContext;
    private LayoutInflater inflater;
    private JSONArray news;
    private List<newsList> myData;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int len;
//    private LayoutInflater inflater;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public longcardAdapter(JSONArray news) throws JSONException {
//        this.mContext=context;
//        inflater = LayoutInflater.from(mContext);
//        this.inflater = inflater;

//        editor.clear();
//        editor.commit();
        myData = new ArrayList<newsList>();
        len = news.length();

        for(int i = 0; i < len; i++){
            String title = news.getJSONObject(i).getString("title");
            String section = news.getJSONObject(i).getString("section");
            String time = news.getJSONObject(i).getString("date");

            String image = news.getJSONObject(i).getString("image");
            String share = news.getJSONObject(i).getString("shareUrl");
            String id = news.getJSONObject(i).getString("id");




            myData.add(new newsList(title, time, section, image, share, id));
        }
    }

    @Override
    public int getItemCount() {
        return len;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
            pref = mContext.getSharedPreferences("MyPref", 0);
            editor = pref.edit();
            inflater = LayoutInflater.from(mContext);
        }

        View view = inflater.inflate(R.layout.longcard, parent, false);
        MyViewHolder holder= new MyViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final newsList data = myData.get(position);
        final String keyTitle = data.getTitle();
        final String id = data.getId();

        if(pref.contains(id)){
            holder.emptyBookmark.setVisibility(View.GONE);
            holder.savedBookmark.setVisibility(View.VISIBLE);
        }
        else{
            holder.emptyBookmark.setVisibility(View.VISIBLE);
            holder.savedBookmark.setVisibility(View.GONE);
        }
        String date = data.getDate();
        String time = data.getDate();

        DateTimeFormatter f = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault());
        ZonedDateTime zdt = ZonedDateTime.parse(date, f);
        LocalDateTime ldt = zdt.toLocalDateTime();
        LocalDateTime ldtNow = LocalDateTime.now();
        Duration duration = Duration.between(ldt, ldtNow);
//        Log.d("time", String.valueOf(ldt));

        if (duration.toDays() > 0){
            date = String.valueOf(Math.round(duration.toDays())) + "d";
        }
        else if(duration.toHours() > 0){
            date = String.valueOf(Math.round(duration.toHours())) + "h";
        }
        else if(duration.toMinutes() > 0){
            date = String.valueOf(Math.round(duration.toMinutes())) + "m";
        }
        else{
            date = String.valueOf(Math.round(duration.toMinutes())) + "s";
        }

        holder.title.setText(keyTitle);
        String temp = date + " ago | " + data.getSection();
        holder.info.setText(temp);
        Glide.with(mContext).load(data.getImg()).into(holder.img);
        final String addBookmark = "\"" + keyTitle + "\" was added to bookmarks";
        final String rmvBookmark = "\"" + keyTitle + "\" was removed from favorites";

        Gson gson = new Gson();
        final String value = gson.toJson(data);

        
        holder.emptyBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, addBookmark, Toast.LENGTH_LONG).show();
                holder.emptyBookmark.setVisibility(View.GONE);
                holder.savedBookmark.setVisibility(View.VISIBLE);
                editor.putString(id, value);
                editor.commit();
            }
        });

        holder.savedBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, rmvBookmark, Toast.LENGTH_LONG).show();
                holder.emptyBookmark.setVisibility(View.VISIBLE);
                holder.savedBookmark.setVisibility(View.GONE);
                editor.remove(id);
                editor.commit();
            }
        });

        holder.longcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(mContext, DetailArticle.class);
                myIntent.putExtra("id",data.getId());
                mContext.startActivity(myIntent);

            }
        });

        holder.longcard.setOnLongClickListener(new View.OnLongClickListener() {
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
                if(pref.contains(id)){
                    dialog_empty.setVisibility(View.GONE);
                    dialog_saved.setVisibility(View.VISIBLE);
                }
                else{
                    dialog_empty.setVisibility(View.VISIBLE);
                    dialog_saved.setVisibility(View.GONE);
                }

                dialog_empty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, addBookmark, Toast.LENGTH_LONG).show();
                        holder.emptyBookmark.setVisibility(View.GONE);
                        holder.savedBookmark.setVisibility(View.VISIBLE);
                        dialog_empty.setVisibility(View.GONE);
                        dialog_saved.setVisibility(View.VISIBLE);
                        editor.putString(id, value);
                        editor.commit();
                    }
                });

                dialog_saved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, rmvBookmark, Toast.LENGTH_LONG).show();
                        holder.emptyBookmark.setVisibility(View.VISIBLE);
                        holder.savedBookmark.setVisibility(View.GONE);
                        dialog_empty.setVisibility(View.VISIBLE);
                        dialog_saved.setVisibility(View.GONE);
                        editor.remove(id);
                        editor.commit();
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
        ImageView img;
        ImageButton emptyBookmark;
        ImageButton savedBookmark;
        CardView longcard;
        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.card_title);
            info = view.findViewById(R.id.card_bottom);
            img = view.findViewById(R.id.newsImg);
            emptyBookmark = view.findViewById(R.id.emptyBookmark);
            savedBookmark = view.findViewById(R.id.savedBookmark);
            longcard = view.findViewById(R.id.long_card);
        }


    }


}
