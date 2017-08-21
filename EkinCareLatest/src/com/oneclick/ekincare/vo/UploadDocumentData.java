package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class UploadDocumentData implements Parcelable{

	String message;
	Document document;
	public UploadDocumentData() {
	}
	public String getMessage() {
		return message;
	}

	public Document getDocument() {
		return document;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public class Document implements Parcelable{
	String id;
	String title;
	String created_at;
	String updated_at;
	String date;
	public Document() {
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Emr getEmr() {
		return emr;
	}

	public void setEmr(Emr emr) {
		this.emr = emr;
	}

	Emr emr;

	public class Emr implements Parcelable{
		String url;

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

		}

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
