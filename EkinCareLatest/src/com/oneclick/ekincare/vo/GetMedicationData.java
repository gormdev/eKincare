package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajay on 21-09-2016.
 */
public class GetMedicationData implements Parcelable{

    ArrayList<Medication> medications;
    private String status;

    public GetMedicationData(){
        medications = new ArrayList<Medication>();
    }



    public ArrayList<Medication> getMedications() {
        return medications;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {}

    public static final Parcelable.Creator<GetMedicationData> CREATOR = new Creator<GetMedicationData>() {
        public GetMedicationData createFromParcel(Parcel source) {
            GetMedicationData getMedicationData = new GetMedicationData();

            return getMedicationData;
        }
        @Override
        public GetMedicationData[] newArray(int size)
        {
            return new GetMedicationData[size];
        }
    };


}
