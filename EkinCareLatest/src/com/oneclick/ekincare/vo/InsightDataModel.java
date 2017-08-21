package com.oneclick.ekincare.vo;

import java.util.List;

/**
 * Created by RaviTejaN on 13-12-2016.
 */

public class InsightDataModel {
    private InsightCustomerHealthScore customer_health_score;
    private InsightWaist waist;
    private InsightBmi bmi;
    private List<HealthRisksData> health_risks_data;


    private List<InsightBehaviourPercentile> behaviour_percentile;
   // private List<InsightHeartDiseasesData> heart_diseases_data;

    public List<HealthRisksData> getHealth_risks_data() {
        return health_risks_data;
    }

    public void setHealth_risks_data(List<HealthRisksData> health_risks_data) {
        this.health_risks_data = health_risks_data;
    }

    public InsightCustomerHealthScore getCustomer_health_score() {
        return customer_health_score;
    }

    public void setCustomer_health_score(InsightCustomerHealthScore customer_health_score) {
        this.customer_health_score = customer_health_score;
    }

    public InsightWaist getWaist() {
        return waist;
    }

    public void setWaist(InsightWaist waist) {
        this.waist = waist;
    }

    public InsightBmi getBmi() {
        return bmi;
    }

    public void setBmi(InsightBmi bmi) {
        this.bmi = bmi;
    }

    public List<InsightBehaviourPercentile> getBehaviour_percentile() {
        return behaviour_percentile;
    }

    public void setBehaviour_percentile(List<InsightBehaviourPercentile> behaviour_percentile) {
        this.behaviour_percentile = behaviour_percentile;
    }

   /* public List<InsightHeartDiseasesData> getHeart_diseases_data() {
        return heart_diseases_data;
    }

    public void setHeart_diseases_data(List<InsightHeartDiseasesData> heart_diseases_data) {
        this.heart_diseases_data = heart_diseases_data;
    }*/
}
