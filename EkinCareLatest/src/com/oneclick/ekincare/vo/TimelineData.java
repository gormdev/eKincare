package com.oneclick.ekincare.vo;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class TimelineData implements Parcelable {

	private List<TimelineDataset>timeline;
	private String msg;


	public TimelineData() {
		// TODO Auto-generated constructor stub
		timeline = new ArrayList<TimelineDataset>();

	}



	public List<TimelineDataset> getTimeline() {
		return timeline;
	}





	public class TimelineDataset implements Parcelable {

		private String id;
		private String activity_type;
		private String action;
		private String text;
		private String associated_id;
		private List<OutOfZone>out_of_zone;

		public TimelineDataset() {
			out_of_zone=new ArrayList<OutOfZone>();
		}

		public List<OutOfZone> getOut_of_zone() {
			return out_of_zone;
		}

		public void setOut_of_zone(List<OutOfZone> out_of_zone) {
			this.out_of_zone = out_of_zone;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getActivity_type() {
			return activity_type;
		}

		public void setActivity_type(String activity_type) {
			this.activity_type = activity_type;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getAssociated_id() {
			return associated_id;
		}

		public void setAssociated_id(String associated_id) {
			this.associated_id = associated_id;
		}

		public String getCustomer_id() {
			return customer_id;
		}

		public void setCustomer_id(String customer_id) {
			this.customer_id = customer_id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getBadge() {
			return badge;
		}

		public void setBadge(String badge) {
			this.badge = badge;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getFile_name() {
			return file_name;
		}

		public void setFile_name(String file_name) {
			this.file_name = file_name;
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

		public String getAssessment_activity_id() {
			return assessment_activity_id;
		}

		public void setAssessment_activity_id(String assessment_activity_id) {
			this.assessment_activity_id = assessment_activity_id;
		}

		public String getInteger() {
			return integer;
		}

		public void setInteger(String integer) {
			this.integer = integer;
		}

		public String getDoctor_name() {
			return doctor_name;
		}

		public void setDoctor_name(String doctor_name) {
			this.doctor_name = doctor_name;
		}

		public String getProvider_name() {
			return provider_name;
		}

		public void setProvider_name(String provider_name) {
			this.provider_name = provider_name;
		}

		private String customer_id;
		private String title;
		private String description;
		private String badge;
		private String url;
		private String file_name;
		private String created_at;
		private String updated_at;
		private String assessment_activity_id;
		private String integer;
		private String doctor_name;
		private String provider_name;
		private String request_date;

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub

		}

		public String getRequest_date() {
			return request_date;
		}

		public void setRequest_date(String request_date) {
			this.request_date = request_date;
		}

	}

	public class OutOfZone implements Parcelable {

		private String test_component_name;
		private String units;
		private String color;
		private String result;



		public String getTest_component_name() {
			return test_component_name;
		}

		public void setTest_component_name(String test_component_name) {
			this.test_component_name = test_component_name;
		}

		public String getUnits() {
			return units;
		}

		public void setUnits(String units) {
			this.units = units;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {

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


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}
}
