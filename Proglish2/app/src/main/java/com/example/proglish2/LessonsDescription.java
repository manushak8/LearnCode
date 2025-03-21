package com.example.proglish2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LessonsDescription extends AppCompatActivity {
    private TextView lessonNameTextView, wordTextView;
    private LinearLayout next_button;
    private FirebaseFirestore db;
    private List<String> wordsList = new ArrayList<>();
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_description);

        db = FirebaseFirestore.getInstance();
        next_button = findViewById(R.id.next_button);
        lessonNameTextView = findViewById(R.id.lessonName);
        wordTextView = findViewById(R.id.lessonDescription);

        loadDetails();

        next_button.setOnClickListener(v -> showNextWord());
    }

    private void loadDetails() {
        String lessonID = getIntent().getStringExtra("lessonID");

        db.collection("Lessons")
                .whereEqualTo("id", lessonID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String lessonName = document.getString("name");
                            if (lessonName != null) {
                                lessonNameTextView.setText(lessonName);
                            }

                            for (int i = 1; i <= 10; i++) {
                                String description = document.getString("description" + i);
                                if (description != null) {
                                    String formattedText = description.replaceAll("([.!?]) ", "$1\n");

                                    wordsList.add(formattedText);
                                }
                            }
                            if (!wordsList.isEmpty()) {
                                wordTextView.setText(wordsList.get(0));
                            }
                        }
                    }
                });
    }

    private void showNextWord() {
        if (currentIndex < wordsList.size() - 1) {
            currentIndex++;
            wordTextView.setText(wordsList.get(currentIndex));
        } else {
            Intent intent = new Intent(LessonsDescription.this, QuizActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
