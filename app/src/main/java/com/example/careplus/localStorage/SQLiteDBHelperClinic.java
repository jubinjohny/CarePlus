package com.example.careplus.localStorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteDBHelperClinic extends SQLiteOpenHelper {
    public SQLiteDBHelperClinic( Context context) {
        super(context, "ClientRegistrationData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE ClinicDetails(clinicID TEXT, name TEXT, type TEXT, email TEXT, phone TEXT, " +
                "address TEXT, city TEXT, state TEXT, zipcode TEXT, weekdayStartHour NUMBER, weekdayStartMin NUMBER," +
                "weekdayEndHour NUMBER, weekdayEndMin NUMBER, weekendStartHour NUMBER, weekendStartMin NUMBER, " +
                "weekendEndHour NUMBER, weekendEndMin NUMBER,website TEXT, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS ClinicDetails");
    }

    public Boolean insertInitialClinicData(String clinicID, String name, String type, String email, String phone,String address, String city, String state,
                                           String zipcode,int weekdayStartHour, int weekdayStartMin, int weekdayEndHour,int weekdayEndMin,
                                           int weekendStartHour, int weekendStartMin,int weekendEndHour,int weekendEndMin, String website, String password) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("clinicID", clinicID);
        contentValues.put("name", name);
        contentValues.put("type", type);
        contentValues.put("email", email);
        contentValues.put("phone", phone);
        contentValues.put("address", address);
        contentValues.put("city", city);
        contentValues.put("state", state);
        contentValues.put("zipcode", zipcode);
        contentValues.put("weekdayStartHour", weekdayStartHour);
        contentValues.put("weekdayStartMin", weekdayStartMin);
        contentValues.put("weekdayEndHour", weekdayEndHour);
        contentValues.put("weekdayEndMin", weekdayEndMin);
        contentValues.put("weekendStartHour", weekendStartHour);
        contentValues.put("weekendStartMin", weekendStartMin);
        contentValues.put("weekendEndHour", weekendEndHour);
        contentValues.put("weekendEndMin", weekendEndMin);
        contentValues.put("website", website);
        contentValues.put("password", password);
        long result = DB.insert("ClinicDetails", null, contentValues);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean updateClinicSecond( String clinicID, String address, String city, String state, String zipcode) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("address", address);
        contentValues.put("city", city);
        contentValues.put("state", state);
        contentValues.put("zipcode", zipcode);
        Cursor cursor = DB.rawQuery("Select * from ClinicDetails where clinicID = ?", new String[] {clinicID});
        if(cursor.getCount() >= 0) {
            long result = DB.update("ClinicDetails", contentValues, "clinicID=?", new String[] {clinicID});
            if(result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean updateClinicThird( String clinicID, int weekdayStartHour, int weekdayStartMin, int weekdayEndHour,int weekdayEndMin,
                                      int weekendStartHour, int weekendStartMin,int weekendEndHour,int weekendEndMin, String website) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("weekdayStartHour", weekdayStartHour);
        contentValues.put("weekdayStartMin", weekdayStartMin);
        contentValues.put("weekdayEndHour", weekdayEndHour);
        contentValues.put("weekdayEndMin", weekdayEndMin);
        contentValues.put("weekendStartHour", weekendStartHour);
        contentValues.put("weekendStartMin", weekendStartMin);
        contentValues.put("weekendEndHour", weekendEndHour);
        contentValues.put("weekendEndMin", weekendEndMin);
        contentValues.put("website", website);
        Cursor cursor = DB.rawQuery("Select * from ClinicDetails where clinicID = ?", new String[] {clinicID});
        if(cursor.getCount() >= 0) {
            long result = DB.update("ClinicDetails", contentValues, "clinicID=?", new String[] {clinicID});
            if(result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean updateClinicLast( String clinicID, String password) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        Cursor cursor = DB.rawQuery("Select * from ClinicDetails where clinicID = ?", new String[] {clinicID});
        if(cursor.getCount() >= 0) {
            long result = DB.update("ClinicDetails", contentValues, "clinicID=?", new String[] {clinicID});
            if(result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean deleteClinicData( String clinicID) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from ClinicDetails where clinicID = ?", new String[] {clinicID});
        if(cursor.getCount() >= 0) {
            long result = DB.delete("ClinicDetails", "clinicID=?", new String[] {clinicID});
            if(result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public String getClinicID() {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT clinicID FROM ClinicDetails ORDER BY  clinicID DESC LIMIT 1", null);
        cursor.moveToLast();
        return cursor.getString(0);
    }

    public String getClinicEmail() {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT email FROM ClinicDetails ORDER BY  clinicID DESC LIMIT 1", null);
        cursor.moveToLast();
        return cursor.getString(0);
    }

    public String getClinicPassword() {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT password FROM ClinicDetails ORDER BY  clinicID DESC LIMIT 1", null);
        cursor.moveToLast();
        return cursor.getString(0);
    }

    public Cursor getAllData() {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select * from ClinicDetails", null);
        return cursor;
    }
}
