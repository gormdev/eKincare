package com.oneclick.ekincare.vo;

/**
 * Created by RaviTejaN on 12-04-2016.
 */
public class NotificationModel {

    private int id;
    private String customer_id;
    private String family_member_id;
    private String title;
    private String type_of_notification;
    private String created_at;
    private String updated_at;
    private String description;

    public NotificationModel() {
    }


    public String getFamily_member_id() {
        return family_member_id;
    }

    public void setFamily_member_id(String family_member_id) {
        this.family_member_id = family_member_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getType_of_notification() {
        return type_of_notification;
    }

    public void setType_of_notification(String type_of_notification) {
        this.type_of_notification = type_of_notification;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

