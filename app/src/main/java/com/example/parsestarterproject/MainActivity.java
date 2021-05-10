package com.example.parsestarterproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Boolean loggedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        ParseObject object = new ParseObject("Test");
//        object.put("Firstname", "Sudhamshu");
//        object.put("Lastname", "Bhat");
//        object.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e == null){
//                    Log.i("Connection", "Successful");
//                }
//                else{
//                    e.printStackTrace();
//                    Log.i("Connection", "Unsuccessful");
//                }
//            }
//        });





//        ParseObject score = new ParseObject("Score");
//        score.put("username", "sean");
//        score.put("score", 65);
//        score.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e == null){
//                    // Everything is okay
//                    Toast.makeText(MainActivity.this, "Saved successfully", Toast.LENGTH_LONG).show();
//                }
//                else{
//                    // Something is wrong
//                    e.printStackTrace();
//                }
//            }
//        });



//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
//        query.getInBackground("3SfRMRwrQ1", new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException e) {
//                if(e == null && object != null){
//
//                    object.put("score", 85);
//                    object.saveInBackground();
//
//                    Toast.makeText(MainActivity.this, object.getString("username") + "  " + object.getInt("score") , Toast.LENGTH_LONG).show();
//                }
//            }
//        });


//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
//
//        query.whereEqualTo("username", "sean");
//        query.setLimit(1);
//
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                if(e == null){
//                    if(objects.size() > 0){
//                        for(ParseObject object : objects){
//                            tv.setText(tv.getText().toString() + "\n" + object.getString("username") + " " + object.getInt("score"));
//                        }
//                    }
//                }
//            }
//        });


//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
//        query.whereGreaterThan("score", 70);
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                if(e == null && objects != null){
//                    for(ParseObject score : objects){
//                        score.put("score", score.getInt("score") + 20);
//                        score.saveInBackground();
//                        tv.setText(tv.getText().toString() + "\n" + score.getString("username") + " " + score.getInt("score"));
//                    }
//                }
//            }
//        });


//        ParseUser user = new ParseUser();
//        user.setUsername("User1");
//        user.setPassword("password");
//
//        user.signUpInBackground(new SignUpCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e == null){
//                    Toast.makeText(MainActivity.this, "SignUp succeeded", Toast.LENGTH_LONG).show();
//                }
//                else{
//                    Toast.makeText(MainActivity.this, "SignUp failed", Toast.LENGTH_LONG).show();
//                }
//            }
//        });



//        ParseUser.logInInBackground("User1", "password", new LogInCallback() {
//            @Override
//            public void done(ParseUser user, ParseException e) {
//                if(user != null){
//                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();
//                }
//                else{
//                    Toast.makeText(MainActivity.this, "Login Unsuccessful", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

//        ParseUser.logOut();

//        if(ParseUser.getCurrentUser() != null){
//            tv.setText("Signed in as " + ParseUser.getCurrentUser().getUsername().toString());
//        }
//        else{
//            tv.setText("No user login found");
//        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences prf = getSharedPreferences("prf", Context.MODE_PRIVATE);
                loggedin = prf.getBoolean("loggedin", false);

                if(loggedin){
                    Intent i = new Intent(MainActivity.this, feed.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Intent i = new Intent(MainActivity.this, Login_screen.class);
                    startActivity(i);
                    finish();
                }

            }
        },800);


        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }


}