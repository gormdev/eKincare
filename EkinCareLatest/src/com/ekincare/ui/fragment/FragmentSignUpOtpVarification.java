package com.ekincare.ui.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ekincare.R;
import com.ekincare.util.SignUpDataInterface;

import java.util.concurrent.TimeUnit;


/**
 * Created by RaviTejaN on 13-10-2016.
 */

public class FragmentSignUpOtpVarification extends Fragment {
    View createView;
    SignUpDataInterface signUpData;
    EditText otpValue;
    TextView btnResendOTP;
    TextView timeleftOtp;
    TextView resendMessage;
    LinearLayout hideSmsTimeLeft;

    CountDownTimer countDownTimer;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        signUpData = (SignUpDataInterface) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        createView = inflater.inflate(R.layout.fragment_signup_otp_validation, container, false);

        signUpData.hideBackButton();

        init();

        return  createView;
    }

    @Override
    public void onStop() {
        super.onStop();
        countDownTimer.cancel();
    }

    private void init() {
        otpValue = (EditText)createView.findViewById(R.id.otp_value_enter);
        btnResendOTP=(TextView)createView.findViewById(R.id.resend_otp);
        timeleftOtp=(TextView)createView.findViewById(R.id.timer_for_otp);
        hideSmsTimeLeft=(LinearLayout)createView.findViewById(R.id.hide_sms_time_left);
        resendMessage=(TextView)createView.findViewById(R.id.resend_otp_message);

        countDownTimer = new CountDownTimer(30000, 1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                timeleftOtp.setText("Waiting in "+"" +String.format("%d seconds ",
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

               /* timeleftOtp.setText(""+String.format("%d : %d sec ",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
        */    }

            public void onFinish() {
                hideSmsTimeLeft.setVisibility(View.GONE);
                timeleftOtp.setVisibility(View.INVISIBLE);
                resendMessage.setVisibility(View.INVISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btnResendOTP.setBackground( getResources().getDrawable(R.drawable.pkg_select_btn));
                }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
                    btnResendOTP.setBackgroundDrawable( getResources().getDrawable(R.drawable.pkg_select_btn) );
                }
                btnResendOTP.setPadding(getResources().getDimensionPixelOffset(R.dimen.margin_5),
                        getResources().getDimensionPixelOffset(R.dimen.margin_5),
                        getResources().getDimensionPixelOffset(R.dimen.margin_5),
                        getResources().getDimensionPixelOffset(R.dimen.margin_5));
                btnResendOTP.setTextColor(getResources().getColor(R.color.white));
                btnResendOTP.setVisibility(View.VISIBLE);
                resendMessage.setVisibility(View.GONE);
            }
        }.start();

        btnResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpData.resendOtp();
            }
        });
    }

    public boolean isValidOtp(){
        if(otpValue.getText().toString().isEmpty()){
            return false;
        }else if((otpValue.getText().toString().length()==6)){
            signUpData.setOtp(otpValue.getText().toString());
            return true;
        }else{
            return false;
        }
    }

    public void setOtpToEditText(String otp){
        countDownTimer.cancel();
        otpValue.setText(otp);
        countDownTimer.cancel();

        hideSmsTimeLeft.setVisibility(View.GONE);
        btnResendOTP.setVisibility(View.GONE);
        hideSmsTimeLeft.setVisibility(View.GONE);
    }

}
