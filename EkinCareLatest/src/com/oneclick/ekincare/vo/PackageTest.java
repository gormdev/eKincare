package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RaviTejaN on 08-06-2016.
 */
public class PackageTest implements Parcelable {

    private List<PackageDataSet> packages;
    private String message;


    public PackageTest() {
        packages = new ArrayList<PackageDataSet>();
    }


    public List<PackageDataSet> getPackages() {
        return packages;
    }

    public void setTimeline(List<PackageDataSet> packages) {
        this.packages = packages;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.packages);
        dest.writeString(this.message);
    }

    protected PackageTest(Parcel in) {
        this.packages = in.createTypedArrayList(PackageDataSet.CREATOR);
        this.message = in.readString();
    }

    public static final Parcelable.Creator<PackageTest> CREATOR = new Parcelable.Creator<PackageTest>() {
        @Override
        public PackageTest createFromParcel(Parcel source) {
            return new PackageTest(source);
        }

        @Override
        public PackageTest[] newArray(int size) {
            return new PackageTest[size];
        }
    };
}
