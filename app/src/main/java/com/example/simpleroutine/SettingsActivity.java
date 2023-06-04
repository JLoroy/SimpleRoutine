package com.example.simpleroutine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private RecyclerView settingsStepsList;
    private StepsAdapter stepsAdapter;
    private Button settingsAddButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dbHelper = new DatabaseHelper(this);
        settingsStepsList = findViewById(R.id.settings_steps_list);
        settingsAddButton = findViewById(R.id.settings_add_button);

        loadSettingsTab();

        settingsAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStep();
            }
        });
    }

    private void loadSettingsTab() {
        List<Step> steps = dbHelper.getSteps();
        stepsAdapter = new StepsAdapter(steps, this);
        settingsStepsList.setAdapter(stepsAdapter);
        settingsStepsList.setLayoutManager(new LinearLayoutManager(this));
        stepsAdapter.setOnStepChangeListener(new StepsAdapter.OnStepChangeListener() {
            @Override
            public void onStepOrderChanged(List<Step> updatedSteps) {
                updateOrder(updatedSteps);
            }

            @Override
            public void onStepEdited(Step step) {
                editStep(step);
            }
        });
    }

    private void updateOrder(List<Step> updatedSteps) {
        dbHelper.updateStepsOrder(updatedSteps);
    }

    private void addStep() {
        Step newStep = dbHelper.addStep();
        stepsAdapter.addStep(newStep);
    }

    private void editStep(Step step) {
        dbHelper.updateStep(step);
    }
}