
package com.oneclick.ekincare.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalRecord {

    @Expose
    private Integer id;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("consumed_date")
    @Expose
    private String consumedDate;
    @SerializedName("water_consumed")
    @Expose
    private String waterConsumed;
    @SerializedName("actual_consumption")
    @Expose
    private String actualConsumption;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    /**
     *
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     *     The customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     *
     * @param customerId
     *     The customer_id
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     *
     * @return
     *     The consumedDate
     */
    public String getConsumedDate() {
        return consumedDate;
    }

    /**
     *
     * @param consumedDate
     *     The consumed_date
     */
    public void setConsumedDate(String consumedDate) {
        this.consumedDate = consumedDate;
    }

    /**
     *
     * @return
     *     The waterConsumed
     */
    public String getWaterConsumed() {
        return waterConsumed;
    }

    /**
     *
     * @param waterConsumed
     *     The water_consumed
     */
    public void setWaterConsumed(String waterConsumed) {
        this.waterConsumed = waterConsumed;
    }

    /**
     *
     * @return
     *     The actualConsumption
     */
    public String getActualConsumption() {
        return actualConsumption;
    }

    /**
     *
     * @param actualConsumption
     *     The actual_consumption
     */
    public void setActualConsumption(String actualConsumption) {
        this.actualConsumption = actualConsumption;
    }

    /**
     *
     * @return
     *     The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     *     The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     *     The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     *     The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Totalrecord{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", consumedDate='" + consumedDate + '\'' +
                ", waterConsumed='" + waterConsumed + '\'' +
                ", actualConsumption='" + actualConsumption + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
