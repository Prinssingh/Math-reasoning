package com.Maths.mathematicalreasoning;

public class GameLevel {
    int   _level;
    String  question;
    int   answer;
    String  hint;
    String  solution;
    int status;
    // Constructor's
    public GameLevel(){}

    public GameLevel(int _level, String  question, int   answer, String  hint, String  solution,int status){
        this._level=_level;
        this.question=question;
        this.answer=answer;
        this.hint=hint;
        this.solution=solution;
        this.status=status;
    }

    //properties

    public void setLevel(int level){ this._level=level;}

    public int  getLevel(){return this._level;}

    public void setQuestion(String question){this.question=question;}

    public String getQuestion(){return this.question;}

    public void setAnswer(int answer){this.answer=answer;}

    public int getAnswer(){return this.answer;}

    public void setHint(String hint){this.hint=hint;}

    public String getHint(){return this.hint;  }

    public void setSolution(String solution){this.solution=solution;}

    public String getSolution(){return this.solution;}

    public void setStatus(int status){this.status=status;}
    public int getStatus(){return this.status;}
}
