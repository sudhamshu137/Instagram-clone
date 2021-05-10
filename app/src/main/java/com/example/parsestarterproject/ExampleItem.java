package com.example.parsestarterproject;

import android.graphics.Bitmap;

public class ExampleItem {

    private Bitmap mImageResource;
    private String mText;

    public ExampleItem(Bitmap imageResource, String text){
        mImageResource = imageResource;
        mText = text;
    }

    public Bitmap getImageResource(){
        return mImageResource;
    }

    public String gettext(){
        return mText;
    }



}
