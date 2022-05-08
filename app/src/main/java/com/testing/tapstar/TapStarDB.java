package com.testing.tapstar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TapStarDB extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "tapstar.db";
    public static final String TABLE_NAME = "player_info";
    public static final String COL_2 = "DIFFICULTY";
    public static final String COL_3 = "SOUND";
    public static final String COL_4 = "HIGHSCORE";

    public TapStarDB(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "DIFFICULTY TEXT, SOUND TEXT, HIGHSCORE INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String difficulty, String sound, long highscore)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, difficulty);
        contentValues.put(COL_3, sound);
        contentValues.put(COL_4, highscore);

        long r = db.insert(TABLE_NAME, null, contentValues);

        if (r == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean updateData(String difficulty, String sound, long highscore)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, difficulty);
        contentValues.put(COL_3, sound);
        contentValues.put(COL_4, highscore);

        long r = db.update(TABLE_NAME, contentValues, "ID=?", new String[] {"1"});

        if (r == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor fetch()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
        }

        return cursor;
    }
}
