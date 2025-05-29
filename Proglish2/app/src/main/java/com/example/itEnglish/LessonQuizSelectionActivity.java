package com.example.itEnglish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class LessonQuizSelectionActivity extends AppCompatActivity implements RecyclerViewInterface {

    ArrayList<LessonQuizItem> lessonQuizList = new ArrayList<>();

    RecyclerView recyclerView;
    LessonQuizAdapter adapter;
    FirebaseFirestore db;
    DrawerLayout drawerLayout;
    ImageView menu, profileImage;
    LinearLayout home, dictionary, leaderboard, about, logout;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView mail;
    private ActivityResultLauncher<String> pickImageLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_quiz_selection);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        drawerLayout = findViewById(R.id.DrawerLayout);
        menu = findViewById(R.id.menu);
        about = findViewById(R.id.info);
        logout = findViewById(R.id.logOut);
        dictionary = findViewById(R.id.dictionary);
        leaderboard = findViewById(R.id.leaderboard);
        home = findViewById(R.id.home);
        mail = findViewById(R.id.userEmail);

        adapter = new LessonQuizAdapter(this, lessonQuizList, this);
        recyclerView.setAdapter(adapter);

        fetchLessonQuizData();

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

        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LessonQuizSelectionActivity.this, LeaderboardActivity.class);
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
                                LessonQuizHeader header = new LessonQuizHeader(lessonName);

                                lessonQuizList.add(header);
                                lessonQuizList.add(new LessonQuizContent(lessonID, "Тема", "lesson"));
                                lessonQuizList.add(new LessonQuizContent(lessonID, "Викторина", "quiz"));

                                // Fetch user's quiz result for this lesson
                                db.collection("quizResults")
                                        .whereEqualTo("userId", user.getUid())
                                        .whereEqualTo("quizId", "quiz" + lessonID)
                                        .get()
                                        .addOnSuccessListener(resultTask -> {
                                            int scorePercent = -1;

                                            if (!resultTask.isEmpty()) {
                                                DocumentSnapshot quizResult = resultTask.getDocuments().get(0);

                                                Long score = quizResult.getLong("score");
                                                int maxScore = 10;
                                                if (score != null && maxScore > 0) {
                                                    scorePercent = (int) ((score * 10) / maxScore);
                                                }
                                            } else {
                                                Log.d("QUIZ_PROGRESS", "NO RESULT FOUND for userId=" + user.getUid() + " quizId=" + lessonID);
                                            }

                                            header.setProgress(scorePercent);
                                            adapter.notifyDataSetChanged();
                                        });
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
                intent.putExtra("quizId", content.getId());
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
