package com.example.proglish2;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class LessonQuizSelectionActivity extends AppCompatActivity implements RecyclerViewInterface {

    ArrayList<LessonQuizModel> lessonQuizList = new ArrayList<>();
    RecyclerView recyclerView;
    LessonQuizAdapter adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_quiz_selection);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LessonQuizAdapter(this, lessonQuizList, this);
        recyclerView.setAdapter(adapter);

        fetchLessonQuizData();
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

                        for (int i = startIndex; i < endIndex; i++) {
                            QueryDocumentSnapshot document = allDocuments.get(i);
                            String lessonID = document.getString("id");
                            String lessonName = document.getString("name");

                            if (lessonID != null && lessonName != null) {
                                lessonQuizList.add(new LessonQuizModel(lessonID, lessonName, "lesson"));
                                lessonQuizList.add(new LessonQuizModel(lessonID, "Quiz: " + lessonName, "quiz"));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    @Override
    public void onItemClick(int position) {
        LessonQuizModel item = lessonQuizList.get(position);
        Intent intent;

        if (item.getType().equals("lesson")) {
            intent = new Intent(this, LessonsDescription.class);
            intent.putExtra("lessonID", item.getId());
        } else {
            intent = new Intent(this, DashboardActivity.class);
        }

        startActivity(intent);
    }
}
