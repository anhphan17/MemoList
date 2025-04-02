package com.example.memolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class MemoDataSource {

    private SQLiteDatabase database;
    private MemoDBHelper dbHelper;

    public MemoDataSource(Context context) {
        dbHelper = new MemoDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertMemo(Memo m) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("memotitle", m.getMemoTitle());
            initialValues.put("memodescription", m.getMemoDescription());
            initialValues.put("priorityselection", m.getPrioritySelection());
            initialValues.put("date", String.valueOf(m.getDate().getTimeInMillis()));

            didSucceed = database.insert("memo", null, initialValues) > 0;
        }
        catch (Exception e) {

        }
        return didSucceed;
    }

    public boolean updateMemo (Memo m) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) m.getMemoID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("memotitle", m.getMemoTitle());
            updateValues.put("memodescription", m.getMemoDescription());
            updateValues.put("priorityselection", m.getPrioritySelection());
            updateValues.put("date", String.valueOf(m.getDate().getTimeInMillis()));

            didSucceed = database.update("memo", updateValues, "id="
                    + rowId, null) > 0;
        }
        catch (Exception e) {

        }
        return didSucceed;
    }

    public int getLastMemoID() {
        int lastId;
        try {
            String query = "Select MAX(id) from memo";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    public ArrayList<Memo> getMemos() {
        ArrayList<Memo> memos = new ArrayList<Memo>();
        try {
            String query = "SELECT * FROM memo";
            Cursor cursor = database.rawQuery(query, null);

            Memo newMemo;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newMemo = new Memo();
                newMemo.setMemoID(cursor.getInt(0));
                newMemo.setMemoTitle(cursor.getString(1));
                newMemo.setMemoDescription(cursor.getString(2));
                newMemo.setPrioritySelection(cursor.getString(3));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(4)));
                newMemo.setDate(calendar);
                memos.add(newMemo);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            memos = new ArrayList<Memo>();
        }
        return memos;
    }

    public boolean deleteMemo(int memoId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("memo", "id=" + memoId, null) > 0;
        }
        catch (Exception e) {

        }
        return didDelete;
    }




}
