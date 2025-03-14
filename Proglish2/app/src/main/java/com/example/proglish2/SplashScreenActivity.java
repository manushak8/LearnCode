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
                "Что такое variable в программированин?",
                "A) Тип данных",
                "B) Функция",
                "C) Именованная область памяти ",
                "D)  Операционная система",
                "C) Именованная область памяти "
        ));

        listOfQuestions.add(new QuizModel(
                " Для чего нужна функция(Function)?",
                "A) Для выполнения определённой задачи",
                "B) Для хранения данных",
                "C) Для работы с интернетом",
                "D) Для обработки изображений",
                "A) Для выполнения определённой задачи"
        ));

        listOfQuestions.add(new QuizModel(
                "Чем method отличается от function?",
                "A) Метод работает только с числами",
                "B) Метод принадлежит классу или объекту",
                "C) Метод используется только в Java",
                "D) Метод – это то же самое, что и переменная",
                "B) Метод принадлежит классу или объекту"
        ));

        listOfQuestions.add(new QuizModel(
                "Что такое parameter функции?",
                "A) Выходные данные функции",
                "B) Переменная, принимаемая функцией",
                "C) Описание функции",
                "D) Часть операционной системы",
                "B) Переменная, принимаемая функцией"
        ));

        listOfQuestions.add(new QuizModel(
                "Чем argument отличается от parameter?",
                "A) Аргумент всегда равен нулю",
                "B) Аргумент и параметр – это одно и то же",
                "C) Аргумент – это только строки",
                "D) Аргумент – это передаваемое значение параметру",
                "D) Аргумент – это передаваемое значение параметру"
        ));

        listOfQuestions.add(new QuizModel(
                "Для чего используется inheritance  в ООП?",
                "A) Для передачи свойств и методов от родительского класса",
                "B) Для удаления ненужных объектов",
                "C) Для хранения данных",
                "D) Для работы с сетью",
                "A) Для передачи свойств и методов от родительского класса"
        ));

        listOfQuestions.add(new QuizModel(
                ": Что такое Class  в ООП?",
                "A) Переменная",
                "B) Файл с кодом",
                "C) Шаблон для создания объектов",
                "D) Встроенная функция",
                "C) Шаблон для создания объектов"
        ));

        listOfQuestions.add(new QuizModel(
                " Что такое Object  в ООП?",
                "A) Функция",
                "B) Экземпляр класса",
                "C) Операционная система",
                "D)  Файл программы",
                "B) Экземпляр класса"
        ));

        listOfQuestions.add(new QuizModel(
                "Какова роль return в функции?",
                "A) Возвращает результат работы функции",
                "B) Останавливает выполнение программы",
                "C) Вызывает другую функцию",
                "D) Удаляет переменную",
                "A) Возвращает результат работы функции"
        ));

        listOfQuestions.add(new QuizModel(
                "Что определяет тип данных?",
                "A) Только имя переменной",
                "B) Формат и допустимые операции с данными",
                "C) Только размер переменной в памяти",
                "D) Только язык программирования",
                "B) Формат и допустимые операции с данными"
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