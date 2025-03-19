package com.example.proglish2;

import static com.example.proglish2.SplashScreenActivity.listOfQuestions;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;



import java.util.Collections;
import java.util.List;


public class DashboardActivity extends AppCompatActivity {
    List<QuizModel> allQuestionsList;
    QuizModel quizModel;
    int index = 0;
    TextView card_question, optionA, optionB, optionC, optionD, exit;
    CardView cardOA, cardOB, cardOC, cardOD;
    ImageView backImageView;
    int correctCount = 0;
    int wrongCount = 0;
    LinearLayout nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dashboard);

        hooks();
        allQuestionsList = listOfQuestions;
        Collections.shuffle(allQuestionsList);
        quizModel = listOfQuestions.get(index);
        resetColor();
        nextBtn.setClickable(false);
        setAllData();

        exit.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, QuizActivity.class);
            startActivity(intent);
            finish();
        });

        backImageView.setOnClickListener(v -> {
            if (index > 0) {
                index--;
                quizModel = allQuestionsList.get(index);
                resetColor();
                setAllData();
                enableBtn();
            } else {
                finish();
            }
        });

    }

    private void setAllData() {
        card_question.setText(quizModel.getQuestion());
        optionA.setText(quizModel.getoA());
        optionB.setText(quizModel.getoB());
        optionC.setText(quizModel.getoC());
        optionD.setText(quizModel.getoD());
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
        backImageView = findViewById(R.id.back_ic);
    }

    public void correct(CardView selectedCard) {
        selectedCard.setCardBackgroundColor(getResources().getColor(R.color.green));
        correctCount++;
        nextBtn.setClickable(true);
    }

    public void wrong(CardView selectedCard) {
        selectedCard.setCardBackgroundColor(getResources().getColor(R.color.red));
        wrongCount++;

        if (quizModel.getoA().equals(quizModel.getAnswer())) {
            cardOA.setCardBackgroundColor(getResources().getColor(R.color.green));
        } else if (quizModel.getoB().equals(quizModel.getAnswer())) {
            cardOB.setCardBackgroundColor(getResources().getColor(R.color.green));
        } else if (quizModel.getoC().equals(quizModel.getAnswer())) {
            cardOC.setCardBackgroundColor(getResources().getColor(R.color.green));
        } else if (quizModel.getoD().equals(quizModel.getAnswer())) {
            cardOD.setCardBackgroundColor(getResources().getColor(R.color.green));
        }

        nextBtn.setClickable(true);
    }

    private void gameWon() {
        Intent intent = new Intent(DashboardActivity.this, WonActivity.class);
        intent.putExtra("correct", correctCount);
        intent.putExtra("wrong", wrongCount);
        startActivity(intent);

    }
    public void enableBtn(){
        cardOA.setClickable(true);
        cardOB.setClickable(true);
        cardOC.setClickable(true);
        cardOD.setClickable(true);
    }

    public void disableBtn(){
        cardOA.setClickable(false);
        cardOB.setClickable(false);
        cardOC.setClickable(false);
        cardOD.setClickable(false);
    }

    public void resetColor(){
        cardOA.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardOB.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardOC.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardOD.setCardBackgroundColor(getResources().getColor(R.color.white));
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

    public void optionClick(CardView selectedCard, String selectedAnswer) {
        disableBtn();
        nextBtn.setEnabled(true);
        nextBtn.setClickable(true);
        nextBtn.setAlpha(1f);

        if (selectedAnswer.equals(quizModel.getAnswer())) {
            correct(selectedCard);
        } else {
            wrong(selectedCard);
        }

        nextBtn.setOnClickListener(v -> {
            if (index < allQuestionsList.size() - 1) {
                index++;
                quizModel = allQuestionsList.get(index);
                resetColor();
                setAllData();
                enableBtn();
                nextBtn.setEnabled(false);
                nextBtn.setClickable(false);
                nextBtn.setAlpha(0.5f);
            } else {
                gameWon();
            }
        });
    }
}