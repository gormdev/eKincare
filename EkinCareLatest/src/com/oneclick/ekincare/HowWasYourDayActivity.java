package com.oneclick.ekincare;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
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
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.oneclick.ekincare.helper.CustomeDialog;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.ProfileData;
import com.oneclick.ekincare.vo.SyncModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by RaviTejaN on 10-08-2016.
 */
public class HowWasYourDayActivity extends AppCompatActivity {
    LinearLayout feelHappyView,feelSadView,feelSickView,feelStressView,feelAngryView;
    ImageView happyImage,sadImage,sickImage,stressImage,angryImage;
    ImageView wineNo,wineYes,smokeno,smokeYes,breakfastNo,breakfastYes,sleepNo,sleepYes,mealsNo,mealsYes;
    LinearLayout linearLayoutAlcohol,linearLayoutSmoke;
    Animation zoomin;
    Toolbar toolbar;
    private PreferenceHelper prefs;
    FloatingActionButton howWasDone;
    private Dialog mAlert_Dialog;
    CircleProgressBar progressWithArrow;
    private Switch howWasDayReminder;
    TextView happyText,sadText,sickText,stressText,angryText;
    DatabaseOverAllHandler dbHandler;

    private Customer mCustomer;
    private CustomerManager customerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_was_day_activity);
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.cancel(0);
        notificationmanager.cancelAll();
        prefs = new PreferenceHelper(this);
        dbHandler = new DatabaseOverAllHandler(this);
        zoomin = AnimationUtils.loadAnimation(this, R.anim.image_zoom_in);


        customerManager = CustomerManager.getInstance(this);
        mCustomer = customerManager.getCurrentCustomer();

        initViews();

        feelHappyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.sethowWasDay("Happy");
                happyText.setVisibility(View.INVISIBLE);
                sadText.setVisibility(View.VISIBLE);
                sickText.setVisibility(View.VISIBLE);
                stressText.setVisibility(View.VISIBLE);
                angryText.setVisibility(View.VISIBLE);
                happyImage.startAnimation(zoomin);
                angryImage.clearAnimation();
                stressImage.clearAnimation();
                sickImage.clearAnimation();
                sadImage.clearAnimation();
            }
        });
        feelSadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.sethowWasDay("Sad");
                sadImage.startAnimation(zoomin);
                happyText.setVisibility(View.VISIBLE);
                sadText.setVisibility(View.INVISIBLE);
                sickText.setVisibility(View.VISIBLE);
                stressText.setVisibility(View.VISIBLE);
                angryText.setVisibility(View.VISIBLE);
                angryImage.clearAnimation();
                stressImage.clearAnimation();
                happyImage.clearAnimation();
                sickImage.clearAnimation();

            }
        });
        feelSickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.sethowWasDay("Sick");
                sickImage.startAnimation(zoomin);
                happyText.setVisibility(View.VISIBLE);
                sadText.setVisibility(View.VISIBLE);
                sickText.setVisibility(View.INVISIBLE);
                stressText.setVisibility(View.VISIBLE);
                angryText.setVisibility(View.VISIBLE);
                angryImage.clearAnimation();
                stressImage.clearAnimation();
                happyImage.clearAnimation();
                sadImage.clearAnimation();

            }
        });
        feelStressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.sethowWasDay("Stress");
                stressImage.startAnimation(zoomin);
                happyText.setVisibility(View.VISIBLE);
                sadText.setVisibility(View.VISIBLE);
                sickText.setVisibility(View.VISIBLE);
                stressText.setVisibility(View.INVISIBLE);
                angryText.setVisibility(View.VISIBLE);
                angryImage.clearAnimation();
                happyImage.clearAnimation();
                sickImage.clearAnimation();
                sadImage.clearAnimation();

            }
        });
        feelAngryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.sethowWasDay("Angry");
                angryImage.startAnimation(zoomin);
                happyText.setVisibility(View.VISIBLE);
                sadText.setVisibility(View.VISIBLE);
                sickText.setVisibility(View.VISIBLE);
                stressText.setVisibility(View.VISIBLE);
                angryText.setVisibility(View.INVISIBLE);
                stressImage.clearAnimation();
                happyImage.clearAnimation();
                sickImage.clearAnimation();
                sadImage.clearAnimation();

            }
        });


        //Yes or No

        wineYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.setDrinkedAlcoholToday(true);
                wineNo.setBackgroundResource(R.drawable.feel_no_before);
                wineYes.setBackgroundResource(R.drawable.feel_yes_selected);
            }
        });

        wineNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.setDrinkedAlcoholToday(false);
                wineNo.setBackgroundResource(R.drawable.feel_no_selected);
                wineYes.setBackgroundResource(R.drawable.feel_yes_before);
            }
        });


        smokeYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.setSmokedToday(true);
                smokeno.setBackgroundResource(R.drawable.feel_no_before);
                smokeYes.setBackgroundResource(R.drawable.feel_yes_selected);
            }
        });

        smokeno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.setSmokedToday(false);
                smokeno.setBackgroundResource(R.drawable.feel_no_selected);
                smokeYes.setBackgroundResource(R.drawable.feel_yes_before);
            }
        });

        breakfastYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.sethadbreakfast(true);
                breakfastNo.setBackgroundResource(R.drawable.feel_no_before);
                breakfastYes.setBackgroundResource(R.drawable.feel_yes_selected);
            }
        });

        breakfastNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.sethadbreakfast(false);
                breakfastNo.setBackgroundResource(R.drawable.feel_no_selected);
                breakfastYes.setBackgroundResource(R.drawable.feel_yes_before);
            }
        });


        mealsYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.setskippedMeals(true);
                mealsNo.setBackgroundResource(R.drawable.feel_no_before);
                mealsYes.setBackgroundResource(R.drawable.feel_yes_selected);
            }
        });

        mealsNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.setskippedMeals(false);
                mealsNo.setBackgroundResource(R.drawable.feel_no_selected);
                mealsYes.setBackgroundResource(R.drawable.feel_yes_before);
            }
        });

        sleepYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.sethadGoodSleep(true);
                sleepNo.setBackgroundResource(R.drawable.feel_no_before);
                sleepYes.setBackgroundResource(R.drawable.feel_yes_selected);
            }
        });

        sleepNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.sethadGoodSleep(false);
                sleepNo.setBackgroundResource(R.drawable.feel_no_selected);
                sleepYes.setBackgroundResource(R.drawable.feel_yes_before);
            }
        });

        if(prefs.gethowWasDayReminder()){
            howWasDayReminder.setChecked(true);
        }else if(prefs.gethowWasDayReminder()){
            howWasDayReminder.setChecked(false);
        }


        howWasDayReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    prefs.sethowWasDayReminder(true);
                } else {
                    prefs.sethowWasDayReminder(false);
                }

            }
        });


        howWasDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prefs.gethowWasDay().equalsIgnoreCase("")){
                    Toast.makeText(HowWasYourDayActivity.this, "Choose today feeling", Toast.LENGTH_SHORT).show();
                }else {
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c.getTime());
                    JSONObject json = new JSONObject();
                    JSONArray req = new JSONArray();
                    try {
                        JSONObject object = new JSONObject();

                        object.put("date", formattedDate);
                        object.put("consumed_alcohol", prefs.getDrinkedAlcoholToday());
                        object.put("smoked_today", prefs.getSmokedToday());
                        object.put("ate_on_time", prefs.getskippedMeals());
                        object.put("had_breakfast", prefs.gethadbreakfast());
                        object.put("good_sleep", prefs.gethadGoodSleep());
                        object.put("mood", prefs.gethowWasDay());

                        req.put(object);

                        json.put("steps", req);

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }catch (IndexOutOfBoundsException e){

                        e.printStackTrace();

                    }
                    if (NetworkCaller.isInternetOncheck(HowWasYourDayActivity.this)) {
                        howsWasDayPost(json);
                    }else {
                        //CustomeDialog.dispDialog(HowWasYourDayActivity.this, "Internet not available");
                        SyncModel syncModel = new SyncModel();
                        syncModel.setURL(TagValues.GOOGLE_FIT_HISTORY);
                        syncModel.setJSON(json.toString());
                        syncModel.setMETHOD("POST");
                        dbHandler.setSyncData(syncModel);

                        prefs.sethowWasDayDone("DayDone");
                        prefs.setHasHowWasYourDayDataFilled(true);
                        Intent intent = new Intent(HowWasYourDayActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }





            }
        });


    }

    private void howsWasDayPost(JSONObject json) {
        System.out.println("test steps======"+json.toString());
            String URL= TagValues.GOOGLE_FIT_HISTORY;
            showPDialog();
            JsonObjectRequest jsonPostGoogleFit = new JsonObjectRequest(Request.Method.POST,URL,json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if(response!=null){
                                System.out.println("steps verify ===="+response.toString());
                                hidePDialog();
                                howWasDayResposne(response);
                            }else{
                                CustomeDialog.dispDialog(HowWasYourDayActivity.this, TagValues.DATA_NOT_FOUND);
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
                    params.put("X-DEVICE-ID", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                    params.put("Content-type", "application/json");
                    params.put("Accept", "application/json");

                    return params;
                }
            };
            VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonPostGoogleFit);

        }

    private void howWasDayResposne(JSONObject response) {
        prefs.sethowWasDayDone("DayDone");
        prefs.setHasHowWasYourDayDataFilled(true);
        Intent intent = new Intent(HowWasYourDayActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void hidePDialog() {
        if (mAlert_Dialog != null) {
            mAlert_Dialog.dismiss();
            mAlert_Dialog=null;
        }
    }
    private void showPDialog() {
        mAlert_Dialog = new Dialog(this);
        mAlert_Dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mAlert_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAlert_Dialog.setContentView(R.layout.materialprogressbar);
        mAlert_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //mAlert_Dialog.setCancelable(false);
        mAlert_Dialog.setCanceledOnTouchOutside(true);
        progressWithArrow = (CircleProgressBar)mAlert_Dialog.findViewById(R.id.progressWithArrow);
        progressWithArrow.setColorSchemeResources(android.R.color.holo_blue_light);
        mAlert_Dialog.show();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpToolBar();
        toolbar.setTitle("How was your day ?");

        linearLayoutAlcohol = (LinearLayout) findViewById(R.id.layout_alcohol);
        linearLayoutSmoke = (LinearLayout) findViewById(R.id.layout_smoke);

        happyText=(TextView)findViewById(R.id.happy_text);
        sadText=(TextView)findViewById(R.id.sad_text);
        sickText=(TextView)findViewById(R.id.sick_text);
        stressText=(TextView)findViewById(R.id.stress_text);
        angryText=(TextView)findViewById(R.id.angry_text);
        howWasDayReminder=(Switch)findViewById(R.id.how_was_day_notification);
        howWasDone=(FloatingActionButton)findViewById(R.id.how_was_done);
        feelHappyView=(LinearLayout)findViewById(R.id.feel_happy_view) ;
        feelSadView=(LinearLayout)findViewById(R.id.feel_sad_view) ;
        feelSickView=(LinearLayout)findViewById(R.id.feel_sick_view) ;
        feelStressView=(LinearLayout)findViewById(R.id.feel_stress_view) ;
        feelAngryView=(LinearLayout)findViewById(R.id.feel_angry_view) ;

        wineNo=(ImageView)findViewById(R.id.feel_wine_no);
        wineYes=(ImageView)findViewById(R.id.feel_wine_yes);
        smokeno=(ImageView)findViewById(R.id.feel_smoke_no);
        smokeYes=(ImageView)findViewById(R.id.feel_smoke_yes);
        breakfastNo=(ImageView)findViewById(R.id.feel_breakfast_no);
        breakfastYes=(ImageView)findViewById(R.id.feel_breakfast_yes);
        sleepNo=(ImageView)findViewById(R.id.feel_less_sleep_no);
        sleepYes=(ImageView)findViewById(R.id.feel_less_sleep_yes);
        mealsNo=(ImageView)findViewById(R.id.feel_skipmeal_no);
        mealsYes=(ImageView)findViewById(R.id.feel_skipmeal_yes);

        happyImage=(ImageView)findViewById(R.id.happy_image);
        sadImage=(ImageView)findViewById(R.id.sad_image);
        sickImage=(ImageView)findViewById(R.id.sick_image);
        stressImage=(ImageView)findViewById(R.id.stress_image);
        angryImage=(ImageView)findViewById(R.id.angry_image);


        wineNo.setBackgroundResource(R.drawable.feel_no_before);
        wineYes.setBackgroundResource(R.drawable.feel_yes_before);

        smokeno.setBackgroundResource(R.drawable.feel_no_before);
        smokeYes.setBackgroundResource(R.drawable.feel_yes_before);

        breakfastNo.setBackgroundResource(R.drawable.feel_no_before);
        breakfastYes.setBackgroundResource(R.drawable.feel_yes_before);

        sleepNo.setBackgroundResource(R.drawable.feel_no_before);
        sleepYes.setBackgroundResource(R.drawable.feel_yes_before);

        mealsNo.setBackgroundResource(R.drawable.feel_no_before);
        mealsYes.setBackgroundResource(R.drawable.feel_yes_before);

        if(mCustomer.getSmoke().equalsIgnoreCase("never")){
            linearLayoutSmoke.setVisibility(View.GONE);
        }else{
            linearLayoutSmoke.setVisibility(View.VISIBLE);
        }
        if(mCustomer.getAlcohol().equalsIgnoreCase("never")){
            linearLayoutAlcohol.setVisibility(View.GONE);
        }else{
            linearLayoutAlcohol.setVisibility(View.VISIBLE);
        }


    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().setTitle(R.string.title_medical_history);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HowWasYourDayActivity.this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                HowWasYourDayActivity.this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
                finish();
            }
        });
    }
}
