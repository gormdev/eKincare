package com.DataStorage;

import com.message.model.ChatDatabaseModel;
import com.oneclick.ekincare.vo.NotificationModel;
import com.oneclick.ekincare.vo.SyncModel;

import java.util.ArrayList;

/**
 * Created by Ajay on 19-04-2016.
 */
public interface DatabaseOverAllListener {

    public void insertTimeline(String json, String identificationToken, String etag);

    //inseted at 17
    public void clearTimeline();

    public void clearAllergy();

    public DbResponse getTimelineData(String identificationToken);

    public void updateTimeline(String json, String identificationToken, String etag);

    public boolean checkIfTimelineRecordExist(String json, String identificationToken, String etag);

    public void insertAssessmentData(String json, String mStringId, String etag);

    public DbResponse getAllAssessmentData(String mStringId);

    public void updateAssessmentData(String json, String mStringId, String etag);

    public boolean checkIfAssessmentRecordExist(String json, String mStringId, String etag);

    public void insertGraphData(String json, String mStringLabResultId, String etag);

    public DbResponse getAllTrendsData(String mStringLabResultId);

    public void updateGraphData(String json, String mStringLabResultId, String etag);

    public boolean checkIfGraphRecordExist(String json, String mStringLabResultId, String etag);

    public void insertImmunization(String json, String identificationToken, String etag);

    public DbResponse getImmunization(String identificationToken);

    public void updateImmunization(String json, String identificationToken, String etag);

    public boolean checkIfImmunizationRecordExist(String json, String identificationToken, String etag);

    public void insertDocuments(String json, String identificationToken, String etag);

    public DbResponse getDocumentsData(String identificationToken);

    public void updateDocuments(String json, String identificationToken, String etag);

    public void clearDocuments();

    public boolean checkIfDocumentsRecordExist(String json, String identificationToken, String etag);

    public void insertMedication(String json, String identificationToken, String etag);

    public DbResponse getMedicationData(String identificationToken);

    public void updateMedication(String json, String identificationToken, String etag);

    public boolean checkIfMedicationRecordExist(String json, String identificationToken, String etag);


    public void addNotification(NotificationModel city);

    public ArrayList<NotificationModel> getAllNotification();

    public int getNotificationCount();

    public void deleteAllData();

    public void deletDataBase();

    public void Deleteitem(String string);

    public void clearDatabase(boolean flag);

    public void setSyncData(SyncModel syncModel);

    public void clearMedication();

    public void clearSyncDatabase(String string);

    public ArrayList<SyncModel> getSyncData();

    public void insertAllergieData(String json, String identificationToken, String etag);

    public DbResponse getallergiesData(String identificationToken);

    public void updateallergies(String json, String identificationToken, String etag);

    public boolean checkIfallergiesRecordExist(String json, String identificationToken, String etag);

    public void insertChatData(ChatDatabaseModel chatDatabaseModel);

    public void clearChatDatabase(String string);

    public ArrayList<ChatDatabaseModel> getChatData();

    public void clearAllChat();
}
