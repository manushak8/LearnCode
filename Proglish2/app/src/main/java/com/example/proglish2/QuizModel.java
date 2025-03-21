package com.example.proglish2;

public class QuizModel {
    String question;
    String oA;
    String oB;
    String oC;
    String oD;
    String answer;

    public QuizModel(String question, String oA, String oB, String oC, String oD, String answer) {
        this.question = question;
        this.oA = oA;
        this.oB = oB;
        this.oC = oC;
        this.oD = oD;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }
    public String getoA() {
        return oA;
    }

    public String getoB() {
        return oB;
    }

    public String getoC() {
        return oC;
    }

    public String getoD() {
        return oD;
    }
    public String getAnswer() {
        return answer;
    }

}
