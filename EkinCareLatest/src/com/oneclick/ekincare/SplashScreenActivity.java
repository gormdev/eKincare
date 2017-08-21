package com.oneclick.ekincare;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ekincare.R;
import com.ekincare.ui.fragment.WalkthroughOne;
import com.ekincare.ui.fragment.WalkthroughThree;
import com.ekincare.ui.fragment.WalkthroughTwo;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.oneclick.ekincare.helper.PreferenceHelper;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;

/**
 * Created by RaviTejaN on 21-07-2016.
 */
public class SplashScreenActivity extends FragmentActivity {
    private MyAdapter mAdapter;
    private ViewPager mPager;
    ImageButton circleOne, circleTwo,circleThree;
    TextView skipButton,registerButton,signInButton;
    LinearLayout registerSigninButtonsView,skpiView;
    private PreferenceHelper prefs;
    private static final int PERMISSION_REQUEST_CODE = 200;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.slapsh_screen);
        prefs = new PreferenceHelper(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        registerSigninButtonsView=(LinearLayout)findViewById(R.id.register_signin_buttons_view);
        skpiView=(LinearLayout)findViewById(R.id.skipButton_view);
        skipButton=(TextView)findViewById(R.id.skipButton);
        registerButton=(TextView)findViewById(R.id.btnRegister);
        signInButton=(TextView)findViewById(R.id.already_account_sign_in) ;
        circleOne = (ImageButton) findViewById(R.id.circle_one);
        circleTwo = (ImageButton) findViewById(R.id.circle_two);
        circleThree=(ImageButton)findViewById(R.id.circle_three);



        try {
            PackageManager pm = getApplicationContext().getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo("com.ekincare", PackageManager.GET_PERMISSIONS);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String installTimenew = dateFormat.format( new Date( packageInfo.firstInstallTime ) );
            prefs.setInstallTime(installTimenew);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(!prefs.getIsLogin()){
            printHashKey();
            mAdapter = new MyAdapter(getSupportFragmentManager());
            mPager = (ViewPager) findViewById(R.id.pager);
            mPager.setAdapter(mAdapter);
            mPager.setOffscreenPageLimit(1);
            mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    switch (position) {
                        case 0:
                            registerSigninButtonsView.setVisibility(View.GONE);
                            skpiView.setVisibility(View.VISIBLE);
                            circleOne.setBackgroundResource(R.drawable.black_circle);
                            circleTwo.setBackgroundResource(R.drawable.white_circle);
                            circleThree.setBackgroundResource(R.drawable.white_circle);
                            break;
                        case 1:
                            registerSigninButtonsView.setVisibility(View.GONE);
                            skpiView.setVisibility(View.VISIBLE);
                            circleOne.setBackgroundResource(R.drawable.white_circle);
                            circleTwo.setBackgroundResource(R.drawable.black_circle);
                            circleThree.setBackgroundResource(R.drawable.white_circle);
                            break;
                        case 2:
                            skpiView.setVisibility(View.GONE);
                            registerSigninButtonsView.setVisibility(View.VISIBLE);
                            circleOne.setBackgroundResource(R.drawable.white_circle);
                            circleTwo.setBackgroundResource(R.drawable.white_circle);
                            circleThree.setBackgroundResource(R.drawable.black_circle);

                        default:
                            break;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }else{
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        //Skpi Button
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(2);
            }
        });

        //Go to Register Page
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkSignUpPermission();



            }
        });

        //Go to LoginPage
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }


    private void checkSignUpPermission() {
        Intent intent = new Intent(SplashScreenActivity.this, ActivitySignUpTest.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void printHashKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.ekincare",  PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures)
            {

                MessageDigest md;

                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));


            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new WalkthroughOne();
                case 1:
                    return new WalkthroughTwo();
                case 2:
                    return new WalkthroughThree();
               default:
                    return null;
            }
        }
    }
}