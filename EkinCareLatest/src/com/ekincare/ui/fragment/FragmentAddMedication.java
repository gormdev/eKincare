package com.ekincare.ui.fragment;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ekincare.R;
import com.ekincare.util.AddMedicationToListInterface;
import com.oneclick.ekincare.vo.Medication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RaviTejaN on 21-07-2016.
 */
public class FragmentAddMedication extends Fragment {

    private List<Integer> medicationResourceImageList;
    private List<String> medicationResourceTextList;
    private RecyclerView recyclerView;
    protected RecyclerViewAdapter mAdapter;

    private AppCompatEditText edittextMedicationName,edittextStrength;
    private TextInputLayout flaotingMedicationName,floatingMedicineStrength;

    private TextView textviewMorningQuantity,textviewNoonQuantity,textviewEveningQuantity;
    private TextView textviewMorningAdd,textviewMorningSub,textviewNoonAdd,textviewNoonSub,textviewEveningAdd,textviewEveningSub;
    private TextView textviewDayAdd,textviewDaySub,textviewDayQuantity;
    private TextView textviewMorningMedicationLabel,textviewNoonMedicationLabel,textviewEveningMedicationLabel;

    private LinearLayout layoutEmpty,layoutData;
    private FrameLayout frameLayoutContainer;
    private AppCompatButton appCompatButtonDone;

    private CheckBox checkBoxBeforeMeal, checkBoxAfterMeal,checkBoxDaily;

    boolean isMedicineSelected = false;
    String errorMessage = "";

    String medType = "Capsule";

    String drug_id="";
    String created_at = "";
    String active = "";
    String end_date = "";
    String id = "";

    AddMedicationToListInterface addMedicationToListInterface;

    String previousDayQuantity = "";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        addMedicationToListInterface = (AddMedicationToListInterface) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View mView = inflater.inflate(R.layout.content_add_medication, container, false);

        medicationResourceImageList = new ArrayList<Integer>();
        medicationResourceTextList = new ArrayList<String>();

        mAdapter = new RecyclerViewAdapter(medicationResourceImageList,medicationResourceTextList);

        initializeView(mView);

        if(addMedicationToListInterface.isEdit()){
            layoutData.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

            setEditConditionData();
        }

        setupResourceList();

        setupRecyclerView();

        textButtonAddFunctionality();

        textButtonSubFunctionality();

        appCompatButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonDoneFunctionality();
            }
        });

        checkBoxBeforeMeal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxAfterMeal.setChecked(false);
                }else{
                    checkBoxAfterMeal.setChecked(true);
                }
                try  {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });

        checkBoxAfterMeal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxBeforeMeal.setChecked(false);
                }else{
                    checkBoxBeforeMeal.setChecked(true);
                }
                try  {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });

        checkBoxDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    previousDayQuantity = textviewDayQuantity.getText().toString();
                    textviewDayQuantity.setText("0");
                }else{
                    textviewDayQuantity.setText(previousDayQuantity);
                }
            }
        });

        floatingMedicineStrength.setErrorEnabled(true);
        edittextStrength.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edittextStrength.getText().toString().isEmpty()){
                    setErrorData(medType,false,true);
                }else{
                    HasSomeText(edittextStrength,floatingMedicineStrength);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //flaotingMedicationName.setErrorEnabled(true);
        edittextMedicationName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(edittextMedicationName.getText().toString().isEmpty()){
                        setErrorData(medType,true,false);
                    }else{
                        HasSomeText(edittextMedicationName,flaotingMedicationName);
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return mView;
    }


    private void initializeView(View view)
    {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_medication_type);

        edittextMedicationName = (AppCompatEditText) view.findViewById(R.id.edit_text_capsule_name);
        edittextStrength = (AppCompatEditText) view.findViewById(R.id.edit_text_strength);

        flaotingMedicationName = (TextInputLayout) view.findViewById(R.id.floating_capsule_name);
        floatingMedicineStrength = (TextInputLayout) view.findViewById(R.id.floating_strenght);

        layoutData = (LinearLayout)view.findViewById(R.id.layout_data_container);
        layoutEmpty = (LinearLayout)view.findViewById(R.id.layout_empty_container);

        layoutEmpty.setVisibility(View.VISIBLE);
        layoutData.setVisibility(View.GONE);

        frameLayoutContainer = (FrameLayout) view.findViewById(R.id.frame_container);

        appCompatButtonDone = (AppCompatButton)view.findViewById(R.id.button_activate_medication);

        textviewMorningQuantity = (TextView) view.findViewById(R.id.text_morning_capsule_quantity);
        textviewNoonQuantity = (TextView) view.findViewById(R.id.text_noon_capsule_quantity);
        textviewEveningQuantity = (TextView) view.findViewById(R.id.text_evening_capsule_quantity);

        textviewMorningQuantity.setText("0");
        textviewNoonQuantity.setText("0");
        textviewEveningQuantity.setText("0");

        textviewMorningAdd = (TextView) view.findViewById(R.id.text_morning_add);
        textviewNoonAdd = (TextView) view.findViewById(R.id.text_noon_add);
        textviewEveningAdd = (TextView) view.findViewById(R.id.text_evening_add);

        textviewMorningSub = (TextView)view. findViewById(R.id.image_morning_sub);
        textviewNoonSub = (TextView) view.findViewById(R.id.image_noon_sub);
        textviewEveningSub = (TextView) view.findViewById(R.id.image_evening_sub);

        textviewMorningMedicationLabel = (TextView) view.findViewById(R.id.text_morning_capsule_label);
        textviewNoonMedicationLabel = (TextView) view.findViewById(R.id.text_noon_capsule_label);
        textviewEveningMedicationLabel = (TextView) view.findViewById(R.id.text_evening_capsule_label);

        textviewMorningMedicationLabel.setVisibility(View.GONE);
        textviewNoonMedicationLabel.setVisibility(View.GONE);
        textviewEveningMedicationLabel.setVisibility(View.GONE);

        textviewDayAdd = (TextView) view.findViewById(R.id.text_day_add);
        textviewDaySub= (TextView) view.findViewById(R.id.text_day_sub);
        textviewDayQuantity = (TextView) view.findViewById(R.id.text_day_capsule_quantity);

        checkBoxAfterMeal = (CheckBox) view.findViewById(R.id.checkbox_after_meal);
        checkBoxBeforeMeal = (CheckBox) view.findViewById(R.id.checkbox_before_meal);
        checkBoxDaily = (CheckBox) view.findViewById(R.id.checkbox_daily);
    }

    private void setupResourceList() {
        medicationResourceImageList.add(R.drawable.capsule);
        medicationResourceImageList.add(R.drawable.tablets);
        medicationResourceImageList.add(R.drawable.ic_syrup_24px);
        medicationResourceImageList.add(R.drawable.injection);
        medicationResourceImageList.add(R.drawable.fluides);

        medicationResourceTextList.add("Capsule");
        medicationResourceTextList.add("Tablet");
        medicationResourceTextList.add("Syrup");
        medicationResourceTextList.add("IV Med");
        medicationResourceTextList.add("IV Fluids");

    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void textButtonAddFunctionality()
    {
        textviewMorningAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    textAddAnimate(textviewMorningQuantity,false);
                }catch (NumberFormatException n){

                }
                try  {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });

        textviewEveningAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    textAddAnimate(textviewEveningQuantity,false);
                }catch (NumberFormatException n){

                }
                try  {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });

        textviewNoonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    textAddAnimate(textviewNoonQuantity,false);
                }catch (NumberFormatException n){

                }
                try  {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });

        textviewDayAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxDaily.setChecked(false);
                try{
                    textAddAnimate(textviewDayQuantity,true);
                }catch (NumberFormatException n){

                }

                try  {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });
    }

    private void textAddAnimate(final TextView textView, final boolean isInteger){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isInteger){
                    int count=0;
                    try{
                        count =  Integer.parseInt(textView.getText().toString()) + 1;
                    }catch (Exception e){

                    }
                    String value = String.valueOf(count);
                    textView.setText(value);
                }else{
                    Double count = 0.0;
                    try{
                        count =  Float.parseFloat(textView.getText().toString()) + 1.0;
                    }catch (Exception e){

                    }
                    String value = String.valueOf(count);
                    textView.setText(value);
                }
            }
        }, 300);
    }

    private void textSubAnimate(final TextView textView, final boolean isInteger){
        System.out.println("FragmentAddMedication.textSubAnimate");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isInteger){
                    int count=0;
                    try{
                        count =  Integer.parseInt(textView.getText().toString()) - 1;
                    }catch (Exception e){

                    }
                    String value = String.valueOf(count);
                    textView.setText(value);
                }
                else{
                    Double count = 0.0;
                    try{
                        count =  Float.parseFloat(textView.getText().toString()) - 1.0;
                    }catch (Exception e){

                    }
                    String value = String.valueOf(count);
                    textView.setText(value);
                }
            }
        }, 300);
    }

    private void textButtonSubFunctionality()
    {
        textviewMorningSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    if(Float.parseFloat(textviewMorningQuantity.getText().toString()) > 0.0){
                        textSubAnimate(textviewMorningQuantity,false);
                    }
                    else{
                        textviewMorningQuantity.setText("0.0");
                    }

                }catch (NumberFormatException n){

                }

                try  {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }

            }
        });

        textviewEveningSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(Float.parseFloat(textviewEveningQuantity.getText().toString()) > 0){
                        textSubAnimate(textviewEveningQuantity,false);
                    }
                    else{
                        textviewEveningQuantity.setText("0.0");
                    }

                }catch (NumberFormatException n){

                }
                try  {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }

            }
        });

        textviewNoonSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(Float.parseFloat(textviewNoonQuantity.getText().toString()) > 0){
                        textSubAnimate(textviewNoonQuantity,false);
                    }
                    else{
                        textviewNoonQuantity.setText("0");
                    }

                }catch (NumberFormatException n){

                }
                try  {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });

        textviewDaySub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxDaily.setChecked(false);
                try{
                    if(Integer.parseInt(textviewDayQuantity.getText().toString()) > 0){
                        textSubAnimate(textviewDayQuantity,true);
                    }
                    else{
                        textviewDayQuantity.setText("0");
                    }

                }catch (NumberFormatException n){

                }
                try  {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }

            }
        });
    }

    private void buttonDoneFunctionality()
    {
        if(isValidData())
        {
            Medication medicationDataModel = new Medication();
            medicationDataModel.setEvening_quantity(textviewEveningQuantity.getText().toString());
            medicationDataModel.setBefore_meal(""+checkBoxBeforeMeal.isChecked());
            medicationDataModel.setRecurring(checkBoxDaily.isChecked()+"");
            medicationDataModel.setNumber_of_days(textviewDayQuantity.getText().toString());
            medicationDataModel.setName(edittextMedicationName.getText().toString());
            medicationDataModel.setDose_quantity(edittextStrength.getText().toString());
            medicationDataModel.setMorning_quantity(textviewMorningQuantity.getText().toString());
            medicationDataModel.setAfternoon_quantity(textviewNoonQuantity.getText().toString());
            medicationDataModel.setMedication_type(medType);
            medicationDataModel.setActive(active);
            medicationDataModel.setEnd_date(end_date);
            medicationDataModel.setDrug_id(drug_id);
            medicationDataModel.setId(id);

            System.out.println("FragmentAddMedication.buttonDoneFunctionality drug_id="+drug_id+" "+checkBoxBeforeMeal.isChecked());

            addMedicationToListInterface.addMedicationToList(medicationDataModel);
        }
        else{
            Toast.makeText(getActivity(),errorMessage,Toast.LENGTH_LONG).show();
        }

        try  {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    private boolean isValidData()
    {
        String NAMEPATTERN = "^[A-Za-z]$";
        String STRENGTHPATTERN = "^[0-9]$";
        if(!HasSomeText(edittextMedicationName,flaotingMedicationName)){
            errorMessage="Invalid Data";
            return false;
        }

        if(!HasSomeText(edittextStrength,floatingMedicineStrength)){
            errorMessage = "Invalid data";
            return false;
        }
        if(textviewEveningQuantity.getText().toString().equalsIgnoreCase("0.0") && textviewMorningQuantity.getText().toString().equalsIgnoreCase("0.0") && textviewNoonQuantity.getText().toString().equalsIgnoreCase("0.0")){
            errorMessage ="Select appropriate medicine time (morning,afternoon or evening) as per prescription.";
            return false;
        }
        if(!checkBoxAfterMeal.isChecked() && !checkBoxBeforeMeal.isChecked()){
            errorMessage = "Medicine are usually taken before or after meal. Select the medication time as per prescription.";
            return false;
        }
        if(textviewDayQuantity.getText().toString().equalsIgnoreCase("0") && !checkBoxDaily.isChecked()){
            errorMessage = "Select number of days for the medication as per medical prescription";
            return false;
        }
        return true;
    }

    /*public  boolean IsValidField(AppCompatEditText editTextEmail, String regex, String errMsg, boolean required)
    {

        String text = editTextEmail.getText().toString().trim();

        editTextEmail.setError(null);

        if ( required && !HasSomeText(editTextEmail) ) return false;

        if (required && !Pattern.matches(regex, text))
        {
            editTextEmail.setError(errMsg);
            return false;
        }
        if(text.length() !=0 && !Pattern.matches(regex, text)){
            editTextEmail.setError(errMsg);
            return false;
        }
        return true;
    }*/

    public  boolean HasSomeText(AppCompatEditText editText,TextInputLayout textInputLayout)
    {
        String text = editText.getText().toString().trim();
        textInputLayout.setError(null);
        if (text.length() == 0)
        {
            textInputLayout.setError("Empty field is not allowed.");
            return false;
        }
        textInputLayout.setErrorEnabled(false);
        return true;
    }

    private void setEditConditionData()
    {
        Medication medication = addMedicationToListInterface.getMedicationObject();

        try {
            edittextMedicationName.setText(medication.getName());
            edittextStrength.setText(medication.getDose_quantity());
            textviewDayQuantity.setText((medication.getNumber_of_days()));

            textviewNoonQuantity.setText("0.0");
            textviewEveningQuantity.setText("0.0");
            textviewMorningQuantity.setText("0.0");

            if(medication.getAfternoon_quantity()!=null){
                textviewNoonQuantity.setText((medication.getAfternoon_quantity()));
            }

            if(medication.getEvening_quantity()!=null){
                textviewEveningQuantity.setText((medication.getEvening_quantity()));
            }

            if(medication.getMorning_quantity()!=null){
                textviewMorningQuantity.setText((medication.getMorning_quantity()));
            }

            if(Boolean.parseBoolean(medication.getBefore_meal())){
                checkBoxBeforeMeal.setChecked(true);
                checkBoxAfterMeal.setChecked(false);
            }else{
                checkBoxBeforeMeal.setChecked(false);
                checkBoxAfterMeal.setChecked(true);
            }

            if(Boolean.parseBoolean(medication.getRecurring())){
                checkBoxDaily.setChecked(true);
            }else{
                checkBoxDaily.setChecked(false);
            }

            String medicineLabel = "";
            if(medication.getMedication_type().equalsIgnoreCase("Syrup")){
                medicineLabel = "Spoon";
            }else{
                medicineLabel = medication.getMedication_type();
            }

            flaotingMedicationName.setHint(medication.getMedication_type() + " name");
            if(edittextMedicationName.getText().toString().isEmpty() ){
                setErrorData(medication.getMedication_type(),true,false);
            }
            if(edittextStrength.getText().toString().isEmpty() ){
                setErrorData(medication.getMedication_type(),false,true);
            }



            medType = medication.getMedication_type();

            textviewNoonMedicationLabel.setText(medicineLabel);
            textviewEveningMedicationLabel.setText(medicineLabel);
            textviewMorningMedicationLabel.setText(medicineLabel);

            drug_id = medication.getDrug_id();
            created_at = medication.getCreated_at();
            end_date = medication.getEnd_date();
            active = medication.getActive();
            id = medication.getId();
        }catch (Exception e){

        }
        System.out.println("FragmentAddMedication.setEditConditionData name=" + medication.getEnd_date());
        System.out.println("FragmentAddMedication.setEditConditionData name=" + medication.getAfternoon_quantity());
        System.out.println("FragmentAddMedication.setEditConditionData name=" + medication.getMorning_quantity());
        System.out.println("FragmentAddMedication.setEditConditionData name=" + medication.getEvening_quantity());

    }

    private void setErrorData(String medicationType,boolean isName,boolean isStrength){
        if(medicationType.equalsIgnoreCase("Syrup")){
            if(isName){
                flaotingMedicationName.setError("Ex. CHLORPHENIRAMINE MALEATE\n");
            }
            if (isStrength){
                floatingMedicineStrength.setError("Ex. 2mg/5ml");
            }
        }if(medicationType.equalsIgnoreCase("Tablet")){
            if(isName){
                flaotingMedicationName.setError("Ex. DICLOFENAC SODIUM");
            }
            if (isStrength){
                floatingMedicineStrength.setError("Ex. 50mg");
            }
        }if(medicationType.equalsIgnoreCase("IV Med")){
            if(isName){
                flaotingMedicationName.setError("Ex. CHLORAMPHENICOL");
            }
            if (isStrength){
                floatingMedicineStrength.setError("Ex. 5.0gm/100ml");
            }
        }if(medicationType.equalsIgnoreCase("Capsule")){
            if(isName){
                flaotingMedicationName.setError("Ex. DIDANOSINE");
            }
            if (isStrength){
                floatingMedicineStrength.setError("Ex. 250mg");
            }
        }
        if(medicationType.equalsIgnoreCase("IV Fluids")){
            if(isName){
                flaotingMedicationName.setError("Ex. IV Fluids");
            }
            if (isStrength){
                floatingMedicineStrength.setError("Ex. 250mg");
            }
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
    {
        private List<Integer> medicationImageList;
        private List<String> medicationTextList;

        private int selectedItem = -1;


        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView medicationText;
            ImageView medicationImage;
            LinearLayout containerLayout;

            public MyViewHolder(View view) {
                super(view);
                medicationText = (TextView) view.findViewById(R.id.text_medication);
                medicationImage = (ImageView) view.findViewById(R.id.image_medication);
                containerLayout = (LinearLayout) view.findViewById(R.id.container_medication);

                containerLayout.setOnClickListener(this);


            }

            @Override
            public void onClick(View v)
            {
                medType = medicationTextList.get(getAdapterPosition());

                isMedicineSelected = true;

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(isMedicineSelected){
                        if(selectedItem!=getAdapterPosition()){
                            exitReveal(getAdapterPosition());
                        }
                    }else{
                        circularRevel(getAdapterPosition());
                    }
                }else{
                    layoutData.setVisibility(View.VISIBLE);
                    layoutEmpty.setVisibility(View.GONE);

                    flaotingMedicationName.setHint(medicationTextList.get(getAdapterPosition()) + " name");


                    String medicineLabel = "";
                    if(medicationTextList.get(getAdapterPosition()).equalsIgnoreCase("Syrup")){
                        medicineLabel = "Spoon";
                    }else{
                        medicineLabel = medicationTextList.get(getAdapterPosition());
                    }

                    textviewNoonMedicationLabel.setText(medicineLabel);
                    textviewEveningMedicationLabel.setText(medicineLabel);
                    textviewMorningMedicationLabel.setText(medicineLabel);
                    edittextMedicationName.setText("");
                    edittextStrength.setText("");

                    textviewDayQuantity.setText("0");
                    textviewEveningQuantity.setText("0");
                    textviewMorningQuantity.setText("0");
                    textviewNoonQuantity.setText("0");

                }

                notifyItemChanged(selectedItem);
                selectedItem = recyclerView.getChildPosition(v);
                notifyItemChanged(selectedItem);


            }
        }


        public RecyclerViewAdapter(List<Integer> medicationImage,List<String> medicationText) {
            this.medicationImageList = medicationImage;
            this.medicationTextList = medicationText;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_add_medication_item_layout, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position)
        {
            String medicalType = medicationTextList.get(position);

            holder.medicationImage.setImageResource(medicationImageList.get(position));

            holder.medicationText.setText(medicationTextList.get(position));

            holder.containerLayout.setSelected(selectedItem == position);
        }

        @Override
        public int getItemCount() {
            return medicationImageList.size();
        }

        @TargetApi(21)
        private void circularRevel(final int position)
        {
            int cx = frameLayoutContainer.getWidth()/2;
            int cy = 0;
            int finalRadius = Math.max(frameLayoutContainer.getWidth(), frameLayoutContainer.getHeight());
            Animator anim = ViewAnimationUtils.createCircularReveal(layoutData, cx, cy, 0, finalRadius);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(600);
            layoutData.setVisibility(View.VISIBLE);

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation)
                {
                    flaotingMedicationName.setHint(medicationTextList.get(position) + " name");

                    String medicineLabel = "";
                    if(medicationTextList.get(position).equalsIgnoreCase("Syrup")){
                        medicineLabel = "Spoons";
                    }else{
                        medicineLabel = medicationTextList.get(position)+"s";
                    }

                    textviewNoonMedicationLabel.setText(medicineLabel);
                    textviewEveningMedicationLabel.setText(medicineLabel);
                    textviewMorningMedicationLabel.setText(medicineLabel);

                    edittextMedicationName.setText("");
                    edittextStrength.setText("");

                    textviewDayQuantity.setText("0");
                    textviewEveningQuantity.setText("0.0");
                    textviewMorningQuantity.setText("0.0");
                    textviewNoonQuantity.setText("0.0");

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    layoutEmpty.setVisibility(View.GONE);
                    //layoutData.setBackgroundColor(getResources().getColor(R.color.white));
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            anim.start();
        }

        @TargetApi(21)
        void exitReveal(final int position)
        {
            int cx = frameLayoutContainer.getMeasuredWidth() / 2;
            int cy = 0;

            // get the initial radius for the clipping circle
            int finalRadius = Math.max(frameLayoutContainer.getWidth(), frameLayoutContainer.getHeight());


            // create the animation (the final radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(layoutData, cx, cy, finalRadius, 0);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(600);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    layoutData.setVisibility(View.INVISIBLE);
                    circularRevel(position);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            // start the animation
            anim.start();
        }
    }
}
