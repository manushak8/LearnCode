package com.example.proglish2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class LeaderboardActivity extends AppCompatActivity {

    private TextView leaderboardTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        leaderboardTextView = findViewById(R.id.leaderboardTextView);

        fetchLeaderboardData();
    }

    private void fetchLeaderboardData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Leaderboard")
                .orderBy("score", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        ArrayList<String> leaderboardList = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String email = (String) document.get("userEmail");
                            Long score = (Long) document.get("score");

                            leaderboardList.add(email + ": " + score);
                        }

                        displayLeaderboard(leaderboardList);
                    }
                });
    }

    private void displayLeaderboard(ArrayList<String> leaderboardList) {
        StringBuilder leaderboardBuilder = new StringBuilder();

        for (String entry : leaderboardList) {
            leaderboardBuilder.append(entry).append("\n");
        }

        leaderboardTextView.setText(leaderboardBuilder.toString());
    }
}