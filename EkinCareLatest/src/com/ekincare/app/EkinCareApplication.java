package com.ekincare.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.ekincare.receiver.ConnectivityReceiver;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.ProfileData;

import java.io.File;

/**
 * Created by jhansi on 28/12/14.
 */
public class EkinCareApplication extends Application {


    private static EkinCareApplication mInstance;

    public Customer getCustomer() {
        ProfileManager profileManager = ProfileManager.getInstance(this);
        ProfileData profileData = profileManager.getProfileData();

        if (profileData == null)
            throw new IllegalStateException("No Profile Data Object to find Customer");

        Customer customer = profileData.getCustomer();

        if (customer == null)
            throw new IllegalStateException("No customer object is found");

        return customer;
    }


    @SuppressLint("UseSparseArrays")
    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

//		AppSingleton.getInstance().SetEkinCareApplication(this);
        //		objectGraph = ObjectGraph.create(getModulesDB().toArray());
        //		objectGraph.inject(this);

        //		objectGraph.inject(this);
        //		objectGraph = ObjectGraph.create(this);
        //		objectGraph.inject(this);

//		SugarContext.init(this);

//		WebServiceConstants.SCORE_MAP = new HashMap<Integer, Integer>();
        buildeKincareDirectory();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void buildeKincareDirectory() {
        buildDirectory(Environment.getExternalStorageDirectory()
                .getPath() + "/eKincare");
        buildDirectory(AppConstants.IMAGE_PATH);
        buildDirectory(AppConstants.PDF_PATH);
    }

    private void buildDirectory(String path) {
        File directory = new File(path);

        if (!directory.exists())
            directory.mkdir();
    }

    public static synchronized EkinCareApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    private static boolean isChatFragmentVisible = false;
    public static boolean isActivityVisible() {
        return isChatFragmentVisible;
    }

    public static void setActivityVisible(boolean flag) {
        isChatFragmentVisible = flag;
    }

}
