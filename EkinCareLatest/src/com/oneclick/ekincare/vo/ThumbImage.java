package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * Created by jhansi on 31/12/14.
 */
public class ThumbImage implements Parcelable {

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(url);

	}

	public static Parcelable.Creator<ThumbImage> CREATOR = new Creator<ThumbImage>() {
		@Override
		public ThumbImage[] newArray(int size) {
			return new ThumbImage[size];
		}
		@Override
		public ThumbImage createFromParcel(Parcel source) {
			return new ThumbImage(source);
		}
	};

	public ThumbImage(Parcel in){

		url = in.readString();
	}

}
