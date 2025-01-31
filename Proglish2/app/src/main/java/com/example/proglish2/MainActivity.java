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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{

    ArrayList<LessonsModel> lessonsModels = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseUser user;
    Button button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.button);
        user = auth.getCurrentUser();
        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        }
        else{
            //textView.setText(user.getEmail());
            if (user == null) {
                redirectToLogin();
            } else {
                if (user.isEmailVerified()) {
                    setUpLessonsModels();
                    RecyclerViweAdapter adapter = new RecyclerViweAdapter(this, lessonsModels, this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
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


    private void setUpLessonsModels() {
        String[] names = getResources().getStringArray(R.array.lessons);
        for (int i = 0; i < names.length; i++) {
            lessonsModels.add(new LessonsModel(names[i]));
        }
    }
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, LessonsDescription.class);

        LessonsModel selectedLesson = lessonsModels.get(position);
        intent.putExtra("lesson", selectedLesson);
        startActivity(intent);

    }
}

