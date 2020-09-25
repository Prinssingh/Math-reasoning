package com.Math.MathReasoning;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserData implements Comparable< UserData >{
    private String Name;
    private String Email;
    private Integer  Level;
    private Integer Hint;
    private Integer  Solution;
    private Long DT;

    public UserData(){}

    public UserData(String Name, String Email, Integer  Level,Integer Hint,Integer Solution,Long DT)
    {
        this.Name=Name;
        this.Email=Email;
        this.Level=Level;
        this.Hint=Hint;
        this.Solution=Solution;
        this.DT =DT;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", this.Name);
        result.put("Email", this.Email);
        result.put("Level", this.Level);
        result.put("Hint",this.Hint);
        result.put("Solution",this.Solution);
        result.put("DT",this.DT);
        return result;
    }


    public String getName(){return this.Name;}
    public void setName(String Name){this.Name=Name;}

    public String getEmail(){return this.Email;}
    public void setEmail(String Email){this.Email=Email;}

    public Integer  getLevel(){return this.Level;}
    public void setLevel(int Level){this.Level=Level;}

    public Integer getHint() { return this.Hint; }
    public void setHint(Integer hint) {this.Hint = hint; }

    public Integer getSolution() { return this.Solution; }
    public void setSolution(Integer solution) { this.Solution = solution; }

    public Long getDT() { return this.DT; }
    public void setDT(Long DT) { this.DT = DT; }


    @Override
    public int compareTo(UserData userData) {
        if(this.Level.equals(userData.Level))
        {
           if(this.getDT() < userData.getDT()) {
               return 1;
           } else if(this.getDT() > userData.getDT()) return -1;
           else return 0;
        }
        return this.getLevel().compareTo(userData.getLevel());
    }
}
