package com.example.dummyquiz;

import android.widget.RadioButton;

import java.util.List;

public class Datamodel {
    String question;
    String optionsall;
    int correct_option;
    boolean bookmark;
    boolean answered;
    int buttonID;
    List<String> options;
    String exact_ans;


    public Datamodel(String question, int correct_option, boolean bookmark, boolean answered, List<String> options, String optionsall, int radioButton,String exact_ans) {

        this.question = question;
        this.correct_option = correct_option;
        this.bookmark = bookmark;
        this.answered = answered;
        this.options = options;
        this.optionsall = optionsall;
        this.buttonID = radioButton;
        this.exact_ans = exact_ans;
    }


//Getters and setters implementation below.
    public String getExact_ans() {
        return exact_ans;
    }

    public String getOptionsall() {
        return optionsall;
    }

    public List<String> getOptions() {
        return options;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    public String getQuestion() {
        return question;
    }

    public int getButtonID() {
        return buttonID;
    }

    public void setButtonID(int buttonID) {
        this.buttonID = buttonID;
    }
}

