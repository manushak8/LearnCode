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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{

    ArrayList<LessonsModel> lessonsModels = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseUser user;
    DrawerLayout drawerLayout;
    ImageView menu, profileImage;
    LinearLayout home, dictionary, leaderboard, about, logout;
    TextView mail;
    private ActivityResultLauncher<String> pickImageLauncher;

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
        leaderboard = findViewById(R.id.leaderboard);
        mail = findViewById(R.id.userEmail);
        profileImage = findViewById(R.id.profileImage);

        if (user == null){
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        }
        else{
            if (user == null) {
                redirectToLogin();
            } else {
                if ("individualproject2025@gmail.com".equals(user.getEmail()) || user.isEmailVerified()) {
                    mail.setText(user.getEmail());
                    fetchLessonsFromFirestore();

                } else {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                    redirectToLogin();
                }
            }

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

            leaderboard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
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