package com.oneclick.ekincare.vo;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;


/**
 * Created by jhansi on 21/01/15.
 */
public class FamilyMembers implements Parcelable{

	private String total_count;
	private List<Customer> member_list;



	public List<Customer> getMember_list() {
		return member_list;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(total_count);
		dest.writeTypedList(member_list);


	}

	public static Parcelable.Creator<FamilyMembers> CREATOR = new Creator<FamilyMembers>(){
		@Override
		public FamilyMembers[] newArray(int size) {
			return new FamilyMembers[size];
		}

		@Override
		public FamilyMembers createFromParcel(Parcel source) {

			return new FamilyMembers(source);
		}

	};

	public FamilyMembers(final Parcel in){

		total_count = in.readString();
		in.readParcelable(member_list.getClass().getClassLoader());

	}


}
