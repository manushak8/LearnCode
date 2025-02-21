package com.example.proglish2;

import static com.example.proglish2.SplashScreenActivity.listOfQuestions;

import android.os.Bundle;

import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import java.util.Collections;
import java.util.List;


public class DashboardActivity extends AppCompatActivity {
    List<QuizModel> allQuestionsList;
    QuizModel quizModel;
    int index = 0;
    TextView card_question, optionA, optionB, optionC, optionD;
    CardView cardOA, cardOB, cardOC, cardOD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dashboard);
        
        Hooks();
        allQuestionsList = listOfQuestions;
        Collections.shuffle(allQuestionsList);
        quizModel = listOfQuestions.get(index);
        setAllData();
    }

    private void setAllData() {
        card_question.setText(quizModel.getQuestion());
        optionA.setText(quizModel.getoA());
        optionB.setText(quizModel.getoB());
        optionC.setText(quizModel.getoC());
        optionD.setText(quizModel.getoD());
    }

    private void Hooks() {
        card_question = findViewById(R.id.card_question);
        optionA = findViewById(R.id.card_answerA);
        optionB = findViewById(R.id.card_answerB);
        optionC = findViewById(R.id.card_answerC);
        optionD = findViewById(R.id.card_answerD);

        cardOA = findViewById(R.id.cardA);
        cardOB = findViewById(R.id.cardB);
        cardOC = findViewById(R.id.cardC);
        cardOD = findViewById(R.id.cardD);
    }
}