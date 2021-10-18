package com.example.license;

import java.util.ArrayList;

public class Storage {

    public static ArrayList<Storage> qArr = new ArrayList<>();

    String subject;
    int num;
    String ques;
    String ex1;
    String ex2;
    String ex3;
    String ex4;
    String img;
    String ans;
    boolean isWrong;

    public Storage(String subject, int num, String ques, String ex1, String ex2, String ex3, String ex4, String img, String ans, boolean isWrong){
        this.subject = subject;
        this.num = num;
        this.ques = ques;
        this.ex1 = ex1;
        this.ex2 = ex2;
        this.ex3 = ex3;
        this.ex4 = ex4;
        this.img = img;
        this.ans = ans;
        this.isWrong = isWrong;
    }

}
