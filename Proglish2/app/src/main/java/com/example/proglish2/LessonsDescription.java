package com.example.proglish2;

import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class LessonsDescription extends AppCompatActivity {
    private TextView lessonNameTextView, lessonDescriptionTextView;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_description);

        db = FirebaseFirestore.getInstance();
        lessonNameTextView = findViewById(R.id.lessonName);
        lessonDescriptionTextView = findViewById(R.id.lessonDescription);

        loadDetails();

    }

    private void loadDetails(){
        db = FirebaseFirestore.getInstance();
        String lessonID = getIntent().getStringExtra("lessonID");

        db.collection("Lessons")
                .whereEqualTo("id", lessonID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String lessonName = document.getString("name");
                            String description = document.getString("description");

                            if (lessonName != null && description != null) {
                                lessonNameTextView.setText(lessonName);
                                lessonDescriptionTextView.setText(description);
                            }
                        }
                    }
                });
    }

}