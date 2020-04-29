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

public class SignUp extends AppCompatActivity implements View.OnClickListener {

   private Button btnsignup;
   private EditText textemail,textpassword;
   private TextView textsignin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btnsignup=findViewById(R.id.signup);
        textsignin=findViewById(R.id.signintext);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(), HomeDashBoard.class));

        }


        progressDialog=new ProgressDialog(this);
        btnsignup=findViewById(R.id.signup);
        textemail=findViewById(R.id.email);
        textpassword=findViewById(R.id.Password);

        textsignin=findViewById(R.id.signintext);

        btnsignup.setOnClickListener(this);
       textsignin.setOnClickListener(this);

    }

    public void registerUser() {
        final String email= textemail.getText().toString().trim();
        String pass=textpassword.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User");
        progressDialog.show();
        progressDialog.setCancelable(false);
        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Add_Edit_User.class));


                        }

                        else
                        {
                            Toast.makeText(SignUp.this,"Registration Failed Try Again",Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                        }
                    }
                });


    }

    @Override
    public void onClick(View view) {
        if(view==btnsignup)
        {
            registerUser();
        }
        if(view==textsignin)
        {
            startActivity(new Intent(this, MainActivity.class));
            //will open sign in screen
        }
    }
}

