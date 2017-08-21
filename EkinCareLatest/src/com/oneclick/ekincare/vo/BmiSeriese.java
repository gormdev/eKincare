package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by RaviTejaN on 14-12-2016.
 */

public class BmiSeriese implements Parcelable{
    private String name;
    private List<String> data;
    Marker marker;

    protected BmiSeriese(Parcel in) {
        name = in.readString();
        data = in.createStringArrayList();
    }

    public static final Creator<BmiSeriese> CREATOR = new Creator<BmiSeriese>() {
        @Override
        public BmiSeriese createFromParcel(Parcel in) {
            return new BmiSeriese(in);
        }

        @Override
        public BmiSeriese[] newArray(int size) {
            return new BmiSeriese[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeStringList(data);
    }

    public class Marker{
        String enabled ;

        public String getEnabled() {
            return enabled;
        }

        public void setEnabled(String enabled) {
            this.enabled = enabled;
        }
    }
}
