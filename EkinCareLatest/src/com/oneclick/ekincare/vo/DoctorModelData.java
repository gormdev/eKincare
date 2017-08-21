package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RaviTejaN on 23-12-2016.
 */

public class DoctorModelData implements Parcelable {

    private String id;
    private String name;
    private String education;
    private String speciality;
    private String photo;

    Fee fee;

    protected DoctorModelData(Parcel in) {
        id = in.readString();
        name = in.readString();
        education = in.readString();
        speciality = in.readString();
        photo = in.readString();
    }

    public static final Creator<DoctorModelData> CREATOR = new Creator<DoctorModelData>() {
        @Override
        public DoctorModelData createFromParcel(Parcel in) {
            return new DoctorModelData(in);
        }

        @Override
        public DoctorModelData[] newArray(int size) {
            return new DoctorModelData[size];
        }
    };

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee consult) {
        this.fee = consult;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(education);
        dest.writeString(speciality);
        dest.writeString(photo);
    }
}
