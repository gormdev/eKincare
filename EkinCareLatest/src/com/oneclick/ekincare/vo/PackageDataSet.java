package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Ajay on 15-12-2016.
 */

public class PackageDataSet implements Parcelable {
    private String id;
    private String name;
    private int mrp;
    private int selling_price;
    private int discount;
    private String package_type;
    private String package_info;
    private  String empty_stomach;


    private List<TestComponents> test_components;


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

    public int getMrp() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
    }

    public int getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(int selling_price) {
        this.selling_price = selling_price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
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

    public String getEmpty_stomach() {
        return empty_stomach;
    }

    public void setEmpty_stomach(String empty_stomach) {
        this.empty_stomach = empty_stomach;
    }

    public List<TestComponents> getTest_components() {
        return test_components;
    }

    public void setTest_components(List<TestComponents> test_components) {
        this.test_components = test_components;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.mrp);
        dest.writeInt(this.selling_price);
        dest.writeInt(this.discount);
        dest.writeString(this.package_type);
        dest.writeString(this.package_info);
        dest.writeString(this.empty_stomach);
        dest.writeTypedList(this.test_components);
    }

    public PackageDataSet() {
    }

    protected PackageDataSet(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.mrp = in.readInt();
        this.selling_price = in.readInt();
        this.discount = in.readInt();
        this.package_type = in.readString();
        this.package_info = in.readString();
        this.empty_stomach = in.readString();
        this.test_components = in.createTypedArrayList(TestComponents.CREATOR);
    }

    public static final Parcelable.Creator<PackageDataSet> CREATOR = new Parcelable.Creator<PackageDataSet>() {
        @Override
        public PackageDataSet createFromParcel(Parcel source) {
            return new PackageDataSet(source);
        }

        @Override
        public PackageDataSet[] newArray(int size) {
            return new PackageDataSet[size];
        }
    };
}
