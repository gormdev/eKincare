package com.ekincare.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ekincare.R;
import com.ekincare.util.SignUpDataInterface;

import java.util.regex.Pattern;

/**
 * Created by RaviTejaN on 21-03-2017.
 */

public class FragmentSignUp extends Fragment
{
    View createView;
    SignUpDataInterface signUpData;
    AppCompatEditText editTextName;
    TextInputLayout textInputLayoutFirstName;
    AppCompatEditText editTextEmail;
    AppCompatEditText editTextMobileNUmber;
    TextInputLayout textInputLayoutemail;
    TextInputLayout textInputLayoutMobileNumber;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        signUpData = (SignUpDataInterface) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        createView = inflater.inflate(R.layout.activity_register, container, false);

       // signUpData.showBackButton();
        editTextName = (AppCompatEditText) createView.findViewById(R.id.edit_text_user_name);
        textInputLayoutFirstName = (TextInputLayout) createView.findViewById(R.id.floating_user_name);
        editTextEmail = (AppCompatEditText)createView.findViewById(R.id.edit_text_user_email);
        editTextMobileNUmber=(AppCompatEditText)createView.findViewById(R.id.edit_text_user_contact);
        textInputLayoutemail = (TextInputLayout)createView.findViewById(R.id.floating_user_email);
        textInputLayoutMobileNumber= (TextInputLayout)createView.findViewById(R.id.floating_user_contact);

        textInputLayoutFirstName.setError("");
        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("FragmentSignUpName.onTextChanged======="+editTextName.getText().toString());
                if(editTextName.getText().toString().isEmpty()){
                    textInputLayoutFirstName.setError("Invalid name");
                }else{
                    textInputLayoutFirstName.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(!signUpData.getName().isEmpty()){
            editTextName.setText(signUpData.getName());
        }

        editTextName.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    signUpData.callNextFragmnet();
                    System.out.println("FragmentFirstAndLastName.onEditorAction===="+"Yesssssss");
                }
                return false;
            }
        });


        textInputLayoutemail.setError("");

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("FragmentSignUpName.onTextChanged======="+ editTextEmail.getText().toString());
                if(editTextEmail.getText().toString().isEmpty()){
                    textInputLayoutemail.setError("Invalid email");
                }else{
                    textInputLayoutemail.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(!signUpData.getEmailId().isEmpty()){
            editTextEmail.setText(signUpData.getEmailId());
        }

        editTextEmail.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    signUpData.callNextFragmnet();
                    System.out.println("FragmentFirstAndLastName.onEditorAction===="+"Yesssssss");
                }
                return false;
            }
        });

        editTextMobileNUmber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editTextMobileNUmber.getText().toString().isEmpty()){
                    textInputLayoutMobileNumber.setError("Invalid mobile");
                }else{
                    textInputLayoutMobileNumber.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return  createView;
    }

    public boolean isFirstNameEmpty(){
        if(editTextName.getText().toString().isEmpty() || editTextName.getText().toString().length()<2){
            return true;
        }
        signUpData.setName(editTextName.getText().toString());
        return false;
    }

    public  void setError(){
        textInputLayoutFirstName.setError("Invalid first name.");
    }

    public boolean isEmailEmpty()
    {
        signUpData.setEmailId(editTextEmail.getText().toString());

        if(IsValidField()){
            return false;
        }else{
            return true;
        }
    }

    private String emailPATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z]+(\\.[A-Za-z]+)*(\\.[A-Za-z]{2,3})$";


    public boolean IsValidField()
    {

        String text = editTextEmail.getText().toString().trim();

        editTextEmail.setError(null);

        if (!HasSomeText() ) return false;

        if (!Pattern.matches(emailPATTERN, text))
        {
            return false;
        }
        if(text.length() !=0 && !Pattern.matches(emailPATTERN, text)){
            return false;
        }
        return true;
    }

    public boolean HasSomeText()
    {
        String text = editTextEmail.getText().toString().trim();
        textInputLayoutemail.setError(null);
        if (text.length() == 0)
        {
            return false;
        }
        textInputLayoutemail.setErrorEnabled(false);
        return true;
    }

    public  void setErrorEmail(){
        textInputLayoutemail.setError("Invalid email.");
    }

    public  void setErrorMobile(){
        textInputLayoutMobileNumber.setError("Invalid mobile");
    }

    public boolean isValidMobile(){
        if(editTextMobileNUmber.getText().toString().isEmpty()){
            return false;
        }else if(!(editTextMobileNUmber.getText().toString().length()==10)){
            return false;
        }else{
            signUpData.setmobileNumber(editTextMobileNUmber.getText().toString());
            return true;
        }
    }



}
