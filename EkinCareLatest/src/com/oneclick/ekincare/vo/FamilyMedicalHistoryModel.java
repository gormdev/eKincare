package com.oneclick.ekincare.vo;

import java.util.Date;

import javax.xml.transform.Source;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rakeshk on 16-04-2015.
 */
public class FamilyMedicalHistoryModel implements Parcelable{
    private int id;
    private String name;
    private String relation;
    private int age;
    private String status;
    private String medical_condition_1;
	private String family_member_id;

	public String getFamily_member_id() {
		return family_member_id;
	}

	public void setFamily_member_id(String family_member_id) {
		this.family_member_id = family_member_id;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(relation);
		dest.writeInt(age);
		dest.writeString(status);
		dest.writeString(medical_condition_1);
		dest.writeString(family_member_id);

	}

	public static Parcelable.Creator<FamilyMedicalHistoryModel> CREATOR = new Creator<FamilyMedicalHistoryModel>() {
		@Override
		public FamilyMedicalHistoryModel[] newArray(int size) {
			return new FamilyMedicalHistoryModel[size];
		}
		@Override
		public FamilyMedicalHistoryModel createFromParcel(Parcel source) {
			return new FamilyMedicalHistoryModel(source);
		}
	};

	public  FamilyMedicalHistoryModel(Parcel in) {

		id = in.readInt();
		name = in.readString();
		relation = in.readString();
		age = in.readInt();
		status = in.readString();
		medical_condition_1 = in.readString();
		family_member_id=in.readString();

	}



}
