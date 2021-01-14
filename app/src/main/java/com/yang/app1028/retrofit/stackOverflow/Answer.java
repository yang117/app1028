package com.yang.app1028.retrofit.stackOverflow;

import com.google.gson.annotations.SerializedName;

public class Answer {
    @SerializedName("answer_id")
    public int answer_id;
    @SerializedName("is_accepted")
    public boolean accepted;
    public int score;

    @Override
    public String toString() {
        return "Answer{" +
                "answer_id=" + answer_id +
                ", accepted=" + (accepted ? "YES" : "NO") +
                ", score=" + score +
                '}';
    }
}
