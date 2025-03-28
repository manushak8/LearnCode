package com.example.proglish2;

public class LessonQuizModel {
    private String id;
    private String name;
    private String type; // "lesson" or "quiz"

    public LessonQuizModel(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
}

