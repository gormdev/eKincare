package com.DataStorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.message.model.ChatDatabaseModel;
import com.oneclick.ekincare.vo.GraphData;
import com.oneclick.ekincare.vo.Immunization;
import com.oneclick.ekincare.vo.NotificationModel;
import com.oneclick.ekincare.vo.SyncModel;

import java.util.ArrayList;

/**
 * Created by Ajay on 19-04-2016.
 */
public class DatabaseOverAllHandler extends SQLiteOpenHelper implements DatabaseOverAllListener {

    DbResponse response;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseOverAllConstants.CREATE_ALTER_IMMUNIZATION_TABLE);
        db.execSQL(DatabaseOverAllConstants.CREATE_ALTER_DOCUMENTDATA__TABLE);
        db.execSQL(DatabaseOverAllConstants.CREATE_ALTER_TIMELINE_ASSESSMENT_TABLE);
        db.execSQL(DatabaseOverAllConstants.CREATE_ALTER_TIMELINE_ASSESSMENT_TRENDS_TABLE);
        db.execSQL(DatabaseOverAllConstants.CREATE_ALTER_TIMELINE_TABLE);
        db.execSQL(DatabaseOverAllConstants.CREATE_ALTER_NOTIFICATION_TABLE);
        db.execSQL(DatabaseOverAllConstants.CREATE_SYNC_TABLE);
        db.execSQL(DatabaseOverAllConstants.CREATE_ALTER_MEDICATION_TABLE);
        db.execSQL(DatabaseOverAllConstants.CREATE_ALTER_ALLERGIEDATA_TABLE);
        db.execSQL(DatabaseOverAllConstants.CREATE_CHAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseOverAllConstants.DROP_IMMUNIZATION_TABLE);
        db.execSQL(DatabaseOverAllConstants.DROP_DOCUMENTS_TABLE);
        db.execSQL(DatabaseOverAllConstants.DROP_TIMELINE_ASSESSMENT_TABLE);
        db.execSQL(DatabaseOverAllConstants.DROP_TIMELINE_ASSESSEMENTS_TRENDS_TABLE);
        db.execSQL(DatabaseOverAllConstants.DROP_TIMELINE_TABLE);
        db.execSQL(DatabaseOverAllConstants.DROP_ALTER_NOTIFICATION_TABLE);

        db.execSQL("DROP TABLE IF EXISTS "  + DatabaseOverAllConstants.TABLE_SYNC);
        db.execSQL(DatabaseOverAllConstants.DROP_MEDICATION_TABLE);
        db.execSQL(DatabaseOverAllConstants.DROP_ALLERGIE_TABLE);
        db.execSQL(DatabaseOverAllConstants.DROP_ALTER_CHAT_TABLE);
        onCreate(db);

    }

    public DatabaseOverAllHandler(Context context) {
        super(context, DatabaseOverAllConstants.DATABASE_ALTER_NAME, null, DatabaseOverAllConstants.DATABASE_VERSION);
    }

    @Override
    public void insertTimeline(String json, String identificationToken, String etag) {


        if (checkIfTimelineRecordExist(json, identificationToken, etag)) {
            this.updateTimeline(json, identificationToken, etag);
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            try {

                ContentValues values = new ContentValues();
                values.put(DatabaseOverAllConstants.JSON, json);
                values.put(DatabaseOverAllConstants.IDENTIFICATION_TOKEN, identificationToken);
                values.put(DatabaseOverAllConstants.ETAG, etag);
                db.insert(DatabaseOverAllConstants.TABLE_ALTER_TIMELINEDATA, null, values);
                db.close();
            } catch (Exception e) {
                Log.e("problem", e + "");
            }
        }


    }

    @Override
    public void clearTimeline(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseOverAllConstants.TABLE_ALTER_TIMELINEDATA, null, null);
    }

    @Override
    public DbResponse getTimelineData(String identificationToken) {
        //   SQLiteDatabase db = this.getReadableDatabase();

        System.out.println("DatabaseOverAllHandler.getTimelineData===========istimelinedatacalling");
        response = new DbResponse();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabaseOverAllConstants.TABLE_ALTER_TIMELINEDATA, new String[]{
                        DatabaseOverAllConstants.IDENTIFICATION_TOKEN, DatabaseOverAllConstants.JSON,
                        DatabaseOverAllConstants.ETAG},
                DatabaseOverAllConstants.IDENTIFICATION_TOKEN + "=?",
                new String[]{identificationToken}, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String token = cursor.getString((0));
                String json = cursor.getString(1);
                String etag = cursor.getString(2);
                response.setResponse(json);
                response.setEtag(etag);
            }
            db.close();
        }
        return response;

    }

    @Override
    public void updateTimeline(String json, String identificationToken, String etag) {
        System.out.println("DatabaseOverAllHandler.updateTimeline===updatetimeleiscalling");

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseOverAllConstants.IDENTIFICATION_TOKEN, identificationToken);
        values.put(DatabaseOverAllConstants.JSON, json);
        values.put(DatabaseOverAllConstants.ETAG, etag);

        // updating row
        db.update(DatabaseOverAllConstants.TABLE_ALTER_TIMELINEDATA, values, DatabaseOverAllConstants.IDENTIFICATION_TOKEN + " = ?",
                new String[]{identificationToken});


    }

    @Override
    public boolean checkIfTimelineRecordExist(String json, String identificationToken, String etag) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + DatabaseOverAllConstants.TABLE_ALTER_TIMELINEDATA + " where " +
                DatabaseOverAllConstants.IDENTIFICATION_TOKEN + "=?", new String[]{identificationToken});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    @Override
    public void insertAssessmentData(String json, String mStringId, String etag) {

        System.out.println("DatabaseOverAllHandler.insertAssessmentData=======insert assessment iscalling");

        if (checkIfAssessmentRecordExist(json, mStringId, etag)) {
            this.updateAssessmentData(json, mStringId, etag);
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            try {

                ContentValues values = new ContentValues();
                values.put(DatabaseOverAllConstants.JSON, json);
                values.put(DatabaseOverAllConstants.ALTER_ASSESSEMENT_STRING_ID, mStringId);
                values.put(DatabaseOverAllConstants.ETAG, etag);
                db.insert(DatabaseOverAllConstants.TABLE_TIMELINE_ALTER_ASSESSMENT_DATA, null, values);
                db.close();
            } catch (Exception e) {
                Log.e("problem", e + "");
            }
        }
    }

    @Override
    public DbResponse getAllAssessmentData(String mStringId) {
        //   SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("DatabaseOverAllHandler.getAllAssessmentData=====isgetallassessmentcallin");
        response = new DbResponse();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabaseOverAllConstants.TABLE_TIMELINE_ALTER_ASSESSMENT_DATA, new String[]{
                        DatabaseOverAllConstants.ALTER_ASSESSEMENT_STRING_ID, DatabaseOverAllConstants.JSON,
                        DatabaseOverAllConstants.ETAG},
                DatabaseOverAllConstants.ALTER_ASSESSEMENT_STRING_ID + "=?",
                new String[]{mStringId}, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String token = cursor.getString((0));
                String json = cursor.getString(1);
                String etag = cursor.getString(2);
                response.setToken(token);
                System.out.println("DatabaseOverAllHandler.getAllAssessmentData======="+response.getToken());
                response.setResponse(json);
                System.out.println("DatabaseOverAllHandler.getAllAssessmentData===="+response.getResponse());
                response.setEtag(etag);
                System.out.println("DatabaseOverAllHandler.getAllAssessmentData===="+response.getEtag());
            }
            db.close();
        }
        return response;

    }

    @Override
    public void updateAssessmentData(String json, String mStringId, String etag) {

        System.out.println("DatabaseOverAllHandler.updateAssessmentData==========update==="+json+"====="+mStringId+"====="+etag);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseOverAllConstants.ALTER_ASSESSEMENT_STRING_ID, mStringId);
        values.put(DatabaseOverAllConstants.JSON, json);
        values.put(DatabaseOverAllConstants.ETAG, etag);


        // updating row
        db.update(DatabaseOverAllConstants.TABLE_TIMELINE_ALTER_ASSESSMENT_DATA, values,
                DatabaseOverAllConstants.ALTER_ASSESSEMENT_STRING_ID + " = ?",
                new String[]{mStringId});


    }

    @Override
    public boolean checkIfAssessmentRecordExist(String json, String mStringId, String etag) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = db.rawQuery("select * from " + DatabaseOverAllConstants.TABLE_TIMELINE_ALTER_ASSESSMENT_DATA +
                values + " where " + DatabaseOverAllConstants.ALTER_ASSESSEMENT_STRING_ID + "=?", new String[]{mStringId});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    @Override
    public void insertGraphData(String json, String mStringLabResultId, String etag) {

        if (checkIfGraphRecordExist(json, mStringLabResultId, etag)) {
            this.updateGraphData(json, mStringLabResultId, etag);
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            try {

                ContentValues values = new ContentValues();
                values.put(DatabaseOverAllConstants.JSON, json);
                values.put(DatabaseOverAllConstants.ASSESSEMENT_ALTER_TRENDS_STRING_ID, mStringLabResultId);
                values.put(DatabaseOverAllConstants.ETAG, etag);
                db.insert(DatabaseOverAllConstants.TABLE_TIMELINE_ALTER_ASSESSMENT_TRENDS_DATA, null, values);
                db.close();
            } catch (Exception e) {
                Log.e("problem", e + "");
            }
        }
    }

    @Override
    public DbResponse getAllTrendsData(String mStringLabResultId) {
        //   SQLiteDatabase db = this.getReadableDatabase();
        response = new DbResponse();
        GraphData graphData = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabaseOverAllConstants.TABLE_TIMELINE_ALTER_ASSESSMENT_TRENDS_DATA, new String[]{
                        DatabaseOverAllConstants.ASSESSEMENT_ALTER_TRENDS_STRING_ID, DatabaseOverAllConstants.JSON,
                        DatabaseOverAllConstants.ETAG},
                DatabaseOverAllConstants.ASSESSEMENT_ALTER_TRENDS_STRING_ID + "=?",
                new String[]{mStringLabResultId}, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                graphData = new GraphData();
                String token = cursor.getString((0));
                String json = cursor.getString(1);
                String etag = cursor.getString(2);
                response.setResponse(json);
                response.setEtag(etag);
            }
            db.close();
        }
        return response;

    }

    @Override
    public void updateGraphData(String json, String mStringLabResultId, String etag) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseOverAllConstants.ASSESSEMENT_ALTER_TRENDS_STRING_ID, mStringLabResultId);
        values.put(DatabaseOverAllConstants.JSON, json);
        values.put(DatabaseOverAllConstants.ETAG, etag);

        // updating row
        db.update(DatabaseOverAllConstants.TABLE_TIMELINE_ALTER_ASSESSMENT_TRENDS_DATA, values,
                DatabaseOverAllConstants.ASSESSEMENT_ALTER_TRENDS_STRING_ID + " = ?",
                new String[]{mStringLabResultId});


    }

    @Override
    public boolean checkIfGraphRecordExist(String json, String mStringLabResultId, String etag) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = db.rawQuery("select * from " + DatabaseOverAllConstants.TABLE_TIMELINE_ALTER_ASSESSMENT_TRENDS_DATA +
                        values + " where " + DatabaseOverAllConstants.ASSESSEMENT_ALTER_TRENDS_STRING_ID + "=?",
                new String[]{mStringLabResultId});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    @Override
    public void insertImmunization(String json, String identificationToken, String etag) {
        if (checkIfImmunizationRecordExist(json, identificationToken, etag)) {
            this.updateImmunization(json, identificationToken, etag);
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            try {
                ContentValues values = new ContentValues();
                values.put(DatabaseOverAllConstants.JSON, json);
                values.put(DatabaseOverAllConstants.IDENTIFICATION_TOKEN, identificationToken);
                values.put(DatabaseOverAllConstants.ETAG, etag);

                db.insert(DatabaseOverAllConstants.TABLE_ALTER_IMMUNIZATION, null, values);
                db.close();
            } catch (Exception e) {
                Log.e("problem", e + "");
            }
        }
    }

    @Override
    public DbResponse getImmunization(String identificationToken) {
        //   SQLiteDatabase db = this.getReadableDatabase();
        response = new DbResponse();
        Immunization immunization = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabaseOverAllConstants.TABLE_ALTER_IMMUNIZATION, new String[]{
                        DatabaseOverAllConstants.IDENTIFICATION_TOKEN, DatabaseOverAllConstants.JSON,
                        DatabaseOverAllConstants.ETAG},
                DatabaseOverAllConstants.IDENTIFICATION_TOKEN + "=?",
                new String[]{identificationToken}, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                immunization = new Immunization();
                String token = cursor.getString((0));
                String json = cursor.getString(1);
                String etag = cursor.getString(2);
                response.setResponse(json);
                response.setEtag(etag);
            }
            db.close();
        }
        return response;

    }

    @Override
    public void updateImmunization(String json, String identificationToken, String etag) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseOverAllConstants.IDENTIFICATION_TOKEN, identificationToken);
        values.put(DatabaseOverAllConstants.JSON, json);
        values.put(DatabaseOverAllConstants.ETAG, etag);

        // updating row
        db.update(DatabaseOverAllConstants.TABLE_ALTER_IMMUNIZATION, values, DatabaseOverAllConstants.IDENTIFICATION_TOKEN + " = ?",
                new String[]{identificationToken});

    }

    @Override
    public boolean checkIfImmunizationRecordExist(String json, String identificationToken, String etag) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + DatabaseOverAllConstants.TABLE_ALTER_IMMUNIZATION + " where " +
                DatabaseOverAllConstants.IDENTIFICATION_TOKEN + "=?", new String[]{identificationToken});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    @Override
    public void insertMedication(String json, String identificationToken, String etag) {
        response = new DbResponse();
        if (checkIfMedicationRecordExist(json, identificationToken, etag)) {
            this.updateMedication(json, identificationToken, etag);
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            try {

                ContentValues values = new ContentValues();
                values.put(DatabaseOverAllConstants.JSON, json);
                values.put(DatabaseOverAllConstants.IDENTIFICATION_TOKEN, identificationToken);
                values.put(DatabaseOverAllConstants.ETAG, etag);
                db.insert(DatabaseOverAllConstants.TABLE_ALTER_MEDICATION, null, values);
                db.close();
            } catch (Exception e) {
                Log.e("problem", e + "");
            }
        }
    }

    @Override
    public DbResponse getMedicationData(String identificationToken) {
        //   SQLiteDatabase db = this.getReadableDatabase();

        response = new DbResponse();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabaseOverAllConstants.TABLE_ALTER_MEDICATION, new String[]{
                        DatabaseOverAllConstants.IDENTIFICATION_TOKEN, DatabaseOverAllConstants.JSON,
                        DatabaseOverAllConstants.ETAG},
                DatabaseOverAllConstants.IDENTIFICATION_TOKEN + "=?",
                new String[]{identificationToken}, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String token = cursor.getString((0));
                String json = cursor.getString(1);
                String etag = cursor.getString(2);
                response.setResponse(json);
                response.setEtag(etag);
            }
            db.close();
        }
        return response;

    }

    @Override
    public void updateMedication(String json, String identificationToken, String etag) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseOverAllConstants.IDENTIFICATION_TOKEN, identificationToken);
        values.put(DatabaseOverAllConstants.JSON, json);
        values.put(DatabaseOverAllConstants.ETAG, etag);

        // updating row
        db.update(DatabaseOverAllConstants.TABLE_ALTER_MEDICATION, values, DatabaseOverAllConstants.IDENTIFICATION_TOKEN + " = ?",
                new String[]{identificationToken});


    }

    @Override
    public void clearMedication(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseOverAllConstants.TABLE_ALTER_MEDICATION, null, null);
    }

    @Override
    public void clearAllergy(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseOverAllConstants.TABLE_ALTER_ALLERGIES, null, null);
    }

    @Override
    public boolean checkIfMedicationRecordExist(String json, String identificationToken, String etag) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + DatabaseOverAllConstants.TABLE_ALTER_MEDICATION + " where " +
                DatabaseOverAllConstants.IDENTIFICATION_TOKEN + "=?", new String[]{identificationToken});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    @Override
    public void insertDocuments(String json, String identificationToken, String etag) {
        response = new DbResponse();
        if (checkIfDocumentsRecordExist(json, identificationToken, etag)) {
            this.updateDocuments(json, identificationToken, etag);
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            try {

                ContentValues values = new ContentValues();
                values.put(DatabaseOverAllConstants.JSON, json);
                values.put(DatabaseOverAllConstants.IDENTIFICATION_TOKEN, identificationToken);
                values.put(DatabaseOverAllConstants.ETAG, etag);
                db.insert(DatabaseOverAllConstants.TABLE_ALTER_DOCUMENTDATA, null, values);
                db.close();
            } catch (Exception e) {
                Log.e("problem", e + "");
            }
        }

    }

    @Override
    public DbResponse getDocumentsData(String identificationToken) {
        //   SQLiteDatabase db = this.getReadableDatabase();

        response = new DbResponse();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabaseOverAllConstants.TABLE_ALTER_DOCUMENTDATA, new String[]{
                        DatabaseOverAllConstants.IDENTIFICATION_TOKEN, DatabaseOverAllConstants.JSON,
                        DatabaseOverAllConstants.ETAG},
                DatabaseOverAllConstants.IDENTIFICATION_TOKEN + "=?",
                new String[]{identificationToken}, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String token = cursor.getString((0));
                String json = cursor.getString(1);
                String etag = cursor.getString(2);
                response.setResponse(json);
                response.setEtag(etag);
            }
            db.close();
        }
        return response;

    }

    @Override
    public void updateDocuments(String json, String identificationToken, String etag) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseOverAllConstants.IDENTIFICATION_TOKEN, identificationToken);
        values.put(DatabaseOverAllConstants.JSON, json);
        values.put(DatabaseOverAllConstants.ETAG, etag);

        // updating row
        db.update(DatabaseOverAllConstants.TABLE_ALTER_DOCUMENTDATA, values, DatabaseOverAllConstants.IDENTIFICATION_TOKEN + " = ?",
                new String[]{identificationToken});


    }

    @Override
    public boolean checkIfDocumentsRecordExist(String json, String identificationToken, String etag) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + DatabaseOverAllConstants.TABLE_ALTER_DOCUMENTDATA + " where " +
                DatabaseOverAllConstants.IDENTIFICATION_TOKEN + "=?", new String[]{identificationToken});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    @Override
    public void clearDocuments(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseOverAllConstants.TABLE_ALTER_DOCUMENTDATA, null, null);
    }

    @Override
    public void addNotification(NotificationModel city) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseOverAllConstants.ALTER_KEY_ID, city.getId());
            values.put(DatabaseOverAllConstants.ALTER_KEY_TITLE, city.getTitle());
            values.put(DatabaseOverAllConstants.ALTER_KEY_TYPE_OF_NOTIFICATION, city.getType_of_notification());
            values.put(DatabaseOverAllConstants.ALTER_KEY_CREATED_AT, city.getCreated_at());
            values.put(DatabaseOverAllConstants.ALTER_KEY_DESCRIPTION, city.getDescription());
            values.put(DatabaseOverAllConstants.ALTER_KEY_FAMILYMEMBER, city.getFamily_member_id());
            values.put(DatabaseOverAllConstants.ALTER_KEY_CUSTOMER, city.getCustomer_id());
            db.insert(DatabaseOverAllConstants.TABLE_ALTER_NOTIFICATION_NAME, null, values);
            db.close();
        } catch (Exception e) {
            Log.e("problem", e + "");
        }
    }

    @Override
    public ArrayList<NotificationModel> getAllNotification() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<NotificationModel> notificationList = null;
        try {
            notificationList = new ArrayList<NotificationModel>();
            String QUERY = "SELECT * FROM " + DatabaseOverAllConstants.TABLE_ALTER_NOTIFICATION_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    NotificationModel city = new NotificationModel();
                    city.setId(cursor.getInt(0));
                    city.setTitle(cursor.getString(1));
                    city.setType_of_notification(cursor.getString(2));
                    city.setCreated_at(cursor.getString(3));
                    city.setDescription(cursor.getString(4));
                    city.setFamily_member_id(cursor.getString(5));
                    city.setCustomer_id(cursor.getString(6));
                    notificationList.add(city);
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return notificationList;
    }

    @Override
    public int getNotificationCount() {
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String QUERY = "SELECT * FROM " + DatabaseOverAllConstants.TABLE_ALTER_NOTIFICATION_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            db.close();
            return num;
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return 0;
    }

    @Override
    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseOverAllConstants.TABLE_ALTER_NOTIFICATION_NAME, null, null);

    }

    @Override
    public void deletDataBase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseOverAllConstants.CREATE_ALTER_IMMUNIZATION_TABLE, null, null);
        db.delete(DatabaseOverAllConstants.CREATE_ALTER_DOCUMENTDATA__TABLE, null, null);
        db.delete(DatabaseOverAllConstants.CREATE_ALTER_TIMELINE_ASSESSMENT_TABLE, null, null);
        db.delete(DatabaseOverAllConstants.CREATE_ALTER_TIMELINE_ASSESSMENT_TRENDS_TABLE, null, null);
        db.delete(DatabaseOverAllConstants.CREATE_ALTER_TIMELINE_TABLE, null, null);
        db.delete(DatabaseOverAllConstants.CREATE_ALTER_NOTIFICATION_TABLE, null, null);
        db.close();
    }

    @Override
    public void Deleteitem(String string) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM TABLE_NAME WHERE KEY_ID=" + string;
        db.execSQL(query);
    }

    @Override
    public void clearDatabase(boolean flag) {
        if(flag){
            SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper

            db.delete(DatabaseOverAllConstants.TABLE_ALTER_DOCUMENTDATA, null, null);
            db.delete(DatabaseOverAllConstants.TABLE_ALTER_IMMUNIZATION, null, null);
            db.delete(DatabaseOverAllConstants.TABLE_ALTER_NOTIFICATION_NAME, null, null);
            db.delete(DatabaseOverAllConstants.TABLE_ALTER_TIMELINEDATA, null, null);
            db.delete(DatabaseOverAllConstants.TABLE_TIMELINE_ALTER_ASSESSMENT_DATA, null, null);
            db.delete(DatabaseOverAllConstants.TABLE_TIMELINE_ALTER_ASSESSMENT_TRENDS_DATA, null, null);
            db.delete(DatabaseOverAllConstants.TABLE_CHAT, null, null);
        }
    }

    @Override
    public void setSyncData(SyncModel syncModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseOverAllConstants.SYNC_URL, syncModel.getURL());
            values.put(DatabaseOverAllConstants.SYNC_JSON, syncModel.getJSON());
            values.put(DatabaseOverAllConstants.SYNC_METHOD_TYPE, syncModel.getMETHOD());

            db.insert(DatabaseOverAllConstants.TABLE_SYNC, null, values);
            db.close();
        } catch (Exception e) {
            Log.e("problem", e + "");
        }
    }

    @Override
    public void clearSyncDatabase(String string) {
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        //String query = "DELETE FROM " + DatabaseOverAllConstants.TABLE_SYNC + "WHERE" + DatabaseOverAllConstants.SYNC_JSON +"=" + string;
        //db.execSQL(query);
        db.delete(DatabaseOverAllConstants.TABLE_SYNC, DatabaseOverAllConstants.SYNC_JSON +"=?", new String[]{string});
    }

    @Override
    public ArrayList<SyncModel> getSyncData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<SyncModel> syncModelArrayList = null;
        try {
            syncModelArrayList = new ArrayList<SyncModel>();
            String QUERY = "SELECT * FROM " + DatabaseOverAllConstants.TABLE_SYNC;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    SyncModel syncModel = new SyncModel();
                    syncModel.setURL(cursor.getString(1));
                    syncModel.setJSON(cursor.getString(2));
                    syncModel.setMETHOD(cursor.getString(3));

                    syncModelArrayList.add(syncModel);
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return syncModelArrayList;
    }

    @Override
    public void insertAllergieData(String json, String identificationToken, String etag) {
        response = new DbResponse();
        if (checkIfallergiesRecordExist(json, identificationToken, etag)) {
            this.updateallergies(json, identificationToken, etag);
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            try {

                ContentValues values = new ContentValues();
                values.put(DatabaseOverAllConstants.JSON, json);
                values.put(DatabaseOverAllConstants.IDENTIFICATION_TOKEN, identificationToken);
                values.put(DatabaseOverAllConstants.ETAG, etag);
                db.insert(DatabaseOverAllConstants.TABLE_ALTER_ALLERGIES, null, values);
                db.close();
            } catch (Exception e) {
                Log.e("problem", e + "");
            }
        }
    }

    @Override
    public DbResponse getallergiesData(String identificationToken) {
        response = new DbResponse();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabaseOverAllConstants.TABLE_ALTER_ALLERGIES, new String[]{
                        DatabaseOverAllConstants.IDENTIFICATION_TOKEN, DatabaseOverAllConstants.JSON,
                        DatabaseOverAllConstants.ETAG},
                DatabaseOverAllConstants.IDENTIFICATION_TOKEN + "=?",
                new String[]{identificationToken}, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String token = cursor.getString((0));
                String json = cursor.getString(1);
                String etag = cursor.getString(2);
                response.setResponse(json);
                response.setEtag(etag);
            }
            db.close();
        }
        return response;

    }

    @Override
    public void updateallergies(String json, String identificationToken, String etag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseOverAllConstants.IDENTIFICATION_TOKEN, identificationToken);
        values.put(DatabaseOverAllConstants.JSON, json);
        values.put(DatabaseOverAllConstants.ETAG, etag);

        // updating row
        db.update(DatabaseOverAllConstants.TABLE_ALTER_ALLERGIES, values, DatabaseOverAllConstants.IDENTIFICATION_TOKEN + " = ?",
                new String[]{identificationToken});

    }

    @Override
    public boolean checkIfallergiesRecordExist(String json, String identificationToken, String etag) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + DatabaseOverAllConstants.TABLE_ALTER_ALLERGIES + " where " +
                DatabaseOverAllConstants.IDENTIFICATION_TOKEN + "=?", new String[]{identificationToken});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    /////////////////////chat datbase//////////////////
    @Override
    public void insertChatData(ChatDatabaseModel chatDatabaseModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseOverAllConstants.CHAT_SOURCE, chatDatabaseModel.getChatSource());
            values.put(DatabaseOverAllConstants.CHAT_TYPE, chatDatabaseModel.getChatType());
            values.put(DatabaseOverAllConstants.CHAT_JSON, chatDatabaseModel.getChatJson());

            db.insert(DatabaseOverAllConstants.TABLE_CHAT, null, values);
            db.close();
        } catch (Exception e) {
            Log.e("problem", e + "");
        }
    }

    @Override
    public void clearChatDatabase(String string) {
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(DatabaseOverAllConstants.TABLE_CHAT, DatabaseOverAllConstants.CHAT_JSON +"=?", new String[]{string});
    }

    @Override
    public ArrayList<ChatDatabaseModel> getChatData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ChatDatabaseModel> chatDatabaseModelArrayList = null;
        try {
            chatDatabaseModelArrayList = new ArrayList<>();
            String QUERY = "SELECT * FROM " + DatabaseOverAllConstants.TABLE_CHAT ;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    ChatDatabaseModel chatDatabaseModel = new ChatDatabaseModel(cursor.getString(2),cursor.getString(1),cursor.getString(3));

                    chatDatabaseModelArrayList.add(chatDatabaseModel);
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return chatDatabaseModelArrayList;
    }

    @Override
    public void clearAllChat(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseOverAllConstants.TABLE_CHAT, null, null);
    }

}