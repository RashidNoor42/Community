package com.gdeveloper.community;

public class User
{
    private String user_id;
    private String username;
    private String useremail;
    private String userintroduction;
    private String userpassword;
    private String profileimage;
    public User()
    {

    }
    User(String user_id,String username,String useremail,String userintroduction,String userpassword,String profileimage)
    {
        this.user_id=user_id;
        this.username=username;
        this.useremail=useremail;
        this.userintroduction=userintroduction;
        this.userpassword=userpassword;
        this.profileimage=profileimage;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getUseremail() {
        return useremail;
    }

    public String getUserintroduction() {
        return userintroduction;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public String getProfileimage() {
        return profileimage;
    }
}
