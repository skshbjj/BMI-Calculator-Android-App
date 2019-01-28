package com.sakshibajaj.bmicalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBHandler extends SQLiteOpenHelper {

    SQLiteDatabase db;
    Context context;

    DBHandler(Context context){
        super(context, "bmi_db", null,1);
        this.context = context;
        db = this.getWritableDatabase();
        }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table bmi(date text primary key," + "bmi text)";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void add(String date, double bmi)
    {
        ContentValues cv = new ContentValues();
        cv.put("date",date);
        cv.put("bmi",bmi);
        long rid = db.insert("bmi",null,cv);
        if(rid<0)
            Toast.makeText(context, "Already exists", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Record saved", Toast.LENGTH_SHORT).show();
    }

    public String view()
    {
        Cursor cursor = db.query("bmi", null,null,null,null,null,null);
        StringBuffer sb = new StringBuffer();
        cursor.moveToFirst();
        if(cursor.getCount()>0)
        {
            do {
                String date = cursor.getString(0);
                String bmi = String.valueOf(cursor.getDouble(1));
                sb.append("Date: "+date+"\nBMI:"+bmi+"\n\n");
             }while (cursor.moveToNext());
        }
        return sb.toString();
    }
}
