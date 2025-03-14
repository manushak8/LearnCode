package com.example.proglish2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class LessonsDescription extends AppCompatActivity {
    private TextView lessonNameTextView, lessonDescriptionTextView;
    private FirebaseFirestore db;

    ArrayList<LessonsModel> lessonsModels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_description);

        db = FirebaseFirestore.getInstance();
        lessonNameTextView = findViewById(R.id.lessonName);
        lessonDescriptionTextView = findViewById(R.id.lessonDescription);

        String lessonID = getIntent().getStringExtra("lessonID");

        if (lessonID != null) {
            loadDetails();
        }else {
            Log.e("DocumentError", "No such document");
        }

    }

    private void loadDetails() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Lessons")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        lessonsModels.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String lessonName = document.getString("name");
                            String description = document.getString("description");
                            String lessonID = document.getString("lessonID");

                            if (lessonName != null && description != null) {
                                lessonsModels.add(new LessonsModel(lessonID, lessonName, description));
                            } else {
                                Log.e("Firestore", "Missing data for document: " + document.getId());
                            }
                        }

                    } else {
                        Log.e("Firestore", "Error getting lessons", task.getException());
                        Toast.makeText(this, "Error getting lessons.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*private void loadDetails(String lessonID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Lessons")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        lessonsModels.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String lessonName = document.getString("name");
                            String description = document.getString("description");
                            if (lessonName != null && description != null) {
                                lessonsModels.add(new LessonsModel(lessonName, description, lessonID));
                                lessonNameTextView.setText(lessonName);
                                lessonDescriptionTextView.setText(description);
                            }
                        }
                    }
                });
    }*/


}
