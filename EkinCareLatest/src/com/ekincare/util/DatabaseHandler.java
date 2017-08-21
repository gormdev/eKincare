package com.ekincare.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.vo.HydroCare;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ehc on 9/3/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "eKinCare";

    // Contacts table name
    private static final String TABLE_HYDROCARE = "HydroCare";

    // Contacts Table Columns names
    private static final String DATE_OF_ENTRY = "date_of_entry";
    private static final String STATUS = "status";
    private static final String TARGET = "target";
    private static final String INTAKE = "intake";
    private static final String USER_ID = "user_id";

    //    protected ProfileManager profileManager;
    protected Context context;
    private PreferenceHelper prefs;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        prefs = new PreferenceHelper(context);
    }

    public static String getDateInString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HYDRO_CARE_TABLE = "CREATE TABLE " + TABLE_HYDROCARE + "(" + DATE_OF_ENTRY + " DATE ," + TARGET
                + " REAL," + INTAKE + " REAL," + STATUS + " INTEGER DEFAULT 0," + USER_ID + " INTEGER, PRIMARY KEY (" + DATE_OF_ENTRY + ", " + USER_ID + "))";
        db.execSQL(CREATE_HYDRO_CARE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HYDROCARE);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */


    public Date getDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public ArrayList<HydroCare> getAll() {
        ArrayList<HydroCare> hydroCares = new ArrayList<HydroCare>();
        int weekday = getWeekDay();
        String selectQuery = "SELECT  * FROM " + TABLE_HYDROCARE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HydroCare hydroCare = new HydroCare();
                hydroCare.setDateOfEntry(getDate(cursor.getString(0)));
                hydroCare.setTarget(cursor.getFloat(1));
                hydroCare.setInTake(cursor.getFloat(2));
                hydroCare.setStatus(cursor.getInt(3));
                hydroCares.add(hydroCare);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return hydroCares;
    }

    public int getProfileId() {
        if (prefs.getSelectedProfileId() != -1) {
            return prefs.getSelectedProfileId();
        } else {
            return 1;
        }
    }

    //	return 1234;
    public int getWeekDay() {
        String selectQuery = "SELECT strftime('%w','now','localtime')";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }


}
