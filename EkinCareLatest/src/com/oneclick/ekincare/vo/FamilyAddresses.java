package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RaviTejaN on 29-09-2016.
 */
public class FamilyAddresses implements Parcelable {
    private String id;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String country;
    private String zip_code;
    private String latitude;
    private  String longitude;




    public String getLine1() {
        return line1;
    }



    public String getLine2() {
        return line2;
    }



    public String getCity() {
        return city;
    }



    public String getState() {
        return state;
    }


    public String getCountry() {
        return country;
    }


    public String getZip_code() {
        return zip_code;
    }


    public String getLatitude() {
        return latitude;
    }


    public String getLongitude() {
        return longitude;
    }


    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(line1);
        dest.writeString(line2);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(zip_code);
        dest.writeString(country);
        dest.writeString(latitude);
        dest.writeString(longitude);


    }


    public static Parcelable.Creator<FamilyAddresses> CREATOR = new Creator<FamilyAddresses>() {
        @Override
        public FamilyAddresses[] newArray(int size) {
            return new FamilyAddresses[size];
        }
        @Override
        public FamilyAddresses createFromParcel(Parcel source) {
            return new FamilyAddresses(source);
        }
    };

    public  FamilyAddresses(Parcel in) {

        id = in.readString();
        line1 = in.readString();
        line2 = in.readString();
        state = in.readString();
        city = in.readString();
        country = in.readString();
        zip_code = in.readString();
        latitude = in.readString();
        longitude = in.readString();



    }



}
