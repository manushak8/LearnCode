package com.example.proglish2;

public abstract class LessonQuizItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT = 1;

    public abstract int getItemType();
}