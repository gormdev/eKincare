package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by RaviTejaN on 23-12-2016.
 */

public class DoctorModel implements Parcelable{
    List<DoctorModelData> doctors;

    protected DoctorModel(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DoctorModel> CREATOR = new Creator<DoctorModel>() {
        @Override
        public DoctorModel createFromParcel(Parcel in) {
            return new DoctorModel(in);
        }

        @Override
        public DoctorModel[] newArray(int size) {
            return new DoctorModel[size];
        }
    };

    public List<DoctorModelData> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<DoctorModelData> doctors) {
        this.doctors = doctors;
    }
}
