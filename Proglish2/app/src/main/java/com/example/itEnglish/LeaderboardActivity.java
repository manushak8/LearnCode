package com.example.itEnglish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardActivity extends AppCompatActivity {
    private RecyclerView leaderboardRecyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseUser user;
    DrawerLayout drawerLayout;
    ImageView menu, profileImage;
    LinearLayout home, dictionary, leaderboard, about, logout;
    TextView mail;
    private ActivityResultLauncher<String> pickImageLauncher;


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

        profileImage = findViewById(R.id.profileImage);

        Bitmap profileBitmap = loadProfileImageFromInternalStorage();
        if (profileBitmap != null) {
            profileImage.setImageBitmap(profileBitmap);
        }

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(uri);
                                Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);
                                inputStream.close();

                                if (originalBitmap != null) {
                                    int width = originalBitmap.getWidth();
                                    int height = originalBitmap.getHeight();
                                    int newEdge = Math.min(width, height);
                                    int xOffset = (width - newEdge) / 2;
                                    int yOffset = (height - newEdge) / 2;
                                    Bitmap squareBitmap = Bitmap.createBitmap(
                                            originalBitmap, xOffset, yOffset, newEdge, newEdge
                                    );

                                    float density = getResources().getDisplayMetrics().density;
                                    int sizePx = (int) (80 * density);

                                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                                            squareBitmap, sizePx, sizePx, true
                                    );
                                    profileImage.setImageBitmap(scaledBitmap);

                                    saveProfileImageToInternalStorage(scaledBitmap);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageLauncher.launch("image/*");
            }
        });

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

        db.collection("quizResults")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        leaderboardEntries.clear();
                        Map<String, LeaderboardEntry> userTotals = new HashMap<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String userEmail = document.getString("userEmail");
                            String userId = document.getString("userId");
                            Object scoreObj = document.get("score");
                            int score = 0;

                            if (scoreObj instanceof Long) {
                                score = ((Long) scoreObj).intValue();
                            } else if (scoreObj instanceof Double) {
                                score = ((Double) scoreObj).intValue();
                            } else if (scoreObj instanceof Integer) {
                                score = (Integer) scoreObj;
                            }

                            if (userId == null || userEmail == null) continue;

                            if (!userTotals.containsKey(userId)) {
                                userTotals.put(userId, new LeaderboardEntry(userEmail, score));
                            } else {
                                LeaderboardEntry entry = userTotals.get(userId);
                                entry.setScore(entry.getScore() + score);
                            }
                        }

                        leaderboardEntries.addAll(userTotals.values());

                        leaderboardEntries.sort((a, b) -> b.getScore() - a.getScore());

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

    // Նկարը պահելու ֆունկցիա
    private void saveProfileImageToInternalStorage(Bitmap bitmap) {
        try {
            FileOutputStream fos = openFileOutput("profile_image.png", MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Նկարը բեռնելու ֆունկցիա
    private Bitmap loadProfileImageFromInternalStorage() {
        try {
            FileInputStream fis = openFileInput("profile_image.png");
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            fis.close();
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}