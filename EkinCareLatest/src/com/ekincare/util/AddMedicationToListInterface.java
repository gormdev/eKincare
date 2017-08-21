package com.ekincare.util;

import android.support.v4.app.Fragment;

import com.oneclick.ekincare.vo.Medication;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ajay on 08-09-2016.
 */
public interface AddMedicationToListInterface  {
    public void addMedicationToList(Medication medicationDataModel);

    public ArrayList<Medication> getMedicationList();

    public void switchFragment(Fragment fragment);

    public void deleteFromMedicationList(Medication medicationDataModel);

    public void activateMedication(JSONObject jsonObject, String id);

    public boolean isEdit();

    public Medication getMedicationObject();
}
