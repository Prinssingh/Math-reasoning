package com.Maths.mathematicalreasoning;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserData implements Comparable< UserData >{
    private String Name;
    private String Email;
    private Integer  Level;

    public UserData(){}

    public UserData(String Name, String Email, Integer  Level)
    {
        this.Name=Name;
        this.Email=Email;
        this.Level=Level;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", this.Name);
        result.put("Email", this.Email);
        result.put("Level", this.Level);
        return result;
    }


    public String getName(){return this.Name;}
    public void setName(String Name){this.Name=Name;}

    public String getEmail(){return this.Email;}
    public void setEmail(String Email){this.Email=Email;}

    public Integer  getLevel(){return this.Level;}
    public void setLevel(int Level){this.Level=Level;}

    @Override
    public int compareTo(UserData userData) {
        return this.getLevel().compareTo(userData.getLevel());
    }
}
