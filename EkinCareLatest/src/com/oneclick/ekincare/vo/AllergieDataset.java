package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ajay on 23-09-2016.
 */
public class AllergieDataset implements Parcelable {

    private String id;
    private String name;
    private String reaction;
    private String created_at;
    private String updated_at;

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
    public String getReaction() {
        return reaction;
    }
    public void setReaction(String reaction) {
        this.reaction = reaction;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
        parcel.writeString(name );
        parcel.writeString(reaction);
    }

    public static final Parcelable.Creator<AllergieDataset> CREATOR = new Creator<AllergieDataset>() {
        public AllergieDataset createFromParcel(Parcel source) {
            AllergieDataset allergieDataset = new AllergieDataset();

            allergieDataset.name = source.readString();
            allergieDataset.created_at = source.readString();
            allergieDataset.updated_at = source.readString();
            allergieDataset.id = source.readString();
            allergieDataset.reaction = source.readString();
            return allergieDataset;
        }
        @Override
        public AllergieDataset[] newArray(int size)
        {
            return new AllergieDataset[size];
        }
    };
}
