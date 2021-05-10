package com.example.parsestarterproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import id.zelory.compressor.Compressor;

public class posting extends AppCompatActivity {

    ImageView iv;
    Boolean selected;
    Bitmap bitmap;
    File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        iv = findViewById(R.id.iv);

        selected = false;

    }


    public void pick(View view){
        int permission = ActivityCompat.checkSelfPermission(posting.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            pickImage();
        }
        else{
            if(permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(posting.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
            else{
                pickImage();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Uri uri = data.getData();
            switch (requestCode){
                case 100:

                    Intent i = new Intent(posting.this, DsPhotoEditorActivity.class);
                    i.setData(uri);
                    i.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Images");
                    i.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, Color.parseColor("#232323"));
                    i.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR, Color.parseColor("#000000"));
                    startActivityForResult(i,101);

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        selected = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;

                case 101:
//                    iv.setImageURI(uri);
//                    iv.setPadding(0,0,0,0);

                    file = new File(uri.getPath());
                    Bitmap compressedImageBitmap2 = Compressor.getDefault(this).compressToBitmap(file);

                    ByteArrayOutputStream stream22 = new ByteArrayOutputStream();
                    compressedImageBitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream22);

                    iv.setImageBitmap(compressedImageBitmap2);
                    iv.setPadding(0,0,0,0);

                    Toast.makeText(posting.this, String.valueOf(stream22.size()), Toast.LENGTH_LONG).show();

                    break;
            }
        }



    }


    private void pickImage() {

        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, 100);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 10 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            pickImage();
        }
        else{
            Toast.makeText(posting.this, "Permission denied", Toast.LENGTH_LONG).show();
        }
    }




    public void post(View view){

        if(!selected){
            Toast.makeText(posting.this, "null image", Toast.LENGTH_LONG).show();
            return;
        }

        if(bitmap == null){
            Toast.makeText(posting.this, "null image", Toast.LENGTH_LONG).show();
            return;
        }

        Bitmap compressedImageBitmap = Compressor.getDefault(this).compressToBitmap(file);

        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        compressedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream2);
        byte[] byteArray2 = stream2.toByteArray();

        ParseFile file = new ParseFile("image.png", byteArray2);
        ParseObject object = new ParseObject("Image");

        object.put("image", file);
        object.put("likes", 0);
        object.put("likers", "");
        object.put("username", ParseUser.getCurrentUser().getUsername());

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(posting.this, "posted successfully", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(posting.this, feed.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(posting.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}