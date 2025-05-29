package com.example.itEnglish;

public class LessonQuizContent extends LessonQuizItem {
    private String id;
    private String name;
    private String type;

    public LessonQuizContent(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public int getItemType() {
        return TYPE_CONTENT;
    }
}