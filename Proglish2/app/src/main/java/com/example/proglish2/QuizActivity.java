package com.example.proglish2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class QuizActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    RecyclerView categoryRecyclerView;
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryList = new ArrayList<>();
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_quiz);

        db = FirebaseFirestore.getInstance();

        // RecyclerView կարգավորում
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoryRecyclerView.setAdapter(categoryAdapter);

        loadCategories();

        bottomNavigationView = findViewById(R.id.BottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.quiz);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_rigth, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.quiz) {
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

    private void loadCategories() {
        db.collection("Categories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        categoryList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String categoryName = document.getString("categoryName");
                            if (categoryName != null) {
                                categoryList.add(new CategoryModel(categoryName));
                            }
                        }
                        categoryAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirebaseError", "Error getting categories: ", task.getException());
                    }
                });
    }
}