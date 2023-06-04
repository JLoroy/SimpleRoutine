package com.example.simpleroutine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.simpleroutine.DatabaseHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class StatsActivity extends AppCompatActivity {

    private BarChart statsChart;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        statsChart = findViewById(R.id.stats_chart);
        dbHelper = new DatabaseHelper(this);

        loadStatsTab();
    }

    private void loadStatsTab() {
        List<BarEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = generateColors();

        List<History> historyList = dbHelper.fetchHistory();
        ArrayList<ArrayList<Integer>> days = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            ArrayList<Integer> daySteps = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -i);
            Date currentDate = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateString = sdf.format(currentDate);

            for (History history : historyList) {
                if (history.getDatetime().startsWith(dateString)) {
                    daySteps.add(history.getMinutes());
                }
            }

            days.add(daySteps);
        }

        for (int i = 0; i < days.size(); i++) {
            ArrayList<Integer> daySteps = days.get(i);
            for (int j = 0; j < daySteps.size(); j++) {
                entries.add(new BarEntry(i, daySteps.get(j), colors.get(j % colors.size())));
            }
        }

        BarDataSet dataSet = new BarDataSet(entries, "Steps");
        dataSet.setColors(colors);
        BarData barData = new BarData(dataSet);
        statsChart.setData(barData);
        statsChart.invalidate();
    }

    private ArrayList<Integer> generateColors() {
        ArrayList<Integer> colors = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 50; i++) {
            int color = ColorTemplate.rgb(String.format("#%06X", random.nextInt(0xFFFFFF + 1)));
            colors.add(color);
        }

        return colors;
    }
}