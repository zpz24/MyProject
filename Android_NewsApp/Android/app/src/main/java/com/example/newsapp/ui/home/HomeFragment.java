package com.example.newsapp.ui.home;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
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
import com.example.newsapp.MainActivity;
import com.example.newsapp.MySingleton;
import com.example.newsapp.R;
import com.example.newsapp.longcardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private longcardAdapter longcardAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar spinner;
    private TextView fetchText;
    private View root;
    private JSONArray jsArray;
    private String cityName;
    private TextView temperature;
    private TextView summary;
    private ImageView weatherImg;
    private TextView city;
//    private LayoutInflater inflater;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
//        this.inflater = inflater;

        //spinner
        spinner = root.findViewById(R.id.homeProgressBar);
        fetchText = root.findViewById(R.id.fetchTextHome);


        //Get location
        Location location = ((MainActivity) getActivity()).getLocation();

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
//        Log.d("location", String.valueOf(location));
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        cityName = addresses.get(0).getLocality();
        String stateName = addresses.get(0).getAdminArea();

        final CardView weatherCard = root.findViewById(R.id.weather_card);
        city = weatherCard.findViewById(R.id.city);
        city.setText(cityName);
        final TextView state = weatherCard.findViewById(R.id.state);
        state.setText(stateName);

        temperature = weatherCard.findViewById(R.id.temperature);
        summary = weatherCard.findViewById(R.id.summary);
        weatherImg = weatherCard.findViewById(R.id.weather_img);

        //refresh
        mSwipeRefreshLayout = root.findViewById(R.id.home_swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetch();
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


        //fetch weather information
        fetch();
        spinner.setVisibility(View.VISIBLE);
        fetchText.setVisibility(View.VISIBLE);



        return root;
    }

    public void fetch() {

        //fetch weather information

        String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=1d3b9fa1db47c8a2108f107300901d9f";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, weatherUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double temp = response.getJSONObject("main").getDouble("temp");
                            temperature.setText(String.valueOf(Math.round(temp)) + "°C");
                            String sum = response.getJSONArray("weather").getJSONObject(0).getString("main");
                            summary.setText(sum);
                            if (sum.equals("Clouds")) {
                                weatherImg.setImageResource(R.drawable.cloudy_weather);
                            } else if (sum.equals("Clear")) {
                                weatherImg.setImageResource(R.drawable.clear_weather);
                            } else if (sum.equals("Snow")) {
                                weatherImg.setImageResource(R.drawable.snowy_weather);
                            } else if (sum.equals("Rain") || sum.equals("Drizzle")) {
                                weatherImg.setImageResource(R.drawable.rainy_weather);
                            } else if (sum.equals("Thunderstorm")) {
                                weatherImg.setImageResource(R.drawable.thunder_weather);
                            } else {
                                weatherImg.setImageResource(R.drawable.sunny_weather);
                            }


                        } catch (JSONException e) {
//                            city.setText("Error");
//                            Log.e("weathertry", e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        city.setText("Error");
                        Log.e("weather", error.toString());
                    }
                });
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);

        //homeNews request
//        String homeNewsUrl = "http://10.0.2.2:4000/home";
        String homeNewsUrl = "http://newsapp-zpzzpz.us-east-1.elasticbeanstalk.com/home";
        JsonObjectRequest homeNewsRequest = new JsonObjectRequest
                (Request.Method.GET, homeNewsUrl, null, new Response.Listener<JSONObject>() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
//                            longcardAdapter = new longcardAdapter(getContext(), response.getJSONArray("savedNews"));
                            longcardAdapter = new longcardAdapter(response.getJSONArray("savedNews"));
                            jsArray = response.getJSONArray("savedNews");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RecyclerView homeRecyclerView = root.findViewById(R.id.home_news);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        homeRecyclerView.setLayoutManager(layoutManager);
                        homeRecyclerView.setAdapter(longcardAdapter);
                        homeRecyclerView.setItemAnimator(new DefaultItemAnimator());

                        spinner.setVisibility(View.GONE);
                        fetchText.setVisibility(View.GONE);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("homeNews", error.toString());
                    }
                });
        MySingleton.getInstance(getActivity()).addToRequestQueue(homeNewsRequest);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (longcardAdapter != null) {
            fetch();
//            String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=1d3b9fa1db47c8a2108f107300901d9f";
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                    (Request.Method.GET, weatherUrl, null, new Response.Listener<JSONObject>() {
//
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                Double temp = response.getJSONObject("main").getDouble("temp");
//                                temperature.setText(String.valueOf(Math.round(temp)) + "°C");
//                                String sum = response.getJSONArray("weather").getJSONObject(0).getString("main");
//                                summary.setText(sum);
//                                if (sum.equals("Clouds")) {
//                                    weatherImg.setImageResource(R.drawable.cloudy_weather);
//                                } else if (sum.equals("Clear")) {
//                                    weatherImg.setImageResource(R.drawable.clear_weather);
//                                } else if (sum.equals("Snow")) {
//                                    weatherImg.setImageResource(R.drawable.snowy_weather);
//                                } else if (sum.equals("Rain") || sum.equals("Drizzle")) {
//                                    weatherImg.setImageResource(R.drawable.rainy_weather);
//                                } else if (sum.equals("Thunderstorm")) {
//                                    weatherImg.setImageResource(R.drawable.thunder_weather);
//                                } else {
//                                    weatherImg.setImageResource(R.drawable.sunny_weather);
//                                }
//
//
//                            } catch (JSONException e) {
//                                city.setText("Error");
//                                Log.e("weathertry", e.toString());
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            city.setText("Error");
//                            Log.e("weather", error.toString());
//                        }
//                    });
//            MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
//            longcardAdapter.notifyDataSetChanged();
        }


    }


}


