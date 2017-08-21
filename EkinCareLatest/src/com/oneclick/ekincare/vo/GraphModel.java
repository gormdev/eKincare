package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.ekincare.util.DateUtility;

import java.util.Date;

/**
 * Created by rakeshk on 06-10-2015.
 */
public class GraphModel implements Parcelable{

    private Date date;
    private String result;
    private String mStringDate;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDateString() {
        return DateUtility.parseTodaysDate(date.toString());
    }

	public String getmStringDate() {
		return mStringDate;
	}

	public void setmStringDate(String mStringDate) {
		this.mStringDate = mStringDate;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mStringDate);
		dest.writeString(result);
	}


}
