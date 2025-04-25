package com.example.proglish2;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{

    ArrayList<LessonsModel> lessonsModels = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseUser user;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, dictionary, about, logout;
    TextView mail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu);
        about = findViewById(R.id.info);
        logout = findViewById(R.id.logOut);
        dictionary = findViewById(R.id.dictionary);
        home = findViewById(R.id.home);
        mail = findViewById(R.id.userEmail);

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
                    mail.setText(user.getEmail());
                    fetchLessonsFromFirestore();

                } else {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                    redirectToLogin();
                }
            }

            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDrawer(drawerLayout);
                }
            });

            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recreate();
                }
            });

            about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(intent);
                }
            });

            dictionary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, Dictionary.class);
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
        }
    }
    private void redirectToLogin() {
        Intent intent = new Intent(getApplicationContext(), login.class);
        startActivity(intent);
        finish();
    }

    private void fetchLessonsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Modules")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        lessonsModels.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String lessonName = document.getString("name");
                            String lessonID = document.getString("id");

                            if (lessonName != null) {
                                lessonsModels.add(new LessonsModel(lessonID, lessonName));
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
        int startIndex = position * 3;
        Intent intent = new Intent(MainActivity.this, LessonQuizSelectionActivity.class);
        intent.putExtra("startIndex", startIndex);
        startActivity(intent);
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