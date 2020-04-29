package com.gdeveloper.community;

public class Post
{

    private int likes;
    private String postid;
    private  String profilepicture;
    private int databasepostid;
    private String posttitle;
    private  String postdescription;
    private String username;
    private String picturepath;
    public Post()
    {

    }
    Post(String postid, String posttile, String description, String username, int databasepostid, String picturepath,String profilepicture,int likes)
    {

        this.postid=postid;
        this.posttitle=posttile;
        this.postdescription=description;
        this.username=username;
        this.databasepostid=databasepostid;
        this.picturepath=picturepath;
        this.profilepicture=profilepicture;
        this.likes=likes;
    }

    public String getPostid() {
        return postid;
    }

    public String getPosttitle() {
        return posttitle;
    }

    public String getPostdescription() {
        return postdescription;
    }
    public String getUsername() {
        return username;
    }
    public String getPicturepath() {
        return picturepath;
    }
    public int getDatabasepostid() {
        return databasepostid;
    }
    public String getProfilepicture(){return profilepicture; }
    public int getLikes(){return likes;}
}
