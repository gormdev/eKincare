package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rakeshk on 19-01-2015.
 */
public class Allergies implements Parcelable {

    private String smoke;




	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(smoke);

	}


	public static Parcelable.Creator<Allergies> CREATOR = new Creator<Allergies>() {
		@Override
		public Allergies[] newArray(int size) {
			return new Allergies[size];
		}
		@Override
		public Allergies createFromParcel(Parcel source) {
			return new Allergies(source);
		}
	};

	public Allergies(Parcel in){

		smoke = in.readString();

	}


}
