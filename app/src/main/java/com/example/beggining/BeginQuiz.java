package com.example.beggining;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class BeginQuiz extends AppCompatActivity {

    sqlhelper db;
    private EditText Ip;
    private EditText Files;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_quiz);
       db= new sqlhelper(this);



    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("www.google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
    public static String getJSON(String url) {
        HttpsURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();

            con.connect();


            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public void Download(View view) {


        Files= findViewById(R.id.ip_box);
        String file=Files.getText().toString();
        downloadJSON(file);
        /*if(file==""||ip=="")
        {
            Toast.makeText(this,"File Or Ip null",Toast.LENGTH_LONG).show();
            return;
        }*/
        //URL url;
        //String address="http://"+file+":80/"+ip;



       //Toast.makeText(this,String.valueOf(),Toast.LENGTH_LONG).show();

        //"https://api.myjson.com/bins/"+ip;
            // InetAddress addr = InetAddress.getByName(address);
            // get URL content
            //url = new URL(address);
        //getJSON("https://api.myjson.com/bins/1ak8ks");
            //URL add= new URL("https://api.myjson.com/bins/1ak8ks");









    }
    public void Logout(View view) {
        SharedPreferences pref = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("nickname", "");
        editor.putString("email", "");
        editor.putString("password","");
        editor.apply();
        Intent quizes= new Intent(this,MainActivity.class);
        startActivity(quizes);
        finish();

    }
    public void Progres (View view){
        Intent quizes= new Intent( this,Progress.class);
        startActivity(quizes);

    }
    public void Courses (View view){

        Intent quizes= new Intent(this,Listquizes.class);
        startActivity(quizes);

    }





    private void downloadJSON(final String urlStr) {
        progressDialog = ProgressDialog.show(this, "", "Downloading Json from " + urlStr);
        final String url = urlStr;

        new Thread() {
            public void run() {
                InputStream in = null;

                Message msg = Message.obtain();
                msg.what = 1;

                try {
                    in = openHttpConnection(url);
                    if(in==null) {
                        progressDialog.dismiss();
                        return;
                    }
                    String input=null;
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                        input= br.lines().collect(Collectors.joining(System.lineSeparator()));
                    }
                    //bitmap = BitmapFactory.decodeStream(in);
                    Bundle b = new Bundle();
                    b.putString("json", input);
                    b.putString("nume", urlStr);
                    msg.setData(b);
                    in.close();
                }catch (IOException e1) {
                    Bundle b = new Bundle();
                    msg.what = 1;
                    b.putString("json", "Can not keep up the connection");
                    msg.setData(b);
                    messageHandler.sendMessage(msg);
                }

                messageHandler.sendMessage(msg);
            }
        }.start();
    }

    private InputStream openHttpConnection(String urlStr) {
        InputStream in = null;
        int resCode = -1;

        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                Bundle b = new Bundle();
                Message msg = Message.obtain();
                msg.what = 1;
                b.putString("json", "not working URL is not an Http URL");
                msg.setData(b);
                messageHandler.sendMessage(msg);
                return null;
                //throw new IOException("");
            }

            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return in;
    }

    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //EditText img =  findViewById(R.id.nickname);
            String taken= msg.getData().getString("json");
            String nume= msg.getData().getString("nume");
            try {
                //Toast.makeText( progressDialog.getContext(),taken+" downloaded",Toast.LENGTH_LONG).show();
                JSONObject jsonObj = new JSONObject(taken);
                String test=jsonObj.getString("nume");

                //FileOutputStream outputStream;
                try {
                    File file = new File(getExternalFilesDir(null), test);

                    //This point and below is responsible for the write operation
                    FileOutputStream outputStream = null;
                    file.createNewFile();
                    outputStream = new FileOutputStream(file, true);
                    //outputStream = openFileOutput( Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+test, Context.MODE_PRIVATE);
                    Toast.makeText( progressDialog.getContext(),Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+test,Toast.LENGTH_LONG).show();
                    outputStream.write(taken.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                java.sql.Date a= java.sql.Date.valueOf("1971-11-29");

                //Quizanswer q1 = new Quizanswer("trial",0,Date.valueOf("1971-11-29"),0,0,"10.0.2.2","end.json");
                Quizanswer q1 = new Quizanswer(nume,0,a,0,0,nume,test);
                db.addQUIZ(q1);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText( progressDialog.getContext(),nume+" downloaded",Toast.LENGTH_LONG).show();

            progressDialog.dismiss();
        }

    };

    private boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

}
