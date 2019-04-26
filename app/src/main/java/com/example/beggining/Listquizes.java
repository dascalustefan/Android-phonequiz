package com.example.beggining;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Listquizes extends AppCompatActivity {
    sqlhelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listquizes);
        db= new sqlhelper(this);
        List<Quizanswer> list=  db.getAllQuizzes();
        ArrayList<Quizanswer> array=new ArrayList<Quizanswer>(list);
        customadaperlistquiz adapter = new customadaperlistquiz(array,this,R.layout.item);

        ListView lv = findViewById(R.id.listview);
        lv.setAdapter(adapter);



    }
}
