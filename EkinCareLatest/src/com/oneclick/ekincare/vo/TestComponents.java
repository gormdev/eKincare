package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ajay on 15-12-2016.
 */

public class TestComponents implements Parcelable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public TestComponents() {
    }

    protected TestComponents(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<TestComponents> CREATOR = new Parcelable.Creator<TestComponents>() {
        @Override
        public TestComponents createFromParcel(Parcel source) {
            return new TestComponents(source);
        }

        @Override
        public TestComponents[] newArray(int size) {
            return new TestComponents[size];
        }
    };
}
