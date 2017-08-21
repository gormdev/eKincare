package com.oneclick.ekincare.vo;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Comment;

import com.oneclick.ekincare.vo.DentalAssessmentData.DentalAssessment.Recommendation;

import android.os.Parcel;
import android.os.Parcelable;

public class AssessmentData implements Parcelable {
	AssessmentInfo assessment_info;

	public AssessmentInfo getAssessment_info() {
		return assessment_info;
	}



	public static class AssessmentInfo implements Parcelable{
		Assessment assessment;
		private List<AssessmentsLabInfoData>assessments_lab_info;
		private List<Recommendation>recommendation;
		private List<Comments>comments;

		public AssessmentInfo(){
			assessments_lab_info = new ArrayList<AssessmentsLabInfoData>();
			recommendation=new ArrayList<Recommendation>();
			comments=new ArrayList<Comments>();
		}

		public List<AssessmentsLabInfoData> getAssessments_lab_info() {
			return assessments_lab_info;
		}

		public List<Recommendation> getRecommendation() {
			return recommendation;
		}

		public void setRecommendation(List<Recommendation> recommendation) {
			this.recommendation = recommendation;
		}

		public List<Comments> getComments() {
			return comments;
		}

		public void setComments(List<Comments> comments) {
			this.comments = comments;
		}

		public void setAssessments_lab_info(
				List<AssessmentsLabInfoData> assessments_lab_info) {
			this.assessments_lab_info = assessments_lab_info;
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


		public static class AssessmentsLabInfoData implements Parcelable {
			String lab_test_name;
			String lab_test_info;
			private List<TestComponentData>test_component;

			public AssessmentsLabInfoData(){
				test_component = new ArrayList<TestComponentData>();
			}

			public String getLab_test_name() {
				return lab_test_name;
			}

			public void setLab_test_name(String lab_test_name) {
				this.lab_test_name = lab_test_name;
			}

			public String getLab_test_info() {
				return lab_test_info;
			}

			public void setLab_test_info(String lab_test_info) {
				this.lab_test_info = lab_test_info;
			}

			public List<TestComponentData> getTest_component() {
				return test_component;
			}

			public void setTest_component(List<TestComponentData> test_component) {
				this.test_component = test_component;
			}

			public static class TestComponentData implements Parcelable{
				String test_component_name;
				String test_component_info;
				String lab_result_id;
				String result_value;
				String units;
				String idealRange;
				String color;
				String lab_result_increased;
				String header;
				String request_date;
				public String getRequest_date() {
					return request_date;
				}

				public void setRequest_date(String header) {
					this.request_date = header;
				}
				public String getHeader() {
					return header;
				}

				public void setHeader(String header) {
					this.header = header;
				}

				private List<GraphData>graph;

				public TestComponentData(){
					graph = new ArrayList<GraphData>();
				}

				public String getTest_component_name() {
					return test_component_name;
				}

				public void setTest_component_name(String test_component_name) {
					this.test_component_name = test_component_name;
				}


				public String getLab_result_id() {
					return lab_result_id;
				}

				public void setLab_result_id(String lab_result_id) {
					this.lab_result_id = lab_result_id;
				}

				public String getTest_component_info() {
					return test_component_info;
				}

				public void setTest_component_info(String test_component_info) {
					this.test_component_info = test_component_info;
				}

				public String getResult_value() {
					return result_value;
				}

				public void setResult_value(String result_value) {
					this.result_value = result_value;
				}

				public String getUnits() {
					return units;
				}

				public void setUnits(String units) {
					this.units = units;
				}

				public String getIdealRange() {
					return idealRange;
				}

				public void setIdealRange(String idealRange) {
					this.idealRange = idealRange;
				}

				public String getColor() {
					return color;
				}

				public void setColor(String color) {
					this.color = color;
				}




				public class GraphData implements Parcelable{
					  String date;
					  String result;
					public String getDate() {
						return date;
					}

					public void setDate(String date) {
						this.date = date;
					}

					public String getResult() {
						return result;
					}

					public void setResult(String result) {
						this.result = result;
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
	}

	public class Assessment implements Parcelable{
		String id;
		String request_date;
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

		public String getRequest_date() {
			return request_date;
		}

		public String getPaid() {
			return paid;
		}

		public String getCustomer_id() {
			return customer_id;
		}

		public String getCreated_at() {
			return created_at;
		}

		public String getUpdated_at() {
			return updated_at;
		}

		public String getStatus() {
			return status;
		}

		public String getPackage_type() {
			return package_type;
		}

		public String getHealth_assessment_id() {
			return health_assessment_id;
		}

		public String getStatus_code() {
			return status_code;
		}

		public String getDoctor_name() {
			return doctor_name;
		}

		public String getEnterprise_id() {
			return enterprise_id;
		}

		public String getProvider_name() {
			return provider_name;
		}


		public void setId(String id) {
			this.id = id;
		}

		public void setRequest_date(String request_date) {
			this.request_date = request_date;
		}

		public void setPaid(String paid) {
			this.paid = paid;
		}

		public void setCustomer_id(String customer_id) {
			this.customer_id = customer_id;
		}

		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}

		public void setUpdated_at(String updated_at) {
			this.updated_at = updated_at;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public void setPackage_type(String package_type) {
			this.package_type = package_type;
		}

		public void setHealth_assessment_id(String health_assessment_id) {
			this.health_assessment_id = health_assessment_id;
		}

		public void setStatus_code(String status_code) {
			this.status_code = status_code;
		}

		public void setDoctor_name(String doctor_name) {
			this.doctor_name = doctor_name;
		}

		public void setEnterprise_id(String enterprise_id) {
			this.enterprise_id = enterprise_id;
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
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

	public class Comments implements Parcelable{

		String doctor_name="";
		String description="";
		String created_at="";

		public void setDoctor_name(String doctor_name) {
			this.doctor_name = doctor_name;
		}
		public String getDoctor_name() {
			return doctor_name;
		}

		public void setDescription(String doctor_name) {
			this.description = doctor_name;
		}
		public String getDescription() {
			return description;
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
