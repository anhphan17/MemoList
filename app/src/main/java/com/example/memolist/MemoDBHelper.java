package com.example.memolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "memolist.db";

    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE_MEMO =
            "create table memo (id integer primary key autoincrement, "
            + "memotitle text not null, memodescription text not null, "
            + "priorityselection text, date text);";

    public MemoDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEMO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS memo");
        onCreate(db);
    }
}
