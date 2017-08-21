package com.ekincare.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.Settings;

import com.ekincare.R;
import com.oneclick.ekincare.vo.Customer;

/**
 * Created by RaviTejaN on 20-07-2016.
 */
public class CustomerManager {


    Customer mCustomer;
    private Customer currentCustomer;
    private Customer familyMember;
    public static CustomerManager instance=null;
    protected Context context;
    private boolean isLoggedInCustomer;
    private ProfileManager profileManager;
    public boolean isWizardShowing = false;
    public boolean isFromLogin = false;



    private CustomerManager(Context mContext) {
        this.context = mContext;
        this.isLoggedInCustomer = true;
        this.profileManager = new ProfileManager(context);
    }

    public static CustomerManager getInstance(Context context) {
        if (instance == null) {
            instance = new CustomerManager(context);
        }
        return instance;
    }


    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public boolean isLoggedInCustomer() {
        if (this.getCurrentCustomer() == null) {
            return true;
        } else {
            return isLoggedInCustomer;
        }

    }

    public void setLoggedInCustomer(boolean isLoggedInCustomer) {
        this.isLoggedInCustomer = isLoggedInCustomer;
    }

    public Customer getCurrentCustomer() {
        if(currentCustomer == null) {
           this.currentCustomer =  profileManager.getLoggedinCustomer();
        }
            return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
        if (currentCustomer.getIdentification_token()
                .equals(profileManager.getLoggedinCustomer().getIdentification_token())) {
            this.isLoggedInCustomer = true;
            mCustomer = currentCustomer;
        } else {
            this.isLoggedInCustomer = false;


        }
    }

    public Customer getCurrentFamilyMember() {
        if(familyMember == null) {
            return null;
        }
        return familyMember;
    }

    public void setCurrentFamilyMember(Customer familyCustomer) {
        this.familyMember = familyCustomer;
        if (familyMember.getIdentification_token().equals(profileManager.getLoggedinCustomer().getIdentification_token())) {
            this.isLoggedInCustomer = true;
            mCustomer = currentCustomer;
        } else {
            this.isLoggedInCustomer = false;

        }
    }




    public void showAlert(String message, Context mContext) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        mBuilder.setTitle(mContext.getResources().getString(R.string.app_name));
        mBuilder.setMessage(message);
        mBuilder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mBuilder.create();
        mBuilder.show();
    }

    public final boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static CustomerManager getInstance() {
        return instance;
    }

    public static void setInstance(CustomerManager instance) {
        CustomerManager.instance = instance;
    }

}
