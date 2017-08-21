package com.ekincare.ui.fragment;

import java.util.Date;

import com.ekincare.R;
import com.ekincare.app.CustomerManager;
import com.ekincare.util.BloodSOSHelperClass;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.oneclick.ekincare.MainActivity;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentSettingTest extends Fragment {
    MainActivity mMainActivity;
    View createView;

    //private boolean isHydrocareNotificationEnable;
    private boolean isBloodSOSSubscriptionEnable;
    private boolean isBloodSOSNotificationEnable;
    //private boolean isHydrocareSubscriptionEnable;
    private boolean isUploadAlertsEnabled;

    private Switch hydrocareNotification;
    private Switch bloodSOSNotification;
    private Switch bloodSOSAppEnable;
    //private Switch hydrocareApp;
    private Switch alertMeWifi;
    private Switch appNotification;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private PreferenceHelper prefs;
    MixpanelAPI mixpanel;
    CustomerManager customerManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTitle();

    }

    private void setTitle(){
        Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.settings));

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        customerManager = CustomerManager.getInstance(getActivity().getApplicationContext());
        mMainActivity = (MainActivity) getActivity();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        prefs = new PreferenceHelper(getActivity());

        //isHydrocareNotificationEnable = preferences.getBoolean("isHydrocareNotificationEnable", true);
        isBloodSOSSubscriptionEnable = preferences.getBoolean("isBloodSOSSubscriptionEnable", true);
        isBloodSOSNotificationEnable = preferences.getBoolean("isBloodSOSNotificationEnable", true);
        isUploadAlertsEnabled = preferences.getBoolean("isUploadAlertsEnabled", true);




    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        createView = inflater.inflate(R.layout.help_feedback, container, false);

        mixpanel = MixpanelAPI.getInstance(getActivity(), TagValues.MIXPANEL_TOKEN);
        mixpanel.timeEvent("SettingPage");
        try {
            JSONObject props = new JSONObject();
            props.put("LoginName",prefs.getCustomerName());
            props.put("LoginNumber",prefs.getUserName());
            mixpanel.track("SettingPage", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        hydrocareNotification = (Switch) createView.findViewById(R.id.hydrocare_notifications);
        bloodSOSNotification = (Switch) createView.findViewById(R.id.bloodSOS_notifications);
        bloodSOSAppEnable = (Switch) createView.findViewById(R.id.bloodSOS_app);
        //hydrocareApp = (Switch) createView.findViewById(R.id.hydrocare_app);
        alertMeWifi = (Switch) createView.findViewById(R.id.wifiAlert);
        appNotification=(Switch)createView.findViewById(R.id.app_custom_notification);


       // mMainActivity.mTextViewTitle.setText(R.string.settings);
        mMainActivity.mNotificationView.setVisibility(View.GONE);
        mMainActivity.switchFamilyMemberLayout.setVisibility(View.GONE);
        mMainActivity.mNotificationView.setVisibility(View.GONE);

        bloodSOSNotification.setChecked(isBloodSOSNotificationEnable);
        bloodSOSAppEnable.setChecked(isBloodSOSSubscriptionEnable);
        //hydrocareApp.setChecked(isHydrocareSubscriptionEnable);
        alertMeWifi.setChecked(isUploadAlertsEnabled);
        System.out.println("checkmy======"+prefs.getallNotification());


        System.out.println("check hyderocare========="+prefs.getisHydrocareSubscriptionEnable());

        if(prefs.getisHydrocareSubscriptionEnable()){
            hydrocareNotification.setChecked(true);
        }else{
            hydrocareNotification.setChecked(false);
        }

       /* if(prefs.isHydrocareNotificationEnable()){
            hydrocareNotification.setChecked(true);
        }
        else{
            hydrocareNotification.setChecked(false);
        }*/

        System.out.println("checkprefs===="+prefs.getallNotification());
        if(prefs.getallNotification()){
            System.out.println("checkprefs===="+"false");
            appNotification.setChecked(false);
            hydrocareNotification.setEnabled(true);
            bloodSOSNotification.setEnabled(true);
            bloodSOSAppEnable.setEnabled(true);
            //hydrocareApp.setEnabled(false);
            alertMeWifi.setEnabled(true);
        }else{
            System.out.println("checkprefs===="+"true");
            appNotification.setChecked(true);
            hydrocareNotification.setEnabled(false);
            bloodSOSNotification.setEnabled(false);
            bloodSOSAppEnable.setEnabled(false);
            //hydrocareApp.setEnabled(false);
            alertMeWifi.setEnabled(false);
        }





        appNotification.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    prefs.setallNotification(false);
                    hydrocareNotification.setEnabled(false);
                    bloodSOSNotification.setEnabled(false);
                    bloodSOSAppEnable.setEnabled(false);
                    //hydrocareApp.setEnabled(false);
                    alertMeWifi.setEnabled(false);
                    Toast.makeText(getActivity(),"You will not receive any more notification.For best experience we recommend you to trun off this feature.",Toast.LENGTH_LONG).show();
                } else {
                    prefs.setallNotification(true);
                    hydrocareNotification.setEnabled(true);
                    bloodSOSNotification.setEnabled(true);
                    bloodSOSAppEnable.setEnabled(true);
                    //hydrocareApp.setEnabled(true);
                    alertMeWifi.setEnabled(true);
                }

            }
        });

        hydrocareNotification.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    prefs.setisHydrocareSubscriptionEnable(true);

                } else {
                    //isHydrocareNotificationEnable = false;
                    prefs.setisHydrocareSubscriptionEnable(false);
                }
            }
        });

        bloodSOSAppEnable.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    isBloodSOSSubscriptionEnable = true;
                    isBloodSOSNotificationEnable = true;
                    bloodSOSNotification.setEnabled(true);
                    bloodSOSNotification.setChecked(true);
                } else {
                    isBloodSOSSubscriptionEnable = false;
                    isBloodSOSNotificationEnable = false;
                    bloodSOSNotification.setEnabled(false);
                    bloodSOSNotification.setChecked(false);
                }

                editor.putBoolean("isBloodSOSSubscriptionEnable", isBloodSOSSubscriptionEnable);
                editor.commit();
                editor.apply();

            }
        });

        bloodSOSNotification.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    isBloodSOSNotificationEnable = true;
                } else {
                    isBloodSOSNotificationEnable = false;
                }

                editor.putBoolean("isBloodSOSNotificationEnable", isBloodSOSNotificationEnable);
                editor.commit();
                editor.apply();

                BloodSOSHelperClass.setAlarm(getActivity(), 0,
                        BloodSOSHelperClass.getAlarmStartTime(new Date()).getTime(), 1000 * 60 * 60 * 2,
                        isBloodSOSSubscriptionEnable, isBloodSOSNotificationEnable);
            }
        });

        alertMeWifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    isUploadAlertsEnabled = true;
                } else {
                    isUploadAlertsEnabled = false;
                }

                editor.putBoolean("isUploadAlertsEnabled", isUploadAlertsEnabled);
                editor.commit();
                editor.apply();
            }
        });

        if (isUploadAlertsEnabled) {
            if (!checkWifiConnection()) {

                customerManager.showAlert("Wifi Disconnected!", getActivity());
            }
        }

        return createView;
    }

    public boolean checkWifiConnection() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        } else {
            return false;
        }
    }



}
