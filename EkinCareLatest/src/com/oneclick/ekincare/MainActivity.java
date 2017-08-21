package com.oneclick.ekincare;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DataStorage.DatabaseOverAllHandler;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.appsflyer.AppsFlyerLib;
import com.ekincare.R;
import com.ekincare.app.CustomerManager;
import com.ekincare.app.EkinCareApplication;
import com.ekincare.app.ProfileManager;
import com.ekincare.app.VolleyRequestSingleton;
import com.ekincare.ui.custom.CustomViewPager;
import com.ekincare.ui.fragment.FragmentDashboardProfileTest;
import com.ekincare.ui.fragment.FragmentDashboardRisks;
import com.ekincare.ui.fragment.FragmentDummy;
import com.ekincare.util.DataUploadSyncService;
import com.ekincare.util.HydrocareHelperClass;
import com.ekincare.util.SleepPatternService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.message.FragmentChat;
import com.message.custominterface.NotificationDialogClickEvent;
import com.message.dialog.NotificationMessageDialog;
import com.message.messageitems.IncomingTextMessage;
import com.message.messageitems.OutgoingImageMessage;
import com.message.model.ChatMessage;
import com.message.model.WalletResponse;
import com.message.utility.Config;
import com.message.utility.MediaUtils;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.oneclick.ekincare.helper.CustomeDialog;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.helper.ThreadAsyncTask;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.UploadDocumentData;
import com.openNoteScanner.OpenNoteScannerActivity;
import com.openNoteScanner.helpers.OpenNoteScannerAppConstant;
import com.razorpay.PaymentResultListener;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by RaviTejaN on 20-07-2016.
 */
public class MainActivity extends AppCompatActivity implements PaymentResultListener, NotificationDialogClickEvent {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int PICKFILE_RESULT_CODE = 1;

    ChatMessage chatMessageCamera;

    public RelativeLayout mNotificationView;
    public ImageView notificationIconnn, arrowIcon;
    public RelativeLayout relativeLayoutWallet;
    public TextView textViewWalletMoney;
    public TextView notificationCountValue;
    public LinearLayout switchFamilyMemberLayout;
    protected ProfileManager profileManager;
    HttpEntity entity;
    Dialog mAlert_Dialog;
    CircleProgressBar progressWithArrow;
    UploadDocumentData mUploadDocumentData;


    public Toolbar mToolbar;


    String childOk, adultOk, childToken, notificationOK, notificationTypePage;
    GoogleCloudMessaging gcm;
    Context context;
    String regId = "";
    ArrayList<Header> headerList;

    private CustomerManager customerManager;
    private PreferenceHelper prefs;


    public ImageLoader imageLoader;
    public DisplayImageOptions options;
    ProfileManager mProfileManager;

    DatabaseOverAllHandler dbHandler;
    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    MixpanelAPI mixpanel;
    private String uploadDoc = " ";

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder build;
    private int[] tabIcons = {
            R.drawable.ic_home_first_tab,
            R.drawable.ic_home_second_tab,
            R.drawable.ic_home_thired_tab
    };
    TextView loginUserProfileName;
    Bundle bundle;
    String updateBlood;

    String mStringFamilyMemberKey;
    private Fragment currentFragment;
    private ViewPagerAdapter adapter;
    String firstTimeRegister;
    String firstTimeDate;
    private boolean isLastArrayListImage = false;
    private int cameraMessagePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_main_tab_new);
        context = getApplicationContext();

        dbHandler = new DatabaseOverAllHandler(this);
        mProfileManager = ProfileManager.getInstance(this);
        profileManager = ProfileManager.getInstance(MainActivity.this);
        customerManager = CustomerManager.getInstance(this);
        prefs = new PreferenceHelper(this);
        System.out.println("Ekincare=============" + prefs.getEkinKey() + "customer=====" + prefs.getCustomerKey());
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        if (savedInstanceState == null && customerManager.getCurrentCustomer() != null) {
            customerManager.setCurrentFamilyMember(customerManager.getCurrentCustomer());
        }

        try {
            if (dbHandler.getSyncData().size() > 0) {
                Intent i = new Intent(context, DataUploadSyncService.class);
                startService(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Intent i = new Intent(context, SleepPatternService.class);
        startService(i);

        HydrocareHelperClass.setAlarm(MainActivity.this, 0, System.currentTimeMillis(), 1 * 60 * 1000);

        setUpMixPanel();

        initializeView();

        setupToolbar();

        setupViewPager();
        viewPager.setOffscreenPageLimit(3);
        currentFragment = adapter.getItem(0);

        setupTabView();

        System.out.println("MainActivity.onCreate=========" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));

        bundle = getIntent().getExtras();
        if (bundle != null) {

            firstTimeRegister = bundle.getString("firstTimeRegister", "");
            firstTimeDate = bundle.getString("RegisterDate", "");
            notificationTypePage = bundle.getString("NotificationTypePage", "");
            notificationOK = bundle.getString("NotificationClick", "");
            uploadDoc = bundle.getString(OpenNoteScannerAppConstant.UPLOAD_DOC, "");
            updateBlood = bundle.getString("UPDATEBLOOD", "");
        }
        System.out.println("uploadDoc======" + uploadDoc);

        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.circular_progressbar)
                .showImageForEmptyUri(R.drawable.ic_launcher).showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();

        //setUpRatingBar();
        System.out.println("NotificationOK======" + notificationOK);

       /* if (notificationOK != null) {
            if (!notificationOK.isEmpty()) {

                System.out.println("Yess NotificationOK======" + notificationOK);

                NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationmanager.cancel(0);
                notificationmanager.cancelAll();

                notificationTitle = getIntent().getStringExtra("notificationTitle");
                notificationDiscription = getIntent().getStringExtra("notificationSubtitle");
                notificationFlag = getIntent().getBooleanExtra("notificationActive", false);

                switchScreenFromGcmNotificationType(getIntent().getStringExtra("notificationType"), notificationTitle, notificationDiscription, getIntent().getStringExtra("notificationIdentificationToken"), getIntent().getStringExtra("notificationButtonText"), getIntent().getStringExtra("notificationButtonAction"));

            }
        }*/

        //////////////////////////GCM///////////////////////////
        try {
            String notificationTitle = bundle.getString("notification_title");
            String notificationDiscription = bundle.getString("notification_message");
            String notificationChatbotPrimaryText = bundle.getString("notification_chatbot_primary_text");
            String notificationChatbotPrimaryAction = bundle.getString("notification_chatbot_primary_action");
            String notificationChatbotSecondaryText = bundle.getString("notification_chatbot_secondary_text");
            String notificationChatbotSecondaryAction = bundle.getString("notification_chatbot_secondary_action");
            String notificationImageUrl = bundle.getString("notification_image_url");
            String notificationType=bundle.getString("notification_type");
            notificationOK = bundle.getString("NotificationClick");

            System.out.println("MainActivity.onCreate notificationTitle=" + notificationTitle);
            System.out.println("MainActivity.onCreate notificationDiscription=" + notificationDiscription);
            System.out.println("MainActivity.onCreate notificationChatbotPrimaryText="+notificationChatbotPrimaryText);
            System.out.println("MainActivity.onCreate notificationChatbotPrimaryAction="+notificationChatbotPrimaryAction);
          if (bundle.getString("notification_title")!= null &&!bundle.getString("notification_title").isEmpty()) {
              if(bundle.getString("notification_title")!= null){
                  System.out.println("testtt========="+"2");
                  NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                  notificationmanager.cancel(0);
                  notificationmanager.cancelAll();
                  LocalNotificationDialog newFragment = new LocalNotificationDialog(MainActivity.this,
                          MainActivity.this,
                          notificationTitle,
                          notificationDiscription,
                          notificationChatbotPrimaryText,
                          notificationChatbotPrimaryAction,
                          notificationChatbotSecondaryText,
                          notificationChatbotSecondaryAction,
                          notificationType);
                  newFragment.show(getSupportFragmentManager(), "");
              }else{
                  switchScreenFromGcmNotificationType(notificationTitle,
                          notificationDiscription,
                          notificationChatbotPrimaryText,
                          notificationChatbotPrimaryAction,
                          notificationChatbotSecondaryText,
                          notificationChatbotSecondaryAction);
              }
            }else{
                System.out.println("testtt========="+"3");
                System.out.println("yesNp==========="+"ok");
                Intent intent = new Intent(MainActivity.this, ActivitySetting.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        LocalBroadcastManager.getInstance(this).registerReceiver(mGCMNotificationReceiver, new IntentFilter("newNotificationCame"));

        switchFamilyMemberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGoogleApiClient != null)
                    mGoogleApiClient.stopAutoManage(MainActivity.this);
                googleApiClientId = googleApiClientId + 2;

                Intent intent = new Intent(MainActivity.this, ActivityFamilyMemberSwitch.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }

        });

        //versionCheckRequest();

        mNotificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, ActivityNotificationList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                }, 300);
            }
        });

        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (prefs.getIsGoogleFitConnected()) {
                    setUpGoogleFitCard();
                }
            }
        }, 1000);

    }

    private void initializeView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        switchFamilyMemberLayout = (LinearLayout) findViewById(R.id.activity_add_family_member_layout);
        switchFamilyMemberLayout.setVisibility(View.GONE);
        loginUserProfileName = (TextView) findViewById(R.id.profile_switcher_name);
        loginUserProfileName.setText("eKincare");
        loginUserProfileName.setVisibility(View.VISIBLE);
        mNotificationView = (RelativeLayout) findViewById(R.id.activity_notification);
        relativeLayoutWallet = (RelativeLayout) findViewById(R.id.activity_wallet);
        textViewWalletMoney = (TextView) findViewById(R.id.wallet_money);
        //mNotificationView.setVisibility(View.VISIBLE);
        relativeLayoutWallet.setVisibility(View.VISIBLE);
        notificationCountValue = (TextView) findViewById(R.id.notification_count_lable);
        notificationIconnn = (ImageView) findViewById(R.id.notification_iconnn);
        int color = Color.parseColor("#ffffff");
        notificationIconnn.setColorFilter(color);
        arrowIcon = (ImageView) findViewById(R.id.arrow_icon);
        int color1 = Color.parseColor("#FFFFFF");
        arrowIcon.setColorFilter(color1);
        arrowIcon.setVisibility(View.GONE);


        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
    }

    public void setPage(int position) {
        viewPager.setCurrentItem(position);
    }

    public void showWalletMoney(boolean showWallet, WalletResponse walletResponse) {
        if (showWallet) {
            relativeLayoutWallet.setVisibility(View.VISIBLE);
            textViewWalletMoney.setText(getResources().getString(R.string.Rs) + " " + walletResponse.getWallet().getBalance() + "/-");
        } else {
           /* relativeLayoutWallet.setVisibility(View.GONE);
            textViewWalletMoney.setText("");*/
            relativeLayoutWallet.setVisibility(View.VISIBLE);
            textViewWalletMoney.setText(getResources().getString(R.string.Rs) + " " + walletResponse.getWallet().getBalance() + "/-");

        }
    }

    public void showNotificationCount(String count) {
        System.out.println("count========"+count);
        if (count.equalsIgnoreCase("0")) {
            mNotificationView.setVisibility(View.GONE);

        } else {
            mNotificationView.setVisibility(View.VISIBLE);
            notificationCountValue.setVisibility(View.VISIBLE);
            notificationIconnn.setVisibility(View.VISIBLE);
            notificationCountValue.setText(count);
        }

    }

    private void setUpMixPanel() {
        mixpanel = MixpanelAPI.getInstance(this, TagValues.MIXPANEL_TOKEN);
        mixpanel.getPeople().initPushHandling("1006237285130");
        mixpanel.getPeople().getNotificationIfAvailable();
        mixpanel.getPeople().showNotificationIfAvailable(this);

        AppsFlyerLib.getInstance().startTracking(this.getApplication(), "7wLBbm2LnVq6MrqvTneNzh");
        AppsFlyerLib.getInstance().sendDeepLinkData(this);//reporting deeplinking for multipleactivities

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        File httpCacheDir = new File(getApplicationContext().getCacheDir(), "http");

        long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
        try {
            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, httpCacheSize);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void uploadMultipleReport(Bitmap bm, boolean isFirst, boolean isLast) {
        System.out.println("MainActivity.uploadMultipleReport");
        if (NetworkCaller.isInternetOncheck(MainActivity.this)) {
            mUploadDocumentData = new UploadDocumentData();
            getUploadMultipleReportSatus(TagValues.Get_All_Documents_URL, mUploadDocumentData, bm, isFirst, isLast);
        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.internet_not_available), Toast.LENGTH_SHORT).show();
        }
    }

    public void getUploadMultipleReportSatus(String methodName, Object mObject, Bitmap bm, boolean isFirst, boolean isLast) {
        System.out.println("MainActivity.getUploadMultipleReportSatus prefs.getIsMainDoc()=" + prefs.getIsMainDoc());
        try {
            if (prefs.getIsMainDoc()) {
                mStringFamilyMemberKey = "";
            } else {
                if (this.customerManager.isLoggedInCustomer()) {
                    mStringFamilyMemberKey = "";
                } else {
                    mStringFamilyMemberKey = this.customerManager.getCurrentFamilyMember().getIdentification_token();
                }
            }

            headerList = new ArrayList<>();
            headerList.add(new BasicHeader("X-CUSTOMER-KEY", prefs.getCustomerKey()));
            headerList.add(new BasicHeader("X-EKINCARE-KEY", prefs.getEkinKey()));
            headerList.add(new BasicHeader("X-DEVICE-ID", customerManager.getDeviceID(this)));

            if (!mStringFamilyMemberKey.equalsIgnoreCase(""))
                headerList.add(new BasicHeader("X-FAMILY-MEMBER-KEY", mStringFamilyMemberKey));


            if (currentFragment instanceof FragmentChat && prefs.getIsMainDoc()) {
                ChatMessage chatImageMessage = new ChatMessage();
                OutgoingImageMessage outgoingImageMessage = new OutgoingImageMessage("", bm);
                outgoingImageMessage.setStatus(Config.MESSAGE_DEIVERED);
                chatImageMessage.setMessageItem(outgoingImageMessage);
                ((FragmentChat) currentFragment).addMessageToQueue(chatImageMessage);
            }

            if (prefs.getIsMainDoc() && isLastArrayListImage) {
                viewPager.setCurrentItem(0);
                if (currentFragment instanceof FragmentChat) {
                    String text = "Your document is being uploaded";
                    IncomingTextMessage incomingTextMessage = ((FragmentChat) currentFragment).createIncomingTextObject(text);
                    chatMessageCamera.setMessageItem(incomingTextMessage);

                    ((FragmentChat) currentFragment).addMessageToQueue(chatMessageCamera);

                    System.out.println("MainActivity.getUploadMultipleReportSatus FragmentChat");


                    cameraMessagePosition = ((FragmentChat) currentFragment).getRecyclerCount();
                }
            } else if (!prefs.getIsMainDoc() && isFirst) {
                viewPager.setCurrentItem(1);
                Toast.makeText(MainActivity.this, "Your document is being uploaded", Toast.LENGTH_LONG).show();
            }


            ThreadAsyncTask testImple = new ThreadAsyncTask(listenerUpload, MainActivity.this, mObject, null, methodName, null, "", false, headerList, entity);
            testImple.execute("");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void showDialog(String errorMessage, String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(title);
        builder.setMessage(errorMessage);

        String positiveText = "Okay";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        // display dialog
        dialog.show();
    }


    private ThreadAsyncTask.OnTaskCompleted listenerUpload = new ThreadAsyncTask.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(String method, Object result) {
            System.out.println("MainActivity.onTaskCompleted");
            if (result != null) {
                mUploadDocumentData = (UploadDocumentData) result;
                if (mUploadDocumentData != null) {
                    System.out.println("uploadIdID========="+mUploadDocumentData.getDocument().getId());

                    if (isLastArrayListImage) {
                        if (mUploadDocumentData.getMessage() != null) {
                            dbHandler.clearDocuments();
                            try {
                                if (prefs.getIsMainDoc()) {
                                    try {
                                        viewPager.setCurrentItem(0);
                                        if (currentFragment instanceof FragmentChat) {
                                            String text = "Your profile will be updated with the new information within " +
                                                    "24 hours and will be notified once it's complete";
                                            IncomingTextMessage incomingTextMessage = ((FragmentChat) currentFragment).createIncomingTextObject(text);
                                            //((FragmentChat) currentFragment).addMessageToQueue(chatMessageCamera);
                                            chatMessageCamera.setMessageItem(incomingTextMessage);
                                            ((FragmentChat) currentFragment).mRecyclerAdapter.update(cameraMessagePosition, chatMessageCamera);

                                            isLastArrayListImage = false;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        viewPager.setCurrentItem(1);

                                        dbHandler.clearDocuments();

                                        if (currentFragment instanceof FragmentDashboardProfileTest) {
                                            ((FragmentDashboardProfileTest) currentFragment).refreshDocumentData();
                                        }
                                        showDialog("Your profile will be updated with the new information " +
                                                "within 24 hours and will be notified once it's complete", getResources().getString(R.string.ekinCare));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    System.out.println("MainActivity.onTaskCompleted isLastArrayListImage=" + isLastArrayListImage);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                for (File file : new java.io.File(OpenNoteScannerAppConstant.IMAGE_PATH).listFiles()) {
                                    if (!file.isDirectory()) {
                                        file.delete();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else {
                // showAlert(getResources().getString(R.string.no_documents_available), MainActivity.this);
            }
        }
    };


    public void uploadDialog() {
        final Dialog Alert_Dialog = new Dialog(this);
        Alert_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Alert_Dialog.setContentView(R.layout.dailog_upload_file_may);
        Alert_Dialog.setCancelable(true);
        Alert_Dialog.setCanceledOnTouchOutside(false);
        Alert_Dialog.show();

        LinearLayout cameraLayout = (LinearLayout) Alert_Dialog.findViewById(R.id.camera_layout);
        LinearLayout imageFile = (LinearLayout) Alert_Dialog.findViewById(R.id.file_layout);

        cameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.setPREF_RUNTIME_PERMISSION("Camera");
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!checkPermission()) {
                        requestPermission();
                    } else {
                        prefs.setIsMainDoc(true);
                        Intent intent = new Intent(MainActivity.this, OpenNoteScannerActivity.class);
                        startActivity(intent);
                    }
                } else {
                    prefs.setIsMainDoc(true);
                    Intent intent = new Intent(MainActivity.this, OpenNoteScannerActivity.class);
                    startActivity(intent);
                }
                Alert_Dialog.dismiss();
            }
        });

        imageFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.setPREF_RUNTIME_PERMISSION("File");
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!checkPermission()) {
                        requestPermission();
                    } else {
                        readfileMemory();
                    }
                } else {
                    readfileMemory();
                }
                Alert_Dialog.dismiss();
            }
        });


        ((TextView) Alert_Dialog.findViewById(R.id.dismiss)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Alert_Dialog.dismiss();
            }
        });

    }


    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void readfileMemory() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission();
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("*/*");
                startActivityForResult(intent, PICKFILE_RESULT_CODE);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("*/*");
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean readAccept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccept = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean camerAccept = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (readAccept && writeAccept && camerAccept) {
                        if (prefs.getPREF_RUNTIME_PERMISSION().equals("Camera")) {
                            prefs.setIsMainDoc(true);
                            Intent intent = new Intent(MainActivity.this, OpenNoteScannerActivity.class);
                            startActivity(intent);

                            // startActivityForResult(new Intent(MainActivity.this, ScanActivity.class), SCAN_PICTURE_REQUEST_B);
                        } else if (prefs.getPREF_RUNTIME_PERMISSION().equals("File")) {

                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            intent.setType("*/*");
                            startActivityForResult(intent, PICKFILE_RESULT_CODE);
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied, You cannot access  camera.", Toast.LENGTH_SHORT).show();

                    }
                }


                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, ActivitySetting.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.signOut:
                signOut();
                return true;

        }
        return true;
    }

    private void signOut() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("");
        builder.setMessage("Do you want to sign out?");

        String positiveText = "Yes";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        prefs.ClearAllData();
                        dbHandler.clearDatabase(true);
                        dbHandler.clearTimeline();
                        dbHandler.clearAllChat();
                        CustomerManager.setInstance(null);
                        profileManager.setInstance(null);
                        dialog.dismiss();
                        try {
                            mGoogleApiClient.clearDefaultAccountAndReconnect();

                            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                                mGoogleApiClient.stopAutoManage(MainActivity.this);
                                mGoogleApiClient.disconnect();
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        String negativeText = "No";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        // display dialog
        dialog.show();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("MainActivity.onActivityResult");
    }


    public void onRestart() {
        super.onRestart();
    }

    public void onResume() {
        super.onResume();
        EkinCareApplication.setActivityVisible(true);
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(context);
            regId = prefs.getRegistrationId();
            if (regId.isEmpty()) {
                registerInBackground();
            } else {

            }

        }

        /*try{
            int numOfMissedMessages = 0;
            String newMessage = getMessage(numOfMissedMessages);
            if (!newMessage.trim().equalsIgnoreCase("") && newMessage != "") {
                Log.i("displaying message", newMessage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/

    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }

                    String googleProjectId = getResources().getString(R.string.project_number);
                    regId = gcm.register(googleProjectId);
                    Log.d("RegisterGcmIdInfo", "registerInBackground - regId: " + regId);
                    System.out.println("registerInBackground=============" + regId);
                    msg = "Device registered, registration ID=" + regId;

                    prefs.storeRegistrationId(context, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterGcmIdInfo", "Error: " + msg);
                }
                Log.d("RegisterGcmIdInfo", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    prefs.storeRegistrationId(context, regId);

                } else {

                }
            }
        }.execute(null, null, null);
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        } else {

        }
        return true;
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("MainActivity.onNewIntent");
        setIntent(intent);

        //////////////////////////GCM///////////////////////////
        try {
            String notificationTitle = bundle.getString("notification_title");
            String notificationDiscription = bundle.getString("notification_message");
            String notificationChatbotPrimaryText = bundle.getString("notification_chatbot_primary_text");
            String notificationChatbotPrimaryAction = bundle.getString("notification_chatbot_primary_action");
            String notificationChatbotSecondaryText = bundle.getString("notification_chatbot_secondary_text");
            String notificationChatbotSecondaryAction = bundle.getString("notification_chatbot_secondary_action");
            String notificationImageUrl = bundle.getString("notification_image_url");
            notificationOK = bundle.getString("NotificationClick");

            System.out.println("MainActivity.onCreate notificationTitle=" + notificationTitle);
            System.out.println("MainActivity.onCreate notificationDiscription=" + notificationDiscription);
            System.out.println("MainActivity.onCreate notificationChatbotPrimaryText="+notificationChatbotPrimaryText);
            System.out.println("MainActivity.onCreate notificationChatbotPrimaryAction="+notificationChatbotPrimaryAction);

            if (!bundle.getString("notification_title").isEmpty()) {
                NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationmanager.cancel(0);
                notificationmanager.cancelAll();

                switchScreenFromGcmNotificationType(notificationTitle,
                        notificationDiscription,
                        notificationChatbotPrimaryText,
                        notificationChatbotPrimaryAction,
                        notificationChatbotSecondaryText,
                        notificationChatbotSecondaryAction);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String cameraFailure = intent.getStringExtra("camera_closed");
            System.out.println("MainActivity.onNewIntent cameraFailure=" + cameraFailure);
            if (cameraFailure != null && prefs.getIsMainDoc()) {
                FragmentChat fragmentChat = (FragmentChat) viewPager.getAdapter().instantiateItem(viewPager, 0);
                fragmentChat.setCameraError(cameraFailure);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //////////////////GOOGLE FIT/////////////
        try {
            firstTimeRegister = intent.getStringExtra("firstTimeRegister");
            firstTimeDate = intent.getStringExtra("RegisterDate");
            if (getIntent().getStringExtra("GOOGLE_FIT").equals("GOOGLE_FIT")) {
                System.out.println("MainActivity.onNewIntent GOOGLE_FIT");

                FragmentDashboardRisks fragmentDashboardRisks = (FragmentDashboardRisks) viewPager.getAdapter().instantiateItem(viewPager, 2);
                if (getIntent().getBooleanExtra("isGFitConnected", false)) {
                    if (mGoogleApiClient != null) {
                        fragmentDashboardRisks.isGoogleFitConnected(true, getString(R.string.google_fit_connected_body_text));
                        callGFitData();
                    } else {
                        System.out.println("MainActivity.onNewIntent mGoogleApiClient is null");
                        setUpGoogleFitCard();
                    }
                } else {
                    fragmentDashboardRisks.isGoogleFitConnected(false, getString(R.string.google_fit_not_connected_body_text));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ///////////////////SWITCH LOGIC///////////////////////
        try {
            if (intent.getBooleanExtra("SWITCH_FAMILY", false)) {
                loginUserProfileName.setText(customerManager.getCurrentFamilyMember().getFirst_name().substring(0, 1).toUpperCase() +
                        customerManager.getCurrentFamilyMember().getFirst_name().substring(1) + " " +
                        customerManager.getCurrentFamilyMember().getLast_name());

                System.out.println("MainActivity.onNewIntent loginUserProfileName=" + loginUserProfileName.getText().toString());

                FragmentDashboardProfileTest fragmentDashboardProfileTest = (FragmentDashboardProfileTest) viewPager.getAdapter().instantiateItem(viewPager, 1);
                fragmentDashboardProfileTest.refreshTab(0, mAlert_Dialog);

                FragmentDashboardRisks fragmentDashboardRisks = (FragmentDashboardRisks) viewPager.getAdapter().instantiateItem(viewPager, 2);
                fragmentDashboardRisks.refreshRiskFragment();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //////////////////////PROFILE EDIT///////////////////////
        try {
            if (getIntent().getBooleanExtra("PROFILE_EDIT", false)) {
                System.out.println("MainActivity.onNewIntent PROFILE_EDIT");
                loginUserProfileName.setText(customerManager.getCurrentFamilyMember().getFirst_name().substring(0, 1).toUpperCase() +
                        customerManager.getCurrentFamilyMember().getFirst_name().substring(1) + " " +
                        customerManager.getCurrentFamilyMember().getLast_name());

                FragmentDashboardProfileTest fragmentDashboardProfileTest = (FragmentDashboardProfileTest) viewPager.getAdapter().instantiateItem(viewPager, 1);
                fragmentDashboardProfileTest.refreshProfileData();

                viewPager.setCurrentItem(1);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ///////////////////////////ACTIVATE MEDICATION////////////////////////////
        try {
            if (intent.getBooleanExtra("ACTIVATE_MEDICATION", false)) {
                System.out.println("MainActivity.onNewIntent ACTIVATE_MEDICATION");
                viewPager.setCurrentItem(1);
                if (currentFragment instanceof FragmentDashboardProfileTest) {
                    ((FragmentDashboardProfileTest) currentFragment).refreshMedicationData();
                }
                switchFamilyMemberLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /////////////////////////ALLERGY///////////////////
        try {
            if (intent.getBooleanExtra("ACTIVATE_ALLERGY", false)) {
                viewPager.setCurrentItem(1);
                if (currentFragment instanceof FragmentDashboardProfileTest) {
                    ((FragmentDashboardProfileTest) currentFragment).refreshAllergyData();
                }
                switchFamilyMemberLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ////////////////////TIMELINE//////////////////
        try {
            updateBlood = intent.getStringExtra("UPDATEBLOOD");
            if (updateBlood != null) {
                if (updateBlood.equalsIgnoreCase("UPDATEBLOOD")) {
                    viewPager.setCurrentItem(1);
                    if (currentFragment instanceof FragmentDashboardProfileTest) {
                        ((FragmentDashboardProfileTest) currentFragment).refreshTimelineData();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ///////////////////WIZARD/////////////////////
        try {
            childOk = intent.getStringExtra("chaildWidgeard");
            adultOk = intent.getStringExtra("AdultWidgeard");

            if (childOk != null || adultOk != null) {
                viewPager.setCurrentItem(1);

                FragmentDashboardProfileTest fragmentDashboardProfileTest = (FragmentDashboardProfileTest) viewPager.getAdapter().instantiateItem(viewPager, 1);
                fragmentDashboardProfileTest.refreshTab(0, null);

                FragmentDashboardRisks fragmentDashboardRisks = (FragmentDashboardRisks) viewPager.getAdapter().instantiateItem(viewPager, 2);
                fragmentDashboardRisks.refreshRiskFragment();

                loginUserProfileName.setText(customerManager.getCurrentFamilyMember().getFirst_name().substring(0, 1).toUpperCase() +
                        customerManager.getCurrentFamilyMember().getFirst_name().substring(1) + " " +
                        customerManager.getCurrentFamilyMember().getLast_name());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ///////////////////////CAMERA UPLOAD/////////////////////
        try {
            uploadDoc = intent.getStringExtra(OpenNoteScannerAppConstant.UPLOAD_DOC);
            if (uploadDoc != null) {
                if (uploadDoc.equalsIgnoreCase("UPLOAD_DOC")) {
                    chatMessageCamera = new ChatMessage();
                    System.out.println("MainActivity.onNewIntent UPLOAD_DOC");
                    mNotifyManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                    build = new NotificationCompat.Builder(MainActivity.this);
                    build.setContentTitle("ekincare medical records")
                            .setContentText("uploading medical record ")
                            .setColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary))
                            .setSmallIcon(R.drawable.notification_logo_all);
                    new Download().execute();

                    ArrayList<String> arrayListImages = getIntent().getStringArrayListExtra(OpenNoteScannerAppConstant.SCANNED_RESULT_ALL);

                    System.out.println("MainActivity.onNewIntent uploadDoc arrayListImages=" + arrayListImages.size() + " " + arrayListImages.toString());
                    if (arrayListImages != null) {

                        for (int count = 0; count < arrayListImages.size(); count++) {

                            isLastArrayListImage = (count == (arrayListImages.size() - 1));

                            Bitmap bm = null;
                            if (arrayListImages.get(count) != null) {
                                String path = arrayListImages.get(count);
                                if (path != null) {

                                    Uri mUri = Uri.parse(path);
                                    try {
                                        bm = MediaUtils.getBitmap(mUri, MainActivity.this);
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }

                                    File file = MediaUtils.savebitmap(bm, path.substring(path.lastIndexOf("/")));
                                    String fileName = path.substring(path.lastIndexOf("/"));

                                    try {
                                        entity = MediaUtils.getUploadEntity(file.getAbsolutePath(), 0, fileName);
                                        uploadMultipleReport(bm, count == 0, count == arrayListImages.size() - 1);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            }

                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchScreenFromGcmNotificationType(String notificationTitle,
                                                     String notificationMessage,
                                                     String notificationPrimaryButtonName,
                                                     String notificationPrimaryButtonAction,
                                                     String notificationSecondaryButtonName,
                                                     String notificationSecondaryButtonAction)
    {
        System.out.println("MainActivity.switchScreenFromGcmNotificationType");

        viewPager.setCurrentItem(0);

        FragmentChat fragmentChat = (FragmentChat) viewPager.getAdapter().instantiateItem(viewPager, 0);
        fragmentChat.onNotificationReceived(notificationTitle, notificationMessage, notificationPrimaryButtonName, notificationPrimaryButtonAction,notificationSecondaryButtonName,notificationSecondaryButtonAction);
    }

    /*// If messages have been missed, check the backlog. Otherwise check the
    // current intent for a new message.
    private String getMessage(int numOfMissedMessages) {
        String message = "";
        if (numOfMissedMessages > 0) {
            String plural = numOfMissedMessages > 1 ? "s" : "";
            Log.i("onResume", "missed " + numOfMissedMessages + " message" + plural);
            NotificationManager mNotification = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotification.cancel(R.string.notification_number);

        } else {
            Log.i("onResume", "no missed messages");
            Intent intent = getIntent();
            if (intent != null) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    for (String key : extras.keySet()) {
                        message += key + "=" + extras.getString(key) + "\n";
                    }
                }
            }
        }

        message += "\n";
        return message;
    }
*/
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    public void refreshData() {
        System.out.println("MainActivity.refreshData");
        setupViewPager();
        viewPager.setCurrentItem(0);
        adapter.notifyDataSetChanged();
        viewPager.destroyDrawingCache();
    }

    public void refreshProfile() {
        System.out.println("MainActivity.refreshProfile");

        FragmentDashboardProfileTest fragmentDashboardProfileTest = (FragmentDashboardProfileTest) viewPager.getAdapter().instantiateItem(viewPager, 1);
        fragmentDashboardProfileTest.refreshProfileData();

        FragmentDashboardRisks fragmentDashboardRisks = (FragmentDashboardRisks) viewPager.getAdapter().instantiateItem(viewPager, 2);
        fragmentDashboardRisks.refreshRiskFragment();

    }

    private void showPDialog() {
        mAlert_Dialog = new Dialog(MainActivity.this);
        mAlert_Dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mAlert_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAlert_Dialog.setContentView(R.layout.materialprogressbar);
        mAlert_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //mAlert_Dialog.setCancelable(false);
        mAlert_Dialog.setCanceledOnTouchOutside(true);
        progressWithArrow = (CircleProgressBar) mAlert_Dialog.findViewById(R.id.progressWithArrow);
        progressWithArrow.setColorSchemeResources(android.R.color.holo_blue_light);
        mAlert_Dialog.show();
    }

    public void goToScreen(int position) {
        showPDialog();
        viewPager.setCurrentItem(1);
        if (position == 13) {
            viewPager.setCurrentItem(2);
            position =0;
        }
        FragmentDashboardProfileTest fragmentDashboardProfileTest = (FragmentDashboardProfileTest) viewPager.getAdapter().instantiateItem(viewPager, 1);
        fragmentDashboardProfileTest.refreshTab(position, mAlert_Dialog);
        FragmentDashboardRisks fragmentDashboardRisks = (FragmentDashboardRisks) viewPager.getAdapter().instantiateItem(viewPager, 2);
        fragmentDashboardRisks.refreshRiskFragment();


        System.out.println("MainActivity.refreshProfile");
    }

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentChat(), "ASSISTANT");
        adapter.addFragment(new FragmentDashboardProfileTest(), "PROFILE");
        adapter.addFragment(new FragmentDashboardRisks(), "RISKS");
        viewPager.setAdapter(adapter);

    }

    private void setupTabView() {
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            tabLayout.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                }
            });
        }

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        viewPager.setCurrentItem(tab.getPosition());
                        adapter.notifyDataSetChanged();
                        System.out.println("MainActivity.onTabSelected======" + "1");
                        setSupportActionBar(mToolbar);
                        getSupportActionBar().setDisplayShowTitleEnabled(true);
                        mToolbar.setTitle("");
                        loginUserProfileName.setText("eKincare");
                        switchFamilyMemberLayout.setVisibility(View.VISIBLE);
                        arrowIcon.setVisibility(View.GONE);
                        currentFragment = adapter.getItem(tab.getPosition());
                        break;
                    case 1:
                        if (prefs.getIsFirstTimeRegister()) {
                            Toast.makeText(context, "Your have not filled Hra", Toast.LENGTH_SHORT).show();
                        } else {
                            viewPager.setCurrentItem(tab.getPosition());
                            adapter.notifyDataSetChanged();
                            //tabLayout.notify();
                            System.out.println("MainActivity.onTabSelected======" + "2");
                            setSupportActionBar(mToolbar);
                            getSupportActionBar().setHomeButtonEnabled(false);
                            getSupportActionBar().setDisplayShowTitleEnabled(false);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
                            switchFamilyMemberLayout.setVisibility(View.VISIBLE);
                            arrowIcon.setVisibility(View.VISIBLE);
                            currentFragment = adapter.getItem(tab.getPosition());

                            loginUserProfileName.setText(customerManager.getCurrentFamilyMember().getFirst_name().substring(0, 1).toUpperCase() +
                                    customerManager.getCurrentFamilyMember().getFirst_name().substring(1) + " " +
                                    customerManager.getCurrentFamilyMember().getLast_name());
                            try {
                                if (currentFragment instanceof FragmentDashboardProfileTest) {
                                    ((FragmentDashboardProfileTest) currentFragment).tabPosition();
                                }
                            } catch (Exception e) {
                                Log.e("12345", "Exception in onPaymentSuccess", e);
                            }

                        }

                        break;
                    case 2:
                        if (prefs.getIsFirstTimeRegister()) {
                            Toast.makeText(context, "You have not filled Hra", Toast.LENGTH_SHORT).show();
                        } else {
                            viewPager.setCurrentItem(tab.getPosition());
                            adapter.notifyDataSetChanged();
                            System.out.println("MainActivity.onTabSelected======" + "3");
                            setSupportActionBar(mToolbar);
                            getSupportActionBar().setHomeButtonEnabled(false);
                            getSupportActionBar().setDisplayShowTitleEnabled(false);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
                            switchFamilyMemberLayout.setVisibility(View.VISIBLE);
                            arrowIcon.setVisibility(View.VISIBLE);
                            currentFragment = adapter.getItem(tab.getPosition());

                            loginUserProfileName.setText(customerManager.getCurrentFamilyMember().getFirst_name().substring(0, 1).toUpperCase() +
                                    customerManager.getCurrentFamilyMember().getFirst_name().substring(1) + " " +
                                    customerManager.getCurrentFamilyMember().getLast_name());
                        }

                        break;

                    default:
                        loginUserProfileName.setText("eKincare");
                        switchFamilyMemberLayout.setVisibility(View.VISIBLE);
                        arrowIcon.setVisibility(View.GONE);
                        currentFragment = adapter.getItem(tab.getPosition());
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public boolean getNumberOfDaysSinceInstallation() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();

        try {
            Date todayDate = dateFormat.parse(dateFormat.format(c.getTime()));
            Date installationDate = dateFormat.parse(prefs.getInstallTime());
            if (todayDate.compareTo(installationDate) % 7 == 0) {
                prefs.setUserRatingCount(0);
                return true;
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setUpRatingBar() {
        if (!prefs.getIsRated()) {
            if (getNumberOfDaysSinceInstallation() || prefs.getUserRatingCount() >= 10) {
                ratingDialog();
            } else {
                prefs.setUserRatingCount(prefs.getUserRatingCount() + 1);
            }
        } else {
            //do nothing
        }

    }

    private void ratingDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Liked Us?");
        builder.setMessage("How is your experience with eKincare so far?");

        String neutralText = "Later";
        builder.setNeutralButton(neutralText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prefs.setIsRated(false);
                        prefs.setUserRatingCount(0);
                        prefs.setUserRatingDayCount(0);
                        dialog.dismiss();
                    }
                });


        String positiveText = "Great";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prefs.setIsRated(true);
                        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                        }
                    }
                });

        String negativeText = "Not So Good";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prefs.setIsRated(true);
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"feedback@ekincare.com"});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                        //email.putExtra(Intent.EXTRA_TEXT, message);
                        emailIntent.setType("message/rfc822");
                        //startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
                        startActivity(emailIntent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // display dialog
        dialog.show();
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        // showWalletMoney(false,null);
        System.out.println("MainActivity.onPaymentSuccess razorpayPaymentID=" + razorpayPaymentID);
        try {
            if (currentFragment instanceof FragmentChat) {
                ((FragmentChat) currentFragment).paymentSuccessServerCall(razorpayPaymentID);
            }
        } catch (Exception e) {
            System.out.println("ErrorePayment============" + "Chat2");
            Log.e("12345", "Exception in onPaymentSuccess", e);
        }
    }


    @Override
    public void onPaymentError(int code, String response) {
        try {
            // showWalletMoney(false,null);
            System.out.println("MainActivity.onPaymentError==========" + code + "========" + response);
            if (currentFragment instanceof FragmentChat) {
                ((FragmentChat) currentFragment).paymentFailuerServerCall(response);
            }
        } catch (Exception e) {
            System.out.println("ErrorePayment============" + "Chat3");
            //showWalletMoney(false,null);
            /* if (currentFragment instanceof FragmentChat) {
                 ((FragmentChat) currentFragment).paymentFailuerServerCall(response);
             }*/
            Log.e("TEST=======", Log.getStackTraceString(e));
            System.out.println("MainActivity.onPaymentError==========" + code + "========" + response);
            Log.e("12345===========", "Exception in onPaymentSuccess", e);
        }

    }

    @Override
    public void onNotificationClick(String text) {
        if (isSchemaPresent(text))
        {
            String id = text.substring(text.indexOf("@") + 1, text.indexOf("/"));
            String transitionScreen = text.substring(text.indexOf("/") + 1);

            System.out.println("MainActivity.onNotificationClick transitionScreen=" + transitionScreen+ "=========="+text);

            List<Customer> familyMembers = mProfileManager.getFamilyMembers();
            if (!customerManager.isLoggedInCustomer()) {
                System.out.println("notloginCustomer========" + "yes");
                Iterator<Customer> it = mProfileManager.getFamilyMembers().iterator();
                while (it.hasNext()) {
                    Customer customer = it.next();
                    if (customerManager.getCurrentCustomer().getIdentification_token().equals(customer.getIdentification_token())) {
                        familyMembers.remove(it);
                    }
                }
                if (!familyMembers.contains(mProfileManager.getLoggedinCustomer()))
                    familyMembers.add(mProfileManager.getLoggedinCustomer());

            } else if (customerManager.isLoggedInCustomer()) {
                System.out.println("notloginCustomer========" + "yes No");
                Customer mCustomer = mProfileManager.getLoggedinCustomer();
                if (!familyMembers.contains(mCustomer))
                    familyMembers.add(0, mCustomer);
            }

            for (int i = 0; i < familyMembers.size(); i++) {
                customerManager.setCurrentFamilyMember(mProfileManager.getLoggedinCustomer());
                if (familyMembers.get(i).getIdentification_token().equalsIgnoreCase(id)) {
                    customerManager.setCurrentFamilyMember(familyMembers.get(i));
                    break;
                }
            }

            int pos = 0;

            if (transitionScreen.equalsIgnoreCase("documents")) {
                pos = 3;
            } else if (transitionScreen.equalsIgnoreCase("timeline")) {
                pos = 1;
            }
            if (transitionScreen.equalsIgnoreCase("medications")) {
                pos = 2;
            }
            if (transitionScreen.equalsIgnoreCase("vaccinations")) {
                pos = 4;
            }
            if (transitionScreen.equalsIgnoreCase("profile")) {
                pos = 0;
            }
            if (transitionScreen.equalsIgnoreCase("allergies")) {
                pos = 5;
            }

            goToScreen(pos);

        } else {
            System.out.println("MainActivity.onNotificationClick in====" + text);
            if (text.contains("UpdateWeightAction")) {
                viewPager.setCurrentItem(1);
            } else if (text.contains("AddFamilyMembersAction")||text.contains("Add Family Member")) {
                try {
                    if (currentFragment instanceof FragmentChat) {
                        ((FragmentChat) currentFragment).addFamilyTextNotification("Add a family Member");
                    }
                } catch (Exception e) {
                }
            } else if (text.contains("UploadMedicalAction")||text.contains("Upload Record")) {
                System.out.println("MainActivity.onNotificationClick yes====" + text);
                try {
                    if (currentFragment instanceof FragmentChat) {
                        ((FragmentChat) currentFragment).addFamilyTextNotification("Upload medical records");
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    private boolean isSchemaPresent(String text) {
        if (text.startsWith("@")) {
            return true;
        }
        return false;
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // return mFragmentTitleList.get(position);
            return null;
        }
    }


    private void versionCheckRequest() {
        String URL = TagValues.VERSION_CHECK;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                PackageInfo pInfo = MainActivity.this.getPackageManager().getPackageInfo(MainActivity.this.getPackageName(), 0);
                                String version = pInfo.versionName;
                                String updateVersion = response.getString("version");
                                if (updateVersion.equals(version)) {

                                } else {
                                    System.out.println("version==========" + "Open Dialog");
                                    //openPlayStore();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                        } else {
                            CustomeDialog.dispDialog(MainActivity.this, TagValues.DATA_NOT_FOUND);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                    }
                }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                Set<String> keySet = headers.keySet();
                return super.parseNetworkResponse(response);

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(MainActivity.this));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

    }

    private void openPlayStore() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        try {
            builder.setTitle("New version available");
            builder.setMessage("Download the latest version at play store.");

            String positiveText = "Update";
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName()));
                            startActivity(intent);
                        }
                    });

            String negativeText = "Cancel";
            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            // display dialog
            dialog.show();
        } catch (Exception e) {

        }

    }

    private void hidePDialog() {
        if (mAlert_Dialog != null) {
            mAlert_Dialog.dismiss();
            mAlert_Dialog = null;
        }
    }


    private GoogleApiClient mGoogleApiClient;
    private int googleApiClientId = 0;
    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;


    private BroadcastReceiver mGCMNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String message = intent.getStringExtra("notification_message");

                String chatbot_primary_text = "";
                if (intent.getStringExtra("notification_chatbot_primary_text") != null) {
                    chatbot_primary_text = intent.getStringExtra("notification_chatbot_primary_text");
                }

                String chatbot_primary_action = "";
                if (intent.getStringExtra("notification_chatbot_primary_action") != null) {
                    chatbot_primary_action = intent.getStringExtra("notification_chatbot_primary_action");
                }

                String chatbot_secondary_text = "";
                if (intent.getStringExtra("notification_chatbot_secondary_text") != null) {
                    chatbot_secondary_text = intent.getStringExtra("notification_chatbot_secondary_text");
                }

                String chatbot_secondary_action = "";
                if (intent.getStringExtra("notification_chatbot_secondary_action") != null) {
                    chatbot_secondary_action = intent.getStringExtra("notification_chatbot_secondary_action");
                }

                String image_url = "";
                if (intent.getStringExtra("notification_image_url") != null) {
                    image_url = intent.getStringExtra("notification_image_url");
                }

                String title = "";
                if (intent.getStringExtra("notification_title") != null) {
                    title = intent.getStringExtra("notification_title");
                }

                System.out.println("MainActivity.BroadcastReceiver.mGCMNotificationReceiver.onReceive title="+title);
                System.out.println("MainActivity.BroadcastReceiver.mGCMNotificationReceiver.onReceive message="+message);
                System.out.println("MainActivity.BroadcastReceiver.mGCMNotificationReceiver.onReceive chatbot_primary_text="+chatbot_primary_text);
                System.out.println("MainActivity.BroadcastReceiver.mGCMNotificationReceiver.onReceive chatbot_primary_action="+chatbot_primary_action);
                System.out.println("MainActivity.BroadcastReceiver.mGCMNotificationReceiver.onReceive chatbot_secondary_text="+chatbot_secondary_text);
                System.out.println("MainActivity.BroadcastReceiver.mGCMNotificationReceiver.onReceive chatbot_secondary_action="+chatbot_secondary_action);

                NotificationMessageDialog newFragment = new NotificationMessageDialog(MainActivity.this,
                        MainActivity.this,
                        title,
                        message,
                        chatbot_primary_text,
                        chatbot_primary_action,
                        chatbot_secondary_text,
                        chatbot_secondary_action);
                newFragment.show(getSupportFragmentManager(), "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };


    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();

    }

    @Override
    public void onStop() {
        super.onStop();
        EkinCareApplication.setActivityVisible(false);
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(MainActivity.this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mixpanel.flush();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(MainActivity.this);
            mGoogleApiClient.disconnect();
        }
    }



    public void setUpGoogleFitCard()
    {
        System.out.println("MainActivity.setUpGoogleFitCard");
        final FragmentDashboardRisks fragmentDashboardRisks = (FragmentDashboardRisks) viewPager.getAdapter().instantiateItem(viewPager, 2);

        final FragmentChat fragmentChat = (FragmentChat) viewPager.getAdapter().instantiateItem(viewPager, 0);

        if (customerManager.getCurrentCustomer() != null)
        {
            if (customerManager.isLoggedInCustomer())
            {
                fragmentDashboardRisks.isGoogleFitVisible(true);

                if (prefs.getIsGoogleFitConnected()) {
                    fragmentDashboardRisks.isGoogleFitConnected(true, getString(R.string.google_fit_connected_body_text));
                } else {
                    String text = getString(R.string.google_fit_image_body_text);
                    fragmentDashboardRisks.isGoogleFitConnected(false, text);
                }

                if (NetworkCaller.isInternetOncheck(MainActivity.this)) {
                    try {
                        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                                .addApi(Fitness.HISTORY_API)
                                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                                    @Override
                                    public void onConnected(@Nullable Bundle bundle) {
                                        System.out.println("FragmentDashboardRisks.onConnected");
                                        prefs.setIsGoogleFitConnected(true);

                                        fragmentDashboardRisks.isGoogleFitConnected(true, getString(R.string.google_fit_connected_body_text));

                                        new ViewTodaysStepCountTask().execute();
                                        new ViewWeekStepCountTask().execute();
                                        new ViewWeekCaloriesCountTask().execute();

                                        fragmentChat.removeGoogleFitMessage();
                                    }

                                    @Override
                                    public void onConnectionSuspended(int i) {
                                        System.out.println("FragmentDashboardRisks.onConnectionSuspended");
                                        prefs.setIsGoogleFitConnected(false);

                                        String text = getString(R.string.google_fit_image_body_text);
                                        fragmentDashboardRisks.isGoogleFitConnected(false, text);
                                    }
                                })
                                .enableAutoManage(MainActivity.this, googleApiClientId, new GoogleApiClient.OnConnectionFailedListener() {
                                    @Override
                                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                        System.out.println("FragmentDashboardRisks.onConnectionFailed");
                                        prefs.setIsGoogleFitConnected(false);
                                        String text = getString(R.string.google_fit_not_connected_body_text) + " " + getString(R.string.google_fit_image_body_text);

                                        fragmentDashboardRisks.isGoogleFitConnected(false, text);


                                        // The failure has a resolution. Resolve it.
                                        // Called typically when the app is not yet authorized, and an
                                        // authorization dialog is displayed to the user.
                                        if (!authInProgress) {
                                            try {
                                                authInProgress = true;
                                                connectionResult.startResolutionForResult(MainActivity.this, REQUEST_OAUTH);
                                            } catch (IntentSender.SendIntentException e) {
                                                System.out.println("Exception while starting resolution activity" + e);
                                            }
                                        }
                                    }
                                })
                                .build();

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                        System.out.println("mGoogleApiClient==========" + "wrong");
                    }
                } else {
                    try {
                        Toast.makeText(MainActivity.this, "Failed to fetch data due to slow/no internet.", Toast.LENGTH_LONG).show();
                        //gfitdaySteps.setText(prefs.getStepsCount());
                        //gfitCalBurned.setText(prefs.getCaloriesCount());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                /*if (appInstalledOrNot("com.google.android.apps.fitness")) {

                } else {
                    fragmentDashboardRisks.isGoogleFitConnected(false, getString(R.string.google_fit_not_connected_body_text));
                }*/
            } else {
                fragmentDashboardRisks.isGoogleFitVisible(false);
            }
        }

    }

    public void callGFitData() {
        new ViewTodaysStepCountTask().execute();
        new ViewWeekStepCountTask().execute();
        new ViewWeekCaloriesCountTask().execute();
    }

    private class ViewTodaysStepCountTask extends AsyncTask<Void, Void, Void> {
        FragmentDashboardRisks fragmentDashboardRisks = (FragmentDashboardRisks) viewPager.getAdapter().instantiateItem(viewPager, 2);

        protected Void doInBackground(Void... params) {
            System.out.println("ViewTodaysStepCountTask.doInBackground");
            try {
                DailyTotalResult resultForSteps = Fitness.HistoryApi.readDailyTotal(mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA).await(1, TimeUnit.MINUTES);
                DailyTotalResult resultForCalories = Fitness.HistoryApi.readDailyTotal(mGoogleApiClient, DataType.TYPE_CALORIES_EXPENDED).await(1, TimeUnit.MINUTES);
                fragmentDashboardRisks.showStepsData(resultForSteps.getTotal());
                fragmentDashboardRisks.showCaloriesData(resultForCalories.getTotal());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class ViewWeekStepCountTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            displayLastWeeksData();
            return null;
        }
    }

    private class ViewWeekCaloriesCountTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            displayLastWeeksCaloriesData();
            return null;
        }
    }

    public void displayLastWeeksData() {
        FragmentDashboardRisks fragmentDashboardRisks = (FragmentDashboardRisks) viewPager.getAdapter().instantiateItem(viewPager, 2);

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = DateFormat.getDateInstance();
        Log.e("History", "Range Start: " + dateFormat.format(startTime));
        Log.e("History", "Range End: " + dateFormat.format(endTime));

        //Check how many steps were walked and recorded in the last 7 days
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        try {
            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mGoogleApiClient, readRequest).await(1, TimeUnit.MINUTES);

            //Used for aggregated data
            if (dataReadResult.getBuckets().size() > 0) {
                Log.e("History", "Number of buckets: " + dataReadResult.getBuckets().size());
                for (Bucket bucket : dataReadResult.getBuckets()) {
                    List<DataSet> dataSets = bucket.getDataSets();
                    for (DataSet dataSet : dataSets) {
                        fragmentDashboardRisks.showWeeklyStepsDataSet(dataSet);
                    }
                }
            }
            //Used for non-aggregated data
            else if (dataReadResult.getDataSets().size() > 0) {
                Log.e("History", "Number of returned DataSets: " + dataReadResult.getDataSets().size());
                for (DataSet dataSet : dataReadResult.getDataSets()) {
                    fragmentDashboardRisks.showWeeklyStepsDataSet(dataSet);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public void displayLastWeeksCaloriesData() {
        FragmentDashboardRisks fragmentDashboardRisks = (FragmentDashboardRisks) viewPager.getAdapter().instantiateItem(viewPager, 2);

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = DateFormat.getDateInstance();
        Log.e("History", "Range Start: " + dateFormat.format(startTime));
        Log.e("History", "Range End: " + dateFormat.format(endTime));

        //Check how many steps were walked and recorded in the last 7 days
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        try {
            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mGoogleApiClient, readRequest).await(1, TimeUnit.MINUTES);
            //Used for aggregated data
            if (dataReadResult.getBuckets().size() > 0) {
                Log.e("History", "Number of buckets: " + dataReadResult.getBuckets().size());
                for (Bucket bucket : dataReadResult.getBuckets()) {
                    List<DataSet> dataSets = bucket.getDataSets();
                    for (DataSet dataSet : dataSets) {
                        fragmentDashboardRisks.showWeeklyCaloriesDataSet(dataSet);
                    }
                }
            }
            //Used for non-aggregated data
            else if (dataReadResult.getDataSets().size() > 0) {
                Log.e("History", "Number of returned DataSets: " + dataReadResult.getDataSets().size());
                for (DataSet dataSet : dataReadResult.getDataSets()) {
                    fragmentDashboardRisks.showWeeklyCaloriesDataSet(dataSet);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void googleFitHistoryPost(JSONObject json) {
        System.out.println("MainActivity.googleFitHistoryPost json=" + json.toString());
        String URL = TagValues.GOOGLE_FIT_HISTORY;
        //showPDialog();
        JsonObjectRequest jsonPostGoogleFit = new JsonObjectRequest(Request.Method.POST, URL, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            System.out.println("MainActivity.googleFitHistoryPost response" + response.toString());
                            // hidePDialog();
                            googleFitHistoryResposne();
                        } else {
                            CustomeDialog.dispDialog(MainActivity.this, TagValues.DATA_NOT_FOUND);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(MainActivity.this));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(MainActivity.this.getApplicationContext()).addToRequestQueue(jsonPostGoogleFit);

    }

    private void googleFitHistoryResposne() {
        System.out.println("MainActivity.googleFitHistoryResposne");
        FragmentDashboardRisks fragmentDashboardRisks = (FragmentDashboardRisks) viewPager.getAdapter().instantiateItem(viewPager, 2);
        fragmentDashboardRisks.clearGFitData();

        if (mGoogleApiClient != null) {
            System.out.println("MainActivity.googleFitHistoryResposne mGoogleApiClient is not null");
            new ViewTodaysStepCountTask().execute();
            new ViewWeekStepCountTask().execute();
            new ViewWeekCaloriesCountTask().execute();
        } else {
            System.out.println("MainActivity.googleFitHistoryResposne mGoogleApiClient is null");
        }
    }

    private class Download extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Displays the progress bar for the first time.
            build.setProgress(100, 0, false);
            mNotifyManager.notify(1, build.build());
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Update progress
            build.setProgress(100, values[0], false);
            mNotifyManager.notify(1, build.build());
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int i;
            for (i = 0; i <= 100; i += 5) {
                // Sets the progress indicator completion percentage
                publishProgress(Math.min(i, 100));
                try {
                    // Sleep for 5 seconds
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.d("Failure", "sleeping failure");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            build.setContentText("upload medical records complete");
            // Removes the progress bar
            build.setProgress(0, 0, false);
            mNotifyManager.notify(1, build.build());

        }
    }

    @Override
    public void onBackPressed() {
        FragmentChat fragmentChat = (FragmentChat) viewPager.getAdapter().instantiateItem(viewPager, 0);

        if (fragmentChat.isAnyProcessActive()) {
            exitDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void exitDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("");
        builder.setMessage("You are in between chat process.Closing the app will abort that process.Do you want to close the app?");

        String positiveText = "Yes";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentChat fragmentChat = (FragmentChat) viewPager.getAdapter().instantiateItem(viewPager, 0);
                        fragmentChat.abortLogic();
                        dialog.dismiss();
                        finish();
                    }
                });

        String negativeText = "No";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        // display dialog
        dialog.show();
    }
}

