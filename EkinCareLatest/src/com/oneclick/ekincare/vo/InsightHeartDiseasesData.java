package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RaviTejaN on 14-12-2016.
 */

public class InsightHeartDiseasesData implements Parcelable {
    private String y;
    private String x;
    private String z;
    private String color;


    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    protected InsightHeartDiseasesData(Parcel in) {
        y = in.readString();
        x = in.readString();
        z = in.readString();
        color = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(y);
        dest.writeString(x);
        dest.writeString(z);
        dest.writeString(color);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InsightHeartDiseasesData> CREATOR = new Creator<InsightHeartDiseasesData>() {
        @Override
        public InsightHeartDiseasesData createFromParcel(Parcel in) {
            return new InsightHeartDiseasesData(in);
        }

        @Override
        public InsightHeartDiseasesData[] newArray(int size) {
            return new InsightHeartDiseasesData[size];
        }
    };
}
