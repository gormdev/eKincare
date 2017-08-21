package com.oneclick.ekincare;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
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
import com.ekincare.util.TransitionAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.oneclick.ekincare.helper.CustomeDialog;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.Register;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by RaviTejaN on 25-11-2016.
 */

public class ActivityFamilyMemberSwitch extends AppCompatActivity {

    private PreferenceHelper prefs;
    private CustomerManager customerManager;
    private List<Customer> familyMembers = new ArrayList<Customer>();
    private RecyclerView recyclerView;
    private ActivityFamilyMemberSwitch.RecyclerViewAdapter mAdapter;
    ProfileManager mProfileManager;
    private Customer mCustomer;

    private Toolbar topToolbar;
    AppBarLayout appbar;
    private int mMaxScrollSize;
    private FloatingActionButton mFab;
    private Dialog mAlert_Dialog;

    CircleProgressBar progressWithArrow;

    private String strFamilyMemberKey = "";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_family_member_list);

        prefs = new PreferenceHelper(this);
        customerManager = CustomerManager.getInstance(this);
        mProfileManager = ProfileManager.getInstance(this);

        familyMembers = mProfileManager.getFamilyMembers();
        System.out.println("ActivityFamilyMemberSwitch.onCreate mProfileManager.getFamilyMembers()="
                +mProfileManager.getFamilyMembers()+"======="+customerManager.isLoggedInCustomer());
        if (!customerManager.isLoggedInCustomer())
        {
            System.out.println("notloginCustomer========"+"yes");
            Iterator<Customer> it = mProfileManager.getFamilyMembers().iterator();
            while(it.hasNext()){
                Customer customer = it.next();
                if (customerManager.getCurrentFamilyMember().getIdentification_token().equals(customer.getIdentification_token())) {
                    familyMembers.remove(it);
                }
            }
            if(!familyMembers.contains(mProfileManager.getLoggedinCustomer()))
                familyMembers.add(mProfileManager.getLoggedinCustomer());

        }else if(customerManager.isLoggedInCustomer())
        {
            System.out.println("notloginCustomer========"+"yes No");
            mCustomer = mProfileManager.getLoggedinCustomer();
            if(!familyMembers.contains(mCustomer))
                familyMembers.add(0, mCustomer);
        }

        mFab = (FloatingActionButton) findViewById(R.id.fab_add);
        mFab.setVisibility(View.GONE);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ActivityFamilyMemberSwitch.RecyclerViewAdapter(familyMembers);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        appbar = (AppBarLayout) findViewById(R.id.app_bar);

        topToolbar = (Toolbar) findViewById(R.id.toolbar_top);

        setupToolbar();

        setupAppBar();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (savedInstanceState == null) {
                mFab.setScaleX(0);
                mFab.setScaleY(0);
                getWindow().getEnterTransition().addListener(new TransitionAdapter() {
                    @Override
                    public void onTransitionEnd(Transition transition) {
                        getWindow().getEnterTransition().removeListener(this);
                        mFab.animate().scaleX(1).scaleY(1);
                    }
                });
            }
        }

        hidePDialog();

    }

    @Override
    protected void onResume() {
        super.onResume();
        hidePDialog();
    }

    private void setupAppBar()
    {
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (mMaxScrollSize == 0)
                    mMaxScrollSize = appBarLayout.getTotalScrollRange();


                float currentScrollPercentage = ((Math.abs(i)) * 100 / mMaxScrollSize);
                System.out.println("i=" + currentScrollPercentage + " " + currentScrollPercentage / 100);





            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(topToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        topToolbar.setTitle("Switch Family Members");

        topToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityFamilyMemberSwitch.this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                ActivityFamilyMemberSwitch.this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
                //finish();
            }
        });




    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<ActivityFamilyMemberSwitch.RecyclerViewAdapter.MyViewHolder> {

        private int color =  Color.BLUE;
        private List<Customer> familyMembers;
        String firstLetter;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, email;
            ImageView drawimage;
            RelativeLayout containerLayout;

            View itemLayoutView;

            public MyViewHolder(View view) {
                super(view);
                this.itemLayoutView = itemLayoutView;
                name = (TextView) view.findViewById(R.id.name);
                email = (TextView) view.findViewById(R.id.emailAddress);
                drawimage=(ImageView)view.findViewById(R.id.draw_image);
                containerLayout = (RelativeLayout) view.findViewById(R.id.popup_user_row_container);
            }
        }


        public RecyclerViewAdapter(List<Customer> familyMembers) {
            this.familyMembers = familyMembers;
        }

        @Override
        public ActivityFamilyMemberSwitch.RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_member_row, parent, false);

            return new ActivityFamilyMemberSwitch.RecyclerViewAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ActivityFamilyMemberSwitch.RecyclerViewAdapter.MyViewHolder holder, final int position) {
            try{
                System.out.println("RecyclerViewAdapter.onBindViewHolder===="+familyMembers.size());
                System.out.println("RecyclerViewAdapter.onBindViewHolde========="+familyMembers.get(position).getFirst_name().length());

                String firstName = familyMembers.get(position).getFirst_name();
                String lastName = familyMembers.get(position).getLast_name();
                final String healthScore = familyMembers.get(position).getHealth_score();

                if(firstName.length()==1){
                    firstLetter = String.valueOf(firstName.substring(0, 1));
                }else{
                    firstLetter = String.valueOf(firstName.substring(0, 2));
                }



                //String firstLetter = String.valueOf(firstName.substring(0, 2));
                if(customerManager.getCurrentFamilyMember().getFirst_name().equals(firstName) && customerManager.getCurrentFamilyMember().getLast_name().equals(lastName))
                {
                    System.out.println("Adapter = " + customerManager.getCurrentFamilyMember().getFirst_name() + " name=" + firstName);
                    if(prefs.getCustomerImageColor()!=0){
                        color = prefs.getCustomerImageColor();

                    }
                }else{
                    ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
                    color =  generator.getRandomColor();
                }

                if(firstName.length()==1){
                    TextDrawable drawable = TextDrawable.builder()
                            .beginConfig()
                            .textColor(Color.WHITE)
                            .useFont(Typeface.DEFAULT)
                            .fontSize(getResources().getInteger(R.integer.int_family_member_list_circle_size))
                            .bold()
                            .withBorder(0)
                            .endConfig()
                            .buildRound(firstLetter.toUpperCase() ,color);
                    holder.drawimage.setImageDrawable(drawable);
                }else{
                    TextDrawable drawable = TextDrawable.builder()
                            .beginConfig()
                            .textColor(Color.WHITE)
                            .useFont(Typeface.DEFAULT)
                            .fontSize(getResources().getInteger(R.integer.int_family_member_list_circle_size))
                            .bold()
                            .withBorder(0)
                            .endConfig()
                            .buildRound(firstLetter.substring(0, 1).toUpperCase() + firstLetter.substring(1),color);
                    holder.drawimage.setImageDrawable(drawable);
                }


                familyMembers.get(position).setUserColor(color);

                StringBuffer stringbf = new StringBuffer();
                StringBuffer stringbf2 = new StringBuffer();
                Matcher m = Pattern.compile("([a-z])([a-z]*)",
                        Pattern.CASE_INSENSITIVE).matcher(firstName);
                Matcher m2 = Pattern.compile("([a-z])([a-z]*)",
                        Pattern.CASE_INSENSITIVE).matcher(lastName);
                while (m.find()) {
                    m.appendReplacement(stringbf,
                            m.group(1).toUpperCase() + m.group(2).toLowerCase());
                }

                while (m2.find()) {
                    m2.appendReplacement(stringbf2,
                            m2.group(1).toUpperCase() + m2.group(2).toLowerCase());
                }

                holder.name.setText(m.appendTail(stringbf).toString() + " " + m2.appendTail(stringbf2).toString());

                if(!familyMembers.get(position).isChild()){
                    if(healthScore!=null){
                        holder.email.setText("Health Score: "+healthScore);
                    }else{
                        holder.email.setText("Wizard pending");
                    }

                }else{
                    holder.email.setText("");
                    holder.email.setVisibility(View.GONE);
                }


                holder.containerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(healthScore!=null){
                            System.out.println("healthscore========="+familyMembers.get(position));
                            Intent intent = new Intent(ActivityFamilyMemberSwitch.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            intent.putExtra("SWITCH_FAMILY",true);
                            //customerManager.setCurrentCustomer(familyMembers.get(position));
                            customerManager.setCurrentFamilyMember(familyMembers.get(position));
                            System.out.println("healthscore========="+customerManager.getCurrentFamilyMember().getIdentification_token());

                            startActivity(intent);
                            finish();
                        }else {
                            if(!familyMembers.get(position).isChild()){
                                customerManager.setCurrentFamilyMember(familyMembers.get(position));
                                System.out.println("customerkey=========" + customerManager.getCurrentFamilyMember().getIdentification_token());
                                wizardVolleyRequest();
                            }else{
                                customerManager.setCurrentFamilyMember(familyMembers.get(position));
                                System.out.println("customerkey=========" + customerManager.getCurrentFamilyMember().getIdentification_token());
                                wizardVolleyChildRequest();
                            }

                        }


                    }
                });

            }catch(Exception e){
                e.printStackTrace();
                holder.containerLayout.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return familyMembers.size();
        }
    }


    private void wizardVolleyChildRequest() {
        String URL = TagValues.WIZARD_STATS_URL ;
        System.out.println("ActivityFamilyMemberSwitch.wizardVolleyChildRequest===="+customerManager.isLoggedInCustomer());
        showPDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){

                            System.out.println("login verify===="+response.toString());
                            hidePDialog();
                            wizardVolleyChildResponse(response);
                        }else{
                            CustomeDialog.dispDialog(ActivityFamilyMemberSwitch.this, "Internet not available");

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
                params.put("X-DEVICE-ID", customerManager.getDeviceID(ActivityFamilyMemberSwitch.this));
                if (!strFamilyMemberKey.equalsIgnoreCase(""))
                    params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    private void wizardVolleyChildResponse(JSONObject response) {
        Register register = new Gson().fromJson(response.toString(), Register.class);
        String wizardStatus = register.getStatus();
        if (wizardStatus.equals("1")) {
            System.out.println("mystatus=========" + wizardStatus);
            Intent adult = new Intent(ActivityFamilyMemberSwitch.this, ChildWizardActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("isFrom", "Register");
            bundle.putInt("question", Integer.parseInt(wizardStatus));
            bundle.putString("FamilyMemberKey", customerManager.getCurrentFamilyMember().getIdentification_token());
            adult.putExtras(bundle);
            startActivity(adult);
            finish();
        }
        else {
            Intent intent = new Intent(ActivityFamilyMemberSwitch.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("SWITCH_FAMILY",true);
            startActivity(intent);
            finish();
        }


    }


    private void wizardVolleyRequest() {
        String URL = TagValues.WIZARD_STATS_URL ;
        showPDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){

                            System.out.println("login verify===="+response.toString());
                            hidePDialog();
                            wizardVolleyResponse(response);
                        }else{
                            CustomeDialog.dispDialog(ActivityFamilyMemberSwitch.this, TagValues.DATA_NOT_FOUND);
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
                params.put("X-DEVICE-ID", customerManager.getDeviceID(ActivityFamilyMemberSwitch.this));
                if (!strFamilyMemberKey.equalsIgnoreCase(""))
                    params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

    }

    private void wizardVolleyResponse(JSONObject response) {
        Register register = new Gson().fromJson(response.toString(), Register.class);
        String wizardStatus = register.getStatus();

        System.out.println("family size===="+prefs.getIsFIrstWizard()+"==="+familyMembers.size()+"===="+Integer.parseInt(wizardStatus));
        if(familyMembers.size()==1){
            Intent adult = new Intent(ActivityFamilyMemberSwitch.this, AdultWizardActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("isFrom", "Register");
            bundle.putInt("question", Integer.parseInt(wizardStatus));
            bundle.putString("FamilyMemberKey", "");
            adult.putExtras(bundle);
            startActivity(adult);
            finish();
        }else{
            Intent adult = new Intent(ActivityFamilyMemberSwitch.this, AdultWizardActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("isFrom", "Register");
            bundle.putInt("question", Integer.parseInt(wizardStatus));
            bundle.putString("FamilyMemberKey", customerManager.getCurrentFamilyMember().getIdentification_token());
            adult.putExtras(bundle);
            startActivity(adult);
            finish();
        }



    }

    private void hidePDialog() {
        if (mAlert_Dialog != null) {
            mAlert_Dialog.dismiss();
            mAlert_Dialog=null;
        }
    }

    private void showPDialog() {
        mAlert_Dialog = new Dialog(ActivityFamilyMemberSwitch.this);
        mAlert_Dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mAlert_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAlert_Dialog.setContentView(R.layout.materialprogressbar);
        mAlert_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert_Dialog.setCancelable(true);
        mAlert_Dialog.setCanceledOnTouchOutside(false);
        progressWithArrow = (CircleProgressBar)mAlert_Dialog.findViewById(R.id.progressWithArrow);
        progressWithArrow.setColorSchemeResources(android.R.color.holo_blue_light);
        mAlert_Dialog.show();
    }

}
