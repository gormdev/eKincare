package com.ekincare.app;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.Immunization;
import com.oneclick.ekincare.vo.ProfileData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jhansi on 11/07/15.
 */
public class ProfileManager {

    public static ProfileManager instance=null;
    protected Context context;
    private ProfileData mProfileData;
    private List<Customer> blankList = new ArrayList<Customer>();
    private PreferenceHelper prefs;
    public ProfileManager(Context mContext) {
        this.context = mContext;
        this.prefs = new PreferenceHelper(context);
    }

    public static ProfileManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProfileManager(context);
        }
        return instance;
    }

    public List<Customer> getFamilyMembers() {
        if (mProfileData != null && mProfileData.getFamily_members() != null) {
            List<Customer> fm = new ArrayList<Customer>();
            fm.addAll(mProfileData.getFamily_members().getMember_list());
            return fm;
        }
        return blankList;
    }


    public ProfileData getProfileData() {

        long start = System.currentTimeMillis();
        if (mProfileData != null) {
            return mProfileData;
        }
        Gson gson = new Gson();
        mProfileData = gson.fromJson(prefs.getProfileData(), ProfileData.class);
        Log.e("CreateProfileObject end", "" + (System.currentTimeMillis() - start));
        return mProfileData;

    }

    public void setProfileData(ProfileData mProfileData) {
        Gson mGson = new Gson();
        prefs.setProfileData(mGson.toJson(mProfileData));
        prefs.setYouCustomer(mGson.toJson(mProfileData.getCustomer()));
        this.mProfileData = mProfileData;
    }

    public Customer getLoggedinCustomer() {
        long start = System.currentTimeMillis();
        Gson gson = new Gson();
        if (mProfileData != null) {
            return mProfileData.getCustomer();
        } else {
            ProfileData pd = getProfileData();
            if(pd != null){
                return pd.getCustomer();
            }
            else {
                return null;
            }
        }

    }


    public static ProfileManager getInstance() {
        return instance;
    }

    public static void setInstance(ProfileManager instance) {
        ProfileManager.instance = instance;
    }
}
