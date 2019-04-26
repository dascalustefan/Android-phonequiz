package com.example.beggining;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Quizstarter extends AppCompatActivity {
    sqlhelper db;
    TextView highscores;
    TextView titles;
    String newString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizstarter);
        highscores=findViewById(R.id.text_view_highscore);
        titles=findViewById(R.id.Quiz_title);

        Bundle extras = getIntent().getExtras();
        newString= extras.getString("fisier");
        db= new sqlhelper(this);
        Quizanswer bee=db.getquiz(newString);
        int highscore=bee.getRight();
        highscores.setText(String.valueOf(highscore));
        titles.setText(newString);
        /*BufferedReader reader;
        String useful=null;
        try {
            reader = new BufferedReader(new FileReader(getExternalFilesDir(null) +"/"+newString));
            Toast.makeText(this,Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+newString,Toast.LENGTH_LONG).show();

            String line = reader.readLine();
            useful=useful+line;
            while (line != null) {
                System.out.println(line);
                // read next line
                line = reader.readLine();
                useful=useful+line;
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = new JSONObject(taken);
        String test=jsonObj.getString("nume");*/


    }
    public void start_quiz(View view)
    {
        Intent it = new Intent(this,Quiz.class);
        it.putExtra("fisier", ""+newString);


        startActivity(it);
    }
}
