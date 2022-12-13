package com.example.careplus.localStorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBScheduleDoctors extends SQLiteOpenHelper {
    public SQLiteDBScheduleDoctors(Context context) {
        super(context, "DoctorScheduleData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE DoctorSchedule(doctorName TEXT, startTime TEXT, endTime TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS DoctorSchedule");
    }

    public Boolean insertDoctorData(String doctorName, String startTime, String endTime) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("doctorName", doctorName);
        contentValues.put("startTime", startTime);
        contentValues.put("endTime", endTime);
        long result = DB.insert("DoctorSchedule", null, contentValues);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean deleteDoctorData( String doctorName) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from DoctorSchedule where doctorID = ?", new String[] {doctorName});
        if(cursor.getCount() >= 0) {
            long result = DB.delete("DoctorSchedule", "doctorName=?", new String[] {doctorName});
            if(result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select * from DoctorSchedule", null);
        return cursor;
    }
}
