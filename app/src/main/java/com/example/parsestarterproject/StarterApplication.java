package com.example.parsestarterproject;

import android.app.Application;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("myappID")
                .clientKey("client_key")
                .server("server_link")
                .build());

        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
