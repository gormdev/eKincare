package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RaviTejaN on 22-12-2016.
 */

public class DoctorAutoSearchModel implements Parcelable {
   private String  id;
    private String  name;
    private String package_type;
    private String  package_info;

    public DoctorAutoSearchModel() {
    }

    protected DoctorAutoSearchModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        package_type = in.readString();
        package_info = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(package_type);
        dest.writeString(package_info);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DoctorAutoSearchModel> CREATOR = new Creator<DoctorAutoSearchModel>() {
        @Override
        public DoctorAutoSearchModel createFromParcel(Parcel in) {
            return new DoctorAutoSearchModel(in);
        }

        @Override
        public DoctorAutoSearchModel[] newArray(int size) {
            return new DoctorAutoSearchModel[size];
        }
    };

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

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    public String getPackage_info() {
        return package_info;
    }

    public void setPackage_info(String package_info) {
        this.package_info = package_info;
    }
}
