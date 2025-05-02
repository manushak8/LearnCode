package com.example.proglish2;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    List<QuizModel> allQuestionsList = new ArrayList<>();
    QuizModel quizModel;
    int index = 0;
    int correctCount = 0;
    int wrongCount = 0;

    TextView card_question, optionA, optionB, optionC, optionD, exit;
    CardView cardOA, cardOB, cardOC, cardOD;
    LinearLayout nextBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dashboard);

        hooks();

        /*String quizId = getIntent().getStringExtra("quizId");
        if (quizId != null) {
            fetchQuestionsFromFirebase();
        } else {
            Toast.makeText(this, "Quiz name is missing", Toast.LENGTH_SHORT).show();
            finish();
        }*/

        fetchQuestionsFromFirebase("quiz1");

        exit.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, LessonQuizSelectionActivity.class));
            finish();
        });
    }

    private void fetchQuestionsFromFirebase(String quizId) {
        db.collection("quizzes")
                .document(quizId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        allQuestionsList.clear();

                        List<Map<String, Object>> questions = (List<Map<String, Object>>) task.getResult().get("questions");

                        if (questions != null && !questions.isEmpty()) {
                            for (Map<String, Object> questionData : questions) {
                                String questionText = (String) questionData.get("question");
                                List<String> options = (List<String>) questionData.get("options");
                                Long correctIndexLong = (Long) questionData.get("correctAnswer");
                                int correctIndex = correctIndexLong != null ? correctIndexLong.intValue() : -1;

                                if (questionText != null && options != null && options.size() == 4 && correctIndex >= 0 && correctIndex < 4) {
                                    QuizModel question = new QuizModel(
                                            questionText,
                                            options.get(0),
                                            options.get(1),
                                            options.get(2),
                                            options.get(3),
                                            options.get(correctIndex)
                                    );
                                    allQuestionsList.add(question);
                                }
                            }

                            Collections.shuffle(allQuestionsList);

                            if (!allQuestionsList.isEmpty()) {
                                index = 0;
                                quizModel = allQuestionsList.get(index);
                                setAllData();
                            } else {
                                Toast.makeText(DashboardActivity.this, "Вопросов не найдено.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(DashboardActivity.this, "Тест не может быть загружен.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void setAllData() {
        if (quizModel != null) {
            card_question.setText(quizModel.getQuestion());
            optionA.setText(quizModel.getoA());
            optionB.setText(quizModel.getoB());
            optionC.setText(quizModel.getoC());
            optionD.setText(quizModel.getoD());
            resetColor();
            enableBtn();
            nextBtn.setEnabled(false);
            nextBtn.setClickable(false);
            nextBtn.setAlpha(0.5f);
        }
    }

    private void hooks() {
        card_question = findViewById(R.id.card_question);
        optionA = findViewById(R.id.card_answerA);
        optionB = findViewById(R.id.card_answerB);
        optionC = findViewById(R.id.card_answerC);
        optionD = findViewById(R.id.card_answerD);

        cardOA = findViewById(R.id.cardA);
        cardOB = findViewById(R.id.cardB);
        cardOC = findViewById(R.id.cardC);
        cardOD = findViewById(R.id.cardD);

        nextBtn = findViewById(R.id.next_btn);
        exit = findViewById(R.id.exit_ic);
    }

    public void optionAClick(View view) {
        optionClick(cardOA, quizModel.getoA());
    }

    public void optionBClick(View view) {
        optionClick(cardOB, quizModel.getoB());
    }

    public void optionCClick(View view) {
        optionClick(cardOC, quizModel.getoC());
    }

    public void optionDClick(View view) {
        optionClick(cardOD, quizModel.getoD());
    }

    private void optionClick(CardView selectedCard, String selectedAnswer) {
        disableBtn();
        nextBtn.setEnabled(true);
        nextBtn.setClickable(true);
        nextBtn.setAlpha(1f);

        if (selectedAnswer.equals(quizModel.getAnswer())) {
            selectedCard.setCardBackgroundColor(getResources().getColor(R.color.green));
            correctCount++;
        } else {
            selectedCard.setCardBackgroundColor(getResources().getColor(R.color.red));
            wrongCount++;
            highlightCorrectAnswer();
        }

        nextBtn.setOnClickListener(v -> {
            if (index < allQuestionsList.size() - 1) {
                index++;
                quizModel = allQuestionsList.get(index);
                setAllData();
            } else {
                Intent intent = new Intent(DashboardActivity.this, WonActivity.class);
                intent.putExtra("correct", correctCount);
                intent.putExtra("wrong", wrongCount);
                startActivity(intent);
                finish();
            }
        });
    }

    private void highlightCorrectAnswer() {
        if (quizModel.getoA().equals(quizModel.getAnswer())) {
            cardOA.setCardBackgroundColor(getResources().getColor(R.color.green));
        } else if (quizModel.getoB().equals(quizModel.getAnswer())) {
            cardOB.setCardBackgroundColor(getResources().getColor(R.color.green));
        } else if (quizModel.getoC().equals(quizModel.getAnswer())) {
            cardOC.setCardBackgroundColor(getResources().getColor(R.color.green));
        } else if (quizModel.getoD().equals(quizModel.getAnswer())) {
            cardOD.setCardBackgroundColor(getResources().getColor(R.color.green));
        }
    }

    private void enableBtn() {
        cardOA.setClickable(true);
        cardOB.setClickable(true);
        cardOC.setClickable(true);
        cardOD.setClickable(true);
    }

    private void disableBtn() {
        cardOA.setClickable(false);
        cardOB.setClickable(false);
        cardOC.setClickable(false);
        cardOD.setClickable(false);
    }

    private void resetColor() {
        cardOA.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardOB.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardOC.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardOD.setCardBackgroundColor(getResources().getColor(R.color.white));
    }
}