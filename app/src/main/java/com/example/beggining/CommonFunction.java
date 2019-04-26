package com.example.beggining;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.net.URL;

public  class CommonFunction {
    public static boolean update(String address,String ip)
    {
        return true;
    }
    public static boolean notupdate()
    {
        return true;
    }
    private static boolean checkDataBase() {
       /* SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;*/
       return true;
    }
    public static String GetUserDatabase(Activity that) {

        SharedPreferences pref=that.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String dbpath=pref.getString("usrdb","");
        Toast.makeText(that,dbpath,Toast.LENGTH_LONG).show();
        return dbpath;

    }


}
