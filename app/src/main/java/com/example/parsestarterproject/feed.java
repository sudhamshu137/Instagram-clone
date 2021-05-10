package com.example.parsestarterproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class feed extends AppCompatActivity {

//    public ArrayList<String> username;
//    public ArrayList<Bitmap> image;
//    public ListView listView;

    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager manager;
    public ArrayList<ExampleItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);


        recyclerView = findViewById(R.id.rv);
        manager = new LinearLayoutManager(this);
        list = new ArrayList<>();
//        list.add(new ExampleItem(R.drawable.one, "first user"));
//        list.add(new ExampleItem(R.drawable.two, "second user"));
//        list.add(new ExampleItem(R.drawable.three, "third user"));
//        list.add(new ExampleItem(R.drawable.four, "fourth user"));
//        list.add(new ExampleItem(R.drawable.five, "fifth user"));
//        list.add(new ExampleItem(R.drawable.six, "sixth user"));
//        list.add(new ExampleItem(R.drawable.seven, "seventh user"));
//        list.add(new ExampleItem(R.drawable.eight, "eight user"));
//        adapter = new ExampleAdapter(list);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(adapter);



//        listView = findViewById(R.id.list);
//
//        username = new ArrayList<>();
//        image = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
//        query.orderByDescending("createdAt");
//        query.whereEqualTo("username", name);
//        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size() > 0){
                        for(ParseObject object : objects){

//                            username.add(object.getString("username"));
                            final String un = object.getString("username");

                            ParseFile file = (ParseFile) object.get("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(data == null){
                                        Toast.makeText(feed.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                                    image.add(bitmap);
//                                    myAdapter adapter = new myAdapter();
//                                    listView.setAdapter(adapter);

                                    list.add(new ExampleItem(bitmap, un));

                                }
                            });
                        }
                        adapter = new ExampleAdapter(list);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }
        });

    }

//    public class myAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return image.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return 0;
//        }
//
//        @SuppressLint("ViewHolder")
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//
//            view = getLayoutInflater().inflate(R.layout.card, viewGroup, false);
//            ImageView iv = view.findViewById(R.id.iv);
//            TextView tv = view.findViewById(R.id.tv);
//
//            iv.setImageBitmap(image.get(i));
//            tv.setText(username.get(i));
//
//            return view;
//        }
//    }




    public void goprofile(View view){
        Intent i = new Intent(feed.this, profile.class);
        startActivity(i);
    }

    public void addpost(View view){
        Intent i = new Intent(feed.this, posting.class);
        startActivity(i);
    }

}