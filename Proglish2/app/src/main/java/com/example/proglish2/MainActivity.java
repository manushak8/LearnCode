package com.example.proglish2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{

    ArrayList<LessonsModel> lessonsModels = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseUser user;
    Button button;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.button);
        user = auth.getCurrentUser();
        bottomNavigationView = findViewById(R.id.BottomNavigationView);
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        }
        else{
            if (user == null) {
                redirectToLogin();
            } else {
                if (user.isEmailVerified()) {
                    fetchLessonsFromFirestore();
                    bottomNavigationView.setSelectedItemId(R.id.home);

                    bottomNavigationView.setOnItemSelectedListener(item -> {
                        int itemId = item.getItemId();
                        if (itemId == R.id.home) {
                            return true;
                        } else if (itemId == R.id.quiz) {
                            startActivity(new Intent(getApplicationContext(), QuizActivity.class));
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

                } else {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                    redirectToLogin();
                }
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    startActivity(intent);
                    finish();
                    redirectToLogin();
                }
            });
        }
    }
    private void redirectToLogin() {
        Intent intent = new Intent(getApplicationContext(), login.class);
        startActivity(intent);
        finish();
    }

    private void fetchLessonsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Lessons")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        lessonsModels.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String lessonName = document.getString("name");
                            String description = document.getString("description");
                            String lessonID = document.getString("id");

                            if (lessonName != null && description != null) {
                                lessonsModels.add(new LessonsModel(lessonID, lessonName, description));
                            }
                        }

                        RecyclerViweAdapter adapter = new RecyclerViweAdapter(this, lessonsModels, this);
                        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));

                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        String lessonID = lessonsModels.get(position).getLessonID();
        if (lessonID != null) {
            Intent intent = new Intent(MainActivity.this, LessonsDescription.class);
            intent.putExtra("lessonID", lessonID);
            startActivity(intent);
        }
    }
}