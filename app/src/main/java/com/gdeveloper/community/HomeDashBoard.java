package com.gdeveloper.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeDashBoard extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView usernamedisplay;
    private ImageView buttonlogout,profilesettings;
    private LinearLayout newpostbutton,newsfeed,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dash_board);



        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(this, MainActivity.class));

        }



        FirebaseUser user=firebaseAuth.getCurrentUser();

        profilesettings=findViewById(R.id.profilesettings);
        newpostbutton=findViewById(R.id.newpost);
        logout=findViewById(R.id.out);
        buttonlogout=findViewById(R.id.buttonlogout);
        newsfeed=findViewById(R.id.newsfeed);
        usernamedisplay=findViewById(R.id.usernamedisplay);


        buttonlogout.setOnClickListener(this);
        profilesettings.setOnClickListener(this);
        newsfeed.setOnClickListener(this);
        newpostbutton.setOnClickListener(this);
        usernamedisplay.setText("Welcome "+user.getEmail());
    }

    @Override
    public void onClick(View view)
    {
        if (view==newpostbutton)
        {
            Toast.makeText(HomeDashBoard.this,"Opening Add Posts",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AddPost.class));

            finish();
        }
        if (view==newsfeed)
        {
            Toast.makeText(HomeDashBoard.this,"Loading Posts",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ShowPost.class));


        }

        if(view ==buttonlogout||view==logout)
        {
            firebaseAuth.signOut();
            startActivity(new Intent(this,MainActivity.class));

        }
        if(view==profilesettings)
        {
            startActivity(new Intent(this,profile_activity.class));
        }

    }
}

