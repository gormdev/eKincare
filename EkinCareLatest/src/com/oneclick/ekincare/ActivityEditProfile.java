package com.oneclick.ekincare;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.ekincare.app.ProfileManager;
import com.ekincare.app.VolleyRequestSingleton;
import com.ekincare.util.DateUtility;
import com.ekincare.util.ValidationClass;
import com.google.gson.Gson;
import com.oneclick.ekincare.helper.CustomeDialog;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.FamilyAddresses;
import com.oneclick.ekincare.vo.ProfileData;
import com.oneclick.ekincare.vo.Register;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by Ajay on 20-09-2016.
 */
public class ActivityEditProfile extends AppCompatActivity
{
    private CardView cardBio,cardLifeStyle,cardContact;
    private View contactView,bioView,lifeStyleView;
    private MaterialSpinner spinnerDailyActive,spinnerExercise,spinnerSmoke,spinnerAlcohol,spinnerDiet;
    private MaterialSpinner spinnerBloodGroup,spinnerGender;

    private AppCompatEditText editTextFirstname,editTextLastName,editTextDOB,editTextHeightFeet,editTextHeightInch,editTextWeight;
    private AppCompatEditText editTextEmail,editTextMobile,editTextAddressLine1,editTextAddressLine2,editTextCity;
    private AppCompatEditText editTextState,editTextPinCode;
    private Toolbar toolbar;

    private TextInputLayout floatingFirstName,floatingLastName,floatingHeightInFeet,floatingHeightInInch;
    private TextInputLayout floatingWeight,floatingEmail,floatingMobile,floatingAdd1,floatingAdd2,floatingCity;
    private TextInputLayout floatingState,floatingPinCode;

    ArrayList<String> genderList = new ArrayList<String>();
    ArrayList<String> listBloodGroup = new ArrayList<String>();
    ArrayList<String> listDiet = new ArrayList<String>();
    ArrayList<String> listAlcohol = new ArrayList<String>();
    ArrayList<String> listSmoke = new ArrayList<String>();
    ArrayList<String> listExercise = new ArrayList<String>();
    ArrayList<String> listDailyActive = new ArrayList<String>();
    private Customer mCustomer;
    private CustomerManager customerManager;
    private PreferenceHelper prefs;
    private ProfileData mProfileData;

    String customerFirstName,customerLastName,customerWeight,customerHeightInFeet,customerHeightInInch,customerDOB="";
    String customerAddressLine1,customerAddressLine2,customerState,customerPinCode,customerCity="";
    String customerGender,customerDailyActivity,customerSmoke,customerAlcohol,customerExercise,customerDiet;
    String customerMobile,customerEmail,customerBloodGroup = "";

    Dialog mAlert_Dialog;
    private String strFamilyMemberKey = "";


    private List<FamilyAddresses> FamilyAddressList;

    private ProfileManager mProfileManager;
    private TextInputLayout floatingEdittextDob;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.color_primary_family_member));
        }

        setContentView(R.layout.activity_edit_profile);

        mProfileManager = ProfileManager.getInstance(ActivityEditProfile.this);
        customerManager = CustomerManager.getInstance(this.getApplicationContext());
        mCustomer = customerManager.getCurrentFamilyMember();
        prefs = new PreferenceHelper(this);
        mProfileData = new ProfileData();

        try{
            FamilyAddressList = customerManager.getCurrentFamilyMember().getAddresses();
            System.out.println("ActivityEditProfile.onCreate FamilyAddressList="+FamilyAddressList.toString());
        }catch (NullPointerException e){
            e.printStackTrace();
            Intent intent = new Intent(ActivityEditProfile.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        }

        setInitialData();
        
        setupList();

        initializeView();

        setupToolbar();

        setupBioCard();

        setupLifeStyleCard();

        setupContactCard();

        if(customerManager.getCurrentFamilyMember().isChild())
        {
            cardBio.setVisibility(View.VISIBLE);
            cardLifeStyle.setVisibility(View.GONE);
            cardContact.setVisibility(View.GONE);
        }else{
            cardBio.setVisibility(View.VISIBLE);
            cardLifeStyle.setVisibility(View.VISIBLE);
            cardContact.setVisibility(View.VISIBLE);
        }

    }


    private void setInitialData()
    {
        if (mCustomer != null)
        {
            customerGender = (mCustomer.getGender());
            customerFirstName =mCustomer.getFirst_name();
            customerLastName = mCustomer.getLast_name();
            customerDOB = DateUtility.getConvertDob(mCustomer.getDate_of_birth());

            try{
                if(FamilyAddressList.isEmpty()){
                    customerAddressLine1 = customerAddressLine2 =("-");
                    customerCity =("-");
                    customerState =("-");
                    customerPinCode =("-");
                }else if(FamilyAddressList.size()==1){
                    customerAddressLine1 =(FamilyAddressList.get(0).getLine1());
                    customerAddressLine2 =(FamilyAddressList.get(0).getLine2());
                    customerCity =(FamilyAddressList.get(0).getCity());
                    customerState =(FamilyAddressList.get(0).getState());
                    customerPinCode =(FamilyAddressList.get(0).getZip_code());
                }else if(FamilyAddressList.size()>1){
                    customerAddressLine1 =(FamilyAddressList.get(FamilyAddressList.size() - 1).getLine1());
                    customerAddressLine2 =(FamilyAddressList.get(FamilyAddressList.size() - 1).getLine2());
                    customerCity =(FamilyAddressList.get(FamilyAddressList.size() - 1).getCity());
                    customerState =(FamilyAddressList.get(FamilyAddressList.size() - 1).getState());
                    customerPinCode =(FamilyAddressList.get(FamilyAddressList.size() - 1).getZip_code());
                }
            }catch (NullPointerException e){

            }


            if (mCustomer.getCustomer_vitals() != null)
            {
                if ((mCustomer.getCustomer_vitals().getFeet() != null && !mCustomer.getCustomer_vitals().getFeet().equalsIgnoreCase(""))
                        || (mCustomer.getCustomer_vitals().getInches() != null && !mCustomer.getCustomer_vitals().getInches().equalsIgnoreCase("")))
                {

                    customerHeightInFeet = mCustomer.getCustomer_vitals().getFeet();
                    customerHeightInInch = mCustomer.getCustomer_vitals().getInches();
                }

                if (mCustomer.getCustomer_vitals().getWeight() != null && !mCustomer.getCustomer_vitals().getWeight().equalsIgnoreCase(""))
                {
                    customerWeight = mCustomer.getCustomer_vitals().getWeight();
                }

                if (mCustomer.getDaily_activity() == null) {
                    customerDailyActivity = "Daily Activity";
                } else {
                    customerDailyActivity = mCustomer.getDaily_activity();
                }

                if (mCustomer.getDiet() == null) {
                    customerDiet = "Diet";
                } else {
                    customerDiet = mCustomer.getDiet();
                }

                if (mCustomer.getAlcohol() == null) {
                    customerAlcohol = "Alcohol";
                } else {
                    customerAlcohol = mCustomer.getAlcohol();
                }
                if (mCustomer.getSmoke() == null) {
                    customerSmoke = "Smoke";
                } else {
                    customerSmoke = mCustomer.getSmoke();
                }
                if (mCustomer.getFrequency_of_exercise() == null) {
                    customerExercise = "Exercise";
                } else {
                    customerExercise = mCustomer.getFrequency_of_exercise();
                }
                if (mCustomer.getMobile_number() == null || mCustomer.getMobile_number().equalsIgnoreCase(" ") || mCustomer.getMobile_number().isEmpty()) {
                    customerMobile = "";
                } else {
                    customerMobile = mCustomer.getMobile_number();
                }
                if (mCustomer.getEmail() == null || mCustomer.getEmail().equalsIgnoreCase(" ") || mCustomer.getEmail().isEmpty()) {
                    customerEmail = "";
                } else {
                    customerEmail = mCustomer.getEmail();
                }

                if ((mCustomer.getCustomer_vitals() != null && mCustomer.getCustomer_vitals().getBlood_group_id() != null
                        && !mCustomer.getCustomer_vitals().getBlood_group_id().toString().equalsIgnoreCase("")))
                {
                    int bgId = 0;
                    try {
                        bgId = Integer.parseInt(mCustomer.getCustomer_vitals().getBlood_group_id());
                    } catch (NumberFormatException e) {
                    }

                    if (bgId == 1) {
                        customerBloodGroup = "A +";
                    } else if (bgId == 2) {
                        customerBloodGroup = "A -";
                    } else if (bgId == 3) {
                        customerBloodGroup = "B +";
                    } else if (bgId == 4) {
                        customerBloodGroup = "B -";
                    } else if (bgId == 5) {
                        customerBloodGroup = "AB +";
                    } else if (bgId == 6) {
                        customerBloodGroup = "AB -";
                    } else if (bgId == 7) {
                        customerBloodGroup = "O +";
                    } else if (bgId == 8) {
                        customerBloodGroup = "O -";
                    }
                }

            } else {
               finish();
            }

        }
    }

    private void setupList() {
        genderList.add("Male");
        genderList.add("Female");

        listBloodGroup.add("O +");
        listBloodGroup.add("O -");
        listBloodGroup.add("A +");
        listBloodGroup.add("A -");
        listBloodGroup.add("B +");
        listBloodGroup.add("B -");
        listBloodGroup.add("AB +");
        listBloodGroup.add("AB -");

        listDiet.add("Veg");
        listDiet.add("Non Veg");

        listAlcohol.add(getResources().getString(R.string.often));
        listAlcohol.add(getResources().getString(R.string.occasional));
        listAlcohol.add(getResources().getString(R.string.never));

        listExercise.add(getResources().getString(R.string.often));
        listExercise.add(getResources().getString(R.string.occasional));
        listExercise.add(getResources().getString(R.string.never));

        listSmoke.add(getResources().getString(R.string.often));
        listSmoke.add(getResources().getString(R.string.occasional));
        listSmoke.add(getResources().getString(R.string.never));


        listDailyActive.add("Active");
        listDailyActive.add("Not Active");
    }

    private void initializeView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        cardBio = (CardView) findViewById(R.id.card_view_bio);
        cardLifeStyle = (CardView) findViewById(R.id.card_view_lifestyle);
        cardContact = (CardView) findViewById(R.id.card_view_contact);

        bioView = View.inflate(this, R.layout.content_edit_profile_card_view_bio, null);
        contactView = View.inflate(this, R.layout.content_edit_profile_card_view_contact, null);
        lifeStyleView = View.inflate(this, R.layout.content_edit_profile_card_view_lifestyle, null);

        spinnerDailyActive = (MaterialSpinner) lifeStyleView.findViewById(R.id.spinner_daily_active);
        spinnerExercise = (MaterialSpinner ) lifeStyleView.findViewById(R.id.spinner_exercise);
        spinnerSmoke = (MaterialSpinner ) lifeStyleView.findViewById(R.id.spinner_smoking);
        spinnerAlcohol = (MaterialSpinner ) lifeStyleView.findViewById(R.id.spinner_alcohol);
        spinnerDiet = (MaterialSpinner ) lifeStyleView.findViewById(R.id.spinner_diet);

        spinnerBloodGroup = (MaterialSpinner ) bioView.findViewById(R.id.spinner_blood_group);
        spinnerGender = (MaterialSpinner) bioView.findViewById(R.id.spinner_gender);

        editTextFirstname = (AppCompatEditText) bioView.findViewById(R.id.edit_text_first_name);
        editTextLastName = (AppCompatEditText) bioView.findViewById(R.id.edit_text_last_name);
        editTextDOB = (AppCompatEditText) bioView.findViewById(R.id.edit_text_dob);
        floatingEdittextDob = (TextInputLayout)bioView.findViewById(R.id.floating_dob);
        editTextHeightFeet = (AppCompatEditText) bioView.findViewById(R.id.edit_text_height_feet);
        editTextHeightInch = (AppCompatEditText) bioView.findViewById(R.id.edit_text_height_inches);
        editTextWeight= (AppCompatEditText) bioView.findViewById(R.id.edit_text_weight);

        floatingFirstName = (TextInputLayout) bioView.findViewById(R.id.floating_first_name);
        floatingLastName = (TextInputLayout) bioView.findViewById(R.id.floating_last_name);
        floatingHeightInFeet = (TextInputLayout) bioView.findViewById(R.id.floating_height_feet);
        floatingHeightInInch = (TextInputLayout) bioView.findViewById(R.id.floating_height_inches);
        floatingWeight = (TextInputLayout) bioView.findViewById(R.id.floating_weight);

        editTextAddressLine1 = (AppCompatEditText) contactView.findViewById(R.id.edit_text_address_line1);
        editTextAddressLine2 = (AppCompatEditText) contactView.findViewById(R.id.edit_text_address_line2);
        editTextCity = (AppCompatEditText) contactView.findViewById(R.id.edit_text_city);
        editTextState = (AppCompatEditText) contactView.findViewById(R.id.edit_text_state);
        editTextPinCode = (AppCompatEditText) contactView.findViewById(R.id.edit_text_pin);
        editTextEmail = (AppCompatEditText) contactView.findViewById(R.id.edit_text_email);
        editTextMobile = (AppCompatEditText) contactView.findViewById(R.id.edit_text_mobile);

        floatingAdd1 = (TextInputLayout) contactView.findViewById(R.id.floating_address_line1);
        floatingAdd2 = (TextInputLayout) contactView.findViewById(R.id.floating_address_line2);
        floatingCity = (TextInputLayout) contactView.findViewById(R.id.floating_city);
        floatingState = (TextInputLayout) contactView.findViewById(R.id.floating_state);
        floatingPinCode = (TextInputLayout) contactView.findViewById(R.id.floating_pin);
        floatingEmail = (TextInputLayout) contactView.findViewById(R.id.floating_email);
        floatingMobile = (TextInputLayout) contactView.findViewById(R.id.floating_mobile);
    }

    private void setupToolbar() {
        try{
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            toolbar.setTitle("Edit Profile");

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityEditProfile.this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                    ActivityEditProfile.this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
                    finish();
                }
            });

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void setupBioCard()
    {
        cardBio.addView(bioView);

        setupSpinnerGender();

        setupSpinnerBloodGroup();

        editTextFirstname.setText(customerFirstName);
        editTextLastName.setText(customerLastName);
        editTextDOB.setText(customerDOB);
        editTextWeight.setText(customerWeight);
        editTextHeightFeet.setText(customerHeightInFeet);
        editTextHeightInch.setText(customerHeightInInch);

        editTextDOB.setClickable(false);
        editTextDOB.setEnabled(false);
        editTextDOB.setFocusable(false);

        floatingEdittextDob.setFocusable(false);
        floatingEdittextDob.setEnabled(false);

        /*editTextDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(editTextDOB);
            }
        });

        editTextDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    showDatePicker(editTextDOB);
                } else {
                    // Hide your calender here
                }
            }
        });*/

        setError(editTextFirstname,floatingFirstName,"Ex. John");
        editTextFirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setError(editTextFirstname,floatingFirstName,"Ex. John");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        setError(editTextLastName,floatingLastName,"Ex. Doe");
        editTextLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setError(editTextLastName,floatingLastName,"Ex. Doe");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        setError(editTextHeightFeet,floatingHeightInFeet,"Ex. 5 Feet");
        editTextHeightFeet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setError(editTextHeightFeet,floatingHeightInFeet,"Ex. 5 Feet");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        setError(editTextHeightInch,floatingHeightInInch,"Ex. 5 Inch");
        editTextHeightInch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setError(editTextHeightInch,floatingHeightInInch,"Ex. 5 Inch");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        setError(editTextWeight,floatingWeight,"Ex. 60kg");
        editTextWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setError(editTextWeight,floatingWeight,"Ex. 60kg");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void showDatePicker(final EditText editText)
    {
        final Dialog dialog = new Dialog(ActivityEditProfile.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.dialog_date_picker_view, null, false);
        dialog.setContentView(dialogLayout);

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.dialog_datepicker);
        final TextView dateTextView = (TextView) dialog.findViewById(R.id.dialog_dateview);
        //final TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);

        final SimpleDateFormat dateViewFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        // View settings
        //dialogTitle.setText("Select Date of Birth...");
        dateTextView.setText(editText.getText().toString());

        int year = 0;
        int month = 0;
        int day = 0;
        Calendar choosenDate = Calendar.getInstance();
        Date dateDOB = null;

        try {
            dateDOB = dateViewFormatter.parse(editText.getText().toString());
            choosenDate.setTime(dateDOB);
            year = choosenDate.get(Calendar.YEAR);
            month = choosenDate.get(Calendar.MONTH);
            day = choosenDate.get(Calendar.DAY_OF_MONTH);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(dateDOB);

        // Initialize datepicker in dialog atepicker
        datePicker.init(year,month,day, new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar choosenDate = Calendar.getInstance();
                choosenDate.set(year, monthOfYear, dayOfMonth);
                dateTextView.setText(dateViewFormatter.format(choosenDate.getTime()));
            }
        });

        // Buttons
        TextView buttonCancel = (TextView) dialog.findViewById(R.id.dialog_close);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView buttonDone = (TextView) dialog.findViewById(R.id.dialog_choose);
        buttonDone.setText("Done");
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar choosen = Calendar.getInstance();
                choosen.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                editText.setText(dateViewFormatter.format(choosen.getTime()));
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void setupSpinnerBloodGroup() {
        ArrayAdapter<String> dataAdapterspinnerBloodGroup = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listBloodGroup);
        dataAdapterspinnerBloodGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodGroup.setAdapter(dataAdapterspinnerBloodGroup);

        try{
            spinnerBloodGroup.setSelection(getIndex(spinnerBloodGroup, customerBloodGroup)+1);

        }catch (IndexOutOfBoundsException e){

        }

        spinnerBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                customerBloodGroup = parentView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void setupSpinnerGender()
    {
        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genderList);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterGender);

        try{
            spinnerGender.setSelection(getIndex(spinnerGender, customerGender)+1);

        }catch (IndexOutOfBoundsException e){}

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                customerGender = parentView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private int getIndex(Spinner spinner, String myString){
        int index = -1;
        try{
            for (int i=0;i<spinner.getCount();i++)
            {
                System.out.println("ActivityEditProfile.getIndex i="+i + " spinner.getItemAtPosition(i)="+spinner.getItemAtPosition(i));
                String gender = (String) spinner.getItemAtPosition(i);
                if (gender.equalsIgnoreCase(myString)){
                    System.out.println("ActivityEditProfile.getIndex index= " + index + " i=" + i + " myString =" + myString);
                    index = i;
                    break;
                }
            }
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return index;
    }

    private void setupContactCard() {
        cardContact.addView(contactView);

        editTextMobile.setText(customerMobile);
        editTextEmail.setText(customerEmail);

        editTextAddressLine1.setText(customerAddressLine1);
        editTextAddressLine2.setText(customerAddressLine2);
        editTextCity.setText(customerCity);
        editTextState.setText(customerState);
        editTextPinCode.setText(customerPinCode);



        setError(editTextMobile,floatingMobile,"Ex. 4532343452");

        editTextMobile.setEnabled(false);
        //floatingMobile.setEnabled(false);
        //floatingMobile.setFocusable(false);

        editTextMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setError(editTextMobile,floatingMobile,"Ex. 4532343452");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        setError(editTextEmail,floatingEmail,"Ex. john@gmail.com");
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setError(editTextEmail,floatingEmail,"Ex. john@gmail.com");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        setError(editTextAddressLine1,floatingAdd1,"Ex. Flat.201,Silicon Villa");
        editTextAddressLine1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setError(editTextAddressLine1,floatingAdd1,"Ex. Flat.201,Silicon Villa");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        setError(editTextAddressLine2,floatingAdd2,"Optional");
        editTextAddressLine2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setError(editTextAddressLine2,floatingAdd2,"Optional");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        setError(editTextCity,floatingCity,"Ex. Bengaluru");
        editTextCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setError(editTextCity,floatingCity,"Ex. Bengaluru");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        setError(editTextState,floatingState,"Ex. Karnataka");
        editTextState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setError(editTextState,floatingState,"Ex. Karnataka");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        setError(editTextPinCode,floatingPinCode,"Ex. 231432");
        editTextPinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setError(editTextPinCode,floatingPinCode,"Ex. 231432");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setError(EditText editText,TextInputLayout textInputLayout,String error){
        System.out.println("ActivityEditProfile.setError textInputLayout="+textInputLayout);
        if(editText.getText().toString().isEmpty()){
            textInputLayout.setError(error);
        }else{
            ValidationClass.HasSomeText(editText,textInputLayout);
        }
    }

    private void setupLifeStyleCard() {
        cardLifeStyle.addView(lifeStyleView);

        setupSpinnerSmoke();

        setupSpinnerAlcohol();

        setupSpinnerDiet();

        setupSpinnerDailyActive();

        setupSpinnerExercise();

    }

    private void setupSpinnerDailyActive()
    {
        ArrayAdapter<String> adapterDailyActive = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listDailyActive);
        adapterDailyActive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDailyActive.setAdapter(adapterDailyActive);

        try{
            spinnerDailyActive.setSelection(getIndex(spinnerDailyActive, customerDailyActivity)+1);

        }catch (IndexOutOfBoundsException e){

        }

        spinnerDailyActive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                customerDailyActivity = parentView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void setupSpinnerExercise() {
        ArrayAdapter<String> adapterExercise = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listExercise);
        adapterExercise.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExercise.setAdapter(adapterExercise);

        System.out.println("ActivityEditProfile.setupSpinnerExercise spinnerExercise="+customerExercise);
        try{
            spinnerExercise.setSelection(getIndex(spinnerExercise, customerExercise)+1);

        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

        spinnerExercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                customerExercise = parentView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void setupSpinnerSmoke() {
        ArrayAdapter<String> adapterSmoke = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSmoke);
        adapterSmoke.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSmoke.setAdapter(adapterSmoke);

        System.out.println("ActivityEditProfile.setupSpinnerSmoke spinnerSmoke="+customerSmoke);
        try{
            spinnerSmoke.setSelection(getIndex(spinnerSmoke, customerSmoke)+1);

        }catch (IndexOutOfBoundsException e){

        }

        spinnerSmoke.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                customerSmoke = parentView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void setupSpinnerAlcohol() {
        ArrayAdapter<String> adapterAlcohol = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAlcohol);
        adapterAlcohol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlcohol.setAdapter(adapterAlcohol);

        System.out.println("ActivityEditProfile.setupSpinnerAlcohol customerAlcohol="+customerAlcohol);
        try{
            spinnerAlcohol.setSelection(getIndex(spinnerAlcohol, customerAlcohol)+1);

        }catch (IndexOutOfBoundsException e){

        }

        spinnerAlcohol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                customerAlcohol = parentView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void setupSpinnerDiet() {
        ArrayAdapter<String> adapterDiet = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listDiet);
        adapterDiet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiet.setAdapter(adapterDiet);

        try{
            spinnerDiet.setSelection(getIndex(spinnerDiet, customerDiet)+1);

        }catch (IndexOutOfBoundsException e){

        }

        spinnerDiet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                customerDiet = parentView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done:
                if (NetworkCaller.isInternetOncheck(ActivityEditProfile.this)) {
                    if(customerGender.equalsIgnoreCase("Gender"))
                    {
                        Toast.makeText(ActivityEditProfile.this,"Please select gender.",Toast.LENGTH_LONG).show();
                    }else if(customerBloodGroup.equalsIgnoreCase("Blood Group"))
                    {
                        Toast.makeText(ActivityEditProfile.this,"Please select blood group",Toast.LENGTH_LONG).show();
                    }
                    else if(checkValidation()){
                        updateProfile();
                    }else{
                        Toast.makeText(ActivityEditProfile.this,"Invalid data. ",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(ActivityEditProfile.this,getResources().getString(R.string.networkloss),Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return true;
    }

    private void updateProfile() {
        showPDialog();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,TagValues.EDIT_PROFILE_URL,createJson(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        hidePDialog();

                        if(response!=null)
                        {
                            System.out.println("ActivityEditProfile.onResponse response="+response);
                            Toast.makeText(ActivityEditProfile.this,"Profile updated successfully.",Toast.LENGTH_LONG).show();

                            if(mCustomer.getCustomer_id().equalsIgnoreCase(customerManager.getCurrentFamilyMember().getCustomer_id())){
                                getCustomerDetailRequest();
                            }
                        }else{
                            Toast.makeText(ActivityEditProfile.this,"Something went wrong. Try again later.",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ActivityEditProfile.onErrorResponse =" + error.toString());
                        Toast.makeText(ActivityEditProfile.this,"Something went wrong. Try again later.",Toast.LENGTH_LONG).show();
                        hidePDialog();
                    }
                }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                Set<String> keySet = headers.keySet();
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                if (customerManager.isLoggedInCustomer()) {
                    strFamilyMemberKey = "";
                } else {
                    strFamilyMemberKey = customerManager.getCurrentFamilyMember().getIdentification_token();
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-HTTP-Method-Override","PATCH");
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(ActivityEditProfile.this));
                if (!strFamilyMemberKey.equalsIgnoreCase(""))
                    params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private JSONObject createJson(){
        JSONObject jsonObject = new JSONObject();

        JSONObject jsonObjectCustomer = new JSONObject();
        JSONObject jsonObjectCustomerAddress = new JSONObject();
        JSONObject jsonObjectCustomerVitals = new JSONObject();

        try {
            jsonObjectCustomer.put("first_name",editTextFirstname.getText().toString());
            jsonObjectCustomer.put("last_name",editTextLastName.getText().toString());
            jsonObjectCustomer.put("email",editTextEmail.getText().toString());
            jsonObjectCustomer.put("date_of_birth",editTextDOB.getText().toString());
            jsonObjectCustomer.put("daily_activity",customerDailyActivity);
            jsonObjectCustomer.put("frequency_of_exercise",customerExercise);
            jsonObjectCustomer.put("gender",customerGender);
            //jsonObjectCustomer.put("martial_status",);
            //jsonObjectCustomer.put("language_spoken",);
            jsonObjectCustomer.put("smoke",customerSmoke);
            jsonObjectCustomer.put("alcohol",customerAlcohol);
            //jsonObjectCustomer.put("medical_insurance",);
            //jsonObjectCustomer.put("customer_type",);
            jsonObjectCustomer.put("diet",customerDiet);
            //jsonObjectCustomer.put("religious_affiliation",);
            jsonObjectCustomer.put("mobile_number",editTextMobile.getText().toString());
            //jsonObjectCustomer.put("alternative_mobile_number",);
            //jsonObjectCustomer.put("number_of_children",);
            //jsonObjectCustomer.put("hydrocare_subscripted",);
            //jsonObjectCustomer.put("blood_sos_subscripted",);
            //jsonObjectCustomer.put("blood_sos_on_off",);


            jsonObjectCustomerAddress.put("line1",editTextAddressLine1.getText().toString());
            jsonObjectCustomerAddress.put("line2",editTextAddressLine2.getText().toString());
            jsonObjectCustomerAddress.put("city",editTextCity.getText().toString());
            jsonObjectCustomerAddress.put("state",editTextState.getText().toString());
            jsonObjectCustomerAddress.put("country","India");
            jsonObjectCustomerAddress.put("zip_code",editTextPinCode.getText().toString());

            jsonObjectCustomerVitals.put("weight",editTextWeight.getText().toString());
            jsonObjectCustomerVitals.put("feet",editTextHeightFeet.getText().toString());
            jsonObjectCustomerVitals.put("inches",editTextHeightInch.getText().toString());
            jsonObjectCustomerVitals.put("blood_group_id",getBloodGroupId());
            //jsonObjectCustomerVitals.put("waist",);

            jsonObject.put("customer",jsonObjectCustomer);
            jsonObject.put("addresses_attributes",jsonObjectCustomerAddress);
            jsonObject.put("customer_vitals_attributes",jsonObjectCustomerVitals);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("ActivityEditProfile.createJson = " + jsonObject.toString());

        return jsonObject;
    }

    private int getBloodGroupId() {
        int bgId = 0;
        if (customerBloodGroup.equalsIgnoreCase("A +")) {
            bgId = 1;
        } else if (customerBloodGroup.equalsIgnoreCase("A -")) {
            bgId = 2;
        } else if (customerBloodGroup.equalsIgnoreCase("B +")) {
            bgId = 3;
        } else if (customerBloodGroup.equalsIgnoreCase("B -")) {
            bgId = 4;
        } else if (customerBloodGroup.equalsIgnoreCase("AB +")) {
            bgId = 5;
        } else if (customerBloodGroup.equalsIgnoreCase("AB -")) {
            bgId = 6;
        } else if (customerBloodGroup.equalsIgnoreCase("O +")) {
            bgId = 7;
        } else if (customerBloodGroup.equalsIgnoreCase("O -")) {
            bgId = 8;
        }
        System.out.println("ActivityEditProfile.getBloodGroupId="+bgId);
        return bgId;
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
        mAlert_Dialog.setContentView(R.layout.circular_progressbar);
        mAlert_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mAlert_Dialog.setCancelable(true);
        mAlert_Dialog.setCanceledOnTouchOutside(true);
        mAlert_Dialog.show();
    }

    private boolean checkValidation()
    {
        boolean ret = true;

        String errorText = "";
        if (!ValidationClass.HasSomeText(editTextFirstname,floatingFirstName)) {
            errorText = "Invalid name. ";
            ret = false;
        }
        if (!ValidationClass.HasSomeText(editTextHeightFeet,floatingHeightInFeet)) {
            errorText="Invalid height. ";
            ret = false;
        }

        if (!ValidationClass.HasSomeText(editTextHeightInch,floatingHeightInInch)) {
            errorText = "Invalid height. ";
            ret = false;
        }

        if (!ValidationClass.HasSomeText(editTextWeight,floatingWeight)) {
            errorText="Invalid weight. ";
            ret = false;
        }

        if (!ValidationClass.isEmailAddress(editTextEmail,floatingEmail,true) && !mCustomer.isChild()) {
            errorText = "Invalid email. ";
            ret = false;
        }

        if (!ValidationClass.IsPhoneNumber(editTextMobile,floatingMobile,true) && !mCustomer.isChild()) {
            errorText="Invalid mobile. ";
            ret = false;
        }
        System.out.println("ActivityEditProfile.checkValidation errorText="+errorText);

        return ret;
    }



    private void getCustomerDetailRequest() {
        String URL =TagValues.GET_CUSTOMER_DETAIL_URL;
        showPDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if(response!=null){
                            hidePDialog();
                            getCustomerDetailResponse(response);
                        }else{
                            CustomeDialog.dispDialog(ActivityEditProfile.this, TagValues.DATA_NOT_FOUND);
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
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                Set<String> keySet = headers.keySet();
                return super.parseNetworkResponse(response);

            }

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
                params.put("X-DEVICE-ID", customerManager.getDeviceID(ActivityEditProfile.this));
                if (!strFamilyMemberKey.equalsIgnoreCase(""))
                    params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

    }

    private void getCustomerDetailResponse(JSONObject response) {

        mProfileData = new Gson().fromJson(response.toString(), ProfileData.class);
        System.out.println("getCustomer========="+mProfileData.getCustomer()+"==="+mProfileData.getCustomer().getBlood_sugar());
        if (mProfileData.getCustomer() != null && mProfileData.getCustomer().getBlood_sugar() != null) {
            try {
                ProfileData mProfileDataNew = mProfileManager.getProfileData();
                List<Customer> familyMembers = new ArrayList<Customer>();

                if (mProfileDataNew != null && mProfileDataNew.getFamily_members() != null) {
                    familyMembers = mProfileDataNew.getFamily_members().getMember_list();
                }

                if (customerManager.isLoggedInCustomer() || mProfileDataNew == null)
                {
                    mProfileManager.setProfileData(mProfileData);
                    customerManager.setCurrentCustomer(mProfileData.getCustomer());
                    customerManager.setCurrentFamilyMember(mProfileData.getCustomer());
                } else {
                    for (int i = 0; i < familyMembers.size(); i++) {
                        Customer temp = familyMembers.get(i);
                        if (temp.getIdentification_token().equalsIgnoreCase(mProfileData.getCustomer().getIdentification_token())) {
                            familyMembers.set(i, mProfileData.getCustomer());
                            customerManager.setCurrentFamilyMember(mProfileData.getCustomer());
                        }
                    }
                    mProfileManager.setProfileData(mProfileDataNew);
                }
                mCustomer = customerManager.getCurrentFamilyMember();

                Intent intent = new Intent(ActivityEditProfile.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("PROFILE_EDIT",true);
                startActivity(intent);
                finish();

            } catch (NullPointerException e) {
                e.printStackTrace();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

        } else {
            String msg = "";
            if (mProfileData.getMsg() != null) {
                msg = mProfileData.getMsg().toString();
                if (msg.contains("401")) {
                    Intent i=new Intent(ActivityEditProfile.this,LoginActivity.class);
                    startActivity(i);
                }
            }
        }
    }
}