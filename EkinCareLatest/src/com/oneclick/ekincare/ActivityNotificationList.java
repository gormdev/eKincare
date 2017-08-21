package com.oneclick.ekincare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DataStorage.DatabaseOverAllHandler;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ekincare.R;
import com.ekincare.app.CustomerManager;
import com.ekincare.app.ProfileManager;
import com.ekincare.app.VolleyRequestSingleton;
import com.ekincare.ui.custom.SwipeDismissListViewTouchListener;
import com.ekincare.util.DateUtility;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.oneclick.ekincare.helper.CustomeDialog;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.NotificationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ajay on 04-08-2016.
 */
public class ActivityNotificationList extends AppCompatActivity
{

    TextView deletallNotification;
    Toolbar toolbar;
    ListView listView;
    NotificationAdaptersceond adapter;
    DatabaseOverAllHandler handler;
    ArrayList<NotificationModel> notificationList;
    RelativeLayout mMainLayout;
    PreferenceHelper prefs;
    String notificationId;
    String message;
    String nfcount;
    ProfileManager profileManager;
    List<Customer> familyMembers;
    MixpanelAPI mixpanel;
    Dialog mAlert_Dialog;
    CircleProgressBar progressWithArrow;
    CustomerManager customerManager;


    Map<String, String> cacheMap = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity_layout);
        customerManager= CustomerManager.getInstance(ActivityNotificationList.this);
        prefs = new PreferenceHelper(this);
        mixpanel = MixpanelAPI.getInstance(this, TagValues.MIXPANEL_TOKEN);
        mixpanel.timeEvent("NotificationPage");
        try {
            JSONObject props = new JSONObject();
            props.put("LoginName",prefs.getCustomerName());
            props.put("LoginNumber",prefs.getUserName());
            mixpanel.track("NotificationPage", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        mMainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        listView = (ListView) findViewById(R.id.list);
        toolbar=(Toolbar)findViewById(R.id.toolbar) ;
        deletallNotification = (TextView) findViewById(R.id.deletall);
        deletallNotification.setVisibility(View.GONE);
        setUpToolBar();

        profileManager = ProfileManager.getInstance(ActivityNotificationList.this);
        familyMembers = profileManager.getFamilyMembers();
        if (familyMembers != null) {
            for (Customer customer : familyMembers) {
                cacheMap.put(customer.getId(), customer.getFirst_name());
            }
        }

        handler = new DatabaseOverAllHandler(this);
        if (NetworkCaller.isInternetOncheck(this)) {
            handler.deleteAllData();
            notificationDataFetchRequest();

        } else {

            notificationList = handler.getAllNotification();
            if (notificationList.size() == 0) {
                mMainLayout.setVisibility(View.VISIBLE);
                deletallNotification.setVisibility(View.GONE);

            } else {
                Collections.reverse(notificationList);

                adapter = new NotificationAdaptersceond(this, notificationList);
                listView.setAdapter(adapter);
            }


        }

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                if (NetworkCaller.isInternetOncheck(ActivityNotificationList.this)) {
                                    for (int position : reverseSortedPositions) {
                                        notificationId = Integer.toString(notificationList.get(position).getId());
                                        notificationList.remove(position);
                                        adapter.notifyDataSetChanged();
                                    }

                                    notificationDeletePatchRequest();

                                } else {
                                    showAlert(getResources().getString(R.string.internet_not_available), ActivityNotificationList.this);
                                }
                                if (adapter.getCount() == 0) {
                                    mMainLayout.setVisibility(View.VISIBLE);
                                    deletallNotification.setVisibility(View.GONE);
                                }
                            }
                        });
        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener(touchListener.makeScrollListener());
        /*
        Delete All Notifications
         */
        deletallNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkCaller.isInternetOncheck(ActivityNotificationList.this)) {
                    notificationList.clear();
                    adapter.notifyDataSetChanged();

                    notificationReadAllPatchRequest();

                    if (adapter.getCount() == 0) {
                        mMainLayout.setVisibility(View.VISIBLE);
                        deletallNotification.setVisibility(View.GONE);
                    }
                } else {
                    showAlert(getResources().getString(R.string.internet_not_available), ActivityNotificationList.this);
                }

            }
        });
    }

    private void notificationReadAllPatchRequest() {

        String URL = TagValues.NOTIFICATION_LIST + "/" + "read" ;
        showPDialog();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PATCH,URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            hidePDialog();
                            try {
                                message = response.getString("message");
                                // nfcount=response.getString("count");
                                prefs.setNotifiCount(nfcount);
                                if (message.equals("Success")) {
                                    mMainLayout.setVisibility(View.VISIBLE);
                                    deletallNotification.setVisibility(View.GONE);
                                    prefs.setNotifiCount("");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            CustomeDialog.dispDialog(ActivityNotificationList.this, TagValues.DATA_NOT_FOUND);
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(ActivityNotificationList.this));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

    }

    private void notificationDeletePatchRequest() {

        String URL = TagValues.NOTIFICATION_LIST + "/" + notificationId  ;
        showPDialog();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PATCH,URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            hidePDialog();
                            try {
                                message = response.getString("message");
                                nfcount = response.getString("count");
                                Toast.makeText(ActivityNotificationList.this, message, Toast.LENGTH_SHORT).show();
                                prefs.setNotifiCount(nfcount);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            CustomeDialog.dispDialog(ActivityNotificationList.this, TagValues.DATA_NOT_FOUND);
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(ActivityNotificationList.this));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);


    }

    private void notificationDataFetchRequest() {
        String URL = TagValues.NOTIFICATION_LIST  ;
        showPDialog();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            System.out.println("login verify===="+response.toString());
                            hidePDialog();
                            notificationDataFetchResponse(response);
                        }else{
                            CustomeDialog.dispDialog(ActivityNotificationList.this, TagValues.DATA_NOT_FOUND);
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(ActivityNotificationList.this));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

    }

    private void notificationDataFetchResponse(JSONObject response) {

        try {
            JSONArray jsonArray = response.getJSONArray("notifications");
            if (jsonArray.length()>0){
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectCity = jsonArray.getJSONObject(i);
                    int notificationID = jsonObjectCity.getInt("id");
                    String notificationCID = jsonObjectCity.getString("customer_id");
                    String notificationFID = jsonObjectCity.getString("family_member_id");
                    String notificationTitle = jsonObjectCity.getString("title");
                    String notificationType = jsonObjectCity.getString("type_of_notification");
                    String notificationCreateDate = jsonObjectCity.getString("created_at");
                    String notificationDesc = jsonObjectCity.getString("description");
                    NotificationModel nfData = new NotificationModel();
                    nfData.setId(notificationID);
                    nfData.setTitle(notificationTitle);
                    nfData.setType_of_notification(notificationType);
                    nfData.setCreated_at(notificationCreateDate);
                    nfData.setDescription(notificationDesc);
                    nfData.setFamily_member_id(notificationFID);
                    nfData.setCustomer_id(notificationCID);
                    handler.addNotification(nfData);// Inserting into DB

                    notificationList = handler.getAllNotification();
                    if (notificationList.size() == 0) {
                        mMainLayout.setVisibility(View.VISIBLE);
                        deletallNotification.setVisibility(View.GONE);
                    } else {
                        Collections.reverse(notificationList);
                        adapter = new NotificationAdaptersceond(this, notificationList);
                        listView.setAdapter(adapter);
                        deletallNotification.setVisibility(View.VISIBLE);

                    }

                }

            }else{
                mMainLayout.setVisibility(View.VISIBLE);
                deletallNotification.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void setUpToolBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Notifications");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityNotificationList.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        });
    }



    private void hidePDialog() {
        if (mAlert_Dialog != null) {
            mAlert_Dialog.dismiss();
            mAlert_Dialog=null;
        }
    }

    private void showPDialog() {
        mAlert_Dialog = new Dialog(ActivityNotificationList.this);
        mAlert_Dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mAlert_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAlert_Dialog.setContentView(R.layout.materialprogressbar);
        mAlert_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mAlert_Dialog.setCancelable(true);
        mAlert_Dialog.setCanceledOnTouchOutside(true);
        progressWithArrow = (CircleProgressBar)mAlert_Dialog.findViewById(R.id.progressWithArrow);
        progressWithArrow.setColorSchemeResources(android.R.color.holo_blue_light);
        mAlert_Dialog.show();
    }

    public void showAlert(String message, Context mContext) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        mBuilder.setTitle(mContext.getResources().getString(R.string.app_name));
        mBuilder.setMessage(message);
        mBuilder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mBuilder.create();
        mBuilder.show();
    }


    public class NotificationAdaptersceond extends BaseAdapter {

        Context context;
        ArrayList<NotificationModel> listData;

        public NotificationAdaptersceond(Context context, ArrayList<NotificationModel> listData) {
            this.context = context;
            this.listData = listData;
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            NotificationAdaptersceond.ViewHolder viewHolder = null;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.notification_row, null);
                viewHolder = new NotificationAdaptersceond.ViewHolder();
                viewHolder.notificationTitle = (TextView) view.findViewById(R.id.t1);
                viewHolder.notificationDecription = (TextView) view.findViewById(R.id.t2);
                viewHolder.uploadRecordsLable = (TextView) view.findViewById(R.id.upload_records);
                viewHolder.separator = (LinearLayout) view.findViewById(R.id.upload_records_separators);
                viewHolder.nfTypeImage = (ImageView) view.findViewById(R.id.img_for_notification_type);
                viewHolder.notificationDate = (TextView) view.findViewById(R.id.date_nf);
                view.setTag(viewHolder);
            } else {
                viewHolder = (NotificationAdaptersceond.ViewHolder) view.getTag();
            }
            final NotificationModel notificationData = listData.get(position);
            //  Set Title with userName
            String name = null;
            name = cacheMap.get(notificationData.getFamily_member_id());
            if (name == null) {
                name = "You";
            }

            if (notificationData.getType_of_notification().equals("assessment")) {
                if (notificationData.getFamily_member_id() == null) {
                    customerManager.setCurrentCustomer(profileManager.getLoggedinCustomer());
                    viewHolder.notificationTitle.setText("Health check reminder for " + customerManager.getCurrentCustomer().getFirst_name());
                } else {
                    viewHolder.notificationTitle.setText("Health check reminder for " + name);
                }
            } else if (notificationData.getType_of_notification().equals("medication")) {
                if (notificationData.getFamily_member_id() == null) {
                    customerManager.setCurrentCustomer(profileManager.getLoggedinCustomer());
                    viewHolder.notificationTitle.setText("Medication for " + customerManager.getCurrentCustomer().getFirst_name());
                } else {
                    viewHolder.notificationTitle.setText("Medication for " + name);
                }
            } else if (notificationData.getType_of_notification().equals("wizard")) {
                if (notificationData.getFamily_member_id() == null) {
                    customerManager.setCurrentCustomer(profileManager.getLoggedinCustomer());
                    viewHolder.notificationTitle.setText("Profile completion reminder for " + customerManager.getCurrentCustomer().getFirst_name());

                } else {
                    viewHolder.notificationTitle.setText("Profile completion reminder for  " + name);
                }
            } else if (notificationData.getType_of_notification().equals("appointment")) {
                if (notificationData.getFamily_member_id() == null) {
                    customerManager.setCurrentCustomer(profileManager.getLoggedinCustomer());
                    viewHolder.notificationTitle.setText("Appointment reminder for " + customerManager.getCurrentCustomer().getFirst_name());
                } else {
                    viewHolder.notificationTitle.setText("Appointment reminder for " + name);
                }
            } else if (notificationData.getType_of_notification().equals("vaccination")) {
                if (notificationData.getFamily_member_id() == null) {
                    customerManager.setCurrentCustomer(profileManager.getLoggedinCustomer());
                    viewHolder.notificationTitle.setText("Vaccination reminder for " + customerManager.getCurrentCustomer().getFirst_name());
                } else {
                    viewHolder.notificationTitle.setText("Vaccination reminder for " + name);
                }

            }else{
                viewHolder.notificationTitle.setText("eKincare");
            }



            viewHolder.notificationDecription.setText(notificationData.getDescription());
            viewHolder.notificationDate.setText(DateUtility.getconvertNotificationdate(notificationData.getCreated_at()));
            viewHolder.uploadRecordsLable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (notificationData.getType_of_notification().equals("assessment")) {
                        if (notificationData.getFamily_member_id().equals("null")) {
                            customerManager.setCurrentFamilyMember(profileManager.getLoggedinCustomer());
                           /* Intent intent = new Intent(ActivityNotificationList.this, ActivityFamilyMember.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
*/

                        } else {

                            for (Customer customer : familyMembers) {

                                if (customer.getId().equalsIgnoreCase(notificationData.getFamily_member_id())) {
                                    customerManager.setCurrentFamilyMember(customer);
                                   /* Intent intent = new Intent(ActivityNotificationList.this, ActivityFamilyMember.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);*/
                                }
                            }

                        }

                    } else if (notificationData.getType_of_notification().equals("medication")) {
                        if (notificationData.getFamily_member_id().equals("null")) {
                            customerManager.setCurrentFamilyMember(profileManager.getLoggedinCustomer());
                            /*Intent intent = new Intent(ActivityNotificationList.this, ActivityFamilyMember.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);*/

                        } else {
                            familyMembers = profileManager.getFamilyMembers();
                            for (Customer customer : familyMembers) {

                                if (customer.getId().equalsIgnoreCase(notificationData.getFamily_member_id())) {
                                    customerManager.setCurrentFamilyMember(customer);
                                    /*Intent intent = new Intent(ActivityNotificationList.this, ActivityFamilyMember.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);*/
                                }
                            }


                        }


                    } else if (notificationData.getType_of_notification().equals("wizard")) {
                        if (notificationData.getFamily_member_id().equals("null")) {
                            customerManager.setCurrentFamilyMember(profileManager.getLoggedinCustomer());
                           /* Intent intent = new Intent(ActivityNotificationList.this, ActivityFamilyMember.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);*/
                        } else {
                            familyMembers = profileManager.getFamilyMembers();
                            for (Customer customer : familyMembers) {
                                if (customer.getId().equalsIgnoreCase(notificationData.getFamily_member_id())) {
                                    customerManager.setCurrentFamilyMember(customer);
                                   /* Intent intent = new Intent(ActivityNotificationList.this, ActivityFamilyMember.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);*/
                                }
                            }

                        }
                    } else if (notificationData.getType_of_notification().equals("appointment")) {
                        if (notificationData.getFamily_member_id().equals("null")) {
                            customerManager.setCurrentFamilyMember(profileManager.getLoggedinCustomer());
                            /*Intent intent = new Intent(ActivityNotificationList.this, ActivityFamilyMember.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);*/
                        } else {
                            familyMembers = profileManager.getFamilyMembers();
                            for (Customer customer : familyMembers) {
                                if (customer.getId().equalsIgnoreCase(notificationData.getFamily_member_id())) {
                                    customerManager.setCurrentFamilyMember(customer);
                                   /* Intent intent = new Intent(ActivityNotificationList.this, ActivityFamilyMember.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);*/
                                }
                            }

                        }


                    } else if (notificationData.getType_of_notification().equals("vaccination")) {
                        if (notificationData.getFamily_member_id().equals("null")) {
                            customerManager.setCurrentFamilyMember(profileManager.getLoggedinCustomer());
                            /*Intent intent = new Intent(ActivityNotificationList.this, ActivityFamilyMember.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);*/

                        } else {
                            familyMembers = profileManager.getFamilyMembers();
                            for (Customer customer : familyMembers) {
                                if (customer.getId().equalsIgnoreCase(notificationData.getFamily_member_id())) {
                                    customerManager.setCurrentFamilyMember(customer);
                                   /* Intent intent = new Intent(ActivityNotificationList.this, ActivityFamilyMember.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);*/
                                }
                            }

                        }

                    }
                }
            });


            if (notificationData.getType_of_notification().equals("assessment")) {
                viewHolder.uploadRecordsLable.setText("Schedule Appointment");
                viewHolder.uploadRecordsLable.setVisibility(View.VISIBLE);
                viewHolder.separator.setVisibility(View.VISIBLE);
            } else if (notificationData.getType_of_notification().equals("medication")) {
                viewHolder.uploadRecordsLable.setText("Medication");
                viewHolder.uploadRecordsLable.setVisibility(View.VISIBLE);
                viewHolder.separator.setVisibility(View.VISIBLE);
            } else if (notificationData.getType_of_notification().equals("wizard")) {
                viewHolder.uploadRecordsLable.setText("Complete profile");
                viewHolder.uploadRecordsLable.setVisibility(View.VISIBLE);
                viewHolder.separator.setVisibility(View.VISIBLE);
            } else if (notificationData.getType_of_notification().equals("appointment")) {
                viewHolder.uploadRecordsLable.setVisibility(View.VISIBLE);
                viewHolder.uploadRecordsLable.setText("Upload Records");
                viewHolder.separator.setVisibility(View.VISIBLE);
            } else if (notificationData.getType_of_notification().equals("vaccination")) {
                viewHolder.uploadRecordsLable.setText("Update Vaccination");
                viewHolder.uploadRecordsLable.setVisibility(View.VISIBLE);
                viewHolder.separator.setVisibility(View.VISIBLE);
            } else {
                viewHolder.uploadRecordsLable.setVisibility(View.GONE);
                viewHolder.separator.setVisibility(View.GONE);
            }

            if (notificationData.getType_of_notification().equals("assessment")) {
                viewHolder.nfTypeImage.setBackgroundResource(R.drawable.notifications_alert);

            } else if (notificationData.getType_of_notification().equals("medication")) {
                viewHolder.nfTypeImage.setBackgroundResource(R.drawable.notifications_alert);

            } else if (notificationData.getType_of_notification().equals("wizard")) {
                viewHolder.nfTypeImage.setBackgroundResource(R.drawable.notification_wizard);


            } else if (notificationData.getType_of_notification().equals("appointment")) {
                viewHolder.nfTypeImage.setBackgroundResource(R.drawable.notifications_alert);


            } else if (notificationData.getType_of_notification().equals("vaccination")) {
                viewHolder.nfTypeImage.setBackgroundResource(R.drawable.notifications_alert);

            }else{
                viewHolder.nfTypeImage.setBackgroundResource(R.drawable.ic_launcher);

            }


            return view;
        }

        class ViewHolder {
            private TextView notificationTitle;
            private TextView notificationDecription;
            private TextView uploadRecordsLable;
            private LinearLayout separator;
            private ImageView nfTypeImage;
            private TextView notificationDate;
        }
    }



}