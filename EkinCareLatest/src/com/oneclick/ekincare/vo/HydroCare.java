package com.oneclick.ekincare.vo;

import java.util.Date;

/**
 * Created by ehc on 14/5/15.
 */
public class HydroCare {
  private Date dateOfEntry;
  private float inTake;
  private float target;
  private int status;

    @Override
    public String toString() {
        return "HydroCare{" +
                "dateOfEntry=" + dateOfEntry +
                ", inTake=" + inTake +
                ", target=" + target +
                ", status=" + status +
                ", userId=" + userId +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private int userId;

    public float getInTake() {
        return inTake;
    }

    public void setInTake(float inTake) {
        this.inTake = inTake;
    }

    public float getTarget() {
        return target;
    }

    public void setTarget(float target) {
        this.target = target;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getDateOfEntry() {

        return dateOfEntry;
    }

    public void setDateOfEntry(Date dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

}
