package com.example.proglish2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Dictionary extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dictionary);

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Word> wordList = new ArrayList<>();
        WordAdapter adapter = new WordAdapter(wordList);

        RecyclerView recyclerView = findViewById(R.id.RecyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        db.collection("dictionary")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String word = doc.getId();
                        String translation = doc.getString("translation");
                        String example = doc.getString("example");

                        wordList.add(new Word(word, translation, example));
                    }
                    adapter.notifyDataSetChanged();
                });

        EditText searchEditText = findViewById(R.id.searchEditText);
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