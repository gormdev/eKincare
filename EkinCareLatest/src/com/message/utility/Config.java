package com.message.utility;


import java.util.ArrayList;

public abstract class Config {
    // copy this keys from your developer dashboard
    public static final String ACCESS_TOKEN = "14522a83b765454ab4ed75083b15d297";

    public static final int MESSAGE_SENT = 1000;
    public static final int MESSAGE_DEIVERED = 2000;
    public static final int MESSAGE_FAILED = 3000;

    public static final int INPUT_TYPE_TEXT = 200;
    public static final int INPUT_TYPE_NUMBER = 200;
    public static final int INPUT_TYPE_EMAIL = 200;

    public static String ADD_WIZARD_DETAIL_URL = "http://192.168.20.164:3000/v1/customers/" + "wizard";
    public static String ADD_FAMILY_MEMBER_URL = "http://192.168.20.164:3000/v1/customers/"+ "add_new_family_member";
    public static String LIst ="http://192.168.20.164:3000/v1/customers/doctors_list?package_id=";

    public static final ArrayList<String> glucouseList() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 50; i < 300; i++) {
            list.add("" + i);
        }
        return list;
    }

    public static final ArrayList<String> systolicList() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 80; i < 200; i++) {
            list.add("" + i);
        }
        return list;
    }
    public static final ArrayList<String> diastolicList() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 60; i < 110; i++) {
            list.add("" + i);
        }
        return list;
    }
    public static final ArrayList<String> heightFeetList() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i < 8; i++) {
            list.add("" + i);
        }
        return list;
    }
    public static final ArrayList<String> heightInchList() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 12; i++) {
            list.add("" + i);

        }
        return list;
    }

    public static final ArrayList<String> waightValuesList() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i < 220; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    public static final ArrayList<String> waistValuesList() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 10; i < 60; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    public static final ArrayList<String> bloodGroupList() {
        ArrayList<String> bloodGroupList = new ArrayList<String>();
        bloodGroupList.add("A+");
        bloodGroupList.add("A-");
        bloodGroupList.add("B+");
        bloodGroupList.add("B-");
        bloodGroupList.add("AB+");
        bloodGroupList.add("AB-");
        bloodGroupList.add("O+");
        bloodGroupList.add("O-");
        return bloodGroupList;
    }
}
