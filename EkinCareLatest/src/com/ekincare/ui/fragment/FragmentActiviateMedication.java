package com.ekincare.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ekincare.R;
import com.ekincare.util.AddMedicationToListInterface;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.vo.Medication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by RaviTejaN on 21-07-2016.
 */
public class FragmentActiviateMedication extends Fragment {

    private ArrayList<Medication> medicationList;
    private RecyclerView recyclerView;
    protected RecyclerViewAdapter mAdapter;
    private AppCompatButton appCompatButtonActivateMedication,appCompatButtonAddMore;

    private AppCompatEditText editTextDoctorName,editTextMedicationReason,editTextStartDate;
    private TextInputLayout textInputLayoutDoctorName,textInputLayoutMedicationReason,textInputLayoutStartDate;

    AddMedicationToListInterface addMedicationToListInterface;


    Calendar todayDate = Calendar.getInstance();
    SimpleDateFormat dateViewFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        addMedicationToListInterface = (AddMedicationToListInterface) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View mView = inflater.inflate(R.layout.fragment_activiate_medication, container, false);

        medicationList = new ArrayList<Medication>();

        setupResourceList();

        mAdapter = new RecyclerViewAdapter(medicationList);

        initializeView(mView);

        if(addMedicationToListInterface.isEdit()){
            appCompatButtonAddMore.setVisibility(View.GONE);
            setEditConditionData();
        }

        setupRecyclerView();


        appCompatButtonActivateMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = medicationList.get(0).getId();
                System.out.println("FragmentActiviateMedication.onClick " + " id=" + medicationList.get(0).getId() + " name="+ medicationList.get(0).getName() );
                setupActivateMedicationFunctionality(id);
            }
        });

        appCompatButtonAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupFabFunctionality();
            }
        });

        editTextDoctorName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editTextDoctorName.getText().toString().isEmpty()) {
                    textInputLayoutDoctorName.setError("Optional");
                }else {
                    textInputLayoutDoctorName.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextMedicationReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editTextMedicationReason.getText().toString().isEmpty()) {
                    textInputLayoutMedicationReason.setError("Optional");
                }else {
                    textInputLayoutMedicationReason.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextStartDate.setClickable(true);
        editTextStartDate.setText(dateViewFormatter.format(todayDate.getTime()));
        editTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(editTextStartDate);
            }
        });

        editTextStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    showDatePicker(editTextStartDate);
                } else {
                    // Hide your calender here
                }
            }
        });

        textInputLayoutStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(editTextStartDate);
            }
        });

        return mView;
    }

    private void setEditConditionData() {
        Medication medication = addMedicationToListInterface.getMedicationObject();

        try {
            editTextDoctorName.setText(medication.getPrescriber_name());
            editTextMedicationReason.setText(medication.getInstructions());
            editTextStartDate.setText((medication.getDate()));

        }catch (Exception e){

        }
    }


    private void setupActivateMedicationFunctionality(String id)
    {
        JSONObject jsonObject;
        if(addMedicationToListInterface.isEdit()){
            jsonObject = createEditJSONObject(id);
        }else{
            jsonObject = createAddJSONObject();
        }

        if (NetworkCaller.isInternetOncheck(getActivity())) {
            addMedicationToListInterface.activateMedication(jsonObject,id);
        }else{
            Toast.makeText(getActivity(),getResources().getString(R.string.networkloss),Toast.LENGTH_LONG).show();
        }

    }

    public void showDatePicker(final EditText date)
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.dialog_date_picker_view, null, false);
        dialog.setContentView(dialogLayout);

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.dialog_datepicker);
        final TextView dateTextView = (TextView) dialog.findViewById(R.id.dialog_dateview);
        //final TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);

        // Minimum date

        todayDate.set(todayDate.get(Calendar.YEAR), todayDate.get(Calendar.MONTH), todayDate.get(Calendar.DAY_OF_MONTH) + 1);

        // View settings
       // dialogTitle.setText("Choose Date for activation of Medication ");

        dateTextView.setText(dateViewFormatter.format(todayDate.getTime()));

        // Initialize datepicker in dialog atepicker
        datePicker.init(todayDate.get(Calendar.YEAR), todayDate.get(Calendar.MONTH), todayDate.get(Calendar.DAY_OF_MONTH) + 1, new DatePicker.OnDateChangedListener() {
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
                date.setText(dateViewFormatter.format(choosen.getTime()));
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private JSONObject createAddJSONObject()
    {
        JSONObject jsonObject = new JSONObject();

        JSONArray req = new JSONArray();

        for(int i = 0; i<medicationList.size();i++)
        {
            try {
                JSONObject object = new JSONObject();
                object.put("name", medicationList.get(i).getName());
                object.put("date", editTextStartDate.getText().toString());
                object.put("drug_id", medicationList.get(i).getDrug_id());
                object.put("provider_id", medicationList.get(i).getProvider_id());
                object.put("medication_type", medicationList.get(i).getMedication_type());
                object.put("instructions", editTextMedicationReason.getText().toString());
                object.put("dose_quantity",medicationList.get(i).getDose_quantity());
                object.put("active", medicationList.get(i).getActive());
                object.put("prescriber_name", editTextDoctorName.getText().toString());
                if(Double.parseDouble(medicationList.get(i).getMorning_quantity())>0){
                    object.put("morning", "1");
                }else{
                    object.put("morning", "0");
                }
                if(Double.parseDouble(medicationList.get(i).getAfternoon_quantity())>0){
                    object.put("afternoon", "1");
                }else{
                    object.put("afternoon", "0");
                }
                if(Double.parseDouble(medicationList.get(i).getEvening_quantity())>0){
                    object.put("evening", "1");
                }else{
                    object.put("evening", "0");
                }
                object.put("morning_quantity", medicationList.get(i).getMorning_quantity());
                object.put("afternoon_quantity", medicationList.get(i).getAfternoon_quantity());
                object.put("evening_quantity", medicationList.get(i).getEvening_quantity());
                object.put("before_meal", medicationList.get(i).getBefore_meal());
                object.put("recurring", medicationList.get(i).getRecurring());
                object.put("number_of_days", medicationList.get(i).getNumber_of_days());
                object.put("end_date",getEndDate(Integer.parseInt(medicationList.get(i).getNumber_of_days())));

                req.put(object);

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }

        try {
            jsonObject.put("medication", req);
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

        return jsonObject;
    }

    private JSONObject createEditJSONObject(String id)
    {
        int i = 0;
        JSONObject jsonObject = new JSONObject();

        JSONObject object = new JSONObject();
        try {
            object.put("id", id);
            object.put("name", medicationList.get(i).getName());
            object.put("date", editTextStartDate.getText().toString());
            object.put("drug_id", medicationList.get(i).getDrug_id());
            object.put("provider_id", medicationList.get(i).getProvider_id());
            object.put("medication_type", medicationList.get(i).getMedication_type());
            object.put("instructions", editTextMedicationReason.getText().toString());
            object.put("dose_quantity", medicationList.get(i).getDose_quantity());
            object.put("active", medicationList.get(i).getActive());
            object.put("prescriber_name", editTextDoctorName.getText().toString());

            if (Double.parseDouble(medicationList.get(i).getMorning_quantity()) > 0) {
                object.put("morning", "1");
            } else {
                object.put("morning", "0");
            }
            if (Double.parseDouble(medicationList.get(i).getAfternoon_quantity()) > 0) {
                object.put("afternoon", "1");
            } else {
                object.put("afternoon", "0");
            }
            if (Double.parseDouble(medicationList.get(i).getEvening_quantity()) > 0) {
                object.put("evening", "1");
            } else {
                object.put("evening", "0");
            }
            object.put("morning_quantity", medicationList.get(i).getMorning_quantity());
            object.put("afternoon_quantity", medicationList.get(i).getAfternoon_quantity());
            object.put("evening_quantity", medicationList.get(i).getEvening_quantity());
            object.put("before_meal", medicationList.get(i).getBefore_meal());
            object.put("recurring", medicationList.get(i).getRecurring());
            object.put("number_of_days", medicationList.get(i).getNumber_of_days());
            object.put("end_date", getEndDate(Integer.parseInt(medicationList.get(i).getNumber_of_days())));

            jsonObject.put("medication", object);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private String getEndDate(int numberOfDays)
    {
        String startDate = editTextStartDate.getText().toString();

        Calendar c = Calendar.getInstance();
        try {
            c.setTime(dateViewFormatter.parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, numberOfDays);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
        String output = sdf1.format(c.getTime());

        System.out.println("FragmentActiviateMedication.getEndDate output=" + output);
        return output;
    }

    private void setupFabFunctionality() {
        addMedicationToListInterface.switchFragment(new FragmentAddMedication());
    }

    private void initializeView(View view)
    {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        appCompatButtonAddMore = (AppCompatButton) view.findViewById(R.id.button_add_medication);
        appCompatButtonActivateMedication = (AppCompatButton) view.findViewById(R.id.button_activate_medication);

        editTextDoctorName = (AppCompatEditText) view.findViewById(R.id.edit_text_doctor_name);
        editTextMedicationReason= (AppCompatEditText) view.findViewById(R.id.edit_text_reson_for_medication);
        editTextStartDate= (AppCompatEditText) view.findViewById(R.id.edit_text_date);

        textInputLayoutDoctorName = (TextInputLayout) view.findViewById(R.id.floating_doctor_name);
        textInputLayoutMedicationReason = (TextInputLayout) view.findViewById(R.id.floating_reson_for_medication);
        textInputLayoutStartDate = (TextInputLayout) view.findViewById(R.id.floating_medication_date);

        textInputLayoutMedicationReason.setError("Optional");
        textInputLayoutDoctorName.setError("Optional");
        textInputLayoutDoctorName.setError("Optional");
    }

    private void setupResourceList() {
        medicationList = addMedicationToListInterface.getMedicationList();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }


    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
    {
        private ArrayList<Medication> medicationList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView medicationText;
            ImageView medicationImage,medicationDeleteImage;
            TextView medicationQuantityMorning,medicationNoon,medicationEvening;
            TextView medicationTime;

            public MyViewHolder(View view) {
                super(view);
                medicationText = (TextView) view.findViewById(R.id.text_medication_name);
                medicationTime = (TextView) view.findViewById(R.id.text_medication_time);
                medicationQuantityMorning = (TextView) view.findViewById(R.id.text_medication_morning_quantity);
                medicationNoon = (TextView) view.findViewById(R.id.text_medication_noon_quantity);
                medicationEvening = (TextView) view.findViewById(R.id.text_medication_evening_quantity);
                medicationImage = (ImageView) view.findViewById(R.id.image_medication);
                medicationDeleteImage = (ImageView) view.findViewById(R.id.image_delete);

                medicationDeleteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addMedicationToListInterface.deleteFromMedicationList(medicationList.get(getAdapterPosition()));
                        notifyItemRemoved(getAdapterPosition());
                    }
                });
            }
        }


        public RecyclerViewAdapter(ArrayList<Medication> medicationList) {
            this.medicationList = medicationList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_medications_layout, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position)
        {
            Medication medicationDataModel = medicationList.get(position);

            int resourceImage;
            String medicalType="";

            if(medicationDataModel.getMedication_type().equalsIgnoreCase("Tablet")){
                resourceImage = R.drawable.tablets;
            }else if(medicationDataModel.getMedication_type().equalsIgnoreCase("Capsule")){
                resourceImage = R.drawable.capsule;
            }else if(medicationDataModel.getMedication_type().equalsIgnoreCase("Syrup")){
                resourceImage = R.drawable.ic_syrup_24px;
            }else if(medicationDataModel.getMedication_type().equalsIgnoreCase("Injection")){
                resourceImage = R.drawable.injection;
            }else{
                resourceImage = R.drawable.fluides;
            }

            holder.medicationImage.setImageResource(resourceImage);

            String mealData = "";
            if(medicationDataModel.getBefore_meal().equalsIgnoreCase("false"))
            {
                mealData = "after meal";
            }else
            {
                mealData = "before meal";
            }

            double morning_quantity = Double.parseDouble(medicationDataModel.getMorning_quantity());
            double afternoon_quantity = Double.parseDouble(medicationDataModel.getAfternoon_quantity());
            double evening_quantity = Double.parseDouble(medicationDataModel.getEvening_quantity());

            String medicationType = medicationDataModel.getMedication_type();
            if(medicationDataModel.getMedication_type().equalsIgnoreCase("Syrup")){
                medicationType = "Spoon";
            }

            if(morning_quantity>0){
                holder.medicationQuantityMorning.setVisibility(View.VISIBLE);
                holder.medicationQuantityMorning.setText(medicationDataModel.getMorning_quantity() + " " + medicationType + " in morning");
            }else{
                holder.medicationQuantityMorning.setVisibility(View.GONE);
            }

            if(evening_quantity>0){
                holder.medicationEvening.setVisibility(View.VISIBLE);
                holder.medicationEvening.setText(medicationDataModel.getEvening_quantity() + " " + medicationType + " in evening");
            }else{
                holder.medicationEvening.setVisibility(View.GONE);
            }

            if(afternoon_quantity>0){
                holder.medicationNoon.setVisibility(View.VISIBLE);
                holder.medicationNoon.setText(medicationDataModel.getAfternoon_quantity() + " " +medicationType + " in noon");
            }else{
                holder.medicationNoon.setVisibility(View.GONE);
            }

            holder.medicationText.setText(medicationDataModel.getName() + ", " + medicationDataModel.getDose_quantity());

            if(Integer.parseInt(medicationDataModel.getNumber_of_days())>0){
                holder.medicationTime.setText("medication for "+medicationDataModel.getNumber_of_days() + " days, " + mealData);
            }else{
                holder.medicationTime.setText("Daily, " + mealData);
            }


        }

        @Override
        public int getItemCount() {
            return medicationList.size();
        }

    }
}