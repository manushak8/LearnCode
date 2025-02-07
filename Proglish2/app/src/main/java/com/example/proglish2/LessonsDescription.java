package com.example.proglish2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LessonsDescription extends AppCompatActivity {
    private TextView lessonNameTextView, lessonDescriptionTextView;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_description);

        String lessonID = getIntent().getStringExtra("lessonID");

        if (lessonID != null) {
            Log.d("LessonsDescription", "Received lesson ID: " + lessonID);

            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Lessons").document(lessonID);
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String lessonName = documentSnapshot.getString("name");
                    String description = documentSnapshot.getString("description");

                    if (lessonName != null && description != null) {
                        lessonNameTextView = findViewById(R.id.lessonName);
                        lessonDescriptionTextView = findViewById(R.id.lessonDescription);

                        lessonNameTextView.setText(lessonName);
                        lessonDescriptionTextView.setText(description);
                    } else {
                        Log.e("LessonsDescription", "Lesson name or description is null.");
                        Toast.makeText(this, "Missing lesson details", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("LessonsDescription", "Document does not exist!");
                    Toast.makeText(this, "Lesson not found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Log.e("LessonsDescription", "Error getting document", e);
                Toast.makeText(this, "Error loading lesson", Toast.LENGTH_SHORT).show();
            });

        } else {
            Log.e("LessonsDescription", "No lesson ID received!");
            Toast.makeText(this, "No lesson ID received!", Toast.LENGTH_SHORT).show();
        }
    }
}