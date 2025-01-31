package com.example.proglish2;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LessonsDescription extends AppCompatActivity {
    private TextView lessonNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_description);

        lessonNumberTextView = findViewById(R.id.lessonNumberTextView);

        LessonsModel lesson = getIntent().getParcelableExtra("lesson");

        if (lesson != null) {
            lessonNumberTextView.setText(lesson.getLessonNumber());
        }
    }
}