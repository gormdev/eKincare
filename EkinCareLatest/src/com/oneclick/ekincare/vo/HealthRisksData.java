package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RaviTejaN on 19-12-2016.
 */

public class HealthRisksData implements Parcelable {



    private String color;
    private  String tag;
    private String name;
    private String percentage;

    protected HealthRisksData(Parcel in) {
        color = in.readString();
        tag = in.readString();
        name = in.readString();
        percentage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(color);
        dest.writeString(tag);
        dest.writeString(name);
        dest.writeString(percentage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HealthRisksData> CREATOR = new Creator<HealthRisksData>() {
        @Override
        public HealthRisksData createFromParcel(Parcel in) {
            return new HealthRisksData(in);
        }

        @Override
        public HealthRisksData[] newArray(int size) {
            return new HealthRisksData[size];
        }
    };

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
