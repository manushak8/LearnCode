package com.example.proglish2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LessonDetailActivity extends AppCompatActivity {

    TextView lessonDetailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_detail);

        lessonDetailText = findViewById(R.id.lessonDetailText);


        String lessonName = getIntent().getStringExtra("lesson");
        lessonDetailText.setText("Details for " + lessonName);
    }
}
