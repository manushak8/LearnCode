package com.example.proglish2;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    private RecyclerView leaderboardRecyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseUser user;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, dictionary, leaderboard, about, logout;
    TextView mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        drawerLayout = findViewById(R.id.DrawerLayout);
        menu = findViewById(R.id.menu);
        about = findViewById(R.id.info);
        logout = findViewById(R.id.logOut);
        dictionary = findViewById(R.id.dictionary);
        leaderboard = findViewById(R.id.leaderboard);
        home = findViewById(R.id.home);
        mail = findViewById(R.id.userEmail);

        mail.setText(user.getEmail());

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeaderboardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeaderboardActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        dictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeaderboardActivity.this, Dictionary.class);
                startActivity(intent);
            }
        });

        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
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

        leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        leaderboardAdapter = new LeaderboardAdapter(leaderboardEntries);
        leaderboardRecyclerView.setAdapter(leaderboardAdapter);

        fetchLeaderboardData();
    }

    private void fetchLeaderboardData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Leaderboard")
                .orderBy("score", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        leaderboardEntries.clear();

                        task.getResult().forEach(document -> {
                            String userEmail = document.getString("userEmail");
                            long totalScore = document.getLong("score");

                            leaderboardEntries.add(new LeaderboardEntry(userEmail, (int) totalScore));
                        });

                        leaderboardAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to load leaderboard.", Toast.LENGTH_SHORT).show();
                    }
                });
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