package com.example.proglish2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class LessonsDescription extends AppCompatActivity {
    private TextView lessonNameTextView, lessonDescriptionTextView;
    private FirebaseFirestore db;

    ArrayList<LessonsModel> lessonsModels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_description);

        db = FirebaseFirestore.getInstance();
        ScrollView scrollView = findViewById(R.id.scroll);

        TextView explanationVariable = findViewById(R.id.explanation_variable);
        TextView explanationFunction = findViewById(R.id.explanation_function);
        TextView explanationMethod = findViewById(R.id.explanation_method);
        TextView explanationParameter = findViewById(R.id.explanation_parameter);
        TextView explanationArgument = findViewById(R.id.explanation_argument);
        TextView explanationReturn = findViewById(R.id.explanation_return);
        TextView explanationType = findViewById(R.id.explanation_type);
        TextView explanationClass = findViewById(R.id.explanation_class);
        TextView explanationObject = findViewById(R.id.explanation_object);
        TextView explanationInheritance = findViewById(R.id.explanation_inheritance);

        // Assigning explanations and examples to TextViews
        explanationVariable.setText("Переменная – это именованная область памяти, предназначенная для хранения данных.\n\nExample:\nint age = 25;  // переменная age хранит число 25\nString name = 'Алексей';  // переменная name хранит строку 'Алексей'");
        explanationFunction.setText("Функция – это блок кода, выполняющий определённую задачу.\n\nExample:\nstatic void greet() {\n    System.out.println('Привет, мир!');\n}\n\npublic static void main(String[] args) {\n    greet();  // вызов функции\n}");
        explanationMethod.setText("Метод – это функция, которая принадлежит классу или объекту.\n\nExample:\npublic class Person {\n    String name;\n\n    void sayHello() {\n        System.out.println('Привет, меня зовут ' + name);\n    }\n\n    public static void main(String[] args) {\n        Person p = new Person();\n        p.name = 'Анна';\n        p.sayHello();\n    }\n}");
        explanationParameter.setText("Параметр – это переменная, принимаемая функцией при вызове.\n\nExample:\nstatic void greet(String name) {\n    System.out.println('Привет, ' + name + '!');\n}\n\npublic static void main(String[] args) {\n    greet('Дмитрий');  // Передаём аргумент 'Дмитрий'\n}");
        explanationArgument.setText("Аргумент – это конкретное значение, передаваемое в параметр функции при вызове.\n\nExample:\ngreet('Анна');  // 'Анна' – аргумент");
        explanationReturn.setText("Return – это ключевое слово, используемое в функциях для возврата результата.\n\nExample:\nstatic int sum(int a, int b) {\n    return a + b;  // возвращаем сумму\n}\n\npublic static void main(String[] args) {\n    int result = sum(5, 3);\n    System.out.println('Сумма: ' + result);\n}");
        explanationType.setText("Тип данных – это характеристика, определяющая, какие данные может хранить переменная.\n\nExample:\nint number = 10;\ndouble price = 5.99;\nchar letter = 'A';\nboolean isJavaFun = true;");
        explanationClass.setText("Класс – это шаблон, по которому создаются объекты.\n\nExample:\npublic class Car {\n    String brand;\n    int speed;\n    void drive() {\n        System.out.println(brand + ' едет со скоростью ' + speed + ' км/ч');\n    }\n}\n\npublic class Main {\n    public static void main(String[] args) {\n        Car myCar = new Car();\n        myCar.brand = 'Toyota';\n        myCar.speed = 120;\n        myCar.drive();\n    }\n}");
        explanationObject.setText("Объект – это экземпляр класса, содержащий данные и методы для их обработки.\n\nExample:\nCar myCar = new Car();  // создаём объект класса Car");
        explanationInheritance.setText("Наследование – это механизм, позволяющий классу-наследнику получать свойства и методы родительского класса.\n\nExample:\nclass Animal {\n    void makeSound() {\n        System.out.println('Животное издает звук');\n    }\n}\n\nclass Dog extends Animal {\n    void bark() {\n        System.out.println('Собака лает');\n    }\n}\n\npublic class Main {\n    public static void main(String[] args) {\n        Dog myDog = new Dog();\n        myDog.makeSound();  // Метод родительского класса\n        myDog.bark();       // Метод класса Dog\n    }\n}");

        Button continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(LessonsDescription.this, QuizActivity.class);
            startActivity(intent);
            finish();
        });
        //String lessonID = getIntent().getStringExtra("lessonID");

        /*if (lessonID != null) {
            loadDetails();
        }else {
            Log.e("DocumentError", "No such document");
        }*/

    }

    /*private void loadDetails() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Lessons")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        lessonsModels.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String lessonName = document.getString("name");
                            String description = document.getString("description");
                            String lessonID = document.getString("lessonID");

                            if (lessonName != null && description != null) {
                                lessonsModels.add(new LessonsModel(lessonID, lessonName, description));
                            } else {
                                Log.e("Firestore", "Missing data for document: " + document.getId());
                            }
                        }

                    } else {
                        Log.e("Firestore", "Error getting lessons", task.getException());
                        Toast.makeText(this, "Error getting lessons.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadDetails(String lessonID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Lessons")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        lessonsModels.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String lessonName = document.getString("name");
                            String description = document.getString("description");
                            if (lessonName != null && description != null) {
                                lessonsModels.add(new LessonsModel(lessonName, description, lessonID));
                                lessonNameTextView.setText(lessonName);
                                lessonDescriptionTextView.setText(description);
                            }
                        }
                    }
                });
    }*/


}
