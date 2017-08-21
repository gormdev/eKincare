package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by RaviTejaN on 14-12-2016.
 */

public class InsightWaist implements Parcelable {
    private List<String> categories_x;
    private List<String> categories_y;
    private List<WaistSeriese> series;

    protected InsightWaist(Parcel in) {
        categories_x = in.createStringArrayList();
        categories_y = in.createStringArrayList();
    }

    public static final Creator<InsightWaist> CREATOR = new Creator<InsightWaist>() {
        @Override
        public InsightWaist createFromParcel(Parcel in) {
            return new InsightWaist(in);
        }

        @Override
        public InsightWaist[] newArray(int size) {
            return new InsightWaist[size];
        }
    };

    public List<String> getCategories_x() {
        return categories_x;
    }

    public void setCategories_x(List<String> categories_x) {
        this.categories_x = categories_x;
    }

    public List<String> getCategories_y() {
        return categories_y;
    }

    public void setCategories_y(List<String> categories_y) {
        this.categories_y = categories_y;
    }

    public List<WaistSeriese> getSeries() {
        return series;
    }

    public void setSeries(List<WaistSeriese> series) {
        this.series = series;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(categories_x);
        dest.writeStringList(categories_y);
    }
}
