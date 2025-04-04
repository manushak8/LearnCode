package com.example.proglish2;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class LessonQuizSelectionActivity extends AppCompatActivity implements RecyclerViewInterface {

    ArrayList<LessonQuizItem> lessonQuizList = new ArrayList<>();

    RecyclerView recyclerView;
    LessonQuizAdapter adapter;
    FirebaseFirestore db;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_quiz_selection);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bottomNavigationView = findViewById(R.id.BottomNavigationView);

        adapter = new LessonQuizAdapter(this, lessonQuizList, this);
        recyclerView.setAdapter(adapter);

        fetchLessonQuizData();

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_rigth, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.quiz) {
                startActivity(new Intent(getApplicationContext(), Dictionary.class));
                overridePendingTransition(R.anim.slide_in_rigth, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.settings) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                overridePendingTransition(R.anim.slide_in_rigth, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }

    private void fetchLessonQuizData() {
        int startIndex = getIntent().getIntExtra("startIndex", 0);

        db.collection("Lessons")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        lessonQuizList.clear();
                        ArrayList<QueryDocumentSnapshot> allDocuments = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            allDocuments.add(document);
                        }

                        int endIndex = Math.min(startIndex + 3, allDocuments.size());

                        int topicIndex = 1;

                        for (int i = startIndex; i < endIndex; i++) {
                            QueryDocumentSnapshot document = allDocuments.get(i);
                            String lessonID = document.getString("id");
                            String lessonName = document.getString("name");

                            if (lessonID != null && lessonName != null) {
                                lessonQuizList.add(new LessonQuizHeader("Topic " + topicIndex));
                                lessonQuizList.add(new LessonQuizContent(lessonID, lessonName, "lesson"));
                                lessonQuizList.add(new LessonQuizContent(lessonID, "Quiz: " + lessonName, "quiz"));
                                topicIndex++;
                            }
                        }
                    }
                });
    }


    @Override
    public void onItemClick(int position) {
        LessonQuizItem item = lessonQuizList.get(position);
        if (item.getItemType() == LessonQuizItem.TYPE_CONTENT) {
            LessonQuizContent content = (LessonQuizContent) item;
            Intent intent;

            if (content.getType().equals("lesson")) {
                intent = new Intent(this, LessonsDescription.class);
                intent.putExtra("lessonID", content.getId());
            } else {
                intent = new Intent(this, DashboardActivity.class);
            }

            startActivity(intent);
        }
    }

}
