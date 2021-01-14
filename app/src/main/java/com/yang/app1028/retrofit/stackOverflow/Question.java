package com.yang.app1028.retrofit.stackOverflow;

import com.google.gson.annotations.SerializedName;

public class Question {
    public String title;
    public String body;

    @SerializedName("question_id")
    public String question_id;

    @Override
    public String toString() {
        return (title);
//        return "Question{" +
//                "title='" + title + '\'' +
//                ", body='" + body + '\'' +
//                ", question_id='" + question_id + '\'' +
//                '}';
    }
}
