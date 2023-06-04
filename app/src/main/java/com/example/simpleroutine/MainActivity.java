package com.example.simpleroutine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView mainDateTitle;
    private TableLayout mainChecklistTable;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainDateTitle = findViewById(R.id.main_date_title);
        mainChecklistTable = findViewById(R.id.main_checklist_table);
        dbHelper = new DatabaseHelper(this);

        loadMainTab();

        Button settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        Button statsButton = findViewById(R.id.stats_button);
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMainTab();
    }

    private void loadMainTab() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        mainDateTitle.setText(currentDate);

        mainChecklistTable.removeAllViews();
        List<Step> steps = dbHelper.getSteps();
        List<History> history = dbHelper.getHistoryForToday();

        for (Step step : steps) {
            TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.step_item, null);
            TextView timeTextView = row.findViewById(R.id.time_text);
            TextView stepTextView = row.findViewById(R.id.step_text);
            CheckBox checkBox = row.findViewById(R.id.step_checkbox);

            stepTextView.setText(step.getText());

            for (History h : history) {
                if (h.getStepID() == step.getID()) {
                    checkBox.setChecked(true);
                    timeTextView.setText(h.getDatetime().substring(11, 16));
                    break;
                }
            }

            final int stepID = step.getID();
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        if (dbHelper.checkPreviousSteps(stepID)) {
                            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                            dbHelper.addHistory(new History(currentTime, stepID));
                            timeTextView.setText(currentTime.substring(11, 16));
                        } else {
                            checkBox.setChecked(false);
                        }
                    } else {
                        dbHelper.deleteHistoryForStep(stepID);
                        timeTextView.setText("");
                    }
                }
            });

            mainChecklistTable.addView(row);
        }
    }
}