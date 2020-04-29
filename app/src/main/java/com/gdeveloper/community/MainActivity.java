package com.gdeveloper.community;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


   private  Button btnsignin;
   private  EditText textemail,textpassword;
    private  TextView signuptext;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth =FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(), HomeDashBoard.class));

        }

        textemail=findViewById(R.id.email);
        textpassword=findViewById(R.id.Password);
        signuptext=findViewById(R.id.signuptext);
        btnsignin=findViewById(R.id.signin);

        progressDialog=new ProgressDialog(this);
        signuptext.setOnClickListener(this);
        btnsignin.setOnClickListener(this);

    }

    private void userlogin()
    {
        String email= textemail.getText().toString().trim();
        String password=textpassword.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logging In User...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful())
                        {
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeDashBoard.class));
                        }
                        else
                        {

                            Toast.makeText(MainActivity.this,"Sign In Failed due to "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                        }



                    }
                });
    }


    @Override
    public void onClick(View view)
    {
        if (view==btnsignin)
        {
            userlogin();
        }
        if(view==signuptext)
        {
            Toast.makeText(MainActivity.this,"Opening Sign up Screen",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SignUp.class));
            finish();
        }
    }
}
