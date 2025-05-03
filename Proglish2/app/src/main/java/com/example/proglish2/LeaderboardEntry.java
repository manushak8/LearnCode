package com.example.proglish2;

public class LeaderboardEntry {
    private String userEmail;
    private int score;

    public LeaderboardEntry(String userEmail, int score) {
        this.userEmail = userEmail;
        this.score = score;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getScore() {
        return score;
    }
}
