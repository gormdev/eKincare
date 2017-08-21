package com.oneclick.ekincare.vo;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class GraphData implements Parcelable{

	List<Values> values;


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

	public GraphData() {
		// TODO Auto-generated constructor stub
		values=new ArrayList<Values>();
	}


	public List<Values> getValues() {
		return values;
	}



	public class Values implements Parcelable{

		String date="";
		String result="";
		String color = "";

		public String getTextColor() {
			return color;
		}



		public String getDate() {
			return date;
		}



		public String getResult() {
			return result;
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

	}

}
