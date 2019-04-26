package com.example.beggining;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Quiz extends AppCompatActivity {

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private int score;
    private boolean answered;
    private List<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private long backPressedTime;
    private Quizanswer quiz;
    String newString;
    private static final long COUNTDOWN_IN_MILLIS = 30000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        questionList=new ArrayList<Question>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        score=0;
        Bundle extras=getIntent().getExtras();
        newString= extras.getString("fisier");
        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);
        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();
        String useful=null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getExternalFilesDir(null) + "/" + newString));
            Toast.makeText(this, Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+newString,Toast.LENGTH_LONG).show();

            String line = reader.readLine();

             useful=  line;
            while (line != null) {
                System.out.println(line);
                // read next line
                line = reader.readLine();
                if(line!=null)
                useful=useful+line;
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(useful);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            try {
                String test=jsonObj.getString("nume");
                JSONArray ja = (JSONArray) jsonObj.getJSONArray("intrebari");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jsonobject = ja.getJSONObject(i);
                    String tip = jsonobject.getString("tip");
                    if(tip.equals("intrebare")) {
                        String numele = jsonobject.getString("intrebare");
                        JSONArray variante = jsonobject.getJSONArray("variante");
                        String[] quest_var = new String[variante.length()];
                        for (int j = 0; j < variante.length(); j++) {
                            variante.getString(j);

                            quest_var[j] =variante.getString(j);

                        }
                        JSONArray raspunsuri = jsonobject.getJSONArray("raspunsuri");

                        int[] quest_rasp = new int[raspunsuri.length()];
                        for (int j = 0; j < raspunsuri.length(); j++) {
                            int varianta = raspunsuri.getInt(j);
                            quest_rasp[j] = varianta;

                        }
                        Question quest=new Question(numele,quest_rasp,quest_var);
                        questionList.add(quest);
                    }




                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }



        questionCountTotal = questionList.size();
        if(questionCountTotal==0)
            finish();


        showNextQuestion();

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(buttonConfirmNext.getContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }
        //quiz = dbHelper.getquiz(filename,ip);//this is the quizzz

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption(1));
            rb2.setText(currentQuestion.getOption(2));
            if(currentQuestion.getOption(3)==null)
            rb3.setText("");
            else
                rb3.setText(currentQuestion.getOption(3));


            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirmNext.setText("Confirm");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {
            finishQuiz();
        }
    }


    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewCountDown.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }
    private void checkAnswer() {
        answered = true;

        countDownTimer.cancel();

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

        if (currentQuestion.verifyanswer(answerNr)) {
            score++;
            textViewScore.setText("Score: " + score);
        }

        showSolution();
    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);


            if(currentQuestion.verifyanswer(1)) {
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is correct");
            }
        if(currentQuestion.verifyanswer(2)) {
            rb2.setTextColor(Color.GREEN);
            textViewQuestion.setText("Answer 2 is correct");
        }
        if(currentQuestion.verifyanswer(3)) {
            rb3.setTextColor(Color.GREEN);
            textViewQuestion.setText("Answer 3 is correct");
        }


        if (questionCounter < questionCountTotal) {
            buttonConfirmNext.setText("Next");
        } else {
            buttonConfirmNext.setText("Finish");
        }
    }

    private void finishQuiz() {
        sqlhelper dbHelper = new sqlhelper(this);
        dbHelper.updatequiz(score,questionCountTotal,newString);
        Intent resultIntent = new Intent(this,BeginQuiz.class);
        startActivity(resultIntent);
        setResult(RESULT_OK, resultIntent);
        finish();


    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }























}
