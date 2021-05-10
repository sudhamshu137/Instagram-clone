package com.example.parsestarterproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;

public class signup extends AppCompatActivity {

    EditText usernameEt, passwordEt;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }

    public void signup(View view){

        if(usernameEt.getText().toString().trim().equals("")){
            return;
        }

        String username = usernameEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){

                    Resources res = getResources();
                    @SuppressLint("UseCompatLoadingForDrawables")
                    Drawable drawable = res.getDrawable(R.drawable.defaultpic);
                    Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    ParseFile file = new ParseFile("image.png", byteArray);
                    final ParseObject object = new ParseObject("profile");
                    object.put("image", file);
                    object.put("username", ParseUser.getCurrentUser().getUsername());
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Toast.makeText(signup.this, "SignUp succeeded", Toast.LENGTH_LONG).show();
//                                object.put("id", );
                                SharedPreferences prf = getSharedPreferences("prf", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prf.edit();
                                editor.putBoolean("loggedin", true);
                                editor.apply();
                                Intent i = new Intent(signup.this, feed.class);
                                startActivity(i);
                                finish();
                            }
                            else{
                                Toast.makeText(signup.this, "SignUp failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(signup.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void LoginPage(View view){
        Intent i = new Intent(signup.this, Login_screen.class);
        startActivity(i);
    }

}