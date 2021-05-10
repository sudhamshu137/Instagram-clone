package com.example.parsestarterproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Login_screen extends AppCompatActivity {

    EditText usernameEt, passwordEt;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        usernameEt = findViewById(R.id.username);
        passwordEt = findViewById(R.id.password);
        btn = findViewById(R.id.btn);

        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(passwordEt.getText().toString().length() > 5){
                    btn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void login(View view){
        if(usernameEt.getText().toString().trim().equals("")){
            return;
        }

        String username = usernameEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user != null){
                    Toast.makeText(Login_screen.this, "Login successful", Toast.LENGTH_LONG).show();

                    SharedPreferences prf = getSharedPreferences("prf", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prf.edit();
                    editor.putBoolean("loggedin", true);
                    editor.apply();

                    Intent i = new Intent(Login_screen.this, feed.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(Login_screen.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void signUpPage(View view){
        Intent i = new Intent(Login_screen.this, signup.class);
        startActivity(i);
    }

}