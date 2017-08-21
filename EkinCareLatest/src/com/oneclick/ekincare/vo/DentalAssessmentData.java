package com.oneclick.ekincare.vo;

import java.util.ArrayList;
import java.util.List;


import android.os.Parcel;
import android.os.Parcelable;

public class DentalAssessmentData implements Parcelable {
	DentalAssessment dental_assessment;

	public DentalAssessment getDental_assessment() {
		return dental_assessment;
	}

	public void setDental_assessment(DentalAssessment dental_assessment) {
		this.dental_assessment = dental_assessment;
	}

	public class DentalAssessment implements Parcelable {

		Assessment assessment;
		private List<AssessmentInfo> assessments_info;
		private List<Recommendation> recommendation;
		private List<Comments>comments;

		public List<Comments> getComments() {
			return comments;
		}

		public void setComments(List<Comments> comments) {
			this.comments = comments;
		}

		public List<AssessmentInfo> getAssessments_info() {
			return assessments_info;
		}

		public void setAssessments_info(List<AssessmentInfo> assessments_info) {
			this.assessments_info = assessments_info;
		}

		public List<Recommendation> getRecommendation() {
			return recommendation;
		}

		public void setRecommendation(List<Recommendation> recommendation) {
			this.recommendation = recommendation;
		}

		public DentalAssessment(){
			assessments_info = new ArrayList<AssessmentInfo>();
			recommendation = new ArrayList<Recommendation>();
		}


		public class AssessmentInfo implements Parcelable {
			String tooth_number;
			String dentition;
			String diagnosis;
			String recommendation;
			String request_date;

			public String getTooth_number() {
				return tooth_number;
			}

			public void setTooth_number(String tooth_number) {
				this.tooth_number = tooth_number;
			}

			public String getDentition() {
				return dentition;
			}

			public void setDentition(String dentition) {
				this.dentition = dentition;
			}

			public String getDiagnosis() {
				return diagnosis;
			}

			public void setDiagnosis(String diagnosis) {
				this.diagnosis = diagnosis;
			}

			public String getRecommendation() {
				return recommendation;
			}

			public void setRecommendation(String request_date) {
				this.recommendation = request_date;
			}

			public String getRequest_date() {
				return request_date;
			}

			public void setRequest_date(String request_date) {
				this.request_date = request_date;
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

		public class Recommendation implements Parcelable {

			String title;
			String description;

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

		public Assessment getAssessment() {
			return assessment;
		}

		public void setAssessment(Assessment assessment) {
			this.assessment = assessment;
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

		public class Assessment implements Parcelable {
			String id;
			String request_date;
			String assessment_type;
			String paid;
			String customer_id;
			String created_at;
			String updated_at;
			String status;
			String package_type;
			String health_assessment_id;
			String status_code;
			String doctor_name;
			String enterprise_id;
			String provider_name;



			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getRequest_date() {
				return request_date;
			}

			public void setRequest_date(String request_date) {
				this.request_date = request_date;
			}

			public String getAssessment_type() {
				return assessment_type;
			}

			public void setAssessment_type(String assessment_type) {
				this.assessment_type = assessment_type;
			}

			public String getPaid() {
				return paid;
			}

			public void setPaid(String paid) {
				this.paid = paid;
			}

			public String getCustomer_id() {
				return customer_id;
			}

			public void setCustomer_id(String customer_id) {
				this.customer_id = customer_id;
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

			public String getStatus() {
				return status;
			}

			public void setStatus(String status) {
				this.status = status;
			}

			public String getPackage_type() {
				return package_type;
			}

			public void setPackage_type(String package_type) {
				this.package_type = package_type;
			}

			public String getHealth_assessment_id() {
				return health_assessment_id;
			}

			public void setHealth_assessment_id(String health_assessment_id) {
				this.health_assessment_id = health_assessment_id;
			}

			public String getStatus_code() {
				return status_code;
			}

			public void setStatus_code(String status_code) {
				this.status_code = status_code;
			}

			public String getDoctor_name() {
				return doctor_name;
			}

			public void setDoctor_name(String doctor_name) {
				this.doctor_name = doctor_name;
			}

			public String getEnterprise_id() {
				return enterprise_id;
			}

			public void setEnterprise_id(String enterprise_id) {
				this.enterprise_id = enterprise_id;
			}

			public String getProvider_name() {
				return provider_name;
			}

			public void setProvider_name(String provider_name) {
				this.provider_name = provider_name;
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

	public class Comments implements Parcelable
	{

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
