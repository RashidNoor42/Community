package com.gdeveloper.community;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Add_Edit_User extends AppCompatActivity implements View.OnClickListener {
private FirebaseAuth firebaseAuth;
private DatabaseReference databaseUsers;
private TextView profileemail;
int UserId = 0;
private Uri filepath;
private StorageReference storageReference;
private FirebaseStorage firebaseStorage;
private EditText userintroduction, username, userpassword;
private Button SaveProfile;
private ImageView profileimage;
private ProgressDialog dialog;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add__edit__user);
            SaveProfile = findViewById(R.id.SaveProfile);
            dialog = new ProgressDialog(this);
            databaseUsers = FirebaseDatabase.getInstance().getReference("Profile");
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseStorage = FirebaseStorage.getInstance();
            storageReference = firebaseStorage.getReference();


            profileemail = findViewById(R.id.profileemail);
            username = findViewById(R.id.profileusername);
            userpassword = findViewById(R.id.profilepassword);
            userintroduction = findViewById(R.id.profileintroduction);
            profileimage = findViewById(R.id.profileimage);

            final FirebaseUser user = firebaseAuth.getCurrentUser();

            profileemail.setText(user.getEmail());
            SaveProfile.setOnClickListener(this);
            profileimage.setOnClickListener(this);


            }
        private void saveduserdata(){
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                final String password = userpassword.getText().toString().trim();

                if (filepath != null&& user!=null) {

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.setMessage("Saving Profile Credentials");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                        final StorageReference reference = storageReference.child("ProfileImages/"
                                + System.currentTimeMillis() + "."
                                + GetFileExtension(filepath));


        // adding listeners on upload
        // or failure of image
        reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                                String download_url = uri.toString();
                                String picturepath = download_url;
                                String nameuser = username.getText().toString().trim();
                                String email = user.getEmail();
                                String introduction = userintroduction.getText().toString().trim();



                                if (TextUtils.isEmpty(nameuser)) {
                                        Toast.makeText(Add_Edit_User.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
                                        return;
                                }
                                if (TextUtils.isEmpty(email)) {
                                        Toast.makeText(Add_Edit_User.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                                        return;
                                }
                                if (TextUtils.isEmpty(introduction)) {
                                        Toast.makeText(Add_Edit_User.this, "Please Enter Introduction", Toast.LENGTH_SHORT).show();
                                        return;
                                }
                                if (TextUtils.isEmpty(password)) {
                                        Toast.makeText(Add_Edit_User.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                                        return;}


                                // Code for showing progressDialog while uploading

                                String id = user.getUid();
                                user.updatePassword(password);
                                User userdata = new User(id, nameuser, email, introduction, password, picturepath);

                                databaseUsers.child(id).setValue(userdata);

                                Toast.makeText(Add_Edit_User.this, "User Data Added Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Add_Edit_User.this, profile_activity.class));


                                                }



                                        });
                                }


                        }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                        // Error, Image not uploaded
                                        dialog.dismiss();
                                        Toast.makeText(Add_Edit_User.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        });
                        // Toast.makeText(getApplicationContext(), "Password Has Changed", Toast.LENGTH_SHORT);
                }
                else
                {
                    Toast.makeText(this, "Please Select Photo.", Toast.LENGTH_SHORT).show();
                }
        }



            @Override
            public void onClick(View view) {
            if (view == SaveProfile) {
            saveduserdata();
            }
            if (view == profileimage) {
            chooseimage();
            }

            }

            private void chooseimage() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }

            // Override onActivityResult method
            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            super.onActivityResult(requestCode, resultCode, data);


            if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            filepath = data.getData();
            try {

            // Setting image on image view using Bitmap
            Bitmap bitmap = MediaStore
            .Images
            .Media
            .getBitmap(
            getContentResolver(),
            filepath);
            profileimage.setImageBitmap(bitmap);
            } catch (IOException e) {
            // Log the exception
            e.printStackTrace();
            }
            }
            }

            public String GetFileExtension(Uri uri) {

            ContentResolver contentResolver = getContentResolver();

            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

            // Returning the file Extension.
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

            }
}


