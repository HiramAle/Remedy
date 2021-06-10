package com.example.remedy.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLConnection extends SQLiteOpenHelper {

    public SQLConnection(Context context,String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseUtilities.dbCreateTaskTable);
        db.execSQL(DataBaseUtilities.dbCreateTaskGroupTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DataBaseUtilities.dbTaskTable);
        db.execSQL("DROP TABLE IF EXISTS "+DataBaseUtilities.dbTaskGroupTable);
        onCreate(db);
    }

    public Cursor raw() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + DataBaseUtilities.dbTaskGroupTable , new String[]{});

        return res;
    }

}
