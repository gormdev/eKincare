package com.oneclick.ekincare.vo;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

/**
 * Created by rakeshk on 19-01-2015.
 */
public class CustomerVitals implements Parcelable {

	private String profileId;
	private String weight;
	private String feet;
	private String inches;
	private String blood_group_id;
	private String waist;
	private String created_at="";
	private String updated_at="";
	private List<Values> formatted_weight_trends;
	private List<Values> formatted_height_trends;





	public String getWeight() {
		return weight;
	}



	public String getFeet() {
		return feet;
	}


	public String getInches() {
		return inches;
	}



	public String getBlood_group_id() {
		return blood_group_id;
	}

	public String getWaist() {
		return waist;
	}

	public void setWaist(String waist) {
		this.waist = waist;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(profileId);
		dest.writeString(weight);
		dest.writeString(feet);
		dest.writeString(inches);
		dest.writeString(blood_group_id);
		dest.writeString(waist);
		dest.writeString(created_at);
		dest.writeString(updated_at);
		dest.writeTypedList(formatted_weight_trends);
		dest.writeTypedList(formatted_height_trends);

	}


	public static Parcelable.Creator<CustomerVitals> CREATOR = new Creator<CustomerVitals>() {
		@Override
		public CustomerVitals[] newArray(int size) {
			return new CustomerVitals[size];
		}
		@Override
		public CustomerVitals createFromParcel(Parcel source) {
			return new CustomerVitals(source);
		}
	};

	public CustomerVitals(Parcel in){

		profileId = in.readString();
		weight = in.readString();
		feet = in.readString();
		inches = in.readString();
		blood_group_id = in.readString();
		waist = in.readString();
		created_at = in.readString();
		updated_at = in.readString();
		in.readTypedList(formatted_weight_trends, Values.CREATOR);


	}

	public String getCreated_at() {
		return created_at;
	}


	public String getUpdated_at() {
		return updated_at;
	}



	public List<Values> getFormatted_weight_trends() {
		return formatted_weight_trends;
	}



	public List<Values> getFormatted_height_trends() {
		return formatted_height_trends;
	}



	public static class Values implements Parcelable{

		String date="";
		String value="";

		public String getValue() {
			return value;
		}



		public String getDate() {
			return date;
		}



		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(date);
			dest.writeString(value);
		}

		public static final Parcelable.Creator<Values> CREATOR = new Creator<Values>() {
			@Override
			public Values[] newArray(int size) {
				return new Values[size];
			}
			@Override
			public Values createFromParcel(Parcel source) {
				return new Values(source);
			}
		};

		public  Values(Parcel in) {
			date = in.readString();
			value = in.readString();
		}

	}

}
