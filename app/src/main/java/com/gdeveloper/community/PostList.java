package com.gdeveloper.community;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostList extends ArrayAdapter<Post> {
    private FirebaseAuth firebaseAuth;
    private Activity activity;
    private List<Post> postList;
    public PostList(Activity activity, List<Post> postList)
    {
        super(activity, R.layout.post_layout,postList);
        this.activity=activity;
        this.postList=postList;

    }



    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View postview = inflater.inflate(R.layout.post_layout, null, true);

        TextView textViewheader=(TextView) postview.findViewById(R.id.title);
        TextView textViewdescription= postview.findViewById(R.id.description);
        TextView textViewusername=(TextView) postview.findViewById(R.id.username);
        ImageView postimage=postview.findViewById(R.id.postimage);
        ImageView profileimage=postview.findViewById(R.id.profileimage);

        Post post=postList.get(position);
        textViewheader.setText(post.getPosttitle());
        textViewdescription.setText(post.getPostdescription());
        textViewusername.setText(post.getUsername());
        Picasso.get().load(post.getPicturepath()).into(postimage);
        Picasso.get().load(post.getProfilepicture()).into(profileimage);



        return  postview;

    }



}

