package com.example.proglish2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AboutActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_about);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        about = findViewById(R.id.info);
        logout = findViewById(R.id.logOut);
        dictionary = findViewById(R.id.dictionary);
        leaderboard = findViewById(R.id.leaderboard);
        home = findViewById(R.id.home);
        mail = findViewById(R.id.userEmail);


        // Գտնում ենք drawer-ի վերևի պրոֆիլի նկարի ImageView-ը
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

        // --------------------------------------------

        mail.setText(user.getEmail());

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        dictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this, Dictionary.class);
                startActivity(intent);
            }
        });

        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this, LeaderboardActivity.class);
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