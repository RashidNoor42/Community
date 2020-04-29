package com.gdeveloper.community;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowPost extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog Dialog;
    ImageView btnnewpost;
    ImageView btnhome;
    ListView listViewpost;
    List<Post> postlist;
    private DatabaseReference databasePosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);

        Dialog=new ProgressDialog(this);
        listViewpost=findViewById(R.id.postlist);
        btnnewpost=findViewById(R.id.newpost);
        btnhome=findViewById(R.id.homedash);
        btnnewpost.setOnClickListener(this);
        btnhome.setOnClickListener(this);

        postlist=new ArrayList<>();
        databasePosts= FirebaseDatabase.getInstance().getReference("post");

    }

    @Override
    protected void onStart() {
        Dialog.setMessage("Please Wait...");
        Dialog.show();
        super.onStart();
        databasePosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

             postlist.clear();



                for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                   Post post=postSnapshot.getValue(Post.class);
                    postlist.add(post);


                }


                PostList adapter=new PostList(ShowPost.this, postlist);

                listViewpost.setAdapter(adapter);
                Dialog.dismiss();


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view)
    {

        if (view==btnnewpost)
        {
            Toast.makeText(ShowPost.this,"Opening Posts...",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AddPost.class));

        }
        if (view==btnhome)
        {
            Toast.makeText(ShowPost.this,"Opening DashBoard...",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeDashBoard.class));

        }
    }

}

