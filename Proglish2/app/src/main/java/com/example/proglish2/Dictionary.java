package com.example.proglish2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Dictionary extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, dictionary, about, logout;
    TextView mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dictionary);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        drawerLayout = findViewById(R.id.DrawerLayout);
        menu = findViewById(R.id.menu);
        about = findViewById(R.id.info);
        logout = findViewById(R.id.logOut);
        dictionary = findViewById(R.id.dictionary);
        home = findViewById(R.id.home);
        mail = findViewById(R.id.userEmail);
        RecyclerView recyclerView = findViewById(R.id.RecyclerView1);
        EditText searchEditText = findViewById(R.id.searchEditText);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Word> wordList = new ArrayList<>();
        WordAdapter adapter = new WordAdapter(wordList);

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
                Intent intent = new Intent(Dictionary.this, MainActivity.class);
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dictionary.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        dictionary.setOnClickListener(new View.OnClickListener() {
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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        db.collection("dictionary")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String word = doc.getId();
                        String translation = doc.getString("translation");
                        String example = doc.getString("example") != null ? doc.getString("example") : "";

                        wordList.add(new Word(word, translation, example));
                    }
                    adapter.notifyDataSetChanged();
                });


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString(), wordList, adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {}
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
    private void filterList(String query, List<Word> originalList, WordAdapter adapter) {
        List<Word> filteredList = new ArrayList<>();
        for (Word word : originalList) {
            if (word.getWord().toLowerCase().contains(query.toLowerCase()) ||
                    word.getTranslation().toLowerCase().contains(query.toLowerCase()) ||
                    word.getExample().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(word);
            }
        }
        adapter.updateList(filteredList);
    }

}