package com.example.proglish2;

public class LessonQuizHeader extends LessonQuizItem {
    private String title;

    public LessonQuizHeader(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getItemType() {
        return TYPE_HEADER;
    }
}
