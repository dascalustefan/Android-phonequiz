package com.example.beggining;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Question {
    private String question;
    int type=0;
    String [] variant;
    int [] answerNr;

    public void setType(int type) {
        this.type = type;
    }
    public boolean verifyanswer(int answer)
    {
        for(int i=0;i<answerNr.length;i++)
        {
            if (answer== answerNr[i])
                return true;

        }

        return false;
    }
    public int getType() {
        return type;
    }
    public String getOption(int order) {
        if(order>=variant.length)
            return null;
        return variant[order];
    }

    public Question() {
    }
    public Question(String question) {
        this.question=question;
    }


    public Question(String question,int[] answerNr,String[] variant) {
        this.question = question;
        this.answerNr = answerNr;
        this.variant=variant;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public int[] getAnswerNr() {
        return answerNr;
    }
    public String[] getVariant() {
        return variant;
    }
}