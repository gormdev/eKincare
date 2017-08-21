package com.oneclick.ekincare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.DataStorage.DatabaseOverAllHandler;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ekincare.R;
import com.ekincare.app.CustomerManager;
import com.ekincare.app.VolleyRequestSingleton;
import com.ekincare.ui.fragment.FragmentActiviateMedication;
import com.ekincare.ui.fragment.FragmentAddMedication;
import com.ekincare.util.AddMedicationToListInterface;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.Medication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActivityFamilyMemberAddMedication extends AppCompatActivity implements AddMedicationToListInterface {


    private ArrayList<Medication> medicationList;
    private Toolbar toolbar;
    boolean isActivated = false;
    private String strFamilyMemberKey = "";
    private PreferenceHelper prefs;
    private CustomerManager customerManager;
    String responseCode;
    private DatabaseOverAllHandler dbHandler;

    private boolean isEdit = false;

    Medication medication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        customerManager = CustomerManager.getInstance(this);
        prefs = new PreferenceHelper(this);
        dbHandler = new DatabaseOverAllHandler(this);
        medicationList = new ArrayList<Medication>();

        if(getIntent().getBooleanExtra("isEdit",false)){
            medication = getIntent().getParcelableExtra("medicineData");
            System.out.println("AddMedicationActivity.onCreate medication = " + medication.getName());
        }

        isEdit = getIntent().getBooleanExtra("isEdit",false);

        setupToolbar();

        setUpFragment(new FragmentAddMedication());
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(isEdit()){
            toolbar.setTitle("Edit Medication");
        }else{
            toolbar.setTitle("Add Medication");
        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.container);
                if(mFragment instanceof FragmentActiviateMedication){
                    if(!isActivated){
                        showErrorDialog("Add Medication?","You have not activated medications.Do you want discard this medication.?");
                    }
                }else if(mFragment instanceof FragmentAddMedication && medicationList.size()>0){
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container,new FragmentActiviateMedication());
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                }
                else{
                    ActivityFamilyMemberAddMedication.this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                    ActivityFamilyMemberAddMedication.this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
                    finish();
                }
            }
        });
    }

    private void setUpFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container,fragment,null);
        fragmentTransaction.commit();
    }

    @Override
    public void addMedicationToList(Medication medicationDataModel) {
        medicationList.add(medicationDataModel);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,new FragmentActiviateMedication());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    @Override
    public ArrayList<Medication> getMedicationList() {
        return medicationList;
    }

    @Override
    public void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    @Override
    public void deleteFromMedicationList(Medication medicationDataModel) {
        medicationList.remove(medicationDataModel);
    }

    @Override
    public void activateMedication(JSONObject jsonObject, String id)
    {
        System.out.println("AddMedicationActivity.activateMedication jsonObject="+jsonObject);

        int method ;
        String url;

        if(isEdit()){
            method = Request.Method.PUT;

            url = TagValues.Get_All_Medication_URL+"/"+id;
            System.out.println("AddMedicationActivity.activateMedication url=" + url);

        }
        else{
            method = Request.Method.POST;
            url = TagValues.Get_All_Medication_URL;
        }

        JsonObjectRequest request = new JsonObjectRequest(method, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result)
            {
                System.out.println("5.5 AddMedicationActivity.activateMedication = " + result);
                if (result != null) {
                    try {
                        if(result.getString("status").equals("200")){
                            isActivated=true;
                            dbHandler.clearMedication();
                            Toast.makeText(ActivityFamilyMemberAddMedication.this,"Medication Activated",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ActivityFamilyMemberAddMedication.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            intent.putExtra("ACTIVATE_MEDICATION",true);
                            startActivity(intent);
                            finish();
                        }else if(responseCode.equals("304 Not Modified")){}
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    //Nodata
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                System.out.println("1.7 ActivityFamilyMember.backgroundTimeLineThreadStoreData error = " + arg0);}
        })
        {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (customerManager.isLoggedInCustomer()) {
                    strFamilyMemberKey = "";
                } else {
                    strFamilyMemberKey = customerManager.getCurrentFamilyMember().getIdentification_token();
                }

                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(ActivityFamilyMemberAddMedication.this));
                if (!strFamilyMemberKey.equalsIgnoreCase(""))
                    params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);
                params.put("Content-type", "application/json");

                return params;
            }
        };

        VolleyRequestSingleton.getInstance(ActivityFamilyMemberAddMedication.this.getApplicationContext()).addToRequestQueue(request);
        //getActivity().finish();
    }

    @Override
    public boolean isEdit() {
        return isEdit;
    }

    @Override
    public Medication getMedicationObject() {
        return medication;
    }

    @Override
    public void onBackPressed() {
        Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if(mFragment instanceof FragmentActiviateMedication){
            if(!isActivated){
                if(isEdit()){
                    showErrorDialog("Update Medication?","You have not activated medications.Do you want discard this medication updation.?");
                }else{
                    showErrorDialog("Add Medication?","You have not activated medications.Do you want discard this medication.?");
                }
            }
        }else if(mFragment instanceof FragmentAddMedication && medicationList.size()>0){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,new FragmentActiviateMedication());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }
        else{
            super.onBackPressed();
        }
    }

    private void showErrorDialog(String title,String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityFamilyMemberAddMedication.this);

        builder.setTitle(title);
        builder.setMessage(message);

        String positiveText = "Yes";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

        String negativeText = "Close";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        // display dialog
        dialog.show();
    }

}
