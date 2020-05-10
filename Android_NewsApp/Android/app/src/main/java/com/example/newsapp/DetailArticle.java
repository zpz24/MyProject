package com.example.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
//import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
//import org.sufficientlysecure.htmltextview.HtmlTextView;

public class DetailArticle extends AppCompatActivity {

    private ProgressBar spinner;
    private TextView fetchText;
    private ScrollView scrollView;
    private ImageView imageView;
    private TextView title;
    private TextView section;
    private TextView date;
    private TextView description;
    private Context context;
    private Toolbar myToolbar;
    private ActionBar actionBar;
    private TextView fullArticle;
    private ImageButton empty;
    private ImageButton saved;
    private ImageButton twitter;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        context = this;

        myToolbar = findViewById(R.id.article_toolbar);
        setSupportActionBar(myToolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);


        scrollView = findViewById(R.id.scroll_article);
        spinner = findViewById(R.id.article_progressBar);
        fetchText = findViewById(R.id.article_fetch);
        spinner.setVisibility(View.VISIBLE);
        fetchText.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        imageView = findViewById(R.id.article_img);
        title = findViewById(R.id.article_title);
        section = findViewById(R.id.article_section);
        date = findViewById(R.id.article_date);
        description = findViewById(R.id.article_description);
        fullArticle = findViewById(R.id.full_article);

        pref = this.getSharedPreferences("MyPref", 0);
        editor = pref.edit();

        empty = findViewById(R.id.article_emptyBookmark);
        saved = findViewById(R.id.article_savedBookmark);
        twitter = findViewById(R.id.article_twitter);
        empty.setVisibility(View.GONE);
        saved.setVisibility(View.GONE);
        twitter.setVisibility(View.GONE);


        Intent myIntent = getIntent();
        String id = myIntent.getStringExtra("id");
        fetch(id);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    private void fetch(String id){
        final String[] list = new String[8];
        String url = "http://newsapp-zpzzpz.us-east-1.elasticbeanstalk.com/detail/?id=" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject myArticle = response.getJSONObject("my_article");
                            list[0] = myArticle.getString("image");
                            list[1] = myArticle.getString("title");
                            list[2] = myArticle.getString("section");


                            String date = myArticle.getString("date");
                            String originDate = date;
                            list[7] = originDate;
                            DateTimeFormatter f = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault());
                            ZonedDateTime zdt = ZonedDateTime.parse(date, f);
                            LocalDateTime ldt = zdt.toLocalDateTime();
                            date = String.valueOf(ldt);
                            String year = date.substring(0,4);
                            String month = date.substring(5,7);
                            String day = date.substring(8,10);
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
                            list[3] = day + " " + month + " " + year;

                            list[4] = myArticle.getString("description");
                            list[5] = myArticle.getString("shareUrl");
                            list[6] = myArticle.getString("id");
                            Log.d("article", String.valueOf(list));


                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                        twitter.setVisibility(View.VISIBLE);
                        Glide.with(context).load(list[0]).into(imageView);
                        title.setText(list[1]);
                        actionBar.setTitle(list[1]);
                        section.setText(list[2]);
                        date.setText(list[3]);
                        description.setText(Html.fromHtml(list[4]));
                        fullArticle.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                        final Uri uri = Uri.parse(list[5]);
                        fullArticle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        });

                        if(pref.contains(list[6])){
                            empty.setVisibility(View.GONE);
                            saved.setVisibility(View.VISIBLE);
                        }
                        else{
                            saved.setVisibility(View.GONE);
                            empty.setVisibility(View.VISIBLE);
                        }

                        final String rmvBookmark = "\"" + list[1] + "\" was removed from favorites";
                        final String addBookmark = "\"" + list[1] + "\" was added to bookmarks";

                        empty.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                empty.setVisibility(View.GONE);
                                saved.setVisibility(View.VISIBLE);
                                Toast.makeText(context, addBookmark, Toast.LENGTH_LONG).show();
                                newsList newsList = new newsList(list[1], list[7], list[2], list[0], list[5], list[6]);
                                Gson gson = new Gson();
                                String value = gson.toJson(newsList);
                                editor.putString(list[6], value);
                                editor.commit();
                            }
                        });

                        saved.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                saved.setVisibility(View.GONE);
                                empty.setVisibility(View.VISIBLE);
                                Toast.makeText(context, rmvBookmark, Toast.LENGTH_LONG).show();
                                editor.remove(list[6]);
                                editor.commit();
                            }
                        });

                        twitter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Uri share = Uri.parse("https://twitter.com/intent/tweet?text=Check out this Link:" + list[5] +"&hashtags=CSCI571NewsSearch");
                                Intent shareIntent = new Intent(Intent.ACTION_VIEW, share);
                                startActivity(shareIntent);
                            }
                        });

                        spinner.setVisibility(View.GONE);
                        fetchText.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("article",error.toString());
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

}
