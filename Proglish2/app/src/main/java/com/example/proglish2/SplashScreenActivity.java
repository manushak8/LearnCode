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
                "Что такое 'Algorithm'?",
                "A) Структура данных",
                "B) Язык программирования",
                "C) Последовательность инструкций для выполнения задачи",
                "D) Тип ошибки",
                "C) Последовательность инструкций для выполнения задачи"
        ));

        listOfQuestions.add(new QuizModel(
                " Что делает API?",
                "A) Предоставляет способ для общения разных программных приложений",
                "B) Хранит данные",
                "C) Обрабатывает ошибки",
                "D) Отлаживает код",
                "A) Предоставляет способ для общения разных программных приложений"
        ));

        listOfQuestions.add(new QuizModel(
                "Коллекция разных типов данных",
                "A) Метод работает только с числами",
                "B) Коллекция элементов одного типа",
                "C) Коллекция элементов одного типа",
                "D) Функция, возвращающая несколько значений",
                "B) Коллекция элементов одного типа"
        ));

        listOfQuestions.add(new QuizModel(
                "Что значит 'Асинхронный' в программировании?",
                "A) Выходные данные функции",
                "B) Операции, которые происходят независимо и вне очереди",
                "C) Операции, которые происходят по очереди",
                "D) Операции, которые происходят одновременно",
                "B) Операции, которые происходят независимо и вне очереди"
        ));

        listOfQuestions.add(new QuizModel(
                "Что такое 'Атрибут' в программировании?",
                "A) Тип функции",
                "B) Значение, присвоенное переменной",
                "C) Свойство объекта или класса",
                "D) Метод для получения данных",
                "С) Свойство объекта или класса"
        ));

        listOfQuestions.add(new QuizModel(
                "Что такое 'Аутентификация'?",
                "A) Подтверждение личности пользователя",
                "B) Предоставление прав доступа пользователю",
                "C) Для хранения данных",
                "D) Для работы с сетью",
                "A) Подтверждение личности пользователя"
        ));

        listOfQuestions.add(new QuizModel(
                "Что такое 'Авторизация'?",
                "A) Подтверждение пароля",
                "B) Файл с кодом",
                "C) Предоставление доступа к ресурсам на основе прав",
                "D) Встроенная функция",
                "C) Предоставление доступа к ресурсам на основе прав"
        ));

        listOfQuestions.add(new QuizModel(
                "Чем занимается 'Бэкэнд' разработка?",
                "A) Пользовательским интерфейсом",
                "B) Логикой на сервере, базами данных и конфигурацией сервера",
                "C) Обработкой ввода пользователя",
                "D) Дизайном фронтенда",
                "B) Логикой на сервере, базами данных и конфигурацией сервера"
        ));

        listOfQuestions.add(new QuizModel(
                "Что такое 'Двоичная' система?",
                "A) Система, основанная на двух значениях: 0 и 1",
                "B) Тип языка программирования",
                "C) Функция для обработки ошибок",
                "D) Тип базы данных",
                "A) Система, основанная на двух значениях: 0 и 1"
        ));

        listOfQuestions.add(new QuizModel(
                "Что такое 'Булево' значение?",
                "A) Значение, которое хранит текст",
                "B) Значение, которое может быть истинным или ложным",
                "C) Значение, которое хранит текст",
                "D) Значение, которое хранит несколько условий",
                "B) Значение, которое может быть истинным или ложным"
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