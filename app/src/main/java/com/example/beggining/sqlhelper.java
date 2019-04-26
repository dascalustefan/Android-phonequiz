package com.example.beggining;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class sqlhelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Scores.db";
    private static final int DATABASE_VERSION = 3;
    private SQLiteDatabase db;
    public sqlhelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db=getWritableDatabase();
    }
    boolean verifynickname(String nickname)
    {
        Cursor c = db.rawQuery("SELECT * FROM USERS" + " WHERE NICKNAME = '"+ nickname +"' ", null);
        if (c.moveToFirst()) {
            return true;
        }
        else
            return false;


    }
    String getnickname(String email,String password)
    {
        Cursor c = db.rawQuery("SELECT * FROM " + "USERS WHERE EMAIL = '"+ email+"' AND PASSWORD = '"+ password+"'", null);
        if (c.moveToFirst()) {
            String result=c.getString(c.getColumnIndex("NICKNAME"));
            return result;
        }
        return null;
    }
    boolean verifycredentials(String email,String password)
    {
        Cursor c = db.rawQuery("SELECT * FROM " + "USERS WHERE EMAIL ='"+ email+"' AND PASSWORD = '"+ password+"'", null);
        if (c.moveToFirst()) {
            return false;
        }
        return true;
    }
    boolean addperson(String nickname,String email,String password)
    {

        ContentValues cv = new ContentValues();
        cv.put("EMAIL", email);
        cv.put("PASSWORD", password);
        cv.put("NICKNAME",nickname);

        Cursor c = db.rawQuery("SELECT * FROM " + "USERS WHERE EMAIL = '"+ email+"' ", null);

        if (c.moveToFirst()) {
            return false;
        }

        c.close();

        long error=db.insert("USERS",null,cv);
        if(error>0)
        return true;
        else
            return false;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        final String SQL_CREATE_Answers_TABLE = "CREATE TABLE " +
                "QUIZSCORES" + " ( " +
                "Quizname" + " TEXT PRIMARY KEY , " +
                "Date_Taken" + " TEXT, " +
                "Numbers_Taken" + " INTEGER, " +
                "Right" + " INTEGER, " +
                "Wrong" + " INTEGER," +
                "FILENAME" + " TEXT UNIQUE," +
                "IP" + " TEXT" +
                ")";

       /* db.execSQL(SQL_CREATE_Answers_TABLE);
        final String SQL_CREATE_QUIZ_TABLE = "CREATE TABLE " +
                "QUIZZES" + " ( " +
                "FILENAMES" + " TEXT PRIMARY KEY, " +
                "IP" + " TEXT  " +

                ")";*/

        db.execSQL(SQL_CREATE_Answers_TABLE);
        final String SQL_CREATE_PEOPLE_TABLE = "CREATE TABLE " +
                "USERS" + " ( " +
                "EMAIL" + " TEXT PRIMARY KEY , " +
                "PASSWORD" + " TEXT, " +
                "NICKNAME" + " TEXT UNIQUE " +
                ")";
        db.execSQL(SQL_CREATE_PEOPLE_TABLE);
        //fillQuizzTable("10.0.2.2","end.json");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "QUIZSCORES");
        db.execSQL("DROP TABLE IF EXISTS " + "USERS");
        onCreate(db);
    }
    /*private void fillQuizzTable(String ip,String file) {
        Date a= Date.valueOf("1971-11-29");
        Quizanswer q1 = new Quizanswer("trial",0,Date.valueOf("1971-11-29"),0,0,"10.0.2.2","end.json");
        addQUIZ(q1);
        /*
        Question q2 = new Question("B is correct", "A", "B", "C", 2);
        addQuestion(q2);
        Question q3 = new Question("C is correct", "A", "B", "C", 3);
        addQuestion(q3);
        Question q4 = new Question("A is correct again", "A", "B", "C", 1);
        addQuestion(q4);
        Question q5 = new Question("B is correct again", "A", "B", "C", 2);
        addQuestion(q5);
    }*/

    void addQUIZ(Quizanswer question) {
        ContentValues cv = new ContentValues();
        cv.put("Quizname", question.getQuizname());
        cv.put("Date_Taken", question.getDatetaken().toString());
        cv.put("Numbers_Taken", question.getNumbers_Taken());
        cv.put("Right", question.getRight());
        cv.put("Wrong", question.getLeft());
        cv.put("IP", question.getIp());
        cv.put("FILENAME", question.getFilename());

        int row= (int) db.insert("QUIZSCORES", null, cv);
    }
    public List<Quizanswer> getAllQuizzes() {
        List<Quizanswer> quizlist= new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM " + "QUIZSCORES", null);

        if (c.moveToFirst()) {
            do {
                Quizanswer quiz = new Quizanswer();
                quiz.setQuizname(c.getString(c.getColumnIndex("Quizname")));
                quiz.setDatetaken(Date.valueOf(c.getString(c.getColumnIndex("Date_Taken"))));
                quiz.setLeft(c.getInt(c.getColumnIndex("Wrong")));
                quiz.setRight(c.getInt(c.getColumnIndex("Right")));
                quiz.setNumbers_Taken(c.getInt(c.getColumnIndex("Numbers_Taken")));
                quiz.setIp(c.getString(c.getColumnIndex("IP")));
                quiz.setFilename(c.getString(c.getColumnIndex("FILENAME")));
                quizlist.add(quiz);
            } while (c.moveToNext());
        }

        c.close();
        return quizlist;
    }
    public Quizanswer getquiz(String filename)
    {
        Quizanswer quiz=null;
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + "QUIZSCORES WHERE FILENAME='"+filename+"'" , null);

        if (c.moveToFirst()) {
                quiz = new Quizanswer();
                quiz.setQuizname(c.getString(c.getColumnIndex("Quizname")));
                quiz.setDatetaken(Date.valueOf(c.getString(c.getColumnIndex("Date_Taken"))));
                quiz.setLeft(c.getInt(c.getColumnIndex("Wrong")));
                quiz.setRight(c.getInt(c.getColumnIndex("Right")));
                quiz.setNumbers_Taken(c.getInt(c.getColumnIndex("Numbers_Taken")));
                quiz.setFilename(c.getString(c.getColumnIndex("FILENAME")));
                quiz.setIp(c.getString(c.getColumnIndex("IP")));

        }

        c.close();
        return quiz;
    }

    public void updatequiz(int score, int questionCountTotal, String newString) {
        Quizanswer a = getquiz(newString);

        Date now = new java.sql.Date(new java.util.Date().getTime());
        a.setDatetaken(now);
        if (score > a.getRight())
            a.setRight(score);
        if (a.getLeft() < questionCountTotal-a.getRight())
            a.setLeft(questionCountTotal-a.getRight());
        String sql="UPDATE QUIZSCORES SET Date_Taken = '" + a.getDatetaken().toString() + "' , Right =" + String.valueOf(a.getRight()) + " , Wrong=" + String.valueOf(a.getLeft()) + "  WHERE FILENAME='" + newString + "'";
        db.execSQL(sql);
         sql="UPDATE QUIZSCORES SET  Right = " + String.valueOf(a.getRight()) + " , Wrong = " + String.valueOf(a.getLeft()) + "  WHERE FILENAME='" + newString + "'";
        db.execSQL(sql);
        a = getquiz(newString);
        a.getRight();

    }
   /* public List<Question> getAllQuestions() {
        List<Quizanswer> quizlist= new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + "QUIZSCORES", null);

        if (c.moveToFirst()) {
            do {
                Quizanswer quiz = new Quizanswer();
                quiz.setQuizname(c.getString(c.getColumnIndex("Quizname")));
                quiz.setDatetaken(Date.valueOf(c.getString(c.getColumnIndex("Date_Taken"))));
                quiz.setLeft(c.getInt(c.getColumnIndex("Numbers_Taken")));
                quiz.setRight(c.getInt(c.getColumnIndex("Wrong")));
                quiz.setNumbers_Taken(c.getInt(c.getColumnIndex("Numbers_Taken")));
                quizlist.add(quiz);
            } while (c.moveToNext());
        }

        c.close();
        return quizlist;
    }*/
}
