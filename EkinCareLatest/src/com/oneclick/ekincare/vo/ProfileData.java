package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * Created by jhansi on 21/01/15.
 */
public class ProfileData implements Parcelable{

	private String msg;
	private String message;
	private String error;
	private Customer customer;
	private FamilyMembers family_members ;

	public ProfileData() {

	}


    public String getMsg() {
        return msg;
    }

    public void setMsg(String message) {
        this.msg = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public FamilyMembers getFamily_members() {
		return family_members;
	}

	public void setFamily_members(FamilyMembers family_members) {
		this.family_members = family_members;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {

		arg0.writeString(message);
		arg0.writeString(msg);
		arg0.writeParcelable(customer, arg1);
		arg0.writeParcelable(family_members, arg1);

	}

	public static Parcelable.Creator<ProfileData> CREATOR = new Creator<ProfileData>(){
		@Override
		public ProfileData[] newArray(int size) {
			return new ProfileData[size];
		}

		@Override
		public ProfileData createFromParcel(Parcel source) {

			return new ProfileData(source);
		}

	};

	public ProfileData(final Parcel in){

		message = in.readString();
		msg = in.readString();
		in.readParcelable(customer.getClass().getClassLoader());
		in.readParcelable(family_members.getClass().getClassLoader());

	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}



}
