package com.example.careplus.localStorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Map;

public class SQLiteDBHelperDoctor extends SQLiteOpenHelper {
    public SQLiteDBHelperDoctor(Context context) {
        super(context, "DoctorRegistrationData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE DoctorDetails(doctorID TEXT, firstname TEXT, lastName TEXT, email TEXT, phone TEXT, " +
                "specialization TEXT, password TEXT, availability [TEXT])");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS DoctorDetails");
    }

    public Boolean insertInitialDoctorData(String doctorID, String firstname, String lastname, String email, String phone, String specialization, String password, String availability) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("doctorID", doctorID);
        contentValues.put("firstname", firstname);
        contentValues.put("lastname", lastname);
        contentValues.put("email", email);
        contentValues.put("phone", phone);
        contentValues.put("specialization", specialization);
        contentValues.put("password", password);
        contentValues.put("availability", String.valueOf(availability));
        long result = DB.insert("DoctorDetails", null, contentValues);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean updateDoctorData(String doctorID, String specialization, String password, String availability) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("specialization", specialization);
        contentValues.put("password", password);
        contentValues.put("availability", String.valueOf(availability));
        Cursor cursor = DB.rawQuery("Select * from DoctorDetails where doctorID = ?", new String[] {doctorID});
        if(cursor.getCount() >= 0) {
            long result = DB.update("DoctorDetails", contentValues, "doctorID=?", new String[] {doctorID});
            if(result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean deleteDoctorData( String doctorID) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from DoctorDetails where doctorID = ?", new String[] {doctorID});
        if(cursor.getCount() >= 0) {
            long result = DB.delete("DoctorDetails", "doctorID=?", new String[] {doctorID});
            if(result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public String getDoctorID() {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT doctorID FROM DoctorDetails ORDER BY  doctorID DESC LIMIT 1", null);
        cursor.moveToLast();
        return cursor.getString(0);
    }

    public String getDoctorEmail() {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT email FROM DoctorDetails ORDER BY  doctorID DESC LIMIT 1", null);
        cursor.moveToLast();
        return cursor.getString(0);
    }

    public String getDoctorPassword() {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT password FROM DoctorDetails ORDER BY  doctorID DESC LIMIT 1", null);
        cursor.moveToLast();
        return cursor.getString(0);
    }

    public Cursor getAllData() {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select * from DoctorDetails", null);
        return cursor;
    }
}
