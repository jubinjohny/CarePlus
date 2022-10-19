package com.example.careplus.localStorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

public class SQLiteDBHelper extends SQLiteOpenHelper {
    public SQLiteDBHelper(Context context) {
        super(context, "PatientRegistrationData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE PatientDetails(patientID TEXT primary key, firstName TEXT, lastName TEXT, email TEXT, dob TEXT, phone TEXT,gender TEXT, address TEXT, city TEXT, state TEXT, zipCode TEXT, insuranceProvider TEXT, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS PatientDetails");
    }

    public Boolean insertPatientInitialData(String patientID, String firstName, String lastName, String email, String dob, String phone, String gender, String address, String city, String state, String zipCode,String insuranceProvider, String password) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("patientID", patientID);
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("email", email);
        contentValues.put("dob", dob);
        contentValues.put("phone", phone);
        contentValues.put("gender", gender);
        contentValues.put("address", address);
        contentValues.put("city", city);
        contentValues.put("state", state);
        contentValues.put("zipCode", zipCode);
        contentValues.put("insuranceProvider", insuranceProvider);
        contentValues.put("password", password);
        long result = DB.insert("PatientDetails", null, contentValues);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean updatePatientSecond( String patientID, String dob, String phone, String gender) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dob", dob);
        contentValues.put("phone", phone);
        contentValues.put("gender", gender);
        Cursor cursor = DB.rawQuery("Select * from PatientDetails where patientID = ?", new String[] {patientID});
        if(cursor.getCount() >= 0) {
            long result = DB.update("PatientDetails", contentValues, "patientID=?", new String[] {patientID});
            if(result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean updatePatientThird( String patientID, String address, String city, String state, String zipCode) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("address", address);
        contentValues.put("city", city);
        contentValues.put("state", state);
        contentValues.put("zipCode", zipCode);
        Cursor cursor = DB.rawQuery("Select * from PatientDetails where patientID = ?", new String[] {patientID});
        if(cursor.getCount() >= 0) {
            long result = DB.update("PatientDetails", contentValues, "patientID=?", new String[] {patientID});
            if(result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean updatePatientFourth( String patientID, String insuranceProvider, String password) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("insuranceProvider", insuranceProvider);
        contentValues.put("password", password);
        Cursor cursor = DB.rawQuery("Select * from PatientDetails where patientID = ?", new String[] {patientID});
        if(cursor.getCount() >= 0) {
            long result = DB.update("PatientDetails", contentValues, "patientID=?", new String[] {patientID});
            if(result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public Boolean deletePatientData( String patientID) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from PatientDetails where patientID = ?", new String[] {patientID});
        if(cursor.getCount() >= 0) {
            long result = DB.delete("PatientDetails", "patientID=?", new String[] {patientID});
            if(result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public String getPatientID() {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT patientID FROM PatientDetails ORDER BY  patientID DESC LIMIT 1", null);
        cursor.moveToLast();
        return cursor.getString(0);
    }

    public String getPatientEmail() {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT email FROM PatientDetails ORDER BY  patientID DESC LIMIT 1", null);
        cursor.moveToLast();
        return cursor.getString(0);
    }

    public String getPatientPassword() {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT password FROM PatientDetails ORDER BY  patientID DESC LIMIT 1", null);
        cursor.moveToLast();
        return cursor.getString(0);
    }

    public Cursor getAllData() {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("Select * from PatientDetails", null);
        return cursor;
    }

}
