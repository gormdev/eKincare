package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * Created by jhansi on 31/12/14.
 */
public class Image implements Parcelable{

	private String url;

	private ThumbImage thumb;

	private ThumbImage thumbAvatar;

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
		// TODO Auto-generated method stub
		dest.writeString(url);
		dest.writeParcelable(thumb, flags);
		dest.writeParcelable(thumbAvatar, flags);

	}

	public static Parcelable.Creator<Image> CREATOR = new Creator<Image>() {
		@Override
		public Image[] newArray(int size) {
			return new Image[size];
		}
		@Override
		public Image createFromParcel(Parcel source) {
			return new Image(source);
		}
	};

	public Image(Parcel in){

		url = in.readString();
		thumb = in.readParcelable(ThumbImage.class.getClassLoader());
		thumbAvatar = in.readParcelable(ThumbImage.class.getClassLoader());
	}

}