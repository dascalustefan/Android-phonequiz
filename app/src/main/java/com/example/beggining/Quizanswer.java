package com.example.beggining;

import java.io.File;
import java.sql.Date;

public class Quizanswer {
    public Quizanswer() {
    }

    public String getQuizname() {
        return quizname;
    }

    public void setQuizname(String quizname) {
        this.quizname = quizname;
    }

    private String quizname;
    private int right;
    private Date datetaken;
    private int left;
    private int Numbers_Taken;

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public String getFilename() {
        return Filename;
    }

    public void setFilename(String filename) {
        Filename = filename;
    }

    private String  Ip;
    private String  Filename;

    public Quizanswer( String quizname, int right, Date datetaken, int left, int numbers_Taken,String Ip,String Filename) {
        this.quizname = quizname;

        this.right = right;
        this.datetaken = datetaken;
        this.left = left;
        this.Ip=Ip;
        this.Filename= Filename;
        Numbers_Taken = numbers_Taken;
    }


    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public Date getDatetaken() {
        return datetaken;
    }

    public void setDatetaken(Date datetaken) {
        this.datetaken = datetaken;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getNumbers_Taken() {
        return Numbers_Taken;
    }

    public void setNumbers_Taken(int numbers_Taken) {
        Numbers_Taken = numbers_Taken;
    }
}
