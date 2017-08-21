package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RaviTejaN on 21-09-2016.
 */
public class AllergieData implements Parcelable {

    private List<AllergieDataset> allergies;
    private String msg;


    public AllergieData() {
        // TODO Auto-generated constructor stub
        allergies = new ArrayList<AllergieDataset>();

    }

    public List<AllergieDataset> getAllergies() {
        return allergies;
    }



    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub

    }

}
