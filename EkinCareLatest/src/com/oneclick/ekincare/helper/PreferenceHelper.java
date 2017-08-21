package com.oneclick.ekincare.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;


import java.util.HashSet;
import java.util.Set;

public class PreferenceHelper  {

    private final SharedPreferences mPrefs;

    public PreferenceHelper(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private String PREF_IsLogin = "IsLogin";
    private String PREF_AppToken = "AppToken";
    private String PREF_UserId = "UserId";

    private String PREF_FIRST_USER_WIZARD = "IsWizardCompliteFirst";

    private String PREF_Lat = "Lat";
    private String PREF_Long = "Long";

    private String PREF_UserName = "UserName";
    private String PREF_CustomerName = "CustomerName";
    private String PREF_Password = "Password";
    private String PREF_Device_Password = "Device_Password";
    private String PREF_Device_Name = "Device_Name";

    private String PREF_X_EKINCARE_KEY = "EkinCareKey";
    private String PREF_X_CUSTOMER_KEY = "CustomerKey";
    private String PREF_ETAG = "Etag";

    private String PREF_ProfileModel = "ProfileModel";
    private String PREF_ProfileData = "ProfileData";
    private String PREF_Immunization = "Immunization";
    private String PREF_BasicCustomer = "BasicCustomer";

    private String PREF_IsFirstInstall = "IsFirstInstall";

    private String PREF_FamilyMemCount = "FamilyMemCount";

    private String PREF_IsWizardComplete = "IsWizardComplete";

    private String PREF_LastQuestion = "LastQuestion";

    private String PREF_IsFamilyWizrdComplete = "IsFamilyWizrdComplete";

    private String PREF_IsLoginVisible = "IsLoginVisible";
    private String PREF_IsHaveCodeVisible = "IsHaveCodeVisible";

    private String PREF_SelectedProfileId = "SelectedProfileId";
    private String PREF_IsYou = "IsYou";

    private String PREF_Target = "Target";
    private String PREF_BloodGroup = "BloodGroup";

    private String PREF_HydrocareIntakesTimeStamp = "HydrocareIntakesTimeStamp";
    private String PREF_TemperatureTimeStamp = "TemperatureTimeStamp";
    private String PREF_Temperature = "Temperature";

    private String PREF_isHydrocareSubscriptionEnable = "isHydrocareSubscriptionEnable";
    private String PREF_isBloodSOSSubscriptionEnable = "isBloodSOSSubscriptionEnable";

    private String PREF_YouCustomer = "YouCustomer";
    private String PREF_FamilyMemberList = "FamilyMemberList";

    private String PREF_HydrocareSubscripted = "HydrocareSubscripted";
    //private String PREF_RegistrationId = "regId";
    private String REG_ID = "regId";
    private String APP_VERSION = "appVersion";
    static final String TAG = "RegisterGcmIdInfo";
    private String PREF_NotificationCount = "NotifiCount";

    private String PREF_StepsCount = "StepsCount";
    private String PREF_CaloriesCount = "CaloriesCount";
    private String PREF_DistanceCount = "DistanceCount";

    public final static String IS_HYDROCARE_NOTIFICATION_ENABLE = "isHydrocareNotificationEnable";
    public final static String IS_HYDROCARE_SUBSCRIPTION_ENABLE = "isHydrocareSubscriptionEnable";


    private String PREF_PACKAGETYPE = "PackageType";
    private String PACKAGE_RECOMMENDEDSEARCH = "PackageRecommendedSearch";
    private String PREF_PACKAGEID = "PackageID";
    private String PREF_PACKAGEMRP = "PackageMrp";
    private String PREF_PACKAGESRP = "PackageSrp";
    private String PREF_DISCOUNT = "PackageDiscount";
    private String PREF_PACKAGEINFO = "PackageInfo";
    private String PREF_PACKAGETITLE = "PackageTitle";
    private String PREF_PROVIDER_LINE1 = "ProviderAddress";
    private String PREF_PROVIDER_POC = "ProviderPersonContact";
    private String PREF_PROVIDER_POC_NUMBER = "ProviderPersonContactNumber";
    private String PREF_PROVIDER_NAME = "ProviderName";
    private String PREF_EMPTY_STOMACH = "EmptyStomach";
    private String PREF_PROVIDER_ID = "ProviderID";
    private String PREF_UNIQUE_ID = "UniqueID";
    private String PREF_APPOINTMENT_TIME = "Time";
    private String PREF_FIRSTAPPOINTMENT = "FirstAppointment";
    private String PREF_APPOINTMENT_TIME_DATE = "FirstDate";
    private String PREF_CURRENT_MONTH = "CurrentMonth";
    private String PREFS_DATE_TIME_FORMAT = "TimeDateFormat";
    private String PREFS_CURRENT_YEAR = "CurrentYear";
    private String PREFS_HOMECARE_TIME_DATE_YEAR="HomecareTimeDate";
    private String PREF_LOCATION_LAT = "Locationlatitude";
    private String PREF_LOCATION_LAN = "Locationlongitude";
    private String PREF_SAVE_LOC_VALUE = "SaveLoc";
    private String PREF_SAVE_LOC_NAME = "SaveGoogleLocAddress";
    private String PREF_CONSULT_URL = "ConsultUrl";
    private String PREF_TRENDS="TrendsId";
    private String PREF_PACKAGE_LAT = "SaveLatitude";
    private String PREF_PACKAGE_LANG = "Savelongitude";

    private String PREF_INSTALL_TIME = "InstallTime";

    private String PREF_INSTALL_TWODAY = "InstallTimeTwodays";

    private String PREF_INSTALL_FIVEDAYS = "InstallTimeFIVE";

    private String PREF_INSTALL_EIGHT = "InstallTimeEIGHT";

    private String PREF_APPALL_NOTIFICATION = "allNotification";

    private String PREF_UPDATE_WATER="updateWater";
    private String PREF_UPDATE_WATER_TOTAL="totalUpdateWater";

    private String PREF_RATING_FOR_COUNT="CountsPerDay";
    private String PREF_RATING_FOR_DAY="CountOfDay";
    private String PREF_IS_RATED="IsRated";

    private String PREF_ATE_ON_TIME="ateOnTime";
    private String PREF_SMOKED_TODAY="smokedToday";
    private String PREF_DRINKED_ALCOHOL="drinkedAlcohol";
    private String PREF_DRINKED_COFFEE="drinkedCoffee";
    private String PREF_SLEEP_ON_TIME="sleepOnTime";
    private String PREF_HAD_BREAKFAST="hadbreakfast";
    private String PREF_SKIPPED_MEAL="skippedMeals";
    private String PREF_HAD_GOODSLEEP="hadGoodSleep";
    private String PREF_TODAY_HOW_FEEL="howWasDay";

    private String PREF_SLEEP_TIME="sleepTime";
    private String PREF_WOKE_TIME="wakeTime";
    private String PREF_IMAGE_URL="imageUrl";

    private String PREF_CUSTOMER_PROFILE_IMAGE_COLOR="imageColor";

    private String PREF_CUSTOMER_TOKEN="customerNToken";

    private String PREF_IS_ANY_DIGITIZED_RECORED="isAnyDigitizedRecords";
    private String PREF_HAS_SEEN_DIGITIZED_RECORED="hasSeenDigitizedRecords";

    private String PREF_HAS_HOW_WAS_YOUR_DAY_DATA_FILLED="showHowWasYourDay";

    private String FAMILY_MEMBER_LIST="totalFamilyMemberList";

    private String PREF_TAKEN_MORNING_MEDICATION_ID = "medicationMorningId";
    private String PREF_TAKEN_EVENING_MEDICATION_ID = "medicationEveningId";
    private String PREF_TAKEN_AFTERNOON_MEDICATION_ID = "medicationAfternoonId";

    private String PREF_HAS_SEEN_TUTORIAL = "hasSeenTutorial";
    private String PREF_HAS_SEEN_ACTIVITY_FAMILY_MEMBER_TUTORIAL = "hasSeenActivityFamilyMemberTutorial";
    private String PREF_HAS_SEEN_DOC_FAMILY_MEMBER_TUTORIAL = "hasSeenDOCFamilyMemberTutorial";
    private String PREF_HAS_SEEN_MEDICICATION_FAMILY_MEMBER_TUTORIAL = "hasSeenMedicationFamilyMemberTutorial";
    private String PREF_HAS_SEEN_ALLERGY_FAMILY_MEMBER_TUTORIAL = "hasSeenAllergyFamilyMemberTutorial";
    private String PREF_HAS_SEEN_HISTORY_FAMILY_MEMBER_TUTORIAL = "hasSeenHistoryFamilyMemberTutorial";
    private String PREF_HAS_SEEN_DOCUMENT_UPLOAD_TUTORIAL = "hasSeenDocumentUploadTutorial";
    private String PREF_NEVER_SHOW_DOCUMENT_UPLOAD_TUTORIAL = "hasSeenDocumentUploadTutorial";


    private String PREF_RUNTIME_PERMISSION="runtimePermission";

    private String PREF_DOC_MAIN="MAIN_DOC";
    private String PREF_IS_FIRST_TIME_CHAT="isFirstTimeChat";
    private String PREF_DOCTORID = "DoctorID";

    private String PREF_IsFirstTimeRegister = "IsFirstTimeRegister";
    private String PREF_LOGGED_IN_CUSTOMER_DOB = "loggedInCustomerDob";
    private String PREF_LOGGED_IN_CUSTOMER_GENDER = "loggedInCustomerGender";
    private String PREF_LOGGED_IN_CUSTOMER_WIZARD = "loggedInCustomerWizard";

    private String PREF_IS_GOOGLE_FIT_CONNECTED = "isGoogleFitConnected";

    private static final String PREF_IS_HRA_COMPLETED = "isHraCompleted";


    Context context;

    public SharedPreferences getmPrefs() {
        return mPrefs;
    }


    public boolean getIsGoogleFitConnected() {
        boolean str = mPrefs.getBoolean(PREF_IS_GOOGLE_FIT_CONNECTED, false);
        return str;
    }

    public void setIsGoogleFitConnected(boolean pref_isfirsttimeregister) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_IS_GOOGLE_FIT_CONNECTED, pref_isfirsttimeregister);
        mEditor.commit();
    }


    public boolean getIsFirstTimeRegister() {
        boolean str = mPrefs.getBoolean(PREF_IsFirstTimeRegister, true);
        return str;
    }

    public void setIsFirstTimeRegister(boolean pref_isfirsttimeregister) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_IsFirstTimeRegister, pref_isfirsttimeregister);
        mEditor.commit();
    }

    public boolean getIsHraCompleted() {
        boolean str = mPrefs.getBoolean(PREF_IS_HRA_COMPLETED, false);
        return str;
    }

    public void setIsHraCompleted(boolean pref_isfirsttimeregister) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_IS_HRA_COMPLETED, pref_isfirsttimeregister);
        mEditor.commit();
    }

    public String getCustomerDOB() {
        String str = mPrefs.getString(PREF_LOGGED_IN_CUSTOMER_DOB, "");
        return str;
    }

    public void setLoggedinUserDOB(String pref_modequery) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_LOGGED_IN_CUSTOMER_DOB, pref_modequery);
        mEditor.commit();
    }

    public String getCustomerGender() {
        String str = mPrefs.getString(PREF_LOGGED_IN_CUSTOMER_GENDER, "");
        return str;
    }

    public void setLoggedinUserGender(String pref_modequery) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_LOGGED_IN_CUSTOMER_GENDER, pref_modequery);
        mEditor.commit();
    }

    public int getCustomerWizardStatus() {
        return mPrefs.getInt(PREF_LOGGED_IN_CUSTOMER_WIZARD, 0);
    }

    public void setCustomerWizardStatus(int data) {
        Editor mEditor = mPrefs.edit();
        mEditor.putInt(PREF_LOGGED_IN_CUSTOMER_WIZARD, data);
        mEditor.commit();
    }
    public String getPREF_DOCTORID() {
        String str = mPrefs.getString(PREF_DOCTORID, "");
        return str;
    }

    public void setPREF_DOCTORID(String pref_doctorid) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_DOCTORID, pref_doctorid);
        mEditor.commit();
    }

    public boolean getIsFirstTimeChat() {
        boolean bool = mPrefs.getBoolean(PREF_IS_FIRST_TIME_CHAT,true);
        return bool;
    }

    public void setIsFirstTimeChat(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_IS_FIRST_TIME_CHAT, flag);
        mEditor.commit();
    }


    public boolean getIsMainDoc() {
        boolean bool = mPrefs.getBoolean(PREF_DOC_MAIN,true);
        return bool;
    }

    public void setIsMainDoc(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_DOC_MAIN, flag);
        mEditor.commit();
    }

    public boolean getHasSeenDocUploadTuts() {
        boolean bool = mPrefs.getBoolean(PREF_HAS_SEEN_DOCUMENT_UPLOAD_TUTORIAL,false);
        return bool;
    }

    public void setHasSeenDocUploadTuts(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_HAS_SEEN_DOCUMENT_UPLOAD_TUTORIAL, flag);
        mEditor.commit();
    }

    public boolean getNeverShowDocUploadTuts() {
        boolean bool = mPrefs.getBoolean(PREF_NEVER_SHOW_DOCUMENT_UPLOAD_TUTORIAL,false);
        return bool;
    }

    public void setNeverShowDocUploadTuts(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_NEVER_SHOW_DOCUMENT_UPLOAD_TUTORIAL, flag);
        mEditor.commit();
    }

    public String getPREF_RUNTIME_PERMISSION() {
        String str = mPrefs.getString(PREF_RUNTIME_PERMISSION, "");
        return str;

    }

    public void setPREF_RUNTIME_PERMISSION(String pref_runtime_permission) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_RUNTIME_PERMISSION, pref_runtime_permission);
        mEditor.commit();
    }

    public boolean getHasSeenTutorial() {
        boolean bool = mPrefs.getBoolean(PREF_HAS_SEEN_TUTORIAL,false);
        return bool;
    }

    public void setHasSeenTutorial(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_HAS_SEEN_TUTORIAL, flag);
        mEditor.commit();
    }

    public boolean getHasSeenActivityFamilyMemeberTutorial() {
        boolean bool = mPrefs.getBoolean(PREF_HAS_SEEN_ACTIVITY_FAMILY_MEMBER_TUTORIAL,false);
        return bool;
    }

    public void setHasSeenActivityFamilyMemeberTutorial(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_HAS_SEEN_ACTIVITY_FAMILY_MEMBER_TUTORIAL, flag);
        mEditor.commit();
    }

    public boolean getHasSeenDocumentFamilyMemeberTutorial() {
        boolean bool = mPrefs.getBoolean(PREF_HAS_SEEN_DOC_FAMILY_MEMBER_TUTORIAL,false);
        return bool;
    }

    public void setHasSeenDocumentFamilyMemeberTutorial(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_HAS_SEEN_DOC_FAMILY_MEMBER_TUTORIAL, flag);
        mEditor.commit();
    }

    public boolean getHasSeenHistoryFamilyMemeberTutorial() {
        boolean bool = mPrefs.getBoolean(PREF_HAS_SEEN_HISTORY_FAMILY_MEMBER_TUTORIAL,false);
        return bool;
    }

    public void setHasSeenHistoryFamilyMemeberTutorial(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_HAS_SEEN_HISTORY_FAMILY_MEMBER_TUTORIAL, flag);
        mEditor.commit();
    }

    public boolean getHasSeenMedeicationFamilyMemeberTutorial() {
        boolean bool = mPrefs.getBoolean(PREF_HAS_SEEN_MEDICICATION_FAMILY_MEMBER_TUTORIAL,false);
        return bool;
    }

    public void setHasSeenMedicationFamilyMemeberTutorial(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_HAS_SEEN_MEDICICATION_FAMILY_MEMBER_TUTORIAL, flag);
        mEditor.commit();
    }

    public boolean getHasSeenAllergyFamilyMemeberTutorial() {
        boolean bool = mPrefs.getBoolean(PREF_HAS_SEEN_ALLERGY_FAMILY_MEMBER_TUTORIAL,false);
        return bool;
    }

    public void setHasSeenAllergyFamilyMemeberTutorial(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_HAS_SEEN_ALLERGY_FAMILY_MEMBER_TUTORIAL, flag);
        mEditor.commit();
    }

    public String getAfterNoonTakenMedicationId() {
        String str = mPrefs.getString(PREF_TAKEN_AFTERNOON_MEDICATION_ID, "");
        return str;
    }

    public void setAfterNoonTakenMedicationId(String ids) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_TAKEN_AFTERNOON_MEDICATION_ID, ids);
        mEditor.commit();
    }

    public String getMorningTakenMedicationId() {
        String str = mPrefs.getString(PREF_TAKEN_MORNING_MEDICATION_ID, "");
        return str;
    }

    public void setMorningTakenMedicationId(String ids) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_TAKEN_MORNING_MEDICATION_ID, ids);
        mEditor.commit();
    }

    public String getEveningTakenMedicationId() {
        String str = mPrefs.getString(PREF_TAKEN_EVENING_MEDICATION_ID, "");
        return str;
    }

    public void setEveningTakenMedicationId(String ids) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_TAKEN_EVENING_MEDICATION_ID, ids);
        mEditor.commit();
    }

    public String gettotalFamilyMemberList() {
        String str = mPrefs.getString(FAMILY_MEMBER_LIST, "");
        return str;
    }

    public void settotalFamilyMemberList(String family_member_list) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(FAMILY_MEMBER_LIST, family_member_list);
        mEditor.commit();
    }

    public boolean getHasHowWasYourDayDataFilled(){
        boolean flag = mPrefs.getBoolean(PREF_HAS_HOW_WAS_YOUR_DAY_DATA_FILLED, false);
        return flag;
    }

    public void setHasHowWasYourDayDataFilled(boolean flag){
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_HAS_HOW_WAS_YOUR_DAY_DATA_FILLED, flag);
        mEditor.commit();
    }

    public boolean getHasSeenDigitizedRecords(){
        boolean flag = mPrefs.getBoolean(PREF_HAS_SEEN_DIGITIZED_RECORED, false);
        return flag;
    }

    public void setHasSeenDigitizedRecords(boolean flag){
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_HAS_SEEN_DIGITIZED_RECORED, flag);
        mEditor.commit();
    }

    public boolean getIsAnyDigitizedRecords(){
        boolean flag = mPrefs.getBoolean(PREF_IS_ANY_DIGITIZED_RECORED, false);
        return flag;
    }

    public void setIsAnyDigitizedRecords(boolean flag){
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_IS_ANY_DIGITIZED_RECORED, flag);
        mEditor.commit();
    }

    public int getCustomerImageColor(){
        int color = mPrefs.getInt(PREF_CUSTOMER_PROFILE_IMAGE_COLOR, 0);
        return color;
    }




    public void setcustomerNToken(String pref_customer_token){
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_CUSTOMER_TOKEN, pref_customer_token);
        mEditor.commit();


    }






    public void setimageUrl(String pref_image_url){
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_IMAGE_URL, pref_image_url);
        mEditor.commit();


    }

    public String getSleepTime() {
        String  time = mPrefs.getString(PREF_SLEEP_TIME, "0");
        return time;
    }

    public void setSleepTime(String time) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_SLEEP_TIME, time);
        mEditor.commit();
    }

    public String getWakeTime() {
        String  time = mPrefs.getString(PREF_WOKE_TIME, "0");
        return time;
    }

    public void setWakeTime(String time) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_WOKE_TIME, time);
        mEditor.commit();
    }


    private  String PREF_HOW_WAS_DONE="howWasDayDone";

    private  String PREF_HOW_WAS_DAY_REMINDER="howWasDayReminder";




    public boolean gethowWasDayReminder() {
        boolean str = mPrefs.getBoolean(PREF_HOW_WAS_DAY_REMINDER, true);
        return str;
    }

    public void sethowWasDayReminder(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_HOW_WAS_DAY_REMINDER, flag);
        mEditor.commit();
    }



    public String gethowWasDayDone() {
        String str = mPrefs.getString(PREF_HOW_WAS_DONE, "");
        return str;
    }

    public void sethowWasDayDone(String pref_how_was_done) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_HOW_WAS_DONE, pref_how_was_done);
        mEditor.commit();
    }
    public String gethowWasDay() {
        String str = mPrefs.getString(PREF_TODAY_HOW_FEEL, "");
        return str;
    }

    public void sethowWasDay(String pref_today_how_feel) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_TODAY_HOW_FEEL, pref_today_how_feel);
        mEditor.commit();
    }

    public boolean gethadGoodSleep() {
        boolean flag = mPrefs.getBoolean(PREF_HAD_GOODSLEEP, false);
        return flag;
    }

    public void sethadGoodSleep(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_HAD_GOODSLEEP, flag);
        mEditor.commit();
    }

    public boolean getskippedMeals() {
        boolean flag = mPrefs.getBoolean(PREF_SKIPPED_MEAL, false);
        return flag;
    }

    public void setskippedMeals(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_SKIPPED_MEAL, flag);
        mEditor.commit();
    }

    public boolean gethadbreakfast() {
        boolean flag = mPrefs.getBoolean(PREF_HAD_BREAKFAST, false);
        return flag;
    }

    public void sethadbreakfast(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_HAD_BREAKFAST, flag);
        mEditor.commit();
    }


    public boolean getLateNightPhoneUsuage() {
        boolean flag = mPrefs.getBoolean(PREF_SLEEP_ON_TIME, false);
        return flag;
        }

    public void setLateNightPhoneUsuage(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_SLEEP_ON_TIME, flag);
        mEditor.commit();
        }


    public void setDrinkedCoffeeToday(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_DRINKED_COFFEE, flag);
        mEditor.commit();
        }



    public boolean getDrinkedAlcoholToday() {
        boolean flag = mPrefs.getBoolean(PREF_DRINKED_ALCOHOL, false);
        return flag;
        }

    public void setDrinkedAlcoholToday(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_DRINKED_ALCOHOL, flag);
        mEditor.commit();
       }

    public boolean getSmokedToday() {
       boolean flag = mPrefs.getBoolean(PREF_SMOKED_TODAY, false);
       return flag;
        }

    public void setSmokedToday(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_SMOKED_TODAY, flag);
        mEditor.commit();
        }


    public void setAteOnTime(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_ATE_ON_TIME, flag);
        mEditor.commit();
        }

    public boolean getIsRated() {
        boolean flag = mPrefs.getBoolean(PREF_IS_RATED, false);
        return flag;
    }

    public void setIsRated(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_IS_RATED, flag);
        mEditor.commit();
    }


    public void setUserRatingDayCount(int count) {
        Editor mEditor = mPrefs.edit();
        mEditor.putInt(PREF_RATING_FOR_DAY, count);
        mEditor.commit();
    }

    public int getUserRatingCount() {
        int str = mPrefs.getInt(PREF_RATING_FOR_COUNT, 0);
        return str;
    }

    public void setUserRatingCount(int count) {
        Editor mEditor = mPrefs.edit();
        mEditor.putInt(PREF_RATING_FOR_COUNT, count);
        mEditor.commit();
    }

    public String gettotalUpdateWater() {
        String str = mPrefs.getString(PREF_UPDATE_WATER_TOTAL, "");
        return str;
    }

    public void settotalUpdateWater(String pref_update_water_total) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_UPDATE_WATER_TOTAL, pref_update_water_total);
        mEditor.commit();
    }

    public String getupdateWater() {
        String str = mPrefs.getString(PREF_UPDATE_WATER, "");
        return str;
    }

    public void setupdateWater(String pref_update_water) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_UPDATE_WATER, pref_update_water);
        mEditor.commit();
    }

    public boolean getallNotification() {
        boolean flag = mPrefs.getBoolean(PREF_APPALL_NOTIFICATION, true);
        return flag;
    }

    public void setallNotification(boolean flag) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_APPALL_NOTIFICATION, flag);
        mEditor.commit();
    }



    public String getInstallTimeEIGHT() {
        String str = mPrefs.getString(PREF_INSTALL_EIGHT, "");
        return str;
    }


    public String getInstallTimeFIVE() {
        String str = mPrefs.getString(PREF_INSTALL_FIVEDAYS, "");
        return str;
    }


    public String getInstallTimeTwodays() {
        String str = mPrefs.getString(PREF_INSTALL_TWODAY, "");
        return str;
    }







    public String getInstallTime() {
        String str = mPrefs.getString(PREF_INSTALL_TIME, "");
        return str;
    }

    public void setInstallTime(String pref_install_time) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_INSTALL_TIME, pref_install_time);
        mEditor.commit();
    }



    public String getSavelongitude() {
        String str = mPrefs.getString(PREF_PACKAGE_LANG, "");
        return str;
    }

    public void setSavelongitude(String pref_package_lang) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_PACKAGE_LANG, pref_package_lang);
        mEditor.commit();
    }



    public String getSaveLatitude() {
        String str = mPrefs.getString(PREF_PACKAGE_LAT, "");
        return str;
    }

    public void setSaveLatitude(String pref_package_lat) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_PACKAGE_LAT, pref_package_lat);
        mEditor.commit();
    }



    public String getTrendsId(){
        String str = mPrefs.getString(PREF_TRENDS, "");
        return str;

    }

public void setTrendsId(String prefs_trends_id){
    Editor mEditor = mPrefs.edit();
    mEditor.putString(PREF_TRENDS, prefs_trends_id);
    mEditor.commit();

}

    public String getTimeDateFormat() {
        String str = mPrefs.getString(PREFS_DATE_TIME_FORMAT, "");
        return str;
    }

    public void setTimeDateFormat(String prefs_time_date_format) {

        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREFS_DATE_TIME_FORMAT, prefs_time_date_format);
        mEditor.commit();


    }

    public String getSaveGoogleLocAddress() {
        String str = mPrefs.getString(PREF_SAVE_LOC_NAME, "");
        return str;
    }

    public void setSaveGoogleLocAddress(String pref_save_loc_name) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_SAVE_LOC_NAME, pref_save_loc_name);
        mEditor.commit();
    }

    public String getSaveLoc() {
        String str = mPrefs.getString(PREF_SAVE_LOC_VALUE, "");
        return str;
    }

    public void setSaveLoc(String pref_save_loc_value) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_SAVE_LOC_VALUE, pref_save_loc_value);
        mEditor.commit();
    }

    public String getLocationlongitude() {
        String str = mPrefs.getString(PREF_LOCATION_LAN, "");
        return str;
    }

    public void setLocationlongitude(String pref_location_lan) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_LOCATION_LAN, pref_location_lan);
        mEditor.commit();
    }

    public String getLocationlatitude() {
        String str = mPrefs.getString(PREF_LOCATION_LAT, "");
        return str;
    }

    public void setLocationlatitude(String pref_location_lat) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_LOCATION_LAT, pref_location_lat);
        mEditor.commit();
    }

    public String getCurrentYear() {
        String str = mPrefs.getString(PREFS_CURRENT_YEAR, "");
        return str;

    }

    public void setCurrentYear(String prefs_current_year) {

        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREFS_CURRENT_YEAR, prefs_current_year);
        mEditor.commit();

    }


    public String getCurrentMonth() {
        String str = mPrefs.getString(PREF_CURRENT_MONTH, "");
        return str;

    }

    public void setCurrentMonth(String prefs_current_month) {

        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_CURRENT_MONTH, prefs_current_month);
        mEditor.commit();


    }

    public String getSelectedDate() {
        String str = mPrefs.getString(PREF_APPOINTMENT_TIME_DATE, "");
        return str;

    }

    public void setSelectedDate(String prefs_first_date) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_APPOINTMENT_TIME_DATE, prefs_first_date);
        mEditor.commit();

    }

    public String getSelectedAppointment() {
        String str = mPrefs.getString(PREF_FIRSTAPPOINTMENT, "");
        return str;

    }

    public void setSelectedAppointment(String prefs_first_appointment) {

        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_FIRSTAPPOINTMENT, prefs_first_appointment);
        mEditor.commit();

    }


    public String getProviderID() {
        String str = mPrefs.getString(PREF_PROVIDER_ID, "");
        return str;
    }

    public void setProviderID(String prefs_providerid) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_PROVIDER_ID, prefs_providerid);
        mEditor.commit();
    }

    public void setEmptyStomach(String pref_empty_stomach) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_EMPTY_STOMACH, pref_empty_stomach);
        mEditor.commit();
    }






    public void setProviderName(String pref_provider_name) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_PROVIDER_NAME, pref_provider_name);
        mEditor.commit();
    }


    public String getProviderPersonContactNumber() {
        String str = mPrefs.getString(PREF_PROVIDER_POC_NUMBER, "");
        return str;
    }

    public void setProviderPersonContactNumber(String pref_provider_poc_number) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_PROVIDER_POC_NUMBER, pref_provider_poc_number);
        mEditor.commit();
    }


    public String getProviderPersonContact() {
        String str = mPrefs.getString(PREF_PROVIDER_POC, "");
        return str;
    }

    public void setProviderPersonContact(String pref_provider_poc) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_PROVIDER_POC, pref_provider_poc);
        mEditor.commit();
    }


    public String getProviderAddress() {
        String str = mPrefs.getString(PREF_PROVIDER_LINE1, "");
        return str;
    }

    public void setProviderAddress(String pref_provider_line1) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_PROVIDER_LINE1, pref_provider_line1);
        mEditor.commit();
    }


    public String getPackageTitle() {
        String str = mPrefs.getString(PREF_PACKAGETITLE, "");
        return str;
    }

    public void setPackageTitle(String pref_packagetitle) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_PACKAGETITLE, pref_packagetitle);
        mEditor.commit();
    }


    public String getPackageID() {
        String str = mPrefs.getString(PREF_PACKAGEID, "");
        return str;
    }

    public void setPackageID(String pref_packageid) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_PACKAGEID, pref_packageid);
        mEditor.commit();
    }


    public String getPackageType() {
        String str = mPrefs.getString(PREF_PACKAGETYPE, "");
        return str;
    }

    public void setPackageType(String pref_packagetype) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_PACKAGETYPE, pref_packagetype);
        mEditor.commit();
    }

    public String getPackageRecommendedSearch() {
        String str = mPrefs.getString(PACKAGE_RECOMMENDEDSEARCH, "");
        return str;
    }

    public void setPackageRecommendedSearch(String package_recommendedsearch) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PACKAGE_RECOMMENDEDSEARCH, package_recommendedsearch);
        mEditor.commit();
    }

    public String getPackageMrp() {
        String str = mPrefs.getString(PREF_PACKAGEMRP, "");
        return str;
    }

    public void setPackageMrp(String pref_packagemrp) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_PACKAGEMRP, pref_packagemrp);
        mEditor.commit();
    }


    public String getPackageSrp() {
        String str = mPrefs.getString(PREF_PACKAGESRP, "");
        return str;
    }

    public void setPackageSrp(String pref_packagesrp) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_PACKAGESRP, pref_packagesrp);
        mEditor.commit();
    }

    public String getPackageDiscount() {
        String str = mPrefs.getString(PREF_DISCOUNT, "");
        return str;
    }

    public void setPackageDiscount(String pref_discount) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_DISCOUNT, pref_discount);
        mEditor.commit();
    }

    public String getPackageInfo() {
        String str = mPrefs.getString(PREF_PACKAGEINFO, "");
        return str;
    }

    public void setPackageInfo(String pref_packageinfo) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_PACKAGEINFO, pref_packageinfo);
        mEditor.commit();
    }




    public String getNotifiCount() {
        String str = mPrefs.getString(PREF_NotificationCount, "");
        return str;
    }

    public void setNotifiCount(String pREF_NotificationCount) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_NotificationCount, pREF_NotificationCount);
        mEditor.commit();
    }

    public String getStepsCount() {
        String str = mPrefs.getString(PREF_StepsCount, "0");
        return str;
    }

    public void setStepsCount(String pREF_StepsCount) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_StepsCount, pREF_StepsCount);
        mEditor.commit();
    }

    public String getCaloriesCount() {
        String str = mPrefs.getString(PREF_CaloriesCount, "0");
        return str;
    }

    public void setCaloriesCount(String pREF_CaloriesCount) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_CaloriesCount, pREF_CaloriesCount);
        mEditor.commit();
    }



    public boolean getIsFIrstWizard() {
        boolean str = mPrefs.getBoolean(PREF_FIRST_USER_WIZARD, false);
        return str;
    }

    public void setIsFIrstWizard(boolean pPREF_FIRST_USER_WIZARD) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_FIRST_USER_WIZARD, pPREF_FIRST_USER_WIZARD);
        mEditor.commit();
    }



    public boolean getIsLogin() {
        boolean str = mPrefs.getBoolean(PREF_IsLogin, false);
        return str;
    }

    public void setIsLogin(boolean pREF_IsLogin) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_IsLogin, pREF_IsLogin);
        mEditor.commit();
    }

    public String getCustomerName() {
        String str = mPrefs.getString(PREF_CustomerName, "");
        return str;
    }

    public void setCustomerName(String pREF_CustomerName) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_CustomerName, pREF_CustomerName);
        mEditor.commit();
    }

    public String getAppToken() {
        String str = mPrefs.getString(PREF_AppToken, "");
        return str;
    }

    public boolean isSentTokenFor(String regId) {

        return mPrefs.getString("token_push", "").equals(regId);

    }

    public void sentTokenFor(String regId) {

        Editor mEditor = mPrefs.edit();
        mEditor.putString("token_push", regId);
        mEditor.commit();


    }

    public boolean isWizardCompletedFor(String identificationToken) {
        return mPrefs.getBoolean(identificationToken + "WIZARD", false);
    }

    public void setWizardCompleteFor(String identificationToken, boolean value) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(identificationToken + "WIZARD", value);
        mEditor.commit();
    }

    public void setAppToken(String pREF_AppToken) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_AppToken, pREF_AppToken);
        mEditor.commit();
    }




    public String getLong() {
        String str = mPrefs.getString(PREF_Long, "");
        return str;
    }

    public void setLong(String pREF_Long) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_Long, pREF_Long);
        mEditor.commit();
    }

    public String getUserName() {
        String str = mPrefs.getString(PREF_UserName, "");
        return str;
    }

    public void setLoggedinUserName(String pREF_UserName) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_UserName, pREF_UserName);
        mEditor.commit();
    }




    public String getEkinKey() {
       String str = mPrefs.getString(PREF_X_EKINCARE_KEY, "");
        return str;
    }

    public void setEkinKey(String pREF_EkinKey) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_X_EKINCARE_KEY, pREF_EkinKey);
        mEditor.commit();
    }

    public String getCustomerKey() {
        String str = mPrefs.getString(PREF_X_CUSTOMER_KEY, "");
        return str;
    }

    public void setCustomerKey(String pREF_CustomerKey) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_X_CUSTOMER_KEY, pREF_CustomerKey);
        mEditor.commit();
    }





    public String getPassword() {
        String str = mPrefs.getString(PREF_Password, "");
        return str;
    }

    public void setPassword(String pREF_Password) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_Password, pREF_Password);
        mEditor.commit();
    }



    public String getProfileData() {
        String str = mPrefs.getString(PREF_ProfileData, "");
        return str;
    }

    public void setProfileData(String pREF_ProfileData) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_ProfileData, pREF_ProfileData);
        mEditor.commit();
    }

    public void setYouCustomer(String pREF_youCustomer) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_YouCustomer, pREF_youCustomer);
        mEditor.commit();
    }



    public void setBasicCustomer(String pREF_basicCustomer) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_BasicCustomer, pREF_basicCustomer);
        mEditor.commit();
    }







    public boolean getIsWizardComplete() {
        boolean str = mPrefs.getBoolean(PREF_IsWizardComplete, false);
        return str;
    }




    public int getSelectedProfileId() {
        int str = mPrefs.getInt(PREF_SelectedProfileId, -1);
        return str;
    }



    public float getTarget() {
        float str = mPrefs.getFloat(PREF_Target, 0);
        return str;
    }

    public void setTarget(float pREF_target) {
        Editor mEditor = mPrefs.edit();
        mEditor.putFloat(PREF_Target, pREF_target);
        mEditor.commit();
    }





    public boolean getisHydrocareSubscriptionEnable() {
        boolean str = mPrefs.getBoolean(PREF_isHydrocareSubscriptionEnable, false);
        return str;
    }

    public void setisHydrocareSubscriptionEnable(boolean pREF_IsLogin) {
        Editor mEditor = mPrefs.edit();
        mEditor.putBoolean(PREF_isHydrocareSubscriptionEnable, pREF_IsLogin);
        mEditor.commit();
    }



    public void setHydrocareSubscripted(String pREF_HydrocareSubscripted) {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_HydrocareSubscripted, pREF_HydrocareSubscripted);
        mEditor.commit();
    }

    public String getRegistrationId() {

        String registrationId = mPrefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        return registrationId;
    }

    private int getAppVersion(Context context) {

        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("RegisterGcmIdInfo",
                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }


    }


    public void storeRegistrationId(Context context, String regId) {
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }



    public void ClearAllData() {
        Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_UPDATE_WATER,"0");
        mEditor.putString(PREF_TODAY_HOW_FEEL,"");
        mEditor.putString(PREF_UPDATE_WATER_TOTAL,"0");
        mEditor.putBoolean(PREF_IsLogin, false);
        mEditor.putString(PREF_AppToken, "");
        mEditor.putString(PREF_UserId, "");
        mEditor.putString(PREF_Lat, "");
        mEditor.putString(PREF_Long, "");
        mEditor.putString(PREF_UserName, "");
        mEditor.putString(PREF_Device_Password, "");
        mEditor.putString(PREF_Device_Name, "");
        mEditor.putString(PREF_X_EKINCARE_KEY, "");
        mEditor.putString(PREF_X_CUSTOMER_KEY, "");
        mEditor.putString(PREF_Password, "");
        mEditor.putString(PREF_ProfileModel, "");
        mEditor.putString(PREF_ProfileData, "");
        mEditor.putString(PREF_Immunization, "");
        mEditor.putString(PREF_BasicCustomer, "");
        mEditor.putInt(PREF_FamilyMemCount, 0);
        mEditor.putBoolean(PREF_IsWizardComplete, false);
        mEditor.putBoolean(PREF_FIRST_USER_WIZARD,false);
        mEditor.putInt(PREF_LastQuestion, 0);
        mEditor.putStringSet(PREF_IsFamilyWizrdComplete, new HashSet<String>());
        mEditor.putBoolean(PREF_IsLoginVisible, false);
        mEditor.putBoolean(PREF_IsHaveCodeVisible, false);
        mEditor.putInt(PREF_SelectedProfileId, -1);
        mEditor.putBoolean(PREF_IsYou, true);
        mEditor.putFloat(PREF_Target, 0);
        mEditor.putString(PREF_BloodGroup, "");
        mEditor.putString(PREF_HydrocareIntakesTimeStamp, "");
        mEditor.putString(PREF_TemperatureTimeStamp, "");
        mEditor.putString(PREF_Temperature, "");
        mEditor.putBoolean(PREF_isHydrocareSubscriptionEnable, false);
        mEditor.putBoolean(PREF_isBloodSOSSubscriptionEnable, false);
        mEditor.putString(PREF_FamilyMemberList, "");
        mEditor.putString(PREF_YouCustomer, "");
        mEditor.putString(PREF_HydrocareSubscripted, "0");
        mEditor.putString(PREF_NotificationCount,"null");
        mEditor.putString(PREF_StepsCount,"0");
        mEditor.putString(PREF_CaloriesCount,"0");
        mEditor.putBoolean(PREF_HAS_SEEN_DOCUMENT_UPLOAD_TUTORIAL,false);
        mEditor.putBoolean(PREF_NEVER_SHOW_DOCUMENT_UPLOAD_TUTORIAL,false);

        setStepsCount("0");
        setCaloriesCount("0");
        setAteOnTime(false);
        setDrinkedCoffeeToday(false);
        setDrinkedAlcoholToday(false);
        setSmokedToday(false);
        setLateNightPhoneUsuage(false);
        setSleepTime("0");
        setWakeTime("0");
        setSleepTime("0");
        sethadbreakfast(false);
        settotalUpdateWater("0");
        setskippedMeals(false);
        setHasHowWasYourDayDataFilled(false);
        sethowWasDay("");
        mEditor.commit();

        setHasSeenMedicationFamilyMemeberTutorial(false);
        setHasSeenActivityFamilyMemeberTutorial(false);
        setHasSeenAllergyFamilyMemeberTutorial(false);
        setHasSeenDocumentFamilyMemeberTutorial(false);
        setHasSeenHistoryFamilyMemeberTutorial(false);
        setHasSeenTutorial(false);

        setIsFirstTimeRegister(false);

        setIsGoogleFitConnected(false);
        setUserRatingCount(0);
        setUserRatingDayCount(0);
        setIsRated(false);

        setIsFirstTimeChat(true);
    }


    protected SharedPreferences getSharedPreferences() {
        SharedPreferences sharedPref = context.getSharedPreferences("eKinCareApp", Context.MODE_PRIVATE);
        return sharedPref;
    }




}
