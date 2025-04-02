package com.example.memolist;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Memo {
    public int memoID;
    public String memoTitle;
    public String memoDescription;
    public String prioritySelection;
    public Calendar date;
    public Memo() {
        memoID = -1;
        date = Calendar.getInstance();
    }

    public int getMemoID() {
        return memoID;
    }

    public void setMemoID(int i) {
        memoID = i;
    }

    public String getMemoTitle() {
        return memoTitle;
    }

    public void setMemoTitle(String s) {
        memoTitle = s;
    }

    public String getMemoDescription() {
        return memoDescription;
    }

    public void setMemoDescription(String s) {
        memoDescription = s;
    }

    public String getPrioritySelection() {
        return prioritySelection;
    }

    public void setPrioritySelection(String s) {
        prioritySelection = s;
    }

    public Calendar getDate() {
        return date;
    }

    public String getDateAsString() {
        SimpleDateFormat dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateString.format(date.getTime());
    }

    public void setDate(Calendar c) {
        date = c;
    }
}
