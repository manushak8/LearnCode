package com.example.proglish2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

        String lessonID = getIntent().getStringExtra("lessonID");

        if (lessonID != null) {
            loadUserDetails(lessonID);
        }else {
            Log.e("DocumentError", "No such document");
        }


        /*db.collection("Lessons").document(lessonID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String lessonName = document.getString("name");
                            String lessonDescription = document.getString("description");

                            lessonNameTextView.setText(lessonName);
                            lessonDescriptionTextView.setText(lessonDescription);
                        } else {
                            Toast.makeText(LessonsDescription.this, "No such document", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LessonsDescription.this, "Error getting document", Toast.LENGTH_SHORT).show();
                    }
                });*/

    }

    private void loadUserDetails(String lessonID) {
        DocumentReference docRef = db.collection("Lessons").document(lessonID);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                String description = documentSnapshot.getString("description");

                lessonNameTextView.setText(name);
                lessonDescriptionTextView.setText(description);
            }
        });
    }
}