package com.example.newsapp.ui.trending;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.newsapp.MySingleton;
import com.example.newsapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrendingFragment extends Fragment {

    private TrendingViewModel trendingViewModel;
    private LineChart lineChart;
    private EditText inputText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        trendingViewModel =
                ViewModelProviders.of(this).get(TrendingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_trending, container, false);
        lineChart = root.findViewById(R.id.chart);
        inputText = root.findViewById(R.id.trending_input);
        fetch("CoronaVirus");

        Legend legend = lineChart.getLegend();
        legend.setTextSize(16);
        legend.setFormSize(16);

        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())){
                    fetch(inputText.getText().toString());
                    lineChart.invalidate();
//                    return true;
                }
                return true;
            }
        });

        return root;
    }

    private void fetch(final String input){
//        String url = "http://10.0.2.2:4000/trending/?id=" + input;
        String url = "http://newsapp-zpzzpz.us-east-1.elasticbeanstalk.com/trending/?id=" + input;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray data = response.getJSONArray("data");
                    int len = data.length();
                    final String[] x = new String[len];
                    List<Entry> entries = new ArrayList<>();
                    for(int i = 0; i < len; i++){
                        x[i] = String.valueOf(i);
                        Entry entry = new Entry(i, data.getInt(i));
                        entries.add(entry);
                    }
                    LineDataSet lineDataSet = new LineDataSet(entries, "Trending Chart for " + input);
                    LineData lineData = new LineData(lineDataSet);
                    lineDataSet.setDrawCircleHole(false);

                    int color = Color.rgb(54, 0, 177);
                    lineDataSet.setCircleColor(color);
                    lineDataSet.setColor(color);
                    lineData.setValueTextColor(color);
//                    Log.d("data", String.valueOf(lineDataSet));
                    lineChart.setData(lineData);


                    ValueFormatter formatter = new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            return x[(int) value];
                        }
                    };
                    XAxis xAxis = lineChart.getXAxis();
                    YAxis leftYAxis = lineChart.getAxisLeft();
                    YAxis rightYAxis = lineChart.getAxisRight();
                    xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
                    xAxis.setValueFormatter(formatter);
                    xAxis.setAxisLineColor(Color.rgb(220,220,220));
                    xAxis.setAxisLineWidth(1);
                    xAxis.setDrawGridLines(false);
                    leftYAxis.setDrawAxisLine(false);
                    leftYAxis.setDrawGridLines(false);
                    rightYAxis.setDrawGridLines(false);

                    lineChart.invalidate();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("chartError", String.valueOf(error));
            }
        });

        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }





}
