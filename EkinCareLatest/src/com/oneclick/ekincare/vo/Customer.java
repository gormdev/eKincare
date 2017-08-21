package com.oneclick.ekincare.vo;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jhansi on 21/01/15.
 */
public class Customer implements Parcelable{

	private String id;
	private String first_name;
	private String last_name;
	private String gender;
	private String relation;
	private String martial_status;
	private String email;
	private String weight;
	private String date_of_birth;
	private String mobile_number;
	private String customer_id;
	private String number_of_children;
	private String daily_activity;
	private String frequency_of_exercise;
	private String hydrocare_subscripted;
	private String smoke;
	private String alcohol;
	private String medical_insurance;
	private String blood_group_id;
	private String diet;
	private String feet;
	private String wizard_status;
	private String inches;
	private String general_issues;
	private String other_body_parts;
	private Image image;
	private String otp_secret_key;
	private String is_mobile_number_verified;
	private String health_score;
	private String optimum_health_score;
	private String hypertensive_treatment;
	private String diabetic_treatment;
	private String identification_token;
	private CustomerVitals customer_vitals;
	private List<FamilyMedicalHistoryModel> family_medical_histories;
	private List<FamilyAddresses> addresses;
	private List<String> gamification;
	private int last_question=-1;
	private String isWizardComplete;
	private Systolic blood_sugar;
	private Systolic random_blood_sugar;
	private Systolic hba1c_sugar;

	private Systolic systolic;
	private Systolic diastolic;
	private String msg;
	private String guardian_id;
	private String referral_code;
	private String diabetes_risk;
	private String cvd_risk;
	private String hypertensive_risk;
	private ImpactSteps impact_steps;
	//	private Trends weight_trends;
	//	private Trends height_trends;
	//	private Allergies allergies;
	private String created_at="";

	private String updated_at="";

	private int userColor = Color.GREEN;



	public int getUserColor() {
		return userColor;
	}

	public void setUserColor(int color) {
		this.userColor = color;
	}
	public List<FamilyAddresses> getAddresses() {
		return addresses;
	}

	public String getReferral_code() {
		return referral_code;
	}

	public String getId() {
		return id;
	}



	public String getFirst_name() {
		return first_name;
	}



	public String getLast_name() {
		return last_name;
	}
	public String getGender() {
		return gender;
	}
	public String getEmail() {
		return email;
	}

	public String getDate_of_birth() {
		return date_of_birth;
	}

	public String getMobile_number() {
		return mobile_number;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public String getDaily_activity() {
		return daily_activity;
	}

	public String getFrequency_of_exercise() {
		return frequency_of_exercise;
	}
	public String getSmoke() {
		return smoke;
	}



	public String getAlcohol() {
		return alcohol;
	}


	public String getWizard_status() {
		return wizard_status;
	}
	public String getDiet() {
		return diet;
	}

	public String getOptimum_health_score() {
		return optimum_health_score;
	}

	public void setOptimum_health_score(String optimum_health_score) {
		this.optimum_health_score = optimum_health_score;
	}

	public String getHealth_score() {
		return health_score;
	}
	public String getIdentification_token() {
		return identification_token;
	}

	public CustomerVitals getCustomer_vitals() {
		return customer_vitals;
	}

	public List<FamilyMedicalHistoryModel> getFamily_medical_histories() {
		return family_medical_histories;
	}

	public void setFamily_medical_histories(List<FamilyMedicalHistoryModel> family_medical_histories) {
		this.family_medical_histories = family_medical_histories;
	}

	public String getAge() {

		//      Calendar now = Calendar.getInstance();
		//      if (dateOfBirth != null) {
		//          Calendar dob = Calendar.getInstance();
		//          dob.setTime(dateOfBirth);
		//          int birthYear = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
		//          return birthYear;
		//      }

		LocalDate now = new LocalDate();
		if (date_of_birth != null) {
			LocalDate birthdate = new LocalDate(date_of_birth);
			Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
			return ""+period.getYears();
		}
		return "";

	}

	public boolean isChild() {

		boolean isChild=false;
		LocalDate now = new LocalDate();
		if (date_of_birth != null) {
			LocalDate birthdate = new LocalDate(date_of_birth);
			Period period = new Period(birthdate, now, PeriodType.yearMonthDay());

			if(period.getYears()>18){
				isChild=false;
			}else if(period.getYears()==18){
				if(period.getMonths()!=0){
					isChild=false;
				}else if(period.getDays()!=0){
					isChild=false;
				}else{
					isChild=true;
				}
			}else{
				isChild=true;
			}
		}
		return isChild;

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(id);
		//		dest.writeString(guardian_id);
		dest.writeString(first_name);
		dest.writeString(last_name);
		dest.writeString(gender);
		dest.writeString(relation);
		dest.writeString(martial_status);
		dest.writeString(email);
		dest.writeString(weight);
		dest.writeString(date_of_birth);
		dest.writeString(mobile_number);
		dest.writeString(customer_id);
		dest.writeString(number_of_children);
		dest.writeString(daily_activity);
		dest.writeString(frequency_of_exercise);
		dest.writeString(hydrocare_subscripted);
		dest.writeString(smoke);
		dest.writeString(alcohol);
		dest.writeString(wizard_status);
		dest.writeString(medical_insurance);
		dest.writeString(blood_group_id);
		dest.writeString(diet);
		dest.writeString(feet);
		dest.writeString(inches);
		dest.writeString(general_issues);
		dest.writeString(other_body_parts);
		dest.writeParcelable(image, flags);
		dest.writeString(otp_secret_key);
		dest.writeString(is_mobile_number_verified);
		dest.writeString(health_score);
		dest.writeString(hypertensive_treatment);
		dest.writeString(diabetic_treatment);
		dest.writeString(identification_token);
		dest.writeInt(last_question);
		dest.writeString(isWizardComplete);
		dest.writeString(diabetes_risk);
		dest.writeString(cvd_risk);
		dest.writeString(hypertensive_risk);
		dest.writeString(created_at);
		dest.writeString(updated_at);
		dest.writeParcelable(customer_vitals,flags);
		dest.writeParcelable(impact_steps, flags);
		//		dest.writeParcelable(weight_trends, flags);
		//		dest.writeParcelable(height_trends, flags);
		//		dest.writeParcelable(allergies, flags);
		dest.writeParcelable(blood_sugar,flags);
		dest.writeParcelable(random_blood_sugar,flags);
		dest.writeParcelable(hba1c_sugar,flags);
		dest.writeParcelable(systolic,flags);
		dest.writeParcelable(diastolic,flags);
		dest.writeTypedList(family_medical_histories);
		dest.writeTypedList(addresses);
		dest.writeString(msg);
	}


	public static Parcelable.Creator<Customer> CREATOR = new Creator<Customer>(){
		@Override
		public Customer[] newArray(int size) {
			return new Customer[size];
		}

		@Override
		public Customer createFromParcel(Parcel source) {

			return new Customer(source);
		}
	};

	public Customer(final Parcel in){

		id = in.readString();
		//		guardian_id = in.readString();
		first_name = in.readString();
		last_name = in.readString();
		gender = in.readString();
		relation = in.readString();
		martial_status = in.readString();
		email = in.readString();
		weight = in.readString();
		date_of_birth = in.readString();
		mobile_number = in.readString();
		customer_id = in.readString();
		number_of_children = in.readString();
		daily_activity = in.readString();
		frequency_of_exercise = in.readString();
		hydrocare_subscripted = in.readString();
		smoke = in.readString();
		alcohol = in.readString();
		wizard_status=in.readString();
		medical_insurance = in.readString();
		blood_group_id = in.readString();
		diet = in.readString();
		feet = in.readString();
		inches = in.readString();
		general_issues = in.readString();
		other_body_parts = in.readString();
		in.readParcelable(image.getClass().getClassLoader());
		otp_secret_key = in.readString();
		is_mobile_number_verified = in.readString();
		health_score = in.readString();
		hypertensive_treatment = in.readString();
		diabetic_treatment = in.readString();
		identification_token = in.readString();
		last_question = in.readInt();
		isWizardComplete = in.readString();
		diabetes_risk = in.readString();
		cvd_risk = in.readString();
		hypertensive_risk = in.readString();
		created_at = in.readString();
		updated_at = in.readString();
		in.readParcelable(customer_vitals.getClass().getClassLoader());
		in.readParcelable(impact_steps.getClass().getClassLoader());
		//		in.readParcelable(weight_trends.getClass().getClassLoader());
		//		in.readParcelable(height_trends.getClass().getClassLoader());
		//		in.readParcelable(allergies.getClass().getClassLoader());
		in.readTypedList(family_medical_histories, FamilyMedicalHistoryModel.CREATOR);
		in.readTypedList(addresses,FamilyAddresses.CREATOR);
		in.readParcelable(blood_sugar.getClass().getClassLoader());
		in.readParcelable(random_blood_sugar.getClass().getClassLoader());
		in.readParcelable(hba1c_sugar.getClass().getClassLoader());
		in.readParcelable(systolic.getClass().getClassLoader());
		in.readParcelable(diastolic.getClass().getClassLoader());
		msg = in.readString();

	}



	public Systolic getRandom_blood_sugar() {
		return random_blood_sugar;
	}
	public Systolic getBlood_sugar() {
		return blood_sugar;
	}
	public Systolic getSystolic() {
		return systolic;
	}
	public Systolic getDiastolic() {
		return diastolic;
	}
	public ImpactSteps getImpact_steps() {
		return impact_steps;
	}
	public String getDiabetes_risk() {
		return diabetes_risk;
	}
	public String getCvd_risk() {
		return cvd_risk;
	}
	public String getHypertensive_risk() {
		return hypertensive_risk;
	}


	public String getHydrocare_subscripted() {
		return hydrocare_subscripted;
	}






}
