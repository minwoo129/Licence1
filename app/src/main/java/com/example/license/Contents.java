package com.example.license;

public class Contents {
    String ltitle;
    String mtitle;
    String stitle;
    String content;


    String date;
    String startDay;
    String endDay;
    String testStDay;
    String testEndDay;
    String passStDay;
    String passEndDay;
    int num;




    public Contents(String ltitle, String mtitle, String stitle, String content, int num) {
        this.ltitle = ltitle;
        this.mtitle = mtitle;
        this.stitle = stitle;
        this.content = content;
        this.num = num;
    }

    public Contents(String date, String startDay, String endDay, String testStDay, String testEndDay, String passStDay, String passEndDay, int num) {
        this.date = date;
        this.startDay = startDay;
        this.endDay = endDay;
        this.testStDay = testStDay;
        this.testEndDay = testEndDay;
        this.passStDay = passStDay;
        this.passEndDay = passEndDay;
        this.num = num;
    }


}
