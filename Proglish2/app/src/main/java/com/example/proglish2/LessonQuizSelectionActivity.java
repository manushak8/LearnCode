package com.example.proglish2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class LessonQuizSelectionActivity extends AppCompatActivity implements RecyclerViewInterface {

    ArrayList<LessonQuizItem> lessonQuizList = new ArrayList<>();

    RecyclerView recyclerView;
    LessonQuizAdapter adapter;
    FirebaseFirestore db;

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, dictionary, about, logout;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_quiz_selection);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        drawerLayout = findViewById(R.id.DrawerLayout);
        menu = findViewById(R.id.menu);
        about = findViewById(R.id.info);
        logout = findViewById(R.id.logOut);
        dictionary = findViewById(R.id.dictionary);
        home = findViewById(R.id.home);
        //bottomNavigationView = findViewById(R.id.BottomNavigationView);

        adapter = new LessonQuizAdapter(this, lessonQuizList, this);
        recyclerView.setAdapter(adapter);

        fetchLessonQuizData();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LessonQuizSelectionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LessonQuizSelectionActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        dictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LessonQuizSelectionActivity.this, Dictionary.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
                redirectToLogin();
            }
        });

        /*bottomNavigationView.setSelectedItemId(R.id.home);

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
        });*/
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
                                lessonQuizList.add(new LessonQuizHeader(lessonName));
                                lessonQuizList.add(new LessonQuizContent(lessonID, "Тема", "lesson"));
                                lessonQuizList.add(new LessonQuizContent(lessonID, "Викторина", "quiz"));
                                topicIndex++;
                            }
                        }
                        adapter.notifyDataSetChanged();
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

    private void redirectToLogin() {
        Intent intent = new Intent(getApplicationContext(), login.class);
        startActivity(intent);
        finish();
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

}