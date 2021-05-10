package com.example.parsestarterproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class profile extends AppCompatActivity {

    TextView un;
    String name;

    CircleImageView ci;
    Uri uri;
    File file;

    public ArrayList<String> username;
    public ArrayList<Bitmap> image;
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        un = findViewById(R.id.un);
        ci = findViewById(R.id.ci);

        ParseUser user = ParseUser.getCurrentUser();
        name = user.getUsername();
        un.setText(name);

        listView = findViewById(R.id.list);

        username = new ArrayList<>();
        image = new ArrayList<>();

        ParseQuery<ParseObject> pQuery = ParseQuery.getQuery("profile");
        pQuery.whereEqualTo("username", name);
        pQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for(ParseObject object : objects){

                }
            }
        });


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
        query.whereEqualTo("username", name);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size() > 0){
                        for(ParseObject object : objects){
                            username.add(object.getString("username"));

                            ParseFile file = (ParseFile) object.get("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    image.add(bitmap);
                                    myAdapter adapter = new myAdapter();
                                    listView.setAdapter(adapter);
                                }
                            });
                        }
                    }
                }
            }
        });



    }



    public class myAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return image.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.card, viewGroup, false);
            ImageView iv = view.findViewById(R.id.iv);
            TextView tv = view.findViewById(R.id.tv);

            iv.setImageBitmap(image.get(i));
            tv.setText(username.get(i));

            return view;
        }
    }

    public void goback(View view){
        super.onBackPressed();
    }








    public void profilepick(View view){
        int permission = ActivityCompat.checkSelfPermission(profile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            pickImage();
        }
        else{
            if(permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(profile.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
            else{
                pickImage();
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
            Toast.makeText(profile.this, "Permission denied", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null && resultCode==RESULT_OK){
            uri = data.getData();

            switch (requestCode){
                case 100:
                    Intent i = new Intent(profile.this, DsPhotoEditorActivity.class);
                    i.setData(uri);
                    i.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Images");
                    i.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, Color.parseColor("#232323"));
                    i.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR, Color.parseColor("#000000"));
                    startActivityForResult(i,101);

                case 101:
                    file = new File(uri.getPath());
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                    ci.setImageBitmap(bitmap);
                    final Bitmap compressedBitmap = Compressor.getDefault(profile.this).compressToBitmap(file);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    compressedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    ParseFile pfile = new ParseFile("image.png", byteArray);
                    ParseObject object = new ParseObject("profile");
                    object.put("image", pfile);
                    object.put("username", ParseUser.getCurrentUser().getUsername());

                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                ci.setImageBitmap(compressedBitmap);
                                Toast.makeText(profile.this, "profile picture updated successfully", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(profile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            }


        }

    }

}
