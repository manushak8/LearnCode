package com.example.itEnglish;

public class LessonQuizHeader extends LessonQuizItem {
    private String title;
    private int progress;
    public LessonQuizHeader(String title) {
        this.title = title;
        this.progress = -1;
    }
    public String getTitle() {
        return title;
    }
    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }
    @Override
    public int getItemType() {
        return TYPE_HEADER;
    }
}
