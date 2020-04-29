package com.gdeveloper.community;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class profile_activity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = this.getClass().getName().toUpperCase();
    private ProgressDialog Dialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseUser;
    private ImageView profileimage;
    private TextView email,name,intro;
    private Button OpenDashBoard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);
        OpenDashBoard=findViewById(R.id.EditProfile);


        firebaseAuth=FirebaseAuth.getInstance();

        Dialog=new ProgressDialog(this);
        email=findViewById(R.id.email);
        name=findViewById(R.id.username);
        intro=findViewById(R.id.intro);
        profileimage=findViewById(R.id.profileimgeuser);



        final FirebaseUser user=firebaseAuth.getCurrentUser();
        email.setText(user.getEmail());
        databaseUser = FirebaseDatabase.getInstance().getReference("Profile").child(user.getUid());
        OpenDashBoard.setOnClickListener(this);
        Dialog.setMessage("Please Wait...");
        Dialog.show();
        databaseUser.addValueEventListener(new ValueEventListener() {


                String profileemail, profilename,userimage, profileintroduction;//, phone;
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                        profileemail = dataSnapshot.child("useremail").getValue(String.class);
                        profilename = dataSnapshot.child("username").getValue(String.class);
                        profileintroduction = dataSnapshot.child("userintroduction").getValue(String.class);
                        userimage = dataSnapshot.child("profileimage").getValue(String.class);


                            //profileemail = keyId.child("useremail").getValue(String.class);
                         //   workplace = keyId.child("workplace").getValue(String.class);
                          //  phone = keyId.child("phone").getValue(String.class);


                    name.setText(profilename);
                  //  emailTxtView.setText(email);
                    intro.setText(profileintroduction);
                    Picasso.get().load(userimage).into(profileimage);
//                    workTxtView.setText(workplace);
//                    phoneTxtView.setText(phone);
//                   videoTxtView.setText(phone);
                    Dialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

    }

    @Override
    public void onClick(View view)
    {
            if (view==OpenDashBoard)
                    {
                        Toast.makeText(profile_activity.this,"Opening DashBoard...",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this,HomeDashBoard.class));
                    }
    }

}
