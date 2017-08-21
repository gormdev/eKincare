package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rakeshk on 19-01-2015.
 */
public class ImpactSteps implements Parcelable {

	private String smoke;
	private String frequency_of_exercise;
	private String hyper_tension;
	private String bmi;
	private String alcohol;
	private String waist_size;
	private String blood_pressure;
	private String fasting_blood_glucose;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(smoke);
		dest.writeString(frequency_of_exercise);
		dest.writeString(hyper_tension);
		dest.writeString(bmi);
		dest.writeString(alcohol);
		dest.writeString(waist_size);
		dest.writeString(blood_pressure);
		dest.writeString(fasting_blood_glucose);

	}


	public static Parcelable.Creator<ImpactSteps> CREATOR = new Creator<ImpactSteps>() {
		@Override
		public ImpactSteps[] newArray(int size) {
			return new ImpactSteps[size];
		}
		@Override
		public ImpactSteps createFromParcel(Parcel source) {
			return new ImpactSteps(source);
		}
	};

	public ImpactSteps(Parcel in){

		smoke = in.readString();
		frequency_of_exercise = in.readString();
		hyper_tension = in.readString();
		bmi = in.readString();
		alcohol = in.readString();
		waist_size = in.readString();
		blood_pressure = in.readString();
		fasting_blood_glucose = in.readString();

	}
	public String getSmoke() {
		return smoke;
	}
	public String getFrequency_of_exercise() {
		return frequency_of_exercise;
	}
	public String getHyper_tension() {
		return hyper_tension;
	}
	public String getBmi() {
		return bmi;
	}
	public String getWaist_size() {
		return waist_size;
	}
	public String getAlcohol() {
		return alcohol;
	}
	public String getBlood_pressure() {
		return blood_pressure;
	}
	public String getFasting_blood_glucose() {
		return fasting_blood_glucose;
	}


}
