package com.example.proglish2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class SplashScreenActivity extends AppCompatActivity {

    public static ArrayList<QuizModel> listOfQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);

        listOfQuestions = new ArrayList<>();
        listOfQuestions.add(new QuizModel(
                "What does 'API' stand for?",
                "A) Application Programming Interface",
                "B) Advanced Program Integration",
                "C) Automated Process Interaction",
                "D) Application Process Interface",
                "A) Application Programming Interface"
        ));

        listOfQuestions.add(new QuizModel(
                "Which data structure uses LIFO (Last In, First Out)?",
                "A) Queue",
                "B) Stack",
                "C) Linked List",
                "D) Array",
                "B) Stack"
        ));

        listOfQuestions.add(new QuizModel(
                "What does 'IDE' stand for in software development?",
                "A) Integrated Development Environment",
                "B) Internal Debugging Engine",
                "C) Intelligent Data Execution",
                "D) Interactive Design Editor",
                "A) Integrated Development Environment"
        ));

        listOfQuestions.add(new QuizModel(
                "Which SQL command is used to retrieve data from a database?",
                "A) INSERT",
                "B) UPDATE",
                "C) DELETE",
                "D) SELECT",
                "D) SELECT"
        ));

        listOfQuestions.add(new QuizModel(
                "What is the primary function of a compiler?",
                "A) To execute code directly",
                "B) To translate code into machine language",
                "C) To debug software",
                "D) To manage memory allocation",
                "B) To translate code into machine language"
        ));

        listOfQuestions.add(new QuizModel(
                "What does 'OOP' stand for in programming?",
                "A) Object-Oriented Programming",
                "B) Online Operational Processing",
                "C) Optimized Object Parsing",
                "D) Overloaded Operator Processing",
                "A) Object-Oriented Programming"
        ));

        listOfQuestions.add(new QuizModel(
                "Which of the following is NOT a programming language?",
                "A) Python",
                "B) JavaScript",
                "C) HTML",
                "D) C++",
                "C) HTML"
        ));

        listOfQuestions.add(new QuizModel(
                "What is the purpose of a version control system like Git?",
                "A) To run multiple programs at once",
                "B) To manage changes in source code",
                "C) To speed up code execution",
                "D) To check for syntax errors",
                "B) To manage changes in source code"
        ));

        listOfQuestions.add(new QuizModel(
                "Which of the following is a frontend JavaScript framework?",
                "A) React",
                "B) Django",
                "C) Flask",
                "D) Spring",
                "A) React"
        ));

        listOfQuestions.add(new QuizModel(
                "What is the main purpose of a database index?",
                "A) To store backup copies",
                "B) To speed up search queries",
                "C) To increase data security",
                "D) To reduce storage space",
                "B) To speed up search queries"
        ));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, login.class);
                startActivity(intent);
            }
        }, 1500);
    }
}