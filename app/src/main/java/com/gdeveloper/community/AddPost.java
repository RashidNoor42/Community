package com.gdeveloper.community;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class AddPost extends AppCompatActivity implements View.OnClickListener {

    private final String TAG =  this.getClass().getName().toUpperCase();
    private FirebaseAuth firebaseAuth;
    private ImageView postimage;
    private ImageButton btnchooseimage;
    private TextView usernameshow;
    private EditText title, description;
    private Button btnaddpost;
    private Uri filepath;
    private int likes=0;
    private String profilepicture;

    private DatabaseReference databasePosts,databaseUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    int databaseid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        btnchooseimage = findViewById(R.id.uploadimage);

        postimage = findViewById(R.id.postimage);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


       final FirebaseUser user = firebaseAuth.getCurrentUser();
        databasePosts = FirebaseDatabase.getInstance().getReference("post");
        title = findViewById(R.id.title);
        usernameshow = findViewById(R.id.username);
        description = findViewById(R.id.description);

        btnchooseimage.setOnClickListener(this);

        btnaddpost = findViewById(R.id.btnaddpost);
        databasePosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    databaseid++;
                } else {
                    databaseid = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseUser = FirebaseDatabase.getInstance().getReference("Profile").child(user.getUid());
        btnaddpost.setOnClickListener(this);

        databaseUser.addValueEventListener(new ValueEventListener() {

            String username;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                username = dataSnapshot.child("username").getValue(String.class);
                profilepicture=dataSnapshot.child("profileimage").getValue(String.class);
                usernameshow.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });


    }

    @Override
    public void onClick(View view) {
        if (view == btnchooseimage) {
            chooseimage();
        }
        if (view == btnaddpost)
            addpost();
    }

    private void addpost() {

        if (filepath != null) {
            final FirebaseUser user = firebaseAuth.getCurrentUser();
            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            final StorageReference reference = storageReference.child("images/"
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

                            progressDialog.dismiss();
                            Toast.makeText(AddPost.this, "Successfully uploaded", Toast.LENGTH_LONG).show();
                            String user_name = usernameshow.getText().toString().trim();
                            String picturepath = download_url;
                            String posttitle = title.getText().toString().trim();
                            String postdescription = description.getText().toString().trim();

                            int PostId = databaseid;
                            if (TextUtils.isEmpty(posttitle)) {
                                Toast.makeText(AddPost.this, "Please Eneter Title", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (TextUtils.isEmpty(postdescription)) {
                                Toast.makeText(AddPost.this, "Please Enter Description", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                String id =databasePosts.push().getKey();

                                databaseid++;
                                Post post = new Post(id, posttitle, postdescription, user_name, databaseid,picturepath,profilepicture,likes);

                                databasePosts.child(id).setValue(post);

                                Toast.makeText(AddPost.this, "Post Added Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddPost.this, ShowPost.class));
                            }

                        }



                    });

                }


            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(AddPost.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                    progressDialog.setCancelable(false);
                                }
                            });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

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
                postimage.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

}



