package com.example.beggining;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collectors;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity {


    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr ;
            ipAddr= InetAddress.getByName("www.google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        //checkInternetConenction();



        //Toast.makeText(this,String.valueOf(a.doInBackground()),Toast.LENGTH_LONG).show();
        Toolbar barperson=(Toolbar)findViewById(R.id.appbar);
        barperson.setTitle("Home");
        setSupportActionBar(barperson);
        SharedPreferences pref=getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String name=pref.getString("nickname","");
        String pass=pref.getString("pass","");
        String email=pref.getString("email","");

        if(name!="")
        {
            Intent quizes= new Intent(this,BeginQuiz.class);
            startActivity(quizes);
            finish();
        }

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
       return super.onCreateOptionsMenu(menu);
    }
    public void update()
    {
        File f = new File(android.os.Environment.getExternalStorageDirectory(),File.separator+"MyContactsBackUp/");
        f.mkdirs();
    }
    public void openLogin(View view) {
        Intent login= new Intent(this,AppLogin.class);
        startActivity(login);

    }
    public boolean OpenAbout(MenuItem item) {
        Intent login= new Intent(this,About.class);
        startActivity(login);
        return true;
    }
    public boolean OpenLogin(MenuItem item) {
        Intent login= new Intent(this,AppLogin.class);
        startActivity(login);
        return true;
    }
    public boolean OpenFaq(MenuItem item) {
        Intent login= new Intent(this,AppLogin.class);
        startActivity(login);
        return true;
    }
    public boolean docks(MenuItem item) {
        Intent login= new Intent(this,AppLogin.class);
        startActivity(login);
        return true;
    }

    public void Sign_up(View view) {
        String nickname,email,password;
        EditText nick,em,pas;
        nick=findViewById(R.id.nickname);
        em=findViewById( R.id.email_signup);
        pas=findViewById(R.id.passwordsignup);
        nickname=nick.getText().toString();
        email=em.getText().toString();
        password=pas.getText().toString();
        saveinfo(nickname,email,password);


    }
    public void saveinfo(String nickname,String email,String password){
        sqlhelper db= new sqlhelper(this);
        boolean alreadynick=db.verifynickname(nickname);
        if(alreadynick)
        {
            Toast.makeText(this,"Username exists",Toast.LENGTH_LONG).show();
            return;
        }
        boolean works=db.addperson(nickname,email,password);
        if (works==false)
        {
            Toast.makeText(this,"Email already used",Toast.LENGTH_LONG).show();
            return;
        }
        else {
            SharedPreferences pref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("nickname", nickname);
            editor.putString("email", email);
            editor.putString("password",password);
            editor.apply();
            Intent quizes= new Intent(this,BeginQuiz.class);
            startActivity(quizes);
            finish();

        }



        //if(user)
        //Toast.makeText(this,"Sign-ed up",Toast.LENGTH_LONG).show();
        //display();
        //Toast.makeText(this,"saved",Toast.LENGTH_LONG).show();


    }


    public void display() {
        SharedPreferences pref=getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String name=pref.getString("nickname","");
                String pass=pref.getString("pass","");
                String email=pref.getString("email","");
        Toast.makeText(this,name+pass+email,Toast.LENGTH_LONG).show();

    }



}
