package com.example.proglish2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import static android.graphics.Typeface.BOLD;

public class LessonsDescription extends AppCompatActivity {
    private TextView lessonNameTextView, wordTextView, exit, nextButtonText;
    private LinearLayout next_button;
    private FirebaseFirestore db;
    private List<CharSequence> wordsList = new ArrayList<>();
    private int currentIndex = 0;
    ImageView backImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_lessons_description);

        db = FirebaseFirestore.getInstance();
        next_button = findViewById(R.id.next_button);
        lessonNameTextView = findViewById(R.id.lessonName);
        wordTextView = findViewById(R.id.lessonDescription);
        exit = findViewById(R.id.exit_ic);
        backImageView = findViewById(R.id.back_ic);
        nextButtonText = findViewById(R.id.next_button_text);

        loadDetails();

        exit.setOnClickListener(v -> {
            Intent intent = new Intent(LessonsDescription.this, LessonQuizSelectionActivity.class);
            startActivity(intent);
            finish();
        });

        backImageView.setOnClickListener(v -> showPreviousWord());
        next_button.setOnClickListener(v -> showNextWord());
    }

    private void loadDetails() {
        String lessonID = getIntent().getStringExtra("lessonID");

        db.collection("Lessons")
                .whereEqualTo("id", lessonID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String lessonName = document.getString("name");
                            if (lessonName != null) {
                                lessonNameTextView.setText(lessonName);
                            }

                            for (int i = 1; i <= 10; i++) {
                                String description = document.getString("description" + i);
                                if (description != null) {
                                    String[] parts = description.split("Translation: ");
                                    if (parts.length >= 2) {
                                        String keyword = parts[0].trim();
                                        String[] subParts = parts[1].split("Example: ");
                                        String translation = "Перевод: " + subParts[0].trim();
                                        String example = subParts.length > 1 ? "Пример: " + subParts[1].trim() : "";

                                        SpannableStringBuilder formattedText = new SpannableStringBuilder();

                                        // Format keyword
                                        SpannableString keywordSpan = new SpannableString(keyword);
                                        keywordSpan.setSpan(new StyleSpan(BOLD), 0, keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        keywordSpan.setSpan(new RelativeSizeSpan(1.5f), 0, keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        keywordSpan.setSpan(new ForegroundColorSpan(Color.WHITE), 0, keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        formattedText.append(keywordSpan);
                                        formattedText.append("\n");
                                        formattedText.append(translation);
                                        formattedText.append("\n");
                                        formattedText.append(example);

                                        wordsList.add(formattedText);
                                    }
                                }
                            }
                            if (!wordsList.isEmpty()) {
                                wordTextView.setText(wordsList.get(0));
                            }
                        }
                    }
                });
    }

    private void showNextWord() {
        if (currentIndex < wordsList.size() - 1) {
            currentIndex++;
            wordTextView.setText((Spannable) wordsList.get(currentIndex));
            updateNextButtonText();
        } else {
            Intent intent = new Intent(LessonsDescription.this, LessonQuizSelectionActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void showPreviousWord() {
        if (currentIndex > 0) {
            currentIndex--;
            wordTextView.setText((Spannable) wordsList.get(currentIndex));
            updateNextButtonText();
        }
    }

    private void updateNextButtonText() {
        if (currentIndex == wordsList.size() - 1) {
            nextButtonText.setText("Закончить");
        } else {
            nextButtonText.setText("Продолжить");
        }
    }

}