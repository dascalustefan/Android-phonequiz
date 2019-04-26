package com.example.beggining;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Progress extends AppCompatActivity {
    sqlhelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        db= new sqlhelper(this);
        List<Quizanswer> list=  db.getAllQuizzes();
        ArrayList<Quizanswer> array=new ArrayList<Quizanswer>(list);
        customadapterprogres adapter = new customadapterprogres(array,this,R.layout.item2);

        ListView lv = findViewById(R.id.progress_list);
        lv.setAdapter(adapter);
    }
}
