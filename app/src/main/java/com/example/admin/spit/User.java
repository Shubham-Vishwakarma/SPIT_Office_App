package com.example.admin.spit;

/**
 * Created by Admin on 09/11/2016.
 */

public class User {
    public String Name;
    public String Email;
    public String UID;
    public String Gender;
    public String Department;

    public User()
    {

    }

    public User(String Name,String Email,String UID,String Gender,String Department)
    {
        this.Name=Name;
        this.Email=Email;
        this.UID=UID;
        this.Gender=Gender;
        this.Department=Department;
    }

}