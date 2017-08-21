package com.DataStorage;

import android.net.Uri;

/**
 * Created by Ajay on 19-04-2016.
 */
public class DatabaseOverAllConstants
    
{
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_ALTER_NAME = "eKinCareCache";

    public static final String TABLE_ALTER_IMMUNIZATION = "AlterImmunization";
    public static final String IDENTIFICATION_TOKEN = "identification_token";
    public static final String JSON = "json";

    public static final String TABLE_ALTER_TIMELINEDATA = "AlterTimelineData";
    public static final String TABLE_TIMELINE_ALTER_ASSESSMENT_DATA = "AlterTimelineAssessmentData";
    public static final String ALTER_ASSESSEMENT_STRING_ID = "AltermStringId";

    public static final String TABLE_ALTER_DOCUMENTDATA = "AlterDocumentData";
    public static final String TABLE_TIMELINE_ALTER_ASSESSMENT_TRENDS_DATA = "AlterTimelineAssessmentTrendsData";
    public static final String ASSESSEMENT_ALTER_TRENDS_STRING_ID = "AltermStringLabResultId";
    public static final String ETAG = "etag";
    public static final String TABLE_ALTER_NOTIFICATION_NAME = "alter_notification_table";
    public static final String ALTER_KEY_ID = "_id";
    public static final String ALTER_KEY_TITLE = "_title";
    public static final String ALTER_KEY_TYPE_OF_NOTIFICATION = "_type_of_notification";
    public static final String ALTER_KEY_CREATED_AT = "_created_at";
    public static final String ALTER_KEY_DESCRIPTION = "_description";
    public static final String ALTER_KEY_FAMILYMEMBER = "_family_member_id";
    public static final String ALTER_KEY_CUSTOMER = "_customer_id";

    public static final String TABLE_SYNC = "syncTable";
    public static final String SYNC_ID = "sync_id";
    public static final String SYNC_URL = "sync_url";
    public static final String SYNC_JSON = "sync_json";
    public static final String SYNC_METHOD_TYPE = "sync_method_type";

    public static final String TABLE_CHAT = "chatTable";
    public static final String CHAT_ID = "chat_id";
    public static final String CHAT_TYPE = "chat_type";
    public static final String CHAT_JSON = "chat_json";
    public static final String CHAT_SOURCE = "chat_source";
    public static final String CHAT_DATE= "chat_date";
    public static final String CHAT_TIME = "chat_time";

    public static final String TABLE_ALTER_MEDICATION = "medicationTable";

    public static final String TABLE_ALTER_ALLERGIES = "AlterAllergiesData";


    //IMMUNIZATION
    public static final String CREATE_ALTER_IMMUNIZATION_TABLE = "CREATE TABLE " + TABLE_ALTER_IMMUNIZATION + " (" + IDENTIFICATION_TOKEN + " TEXT,"
            + JSON + " TEXT," + ETAG + " TEXT)";
    public static final String DROP_IMMUNIZATION_TABLE = "DROP TABLE IF EXISTS " + TABLE_ALTER_IMMUNIZATION;


    //TIMELINE
    public static final String CREATE_ALTER_TIMELINE_TABLE = "CREATE TABLE " + TABLE_ALTER_TIMELINEDATA + " (" + IDENTIFICATION_TOKEN + " TEXT,"
            + JSON + " TEXT," + ETAG + " TEXT)";
    public static final String DROP_TIMELINE_TABLE = "DROP TABLE IF EXISTS " + TABLE_ALTER_TIMELINEDATA;

    //ASSESSMENTTIMELINE
    public static final String CREATE_ALTER_TIMELINE_ASSESSMENT_TABLE = "CREATE TABLE " + TABLE_TIMELINE_ALTER_ASSESSMENT_DATA + " (" +
            ALTER_ASSESSEMENT_STRING_ID + " TEXT,"
            + JSON + " TEXT," + ETAG + " TEXT)";
    public static final String DROP_TIMELINE_ASSESSMENT_TABLE = "DROP TABLE IF EXISTS " + TABLE_TIMELINE_ALTER_ASSESSMENT_DATA;

    //DOCUMENTS
    public static final String CREATE_ALTER_DOCUMENTDATA__TABLE = "CREATE TABLE " + TABLE_ALTER_DOCUMENTDATA + " (" + IDENTIFICATION_TOKEN + " TEXT,"
            + JSON + " TEXT," + ETAG + " TEXT)";
    public static final String DROP_DOCUMENTS_TABLE = "DROP TABLE IF EXISTS " + TABLE_ALTER_DOCUMENTDATA;

    //ALLERGIES
    public static final String CREATE_ALTER_ALLERGIEDATA_TABLE = "CREATE TABLE " + TABLE_ALTER_ALLERGIES + " (" + IDENTIFICATION_TOKEN + " TEXT,"
            + JSON + " TEXT," + ETAG + " TEXT)";
    public static final String DROP_ALLERGIE_TABLE = "DROP TABLE IF EXISTS " + TABLE_ALTER_ALLERGIES;



    //MEDICATION
    public static final String CREATE_ALTER_MEDICATION_TABLE = "CREATE TABLE " + TABLE_ALTER_MEDICATION + " (" + IDENTIFICATION_TOKEN + " TEXT,"
            + JSON + " TEXT," + ETAG + " TEXT)";
    public static final String DROP_MEDICATION_TABLE = "DROP TABLE IF EXISTS " + TABLE_ALTER_MEDICATION;


    //TRENDSTIMELINE
    public static final String CREATE_ALTER_TIMELINE_ASSESSMENT_TRENDS_TABLE = "CREATE TABLE " +
            TABLE_TIMELINE_ALTER_ASSESSMENT_TRENDS_DATA + " (" + ASSESSEMENT_ALTER_TRENDS_STRING_ID + " TEXT,"
            + JSON + " TEXT," + ETAG + " TEXT)";
    public static final String DROP_TIMELINE_ASSESSEMENTS_TRENDS_TABLE = "DROP TABLE IF EXISTS " + TABLE_TIMELINE_ALTER_ASSESSMENT_TRENDS_DATA;

    //NOTIFICATIONS

    public static final String CREATE_ALTER_NOTIFICATION_TABLE = "CREATE TABLE " + TABLE_ALTER_NOTIFICATION_NAME + " (" + ALTER_KEY_ID +
            " INTEGER PRIMARY KEY,"
            + ALTER_KEY_TITLE + " TEXT," +
            ALTER_KEY_TYPE_OF_NOTIFICATION + " TEXT," +
            ALTER_KEY_CREATED_AT + " TEXT," + ALTER_KEY_DESCRIPTION + " TEXT," +
            ALTER_KEY_FAMILYMEMBER + " TEXT," + ALTER_KEY_CUSTOMER + " TEXT)";


    //SYNC
    public static final String CREATE_SYNC_TABLE = "CREATE TABLE " + TABLE_SYNC + " (" + SYNC_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SYNC_URL + " TEXT," +
            SYNC_JSON + " TEXT," +
            SYNC_METHOD_TYPE + " TEXT)";
    public static final String DROP_ALTER_NOTIFICATION_TABLE = "DROP TABLE IF EXISTS " + TABLE_ALTER_NOTIFICATION_NAME;

    //CHAT TABLE
    public static final String CREATE_CHAT_TABLE = "CREATE TABLE " + TABLE_CHAT + " (" +
            CHAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            CHAT_SOURCE + " TEXT," +
            CHAT_TYPE + " TEXT," +
            CHAT_JSON + " TEXT)";
    public static final String DROP_ALTER_CHAT_TABLE = "DROP TABLE IF EXISTS " + TABLE_CHAT;



}
