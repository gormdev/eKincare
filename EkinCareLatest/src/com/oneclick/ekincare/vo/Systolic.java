package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class Systolic implements Parcelable {
	private String result="";
	private String color="";
	private String units="";
	private String date="";
	private String blood_sugar="";

	private  String lonic_code;

	public String getLonic_code() {
		return lonic_code;
	}

	public void setLonic_code(String lonic_code) {
		this.lonic_code = lonic_code;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

	public String getBllod_sugar() {
		return blood_sugar;
	}

	public void setBllod_sugar(String bllod_sugar) {
		this.blood_sugar = bllod_sugar;
	}

}
