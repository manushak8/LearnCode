package com.example.proglish2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;


public class WonActivity extends AppCompatActivity {

    CircularProgressBar circularProgressBar;
    TextView resultText, exit;
    int correct, wrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);
        getSupportActionBar().hide();

        circularProgressBar = findViewById(R.id.circularProgressBar);
        exit = findViewById(R.id.exit_ic);
        resultText = findViewById(R.id.resultText);
        correct = getIntent().getIntExtra("correct", 0);
        wrong = getIntent().getIntExtra("wrong", 0);

        circularProgressBar.setProgress(correct);
        resultText.setText(correct + "/10");

        exit.setOnClickListener(v -> {
            Intent intent = new Intent(WonActivity.this, LessonQuizSelectionActivity.class);
            startActivity(intent);
            finish();
        });


    }
}