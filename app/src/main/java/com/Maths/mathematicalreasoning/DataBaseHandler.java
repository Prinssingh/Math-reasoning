package com.Maths.mathematicalreasoning;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DataBaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "MydataBase";
    private static final String TABLE_GAME_LEVELS = "Mytable";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_HINT = "hint";
    private static final String KEY_SOLUTION = "solution";
    private static final String KEY_STATUS = "status";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Creating","Creating Data Base ");
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_GAME_LEVELS + "("
                + KEY_LEVEL + " INTEGER PRIMARY KEY,"
                + KEY_QUESTION + " TEXT,"
                + KEY_ANSWER + " INTEGER,"
                + KEY_HINT + " TEXT,"
                + KEY_SOLUTION + " TEXT,"
                + KEY_STATUS + " INTEGER"+ ");";

        db.execSQL(CREATE_CONTACTS_TABLE);
        Log.d("Creating","Creating Data Base ...........................DONE!!!");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        Log.d("Upgrading","in progress");

        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE "+TABLE_GAME_LEVELS+";");
            onCreate(db);
        }


        Log.d("Upgrading","in  DONE.............................................");
    }

    // code to add the new GameLevel
    void addGameLevel(GameLevel gameLevel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_QUESTION, gameLevel.getQuestion());
        values.put(KEY_ANSWER, gameLevel.getAnswer());
        values.put(KEY_HINT, gameLevel.getHint());
        values.put(KEY_SOLUTION, gameLevel.getSolution());
        values.put(KEY_STATUS,gameLevel.getStatus());


        // Inserting Row
        db.insert(TABLE_GAME_LEVELS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single GameLevel
    public GameLevel getGameLevel(int level) {
        SQLiteDatabase db = this.getReadableDatabase();

       /* Cursor cursor = db.query(TABLE_GAME_LEVELS, new String[]{KEY_LEVEL,
                        KEY_QUESTION, KEY_ANSWER, KEY_HINT, KEY_SOLUTION,KEY_STATUS}, KEY_LEVEL + "=?",
                new String[]{String.valueOf(level)}, null, null, null, null);*/



        Cursor cursor = db.rawQuery("SELECT  *"  + " FROM " + TABLE_GAME_LEVELS + " WHERE "+KEY_LEVEL +" like '%" + String.valueOf(level)+ "%'", null);

        if (cursor != null)
            cursor.moveToFirst();

        GameLevel gameLevel = new GameLevel(Integer.parseInt(cursor.getString(0)),
                String.valueOf(cursor.getString(1)),
                Integer.parseInt(cursor.getString(2)),
                String.valueOf(cursor.getString(3)),
                String.valueOf(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)));
        // return contact
        cursor.close();
        db.close();
        return gameLevel;
    }

    // code to get all GameLevels in a list view
    public List<GameLevel> getAllGameLevels() {
        List<GameLevel> GameLevelList = new ArrayList<GameLevel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GAME_LEVELS;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GameLevel gameLevel = new GameLevel();
                gameLevel.setLevel(Integer.parseInt(cursor.getString(0)));
                gameLevel.setQuestion(String.valueOf(cursor.getString(1)));
                gameLevel.setAnswer(Integer.parseInt(cursor.getString(2)));
                gameLevel.setHint(String.valueOf(cursor.getString(3)));
                gameLevel.setSolution(String.valueOf(cursor.getString(4)));
                gameLevel.setStatus(Integer.parseInt(cursor.getString(5)));

                // Adding contact to list
                GameLevelList.add(gameLevel);
            } while (cursor.moveToNext());
        }

        // return contact list
        return GameLevelList;
    }

    // code to update the single GameLevel
    public int updateGameLevel(GameLevel gameLevel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, gameLevel.getQuestion());
        values.put(KEY_ANSWER, gameLevel.getAnswer());
        values.put(KEY_HINT, gameLevel.getHint());
        values.put(KEY_SOLUTION, gameLevel.getSolution());
        values.put(KEY_STATUS,gameLevel.getStatus());

        // updating row
        return db.update(TABLE_GAME_LEVELS, values, KEY_LEVEL + " = ?",
                new String[]{String.valueOf(gameLevel.getLevel())});
    }

    // code to updte the status of single gameLevel
    public int UpdateStatus(int Level,int status)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        GameLevel gameLevel=getGameLevel(Level);
        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, gameLevel.getQuestion());
        values.put(KEY_ANSWER, gameLevel.getAnswer());
        values.put(KEY_HINT, gameLevel.getHint());
        values.put(KEY_SOLUTION, gameLevel.getSolution());
        values.put(KEY_STATUS,status);


        return db.update(TABLE_GAME_LEVELS, values, KEY_LEVEL + " = ?",
                new String[]{String.valueOf(Level)});

    }

    // Deleting single GameLevel
    public void deleteGameLevel(GameLevel gameLevel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GAME_LEVELS, KEY_LEVEL + " = ?",
                new String[]{String.valueOf(gameLevel.getLevel())});
        db.close();
    }

    // Getting GameLevel Count
    public int getGameLevelCount() {
        String countQuery = "SELECT  * FROM " + TABLE_GAME_LEVELS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public GameLevel getGameLevel1(int level)
    {
        List<GameLevel> p =getAllGameLevels();
        return p.get(level);
    }

}