package com.message;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;


import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
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
import com.bumptech.glide.Glide;
import com.ekincare.R;
import com.ekincare.app.CustomerManager;
import com.ekincare.app.EkinCareApplication;
import com.ekincare.app.ProfileManager;
import com.ekincare.app.VolleyRequestSingleton;
import com.ekincare.receiver.ConnectivityReceiver;
import com.ekincare.util.DateUtility;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.message.adapter.ChatListFeatures;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.message.adapter.MessageRecyclerAdapter;
import com.message.adapter.MultitextCompleteViewSearchAdapter;
import com.message.adapter.PackageDoctorSearchAdapter;
import com.message.async.AddNewMessageTask;
import com.message.custominterface.BottomSheetFragmentInterface;
import com.message.custominterface.CardDcClick;
import com.message.custominterface.CardDoctorConsultClick;
import com.message.custominterface.CardWithImageClickEvent;
import com.message.custominterface.CardWithListButtonClickEvent;
import com.message.custominterface.DoctorAutoString;
import com.message.custominterface.HorizontalCardListItemClickEvent;
import com.message.custominterface.HorizontalListItemClickEvent;
import com.message.custominterface.SchemaClickEvent;
import com.message.custominterface.ServerRequestInterface;
import com.message.fragment.FragmentDatePickerView;
import com.message.fragment.FragmentDoubleWheelView;
import com.message.fragment.FragmentHraFamilyHistoryView;
import com.message.fragment.FragmentProgressView;
import com.message.fragment.FragmentSingleWheelView;
import com.message.messageitems.IncomeingCardVitalMessage;
import com.message.messageitems.IncomingCardGraphWithListMessage;

import com.message.messageitems.IncomingCardWithImageMessage;
import com.message.messageitems.IncomingCardWithListMedicationsMessage;
import com.message.messageitems.IncomingCardWithListMessage;
import com.message.messageitems.IncomingCardWithListVaccinationMessage;
import com.message.messageitems.IncomingCardWithListWellnessMessage;
import com.message.messageitems.IncomingCardWithTextAndButtonMessage;

import com.message.messageitems.IncomingCardWithTextMessage;
import com.message.messageitems.IncomingCartCardWithListMessage;

import com.message.messageitems.IncomingHorizontalDcCardListMessage;
import com.message.messageitems.IncomingHorizontalDoctorCardListMessage;

import com.message.messageitems.IncomingHorizontalListMessage;
import com.message.messageitems.IncomingHorizontalPackageCardListMessage;
import com.message.messageitems.IncomingProfileCompletionMessage;
import com.message.messageitems.OutgoingMapMessage;
import com.message.messageitems.IncomingTextMessage;
import com.message.messageitems.IncomingTextProgressMessage;
import com.message.messageitems.IntroductionMessage;
import com.message.messageitems.OutgoingDcCardMessage;
import com.message.messageitems.OutgoingImageMessage;
import com.message.messageitems.OutgoingTextMessage;
import com.message.messageitems.SpinnerItem;
import com.message.model.BookAppointmentResponse;
import com.message.model.CardViewModel;
import com.message.model.ChatDatabaseModel;
import com.message.model.DCPackage;
import com.message.model.JsonMessageResponse;
import com.message.model.Labs;
import com.message.model.MedicationsChatModel;
import com.message.model.ChatMessage;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.model.PackageItem;
import com.message.model.TestComponentData;
import com.message.model.TrendsModelData;
import com.message.model.VaccaniationDataModel;
import com.message.model.WalletResponse;
import com.message.model.WellnessDataModel;
import com.message.utility.Config;
import com.message.utility.DateUtils;
import com.message.utility.ServerRequests;
import com.message.view.ViewUtils;

import com.oneclick.ekincare.DialogTermsAndCondition;

import com.oneclick.ekincare.MainActivity;
import com.oneclick.ekincare.MainActivity2;
import com.oneclick.ekincare.helper.CustomeDialog;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.utility.FileUtil;
import com.oneclick.ekincare.utility.FileUtility;
import com.oneclick.ekincare.vo.ChatFeaturesItem;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.DoctorModel;
import com.oneclick.ekincare.vo.DoctorModelData;
import com.oneclick.ekincare.vo.Immunization;
import com.oneclick.ekincare.vo.PackageDataSet;
import com.oneclick.ekincare.vo.PackageTest;
import com.oneclick.ekincare.vo.ProfileData;
import com.openNoteScanner.OpenNoteScannerActivity;

import com.razorpay.Checkout;


import org.apache.http.HttpEntity;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.google.android.gms.analytics.internal.zzy.c;


/**
 * Created by Ajay on 23-11-2016.
 */

public class FragmentChat extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener, HorizontalListItemClickEvent,
        HorizontalCardListItemClickEvent, CardWithListButtonClickEvent, CardWithImageClickEvent,
        ServerRequestInterface, DoctorAutoString, CardDcClick, SchemaClickEvent, CardDoctorConsultClick, BottomSheetFragmentInterface {

    private View rootView;
    private EditText editTextMessage;
    private ImageView imageViewAbort;
    private LinearLayout linearLayoutLoading;
    public RecyclerView recyclerViewChat;
    private ImageView imageViewSend, imageViewSnap, imageViewDone;
    public MessageRecyclerAdapter mRecyclerAdapter;
    private List<ChatMessage> chatMessageList, tempChatMessageQueue;
    private ChatMessage outgoingChatMessage;
    private static final int PERMISSION_REQUEST_CODE = 200;

    private RelativeLayout relativeLayoutSend;
    DoctorAutoString doctorautostring;

    Fragment currentFragment;

    private int sentPosition = -1;
    Animation slide_down, slide_up;

    private Customer mCustomer;
    private CustomerManager customerManager;
    private PreferenceHelper prefs;
    private ProfileData mProfileData;
    private ProfileManager mProfileManager;
    private ServerRequests serverRequests;
    int capabilityId = -1;
    int askQueryId = -1;

    JsonMessageResponse jsonMessageResponse;

    int addFamilyLogicId = -1;
    String familyMemberName = "";
    String familyMemberDOF = "";
    String familyMemberGender = "";
    String familyMemberRelation = "";
    String familyMemberMobile = "";
    String familyMemberEmail = "";
    int familyMemberAge = 0;

    int hraId = -1;
    boolean isHRATrue = false;

    boolean isAddFamilyTrue = false;
    boolean isaskQuaryDoctorTrue = false;

    String hraHeightInch = "";
    String hraHeightFeet = "";
    String hraWeight = "";
    String hraBloodGroupId = "";
    String hraBloodGroup = "";
    String hraWaist = "";
    String hraSmoke = "";
    String hraAlcohol = "";
    String hraExercise = "";
    String hraFatherCondition = "";
    String hraMotherCondition = "";
    String hraBloodSugar = "";
    String hraSystolic = "";
    String hraDiastolic = "";
    String hraUserCondition = "";

    String askQueryDate = "";
    String askQueryMode = "";
    String askRelation = "";
    String askQuerySpeciality = "";
    String askQueryTime = "";
    String askQueryQuestion = "";
    int askQueryPackagePrice;
    int askQueryTotalPackagePrice = 0;
    private String askShareProfile = "";


    String testQueryDoctor = "";

    boolean isBookAppointmentTrue = false;
    int bookAppointmentId = -1;
    List<PackageItem> requestPackages;
    String packageIds = "";
    String sponsorIds = "";
    String homeCollectionEnable="";
    int packagePrice = 0;
    int packageTotalPrice = 0;
    String packageTxnid = "";
    String packageTime = "";
    String packageDate = "";
    String packageWholeAddress="";
    String packageAddressLine1="";
    String packageAddressLine2="";
    String packageCity="";
    String packageState="";
    String packageCounty="";
    String packagePostalCode="";
    String packageLatitude="";
    String pacakageLangtude="";
    String pacakageHouseNumber="";
    int packageYear = 0;
    int packageMonth = 0;
    int packageDay = 0;
    int packageHour = 0;
    int packageMin = 0;
    String packageAppointmentTime = "";

    ValueAnimator mAnimator;
    protected LocationManager locationManager;
    double currentLocationLat, currentLocationLang;
    Location location;
    Geocoder geocoder;
    List<Address> addresses;

    Dialog dialogAdditionalFeatures;
    TextView dialogAdditionalFeaturesCancel;
    public static final String[] titles = new String[]{
            "Digitize medical reports", "Add a family member", "Book a health check", "Show blood glucose results", "Consult a doctor", "Show my father's health status", "Book Video call with a doctor", "Share my profile with doctor"};

    private String strFamilyMemberKey = "";
    private String bookPackageCustomerId="";


    public static final Integer[] images = {
            R.drawable.ic_folder_white_24px, R.drawable.ic_group_white_24px, R.drawable.ic_book_healthcheck_popup, R.drawable.ic_bloodglucose_popup, R.drawable.ic_ask_query_doctor_popup, R.drawable.ic_health_status_popup, R.drawable.ic_health_status_popup, R.drawable.ic_health_status_popup};
    ListView additionalList;
    List<ChatFeaturesItem> chatfeaturesitem;
    ImageView fullScreenDialogClose;

    Toolbar mToolbar;
    String helpText = "";

    private static final int PICKFILE_RESULT_CODE = 167;
    private static final int PLACE_PICKER_REQUEST = 5;


    HttpEntity entity;

    protected static List<String> fileTypes = new ArrayList<String>(
            Arrays.asList(".doc", ".docx", ".pdf", ".zip", ".rar"));

    DatabaseOverAllHandler dbHandler;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder build;
    private Immunization mImmunization;

    //////RATING//////////
    private boolean isRatingTrue = false;
    private boolean isRatingButtonClicked = false;
    MultiAutoCompleteTextView autoCompleteTextView;
    PackageDoctorSearchAdapter adapter2;
    List<DoctorModelData> totalDoctorPackages;

    ArrayList<Labs> dcLabs;
    Labs selectedDclabs;

    ImageView speechToTextButton;
    private static final int SPEECH_RECOGNITION_CODE = 2;
    private boolean isShowResults = false;
    private boolean isFamilyMemberDoesnotExists = false;
    private boolean isRetakeHraEnabled = false;
    private boolean isGalleryTrue = false;
    private String bookPackageServerTime = "";
    private String bookPackageServerDate = "";
    private String bookPackageRelation = "";
    private FrameLayout expandableLayout;
    private ArrayList<String> selectedDcPackageDateList, selectedDcPackageTimeList;
    private LinearLayout expandableLayoutContainer;
    private int hraStartPosition = 0;
    private String doneString = "";
    private int walletMoney = 0;
    private int googleFitMessagePosition = 0;
    private boolean isPaymentFailed = false;
    private boolean isFirstSelectiveMessages = false;
    private int isFirstSelectiveMessagesPosition = 0;
    private boolean isBottomSheetFragmentClicked = false;
    private int sentDocumentPosition = 0;
    private int dc_collection_price = 0;
    List<Customer> familyMembers;
    ArrayList<String>  familyMemberNamesList;
    private boolean isAgeGenderFlowNeed = false;
    private String customerDOB = "";
    private String customerGender = "";


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTitle();
    }

    private void setTitle() {
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        mToolbar.setTitle("");
        LinearLayout switchFamilyMemberLayout = (LinearLayout) getActivity().findViewById(R.id.activity_add_family_member_layout);
        switchFamilyMemberLayout.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.fragment_chat, null);
        this.rootView = v;

        setRetainInstance(true);

        chatMessageList = new ArrayList<>();
        chatDatabaseModelList = new ArrayList<>();
        tempChatMessageQueue = new ArrayList<>();
        familyMembers = new ArrayList<Customer>();
        familyMemberNamesList= new ArrayList<>();
        dbHandler = new DatabaseOverAllHandler(getActivity());
        customerManager = CustomerManager.getInstance(getActivity());
        mProfileManager = ProfileManager.getInstance(getActivity());
        mCustomer = mProfileManager.getLoggedinCustomer();
        prefs = new PreferenceHelper(getActivity());
        mProfileData = new ProfileData();
        jsonMessageResponse = new JsonMessageResponse();
        requestPackages = new ArrayList<>();
        selectedDcPackageDateList = new ArrayList<>();
        selectedDcPackageTimeList = new ArrayList<>();
        dcLabs = new ArrayList<>();
        selectedDclabs = new Labs();

        serverRequests = new ServerRequests(getActivity(), this, prefs, customerManager);

        initializeViews();
        setupRecyclerView();
        System.out.println("FragmentChat.onCreateView==========" + prefs.getallNotification());
        System.out.println("WakeUpTime==="+prefs.getWakeTime());
        System.out.println("SleepUpTime==="+prefs.getSleepTime());
        // Add interfaces
        imageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageSendLogic();
            }
        });

        imageViewAbort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abortLogic();
            }
        });

        speechToTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        });

        imageViewSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        featureDialog();
                    }
                }, 300);

            }
        });

        imageViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButtonLogic();
            }
        });

        hideKeyboard();
        enableRelativeLayoutSend(true, true, true, false);

        if (prefs.getIsFirstTimeChat()) {
            dbHandler.clearAllChat();
        }

        if (prefs.getIsFirstTimeChat()) {
            setUpLoadingView();
            prefs.setIsFirstTimeChat(false);
            botIntro(true);
        } else {
            Thread thread = new Thread(new GetChatThreadThread());
            thread.start();
        }

        recyclerViewChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
              /*  System.out.println("FragmentChat.onScrolled and reached top dy < 0=" + (dy < 0)
                        + " !isLoading =" + (!isLoading)
                        + " manager.findFirstCompletelyVisibleItemPosition() == 1=" + (manager.findFirstCompletelyVisibleItemPosition() == 1)
                        + " !isPaginationEnd =" + (!isPaginationEnd)
                        + " chatDatabaseModelList.size()>totalDisplayItem=" + (chatDatabaseModelList.size() > totalDisplayItem));
          */      if (dy < 0) {
                    if ((!isLoading && manager.findFirstCompletelyVisibleItemPosition() == 1)) {
                        System.out.println("FragmentChat.onScrolled and reached top");

                        isLoading = true;

                        if (!isPaginationEnd && chatDatabaseModelList.size() > totalDisplayItem) {
                            pageCount++;

                            SpinnerItem spinnerItem = new SpinnerItem();
                            spinnerItem.showProgress(true);
                            outgoingChatMessage = new ChatMessage();
                            outgoingChatMessage.setMessageItem(spinnerItem);

                            mRecyclerAdapter.add(0, outgoingChatMessage);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Thread thread = new Thread(new GetChatThreadThread());
                                    thread.start();
                                }
                            }, 1500);
                        }
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                hideToolbarOnKeyboardUp();
            }
        });


        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        expandableLayoutContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                expandableLayoutContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                expandableLayoutContainer.setVisibility(View.GONE);

                final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                expandableLayoutContainer.measure(widthSpec, heightSpec);

                mAnimator = slideAnimator(0, expandableLayoutContainer.getMeasuredHeight());

                return true;
            }
        });

        return rootView;
    }

    private void setUpLoadingView() {
        recyclerViewChat.setVisibility(View.VISIBLE);
        linearLayoutLoading.setVisibility(View.GONE);
    }

    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak something...");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void hideToolbarOnKeyboardUp() {
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        int screenHeight = rootView.getRootView().getHeight();

        int keypadHeight = screenHeight - r.bottom;

        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad hraHeight.
            mToolbar.setVisibility(View.GONE);
            recyclerViewChat.smoothScrollToPosition(mRecyclerAdapter.getItemCount());
        } else {
            // keyboard is closed
            mToolbar.setVisibility(View.VISIBLE);
        }
    }

    private void featureDialog() {
        //dialogAdditionalFeatures = new Dialog(getActivity());
        dialogAdditionalFeatures = new Dialog(getActivity(), android.R.style.DeviceDefault_Light_ButtonBar);
        dialogAdditionalFeatures.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAdditionalFeatures.setContentView(R.layout.item_message_help_dialog);
        Window window = dialogAdditionalFeatures.getWindow();
        if (window != null) {
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        // window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimationallergy;
        dialogAdditionalFeatures.setCancelable(true);
        dialogAdditionalFeatures.setCanceledOnTouchOutside(true);
        additionalList = (ListView) dialogAdditionalFeatures.findViewById(R.id.dialoglist);
        fullScreenDialogClose = (ImageView) dialogAdditionalFeatures.findViewById(R.id.full_screen_dialog_close);

        chatfeaturesitem = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            ChatFeaturesItem item = new ChatFeaturesItem(images[i], titles[i]);
            chatfeaturesitem.add(item);
        }

        ChatListFeatures adapter = new ChatListFeatures(getActivity(),
                R.layout.chat_box_additional_features_list, chatfeaturesitem);
        additionalList.setAdapter(adapter);

        additionalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSweet = additionalList.getItemAtPosition(position).toString();
                System.out.println("FragmentChat.onItemClick=====" + titles[position]);
                helpText = titles[position];
                editTextMessage.setText(helpText);
                messageSendLogic();
                dialogAdditionalFeatures.dismiss();
            }
        });
        fullScreenDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdditionalFeatures.dismiss();
            }
        });

        dialogAdditionalFeatures.show();

    }

    private void ratingMessage() {
        ChatMessage chatMessage = createIncomingTextMessage("Rate Us");
        final ChatMessage chatMessage2 = createIncomingTextMessage("Tell us your experience ?");
        final ChatMessage chatMessage3 = new ChatMessage();

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Not So Good");
        arrayList.add("Later");
        arrayList.add("Great");
        IncomingHorizontalListMessage incomingHorizontalListMessage = new IncomingHorizontalListMessage(getContext(),
                arrayList, this);
        chatMessage3.setMessageItem(incomingHorizontalListMessage);

        addMessageToQueue(chatMessage);
        addMessageToQueue(chatMessage2);
        addMessageToQueue(chatMessage3);
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

    private void initializeViews() {
        // Setup views
        speechToTextButton = (ImageView) rootView.findViewById(R.id.image_view_mic);
        editTextMessage = (EditText) rootView.findViewById(R.id.edit_text_entry_field);
        imageViewSend = (ImageView) rootView.findViewById(R.id.image_view_send);
        imageViewDone = (ImageView) rootView.findViewById(R.id.image_view_done);
        imageViewAbort = (ImageView) rootView.findViewById(R.id.image_view_abort);
        imageViewSnap = (ImageView) rootView.findViewById(R.id.image_view_snap);
        recyclerViewChat = (RecyclerView) rootView.findViewById(R.id.chat_recycler_view);
        linearLayoutLoading = (LinearLayout) rootView.findViewById(R.id.chat_loading_continer);
        relativeLayoutSend = (RelativeLayout) rootView.findViewById(R.id.header);
        expandableLayout = (FrameLayout) rootView.findViewById(R.id.expandable);
        expandableLayoutContainer = (LinearLayout) rootView.findViewById(R.id.expandableContainer);

        autoCompleteTextView = (MultiAutoCompleteTextView) rootView.findViewById(R.id.auto_complet_search_text);
        autoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        autoCompleteTextView.setThreshold(1);

        imageViewDone.setVisibility(View.GONE);

        slide_down = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        slide_down.setFillAfter(true);
        slide_down.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                try {
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.expandable, new FragmentProgressView()).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationEnd(Animation animation) {
                //relativeLayoutSend.setVisibility(View.GONE);
                expandableLayoutContainer.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= 21) {
                    relativeLayoutSend.setElevation(0.0f);
                } else {

                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        slide_up = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        slide_up.setFillAfter(true);
        slide_up.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                recyclerViewChat.smoothScrollToPosition(mRecyclerAdapter.getItemCount());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        relativeLayoutSend.startAnimation(slide_down);
    }

    LinearLayoutManager manager;

    private void setupRecyclerView() {
        //recyclerViewChat.setHasFixedSize(true);
        manager = new LinearLayoutManager(getActivity());
        manager.setStackFromEnd(true);
        recyclerViewChat.setLayoutManager(manager);

        mRecyclerAdapter = new MessageRecyclerAdapter(chatMessageList, getActivity());
        recyclerViewChat.setAdapter(mRecyclerAdapter);

        recyclerViewChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextMessage.getWindowToken(), 0);

                return false;
            }
        });
    }


    public void abortLogic() {
        String text = "";
        collapse();
        editTextMessage.setText("");
        editTextMessage.setHint("Enter message...");
        autoCompleteTextView.setText("");

        editTextMessage.setFilters(new InputFilter[]{textFilter, new InputFilter.LengthFilter(200)});

        editTextMessage.setInputType(InputType.TYPE_CLASS_TEXT);

        isPaymentFailed = false;

        isFirstSelectiveMessages = true;

        if (isBookAppointmentTrue) {
            text = "Package booking is stopped";
            familyMembers.clear();
            familyMemberNamesList.clear();
            clearBookPackageFlow();
        } else if (isHRATrue || isRetakeHraEnabled) {
            tempChatMessageQueue.clear();
            text = "Health risk assessment is stopped";
            clearHraFlow();
        } else if (isAddFamilyTrue) {
            text = "Adding family member is stopped";
            clearAddFamilyFlow();
        } else if (isaskQuaryDoctorTrue) {
            if (askQueryId == 8) {
                removeMessage(getRecyclerCount() - 1);
            }
            text = "OK, I have cancelled the booking.";
            clearAskQueryFlow();

        }

        enableRelativeLayoutSend(true, true, true, false);
        addMessageToQueue(createIncomingTextMessage(text));
        String loginJson = "{\"type\":" + "\"" + (MessageType.INCOMING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + text + "\"" + "}";
        dbHandler.insertChatData(new ChatDatabaseModel((MessageType.INCOMING_TEXT).toString(), (MessageSource.BOT).toString(), loginJson));

        if(!prefs.getIsFirstTimeRegister()){
            saveAndSendIncomingMessage(new IncomingTextMessage("In order to access the app features completion of Health risk assessment is mandatory",getActivity()));
            botIntro(false);
            return;
        }
        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
        addMessageToQueue(createIncomingTextMessage("Here are some things I can do."));
        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

        ChatMessage featureMessage = new ChatMessage();
        List<String> stringList = new ArrayList<>();
        stringList.add("Digitize medical reports");
        stringList.add("Add family members");
        stringList.add("Book health check");
        stringList.add("Show blood glucose results");
        stringList.add("Consult a doctor");
        stringList.add("Show my father's health status");
        IncomingHorizontalListMessage incomingHorizontalListMessage = new IncomingHorizontalListMessage(getActivity(), stringList, this);
        featureMessage.setMessageItem(incomingHorizontalListMessage);
        addMessageToQueue(featureMessage);

        isFirstSelectiveMessagesPosition = getRecyclerCount();
        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

    }

    private void doneButtonLogic() {
        System.out.println("FragmentChat.doneButtonLogic");
        outgoingChatMessage = new ChatMessage();
        OutgoingTextMessage outgoingTextMessage = new OutgoingTextMessage(doneString, getActivity());

        playOutgoingSound();

        if (doneString.isEmpty()) {
            return;
        }

        if (isBookAppointmentTrue && NetworkCaller.isInternetOncheck(getActivity())) {
            collapse();
            System.out.println("Date test=" + bookAppointmentId+"=========="+doneString);

            proceedBookPackageOutgoingMessages(doneString);
        } else if (isAddFamilyTrue) {
            collapse();
            String loginJson = "{\"type\":" + "\"" + (MessageType.OUTGOING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + doneString + "\"" + "}";
            dbHandler.insertChatData(new ChatDatabaseModel((MessageType.OUTGOING_TEXT).toString(), (MessageSource.SELF).toString(), loginJson));

            ProceedAddFamilyMemberMessages(doneString);
        } else if (isHRATrue) {
            collapse();
            String loginJson = "{\"type\":" + "\"" + (MessageType.OUTGOING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + doneString + "\"" + "}";
            dbHandler.insertChatData(new ChatDatabaseModel((MessageType.OUTGOING_TEXT).toString(), (MessageSource.SELF).toString(), loginJson));

            proceedHraOutgoingMessages(doneString);
        } else if (isaskQuaryDoctorTrue) {
            proceedAskQueryDoctorMessages(doneString);
        } else if (isAgeGenderFlowNeed)
        {
            if(customerDOB.isEmpty())
            {
                DateFormat srcDf = new SimpleDateFormat("MMM dd, yyyy");
                Date date = null;
                try {
                    date = (Date) srcDf.parse(doneString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DateFormat destDf = new SimpleDateFormat("yyyy-MM-dd");
                customerDOB = destDf.format(date);

                prefs.setLoggedinUserDOB(customerDOB);
                System.out.println("FragmentChat.doneButtonLogic selectedDate="+customerDOB);

                String loginJson = "{\"type\":" + "\"" + (MessageType.OUTGOING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + doneString + "\"" + "}";
                dbHandler.insertChatData(new ChatDatabaseModel((MessageType.OUTGOING_TEXT).toString(), (MessageSource.SELF).toString(), loginJson));

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = dateFormat.format(date);

                int years = DateUtils.differenceBtwDate(prefs.getCustomerDOB(), formattedDate);
                System.out.println("FragmentChat.doneButtonLogic isAgeGenderFlowNeed years=" + years);
                familyMemberAge = years;

                createOutgoingMessageNoRemoval(doneString);
                startGenderQuery();
            }else if(customerGender.isEmpty()){
                collapse();
                customerGender = doneString;

                String loginJson = "{\"type\":" + "\"" + (MessageType.OUTGOING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + doneString + "\"" + "}";
                dbHandler.insertChatData(new ChatDatabaseModel((MessageType.OUTGOING_TEXT).toString(), (MessageSource.SELF).toString(), loginJson));

                createOutgoingMessageNoRemoval(customerGender);
                prefs.setLoggedinUserGender(customerGender);
                generateIncomingMessageAnimation();
                serverRequests.updateProfile(customerDOB,customerGender,"");
            }
        } else {
            System.out.println("Date test=" + bookAppointmentId+"=========="+"else part");

            sentPosition = mRecyclerAdapter.getItemCount();

            if (NetworkCaller.isInternetOncheck(getActivity())) {
                System.out.println("test=========" + "yes");
                outgoingTextMessage.setStatus(Config.MESSAGE_SENT);
                outgoingChatMessage.setMessageItem(outgoingTextMessage);
                addNewMessage(outgoingChatMessage, sentPosition);
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

                String loginJson = "{\"type\":" + "\"" + (MessageType.OUTGOING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + doneString + "\"" + "}";
                dbHandler.insertChatData(new ChatDatabaseModel((MessageType.OUTGOING_TEXT).toString(), (MessageSource.SELF).toString(), loginJson));

                sendDataLogic(doneString);
            } else {
                System.out.println("test=========" + "No");
                outgoingTextMessage.setStatus(Config.MESSAGE_FAILED);
                outgoingChatMessage.setMessageItem(outgoingTextMessage);
                addNewMessage(outgoingChatMessage, sentPosition);
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

                enableRelativeLayoutSend(true, true, true, false);

                String loginJson = "{\"type\":" + "\"" + (MessageType.OUTGOING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + doneString + "\"" + "}";
                dbHandler.insertChatData(new ChatDatabaseModel((MessageType.OUTGOING_TEXT).toString(), (MessageSource.SELF).toString(), loginJson));

            }
        }

        editTextMessage.setHint("Enter message...");
        doneString = "";
    }

    private void messageSendLogic() {
        //collapse();
        System.out.println("FragmentChat.messageSendLogic ===" + customerManager.getCurrentCustomer().getIdentification_token());

        if (isFirstSelectiveMessages) {
            isFirstSelectiveMessages = false;
            removeMessage(getRecyclerCount() - 1);
            removeMessage(getRecyclerCount() - 1);
        }

        if (isRatingTrue) {
            if (!isRatingButtonClicked) {
                removeMessage(getRecyclerCount() - 1);
                removeMessage(getRecyclerCount() - 1);
                removeMessage(getRecyclerCount() - 1);
                isRatingTrue = false;
                prefs.setUserRatingCount(0);
                prefs.setUserRatingDayCount(0);
            }
            isRatingButtonClicked = false;
            isRatingTrue = false;
        }

        String text = "";
        if (editTextMessage.getVisibility() == View.VISIBLE) {
            text = ViewUtils.getStringFromEditText(editTextMessage);
        } else if (autoCompleteTextView.getVisibility() == View.VISIBLE) {
            text = ViewUtils.getStringFromEditText(autoCompleteTextView);
        }

        if (TextUtils.isEmpty(text))
            return;

        autoCompleteTextView.setText("");
        editTextMessage.setText("");

        System.out.println("FragmentChat.messageSendLogic text=" + text);
        outgoingChatMessage = new ChatMessage();
        OutgoingTextMessage outgoingTextMessage = new OutgoingTextMessage(text, getActivity());

        if (isBookAppointmentTrue && NetworkCaller.isInternetOncheck(getActivity())) {
            proceedBookPackageOutgoingMessages(text);
            try{
                System.out.println("FragmentChat.messageSendLogic bookAppointmentId = " + bookAppointmentId + " " + " text="+ text + text.charAt(text.length() - 2) + text.charAt(text.length() - 3));
            }catch (Exception e){
                System.out.println("FragmentChat.messageSendLogic bookAppointmentId = " + bookAppointmentId + " " + " text="+ text );
            }
            } else if (isAddFamilyTrue) {
            collapse();
            String loginJson = "{\"type\":" + "\"" + (MessageType.OUTGOING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + text + "\"" + "}";
            dbHandler.insertChatData(new ChatDatabaseModel((MessageType.OUTGOING_TEXT).toString(), (MessageSource.SELF).toString(), loginJson));

            System.out.println("FragmentChat.messageSendLogic family add addFamilyLogicId=" + addFamilyLogicId);
            ProceedAddFamilyMemberMessages(text);
        } else if (isHRATrue) {
            collapse();
            String loginJson = "{\"type\":" + "\"" + (MessageType.OUTGOING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + text + "\"" + "}";
            dbHandler.insertChatData(new ChatDatabaseModel((MessageType.OUTGOING_TEXT).toString(), (MessageSource.SELF).toString(), loginJson));

            proceedHraOutgoingMessages(text);
        } else if (isaskQuaryDoctorTrue) {
            proceedAskQueryDoctorMessages(text);
        } else {
            playOutgoingSound();

            sentPosition = mRecyclerAdapter.getItemCount();

            if (NetworkCaller.isInternetOncheck(getActivity())) {
                System.out.println("test=========" + "yes");
                outgoingTextMessage.setStatus(Config.MESSAGE_SENT);
                outgoingChatMessage.setMessageItem(outgoingTextMessage);
                addNewMessage(outgoingChatMessage, sentPosition);
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

                String loginJson = "{\"type\":" + "\"" + (MessageType.OUTGOING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + text + "\"" + "}";
                dbHandler.insertChatData(new ChatDatabaseModel((MessageType.OUTGOING_TEXT).toString(), (MessageSource.SELF).toString(), loginJson));

                sendDataLogic(text);
            } else {
                System.out.println("test=========" + "No");
                outgoingTextMessage.setStatus(Config.MESSAGE_FAILED);
                outgoingChatMessage.setMessageItem(outgoingTextMessage);
                addNewMessage(outgoingChatMessage, sentPosition);
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                enableRelativeLayoutSend(true, true, true, false);

                String loginJson = "{\"type\":" + "\"" + (MessageType.OUTGOING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + text + "\"" + "}";
                dbHandler.insertChatData(new ChatDatabaseModel((MessageType.OUTGOING_TEXT).toString(), (MessageSource.SELF).toString(), loginJson));

            }
        }
    }

    private void sendDataLogic(final String text) {
        enableRelativeLayoutSend(false, true, false, false);
        System.out.println("FragmentChat.sendDataLogic");
        JSONObject jsonObject = new JSONObject();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String formattedDate = df.format(c.getTime());

        try {
            jsonObject.put("message", text);
            jsonObject.put("timestamp", formattedDate);
            jsonObject.put("sessionId", jsonMessageResponse.getSessionId());
            jsonObject.put("contexts", new JSONArray(jsonMessageResponse.getContexts().toString()));
        } catch (Exception e) {
            System.out.println("FragmentChat.sendDataLogic JsonCreation Exception=" + e);
        }

        try {
            generateIncomingMessageAnimation();

            recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
            System.out.println("FragmentChat.sendDataLogic=======" + jsonObject.toString());
            serverRequests.callChatServerRequest(jsonObject, text);
        } catch (NullPointerException e) {
            generateIncomingMessageAnimation();
            recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
            System.out.println("FragmentChat.sendDataLogic NullPointerException");
            serverRequests.callChatServerRequest(jsonObject, text);
        }
    }

    public void enableRelativeLayoutSend(boolean flag, boolean isEdittextVisible, boolean isQuickOptionVisible, boolean isSendOnlyEnabled) {
        Activity activity = getActivity();
        if (activity != null && isAdded()) {
            System.out.println("FragmentChat.enableRelativeLayoutSend========= flag=" + flag + " askQueryId=" + askQueryId);
            System.out.println("FragmentChat.enableRelativeLayoutSend=========isEdittextVisible=" + isEdittextVisible);
            System.out.println("FragmentChat.enableRelativeLayoutSend=========isQuickOptionVisible=" + isQuickOptionVisible);
            System.out.println("FragmentChat.enableRelativeLayoutSend=========isSendOnlyEnabled=" + isSendOnlyEnabled);

            if (flag) {
                editTextMessage.setTextColor(getResources().getColor(R.color.text_navy));
                editTextMessage.setBackground(getResources().getDrawable(R.drawable.rounded_chat_bg));
                autoCompleteTextView.setBackground(getResources().getDrawable(R.drawable.rounded_chat_bg));

                imageViewSend.setEnabled(true);
                imageViewSend.setFocusable(true);

                if (isEdittextVisible) {
                    editTextMessage.setVisibility(View.VISIBLE);
                    autoCompleteTextView.setVisibility(View.GONE);

                    editTextMessage.setFocusable(true);
                    editTextMessage.setEnabled(true);

                    autoCompleteTextView.setFocusable(false);
                    autoCompleteTextView.setEnabled(false);

                    editTextMessage.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            v.setFocusable(true);
                            v.setFocusableInTouchMode(true);
                            return false;
                        }
                    });
                } else {
                    autoCompleteTextView.setVisibility(View.VISIBLE);
                    editTextMessage.setVisibility(View.GONE);

                    autoCompleteTextView.setFocusable(true);
                    autoCompleteTextView.setEnabled(true);

                    editTextMessage.setFocusable(false);
                    editTextMessage.setEnabled(false);

                    autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            v.setFocusable(true);
                            v.setFocusableInTouchMode(true);
                            return false;
                        }
                    });
                }

                if (isQuickOptionVisible) {
                    imageViewSnap.setVisibility(View.VISIBLE);
                    imageViewAbort.setVisibility(View.GONE);
                    imageViewSnap.setEnabled(true);
                    imageViewSnap.setFocusable(true);
                    imageViewAbort.setEnabled(false);
                    imageViewAbort.setFocusable(false);
                } else {
                    imageViewSnap.setVisibility(View.GONE);
                    imageViewAbort.setVisibility(View.VISIBLE);
                    imageViewSnap.setEnabled(false);
                    imageViewSnap.setFocusable(false);
                    imageViewAbort.setEnabled(true);
                    imageViewAbort.setFocusable(true);
                }

                if (prefs.getIsFirstTimeRegister()) {
                    imageViewSnap.setEnabled(false);
                    imageViewSnap.setFocusable(false);
                    imageViewAbort.setEnabled(false);
                    imageViewAbort.setFocusable(false);
                }

                if (isSendOnlyEnabled) {
                    imageViewDone.setVisibility(View.VISIBLE);
                    imageViewSend.setVisibility(View.GONE);

                    imageViewSend.setEnabled(false);
                    imageViewSend.setFocusable(false);
                    imageViewDone.setEnabled(true);
                    imageViewDone.setFocusable(true);

                    System.out.println("FragmentChat.enableRelativeLayoutSend isSendOnlyEnabled true");
                    editTextMessage.setEnabled(false);
                    editTextMessage.setFocusable(false);
                    autoCompleteTextView.setEnabled(false);
                    autoCompleteTextView.setFocusable(false);
                } else {
                    imageViewDone.setVisibility(View.GONE);
                    imageViewSend.setVisibility(View.VISIBLE);
                    editTextMessage.setEnabled(true);
                    editTextMessage.setFocusable(true);
                    autoCompleteTextView.setEnabled(true);
                    autoCompleteTextView.setFocusable(true);

                    imageViewSend.setEnabled(true);
                    imageViewSend.setFocusable(true);
                    imageViewDone.setEnabled(false);
                    imageViewDone.setFocusable(false);

                }
            } else {
                editTextMessage.setEnabled(false);
                autoCompleteTextView.setEnabled(false);
                imageViewSend.setEnabled(false);

                editTextMessage.setFocusable(false);
                autoCompleteTextView.setFocusable(false);
                imageViewSend.setFocusable(false);

                imageViewSnap.setEnabled(false);
                imageViewSnap.setFocusable(false);

                editTextMessage.setTextColor(getResources().getColor(R.color.opacity));
                editTextMessage.setBackground(getResources().getDrawable(R.drawable.rounded_chat_bg_disabled));
                autoCompleteTextView.setBackground(getResources().getDrawable(R.drawable.rounded_chat_bg_disabled));

                if (isEdittextVisible) {
                    editTextMessage.setVisibility(View.VISIBLE);
                    autoCompleteTextView.setVisibility(View.GONE);
                } else {
                    autoCompleteTextView.setVisibility(View.VISIBLE);
                    editTextMessage.setVisibility(View.GONE);
                }
            }
        }
    }

    public void addNewMessage(ChatMessage chatMessage, int position) {
        System.out.println("FragmentChat.addNewMessage");
        //chatMessageList.add(chatMessage);
        //mRecyclerAdapter.notifyItemInserted(position);

        mRecyclerAdapter.add(position, chatMessage);
        new Handler(Looper.getMainLooper()).post(new Runnable() { // Tried new Handler(Looper.myLopper()) also
            @Override
            public void run() {
                try {
                    new AddNewMessageTask(chatMessageList, mRecyclerAdapter, recyclerViewChat, 4).execute();
                } catch (Exception e) {
                    System.out.println("FragmentChat.addNewMessage exception e=" + e.toString());
                    e.printStackTrace();
                }
            }
        });


    }

    public void removeMessage(int position) {
        System.out.println("FragmentChat.removeMessage");
        mRecyclerAdapter.delete(position);
        //new AddNewMessageTask(chatMessageList, mRecyclerAdapter, recyclerViewChat).execute();
        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
    }

    public void generateIncomingMessageAnimation() {
        System.out.println("FragmentChat.generateIncomingMessageAnimation");
        ChatMessage chatMessage = new ChatMessage();
        IncomingTextProgressMessage incomingTextProgressMessage = new IncomingTextProgressMessage(getActivity());
        chatMessage.setMessageItem(incomingTextProgressMessage);
        addNewMessage(chatMessage, getRecyclerCount());

    }

    private void startAddFamilyLogic(int id) {
        System.out.println("FragmentChat.startAddFamilyLogic id=" + id);
        String hisher = "";
        int timeWait = 0;

        switch (id) {
            case 0:
                enableRelativeLayoutSend(false, true, false, true);
                final ArrayList<String> listRelationship = new ArrayList<>();

                listRelationship.add("Father");
                listRelationship.add("Mother");

                if (mCustomer.getGender().equalsIgnoreCase("male")) {
                    listRelationship.add("Wife");
                } else {
                    listRelationship.add("Husband");
                }

                listRelationship.add("Son");
                listRelationship.add("Daughter");
                listRelationship.add("Brother");
                listRelationship.add("Sister");
                listRelationship.add("Other");


                for (int i = 0; i < mCustomer.getFamily_medical_histories().size(); i++) {
                    switch (mCustomer.getFamily_medical_histories().get(i).getRelation()) {
                        case "Father":
                            if (mCustomer.getFamily_medical_histories().get(i).getFamily_member_id() != null)
                                listRelationship.remove("Father");
                            break;
                        case "Mother":
                            if (mCustomer.getFamily_medical_histories().get(i).getFamily_member_id() != null)
                                listRelationship.remove("Mother");
                            break;
                        case "Spouse":
                            if (mCustomer.getFamily_medical_histories().get(i).getFamily_member_id() != null)
                                listRelationship.remove("Spouse");
                            listRelationship.remove("Husband");
                            listRelationship.remove("Wife");
                            break;
                        case "Husband":
                            if (mCustomer.getFamily_medical_histories().get(i).getFamily_member_id() != null)
                                listRelationship.remove("Husband");
                            break;
                        case "Wife":
                            if (mCustomer.getFamily_medical_histories().get(i).getFamily_member_id() != null)
                                listRelationship.remove("Wife");
                            break;
                    }
                }

                System.out.println("FragmentChat.startAddFamilyLogic listRelationship = " + listRelationship.toString());
                /*final ChatMessage temp4 = new ChatMessage();
                IncomingHorizontalListMessage textMessage24 = new IncomingHorizontalListMessage(getActivity(),listRelationship,this);
                temp4.setMessageItem(textMessage24);
*/
                final Fragment fragmentRelation = new FragmentSingleWheelView(this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expand(fragmentRelation, listRelationship, null, true, "", "");
                    }
                }, 300);

                //addMessageToQueue(temp4);

                break;
            case 1:
                String questionGender;
                if (familyMemberRelation.equalsIgnoreCase("other")) {
                    questionGender = "Select gender?";
                } else {
                    questionGender = getResources().getString(R.string.add_family_What_is) + " " + familyMemberRelation.toLowerCase() + "'s gender?";
                }


                saveAndSendIncomingMessage(new IncomingTextMessage(questionGender, getActivity()));

                final ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("Male");
                arrayList.add("Female");

                final Fragment fragmentGender = new FragmentSingleWheelView(this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expand(fragmentGender, arrayList, null, true, "", "");
                    }
                }, 1500);

                break;
            case 2:
                String questionName;
                if (familyMemberRelation.equalsIgnoreCase("other")) {
                    if (familyMemberGender.equalsIgnoreCase("male")) {
                        questionName = "What's his name?";
                    } else {
                        questionName = "What's her name?";
                    }
                } else {
                    questionName = getResources().getString(R.string.add_family_What_is_your) + " " + familyMemberRelation.toLowerCase() + "'s name?";
                }

                saveAndSendIncomingMessage(new IncomingTextMessage(questionName, getActivity()));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        editTextMessage.setFilters(new InputFilter[]{textFilter, new InputFilter.LengthFilter(50)});
                        editTextMessage.setHint("Enter name");
                        editTextMessage.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                        System.out.println("FragmentChat.enableRelativeLayoutSend in case2 addFamilyMember");
                        enableRelativeLayoutSend(true, true, false, false);
                    }
                }, 1000);

                break;
            case 3:
                enableRelativeLayoutSend(false, true, false, false);

                saveAndSendIncomingMessage(new IncomingTextMessage(
                        getResources().getString(R.string.add_family_What_is) + " " + familyMemberName.toLowerCase() + "'s date of birth?",
                        getActivity()));


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR) - 18;
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                        Date date = null;
                                        try {
                                            date = (Date) formatter.parse(dayOfMonth + "-" +
                                                    (monthOfYear + 1) + "-" + year);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        DateFormat destDf = new SimpleDateFormat("EEE, d MMM yyyy");

                                        saveAndSendOutgoingMessage(new OutgoingTextMessage(destDf.format(date), getActivity()));
                                        //createOutgoingMessageNoRemoval(destDf.format(date));

                                        Calendar c = Calendar.getInstance();
                                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                        String formattedDate = df.format(c.getTime());

                                        int years = DateUtils.differenceBtwDate(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, formattedDate);
                                        System.out.println("FragmentChat.onCardWithImagePositiveClick years=" + years);

                                        familyMemberDOF = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                        familyMemberAge = years;

                                        if (familyMemberAge >= 18) {
                                            addFamilyLogicId = addFamilyLogicId + 1;
                                        } else {
                                            addFamilyLogicId = addFamilyLogicId + 3;
                                        }
                                        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                                        startAddFamilyLogic(addFamilyLogicId);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.setCancelable(false);
                        datePickerDialog.setCanceledOnTouchOutside(false);
                        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                                "",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getActivity(), "Select " + familyMemberName.toLowerCase() + "'s date of birth", Toast.LENGTH_LONG).show();
                                    }
                                });

                        datePickerDialog.show();
                    }
                }, 2400);
                break;
            case 4:
                if (familyMemberGender.equalsIgnoreCase("female")) {
                    hisher = "her";
                } else {
                    hisher = "his";
                }

                saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.add_family_What_is) + " " + familyMemberName.toLowerCase() + "'s mobile number?", getActivity()));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        editTextMessage.setFilters(new InputFilter[]{numberFilter, new InputFilter.LengthFilter(10)});
                        editTextMessage.setHint("Enter the mobile number");
                        editTextMessage.setInputType(InputType.TYPE_CLASS_NUMBER);
                        enableRelativeLayoutSend(true, true, false, false);
                        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                    }
                }, 1000);
                break;
            case 5:
                enableRelativeLayoutSend(false, true, false, false);

                saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.add_family_What_is) + " " + familyMemberName.toLowerCase() + "'s email address? This" +
                        " will be used to notify " + familyMemberName.toLowerCase() + " about " + hisher + " health recommendations", getActivity()));

                ChatMessage temp3 = new ChatMessage();
                ArrayList<String> list = new ArrayList<>();
                list.add("Skip");
                list.add("Yeah sure");

                IncomingHorizontalListMessage textMessage23 = new IncomingHorizontalListMessage(getActivity(), list, this);
                temp3.setMessageItem(textMessage23);

                addMessageToQueue(temp3);
                break;
            case 6:
                generateIncomingMessageAnimation();

                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

                JSONObject object = new JSONObject();
                try {

                    object.put("first_name", familyMemberName);
                    object.put("last_name", "");
                    object.put("email", familyMemberEmail);
                    object.put("mobile_number", familyMemberMobile);
                    object.put("date_of_birth", familyMemberDOF);
                    String strGuardianId;
                    strGuardianId = mCustomer.getId();
                    object.put("guardian_id", strGuardianId);
                    if (familyMemberRelation.equalsIgnoreCase("Brother") || familyMemberRelation.equalsIgnoreCase("Sister")) {
                        object.put("relation", "Sibling");
                    } else if (familyMemberRelation.equalsIgnoreCase("Wife") || familyMemberRelation.equalsIgnoreCase("Husband")) {
                        object.put("relation", "Spouse");
                    } else if (familyMemberRelation.equalsIgnoreCase("Son") || familyMemberRelation.equalsIgnoreCase("Daughter")) {
                        object.put("relation", "Child");
                    } else {
                        object.put("relation", familyMemberRelation);
                    }
                    object.put("gender", familyMemberGender);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                editTextMessage.setFilters(new InputFilter[]{textFilter, new InputFilter.LengthFilter(1000)});
                editTextMessage.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextMessage.setHint("Enter message...");

                if (NetworkCaller.isInternetOncheck(getActivity())) {
                    mProfileData = new ProfileData();
                    enableRelativeLayoutSend(false, true, false, false);

                    serverRequests.addFamilyMemberServerRequest(object);
                } else {
                    isAddFamilyTrue = false;
                    addFamilyLogicId = -1;
                    enableRelativeLayoutSend(true, true, true, false);

                    saveAndSendIncomingMessage(new IncomingTextMessage("Internet is not available. Failed to add the member." + " " + getEmojiByUnicode(0x1F635), getActivity()));
                }


                break;
        }

    }

    private void startCapability() {
        saveAndSendIncomingMessage(new IncomingTextMessage("Here are few things I can do", getActivity()));
        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

        ArrayList<String> list = new ArrayList<>();
        list.add("Digitize medical reports");
        list.add("Add a family member");
        list.add("Book a health check");
        list.add("Show blood glucose results");
        list.add("Consult a doctor");
        list.add("Show my father's health status");

        saveAndSendIncomingMessage(new IncomingHorizontalListMessage(getActivity(), list, this));
        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

        enableRelativeLayoutSend(true, true, true, false);
    }

    private void startAskQueryToDoctor(int id) {
        System.out.println("FragmentChat.startAskQueryToDoctor id=" + id);
        switch (id) {
            case 0:
                if (askQuerySpeciality.isEmpty()) {
                    saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.ask_select_a_specialization), getActivity()));

                    recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

                    adapter2 = new PackageDoctorSearchAdapter(getActivity(), autoCompleteTextView.getText().toString(), autoCompleteTextView, this);
                    autoCompleteTextView.setAdapter(adapter2);
                    autoCompleteTextView.setThreshold(1);

                    System.out.println("FragmentChat.startBookPackage case 1 bookAppointmentId=" + bookAppointmentId);
                    enableRelativeLayoutSend(true, false, false, false);
                } else {
                    //prefs.setPackageIDSearch(askQuerySpeciality);
                    askQueryId = askQueryId + 1;
                    startAskQueryToDoctor(askQueryId);
                }
                break;
            case 1:
                System.out.println("FragmentChat.startAskQueryToDoctor askQueryMode=" + askQueryMode);
                if (askQueryMode.isEmpty()) {
                    saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.ask_how_to_ask_query), getActivity()));

                    final ArrayList<String> qyeryModeList = new ArrayList<>();

                    if (askQueryTime.isEmpty() && askQueryDate.isEmpty()) {
                        qyeryModeList.add("Ask a Query");
                    }
                    qyeryModeList.add("Audio Call");
                    qyeryModeList.add("Video Call");

                    final Fragment fragmentMode = new FragmentSingleWheelView(this);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            expand(fragmentMode, qyeryModeList, null, true, "", "");
                        }
                    }, 1300);

                } else {
                    if (askQueryMode.equalsIgnoreCase("text")) {
                        askQueryId = askQueryId + 2;
                        startAskQueryToDoctor(askQueryId);
                    } else {
                        System.out.println("FragmentChat.startAskQueryToDoctor AUDIO?VIDEO case");
                        saveAndSendIncomingMessage(new IncomingTextMessage("Select a doctor to consult", getActivity()));

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                generateIncomingMessageAnimation();
                                serverRequests.getPackageDoctorList(askQuerySpeciality);
                            }
                        }, 1300);
                    }
                }
                break;
            case 2:
                saveAndSendIncomingMessage(new IncomingHorizontalDoctorCardListMessage(getActivity(), totalDoctorPackages, this));
                break;
            case 3:

                //saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.ask_enter_your_query),getActivity()));

                addMessageToQueue(createIncomingTextMessage(getResources().getString(R.string.ask_enter_your_query)));
                addMessageToQueue(createIncomingTextMessage(getResources().getString(R.string.ask_enter_your_query_message_two)));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enableRelativeLayoutSend(true, true, false, false);
                    }
                }, 1500);
                break;
            case 4:
                String header = getResources().getString(R.string.ask_would_you_like_to_share_profile);
                String subheader = getResources().getString(R.string.ask_would_you_like_to_share_profile_description);
                String primary_button_name = "";
                String secondary_button_name = "";
                String url = " ";
                String secondary_button_action = "";
                String primary_button_action = "";
                String additional = " ";
                CardViewModel cardViewModel = new CardViewModel(header, subheader, primary_button_name,
                        secondary_button_name, url, 0, additional, primary_button_action, secondary_button_action);


                saveAndSendIncomingMessage(new IncomingCardWithTextMessage(cardViewModel, getActivity(), this));


                final ArrayList<String> qyeryShareList = new ArrayList<>();
                qyeryShareList.add("Yes");
                qyeryShareList.add("No");

                final Fragment fragmentShare = new FragmentSingleWheelView(this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expand(fragmentShare, qyeryShareList, null, true, "", "");
                    }
                }, 1500);

                break;
            case 5:
                enableRelativeLayoutSend(false, true, false, false);
                if (!askQueryDate.isEmpty()) {
                    askQueryId = askQueryId + 1;
                    startAskQueryToDoctor(askQueryId);
                } else {
                    saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.ask_book_available_date), getActivity()));

                    final ArrayList<String> timeFortextQuery = new ArrayList<>();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    for (int i = 1; i < 7; i++) {
                        Calendar calendar = new GregorianCalendar();
                        calendar.add(Calendar.DATE, i);
                        String day = sdf.format(calendar.getTime());
                        timeFortextQuery.add(day);
                        System.out.println("FragmentChat.onCreateView=======" + day);
                    }

                    final Fragment fragmentQueryDate = new FragmentSingleWheelView(this);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            expand(fragmentQueryDate, timeFortextQuery, null, true, "", "");
                        }
                    }, 1500);
                }
                break;
            case 6:
                enableRelativeLayoutSend(false, true, false, false);
                System.out.println("check======" + askQueryDate);
                if (!askQueryTime.isEmpty()) {
                    askQueryId = askQueryId + 1;
                    startAskQueryToDoctor(askQueryId);
                } else {
                    saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.ask_select_a_time), getActivity()));

                    final ArrayList<String> timeSlots = new ArrayList<>();
                    timeSlots.add("08:00AM-10:00AM");
                    timeSlots.add("10:00AM-12:00PM");
                    timeSlots.add("12:00PM-02:00PM");
                    timeSlots.add("02:00PM-04:00PM");
                    timeSlots.add("04:00PM-06:00PM");
                    timeSlots.add("06:00PM-08:00PM");

                    final Fragment fragmentQueryTime = new FragmentSingleWheelView(this);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            expand(fragmentQueryTime, timeSlots, null, true, "", "");
                        }
                    }, 1500);
                }

                break;
            case 7:
                generateIncomingMessageAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        serverRequests.getPackagePaymentAmount(TagValues.DOCTOR_FEE, TagValues.PACKAGE_QUERY_TYPE, askQuerySpeciality, "consult");
                    }
                }, 2000);
                break;
            case 8:
                if (!isPaymentFailed) {
                    try {
                        askQueryTotalPackagePrice = Integer.parseInt(prefs.getPackageMrp());
                    } catch (NumberFormatException e) {
                        float f = Float.parseFloat(prefs.getPackageMrp());
                        askQueryTotalPackagePrice = (int) f;
                    }
                }
                System.out.println("FragmentChat.startAskQueryToDoctor askQueryTotalPackagePrice=" + askQueryTotalPackagePrice);

                if (walletMoney > 0) {
                    if (walletMoney > askQueryTotalPackagePrice) {
                        askQueryPackagePrice = 0;
                    } else {
                        askQueryPackagePrice = askQueryTotalPackagePrice - walletMoney;
                    }
                } else {
                    askQueryPackagePrice = askQueryTotalPackagePrice;
                }

                if (askQueryPackagePrice > 0) {
                    saveAndSendIncomingMessage(new IncomingTextMessage("Proceeding to payment. Please wait for payment window", getActivity()));

                    recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startPayment(askQueryPackagePrice * 100, "Doctor consultation");
                        }
                    }, 1500);
                } else {
                    generateIncomingMessageAnimation();
                    paymentSuccessServerCall("");
                }
                break;
        }
    }

    private void startHRALogic(int id) {
        System.out.println("FragmentChat.startHRALogic id=" + id);
        switch (id) {
            case 0:
                imageViewSnap.setEnabled(false);
                imageViewSnap.setFocusable(false);
                imageViewDone.setEnabled(false);
                imageViewDone.setFocusable(false);

                saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.add_family_hra_yes), getActivity()));

                String heightQuestion = getResources().getString(R.string.add_family_What_is) + " " + familyMemberName.toLowerCase() + "'s height?";
                if (prefs.getIsFirstTimeRegister() || isRetakeHraEnabled) {
                    heightQuestion = getResources().getString(R.string.add_family_What_is) + " " + "your height?";
                }

                final ArrayList<String> feetList = new ArrayList<String>();
                for (int i = 1; i < 8; i++) {
                    feetList.add(i + "");
                }

                final ArrayList<String> inchList = new ArrayList<String>();
                for (int i = 0; i < 12; i++) {
                    inchList.add(i + "");
                }

                saveAndSendIncomingMessage(new IncomingTextMessage(heightQuestion, getActivity()));

                final Fragment fragmentHeight = new FragmentDoubleWheelView(this);
                currentFragment = fragmentHeight;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expand(fragmentHeight, feetList, inchList, false, "Feet", "Inch");
                        enableRelativeLayoutSend(true, true, false, true);
                    }
                }, 3500);
                break;
            case 1:
                imageViewAbort.setEnabled(false);
                imageViewAbort.setFocusable(false);
                imageViewDone.setEnabled(false);
                imageViewDone.setFocusable(false);

                String weightQuestion = getResources().getString(R.string.add_family_What_is) + " " + familyMemberName.toLowerCase() + "'s weight?";
                if (prefs.getIsFirstTimeRegister() || isRetakeHraEnabled) {
                    weightQuestion = getResources().getString(R.string.add_family_What_is) + " " + "your weight?";
                }

                saveAndSendIncomingMessage(new IncomingTextMessage(weightQuestion, getActivity()));

                final ArrayList<String> weightList = new ArrayList<String>();
                for (int i = 1; i < 220; i++) {
                    if (i < 10) {
                        weightList.add("0" + i);
                    } else {
                        weightList.add("" + i);
                    }
                }

                final Fragment fragmentWeight = new FragmentSingleWheelView(this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expand(fragmentWeight, weightList, null, true, "Kgs", "");
                        enableRelativeLayoutSend(true, true, false, true);
                    }
                }, 1500);

                break;
            case 2:
                imageViewAbort.setEnabled(false);
                imageViewAbort.setFocusable(false);
                imageViewDone.setEnabled(false);
                imageViewDone.setFocusable(false);

                String waistQuestion = getResources().getString(R.string.add_family_What_is) + " " + familyMemberName.toLowerCase() + "'s waist?";
                if (prefs.getIsFirstTimeRegister() || isRetakeHraEnabled) {
                    waistQuestion = getResources().getString(R.string.add_family_What_is) + " " + "your waist?";
                }

                saveAndSendIncomingMessage(new IncomingTextMessage(waistQuestion, getActivity()));

                final ArrayList<String> waistList = new ArrayList<String>();
                waistList.add("26");
                waistList.add("28");
                waistList.add("30");
                waistList.add("32");
                waistList.add("34");
                waistList.add("36");
                waistList.add("38");
                waistList.add("40");
                waistList.add("42");
                waistList.add("44");
                waistList.add("46");


                final Fragment fragmentWaist = new FragmentSingleWheelView(this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expand(fragmentWaist, waistList, null, true, "Inch", "");
                        enableRelativeLayoutSend(true, true, false, true);
                    }
                }, 1500);

                break;/*
            case 3:
                imageViewAbort.setEnabled(false);
                imageViewAbort.setFocusable(false);
                imageViewDone.setEnabled(false);
                imageViewDone.setFocusable(false);

                String bloodGroupQuestion = getResources().getString(R.string.add_family_What_is) + " " + familyMemberName.toLowerCase() + "'s blood group?";
                if (prefs.getIsFirstTimeRegister() || isRetakeHraEnabled) {
                    bloodGroupQuestion = getResources().getString(R.string.add_family_What_is) + " " + "your blood group?";
                }

                saveAndSendIncomingMessage(new IncomingTextMessage(bloodGroupQuestion, getActivity()));

                final ArrayList<String> bloodGroupList = new ArrayList<String>();
                bloodGroupList.add("A+");
                bloodGroupList.add("A-");
                bloodGroupList.add("B+");
                bloodGroupList.add("B-");
                bloodGroupList.add("AB+");
                bloodGroupList.add("AB-");
                bloodGroupList.add("O+");
                bloodGroupList.add("O-");

                final Fragment fragmentBloodGroup = new FragmentSingleWheelView(this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expand(fragmentBloodGroup, bloodGroupList, null, true, "", "");
                        enableRelativeLayoutSend(true, true, false, true);
                    }
                }, 1500);

                break;*/
            case 4:
                imageViewAbort.setEnabled(false);
                imageViewAbort.setFocusable(false);
                imageViewDone.setEnabled(false);
                imageViewDone.setFocusable(false);

                String exerciseQuestion = "How often " + familyMemberName.toLowerCase() + " exercise in a week?";
                String lifestyleQuestion = "A little about " + familyMemberName.toLowerCase() + "'s lifestyle";
                if (prefs.getIsFirstTimeRegister() || isRetakeHraEnabled) {
                    exerciseQuestion = "How often you exercise in a week?";
                    lifestyleQuestion = "A little about your lifestyle";
                }

                saveAndSendIncomingMessage(new IncomingTextMessage(lifestyleQuestion, getActivity()));
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.add_family_lifestyle_second), getActivity()));
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                saveAndSendIncomingMessage(new IncomingTextMessage(exerciseQuestion, getActivity()));
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

                final ArrayList<String> exerciseList = new ArrayList<>();
                exerciseList.add("Never");
                exerciseList.add("Occasionally");
                exerciseList.add("Frequently");

                final Fragment fragmentExercise = new FragmentSingleWheelView(this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expand(fragmentExercise, exerciseList, null, true, "", "");
                        enableRelativeLayoutSend(true, true, false, true);
                    }
                }, 4500);

                break;
            case 5:
                imageViewAbort.setEnabled(false);
                imageViewAbort.setFocusable(false);
                imageViewDone.setEnabled(false);
                imageViewDone.setFocusable(false);


                String smokeQuestion = "How often " + familyMemberName.toLowerCase() + " smokes in a week?";
                if (prefs.getIsFirstTimeRegister() || isRetakeHraEnabled) {
                    smokeQuestion = "How often you smokes in a week?";
                }

                saveAndSendIncomingMessage(new IncomingTextMessage(smokeQuestion, getActivity()));

                final ArrayList<String> smokeList = new ArrayList<>();
                smokeList.add("Never");
                smokeList.add("Occasionally");
                smokeList.add("Frequently");

                final Fragment fragmentSmoke = new FragmentSingleWheelView(this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expand(fragmentSmoke, smokeList, null, true, "", "");
                        enableRelativeLayoutSend(true, true, false, true);
                    }
                }, 1500);
                break;
            case 6:
                imageViewAbort.setEnabled(false);
                imageViewAbort.setFocusable(false);
                imageViewDone.setEnabled(false);
                imageViewDone.setFocusable(false);

                String alcoholQuestion = "How often " + familyMemberName.toLowerCase() + " drinks alcohol in a week?";
                if (prefs.getIsFirstTimeRegister() || isRetakeHraEnabled) {
                    alcoholQuestion = "How often you drinks alcohol in a week?";
                }

                saveAndSendIncomingMessage(new IncomingTextMessage(alcoholQuestion, getActivity()));

                final ArrayList<String> alcoholList = new ArrayList<>();
                alcoholList.add("Never");
                alcoholList.add("Occasionally");
                alcoholList.add("Frequently");

                final Fragment fragmentAlcohol = new FragmentSingleWheelView(this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expand(fragmentAlcohol, alcoholList, null, true, "", "");
                        enableRelativeLayoutSend(true, true, false, true);
                    }
                }, 1500);
                break;
            case 7:
                imageViewAbort.setEnabled(false);
                imageViewAbort.setFocusable(false);
                imageViewDone.setEnabled(false);
                imageViewDone.setFocusable(false);

                saveAndSendIncomingMessage(new IncomingTextMessage("Family History", getActivity()));

                String fatherConditionQuestion = "Did " + familyMemberName.toLowerCase() + "'s " + getResources().getString(R.string.add_family_history_father_condition);
                String familyHistoryQuestion = "Genetic profile helps to forecast " + familyMemberName.toLowerCase() + "'s exposure to health risks";
                if (prefs.getIsFirstTimeRegister() || isRetakeHraEnabled) {
                    fatherConditionQuestion = "Did your " + getResources().getString(R.string.add_family_history_father_condition);
                    familyHistoryQuestion = getResources().getString(R.string.add_family_history_second);
                }

                saveAndSendIncomingMessage(new IncomingTextMessage(familyHistoryQuestion, getActivity()));
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                saveAndSendIncomingMessage(new IncomingTextMessage(fatherConditionQuestion, getActivity()));
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

                final Fragment fragmentFatherHistory = new FragmentHraFamilyHistoryView(this, true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expand(fragmentFatherHistory, null, null, false, "", "");
                        enableRelativeLayoutSend(true, true, false, true);
                        editTextMessage.setHint("Select all that apply...");
                    }
                }, 4500);
                break;
            case 8:
                imageViewAbort.setEnabled(false);
                imageViewAbort.setFocusable(false);
                imageViewDone.setEnabled(false);
                imageViewDone.setFocusable(false);

                String motherConditionQuestion = "Did " + familyMemberName.toLowerCase() + "'s " + getResources().getString(R.string.add_family_history_mother_condition);
                if (prefs.getIsFirstTimeRegister() || isRetakeHraEnabled) {
                    motherConditionQuestion = "Did your " + getResources().getString(R.string.add_family_history_mother_condition);
                }

                saveAndSendIncomingMessage(new IncomingTextMessage(motherConditionQuestion, getActivity()));

                final Fragment fragmentMotherHistory = new FragmentHraFamilyHistoryView(this, true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expand(fragmentMotherHistory, null, null, true, "", "");
                        enableRelativeLayoutSend(true, true, false, true);
                        editTextMessage.setHint("Select all that apply...");
                    }
                }, 2500);

                break;

            case 9:
                String bgQuestion = getResources().getString(R.string.add_family_What_is) + " " + familyMemberName.toLowerCase() + "'s blood glucose?";
                String currentStatusQuestion = familyMemberName.toLowerCase() + "'s current health status";
                if (prefs.getIsFirstTimeRegister() || isRetakeHraEnabled) {
                    bgQuestion = getResources().getString(R.string.add_family_What_is) + " your blood glucose?";
                    currentStatusQuestion = getResources().getString(R.string.add_family_current_health_status);
                }

                saveAndSendIncomingMessage(new IncomingTextMessage(currentStatusQuestion, getActivity()));
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.add_family_current_health_status_second), getActivity()));
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                saveAndSendIncomingMessage(new IncomingTextMessage(bgQuestion, getActivity()));
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

                final ChatMessage bloodGlucoseListChatMessage = new ChatMessage();
                ArrayList<String> bloodGlucoseList = new ArrayList<>();
                bloodGlucoseList.add("I don't know");
                bloodGlucoseList.add("I know");
                IncomingHorizontalListMessage bloodGlucoseIncomingHorizontalListMessage = new IncomingHorizontalListMessage(getActivity(), bloodGlucoseList, this);
                bloodGlucoseListChatMessage.setMessageItem(bloodGlucoseIncomingHorizontalListMessage);

                addMessageToQueue(bloodGlucoseListChatMessage);

                break;
            case 10:
                String bpQuestion = getResources().getString(R.string.add_family_What_is) + " " + familyMemberName.toLowerCase() + "'s blood pressure?";
                if (prefs.getIsFirstTimeRegister() || isRetakeHraEnabled) {
                    bpQuestion = getResources().getString(R.string.add_family_What_is) + " your blood pressure?";
                }
                saveAndSendIncomingMessage(new IncomingTextMessage(bpQuestion, getActivity()));

                ChatMessage systolicListChatMessage = new ChatMessage();
                ArrayList<String> systolicList = new ArrayList<>();
                systolicList.add("I don't know");
                systolicList.add("I know");

                IncomingHorizontalListMessage systolicListIncomingHorizontalListMessage = new IncomingHorizontalListMessage(getActivity(), systolicList, this);
                systolicListChatMessage.setMessageItem(systolicListIncomingHorizontalListMessage);

                addMessageToQueue(systolicListChatMessage);

                break;
            case 11:
                String ownConditionQuestion = "Has " + familyMemberName.toLowerCase() + " is ever being treated for any of the following conditions?";
                if (prefs.getIsFirstTimeRegister() || isRetakeHraEnabled) {
                    ownConditionQuestion = "Have you" + " ever treated for any of the following conditions?";
                }
                saveAndSendIncomingMessage(new IncomingTextMessage(ownConditionQuestion, getActivity()));

                final Fragment fragmentOwnHistory = new FragmentHraFamilyHistoryView(this, false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        expand(fragmentOwnHistory, null, null, true, "", "");
                        editTextMessage.setHint("Select all that apply...");
                    }
                }, 2500);
                break;
        }
    }

    private void startBookPackage(final int bookAppointmentId) {
        System.out.println("Date test=" + bookAppointmentId);

        System.out.println("FragmentChat.startBookPackage bookAppointmentId=" + bookAppointmentId);
        switch (bookAppointmentId) {
            case 0:

                try {
                    saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.book_what_kind_of_help), getActivity()));
                    recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                    autoCompleteTextView.setAdapter(new MultitextCompleteViewSearchAdapter(getActivity())); // 'this' is Activity instance
                    autoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                    autoCompleteTextView.setThreshold(1);
                    autoCompleteTextView.setHint("Search package...");
                    System.out.println("FragmentChat.startBookPackage case 1 bookAppointmentId=" + bookAppointmentId);
                    enableRelativeLayoutSend(true, false, false, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:

                enableRelativeLayoutSend(false, true, false, false);

                /*ChatMessage chatMessageDc = createIncomingTextMessage(getResources().getString(R.string.book_select_diagnostic_center));
                addMessageToQueue(chatMessageDc);*/

                ChatMessage chatMessageDc = new ChatMessage();
                String title = " ";
                String subtitle = getResources().getString(R.string.book_select_diagnostic_center);
                String primaryButton = "Change Location";
                String secondaryButton = " ";
                String primaryAction = "Change Location Package";
                String secondaryAction = " ";
                String url = "";
                int resource = 0;

                CardViewModel cardViewModel = new CardViewModel(title, subtitle, primaryButton, secondaryButton, url,
                        resource, "", primaryAction, secondaryAction);
                IncomingCardWithTextAndButtonMessage incomingCardWithTextAndButtonMessage = new IncomingCardWithTextAndButtonMessage(cardViewModel,
                        getActivity(), this, this);
                chatMessageDc.setMessageItem(incomingCardWithTextAndButtonMessage);
                addMessageToQueue(chatMessageDc);

                System.out.println("FragmentChat.startBookPackage dcLabs=" + dcLabs.size());
                ChatMessage chatMessageDcCard = new ChatMessage();
                IncomingHorizontalDcCardListMessage incomingHorizontalDcCardListMessage = new IncomingHorizontalDcCardListMessage(dcLabs, getActivity(), this);
                chatMessageDcCard.setMessageItem(incomingHorizontalDcCardListMessage);
                addMessageToQueue(chatMessageDcCard);

                break;
            case 2:
                final Fragment fragmentPackageDate = new FragmentSingleWheelView(this);
                System.out.println("FragmentChat.startBookPackage bookPackageServerDate=" + bookPackageServerDate);
                if (bookPackageServerDate.isEmpty()) {
                    System.out.println("Date test=" + bookAppointmentId+"======="+"Date empty");
                    saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.book_select_a_date), getActivity()));

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            expand(fragmentPackageDate, selectedDcPackageDateList, null, true, "", "");
                        }
                    }, 1500);
                } else {
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = (Date) formatter.parse(bookPackageServerDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    DateFormat destDf = new SimpleDateFormat("EEE, d MMM yyyy");

                    System.out.println("FragmentChat.startBookPackage destDf.format(date)=" + destDf.format(date));
                    if (selectedDcPackageDateList.contains(destDf.format(date))) {
                        if (selectedDclabs.getSlots().isJsonObject()) {
                            Set<Entry<String, JsonElement>> ens = ((JsonObject) selectedDclabs.getSlots()).entrySet();
                            if (ens != null) {
                                selectedDcPackageTimeList.clear();
                                for (Entry<String, JsonElement> en : ens) {
                                    if (bookPackageServerDate.equals(en.getKey())) {
                                        JsonArray jarr = en.getValue().getAsJsonArray();
                                        for (int i = 0; i < jarr.size(); i++) {
                                            selectedDcPackageTimeList.add(jarr.get(i).getAsString());
                                        }
                                    }
                                }
                            }
                        }
                        System.out.println("FragmentChat.startBookPackage selectedDcPackageTimeList=" + selectedDcPackageTimeList.toString());
                        packageDate = destDf.format(date);
                        System.out.println("FragmentChat.startBookPackage packageDate=" + packageDate);
                        this.bookAppointmentId = this.bookAppointmentId + 1;
                        startBookPackage(this.bookAppointmentId);
                    } else {
                        saveAndSendIncomingMessage(new IncomingTextMessage("Booking is unavailable on " + destDf.format(date) + ". Select some other date.", getActivity()));

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                expand(fragmentPackageDate, selectedDcPackageDateList, null, true, "", "");
                            }
                        }, 1500);
                    }
                }
                break;
            case 3:
                System.out.println("Date test=" + bookAppointmentId+"======="+"case 3");

                System.out.println("FragmentChat.startBookPackage bookPackageServerTime=" + bookPackageServerTime + " bookPackageServerTime=" + selectedDcPackageTimeList.toString());
                if (bookPackageServerTime.isEmpty()) {
                    System.out.println("Date test=" + bookAppointmentId+"======="+"time empty");

                    saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.book_select_a_time), getActivity()));

                    final Fragment fragmentPackageTime = new FragmentSingleWheelView(this);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            expand(fragmentPackageTime, selectedDcPackageTimeList, null, true, "", "");
                        }
                    }, 1500);
                } else {
                    boolean isTImeMatch = false;
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("EEE, d MMM yyyy");
                    SimpleDateFormat dateParser = new SimpleDateFormat("h:mm a");
                    SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm");

                    for (int i = 0; i < selectedDcPackageTimeList.size(); i++) {
                        try {
                            String givenTime = dateFormater.format(dateParser.parse(selectedDcPackageTimeList.get(i)));
                            Date givenDate = dateFormater.parse(givenTime);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(givenDate);

                            String askedTime = dateFormater.format(dateParser.parse(bookPackageServerTime));
                            Date askedDate = dateFormater.parse(askedTime);
                            Calendar cal2 = Calendar.getInstance();
                            cal2.setTime(askedDate);

                            System.out.println("FragmentChat.startBookPackage cal.get(Calendar.HOUR_OF_DAY)=" + cal.get(Calendar.HOUR_OF_DAY));
                            System.out.println("FragmentChat.startBookPackage cal2.get(Calendar.HOUR_OF_DAY)=" + cal2.get(Calendar.HOUR_OF_DAY));
                            if (cal.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY)) {
                                isTImeMatch = true;
                                break;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }


                    if (isTImeMatch) {
                        try {
                            packageTime = dateFormater.format(dateParser.parse(bookPackageServerTime));
                            System.out.println("FragmentChat.startBookPackage packageTime=" + packageTime + " packageDate=" + packageDate);
                            Date d = dateFormater.parse(packageTime);
                            System.out.println("FragmentChat.startBookPackage d=" + d);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(d);
                            packageHour = cal.get(Calendar.HOUR_OF_DAY);
                            packageMin = cal.get(Calendar.MINUTE);
                            System.out.println("FragmentChat.startBookPackage packageHour=" + packageHour + " packageMin=" + packageMin);

                        } catch (ParseException e) {
                            System.err.println("Cannot parse this time string !");
                        }


                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.YEAR, formatter.parseDateTime(packageDate).getYear());
                        System.out.println("FragmentChat.startBookPackage formatter.parseDateTime(packageDate).getMonthOfYear()=" + formatter.parseDateTime(packageDate).getMonthOfYear());
                        calendar1.set(Calendar.MONTH, formatter.parseDateTime(packageDate).getMonthOfYear() - 1);
                        calendar1.set(Calendar.DATE, formatter.parseDateTime(packageDate).getDayOfMonth());
                        calendar1.set(Calendar.HOUR_OF_DAY, packageHour);
                        calendar1.set(Calendar.MINUTE, packageMin);
                        Date time = calendar1.getTime();

                        SimpleDateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.ENGLISH);
                        String dateAsString = outputformat.format(time);
                        System.out.println("FragmentChat.onTimeSet dateAsString=" + dateAsString);
                        packageAppointmentTime = dateAsString;

                        this.bookAppointmentId = this.bookAppointmentId + 1;

                        packageTime = bookPackageServerTime;

                        startBookPackage(this.bookAppointmentId);
                    } else {
                        saveAndSendIncomingMessage(new IncomingTextMessage("Slots are not available at " + bookPackageServerTime + ". Select some other time.", getActivity()));

                        final Fragment fragmentPackageTime = new FragmentSingleWheelView(this);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                expand(fragmentPackageTime, selectedDcPackageTimeList, null, true, "", "");
                            }
                        }, 1500);
                    }
                }

                break;
            case 4:
                System.out.println("Date test=" + bookAppointmentId+"======="+"case 4 empty");

                enableRelativeLayoutSend(false, true, false, false);
                generateIncomingMessageAnimation();
                serverRequests.getWalletData();
                break;

        }

    }

    public ChatMessage createIncomingTextMessage(String text) {
        System.out.println("FragmentChat.createIncomingTextMessage=======" + text);
        ChatMessage chatMessage = new ChatMessage();
        IncomingTextMessage incomingTextMessage = new IncomingTextMessage(text, getActivity());
        chatMessage.setMessageItem(incomingTextMessage);
        return chatMessage;
    }

    public IncomingTextMessage createIncomingTextObject(String text) {
        System.out.println("FragmentChat.createIncomingTextObject=======" + text);
        IncomingTextMessage incomingTextMessage = new IncomingTextMessage(text, getActivity());
        return incomingTextMessage;
    }

    private void botIntro(boolean isFirstTime) {
        int time = 0;

        if (isFirstTime) {
            saveAndSendOutgoingMessage(new IntroductionMessage());

            String text;
            if (mCustomer != null) {
                text = "Hi " + mCustomer.getFirst_name() + "...! I am flo, your personal health assistant and will assist you from here onwards " + getEmojiByUnicode(0x1F603);
            } else {
                text = "Hi there...! I am flo your personal health assistant and will assist you from here onwards " + getEmojiByUnicode(0x1F603);
            }
            saveAndSendIncomingMessage(new IncomingTextMessage(text, getActivity()));

            if ((mCustomer != null  && mCustomer.getAge()!=null && mCustomer.getGender()!=null && mCustomer.getHealth_score()!=null)) {
                saveAndSendIncomingMessage(new IncomingTextMessage("Here are some of the things I can do...", getActivity()));

                List<String> stringList = new ArrayList<>();
                stringList.add("Digitize medical reports");
                stringList.add("Add family members");
                stringList.add("Book health check");
                stringList.add("Show blood glucose results");
                stringList.add("Consult a doctor");
                stringList.add("Show my father's health status");
                IncomingHorizontalListMessage incomingHorizontalListMessage = new IncomingHorizontalListMessage(getActivity(), stringList, this);
                saveAndSendIncomingMessage(incomingHorizontalListMessage);

                if (mCustomer != null) {
                    serverRequests.getWalletDataFirst();
                }

                time = 2600;
            }


            time = time + 2600;
        }

        if(mCustomer == null){
            prefs.setIsFirstTimeRegister(true);
        }

        if (prefs.getIsFirstTimeRegister() || (mCustomer == null)  || (mCustomer.getAge()==null) || (mCustomer.getGender()==null) || (mCustomer.getHealth_score()==null)) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    prefs.setIsFirstTimeRegister(true);
                    if(prefs.getCustomerDOB().isEmpty()){
                        startAgeQuery();
                    }else{
                        if(prefs.getCustomerGender().isEmpty()){
                           startGenderQuery();
                        }else{
                            isAgeGenderFlowNeed = false;
                            isHRATrue = true;
                            hraId = prefs.getCustomerWizardStatus();
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                            String formattedDate = df.format(c.getTime());
                            System.out.println("FragmentChat.botIntro prefs.getCustomerDOB()=" + prefs.getCustomerDOB());
                            int years = DateUtils.differenceBtwDate(prefs.getCustomerDOB(), formattedDate);
                            System.out.println("FragmentChat.onCardWithImagePositiveClick years=" + years + "hraId= " + hraId);
                            familyMemberAge = years;
                            //initializeViews();
                            startHRALogic(hraId);
                        }
                    }

                    relativeLayoutSend.setVisibility(View.VISIBLE);
                    relativeLayoutSend.startAnimation(slide_up);
                }
            }, 2400);

        } else {

            if (!prefs.getIsRated()) {
                if (getNumberOfDaysSinceInstallation() || prefs.getUserRatingCount() >= 10) {
                    isRatingTrue = true;
                    ratingMessage();
                    time = time + 4000;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            relativeLayoutSend.setVisibility(View.VISIBLE);
                            relativeLayoutSend.startAnimation(slide_up);
                        }
                    }, time);
                } else {
                    botIntro2(time);
                }
            } else {
                botIntro2(time);
            }
        }


    }

    private void startGenderQuery(){
        isAgeGenderFlowNeed = true;

        saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.hra_gender_query), getActivity()));

        final ArrayList<String> genderList = new ArrayList<String>();
        genderList.add("Female");
        genderList.add("Male");

        final Fragment fragmentGender = new FragmentSingleWheelView(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                expand(fragmentGender, genderList, null, true, "", "");
                enableRelativeLayoutSend(true, true, false, true);
            }
        }, 1500);

    }

    private void startAgeQuery()
    {
        isAgeGenderFlowNeed = true;

        saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.hra_age_query), getActivity()));

        final Fragment fragmentDatePickerView = new FragmentDatePickerView(this);
        currentFragment = fragmentDatePickerView;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                expand(fragmentDatePickerView);
                enableRelativeLayoutSend(true, true, false, true);
            }
        }, 1500);
    }

    private void botIntro2(int time) {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            final MainActivity myactivity = (MainActivity) activity;
            if (!prefs.getIsGoogleFitConnected()) {
                CardViewModel cardViewModel = new CardViewModel("Google Fit", getResources().getString(R.string.google_fit_image_body_text), "Connect",
                        "", "", 0, "", "GOOGLE_FIT", "");
                IncomingCardWithTextAndButtonMessage incomingCardWithTextAndButtonMessage = new IncomingCardWithTextAndButtonMessage(cardViewModel, getActivity(), this, this);
                ChatMessage chatMessage2 = new ChatMessage();
                chatMessage2.setMessageItem(incomingCardWithTextAndButtonMessage);
                addMessageToQueue(chatMessage2);


            }
            time = time + 1000;
        }


        isRatingTrue = false;
        prefs.setUserRatingCount(prefs.getUserRatingCount() + 1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                googleFitMessagePosition = getRecyclerCount();
                relativeLayoutSend.setVisibility(View.VISIBLE);
                relativeLayoutSend.startAnimation(slide_up);
            }
        }, time);

        new AddNewMessageTask(chatMessageList, mRecyclerAdapter, recyclerViewChat, chatMessageList.size()).execute();

    }

    private void saveAndSendOutgoingMessage(MessageItem messageItem) {
        final String type = messageItem.getMessageType().toString();
        String source = messageItem.getMessageSource().toString();
        String loginJson = "";

        System.out.println("FragmentChat.saveAndSendOutgoingMessage type=" + type + " source=" + source);

        if (type.equalsIgnoreCase((MessageType.OUTGOING_DC_CARD).toString())) {
            OutgoingDcCardMessage outgoingDcCardMessage = (OutgoingDcCardMessage) messageItem;

            loginJson = "{\"type\":" + "\"" + type + "\"" + "," +
                    "\"labs\":{\"enterprise_name\":" + "\"" + outgoingDcCardMessage.getLabs().getEnterprise_name() + "\"" + "," +
                    "\"distance\":" + "\"" + outgoingDcCardMessage.getLabs().getDistance() + "\"" + "," +
                    "\"provider_id\":" + "\"" + outgoingDcCardMessage.getLabs().getDistance() + "\"" + "," +
                    "\"slots\":" + "\"" + outgoingDcCardMessage.getLabs().getDistance() + "\"" + "," +
                    "\"home_collection_enabled\":" + "\"" + outgoingDcCardMessage.getLabs().getDistance() + "\"" + "}}";
        } else if (type.equalsIgnoreCase((MessageType.OUTGOING_IMAGE).toString())) {
            final OutgoingImageMessage outgoingImageMessage = (OutgoingImageMessage) messageItem;
            final String[] tempImage = new String[1];
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    tempImage[0] = outgoingImageMessage.getBitmap();
                }
            });
            loginJson = "{\"type\":" + "\"" + type + "\"" + "," + "\"url\":" + "\"" + tempImage[0] + "\"" + "}";

        } else if (type.equalsIgnoreCase((MessageType.OUTGOING_MAP).toString())) {
            OutgoingMapMessage outgoingMapMessage = (OutgoingMapMessage) messageItem;
            loginJson = "{\"type\":" + "\"" + type + "\"" + "," + "\"lat\":" + "\"" + outgoingMapMessage.getLat() + "\"" + "," + "\"long\":" + "\"" + outgoingMapMessage.getLong() + "\"" + "}";

        } else if (type.equalsIgnoreCase((MessageType.OUTGOING_TEXT).toString())) {
            OutgoingTextMessage outgoingTextMessage = (OutgoingTextMessage) messageItem;
            outgoingTextMessage.setStatus(Config.MESSAGE_DEIVERED);
            messageItem = outgoingTextMessage;
            loginJson = "{\"type\":" + "\"" + type + "\"" + "," + "\"message\":" + "\"" + outgoingTextMessage.getText() + "\"" + "}";
        } else if (type.equalsIgnoreCase((MessageType.INTRODUCTION_TEXT).toString())) {
            loginJson = "{\"type\":" + "\"" + type + "\"" + "," + "\"message\":" + "\"" + "" + "\"" + "}";
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageItem(messageItem);

        addNewMessage(chatMessage, getRecyclerCount());


        System.out.println("FragmentChat.saveAndSendOutgoingMessage loginJson=" + loginJson);
        dbHandler.insertChatData(new ChatDatabaseModel(type, source, loginJson));
    }

    private void saveAndSendIncomingMessage(MessageItem messageItem) {
        String type = messageItem.getMessageType().toString();
        String source = messageItem.getMessageSource().toString();
        String loginJson = "";

        System.out.println("FragmentChat.saveAndSendIncomingMessage type=" + type + " source=" + source);

        if (type.equalsIgnoreCase((MessageType.INCOMING_TEXT).toString())) {
            IncomingTextMessage incomingTextMessage = (IncomingTextMessage) messageItem;
            loginJson = "{\"type\":" + "\"" + type + "\"" + "," + "\"message\":" + "\"" + incomingTextMessage.getText() + "\"" + "}";
        } else if (type.equalsIgnoreCase((MessageType.INCOMING_HORIZONTAL_LIST).toString())) {
            IncomingHorizontalListMessage incomingHorizontalListMessage = (IncomingHorizontalListMessage) messageItem;
            loginJson = "{\"type\":" + "\"" + type + "\"" + "," + "\"message\":" + "\"" + incomingHorizontalListMessage.getText() + "\"" + "}";
        } else if (type.equalsIgnoreCase((MessageType.INCOMING_CARD_WITH_TEXT).toString())) {
            IncomingCardWithTextMessage incomingCardWithTextMessage = (IncomingCardWithTextMessage) messageItem;
            CardViewModel cardViewModel = incomingCardWithTextMessage.getCardData();
            loginJson = "{\"type\":" + "\"" + type + "\"" + "," +
                    "\"header\":" + "\"" + cardViewModel.getTitle() + "\"" + "," +
                    "\"subheader\":" + "\"" + cardViewModel.getSubTitle() + "\"" + "," +
                    "\"primary_button_name\":" + "\"" + cardViewModel.getButtonText1() + "\"" + "," +
                    "\"secondary_button_name\":" + "\"" + cardViewModel.getButtonText2() + "\"" + "," +
                    "\"imageurl\":" + "\"" + cardViewModel.getUrl() + "\"" + "," +
                    "\"url\":" + "\"" + cardViewModel.getUrl() + "\"" + "," +
                    "\"secondary_button_action\":" + "\"" + cardViewModel.getButtonAction2() + "\"" + "," +
                    "\"primary_button_action\":" + "\"" + cardViewModel.getButtonAction1() + "\"" + "}";

        }else if (type.equalsIgnoreCase((MessageType.INCOMING_MEMBER_PROFILE_CARD).toString())) {
            IncomingProfileCompletionMessage incomingCardWithTextMessage = (IncomingProfileCompletionMessage) messageItem;
            CardViewModel cardViewModel = incomingCardWithTextMessage.getCardData();
            loginJson = "{\"type\":" + "\"" + type + "\"" + "," +
                    "\"header\":" + "\"" + cardViewModel.getTitle() + "\"" + "," +
                    "\"subheader\":" + "\"" + cardViewModel.getSubTitle() + "\"" + "," +
                    "\"primary_button_name\":" + "\"" + cardViewModel.getButtonText1() + "\"" + "," +
                    "\"secondary_button_name\":" + "\"" + cardViewModel.getButtonText2() + "\"" + "," +
                    "\"imageurl\":" + "\"" + cardViewModel.getUrl() + "\"" + "," +
                    "\"url\":" + "\"" + cardViewModel.getUrl() + "\"" + "," +
                    "\"secondary_button_action\":" + "\"" + cardViewModel.getButtonAction2() + "\"" + "," +
                    "\"primary_button_action\":" + "\"" + cardViewModel.getButtonAction1() + "\"" + "}";

        } else if (type.equalsIgnoreCase((MessageType.INCOMING_PACKAGE).toString())) {
            IncomingHorizontalPackageCardListMessage incomingHorizontalPackageCardListMessage = (IncomingHorizontalPackageCardListMessage) messageItem;
            List<PackageItem> tempList = incomingHorizontalPackageCardListMessage.getList();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{\"type\":" + "\"" + type + "\"" + "," + "\"package\":[");
            for (int i = 0; i < tempList.size(); i++) {
                stringBuilder.append("{\"id\":" + "\"" + tempList.get(i).getId() + "\"" + "," +
                        "\"name\":" + "\"" + tempList.get(i).getName() + "\"" + "," +
                        "\"package_info\":" + "\"" + tempList.get(i).getPackage_info() + "\"" + "," +
                        "\"gender\":" + "\"" + tempList.get(i).getGender() + "\"" + "," +
                        "\"package_type\":" + "\"" + tempList.get(i).getPackage_type() + "\"" + "," +
                        "\"mrp\":" + "\"" + tempList.get(i).getMrp() + "\"" + "," +
                        "\"selling_price\":" + "\"" + tempList.get(i).getSelling_price() + "\"" + "," +
                        "\"home_collection_enable\":" + "\"" + tempList.get(i).getHome_collection_enable() + "\"" + "," +
                        "\"empty_stomach\":" + "\"" + tempList.get(i).getEmpty_stomach() + "\"" + "," +
                        "\"minage\":" + "\"" + tempList.get(i).getMinage() + "\"" + "," +
                        "\"maxage\":" + "\"" + tempList.get(i).getMaxage() + "\"" + "," +
                        "\"cut_from_pay\":" + "\"" + tempList.get(i).getCut_from_pay() + "\"" + "," +
                        "\"sponsor_id\":" + "\"" + tempList.get(i).getSponsor_id() + "\"" + "," +
                        "\"enterprise_ids\":" + "\"" + tempList.get(i).getJsonElement() + "\"" + "," +
                        "\"tests_count\":" + "\"" + tempList.get(i).getTests_count() + "\"" + "}");

                if (!(i == tempList.size() - 1)) {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append("]}");
            loginJson = stringBuilder.toString();

        } else if (type.equalsIgnoreCase((MessageType.INCOMING_CART_WITH_LIST).toString())) {
            IncomingCartCardWithListMessage incomingCartCardWithListMessage = (IncomingCartCardWithListMessage) messageItem;
            List<PackageItem> tempList = incomingCartCardWithListMessage.getList();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{\"type\":" + "\"" + type + "\"" + "," + "\"package\":[");
            for (int i = 0; i < tempList.size(); i++) {
                stringBuilder.append("{\"id\":" + "\"" + tempList.get(i).getId() + "\"" + "," +
                        "\"name\":" + "\"" + tempList.get(i).getName() + "\"" + "," +
                        "\"package_info\":" + "\"" + tempList.get(i).getPackage_info() + "\"" + "," +
                        "\"gender\":" + "\"" + tempList.get(i).getGender() + "\"" + "," +
                        "\"package_type\":" + "\"" + tempList.get(i).getPackage_type() + "\"" + "," +
                        "\"mrp\":" + "\"" + tempList.get(i).getMrp() + "\"" + "," +
                        "\"selling_price\":" + "\"" + tempList.get(i).getSelling_price() + "\"" + "," +
                        "\"home_collection_enable\":" + "\"" + tempList.get(i).getHome_collection_enable() + "\"" + "," +
                        "\"empty_stomach\":" + "\"" + tempList.get(i).getEmpty_stomach() + "\"" + "," +
                        "\"minage\":" + "\"" + tempList.get(i).getMinage() + "\"" + "," +
                        "\"maxage\":" + "\"" + tempList.get(i).getMaxage() + "\"" + "," +
                        "\"cut_from_pay\":" + "\"" + tempList.get(i).getCut_from_pay() + "\"" + "," +
                        "\"sponsor_id\":" + "\"" + tempList.get(i).getSponsor_id() + "\"" + "," +
                        "\"enterprise_ids\":" + "\"" + tempList.get(i).getJsonElement() + "\"" + "," +
                        "\"tests_count\":" + "\"" + tempList.get(i).getTests_count() + "\"" + "}");

                if (!(i == tempList.size() - 1)) {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append("]}");
            loginJson = stringBuilder.toString();

        } else if (type.equalsIgnoreCase((MessageType.INCOMING_HORIZONTAL_DOCTOR_CARD_LIST).toString())) {
            IncomingHorizontalDoctorCardListMessage incomingHorizontalDoctorCardListMessage = (IncomingHorizontalDoctorCardListMessage) messageItem;
            List<DoctorModelData> tempList = incomingHorizontalDoctorCardListMessage.getList();

            System.out.println("FragmentChat.saveAndSendIncomingMessage tempList=" + tempList.get(0).getFee().getQuery().getInr_val());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{\"type\":" + "\"" + type + "\"" + "," + "\"doctors\":[");
            for (int i = 0; i < tempList.size(); i++) {
                stringBuilder.append("{\"id\":" + "\"" + tempList.get(i).getId() + "\"" + "," +
                        "\"name\":" + "\"" + tempList.get(i).getName() + "\"" + "," +
                        "\"education\":" + "\"" + tempList.get(i).getEducation() + "\"" + "," +
                        "\"speciality\":" + "\"" + tempList.get(i).getSpeciality() + "\"" + "," +
                        "\"photo\":" + "\"" + tempList.get(i).getPhoto() + "\"" + "," +
                        "\"fee\":{" +
                        "\"consult\":{" + "\"inr_val\":" + "\"" + tempList.get(i).getFee().getConsult().getInr_val() + "\"" + "," +
                        "\"alt_currency_val\":" + "\"" + tempList.get(i).getFee().getConsult().getAlt_currency_val() + "\"" + "," +
                        "\"alt_currency_symbol\":" + "\"" + tempList.get(i).getFee().getConsult().getAlt_currency_symbol() + "\"" + "," +
                        "\"alt_currency_label\":" + "\"" + tempList.get(i).getFee().getConsult().getAlt_currency_label() + "\"" +
                        "}" + "," +
                        "\"query\":{" + "\"inr_val\":" + "\"" + tempList.get(i).getFee().getQuery().getInr_val() + "\"" + "," +
                        "\"alt_currency_val\":" + "\"" + tempList.get(i).getFee().getQuery().getAlt_currency_val() + "\"" + "," +
                        "\"alt_currency_symbol\":" + "\"" + tempList.get(i).getFee().getQuery().getAlt_currency_symbol() + "\"" + "," +
                        "\"alt_currency_label\":" + "\"" + tempList.get(i).getFee().getQuery().getAlt_currency_label() + "\"" +
                        "}" +
                        "}" +
                        "}"
                );
                if (!(i == tempList.size() - 1)) {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append("]}");
            loginJson = stringBuilder.toString();

        } else if (type.equalsIgnoreCase((MessageType.INCOMING_CARD_WITH_IMAGE).toString())) {
            IncomingCardWithImageMessage incomingCardWithImageMessage = (IncomingCardWithImageMessage) messageItem;
            CardViewModel cardViewModel = incomingCardWithImageMessage.getCardData();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{\"type\":" + "\"" + type + "\"" + "," +
                    "\"doctor_profile\":{" +
                    "\"doctor_id\":" + "\"" + "" + "\"" + "," +
                    "\"name\":" + "\"" + cardViewModel.getTitle() + "\"" + "," +
                    "\"education\":" + "\"" + cardViewModel.getSubTitle() + "\"" + "," +
                    "\"specialities\":" + "\"" + cardViewModel.getButtonText1() + "\"" + "," +
                    "\"photo\":" + "\"" + cardViewModel.getUrl() + "\"" + "," +
                    "\"language\":" + "\"" + cardViewModel.getButtonText2() + "\"" + "," +
                    "\"consult_fee\":{" +
                    "\"inr_val\":" + "\"" + cardViewModel.getButtonAction1() + "\"" + "}," +
                    "\"query_fee\":{" +
                    "\"inr_val\":" + "\"" + cardViewModel.getButtonAction2() + "\"" + "}" +
                    "}" +
                    "}"
            );
            loginJson = stringBuilder.toString();

        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageItem(messageItem);

        addMessageToQueue(chatMessage);

        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

        System.out.println("FragmentChat.saveAndSendOutgoingMessage loginJson=" + loginJson);
        dbHandler.insertChatData(new ChatDatabaseModel(type, source, loginJson));
    }

    public void showEditText() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                relativeLayoutSend.setVisibility(View.VISIBLE);
                relativeLayoutSend.startAnimation(slide_up);
            }
        });
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showKeyboard(final EditText editText) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 800);
    }

    public int getRecyclerCount() {
        return mRecyclerAdapter.getItemCount();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onHorizontalListItemClick(String text) {
        System.out.println("FragmentChat.onHorizontalListItemClick text=" + text + " isFirstSelectiveMessagesPosition=" + isFirstSelectiveMessagesPosition);

        isBottomSheetFragmentClicked = false;

        if (isFirstSelectiveMessages) {
            isFirstSelectiveMessages = false;
            try{
                removeMessage(isFirstSelectiveMessagesPosition - 2);
                removeMessage(isFirstSelectiveMessagesPosition - 1);
                callServerWhenItemClicked(text);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else if (isAddFamilyTrue) {
            //recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
            createAddFamilyMemberMessages(text);
        } else if (isHRATrue) {
            createHraOutgoingMessages(text);
        } else if (isaskQuaryDoctorTrue) {
            System.out.println("FragmentChat.onHorizontalListItemClick isaskQuaryDoctorTrue=" + isaskQuaryDoctorTrue);
            createAskQueryDoctorMessages(text);
        } else if (isBookAppointmentTrue) {
            familyMembers.clear();
            familyMemberNamesList.clear();
            System.out.println("AudioVideo============" + "BookApointment");
            createBookPackageOutgoingMessages(text);
        } else if (isShowResults || isFamilyMemberDoesnotExists) {
            isShowResults = false;
            isFamilyMemberDoesnotExists = false;
            removeMessage(getRecyclerCount() - 1);
            //createOutgoingMessage(text);
            saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));
            if (text.equalsIgnoreCase("Yes")) {
                addFamilyLogicId = 2;
                isAddFamilyTrue = true;
                startAddFamilyLogic(addFamilyLogicId);
                enableRelativeLayoutSend(true, true, false, false);
            } else {
                enableRelativeLayoutSend(true, true, true, false);
            }
        } else if (isRatingTrue) {
            System.out.println("AudioVideo============" + "isRating");
            isRatingButtonClicked = true;
            isRatingTrue = false;
            removeMessage(getRecyclerCount() - 1);
            removeMessage(getRecyclerCount() - 1);
            removeMessage(getRecyclerCount() - 1);
            if (text.equalsIgnoreCase("Not So Good")) {
                prefs.setIsRated(true);
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"feedback@ekincare.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                //email.putExtra(Intent.EXTRA_TEXT, message);
                emailIntent.setType("message/rfc822");
                //startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));

                startActivity(emailIntent);
            } else if (text.equalsIgnoreCase("Later")) {
                prefs.setIsRated(false);
                prefs.setUserRatingCount(0);
                prefs.setUserRatingDayCount(0);
            } else if (text.equalsIgnoreCase("Great")) {
                prefs.setIsRated(true);
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="
                            + getActivity().getPackageName())));
                }
            }
        } else {
            System.out.println("FragmentChat.onHorizontalListItemClick else case");
            editTextMessage.setText("");

            //removeMessage(mRecyclerAdapter.getItemCount()-1);

            callServerWhenItemClicked(text);
        }

    }

    private void callServerWhenItemClicked(String text) {
        // Build messageData object
        outgoingChatMessage = new ChatMessage();
        OutgoingTextMessage outgoingTextMessage = new OutgoingTextMessage(text, getActivity());

        sentPosition = mRecyclerAdapter.getItemCount();

        playOutgoingSound();

        String loginJson = "{\"type\":" + "\"" + (MessageType.OUTGOING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + text + "\"" + "}";
        dbHandler.insertChatData(new ChatDatabaseModel((MessageType.OUTGOING_TEXT).toString(), (MessageSource.SELF).toString(), loginJson));

        if (NetworkCaller.isInternetOncheck(getActivity())) {
            outgoingTextMessage.setStatus(Config.MESSAGE_SENT);
            outgoingChatMessage.setMessageItem(outgoingTextMessage);
            addNewMessage(outgoingChatMessage, sentPosition);
            System.out.println("FragmentChat.onHorizontalListItemClick internet success getRecyclerCount()=" + getRecyclerCount());
            sendDataLogic(text);
        } else {
            outgoingTextMessage.setStatus(Config.MESSAGE_FAILED);
            outgoingChatMessage.setMessageItem(outgoingTextMessage);
            addNewMessage(outgoingChatMessage, sentPosition);
            System.out.println("FragmentChat.onHorizontalListItemClick internet failed getRecyclerCount()=" + getRecyclerCount());
        }

    }

    private void createAddFamilyMemberMessages(String text) {
        System.out.println("FragmentChat.createAddFamilyMemberMessages addFamilyLogicId=" + addFamilyLogicId);
        switch (addFamilyLogicId) {
            case 0:
                if (isBottomSheetFragmentClicked) {
                    familyMemberRelation = text;
                    enableRelativeLayoutSend(true, true, false, true);
                    //editTextMessage.setText(text);
                    doneString = text;
                }
                break;
            case 1:
                if (isBottomSheetFragmentClicked) {
                    enableRelativeLayoutSend(true, true, false, true);
                    //editTextMessage.setText(text);
                    doneString = text;
                }
                break;
            case 5:
                if (text.equalsIgnoreCase("Skip")) {
                    createOutgoingMessage(text);

                    String loginJson = "{\"type\":" + "\"" + (MessageType.OUTGOING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + text + "\"" + "}";
                    dbHandler.insertChatData(new ChatDatabaseModel((MessageType.OUTGOING_TEXT).toString(), (MessageSource.SELF).toString(), loginJson));

                    addFamilyLogicId = addFamilyLogicId + 1;
                    startAddFamilyLogic(addFamilyLogicId);
                    recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                } else if (text.equalsIgnoreCase("Yeah sure")) {
                    //createOutgoingMessage(text);

                    //String loginJson = "{\"type\":" + "\"" + (MessageType.OUTGOING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + text + "\"" + "}";
                    //dbHandler.insertChatData(new ChatDatabaseModel((MessageType.OUTGOING_TEXT).toString(), (MessageSource.SELF).toString(), loginJson));

                    editTextMessage.setFilters(new InputFilter[]{emailFilter, new InputFilter.LengthFilter(40)});
                    editTextMessage.setHint("Enter the email");
                    editTextMessage.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                    enableRelativeLayoutSend(true, true, false, false);

                } else {
                    recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                }
                break;
            case 6:
                if (text.equalsIgnoreCase("Yes")) {
                    isAddFamilyTrue = false;
                    addFamilyLogicId = -1;

                    createOutgoingMessage("Yeah sure");
                    recyclerViewChat.scrollToPosition(getRecyclerCount());

                    isHRATrue = true;
                    hraId = hraId + 1;
                    startHRALogic(hraId);
                } else if (text.equalsIgnoreCase("No")) {
                    isAddFamilyTrue = false;
                    addFamilyLogicId = -1;

                    createOutgoingMessage("I will do later");
                    familyMemberGender = "";
                    familyMemberName = "";
                    familyMemberAge = 0;
                    familyMemberRelation = "";
                    familyMemberDOF = "";
                    familyMemberEmail = "";
                    familyMemberMobile = "";

                    enableRelativeLayoutSend(true, true, true, false);
                } else {
                    recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                }
                break;
            default:
                recyclerViewChat.scrollToPosition(getRecyclerCount());
                break;
        }
    }

    private void ProceedAddFamilyMemberMessages(String text) {
        System.out.println("FragmentChat.ProceedAddFamilyMemberMessages addFamilyLogicId=" + addFamilyLogicId);
        switch (addFamilyLogicId) {
            case 0:
                createOutgoingMessageNoRemoval(text);
                if (text.equalsIgnoreCase("Spouse")) {
                    if (mCustomer.getGender().equalsIgnoreCase("male")) {
                        familyMemberGender = "Female";
                    } else {
                        familyMemberGender = "Male";
                    }
                    addFamilyLogicId = addFamilyLogicId + 2;
                    startAddFamilyLogic(addFamilyLogicId);
                } else if (text.equalsIgnoreCase("Mother")) {
                    familyMemberGender = "Female";
                    addFamilyLogicId = addFamilyLogicId + 2;
                    startAddFamilyLogic(addFamilyLogicId);
                } else if (text.equalsIgnoreCase("Father")) {
                    familyMemberGender = "Male";
                    addFamilyLogicId = addFamilyLogicId + 2;
                    startAddFamilyLogic(addFamilyLogicId);
                } else if (text.equalsIgnoreCase("Wife")) {
                    familyMemberGender = "Female";
                    addFamilyLogicId = addFamilyLogicId + 2;
                    startAddFamilyLogic(addFamilyLogicId);
                } else if (text.equalsIgnoreCase("Husband")) {
                    familyMemberGender = "Male";
                    addFamilyLogicId = addFamilyLogicId + 2;
                    startAddFamilyLogic(addFamilyLogicId);
                } else if (text.equalsIgnoreCase("Brother")) {
                    familyMemberGender = "Male";
                    addFamilyLogicId = addFamilyLogicId + 2;
                    startAddFamilyLogic(addFamilyLogicId);
                } else if (text.equalsIgnoreCase("Sister")) {
                    familyMemberGender = "Female";
                    addFamilyLogicId = addFamilyLogicId + 2;
                    startAddFamilyLogic(addFamilyLogicId);
                } else if (text.equalsIgnoreCase("Son")) {
                    familyMemberGender = "Male";
                    addFamilyLogicId = addFamilyLogicId + 2;
                    startAddFamilyLogic(addFamilyLogicId);
                } else if (text.equalsIgnoreCase("Daughter")) {
                    familyMemberGender = "Female";
                    addFamilyLogicId = addFamilyLogicId + 2;
                    startAddFamilyLogic(addFamilyLogicId);
                } else if (text.equalsIgnoreCase("Child")) {
                    addFamilyLogicId = addFamilyLogicId + 1;
                    startAddFamilyLogic(addFamilyLogicId);
                } else if (text.equalsIgnoreCase("Other")) {
                    addFamilyLogicId = addFamilyLogicId + 1;
                    startAddFamilyLogic(addFamilyLogicId);
                } else if (text.equalsIgnoreCase("Sibling")) {
                    addFamilyLogicId = addFamilyLogicId + 1;
                    startAddFamilyLogic(addFamilyLogicId);
                }


                break;
            case 1:
                createOutgoingMessageNoRemoval(text);
                if (text.equalsIgnoreCase("male")) {
                    familyMemberGender = "Male";
                    addFamilyLogicId = addFamilyLogicId + 1;
                } else if (text.equalsIgnoreCase("female")) {
                    familyMemberGender = "Female";
                    addFamilyLogicId = addFamilyLogicId + 1;
                }
                startAddFamilyLogic(addFamilyLogicId);

                break;
            case 2:
                createOutgoingMessageNoRemoval(text);
                familyMemberName = text;
                addFamilyLogicId = addFamilyLogicId + 1;
                startAddFamilyLogic(addFamilyLogicId);

                break;
            case 3:
                break;
            case 4:
                createOutgoingMessageNoRemoval(text);
                familyMemberMobile = text;
                addFamilyLogicId = addFamilyLogicId + 1;
                startAddFamilyLogic(addFamilyLogicId);

                break;
            case 5:
                createOutgoingMessageNoRemoval(text);
                String emailPATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z]+(\\.[A-Za-z]+)*(\\.[A-Za-z]{2,3})$";
                if (Pattern.matches(emailPATTERN, text)) {
                    familyMemberEmail = text;
                    addFamilyLogicId = addFamilyLogicId + 1;
                    startAddFamilyLogic(addFamilyLogicId);
                } else {
                    removeMessage(getRecyclerCount() - 1);
                    Toast.makeText(getActivity(), "Invalid email", Toast.LENGTH_LONG).show();
                    enableRelativeLayoutSend(true, true, false, false);
                    return;
                }
                break;
            case 6:
                break;
        }
    }

    private void createHraOutgoingMessages(String text) {
        System.out.println("FragmentChat.createHraOutgoingMessages text=" + text + " hraId=" + hraId);
        switch (hraId) {
            case 0:
                if (isBottomSheetFragmentClicked) {
                    hraHeightFeet = text.substring(0, text.indexOf(" "));
                    hraHeightInch = text.substring(text.indexOf(" ") + 1);
                    System.out.println("FragmentChat.createHraOutgoingMessages====" + hraHeightFeet + " " + hraHeightInch);
                    doneString = hraHeightFeet + " Feet " + hraHeightInch + " Inch";
                }

                break;
            case 1:
                if (isBottomSheetFragmentClicked) {
                    hraWeight = text;
                    doneString = text + " Kg";
                }

                break;
            case 2:
                if (isBottomSheetFragmentClicked) {
                    hraWaist = text;
                    doneString = text + " Inch";
                }

                break;
            case 3:
                if (isBottomSheetFragmentClicked) {
                    hraBloodGroupId = getBloodId(text);
                    hraBloodGroup = text;
                    doneString = text;
                }

                break;
            case 4:
                if (isBottomSheetFragmentClicked) {
                    hraExercise = text;
                    doneString = text;
                }

                break;
            case 5:
                if (isBottomSheetFragmentClicked) {
                    hraSmoke = text;
                    doneString = text;
                }

                break;
            case 6:
                if (isBottomSheetFragmentClicked) {
                    hraAlcohol = text;
                    doneString = text;
                }

                break;
            case 7:
                if (isBottomSheetFragmentClicked) {
                    hraFatherCondition = text;
                    enableRelativeLayoutSend(true, true, false, true);
                    //editTextMessage.setText(text);
                    doneString = text;
                }

                break;
            case 8:
                if (isBottomSheetFragmentClicked) {
                    hraMotherCondition = text;
                    enableRelativeLayoutSend(true, true, false, true);
                    //editTextMessage.setText(text);
                    doneString = text;
                }

                break;
            case 9:
                if (text.equalsIgnoreCase("I don't know")) {
                    removeMessage(getRecyclerCount()-1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));
                    //createOutgoingMessage(text);
                    hraBloodSugar = "";
                    hraId = hraId + 1;
                    startHRALogic(hraId);
                } else if (text.equalsIgnoreCase("I know")) {
                    removeMessage(getRecyclerCount() - 1);
                    ArrayList<String> arrayListGlucose = new ArrayList<>();
                    for (int i = 50; i < 300; i++) {
                        arrayListGlucose.add("" + i);
                    }
                    Fragment fragmentHeight = new FragmentSingleWheelView(this);
                    expand(fragmentHeight, arrayListGlucose, null, true, "mg/dl", "");
                } else {
                    if (isBottomSheetFragmentClicked) {
                        hraBloodSugar = text;
                        doneString = text + " mg/dl";
                        //editTextMessage.setText(hraBloodSugar + "mg/dl");
                        enableRelativeLayoutSend(true, true, false, true);
                    }
                }
                break;
            case 10:
                if (text.equalsIgnoreCase("I don't know")) {
                    //createOutgoingMessage(text);
                    removeMessage(getRecyclerCount()-1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                    hraSystolic = "";
                    hraId = hraId + 1;
                    startHRALogic(hraId);
                } else if (text.equalsIgnoreCase("I know")) {
                    removeMessage(getRecyclerCount() - 1);
                    Fragment fragmentHeight = new FragmentDoubleWheelView(this);
                    expand(fragmentHeight, Config.systolicList(), Config.diastolicList(), false, "mm", "Hg");
                } else {
                    if (isBottomSheetFragmentClicked) {
                        hraSystolic = text.substring(0, text.indexOf(" "));
                        hraDiastolic = text.substring(text.indexOf(" ") + 1);

                        enableRelativeLayoutSend(true, true, false, true);
                        //editTextMessage.setText(hraSystolic + "/" + hraDiastolic + " mmHg");
                        doneString = hraSystolic + "/" + hraDiastolic + " mmHg";
                    }

                }

                break;
            case 11:
                if (isBottomSheetFragmentClicked) {
                    enableRelativeLayoutSend(true, true, false, true);
                    //editTextMessage.setText(text);
                    doneString = text;
                }
                break;
        }
    }

    private void proceedHraOutgoingMessages(String text) {
        System.out.println("FragmentChat.proceedHraOutgoingMessages text=" + text);
        switch (hraId) {
            case 0:
                createOutgoingMessageNoRemoval(text);
                hraId = hraId + 1;
                startHRALogic(hraId);
                break;
            case 1:
                createOutgoingMessageNoRemoval(text);
                if (familyMemberAge >= 18) {
                    hraId = hraId + 1;
                    startHRALogic(hraId);
                } else {
                    isHRATrue = false;

                    JSONObject object = new JSONObject();
                    JSONObject objectWizard = new JSONObject();
                    try {

                        object.put("weight", hraWeight);
                        object.put("feet", hraHeightFeet);
                        object.put("inches", hraHeightInch);
                        object.put("waist", hraWaist);

                        objectWizard.put("wizard", object);
                        objectWizard.put("calculate_health_score", "false");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    serverRequests.addHraRequest(objectWizard, strFamilyMemberKey);

                }

                break;
            case 2:
                /*createOutgoingMessageNoRemoval(text);
                hraId = hraId + 1;
                startHRALogic(hraId);*/

                collapse();
                createOutgoingMessageNoRemoval(text);

                hraId = hraId + 2;
                startHRALogic(hraId);

                JSONObject object = new JSONObject();
                JSONObject objectWizard = new JSONObject();
                try {

                    object.put("weight", hraWeight);
                    object.put("feet", hraHeightFeet);
                    object.put("inches", hraHeightInch);
                    object.put("waist", hraWaist);

                    objectWizard.put("wizard", object);
                    objectWizard.put("calculate_health_score", "false");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                serverRequests.addHraRequest(objectWizard, strFamilyMemberKey);


                break;
           /* case 3:
                collapse();
                createOutgoingMessageNoRemoval(text);
                if (familyMemberAge >= 18) {
                    hraId = hraId + 1;
                    startHRALogic(hraId);
                } else {
                    isHRATrue = false;
                }

                JSONObject object = new JSONObject();
                JSONObject objectWizard = new JSONObject();
                try {

                    object.put("weight", hraWeight);
                    object.put("feet", hraHeightFeet);
                    object.put("inches", hraHeightInch);
                    object.put("waist", hraWaist);
                    object.put("blood_group_id", hraBloodGroupId);

                    objectWizard.put("wizard", object);
                    objectWizard.put("calculate_health_score", "false");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                serverRequests.addHraRequest(objectWizard, strFamilyMemberKey);


                break;*/
            case 4:
                createOutgoingMessageNoRemoval(text);
                hraId = hraId + 1;
                startHRALogic(hraId);
                break;
            case 5:
                createOutgoingMessageNoRemoval(text);
                hraId = hraId + 1;
                startHRALogic(hraId);
                break;
            case 6:
                createOutgoingMessageNoRemoval(text);
                hraId = hraId + 1;

                JSONObject object1 = new JSONObject();
                JSONObject objectWizard1 = new JSONObject();
                try {
                    object1.put("smoke", hraSmoke);
                    object1.put("alcohol", hraAlcohol);
                    object1.put("exercise", hraExercise);
                    objectWizard1.put("wizard", object1);
                    objectWizard1.put("calculate_health_score", "false");

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                serverRequests.addHraRequest(objectWizard1, strFamilyMemberKey);

                startHRALogic(hraId);
                break;
            case 7:
                createOutgoingMessageNoRemoval(text);
                hraId = hraId + 1;
                startHRALogic(hraId);
                break;
            case 8:
                createOutgoingMessageNoRemoval(text);

                JSONObject object3 = new JSONObject();
                JSONObject objectWizard3 = new JSONObject();
                try {
                    object3.put("father_medical_history", hraFatherCondition);
                    object3.put("mother_medical_history", hraMotherCondition);
                    objectWizard3.put("wizard", object3);
                    objectWizard3.put("calculate_health_score", "false");

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                serverRequests.addHraRequest(objectWizard3, strFamilyMemberKey);

                hraId = hraId + 1;
                startHRALogic(hraId);
                break;
            case 9:
                createOutgoingMessageNoRemoval(text);
                hraId = hraId + 1;
                startHRALogic(hraId);
                break;
            case 10:
                createOutgoingMessageNoRemoval(text);
                hraId = hraId + 1;
                startHRALogic(hraId);
                break;
            case 11:
                createOutgoingMessageNoRemoval(text);

                generateIncomingMessageAnimation();

                boolean isDiabetes = false;
                boolean isHuperTension = false;
                boolean isBloodPressure = false;

                if (text.contains("Blood Pressure")) {
                    isBloodPressure = true;
                }

                if (text.contains("Diabetics")) {
                    isDiabetes = true;
                }

                if (text.contains("Hypertension")) {
                    isHuperTension = true;
                }

                JSONObject objectWizardHra1 = new JSONObject();
                try {

                    JSONObject objectHra1 = new JSONObject();
                    objectHra1.put("result", hraBloodSugar);
                    objectWizardHra1.put("lab_result", objectHra1);

                    JSONObject object2 = new JSONObject();
                    object2.put("systolic", hraSystolic);
                    object2.put("diastolic", hraDiastolic);
                    objectWizardHra1.put("blood_pressure", object2);

                    objectWizardHra1.put("father_medical_history", hraFatherCondition);

                    JSONObject objectTreatment = new JSONObject();
                    objectTreatment.put("diabetic_treatment", isDiabetes);
                    objectTreatment.put("hypertensive_treatment", isHuperTension);
                    objectTreatment.put("cvd_treatment", isBloodPressure);
                    objectWizardHra1.put("wizard", objectTreatment);

                    objectWizardHra1.put("calculate_health_score", true);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                serverRequests.addHraRequest(objectWizardHra1, strFamilyMemberKey);

                isHRATrue = false;
                break;
        }

        doneString = "";
    }

    private String getBloodId(String text) {
        String id;
        if (text.equalsIgnoreCase("A+")) {
            id = "1";
        } else if (text.equalsIgnoreCase("A-")) {
            id = "2";
        } else if (text.equalsIgnoreCase("B+")) {
            id = "3";
        } else if (text.equalsIgnoreCase("B-")) {
            id = "4";
        } else if (text.equalsIgnoreCase("AB+")) {
            id = "5";
        } else if (text.equalsIgnoreCase("AB-")) {
            id = "6";
        } else if (text.equalsIgnoreCase("O+")) {
            id = "7";
        } else if (text.equalsIgnoreCase("O-")) {
            id = "8";
        } else {
            id = "0";
        }

        return id;
    }

    private void createBookPackageOutgoingMessages(String text) {
        System.out.println("FragmentChat.onChatServeResponse========"+bookAppointmentId);
        System.out.println("Date test=" + bookAppointmentId+"========== createBookPackageOutgoingMessages");


        switch (bookAppointmentId) {
            case 99:
                if (isBottomSheetFragmentClicked) {
                    //removeMessage(getRecyclerCount() - 1);


                    bookPackageRelation = text;
                    enableRelativeLayoutSend(true, true, false, true);
                    //editTextMessage.setText(text);
                    doneString = text;
                }
                break;
            case 0:
                System.out.println("Date test=" + bookAppointmentId+"==========");

                System.out.println("FragmentChat.onChatServeResponse========"+"0 Yes");

                if (text.equalsIgnoreCase("Proceed")) {
                    removeMessage(getRecyclerCount() - 1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                    for (int i = 0; i < requestPackages.size(); i++) {
                        sponsorIds=requestPackages.get(i).getSponsor_id();
                        if (packageIds.isEmpty()) {
                            packageIds = requestPackages.get(i).getId();

                            prefs.setPackageType(requestPackages.get(i).getPackage_type());
                            System.out.println("TWOooo========"+requestPackages.get(i).getSponsor_id());

                        } else {
                            packageIds = packageIds + "," + requestPackages.get(i).getId();
                            System.out.println("TWOooo========"+requestPackages.get(i).getSponsor_id());

                        }
                    }

                    saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.book_here_are_your_requested_packages), getActivity()));

                    saveAndSendIncomingMessage(new IncomingHorizontalPackageCardListMessage(requestPackages, getActivity()));

                    saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.book_select_location), getActivity()));

                    recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                            try {
                                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                            } catch (GooglePlayServicesRepairableException e) {
                                e.printStackTrace();
                            } catch (GooglePlayServicesNotAvailableException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 3000);
                    enableRelativeLayoutSend(false, true, false, false);

                } else if (text.equalsIgnoreCase("No")) {
                    removeMessage(getRecyclerCount() - 1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                    enableRelativeLayoutSend(true, true, true, false);
                    bookAppointmentId = -1;
                    isBookAppointmentTrue = false;

                    saveAndSendIncomingMessage(new IncomingTextMessage("OK, I have cancelled the booking", getActivity()));
                } else if (text.equalsIgnoreCase("Abort")) {
                    removeMessage(getRecyclerCount() - 1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                    enableRelativeLayoutSend(true, true, true, false);
                    bookAppointmentId = -1;
                    isBookAppointmentTrue = false;
                    saveAndSendIncomingMessage(new IncomingTextMessage("OK, I have cancelled the booking", getActivity()));
                } else if (text.equalsIgnoreCase("Later")) {
                    removeMessage(getRecyclerCount() - 1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                    bookAppointmentId = -1;
                    isBookAppointmentTrue = false;

                    enableRelativeLayoutSend(true, true, true, false);
                } else if (text.equalsIgnoreCase("Book another package")) {
                    removeMessage(getRecyclerCount() - 1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                    bookAppointmentId = 0;

                    recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

                    requestPackages.clear();
                    packageIds = "";

                    autoCompleteTextView.setAdapter(new MultitextCompleteViewSearchAdapter(getActivity())); // 'this' is Activity instance
                    autoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                    autoCompleteTextView.setThreshold(1);

                    System.out.println("FragmentChat.startBookPackage case 1 bookAppointmentId=" + bookAppointmentId);
                    enableRelativeLayoutSend(true, false, false, false);
                } else if (text.equalsIgnoreCase("Search for different location")) {
                    removeMessage(getRecyclerCount() - 1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                    System.out.println("Search for different location");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                            try {
                                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                            } catch (GooglePlayServicesRepairableException e) {
                                e.printStackTrace();
                            } catch (GooglePlayServicesNotAvailableException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 1000);

                    enableRelativeLayoutSend(false, true, false, false);
                } else if (text.equalsIgnoreCase("select location")) {
                    removeMessage(getRecyclerCount() - 1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                    enableRelativeLayoutSend(false, true, false, false);
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                } else if (text.equalsIgnoreCase("yes")) {
                    removeMessage(getRecyclerCount() - 1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                    enableRelativeLayoutSend(false, true, false, false);
                    bookAppointmentId = 2;
                    startBookPackage(bookAppointmentId);
                } else if (text.equalsIgnoreCase("Add more")) {
                    removeMessage(getRecyclerCount() - 1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                    enableRelativeLayoutSend(false, true, false, false);
                    bookAppointmentId = 0;
                    startBookPackage(bookAppointmentId);
                } else if (text.equalsIgnoreCase("Add")) {
                    removeMessage(getRecyclerCount() - 1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                    enableRelativeLayoutSend(false, true, false, false);
                    bookAppointmentId = 0;
                    startBookPackage(bookAppointmentId);
                } else {
                    recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                }
                break;
            case 1:
                System.out.println("FragmentChat.createBookPackageOutgoingMessages case 1 text= " + text);

                if (text.equalsIgnoreCase("No")) {
                    enableRelativeLayoutSend(false, true, false, false);
                    removeMessage(getRecyclerCount() - 1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                    bookAppointmentId = bookAppointmentId + 1;
                    homeCollectionEnable="true";
                    startBookPackage(bookAppointmentId);

                    enableRelativeLayoutSend(false, true, false, false);
                } else if (text.equalsIgnoreCase("Yeah, sure")) {
                    homeCollectionEnable="true";
                    enableRelativeLayoutSend(false, true, false, false);
                    removeMessage(getRecyclerCount() - 1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                    PackageItem packageItem = new PackageItem();
                    packageItem.setName("Delivery Charges");
                    packageItem.setSelling_price("" + dc_collection_price);
                    packageItem.setMrp("" + dc_collection_price);
                    packageItem.setTests_count("No");
                    packageItem.setPackage_info("Delivery charges for home collection");
                    requestPackages.add(packageItem);

                    bookAppointmentId = bookAppointmentId + 1;
                    startBookPackage(bookAppointmentId);

                    enableRelativeLayoutSend(false, true, false, false);
                } else {
                    recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                }

                break;

            case 2:
                System.out.println("Date test=" + bookAppointmentId+"========== case 2");

                if (isBottomSheetFragmentClicked) {
                    enableRelativeLayoutSend(true, true, false, true);
                    //editTextMessage.setText(text);
                    doneString = text;
                }

                break;
            case 3:
                System.out.println("Date test=" + bookAppointmentId+"========== case 3");

                if (isBottomSheetFragmentClicked) {
                    enableRelativeLayoutSend(true, true, false, true);
                    //editTextMessage.setText(text);
                    doneString = text;
                }

                break;
            case 4:

                if (text.equalsIgnoreCase("Yes")) {
                    removeMessage(getRecyclerCount() - 1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                    final StringBuilder stringBuilder = new StringBuilder();

                    if (!isPaymentFailed) {
                        for (int i = 0; i < requestPackages.size(); i++) {
                            packageTotalPrice = packageTotalPrice + Integer.parseInt(requestPackages.get(i).getSelling_price());
                            if (i == requestPackages.size() - 1) {
                                stringBuilder.append(requestPackages.get(i).getName() + ", ");
                            } else {
                                stringBuilder.append(requestPackages.get(i).getName());
                            }

                        }

                        if (walletMoney > 0) {
                            if (walletMoney > packageTotalPrice) {
                                packagePrice = 0;
                            } else {
                                packagePrice = packageTotalPrice - walletMoney;
                            }
                        } else {
                            packagePrice = packageTotalPrice;
                        }
                    }

                    if (packagePrice > 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startPayment(packagePrice * 100, stringBuilder.toString());
                            }
                        }, 1300);
                    } else {
                        paymentSuccessServerCall("");
                    }
                } else if (text.equalsIgnoreCase("Cancel")) {
                    removeMessage(getRecyclerCount() - 1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                    saveAndSendIncomingMessage(new IncomingTextMessage("OK, I have cancelled the booking", getActivity()));

                    bookAppointmentId = -1;
                    isBookAppointmentTrue = false;
                    enableRelativeLayoutSend(true, true, true, false);
                } else {
                    recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                }
                break;
        }
    }

    private void proceedBookPackageOutgoingMessages(String text) {
        System.out.println("Date test=" + bookAppointmentId+"======="+"case 5 empty out");
        System.out.println("FragmentChat.proceedBookPackageOutgoingMessages id="+bookAppointmentId);
        switch (bookAppointmentId) {
            case 99:
                saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));
                for (int i = 0; i < familyMemberNamesList.size(); i++) {
                    if (familyMemberNamesList.get(i).equalsIgnoreCase(text)) {
                        System.out.println(i);
                        strFamilyMemberKey=familyMembers.get(i).getIdentification_token();
                        bookPackageCustomerId=familyMembers.get(i).getId();
                        break;
                    }
                }
                System.out.println("token========="+strFamilyMemberKey);
                bookAppointmentId = 0;
                //add the realtion code

                if (!requestPackages.isEmpty()) {
                    System.out.println("FragmentChat.onChatServeResponse========"+"requestPackage");

                    //removeMessage(getRecyclerCount() - 1);
                    ChatMessage chatMessage = createIncomingTextMessage(getResources().getString(R.string.book_Would_add_more));
                    ChatMessage chatMessage1 = new ChatMessage();
                    List<String> stringList = new ArrayList<>();
                    stringList.add("Add more");
                    stringList.add("Proceed");
                    IncomingHorizontalListMessage incomingHorizontalListMessage = new IncomingHorizontalListMessage(getActivity(),
                            stringList, this);
                    chatMessage1.setMessageItem(incomingHorizontalListMessage);

                    enableRelativeLayoutSend(true, true, false, true);

                    addMessageToQueue(chatMessage);
                    addMessageToQueue(chatMessage1);
                } else {
                    System.out.println("FragmentChat.onChatServeResponse========"+"else request");
                    startBookPackage(bookAppointmentId);
                }
                break;
            case 0:

                enableRelativeLayoutSend(false, true, false, false);
                if (text.length() > 0 && text.charAt(text.length() - 2) == ',') {
                    text = text.substring(0, text.length() - 2);
                }

                saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                final List<String> items = Arrays.asList(text.split("\\s*,\\s*"));
                System.out.println("FragmentChat.proceedBookPackageOutgoingMessages text=" + text + " items=" + items.toString());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        generateIncomingMessageAnimation();

                        serverRequests.getPackageNameData(items);
                    }
                }, 1500);


                break;
            case 2:
                DateFormat srcDf = new SimpleDateFormat("EEE, d MMM yyyy");
                Date date = null;
                try {
                    date = (Date) srcDf.parse(text);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DateFormat destDf = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDate = destDf.format(date);

                if (selectedDclabs.getSlots().isJsonObject()) {
                    Set<Entry<String, JsonElement>> ens = ((JsonObject) selectedDclabs.getSlots()).entrySet();
                    if (ens != null) {
                        selectedDcPackageTimeList.clear();
                        for (Entry<String, JsonElement> en : ens) {
                            if (selectedDate.equals(en.getKey())) {
                                JsonArray jarr = en.getValue().getAsJsonArray();
                                for (int i = 0; i < jarr.size(); i++) {
                                    selectedDcPackageTimeList.add(jarr.get(i).getAsString());
                                }
                            }
                        }
                    }
                }

                collapse();
                packageDate = text;
                saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                bookAppointmentId = bookAppointmentId + 1;
                startBookPackage(bookAppointmentId);
                break;
            case 3:
                collapse();

                DateTimeFormatter formatter = DateTimeFormat.forPattern("EEE, d MMM yyyy");
                SimpleDateFormat dateParser = new SimpleDateFormat("h:mm a");
                SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm");

                try {
                    packageTime = dateFormater.format(dateParser.parse(text));
                    System.out.println("FragmentChat.proceedBookPackageOutgoingMessages packageTime=" + packageTime + " packageDate=" + packageDate);
                    Date d = dateFormater.parse(packageTime);
                    System.out.println("FragmentChat.proceedBookPackageOutgoingMessages d=" + d);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    packageHour = cal.get(Calendar.HOUR_OF_DAY);
                    packageMin = cal.get(Calendar.MINUTE);
                   /* System.out.println("FragmentChat.proceedBookPackageOutgoingMessages packageHour=" + packageHour + " 1.packageMin=" + packageMin);
                    this.bookAppointmentId = this.bookAppointmentId + 1;
                    System.out.println("Date test=" + bookAppointmentId+"======="+"case 5 empty lcdh out");

                    startBookPackage(this.bookAppointmentId);*/
                } catch (ParseException e) {
                    System.out.println("Date test="+"Exception");
                    System.err.println("Cannot parse this time string !");
                }


                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, formatter.parseDateTime(packageDate).getYear());
                calendar1.set(Calendar.MONTH, formatter.parseDateTime(packageDate).getMonthOfYear() - 1);
                System.out.println("FragmentChat.proceedBookPackageOutgoingMessages formatter.parseDateTime(packageDate).getMonthOfYear()=" + formatter.parseDateTime(packageDate).getMonthOfYear());

                calendar1.set(Calendar.DATE, formatter.parseDateTime(packageDate).getDayOfMonth());
                calendar1.set(Calendar.HOUR_OF_DAY, packageHour);
                calendar1.set(Calendar.MINUTE, packageMin);
                Date time = calendar1.getTime();

                SimpleDateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.ENGLISH);
                String dateAsString = outputformat.format(time);
                System.out.println("FragmentChat.onTimeSet dateAsString=" + dateAsString);
                packageAppointmentTime = dateAsString;

                saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                bookAppointmentId = bookAppointmentId + 1;

                packageTime = text;
                System.out.println("Date test=" + bookAppointmentId+"======="+"case 5  yyyyy empty out");


                startBookPackage(bookAppointmentId);
                break;
            case 4:
                System.out.println("Date test=" + bookAppointmentId+"======="+"case 6 empty");

                editTextMessage.setFilters(new InputFilter[]{textFilter, new InputFilter.LengthFilter(1000)});
                editTextMessage.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextMessage.setHint("Enter message...");

                enableRelativeLayoutSend(false, true, false, false);
                saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));
                pacakageHouseNumber=text;
                saveAndSendIncomingMessage(new IncomingTextMessage("Proceed to payment", getActivity()));
                ChatMessage chatMessagePayment = new ChatMessage();
                ArrayList<String> arrayListPayment = new ArrayList<>();
                arrayListPayment.add("Cancel");
                arrayListPayment.add("Yes");

                IncomingHorizontalListMessage incomingHorizontalPayment = new IncomingHorizontalListMessage(getActivity(), arrayListPayment, this);
                chatMessagePayment.setMessageItem(incomingHorizontalPayment);
                addMessageToQueue(chatMessagePayment);
                break;

            default:
                break;
        }
        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

    }

    private void createAskQueryDoctorMessages(String text) {
        System.out.println("FragmentChat.createAskQueryDoctorMessages==========" + askQueryId + text);

        switch (askQueryId) {
            case 1:
            case 2:
            case 5:
            case 4:
            case 6:
                if (isBottomSheetFragmentClicked) {
                    enableRelativeLayoutSend(true, true, false, true);
                    doneString = text;
                }

                break;
            case 7:
                if (text.equalsIgnoreCase("Yes")) {
                    removeMessage(getRecyclerCount() - 1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                    recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                    askQueryId = 8;
                    startAskQueryToDoctor(askQueryId);
                } else if (text.equalsIgnoreCase("Cancel")) {
                    removeMessage(getRecyclerCount() - 1);
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));


                    askQueryId = -1;
                    askQueryMode = "";
                    askQueryDate = "";
                    askQueryPackagePrice = 0;
                    walletMoney = 0;
                    askQueryTotalPackagePrice = 0;
                    askQueryQuestion = "";
                    askQuerySpeciality = "";
                    askQueryTime = "";
                    askRelation = "";
                    isaskQuaryDoctorTrue = false;

                    saveAndSendIncomingMessage(new IncomingTextMessage("OK, I have cancelled the booking", getActivity()));

                    recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                    enableRelativeLayoutSend(true, true, true, false);

                } else {
                    recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                }
                break;
            default:
                break;
        }

    }

    private void proceedAskQueryDoctorMessages(String text) {
        System.out.println("FragmentChat.proceedAskQueryDoctorMessages==========" + askQueryId);

        switch (askQueryId) {
            case 1:
                collapse();
                saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                if (text.equalsIgnoreCase("Ask a Query")) {
                    askQueryId = askQueryId + 2;
                    startAskQueryToDoctor(askQueryId);
                    askQueryMode = "text";
                } else if (text.equalsIgnoreCase("Audio Call") || text.equalsIgnoreCase("Video Call")) {
                    generateIncomingMessageAnimation();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            serverRequests.getPackageDoctorList(askQuerySpeciality);
                        }
                    }, 1500);
                }
                if (text.equalsIgnoreCase("Audio Call")) {
                    askQueryMode = "phone";
                } else if (text.equalsIgnoreCase("Video Call")) {
                    askQueryMode = "video";
                }
                enableRelativeLayoutSend(false, true, false, false);
                break;
            case 3:
                askQueryQuestion = text;
                saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                askQueryId = askQueryId + 1;
                startAskQueryToDoctor(askQueryId);
                enableRelativeLayoutSend(false, true, false, false);
                break;
            case 4:
                collapse();

                if (askQueryMode.equalsIgnoreCase("text")) {
                    askQueryId = askQueryId + 3;
                } else {
                    askQueryId = askQueryId + 1;
                }

                if (text.equalsIgnoreCase("No")) {
                    askShareProfile = "NO";
                } else {
                    askShareProfile = "YES";
                }

                saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                startAskQueryToDoctor(askQueryId);
                enableRelativeLayoutSend(false, true, false, false);
                break;
            case 5:
                collapse();
                askQueryDate = text;
                saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                askQueryId = askQueryId + 1;
                startAskQueryToDoctor(askQueryId);
                enableRelativeLayoutSend(false, true, false, false);
                break;
            case 6:
                collapse();
                askQueryTime = text;
                saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));

                askQueryId = askQueryId + 1;
                startAskQueryToDoctor(askQueryId);
                enableRelativeLayoutSend(false, true, false, false);
                break;
        }

    }

    boolean isQueueRunning = false;

    public void addMessageToQueue(ChatMessage chatMessages) {
        tempChatMessageQueue.add(tempChatMessageQueue.size(), chatMessages);
        if (!isQueueRunning) {
            showDelayed();
        }
    }


    public void showDelayed() {
        System.out.println("FragmentChat.showDelayed=========" + tempChatMessageQueue.size());
        isQueueRunning = true;
        ChatMessage chatMessage;
        if (tempChatMessageQueue.size() > 0) {
            chatMessage = tempChatMessageQueue.get(0);
            tempChatMessageQueue.remove(0);
            addNewMessageWithDelay(chatMessage);
        } else {
            isQueueRunning = false;
        }
    }


    private void addNewMessageWithDelay(final ChatMessage message) {
        System.out.println("FragmentChat.addNewMessageWithDelay");
        generateIncomingMessageAnimation();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                removeMessage(getRecyclerCount() - 1);
                playIncomingSound();
                addNewMessage(message, getRecyclerCount());
                showDelayed();
            }
        }, 1300);
    }

    private void createOutgoingMessage(String text) {
        removeMessage(mRecyclerAdapter.getItemCount() - 1);
        playOutgoingSound();
        OutgoingTextMessage exerciseOutgoingTextMessage = new OutgoingTextMessage(text, getActivity());
        exerciseOutgoingTextMessage.setStatus(Config.MESSAGE_DEIVERED);
        ChatMessage exerciseGroupChatMessage = new ChatMessage();
        exerciseGroupChatMessage.setMessageItem(exerciseOutgoingTextMessage);
        addNewMessage(exerciseGroupChatMessage, getRecyclerCount());
        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
    }

    private void createOutgoingMessageNoRemoval(String text) {
        OutgoingTextMessage exerciseOutgoingTextMessage = new OutgoingTextMessage(text, getActivity());
        exerciseOutgoingTextMessage.setStatus(Config.MESSAGE_DEIVERED);
        ChatMessage exerciseGroupChatMessage = new ChatMessage();
        exerciseGroupChatMessage.setMessageItem(exerciseOutgoingTextMessage);
        playOutgoingSound();
        addNewMessage(exerciseGroupChatMessage, getRecyclerCount());
        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

    }

    @Override
    public void onHorizontalCardListItemClick(final String text) {
        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
        System.out.println("FragmentChat.onHorizontalCardListItemClick text=" + text);
        if (text.contains("UploadAction")) {
            sendDataLogic("Digitize medical reports");
        } else if (text.contains("AddFamilyMembersAction")) {
            sendDataLogic("Add family member");
        } else if (isAddFamilyTrue || isHRATrue || isBookAppointmentTrue) {
            recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
        } else if (text.contains("UpdateWeightAction")) {
            System.out.println("FragmentChat.onHorizontalCardListItemClick text=" + "INSIDE");

            try {
                Activity activity = getActivity();
                if (activity instanceof MainActivity) {
                    final MainActivity myactivity = (MainActivity) activity;
                    myactivity.setPage(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (text.contains("https://www.ekincare.com/second_opinion")) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
            getActivity().startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (text.contains("Change Location Package")) {
            System.out.println("FragmentChat.onDCPackagesResponse =" + "NO YESS ");
            System.out.println("FragmentChat.onDCPackagesResponse dcLabs=" + "NO ");
            removeMessage(getRecyclerCount() - 1);
            removeMessage(getRecyclerCount() - 1);
            bookAppointmentId = 0;
            ChatMessage chatMessage = createIncomingTextMessage(getResources().getString(R.string.book_select_diagnostic_center));
            addMessageToQueue(chatMessage);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
            }, 4400);

        } else if (text.equalsIgnoreCase("Share Profile Query")
                || text.equalsIgnoreCase("Dont share Profile Query")
                || text.equalsIgnoreCase("Proceed Payment")
                || text.equalsIgnoreCase("Cancle Payment")) {
            createAskQueryDoctorMessages(text);
        } else if (text.equalsIgnoreCase("Gallery")) {
            System.out.println("FragmentChat.onHorizontalCardListItemClick==========" + "Yes Gallery");
            prefs.setPREF_RUNTIME_PERMISSION("File");
            saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));
            //createOutgoingMessage(text);
            enableRelativeLayoutSend(true, true, true, false);
            if (NetworkCaller.isInternetOncheck(getActivity())) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!checkPermission()) {
                        System.out.println("FragmentChat.onHorizontalCardListItemClick==========" + " check permision Yes Gallery");

                        requestPermission();
                    } else {
                        galleryFunctionality();
                    }
                } else {
                    galleryFunctionality();
                }
            } else {
                saveAndSendIncomingMessage(new IncomingTextMessage("No internet. I can't upload the document", getActivity()));
                Toast.makeText(getActivity(), getResources().getString(R.string.networkloss), Toast.LENGTH_LONG).show();
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
            }
        } else if (text.equalsIgnoreCase("Trigger Upload")) {
            saveAndSendOutgoingMessage(new OutgoingTextMessage("Camera", getActivity()));
            //createOutgoingMessage("Camera");
            prefs.setPREF_RUNTIME_PERMISSION("Camera");
            enableRelativeLayoutSend(true, true, true, false);
            if (NetworkCaller.isInternetOncheck(getActivity())) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!checkPermission()) {
                        requestPermission();
                    } else {
                        cameraFunctionality();
                    }
                } else {
                    cameraFunctionality();
                }
            } else {
                saveAndSendIncomingMessage(new IncomingTextMessage("No internet. I can't upload the document", getActivity()));
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                Toast.makeText(getActivity(), getResources().getString(R.string.networkloss), Toast.LENGTH_LONG).show();
            }
        } else if (text.equalsIgnoreCase("Update eKincare app")) {
            removeMessage(getRecyclerCount() - 1);
            enableRelativeLayoutSend(true, true, true, false);

            addMessageToQueue(createIncomingTextMessage(text));

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName()));
            startActivity(intent);
        } else if (text.equalsIgnoreCase("Cancel eKincare app update")) {
            removeMessage(getRecyclerCount() - 1);
            enableRelativeLayoutSend(true, true, true, false);
        } else if (text.equalsIgnoreCase("View More")) {
            DialogTermsAndCondition newFragment = new DialogTermsAndCondition();
            newFragment.show(getActivity().getSupportFragmentManager(), "");

        } else if (text.equalsIgnoreCase("GOOGLE_FIT")) {
            try {
                removeMessage(googleFitMessagePosition);
            } catch (Exception e) {
                removeMessage(googleFitMessagePosition - 1);
            }
            googleFitMessagePosition = 0;
            Intent intent = new Intent(getActivity(), MainActivity2.class);
            startActivity(intent);
        } else {
            editTextMessage.setText("");
            System.out.println("FragmentChat.onHorizontalCardListItemClick defoult text=" + text);

            callServerWhenItemClicked(text);

        }
    }


    private void galleryFunctionality() {
        isGalleryTrue = true;
        saveAndSendIncomingMessage(new IncomingTextMessage("Select a file from gallery", getActivity()));
        System.out.println("FragmentChat.galleryFunctionality");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("*/*");
                startActivityForResult(intent, PICKFILE_RESULT_CODE);
            }
        }, 1300);
    }

    private void cameraFunctionality() {
        //removeMessage(getRecyclerCount()-1);
        saveAndSendIncomingMessage(new IncomingTextMessage("Take a picture of your medical reports using camera", getActivity()));

        prefs.setIsMainDoc(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getActivity(), OpenNoteScannerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        }, 1300);

    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        System.out.println("MainActivity.onRequestPermissionsResult=====" + "Camera OUT" + requestCode);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                System.out.println("MainActivity.onRequestPermissionsResult=====" + "Camera OUT" + grantResults.length);

                if (grantResults.length > 0) {

                    boolean readAccept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccept = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean camerAccept = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    System.out.println("MainActivity.onRequestPermissionsResult=====" + "Camera OUT");

                    if (readAccept && writeAccept && camerAccept) {
                        System.out.println("FragmentChat.onHorizontalCardListItemClick==========" + "Yes  inside Gallery" + "======" +
                                prefs.getPREF_RUNTIME_PERMISSION());

                        if (prefs.getPREF_RUNTIME_PERMISSION().equals("Camera")) {
                            System.out.println("FragmentChat.onHorizontalCardListItemClick==========" + " NO Yes Gallery");

                            removeMessage(getRecyclerCount() - 1);
                            tempChatMessageQueue.add(tempChatMessageQueue.size(), createIncomingTextMessage("Take a picture of your medical reports using camera"));
                            if (!isQueueRunning) {
                                showDelayed();
                            }
                            prefs.setIsMainDoc(true);
                            Intent intent = new Intent(getActivity(), OpenNoteScannerActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                        } else if (prefs.getPREF_RUNTIME_PERMISSION().equals("File")) {
                            System.out.println("FragmentChat.onHorizontalCardListItemClick==========" + "Yes Gallery");
                            isGalleryTrue = true;

                            saveAndSendIncomingMessage(new IncomingTextMessage("Select a file from gallery", getActivity()));
                            System.out.println("FragmentChat.galleryFunctionality");
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            intent.setType("*/*");
                            startActivityForResult(intent, PICKFILE_RESULT_CODE);
                        }

                    } else {
                        Toast.makeText(getActivity(), "Permission Denied, You cannot access  camera.", Toast.LENGTH_SHORT).show();

                    }
                }


                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("FragmentChat.onActivityResult====" + requestCode);
        System.out.println("FragmentChat.onActivityResult resultCode == " + (resultCode == getActivity().RESULT_OK));
        Bitmap mBitmap = null;
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case SPEECH_RECOGNITION_CODE:
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    //sendDataLogic(text);
                    System.out.println("FragmentChat.onActivityResult=========" + text);
                    //txtOutput.setText(text);
                    enableRelativeLayoutSend(true, true, true, false);
                    editTextMessage.setText(text);
                    break;
                case PLACE_PICKER_REQUEST:
                    Place place = null ;
                    try{
                        place = PlacePicker.getPlace(getActivity(),data);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    System.out.println("FragmentChat.onActivityResult place="+place);

                    if(place!=null){
                        try
                        {
                            Geocoder geocoder = new Geocoder(getActivity());

                            if(place.getAddress()!=null){
                                packageWholeAddress = "";
                                packageWholeAddress=place.getAddress().toString();
                                List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude,place.getLatLng().longitude, 1);
                                packageAddressLine1 = addresses.get(0).getAddressLine(0);
                                packageAddressLine2 = addresses.get(0).getAddressLine(1);
                                packageCounty=addresses.get(0).getCountryName();
                                packagePostalCode=addresses.get(0).getPostalCode();
                                packageState=addresses.get(0).getAdminArea();
                                packageCity=addresses.get(0).getSubAdminArea();
                                packageLatitude=String.valueOf(addresses.get(0).getLatitude());
                                pacakageLangtude=String.valueOf(addresses.get(0).getLongitude());

                                saveAndSendOutgoingMessage(new OutgoingTextMessage(getResources().getString(R.string.book_i_am_at_the_location), getActivity()));

                                saveAndSendOutgoingMessage(new OutgoingMapMessage(getActivity(), place.getLatLng()));

                                System.out.println("FragmentChat.onActivityResult requestPackages" +
                                        requestPackages.size() + " requestPackages=" + requestPackages.size() + " packageIds=" + packageIds);
                                final JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("package_ids", packageIds);
                                    jsonObject.put("latitude", place.getLatLng().latitude);
                                    jsonObject.put("longitude", place.getLatLng().longitude);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        generateIncomingMessageAnimation();
                                        serverRequests.getDCProviders(jsonObject);

                                    }
                                }, 1500);
                            }else{
                                System.out.println("FragmentChat.onActivityResult null address");

                                saveAndSendOutgoingMessage(new OutgoingTextMessage("Failed to fetch location" + " " + getEmojiByUnicode(0x1F632), getActivity()));

                                ChatMessage chatMessage1 = new ChatMessage();
                                List<String> stringList = new ArrayList<>();
                                stringList.add("Select location");
                                stringList.add("Abort");
                                IncomingHorizontalListMessage incomingHorizontalListMessage = new IncomingHorizontalListMessage(getActivity(),
                                        stringList, this);
                                chatMessage1.setMessageItem(incomingHorizontalListMessage);

                                saveAndSendIncomingMessage(new IncomingTextMessage("Select a location on map for selecting " +
                                        "diagnostic center?", getActivity()));
                                addMessageToQueue(chatMessage1);


                            }

                        } catch (IOException e)
                        {
                            //bottomSheetAddress.setText("Not Available.");
                            e.printStackTrace();
                        }
                    }else{
                        saveAndSendIncomingMessage(new IncomingTextMessage("Failed to fing location try again",getActivity()));
                    }


                    break;
                case PICKFILE_RESULT_CODE:
                    System.out.println("FragmentChat.onActivityResult condition PICKFILE_RESULT_CODE");
                    try {
                        ClipData clipData = data.getClipData();
                        if (clipData != null) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                ClipData.Item item = clipData.getItemAt(i);
                                Uri uri = item.getUri();
                                String mStringUri = uri.toString();
                                if (mStringUri.contains("content:")) {
                                    if (!FileUtility.isGoogleDriveDocument(uri)) {
                                        String path = FileUtility.getPath(getActivity(), uri);
                                        System.out.println("path==========" + path);
                                        String extension = null;
                                        Bitmap bitmap = null;
                                        if (path != null) {
                                            if (path != null) {
                                                extension = path.substring(path.lastIndexOf("."));
                                                if (FileUtility.isImageType(extension)) {
                                                    try {
                                                        bitmap = getBitmapFileUpload(uri);

                                                        //removeMessage(getRecyclerCount()-1);
                                                        ChatMessage chatMessage = new ChatMessage();
                                                        OutgoingImageMessage outgoingDocumentImageMessage = new OutgoingImageMessage("", bitmap);
                                                        outgoingDocumentImageMessage.setStatus(Config.MESSAGE_DEIVERED);
                                                        saveAndSendOutgoingMessage(outgoingDocumentImageMessage);

                                                        String fileName = path.substring(path.lastIndexOf("/"));
                                                        File file = savebitmap(bitmap, fileName);
                                                        String url = FileUtil.decodeFile(file.getAbsolutePath(), getResources()
                                                                        .getInteger(R.integer.compressedHeightandWidth),
                                                                getResources().getInteger(
                                                                        R.integer.compressedHeightandWidth),
                                                                fileName);
                                                        try {

                                                            System.out.println("path==========" + url);
                                                            entity = getUploadEntity(url, 0, "");
                                                            serverRequests.uploadMultipleReport(entity);

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    } catch (IOException e) {
                                                        // block
                                                        e.printStackTrace();
                                                    }
                                                }
                                                if (fileTypes.contains(extension)) {
                                                    entity = getUploadEntity(path, 0, "");
                                                    serverRequests.uploadMultipleReport(entity);
                                                }
                                            }

                                        }
                                    } else if (mStringUri.contains("file:")) {

                                        entity = getUploadEntity(mStringUri, 0, "");
                                        serverRequests.uploadMultipleReport(entity);
                                    }
                                }

                            }
                        } else {

                            Bitmap bitmap = null;
                            String extension = "";
                            if (data != null && data.getDataString() != null) {
                                String dataString = data.getDataString();
                                String path = "";
                                if (dataString.contains("content:")) {
                                    if (!FileUtility.isGoogleDriveDocument(data.getData())) {
                                        path = FileUtility.getPath(getActivity(), data.getData());
                                        if (path != null) {
                                            extension = path.substring(path.lastIndexOf("."));
                                        }
                                        if (FileUtility.isImageType(extension)) {
                                            try {
                                                bitmap = getBitmapFileUpload(data.getData());

                                                //removeMessage(getRecyclerCount()-1);
                                                ChatMessage chatMessage = new ChatMessage();
                                                OutgoingImageMessage outgoingDocumentImageMessage = new OutgoingImageMessage("", bitmap);
                                                outgoingDocumentImageMessage.setStatus(Config.MESSAGE_DEIVERED);
                                                saveAndSendOutgoingMessage(outgoingDocumentImageMessage);

                                                String fileName = path.substring(path.lastIndexOf("/"));
                                                File file = savebitmap(bitmap, fileName);
                                                String url = FileUtil.decodeFile(file.getAbsolutePath(),
                                                        getResources().getInteger(R.integer.compressedHeightandWidth),
                                                        getResources().getInteger(R.integer.compressedHeightandWidth),
                                                        fileName);
                                                try {
                                                    entity = getUploadEntity(path, 0, url);
                                                    serverRequests.uploadMultipleReport(entity);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            if (fileTypes.contains(extension)) {
                                                entity = getUploadEntity(path, 0, "");
                                                serverRequests.uploadMultipleReport(entity);
                                            } else {
                                                // throw unsupported format
                                                customerManager.showAlert(getResources().getString(R.string.otherformatsnotsupport),
                                                        getActivity());
                                            }
                                        }
                                    } else {
                                        customerManager.showAlert(getResources().getString(R.string.notsupport), getActivity());
                                    }
                                } else if (!FileUtility.isImageType(extension)) {

                                    if (path.contains("file:")) {

                                        String filename = path.substring(path.lastIndexOf("/") + 1);
                                        try {
                                            entity = getUploadEntity(path, 0, filename);
                                            serverRequests.uploadMultipleReport(entity);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;


                default:
                    String returnValue = data.getStringExtra("camera_closed");
                    System.out.println("FragmentChat.onActivityResult returnValue=" + returnValue);
                    break;
            }
        } else {
            Toast.makeText(getActivity(), "You didn't completed the action", Toast.LENGTH_LONG).show();

            if (bookAppointmentId == 0 && isBookAppointmentTrue) {
                saveAndSendOutgoingMessage(new OutgoingTextMessage("Failed to fetch location" + " " + getEmojiByUnicode(0x1F632), getActivity()));

                ChatMessage chatMessage1 = new ChatMessage();
                List<String> stringList = new ArrayList<>();
                stringList.add("Select location");
                stringList.add("Abort");
                IncomingHorizontalListMessage incomingHorizontalListMessage = new IncomingHorizontalListMessage(getActivity(),
                        stringList, this);
                chatMessage1.setMessageItem(incomingHorizontalListMessage);

                saveAndSendIncomingMessage(new IncomingTextMessage("Select a location on map for selecting " +
                        "diagnostic center?", getActivity()));
                addMessageToQueue(chatMessage1);

            }

            if (isGalleryTrue) {
                //removeMessage(getRecyclerCount()-1);
                saveAndSendOutgoingMessage(new OutgoingTextMessage("Failed to fetch photo" + " " + getEmojiByUnicode(0x1F635), getActivity()));
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
            }

            /*removeMessage(getRecyclerCount()-1);

            tempChatMessageQueue.add(tempChatMessageQueue.size(),createIncomingTextMessage(""));

            if(!isQueueRunning){
                showDelayed();
            }*/
        }

    }

    private Bitmap getBitmapFileUpload(Uri selectedimg) throws IOException {
        System.out.println("FragmentChat.getBitmapFileUpload");
        //Uri imgUri = Uri.parse("file://"+selectedimg);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        AssetFileDescriptor fileDescriptor = null;
        fileDescriptor = getActivity().getContentResolver().openAssetFileDescriptor(selectedimg, "r");
        Bitmap original = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
        return original;
    }

    private File savebitmap(Bitmap bitmap, String name) {
        System.out.println("FragmentChat.savebitmap");
        OutputStream outStream = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String destinationFilename = Environment.getExternalStorageDirectory().getPath() + "/eKincare/Images"
                + File.separatorChar + name;
        File file = new File(destinationFilename);
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;

    }

    protected HttpEntity getUploadEntity(String fileUrl, int profileId, String fileName) throws Exception {
        System.out.println("ActivityFamilyMember.getUploadEntity fileUrl=" + fileUrl);
        File file = new File(fileUrl);
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        entity.addPart("files[]", new FileBody(file));
        entity.addPart("name", new StringBody(file.getName()));
        entity.addPart("id", new StringBody("" + profileId));

        return entity;
    }

    @Override
    public void onChatServeResponse(JSONObject response, String textMessage) {
        try {
            System.out.println("FragmentChat.onChatServeResponse==========" + response.toString());
            if (response != null) {
                playIncomingSound();
                OutgoingTextMessage outgoingTextMessage = new OutgoingTextMessage(textMessage, getActivity());
                outgoingTextMessage.setStatus(Config.MESSAGE_DEIVERED);
                outgoingChatMessage.setMessageItem(outgoingTextMessage);
                RecyclerView.ItemAnimator animator = recyclerViewChat.getItemAnimator();
                if (animator instanceof SimpleItemAnimator) {
                    ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
                }
                mRecyclerAdapter.update(sentPosition, outgoingChatMessage);

                jsonMessageResponse = new Gson().fromJson(response.toString(), JsonMessageResponse.class);
                System.out.println("FragmentChat.onChatServeResponse=====" + jsonMessageResponse.getIntentName());


                try {
                    System.out.println("FragmentChat.onResponse jsonMessageResponse intentname=" + jsonMessageResponse.getIntentName());
                    System.out.println("FragmentChat.onResponse jsonMessageResponse sessionId=" + jsonMessageResponse.getSessionId());
                    System.out.println("FragmentChat.onResponse jsonMessageResponse payload=" + jsonMessageResponse.getPayload());
                    System.out.println("FragmentChat.onResponse jsonMessageResponse id=" + jsonMessageResponse.getId());
                    System.out.println("FragmentChat.onResponse jsonMessageResponse context=" + jsonMessageResponse.getContexts());
                    System.out.println("FragmentChat.onResponse jsonMessageResponse timestamp=" + jsonMessageResponse.getTimestamp());
                    System.out.println("FragmentChat.onResponse jsonMessageResponse parameters=" + jsonMessageResponse.getParameters());
                } catch (Exception e) {
                    System.out.println("FragmentChat.onChatServeResponse innner json exception");
                    enableRelativeLayoutSend(true, true, true, false);
                }

                if (jsonMessageResponse.getIntentName().equalsIgnoreCase("ask_query")) {
                    generateServerMessage(jsonMessageResponse);

                    if (jsonMessageResponse.getParameters() != null) {
                        JsonElement jsonElement = jsonMessageResponse.getParameters();
                        System.out.println("FragmentChat.run add family memebr jsonElement.getParameters=" + jsonElement);

                        JsonObject jsonObject = jsonElement.getAsJsonObject();

                        askQuerySpeciality = jsonObject.get("speciality").getAsString();
                        askQueryMode = jsonObject.get("mode").getAsString();
                        askQueryDate = jsonObject.get("date").getAsString();
                        askRelation = jsonObject.get("relation").getAsString();
                        askQueryTime = jsonObject.get("time").getAsString();

                    }

                    askQueryId = 0;
                    isaskQuaryDoctorTrue = true;
                    startAskQueryToDoctor(askQueryId);
                } else if (jsonMessageResponse.getIntentName().equalsIgnoreCase("display_wellness_score")
                        || jsonMessageResponse.getIntentName().equalsIgnoreCase("share_profile")
                        || jsonMessageResponse.getIntentName().equalsIgnoreCase("show_results")
                        || jsonMessageResponse.getIntentName().equalsIgnoreCase("show_history")) {
                    generateServerMessage(jsonMessageResponse);
                    String relation = "";
                    String isFamilyMemberPresent = "";

                    if (jsonMessageResponse.getParameters() != null) {
                        JsonElement jsonElement = jsonMessageResponse.getParameters();
                        System.out.println("FragmentChat.run add family memebr jsonElement.getParameters=" + jsonElement);
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        relation = jsonObject.get("relation").getAsString();
                        isFamilyMemberPresent = jsonObject.get("is_family_member_added").getAsString();
                    }

                    if (!relation.isEmpty()) {
                        if (isFamilyMemberPresent.equalsIgnoreCase("false")) {
                            isFamilyMemberDoesnotExists = true;
                            familyMemberRelation = relation;

                            if (familyMemberRelation.equalsIgnoreCase("Father") || familyMemberRelation.equalsIgnoreCase("Son")
                                    || familyMemberRelation.equalsIgnoreCase("Husband") || familyMemberRelation.equalsIgnoreCase("Brother")) {
                                familyMemberGender = "Male";
                            } else if (familyMemberRelation.equalsIgnoreCase("Mother") || familyMemberRelation.equalsIgnoreCase("Daughter")
                                    || familyMemberRelation.equalsIgnoreCase("Wife") || familyMemberRelation.equalsIgnoreCase("Sister")) {
                                familyMemberGender = "Female";
                            } else if (familyMemberRelation.equalsIgnoreCase("Spouse")) {
                                if (mCustomer.getGender().equalsIgnoreCase("Male")) {
                                    familyMemberGender = "Female";
                                    familyMemberRelation = "Wife";
                                } else {
                                    familyMemberGender = "Male";
                                    familyMemberRelation = "Husband";
                                }
                            }
                            saveAndSendIncomingMessage(new IncomingTextMessage("Would you like to add " + familyMemberRelation.toLowerCase() + " ?", getActivity()));

                            ChatMessage chatMessage = new ChatMessage();
                            ArrayList<String> arrayList = new ArrayList<>();
                            arrayList.add("Yes");
                            arrayList.add("Later");
                            IncomingHorizontalListMessage incomingHorizontalListMessage = new
                                    IncomingHorizontalListMessage(getActivity(), arrayList, this);
                            chatMessage.setMessageItem(incomingHorizontalListMessage);
                            addMessageToQueue(chatMessage);

                            enableRelativeLayoutSend(false, true, false, false);

                        }
                    }
                } else if (jsonMessageResponse.getIntentName().equalsIgnoreCase("add_family_member")) {
                    generateServerMessage(jsonMessageResponse);
                    String relation = "";

                    if (jsonMessageResponse.getParameters() != null) {
                        JsonElement jsonElement = jsonMessageResponse.getParameters();
                        System.out.println("FragmentChat.run add family memebr jsonElement.getParameters=" + jsonElement);
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        relation = jsonObject.get("relation").getAsString();
                    }

                    if (relation.isEmpty()) {
                        System.out.println("FragmentChat.onChatServeResponse addFamily empty relation=" + relation);
                        addFamilyLogicId = 0;
                        isAddFamilyTrue = true;
                        startAddFamilyLogic(addFamilyLogicId);
                    } else {
                        System.out.println("FragmentChat.onChatServeResponse addFamily relation=" + relation);

                        familyMemberRelation = relation;

                        if (familyMemberRelation.equalsIgnoreCase("Father") || familyMemberRelation.equalsIgnoreCase("Son")
                                || familyMemberRelation.equalsIgnoreCase("Husband") || familyMemberRelation.equalsIgnoreCase("Brother")) {
                            familyMemberGender = "Male";
                        } else if (familyMemberRelation.equalsIgnoreCase("Mother") || familyMemberRelation.equalsIgnoreCase("Daughter")
                                || familyMemberRelation.equalsIgnoreCase("Wife") || familyMemberRelation.equalsIgnoreCase("Sister")) {
                            familyMemberGender = "Female";
                        }

                        addFamilyLogicId = 2;
                        isAddFamilyTrue = true;
                        enableRelativeLayoutSend(true, true, false, false);
                    }

                } else if (jsonMessageResponse.getIntentName().equalsIgnoreCase("capabilities")) {
                    removeMessage(getRecyclerCount() - 1);
                    recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                    startCapability();
                } else if (jsonMessageResponse.getIntentName().equalsIgnoreCase("take_hra")) {
                    generateServerMessage(jsonMessageResponse);
                    enableRelativeLayoutSend(false, true, false, false);
                    String relation = "";
                    String isFamilyMemberPresent = "";
                    String wizardStatus = "";
                    int age = 0;

                    if (jsonMessageResponse.getParameters() != null) {
                        JsonElement jsonElement = jsonMessageResponse.getParameters();
                        System.out.println("FragmentChat.run retake hra jsonElement.getParameters=" + jsonElement);
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        relation = jsonObject.get("relation").getAsString();
                        isFamilyMemberPresent = jsonObject.get("is_family_member_added").getAsString();
                        strFamilyMemberKey = jsonObject.get("identification_token").getAsString();
                        wizardStatus = jsonObject.get("wizard_status").getAsString();
                        age = jsonObject.get("age").getAsInt();
                    }

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
                        mCustomer = mProfileManager.getLoggedinCustomer();
                        if (!familyMembers.contains(mCustomer))
                            familyMembers.add(0, mCustomer);
                    }

                    for (int i = 0; i < familyMembers.size(); i++) {
                        if (familyMembers.get(i).getIdentification_token().equalsIgnoreCase(strFamilyMemberKey)) {
                            familyMemberName = familyMembers.get(i).getFirst_name() + familyMembers.get(i).getLast_name();
                            break;
                        }
                    }

                    if (relation.isEmpty()) {
                        if (isFamilyMemberPresent.equalsIgnoreCase("true")) {
                            hraId = 0;
                            isHRATrue = true;
                            strFamilyMemberKey = "";
                            isRetakeHraEnabled = true;
                            familyMemberName = "";
                            familyMemberAge = Integer.parseInt(mCustomer.getAge());
                            enableRelativeLayoutSend(false, true, false, false);
                            startHRALogic(hraId);
                        } else {
                            removeMessage(getRecyclerCount() - 1);
                            addMessageToQueue(createIncomingTextMessage("To start health risk assessment use the family member relation"));
                            addMessageToQueue(createIncomingTextMessage("Example: 'retake my mother health risk assessment'"));
                            addMessageToQueue(createIncomingTextMessage("Or you can simply say 'retake health risk assessment' for your own health risk assessment"));
                            enableRelativeLayoutSend(true, true, true, false);
                        }
                    } else {
                        if (isFamilyMemberPresent.equalsIgnoreCase("false")) {
                            isFamilyMemberDoesnotExists = true;

                            familyMemberRelation = relation;

                            if (familyMemberRelation.equalsIgnoreCase("Father") || familyMemberRelation.equalsIgnoreCase("Son")
                                    || familyMemberRelation.equalsIgnoreCase("Husband") || familyMemberRelation.equalsIgnoreCase("Brother")) {
                                familyMemberGender = "Male";
                            } else if (familyMemberRelation.equalsIgnoreCase("Mother") || familyMemberRelation.equalsIgnoreCase("Daughter")
                                    || familyMemberRelation.equalsIgnoreCase("Wife") || familyMemberRelation.equalsIgnoreCase("Sister")) {
                                familyMemberGender = "Female";
                            }

                            /*addFamilyLogicId = 2;
                            isAddFamilyTrue = true;
                            startAddFamilyLogic(addFamilyLogicId);
                            enableRelativeLayoutSend(true,true,false,false);*/

                            saveAndSendIncomingMessage(new IncomingTextMessage("Would you like to add " + familyMemberRelation.toLowerCase() + " ?", getActivity()));

                            ChatMessage chatMessage = new ChatMessage();
                            ArrayList<String> arrayList = new ArrayList<>();
                            arrayList.add("Yes");
                            arrayList.add("Later");
                            IncomingHorizontalListMessage incomingHorizontalListMessage = new
                                    IncomingHorizontalListMessage(getActivity(), arrayList, this);
                            chatMessage.setMessageItem(incomingHorizontalListMessage);
                            addMessageToQueue(chatMessage);

                            enableRelativeLayoutSend(false, true, false, false);

                        } else {
                            switch (wizardStatus) {
                                case "1":
                                case "2":
                                    hraId = 0;
                                    break;
                                case "3":
                                    hraId = 4;
                                    break;
                                case "4":
                                    hraId = 7;
                                    break;
                                case "5":
                                    hraId = 9;
                                    break;
                                default:
                                    hraId = 0;
                                    break;
                            }
                            isHRATrue = true;
                            familyMemberAge = age;
                            startHRALogic(hraId);
                            //customerManager.setCurrentFamilyMember(familyMembers.get(position));
                        }
                    }
                } else if (jsonMessageResponse.getIntentName().equalsIgnoreCase("book_package")) {
                    System.out.println("FragmentChat.onChatServeResponse========"+"flow book");
                    if (isBookAppointmentTrue) {
                        if (bookAppointmentId == 1) {
                            //call server for package list
                            System.out.println("FragmentChat.onChatServeResponse========"+"is true");

                            bookAppointmentId = bookAppointmentId + 1;
                            startBookPackage(bookAppointmentId);
                        }
                    } else {
                        System.out.println("FragmentChat.onChatServeResponse========"+"else book");

                        generateServerMessage(jsonMessageResponse);

                        requestPackages.clear();

                        String relation = "";
                        String isFamilyMemberPresent = "";

                        if (jsonMessageResponse.getParameters() != null) {
                            try {
                                JsonElement jsonElement = jsonMessageResponse.getParameters();
                                System.out.println("FragmentChat.run retake hra jsonElement.getParameters=" + jsonElement);
                                JsonObject jsonObject = jsonElement.getAsJsonObject();
                                JsonArray jsonArray = jsonObject.getAsJsonArray("package");
                                relation = jsonObject.get("relation").getAsString();
                                isFamilyMemberPresent = jsonObject.get("is_family_member_added").getAsString();
                                bookPackageServerTime = jsonObject.get("time").getAsString();
                                bookPackageServerDate = jsonObject.get("date").getAsString();

                                for (int i = 0; i < jsonArray.size(); i++) {
                                    requestPackages.add(new Gson().fromJson(jsonArray.get(i).getAsJsonObject().toString(), PackageItem.class));
                                }
                                System.out.println("FragmentChat.onChatServeResponse requestPackages=" + requestPackages.toString());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (relation.isEmpty()) {
                            //ask for whom
                        }else{
                            if (isFamilyMemberPresent.equalsIgnoreCase("false")) {
                                addMessageToQueue(createIncomingTextMessage("I couldn't find the "+ relation + " you asked for?"));

                            }
                            //ask for whom
                        }

                        isBookAppointmentTrue = true;

                        familyMembers = mProfileManager.getFamilyMembers();
                        if(familyMembers.size()==0){
                            System.out.println("Bookflow======="+"family size 0");
                            bookAppointmentId = 0;
                            if (!requestPackages.isEmpty()) {

                                bookPackageCustomerId=customerManager.getCurrentCustomer().getId();
                                System.out.println("Bookflow======="+"family size 0"+bookPackageCustomerId);
                                ChatMessage chatMessage = createIncomingTextMessage(getResources().getString(R.string.book_Would_add_more));
                                ChatMessage chatMessage1 = new ChatMessage();
                                List<String> stringList = new ArrayList<>();
                                stringList.add("Add more");
                                stringList.add("Proceed");
                                IncomingHorizontalListMessage incomingHorizontalListMessage = new IncomingHorizontalListMessage(getActivity(),
                                        stringList, this);
                                chatMessage1.setMessageItem(incomingHorizontalListMessage);
                                enableRelativeLayoutSend(true, true, false, true);
                                addMessageToQueue(chatMessage);
                                addMessageToQueue(chatMessage1);
                            } else {
                                bookPackageCustomerId=customerManager.getCurrentCustomer().getId();
                                System.out.println("Bookflow======= "+"family size 0 else "+bookPackageCustomerId);

                                startBookPackage(bookAppointmentId);
                            }

                        }else {
                            System.out.println("Bookflow======="+"family size");
                            bookAppointmentId = 99;
                            addMessageToQueue(createIncomingTextMessage( getResources().getString(R.string.whom_do_you_want_book_appointment)));
                            if (!customerManager.isLoggedInCustomer())
                            {
                                System.out.println("Bookflow======="+"family size not");
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
                                System.out.println("Bookflow======="+"family size else no");
                                mCustomer = mProfileManager.getLoggedinCustomer();
                                if(!familyMembers.contains(mCustomer))
                                    familyMembers.add(0, mCustomer);
                            }

                            for(int i = 0; i<familyMembers.size(); i++){
                                familyMemberNamesList.add(familyMembers.get(i).getFirst_name() +" "+familyMembers.get(i).getLast_name());
                            }


                            final Fragment fragmentRelation = new FragmentSingleWheelView(this);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    expand(fragmentRelation, familyMemberNamesList, null, true, "", "");
                                    enableRelativeLayoutSend(true, true, false, true);
                                }
                            }, 300);
                        }
                        System.out.println("familyMembers======"+familyMembers.size());


                        // enableRelativeLayoutSend(true, true, false, false);


                        /*if (!requestPackages.isEmpty()) {
                            removeMessage(getRecyclerCount() - 1);
                            ChatMessage chatMessage = createIncomingTextMessage(getResources().getString(R.string.book_Would_add_more));
                            ChatMessage chatMessage1 = new ChatMessage();
                            List<String> stringList = new ArrayList<>();
                            stringList.add("Add more");
                            stringList.add("Proceed");
                            IncomingHorizontalListMessage incomingHorizontalListMessage = new IncomingHorizontalListMessage(getActivity(),
                                    stringList, this);
                            chatMessage1.setMessageItem(incomingHorizontalListMessage);

                            enableRelativeLayoutSend(true, true, false, true);

                            addMessageToQueue(chatMessage);
                            addMessageToQueue(chatMessage1);
                        } else {
                            System.out.println("FragmentChat.setServerResponse BOOK PACKAGE");
                            startBookPackage(bookAppointmentId);
                        }*/

                    }
                } else {
                    System.out.println("FragmentChat.onChatServeResponse========"+"else flow book");

                    generateServerMessage(jsonMessageResponse);

                }
            } else {
                System.out.println("FragmentChat.onChatServeResponse Null Response=========" + "No");
                enableRelativeLayoutSend(true, true, true, false);

                addNewMessage(createIncomingTextMessage("Oooops... try something else"), getRecyclerCount());

                if (isAddFamilyTrue) {
                    clearAddFamilyFlow();
                }

                if (isHRATrue) {
                    clearHraFlow();
                }

                if (isaskQuaryDoctorTrue) {
                    clearAskQueryFlow();
                }

                if (isBookAppointmentTrue) {
                    clearBookPackageFlow();
                }
            }
        } catch (NullPointerException e) {
            enableRelativeLayoutSend(true, true, true, false);
            System.out.println("2.FragmentChat.run chat server Exception=" + "Yes");
            e.printStackTrace();
            removeMessage(getRecyclerCount() - 1);
            addNewMessage(createIncomingTextMessage("Oooops. Didn't understood, try something else"), getRecyclerCount());

            if (isAddFamilyTrue) {
                clearAddFamilyFlow();
            }

            if (isHRATrue) {
                clearHraFlow();
            }

            if (isaskQuaryDoctorTrue) {
                clearAskQueryFlow();
            }

            if (isBookAppointmentTrue) {
                clearBookPackageFlow();
            }
        }
    }

    private void clearAddFamilyFlow() {
        familyMemberGender = "";
        familyMemberName = "";
        familyMemberAge = 0;
        familyMemberRelation = "";
        familyMemberDOF = "";
        familyMemberEmail = "";
        familyMemberMobile = "";
        isAddFamilyTrue = false;
        addFamilyLogicId = -1;
    }

    private void clearBookPackageFlow() {
        isBookAppointmentTrue = false;
        bookAppointmentId = -1;
        bookPackageServerTime = "";
        bookPackageServerDate = "";
        packageDate = "";
        packageIds = "";
        sponsorIds="";
        packageWholeAddress="";
        packageAddressLine1="";
        packageAddressLine2="";
        packageCity="";
        packageState="";
        packageCounty="";
        packagePostalCode="";
        packageLatitude="";
        pacakageLangtude="";
        pacakageHouseNumber="";
        homeCollectionEnable="";
        packageAppointmentTime = "";
        packageTime = "";
        packageDay = 0;
        packageYear = 0;
        packageMonth = 0;
        packageHour = 0;
        packageMin = 0;
        packagePrice = 0;
        packageTotalPrice = 0;
        walletMoney = 0;
        familyMembers.clear();
        familyMemberNamesList.clear();

    }

    private void clearAskQueryFlow() {
        askQueryId = -1;
        isaskQuaryDoctorTrue = false;
        askQueryDate = "";
        askQueryMode = "";
        askQueryPackagePrice = 0;
        askQueryTotalPackagePrice = 0;
        walletMoney = 0;
        askQueryQuestion = "";
        askQuerySpeciality = "";
        askQueryTime = "";
    }

    private void clearHraFlow() {
        familyMemberGender = "";
        familyMemberName = "";
        familyMemberAge = 0;
        familyMemberRelation = "";
        familyMemberDOF = "";
        familyMemberEmail = "";
        familyMemberMobile = "";
        isAddFamilyTrue = false;
        addFamilyLogicId = -1;

        hraFatherCondition = "";
        hraBloodSugar = "";
        hraBloodGroup = "";
        hraBloodGroupId = "";
        hraId = -1;
        hraHeightInch = "";
        hraHeightFeet = "";
        hraWeight = "";
        hraWaist = "";
        hraAlcohol = "";
        hraSmoke = "";
        hraExercise = "";
        hraMotherCondition = "";
        hraSystolic = "";
        hraDiastolic = "";
        hraBloodSugar = "";
        isHRATrue = false;
        isRetakeHraEnabled = false;
    }

    private void generateServerMessage(JsonMessageResponse jsonMessageResponse) {
        System.out.println("FragmentChat.generateServerMessage");
        removeMessage(getRecyclerCount() - 1);

        try {

            JsonElement jsonElement = jsonMessageResponse.getPayload();
            System.out.println("FragmentChat.run jsonElement=" + jsonElement);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            enableRelativeLayoutSend(true, true, true, false);

            for (int i = 0; i < jsonArray.size(); i++) {
                System.out.println("FragmentChat.setServerResponse jsonElements=" + jsonArray.get(i) + " i=" + i);

                JsonObject jsonObjectSub = jsonArray.get(i).getAsJsonObject();

                System.out.println("FragmentChat.setServerResponse jsonObjectSub=" + jsonObjectSub.get("type"));

                ChatMessage chatMessage = createMessage(jsonObjectSub, true);

                if (chatMessage != null) {
                    if (i == 0) {
                        addNewMessage(chatMessage, getRecyclerCount());
                        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

                    } else {
                        addMessageToQueue(chatMessage);
                        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                    }
                }
            }
            System.out.println("FragmentChat.run size=" + getRecyclerCount());
        } catch (Exception e) {
            enableRelativeLayoutSend(true, true, true, false);
            System.out.println("1.FragmentChat.run chat server Exception=" + e);
            addNewMessage(createIncomingTextMessage("I have came across such question for the first time. I will find its answer soon."), getRecyclerCount());
            recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
        }
    }

    @Override
    public void onAddFamilyResponse(JSONObject response) {
        mProfileData = new Gson().fromJson(response.toString(), ProfileData.class);
        if (mProfileData != null && mProfileData.getCustomer() != null) {
            try {
                if (mProfileData.getFamily_members() != null
                        && mProfileData.getFamily_members().getMember_list() != null
                        && mProfileData.getFamily_members().getMember_list().get(0) != null) {
                    strFamilyMemberKey = mProfileData.getFamily_members().getMember_list().get(0).getIdentification_token();
                }
                removeMessage(getRecyclerCount() - 1);

                saveAndSendIncomingMessage(new IncomingTextMessage(familyMemberName.toLowerCase() + " is added", getActivity()));

                recyclerViewChat.scrollToPosition(getRecyclerCount());

                ChatMessage chatMessageHRA = createIncomingTextMessage(getResources().getString(R.string.add_family_complet_hra_message));
                addMessageToQueue(chatMessageHRA);
                recyclerViewChat.scrollToPosition(getRecyclerCount());

                ChatMessage tempChatMessage = new ChatMessage();
                ArrayList<String> conformationList = new ArrayList<>();
                conformationList.add("No");
                conformationList.add("Yes");
                IncomingHorizontalListMessage incomingHorizontalListMessage = new IncomingHorizontalListMessage(getActivity(), conformationList, this);
                tempChatMessage.setMessageItem(incomingHorizontalListMessage);

                addMessageToQueue(tempChatMessage);

                mProfileManager.setProfileData(mProfileData);

            } catch (NullPointerException e) {
                e.printStackTrace();
                saveAndSendIncomingMessage(new IncomingTextMessage("Failed to add " + familyMemberName.toLowerCase(), getActivity()));

                return;
            }
        } else {
            {
                String msg = "";
                if (mProfileData.getMsg() != null) {
                    msg = mProfileData.getMsg().toString();
                } else if (mProfileData.getMessage() != null) {
                    msg = mProfileData.getMessage().toString();
                } else {
                    msg = "family member not added";
                }

                saveAndSendIncomingMessage(new IncomingTextMessage(msg, getActivity()));

                //CustomeDialog.dispDialog(getActivity(), msg);
            }
        }
    }

    @Override
    public void onDoctorSpecializationResponse(JSONObject response, String noJson) {

    }

    @Override
    public void onHraComplitionResponse(JSONObject response, String idToken) {
        System.out.println("FragmentChat.onHraComplitionResponse prefs.getCustomerWizardStatus()="
                + prefs.getCustomerWizardStatus());

        System.out.println("FragmentChat.onHraComplitionResponse response = " + response);

        if (prefs.getIsFirstTimeRegister()) {
            switch (prefs.getCustomerWizardStatus()) {
                case 0:
                    prefs.setCustomerWizardStatus(4);
                    break;
                case 4:
                    prefs.setCustomerWizardStatus(7);
                    break;
                case 7:
                    prefs.setCustomerWizardStatus(9);
                    break;
                case 9:
                    prefs.setCustomerWizardStatus(0);
                    break;
                default:
                    break;
            }
        }

        if (!isHRATrue) {

            ProfileData respnseProfileData = new Gson().fromJson(response.toString(), ProfileData.class);
            prefs.setWizardCompleteFor(idToken, true);

            customerManager = CustomerManager.getInstance(getActivity());
            mProfileManager = ProfileManager.getInstance(getActivity());

            ProfileData currentProfileData = mProfileManager.getProfileData();

            Customer updatedCustomer = respnseProfileData.getCustomer();

            saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.hra_completion), getActivity()));

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("@");
            stringBuilder.append(updatedCustomer.getIdentification_token());
            stringBuilder.append("/");
            stringBuilder.append("insights");

            CardViewModel cardViewModel = new CardViewModel(getResources().getString(R.string.hra_message)
                    ,updatedCustomer.getOptimum_health_score(),"View Insights",updatedCustomer.getHealth_score(),"",0,"",stringBuilder.toString(),"");
            IncomingProfileCompletionMessage incomingProfileCompletionMessage =
                    new IncomingProfileCompletionMessage(cardViewModel,getActivity(),this);

            if (isRetakeHraEnabled || prefs.getIsFirstTimeRegister()) {
                saveAndSendIncomingMessage(incomingProfileCompletionMessage);

                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
            }


            if (familyMemberAge >= 18) {
                removeMessage(getRecyclerCount() - 1);
                if (mProfileManager.getLoggedinCustomer() == null || !prefs.getIsFIrstWizard()) {
                    System.out.println("FragmentChat.onHraComplitionResponse registration condition");
                    mProfileManager.setProfileData(respnseProfileData);

                    customerManager.setCurrentCustomer(respnseProfileData.getCustomer());
                    customerManager.setCurrentFamilyMember(respnseProfileData.getCustomer());
                    customerManager.isWizardShowing = false;
                    customerManager.setLoggedInCustomer(true);
                    prefs.setIsFIrstWizard(true);

                } else if (currentProfileData.getFamily_members() != null) {
                    System.out.println("FragmentChat.onHraComplitionResponse adult condition");

                    List<Customer> m = currentProfileData.getFamily_members().getMember_list();

                    for (int i = 0; i < m.size(); i++) {
                        if (updatedCustomer.getIdentification_token().equalsIgnoreCase(m.get(i).getIdentification_token())) {
                            m.set(i, updatedCustomer);
                            ProfileManager profileManager = ProfileManager.getInstance(getActivity());
                            profileManager.setProfileData(currentProfileData);

                            saveAndSendIncomingMessage(incomingProfileCompletionMessage);

                            recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
                            break;
                        }
                    }
                }

                final ChatMessage hramessage43 = createIncomingTextMessage("You can upload old records. All you have to do is take a picture of your lab reports and we will take care of the rest.");

                final ChatMessage hramessage4 = createIncomingTextMessage("You can also add your family members to start tracking their health");
                final ChatMessage hramessage5 = new ChatMessage();
                List<String> stringList = new ArrayList<>();
                stringList.add("Digitize medical reports");
                stringList.add("Add family members");
                stringList.add("Book health check");
                IncomingHorizontalListMessage incomingHorizontalListMessage = new IncomingHorizontalListMessage(getActivity(),
                        stringList, this);
                hramessage5.setMessageItem(incomingHorizontalListMessage);

                addMessageToQueue(hramessage43);
                addMessageToQueue(hramessage4);
                addMessageToQueue(hramessage5);

            } else {
                if (currentProfileData.getFamily_members().getMember_list().size() != 0) {
                    System.out.println("FragmentChat.onHraComplitionResponse child condition");

                    List<Customer> m = currentProfileData.getFamily_members().getMember_list();
                    for (int i = 0; i < m.size(); i++) {
                        if (updatedCustomer.getIdentification_token().equalsIgnoreCase(m.get(i).getIdentification_token())) {
                            m.set(i, updatedCustomer);
                            ProfileManager profileManager = ProfileManager.getInstance(getActivity());
                            profileManager.setProfileData(currentProfileData);

                            //saveAndSendIncomingMessage(new IncomingTextMessage("Awesome! you have completed the health risk assessment of " + familyMemberName, getActivity()));

                            break;
                        }
                    }
                }
            }

            familyMemberGender = "";
            familyMemberName = "";
            familyMemberAge = 0;
            familyMemberRelation = "";
            familyMemberDOF = "";
            familyMemberEmail = "";
            familyMemberMobile = "";
            isAddFamilyTrue = false;
            addFamilyLogicId = -1;

            hraFatherCondition = "";
            hraBloodSugar = "";
            hraBloodGroup = "";
            hraBloodGroupId = "";
            hraId = -1;
            hraHeightInch = "";
            hraHeightFeet = "";
            hraWeight = "";
            hraWaist = "";
            hraAlcohol = "";
            hraSmoke = "";
            hraExercise = "";
            hraMotherCondition = "";
            hraSystolic = "";
            hraDiastolic = "";
            hraBloodSugar = "";
            isHRATrue = false;

            if (isRetakeHraEnabled && familyMemberName.isEmpty()) {
                Activity activity = getActivity();
                if (activity instanceof MainActivity) {
                    final MainActivity myactivity = (MainActivity) activity;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            customerManager.setCurrentFamilyMember(mProfileManager.getLoggedinCustomer());
                            myactivity.refreshProfile();
                        }
                    }, 5400);
                }
            }

            isRetakeHraEnabled = false;

            enableRelativeLayoutSend(true, true, true, false);

            if (prefs.getIsFirstTimeRegister()) {
                prefs.setIsFirstTimeRegister(false);

                System.out.println("FragmentChat.onHraComplitionResponse setIsFirstTimeRegister condition=====" + "YEs");
                pushNotificationToken();

                Activity activity = getActivity();
                if (activity instanceof MainActivity) {
                    final MainActivity myactivity = (MainActivity) activity;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myactivity.refreshProfile();

                            serverRequests.getWalletDataFirst();
                        }
                    }, 5000);
                }
            }

            mCustomer = mProfileManager.getLoggedinCustomer();
        }
    }

    private void pushNotificationToken() {

        String regId = prefs.getRegistrationId();

        if (mProfileManager.getLoggedinCustomer() != null && !prefs.getRegistrationId().equals("") && !prefs.isSentTokenFor(mProfileManager.getLoggedinCustomer().getIdentification_token())) {


            if (NetworkCaller.isInternetOncheck(getActivity())) {
                JSONObject object2 = new JSONObject();
                JSONObject object = new JSONObject();
                try {
                    object.put("device_name", android.os.Build.MODEL);
                    object.put("device_type", TagValues.deviceType);
                    System.out.println("-----registrationid - " + prefs.getRegistrationId());
                    object.put("token", prefs.getRegistrationId());
                    object2.put("push_token", object);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                String requestString = "{\"push_token\":" + object.toString() + "}";

                pushTokenRequest(object2);
            }


        }
    }

    private void pushTokenRequest(JSONObject object2) {
        String URL = TagValues.PUSH_TOCKENS_URL;
        JsonObjectRequest jsObjRequesttwo = new JsonObjectRequest(Request.Method.POST, URL, object2,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            System.out.println("login register otp====" + response.toString());
                            prefs.sentTokenFor(mProfileManager.getLoggedinCustomer().getIdentification_token());
                        } else {
                            CustomeDialog.dispDialog(getActivity(), TagValues.DATA_NOT_FOUND);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                params.put("X-DEVICE-ID", customerManager.getDeviceID(getActivity()));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsObjRequesttwo);

    }

    @Override
    public void onDocumentUploadResponse(final String text, boolean isUploaded) {
        IncomingTextMessage incomingTextMessage = new IncomingTextMessage(text, getActivity());

        if (isUploaded) {
            outgoingChatMessage.setMessageItem(incomingTextMessage);
            RecyclerView.ItemAnimator animator = recyclerViewChat.getItemAnimator();
            if (animator instanceof SimpleItemAnimator) {
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            }
            mRecyclerAdapter.update(sentDocumentPosition, outgoingChatMessage);

            String loginJson = "{\"type\":" + "\"" + (MessageType.INCOMING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + text + "\"" + "}";
            dbHandler.insertChatData(new ChatDatabaseModel((MessageType.INCOMING_TEXT).toString(), (MessageSource.BOT).toString(), loginJson));

            enableRelativeLayoutSend(true, true, true, false);
            sentDocumentPosition = 0;
        } else {
            outgoingChatMessage = new ChatMessage();
            outgoingChatMessage.setMessageItem(incomingTextMessage);

            sentDocumentPosition = getRecyclerCount() + 1;

            addMessageToQueue(outgoingChatMessage);

            enableRelativeLayoutSend(true, true, true, false);
        }
        System.out.println("FragmentChat.onDocumentUploadResponse sentPosition=" + sentPosition + " isUploaded=" + isUploaded);
    }

    @Override
    public void onPictureUploadResponse(JSONObject response) {

    }

    @Override
    public void onErrorResponse(final String error, final String textMessage) {
        System.out.println("FragmentChat.onErrorResponse error=" + error);

        /*IncomingImageMessage incomingImageMessage = new IncomingImageMessage("https://d13yacurqjgara.cloudfront.net/users/1373974/screenshots/3029413/500.gif",getActivity());
        ChatMessage chatMessageImage = new ChatMessage();
        chatMessageImage.setMessageItem(incomingImageMessage);
        addMessageToQueue(chatMessageImage);
*/
        if (textMessage.isEmpty()) {
            removeMessage(mRecyclerAdapter.getItemCount() - 1);
            saveAndSendIncomingMessage(new IncomingTextMessage(error + " " + getEmojiByUnicode(0x1F628), getActivity()));
            recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
            enableRelativeLayoutSend(true, true, true, false);

        } else {
            OutgoingTextMessage outgoingTextMessage = new OutgoingTextMessage(textMessage + " " + getEmojiByUnicode(0x1F633), getActivity());
            outgoingTextMessage.setStatus(Config.MESSAGE_FAILED);
            System.out.println("FragmentChat.onResponse sentPosi-tion=" + sentPosition);
            outgoingChatMessage.setMessageItem(outgoingTextMessage);
            RecyclerView.ItemAnimator animator = recyclerViewChat.getItemAnimator();
            if (animator instanceof SimpleItemAnimator) {
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            }
            mRecyclerAdapter.update(sentPosition, outgoingChatMessage);

            String loginJson = "{\"type\":" + "\"" + (MessageType.INCOMING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + textMessage + " " + getEmojiByUnicode(0x1F633) + "\"" + "}";
            dbHandler.insertChatData(new ChatDatabaseModel((MessageType.INCOMING_TEXT).toString(), (MessageSource.BOT).toString(), loginJson));


            removeMessage(mRecyclerAdapter.getItemCount() - 1);

            ChatMessage chatMessage = createIncomingTextMessage(textMessage);
            addMessageToQueue(chatMessage);
            recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
            enableRelativeLayoutSend(true, true, true, false);
        }

        if (isBookAppointmentTrue) {
            bookAppointmentId = -1;
            isBookAppointmentTrue = false;

            bookPackageServerTime = "";
            bookPackageServerDate = "";
            packageDate = "";
            packageIds = "";
            homeCollectionEnable="";
            sponsorIds="";
            packageWholeAddress="";
            packageAddressLine1="";
            packageAddressLine2="";
            packageCity="";
            packageState="";
            packageCounty="";
            packagePostalCode="";
            packageLatitude="";
            pacakageLangtude="";
            pacakageHouseNumber="";
            packageAppointmentTime = "";
            packageTime = "";
            packageDay = 0;
            packageYear = 0;
            packageMonth = 0;
            packageHour = 0;
            packageMin = 0;
            packagePrice = 0;
            packageTotalPrice = 0;
            walletMoney = 0;

        }

        if (isAddFamilyTrue) {
            addFamilyLogicId = -1;
            isAddFamilyTrue = false;
            familyMemberName = "";
            familyMemberAge = 0;
            familyMemberGender = "";
            familyMemberRelation = "";
            familyMemberMobile = "";
            familyMemberDOF = "";
            familyMemberEmail = "";
        }

        if (isaskQuaryDoctorTrue) {
            askQueryDate = "";
            askQueryMode = "";
            askQueryPackagePrice = 0;
            askQueryTotalPackagePrice = 0;
            walletMoney = 0;
            askQueryQuestion = "";
            askQuerySpeciality = "";
            askQueryTime = "";
            askQueryId = -1;
            isaskQuaryDoctorTrue = false;
        }

        if(isAgeGenderFlowNeed){
            prefs.setLoggedinUserGender("");
            prefs.setLoggedinUserDOB("");
            startAgeQuery();

        }
    }

    @Override
    public void onUpdateAppResponse() {
        ChatMessage chatMessage = new ChatMessage();

        String title = "New version available";
        String subtitle = "Download the latest version at play store";
        String primaryButton = "Update";
        String secondaryButton = "Cancel";
        String primaryAction = "Update eKincare app";
        String secondaryAction = "Cancel eKincare app update";
        String url = "";
        int resource = 0;

        CardViewModel cardViewModel = new CardViewModel(title, subtitle, primaryButton, secondaryButton, url,
                resource, "", primaryAction, secondaryAction);

        IncomingCardWithTextAndButtonMessage incomingCardWithTextAndButtonMessage = new IncomingCardWithTextAndButtonMessage(cardViewModel,
                getActivity(), this, this);

        chatMessage.setMessageItem(incomingCardWithTextAndButtonMessage);

        removeMessage(getRecyclerCount() - 1);
        addNewMessage(chatMessage, getRecyclerCount());
    }

    @Override
    public void onLocationResponse(String address) {
        Toast.makeText(getActivity(), address, Toast.LENGTH_LONG).show();

        removeMessage(getRecyclerCount() - 1);

        ChatMessage chatMessage2 = createIncomingTextMessage("Your are at " + address);

        ChatMessage chatMessage = new ChatMessage();
        OutgoingMapMessage outgoingMapMessage = new OutgoingMapMessage(getActivity(), new LatLng(location.getLatitude(), location.getLongitude()));
        chatMessage.setMessageItem(outgoingMapMessage);

        ChatMessage chatMessage3 = createIncomingTextMessage("Fetching available packages near your " +
                "location.");

        addNewMessage(chatMessage2, getRecyclerCount());

        tempChatMessageQueue.add(tempChatMessageQueue.size(), chatMessage3);
        if (!isQueueRunning) {
            showDelayed();
        }

        //sendDataLogic for packages list

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                generateIncomingMessageAnimation();

            }
        }, 2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //bookAppointmentId = bookAppointmentId + 1;
                //startBookPackage(bookAppointmentId);
                serverRequests.getPackageList(prefs.getPackageType());
            }
        }, 4000);

    }

    @Override
    public void onGetPackageResponse(JSONObject response, String noJson) {
        removeMessage(getRecyclerCount() - 1);

        System.out.println("FragmentChat.onGetPackageResponse response=" + response.toString());

        PackageTest mPackageListData = new Gson().fromJson(response.toString(), PackageTest.class);
        if (mPackageListData != null) {
            System.out.println("FragmentChat.onGetPackageResponse mPackageListData.getPackages()=" + mPackageListData.getPackages());

            List<PackageDataSet> mArrayListAssessmentType = new ArrayList<>();
            mArrayListAssessmentType = mPackageListData.getPackages();

            if (mArrayListAssessmentType.size() > 0) {

                bookAppointmentId = bookAppointmentId + 1;
                startBookPackage(bookAppointmentId);
            } else {
                bookAppointmentId = -1;
                isBookAppointmentTrue = false;
                addNewMessage(createIncomingTextMessage("Error processing request, booking request was unsuccessful" + " " + getEmojiByUnicode(0x1F633)), getRecyclerCount());
            }
        } else {
            bookAppointmentId = -1;
            isBookAppointmentTrue = false;
            tempChatMessageQueue.add(tempChatMessageQueue.size(), createIncomingTextMessage("Error processing request, booking request was unsuccessful" + " " + getEmojiByUnicode(0x1F613)));
            if (!isQueueRunning) {
                showDelayed();
            }

        }

    }

    @Override
    public void onGetPackageFeeResponse(JSONObject response, String noJson) {
        try {
            enableRelativeLayoutSend(false, true, false, false);

            removeMessage(getRecyclerCount() - 1);

            JSONObject jsonFee = response.getJSONObject("fee");

            prefs.setPackageMrp(jsonFee.getString("inr_val"));

            addMessageToQueue(createIncomingTextMessage("The total cost is " + "Rs " + jsonFee.getString("inr_val") + "."));
            recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

            isPaymentFailed = false;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    generateIncomingMessageAnimation();
                    serverRequests.getWalletData();
                }
            }, 1300);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDCPackagesResponse(JSONObject response) {

        System.out.println("FragmentChat.onDCPackagesResponse Json=" + response);

        removeMessage(getRecyclerCount() - 1);

        String message = new Gson().fromJson(response.toString(), DCPackage.class).getMessage();

        if (message.equalsIgnoreCase("There are no diagnostic centers for these packages combination")) {
            enableRelativeLayoutSend(true, true, false, false);
            saveAndSendIncomingMessage(new IncomingTextMessage(message, getActivity()));

            saveAndSendIncomingMessage(new IncomingTextMessage("Would you like to book another package or search for another location?", getActivity()));

            List<String> options = new ArrayList<>();
            options.add("Later");
            options.add("Book another package");
            options.add("Search for different location");

            IncomingHorizontalListMessage incomingHorizontalListMessage = new IncomingHorizontalListMessage(getActivity(), options, this);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMessageItem(incomingHorizontalListMessage);
            addMessageToQueue(chatMessage);

        } else if (message.equalsIgnoreCase("Success")) {
            dcLabs = new Gson().fromJson(response.toString(), DCPackage.class).getLabs();
            System.out.println("FragmentChat.onDCPackagesResponse dcLabs=" + dcLabs.size());
            if (dcLabs.size() > 0) {
                bookAppointmentId = bookAppointmentId + 1;
                startBookPackage(bookAppointmentId);
            } else {
                System.out.println("FragmentChat.onDCPackagesResponse dcLabs=" + "NO ");

                saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.book_no_select_diagnostic_center), getActivity()));


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                        try {
                            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);

            }

        }
    }

    @Override
    public void onProfileResponse(JSONObject response) {
        if (response != null) {
            try {
                JSONObject docProfile = response.getJSONObject("doctor_profile");
                Dialog profileDialog;
                TextView textViewTitle, textViewDiscription, textLanguage, textSpecilites, textViewButton1, textViewButton2;
                CircleImageView imageView;

                profileDialog = new Dialog(getActivity());
                profileDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                profileDialog.setContentView(R.layout.item_message_card_with_image);
                profileDialog.setCancelable(true);

                textViewTitle = (TextView) profileDialog.findViewById(R.id.textview_title);
                textViewDiscription = (TextView) profileDialog.findViewById(R.id.textview_discription);
                textViewButton1 = (TextView) profileDialog.findViewById(R.id.button1_fee);
                textViewButton2 = (TextView) profileDialog.findViewById(R.id.button2_fee);
                textLanguage = (TextView) profileDialog.findViewById(R.id.textview_language);
                textSpecilites = (TextView) profileDialog.findViewById(R.id.textview_specialities);
                imageView = (CircleImageView) profileDialog.findViewById(R.id.imageview_background2);

                if (!docProfile.getString("photo").isEmpty()) {
                    Glide.with(getActivity()).load(docProfile.getString("photo")).into(imageView);
                } else {
                    imageView.setImageResource(R.drawable.ic_launcher);
                }
                textViewDiscription.setText(docProfile.getString("education"));
                textViewTitle.setText(docProfile.getString("name"));
                textLanguage.setText(docProfile.getString("language"));
                textSpecilites.setText(docProfile.getString("specialities"));
                textViewButton1.setText(docProfile.getJSONObject("consult_fee").getString("inr_val") + " " + getActivity().getResources().getString(R.string.Rs));
                textViewButton2.setText(docProfile.getJSONObject("query_fee").getString("inr_val") + " " + getActivity().getResources().getString(R.string.Rs));
                profileDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        System.out.println("FragmentChat.onSpecializationResponse=======" + response.toString());
    }

    @Override
    public void onSpecializationResponse(JSONObject response, String noJson) {
        System.out.println("FragmentChat.onSpecializationResponse=======" + noJson);
        removeMessage(getRecyclerCount() - 1);
        addMessageToQueue(createIncomingTextMessage("No such Specialization found"));
        ChatMessage chatMessage = createIncomingTextMessage("Would you like to add some other Specialization?");
        ChatMessage chatMessage1 = new ChatMessage();
        List<String> stringList = new ArrayList<>();
        stringList.add("Add");
        stringList.add("No");
        IncomingHorizontalListMessage incomingHorizontalListMessage = new IncomingHorizontalListMessage(getActivity(),
                stringList, this);
        chatMessage1.setMessageItem(incomingHorizontalListMessage);
        tempChatMessageQueue.add(tempChatMessageQueue.size(), chatMessage);
        tempChatMessageQueue.add(tempChatMessageQueue.size(), chatMessage1);
        if (!isQueueRunning) {
            showDelayed();
        }


    }

    @Override
    public void onPackageDoctorList(JSONObject response, String noJson) {

        System.out.println("FragmentChat.onPackageDoctorList=======" + response.toString());


        if (response != null) {
            DoctorModel mDoctorModelListData = new Gson().fromJson(response.toString(), DoctorModel.class);
            if (mDoctorModelListData != null) {
                System.out.println("FragmentChat.onGetPackageResponse mPackageListData.getPackages()=" + mDoctorModelListData.getDoctors());

                if (mDoctorModelListData.getDoctors().size() > 0) {
                    removeMessage(getRecyclerCount() - 1);

                    saveAndSendIncomingMessage(new IncomingTextMessage("We have lots of experienced and skilled doctors. Select the doctor with whom you would like to ask your query", getActivity()));

                    totalDoctorPackages = mDoctorModelListData.getDoctors();
                    askQueryId = askQueryId + 1;
                    startAskQueryToDoctor(askQueryId);

                } else {
                    removeMessage(getRecyclerCount() - 1);
                    askQueryId = -1;
                    isaskQuaryDoctorTrue = false;

                    saveAndSendIncomingMessage(new IncomingTextMessage("Error processing request, booking request was unsuccessful" + " " + getEmojiByUnicode(0x1F628), getActivity()));
                }
            } else {
                removeMessage(getRecyclerCount() - 1);
                askQueryId = -1;
                isaskQuaryDoctorTrue = false;

                saveAndSendIncomingMessage(new IncomingTextMessage("Error processing request, booking request was unsuccessful" + " " + getEmojiByUnicode(0x1F628), getActivity()));
            }
        }

    }

    @Override
    public void onPackageNameData(PackageItem packageItem, boolean isLast, boolean notFound, String part, int size) {
        System.out.println("FragmentChat.onPackageNameData packageItem=" + packageItem + " isLast=" + isLast + " size=" + size);
        //removeMessage(getRecyclerCount()-1);

        if (notFound) {
            Toast.makeText(getActivity(), "Package " + part + " not found", Toast.LENGTH_LONG).show();
        } else {
            System.out.println("FragmentChat.onPackageNameData part=" + part);
            if (!requestPackages.contains(packageItem)) {
                requestPackages.add(packageItem);
            }
        }


        if (isLast) {
            removeMessage(getRecyclerCount() - 1);
            if (requestPackages.size() == 0) {
                //addMessageToQueue(createIncomingTextMessage("No such package found"));
                saveAndSendIncomingMessage(new IncomingTextMessage("No such package found", getActivity()));

                ChatMessage chatMessage = createIncomingTextMessage("Would you like to add some other Packages?");
                ChatMessage chatMessage1 = new ChatMessage();
                List<String> stringList = new ArrayList<>();
                stringList.add("Add");
                stringList.add("No");
                IncomingHorizontalListMessage incomingHorizontalListMessage = new IncomingHorizontalListMessage(getActivity(),
                        stringList, this);
                chatMessage1.setMessageItem(incomingHorizontalListMessage);
                tempChatMessageQueue.add(tempChatMessageQueue.size(), chatMessage);
                tempChatMessageQueue.add(tempChatMessageQueue.size(), chatMessage1);
                if (!isQueueRunning) {
                    showDelayed();
                }
            } else {
                saveAndSendIncomingMessage(new IncomingTextMessage(getResources().getString(R.string.book_Would_add_more), getActivity()));
                ChatMessage chatMessage1 = new ChatMessage();
                List<String> stringList = new ArrayList<>();
                stringList.add("Add more");
                stringList.add("Proceed");
                IncomingHorizontalListMessage incomingHorizontalListMessage = new IncomingHorizontalListMessage(getActivity(),
                        stringList, this);
                chatMessage1.setMessageItem(incomingHorizontalListMessage);
                addMessageToQueue(chatMessage1);
            }
        }


        //System.out.println("FragmentChat.onPackageNameData packageItem="+packageItem.toString() + " " + packageIds);
    }

    @Override
    public void onPaymentSuccessServerRequest(JSONObject result) {
        System.out.println("FragmentChat.onPaymentSuccessServerRequest");
        enableRelativeLayoutSend(true, true, true, false);
        removeMessage(getRecyclerCount() - 1);

        if (isBookAppointmentTrue) {
            BookAppointmentResponse bookAppointmentResponse = new Gson().fromJson(result.toString(), BookAppointmentResponse.class);

            System.out.println("FragmentChat.onPaymentSuccessServerRequest isBookAppointmentTrue");

            saveAndSendIncomingMessage(new IncomingTextMessage("Thank you for the  payment", getActivity()));
            recyclerViewChat.smoothScrollToPosition(mRecyclerAdapter.getItemCount());

            isBookAppointmentTrue = false;
            bookAppointmentId = -1;
            try{
                saveAndSendIncomingMessage(new IncomingTextMessage("Your appointment for " + bookAppointmentResponse.getAppointment().getDescription() + " is booked at " +
                        packageTime + " on " + packageDate, getActivity()));

            }catch(Exception e){

            }

            for (int i = 0; i < bookAppointmentResponse.getInstructions().size(); i++) {
                saveAndSendIncomingMessage(new IncomingTextMessage(bookAppointmentResponse.getInstructions().get(i), getActivity()));
            }


            bookPackageServerTime = "";
            bookPackageServerDate = "";
            packageDate = "";
            packageIds = "";
            homeCollectionEnable="";
            sponsorIds="";
            packageWholeAddress="";
            packageAddressLine1="";
            packageAddressLine2="";
            packageCity="";
            packageState="";
            packageCounty="";
            packagePostalCode="";
            packageLatitude="";
            pacakageLangtude="";
            pacakageHouseNumber="";
            packageAppointmentTime = "";
            packageTime = "";
            packageDay = 0;
            packageYear = 0;
            packageMonth = 0;
            packageHour = 0;
            packageMin = 0;
            packagePrice = 0;
            packageTotalPrice = 0;

            enableRelativeLayoutSend(true, true, true, false);

        } else if (isaskQuaryDoctorTrue) {
            System.out.println("FragmentChat.onPaymentSuccessServerRequest===" + isaskQuaryDoctorTrue);
            try {
                addMessageToQueue(createIncomingTextMessage("Thank you for the payment."));
                recyclerViewChat.smoothScrollToPosition(mRecyclerAdapter.getItemCount());
                if (askQueryMode.equalsIgnoreCase("phone") || askQueryMode.equalsIgnoreCase("audio")) {
                    addMessageToQueue(createIncomingTextMessage(getResources().getString(R.string.payment_succes_text_audio)));
                } else if (askQueryMode.equalsIgnoreCase("video")) {
                    addMessageToQueue(createIncomingTextMessage(getResources().getString(R.string.payment_succes_text_video)));
                } else {
                    addMessageToQueue(createIncomingTextMessage(getResources().getString(R.string.payment_succes_text_message)));
                }
                recyclerViewChat.smoothScrollToPosition(mRecyclerAdapter.getItemCount());

                askQueryDate = "";
                askQueryMode = "";
                askQueryPackagePrice = 0;
                askQueryTotalPackagePrice = 0;
                walletMoney = 0;
                askQueryQuestion = "";
                askQuerySpeciality = "";
                askQueryTime = "";
                askQueryId = -1;
                isaskQuaryDoctorTrue = false;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onWalletResponse(WalletResponse walletResponse) {
        System.out.println("FragmentChat.onWalletResponse walletRespons"+walletResponse);

        walletMoney = 0;
        try {
            Activity activity = getActivity();
            if (activity instanceof MainActivity) {
                final MainActivity myactivity = (MainActivity) activity;

                if (walletResponse.getStatus().equalsIgnoreCase("200")) {
                    try {
                        walletMoney = Integer.parseInt(walletResponse.getWallet().getBalance());
                        myactivity.showWalletMoney(true, walletResponse);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isaskQuaryDoctorTrue) {
            enableRelativeLayoutSend(false, true, false, false);
            removeMessage(getRecyclerCount() - 1);
            isPaymentFailed = false;

            saveAndSendIncomingMessage(new IncomingTextMessage("Proceed to payment", getActivity()));
            ChatMessage chatMessagePayment = new ChatMessage();
            ArrayList<String> arrayListPayment = new ArrayList<>();
            arrayListPayment.add("Cancel");
            arrayListPayment.add("Yes");
            IncomingHorizontalListMessage incomingHorizontalPayment = new IncomingHorizontalListMessage(getActivity(), arrayListPayment, this);
            chatMessagePayment.setMessageItem(incomingHorizontalPayment);
            addMessageToQueue(chatMessagePayment);
        } else {
            System.out.println("Date test=" + bookAppointmentId + "=======" + "case 5 empty");

            removeMessage(getRecyclerCount() - 1);

            if (isBookAppointmentTrue) {
                saveAndSendIncomingMessage(new IncomingCartCardWithListMessage(getActivity(), requestPackages));
            }
            saveAndSendIncomingMessage(new IncomingTextMessage("Your current location:", getActivity()));
            saveAndSendIncomingMessage(new IncomingTextMessage(packageWholeAddress, getActivity()));
            saveAndSendIncomingMessage(new IncomingTextMessage("Enter your apartment number/flat number", getActivity()));

            recyclerViewChat.smoothScrollToPosition(mRecyclerAdapter.getItemCount());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    editTextMessage.setFilters(new InputFilter[]{textFilter, new InputFilter.LengthFilter(50)});
                    editTextMessage.setHint("Enter apartment number...");
                    editTextMessage.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    enableRelativeLayoutSend(true, true, false, false);
                }
            }, 3000);
        }
    }

    @Override
    public void onWalletResponseFirst(WalletResponse walletResponse) {
        walletMoney = 0;
        try {
            Activity activity = getActivity();
            if (activity instanceof MainActivity) {
                final MainActivity myactivity = (MainActivity) activity;

                if (walletResponse.getStatus().equalsIgnoreCase("200")) {
                    try {
                        walletMoney = Integer.parseInt(walletResponse.getWallet().getBalance());
                        myactivity.showWalletMoney(true, walletResponse);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotificationResponseFirst(JSONObject response) {
        try {
            Activity activity = getActivity();
            if (activity instanceof MainActivity) {
                final MainActivity myactivity = (MainActivity) activity;

                myactivity.showNotificationCount(response.getString("count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void profileUpdated() {
        removeMessage(getRecyclerCount()-1);
        isHRATrue = true;
        isAgeGenderFlowNeed = false;
        hraId = prefs.getCustomerWizardStatus();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        System.out.println("FragmentChat.botIntro prefs.getCustomerDOB()=" + prefs.getCustomerDOB());
        int years = DateUtils.differenceBtwDate(prefs.getCustomerDOB(), formattedDate);
        System.out.println("FragmentChat.onCardWithImagePositiveClick years=" + years + "hraId= " + hraId);
        familyMemberAge = years;
        //initializeViews();
        startHRALogic(hraId);
    }


    @Override
    public void onCardWithListButtonItemClick(String text) {
        if (isAddFamilyTrue || isBookAppointmentTrue || isHRATrue || isaskQuaryDoctorTrue) {
            recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
        } else {
            editTextMessage.setText("");

            // Build messageData object
            outgoingChatMessage = new ChatMessage();
            OutgoingTextMessage outgoingTextMessage = new OutgoingTextMessage(text, getActivity());

            sentPosition = mRecyclerAdapter.getItemCount();

            String loginJson = "{\"type\":" + "\"" + (MessageType.OUTGOING_TEXT).toString() + "\"" + "," + "\"message\":" + "\"" + text + "\"" + "}";
            dbHandler.insertChatData(new ChatDatabaseModel((MessageType.OUTGOING_TEXT).toString(), (MessageSource.SELF).toString(), loginJson));

            if (NetworkCaller.isInternetOncheck(getActivity())) {
                outgoingTextMessage.setStatus(Config.MESSAGE_SENT);
                outgoingChatMessage.setMessageItem(outgoingTextMessage);
                addNewMessage(outgoingChatMessage, sentPosition);
                sendDataLogic(text);
            } else {
                outgoingTextMessage.setStatus(Config.MESSAGE_FAILED);
                outgoingChatMessage.setMessageItem(outgoingTextMessage);
                addNewMessage(outgoingChatMessage, sentPosition);
            }
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        System.out.println("FragmentChat.onNetworkConnectionChanged");
        if (isConnected) {
            relativeLayoutSend.setVisibility(View.VISIBLE);
            relativeLayoutSend.startAnimation(slide_up);
        } else {
            relativeLayoutSend.startAnimation(slide_down);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EkinCareApplication.getInstance().setConnectivityListener(this);
    }


    @Override
    public void onCardWithImagePositiveClick(String text) {
        System.out.println("FragmentChat.onCardWithImagePositiveClick text" + text + " jsonMessageResponse.getIntentName()-"
                + jsonMessageResponse.getIntentName() + " addFamilyLogicId=" + addFamilyLogicId);

    }

    @Override
    public void onCardWithImageNegativeClick(String text) {
    }


    public ChatMessage createMessage(JsonObject jsonMessage, boolean isFromServer) {
        System.out.println("FragmentChat.createMessage createMessage=" + jsonMessage.toString());
        String header;
        String subheader;
        String primary_button_name;
        String secondary_button_name;
        String url;
        String secondary_button_action;
        String primary_button_action;
        CardViewModel cardViewModel;

        if (isFromServer) {
            ChatDatabaseModel chatDatabaseModel = new ChatDatabaseModel(jsonMessage.get("type").getAsString(), "BOT", jsonMessage.toString());
            dbHandler.insertChatData(chatDatabaseModel);
        }

        ChatMessage chatMessage = new ChatMessage();
        try {
            String type = jsonMessage.get("type").getAsString();
            System.out.println("FragmentChat.createMessage=====" + type);

            if (type.equalsIgnoreCase((MessageType.INCOMING_TEXT).toString())) {
                String text = "";
                try {
                    text = jsonMessage.get("message").getAsString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!text.isEmpty()) {
                    IncomingTextMessage incomingTextMessage = new IncomingTextMessage(text, getActivity());
                    chatMessage.setMessageItem(incomingTextMessage);


                } else {
                    IncomingTextMessage incomingTextMessage = new IncomingTextMessage("Wrong message from server", getActivity());
                    chatMessage.setMessageItem(incomingTextMessage);
                }
            } else if (type.equalsIgnoreCase((MessageType.OUTGOING_TEXT).toString())) {
                OutgoingTextMessage outgoingTextMessage = new OutgoingTextMessage(jsonMessage.get("message").getAsString(), getActivity());
                chatMessage.setMessageItem(outgoingTextMessage);
            } else if (type.equalsIgnoreCase((MessageType.INCOMING_CARD_WITH_TEXT).toString())) {
                header = jsonMessage.get("header").getAsString();
                subheader = jsonMessage.get("subheader").getAsString();
                primary_button_name = jsonMessage.get("primary_button_name").getAsString();
                secondary_button_name = jsonMessage.get("secondary_button_name").getAsString();
                url = jsonMessage.get("imageurl").getAsString();
                secondary_button_action = jsonMessage.get("secondary_button_action").getAsString();
                primary_button_action = jsonMessage.get("primary_button_action").getAsString();

                cardViewModel = new CardViewModel(header, subheader, primary_button_name,
                        secondary_button_name, url, 0, "", primary_button_action, secondary_button_action);

                IncomingCardWithTextAndButtonMessage incomingCardWithTextAndButtonMessage = new IncomingCardWithTextAndButtonMessage(cardViewModel, getActivity(), this, this);
                chatMessage.setMessageItem(incomingCardWithTextAndButtonMessage);
            }else if (type.equalsIgnoreCase((MessageType.INCOMING_MEMBER_PROFILE_CARD).toString())) {
                header = jsonMessage.get("header").getAsString();
                subheader = jsonMessage.get("subheader").getAsString();
                primary_button_name = jsonMessage.get("primary_button_name").getAsString();
                secondary_button_name = jsonMessage.get("secondary_button_name").getAsString();
                url = jsonMessage.get("imageurl").getAsString();
                secondary_button_action = jsonMessage.get("secondary_button_action").getAsString();
                primary_button_action = jsonMessage.get("primary_button_action").getAsString();

                cardViewModel = new CardViewModel(header, subheader, primary_button_name,
                        secondary_button_name, url, 0, "", primary_button_action, secondary_button_action);

                IncomingProfileCompletionMessage incomingProfileCompletionMessage = new IncomingProfileCompletionMessage(cardViewModel, getActivity(),this);
                chatMessage.setMessageItem(incomingProfileCompletionMessage);
            } else if (type.equalsIgnoreCase((MessageType.INCOMING_HORIZONTAL_LIST).toString())) {
                String text = jsonMessage.get("message").getAsString();

                if (!text.isEmpty()) {
                    List<String> items = Arrays.asList(text.split("\\s*,\\s*"));
                    System.out.println("FragmentChat.createMessage items=" + items.toString());
                    IncomingHorizontalListMessage incomingHorizontalListMessage = new IncomingHorizontalListMessage(getActivity(), items, this);
                    chatMessage.setMessageItem(incomingHorizontalListMessage);
                } else {
                    IncomingTextMessage incomingTextMessage = new IncomingTextMessage("Wrong message from server", getActivity());
                    chatMessage.setMessageItem(incomingTextMessage);
                }
            } else if (type.equalsIgnoreCase((MessageType.INCOMING_VITAL_VALUES).toString())) {
                System.out.println("FragmentChat.createMessage=====yes" + type);
                System.out.println("FragmentChat.createMessage=====yes" + jsonMessage.get("header").getAsString());
                header = jsonMessage.get("header").getAsString();
                subheader = jsonMessage.get("subheader").getAsString();
                primary_button_name = jsonMessage.get("primary_button_name").getAsString();
                secondary_button_name = jsonMessage.get("secondary_button_name").getAsString();
                url = jsonMessage.get("imageurl").getAsString();
                secondary_button_action = jsonMessage.get("secondary_button_action").getAsString();
                primary_button_action = jsonMessage.get("primary_button_action").getAsString();

                cardViewModel = new CardViewModel(header, subheader, primary_button_name,
                        secondary_button_name, url, 0, "", primary_button_action, secondary_button_action);
                IncomeingCardVitalMessage incomeingCardVitalMessage = new IncomeingCardVitalMessage(cardViewModel, getActivity());
                chatMessage.setMessageItem(incomeingCardVitalMessage);
            } else if (type.equalsIgnoreCase((MessageType.INCOMING_CARD_WITH_LIST_VACCINATION).toString())) {

                JsonObject jsonObject = jsonMessage.getAsJsonObject("vaccinations");
                System.out.println("FragmentChat.createMessage==========" + jsonObject.get("name"));
                VaccaniationDataModel vaccaniationdatamodel = new Gson().fromJson(jsonObject.toString(),
                        VaccaniationDataModel.class);
                header = jsonMessage.get("header").getAsString();
                subheader = jsonMessage.get("subheader").getAsString();
                primary_button_name = jsonMessage.get("primary_button_name").getAsString();
                secondary_button_name = jsonMessage.get("secondary_button_name").getAsString();
                url = jsonMessage.get("imageurl").getAsString();
                secondary_button_action = jsonMessage.get("secondary_button_action").getAsString();
                primary_button_action = jsonMessage.get("primary_button_action").getAsString();


                cardViewModel = new CardViewModel(header, subheader, primary_button_name,
                        secondary_button_name, url, 0, "", primary_button_action, secondary_button_action);
                IncomingCardWithListVaccinationMessage incomingcardwithlistvaccinationmessage = new IncomingCardWithListVaccinationMessage(getActivity(),
                        vaccaniationdatamodel, this, cardViewModel, mCustomer.getDate_of_birth(), this, this);
                chatMessage.setMessageItem(incomingcardwithlistvaccinationmessage);
            } else if (type.equalsIgnoreCase((MessageType.INCOMING_CARD_WITH_WELLNESS_LIST).toString())) {

                JsonArray jsonArray = jsonMessage.getAsJsonArray("wellness_impact_steps");
                ArrayList<WellnessDataModel> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    WellnessDataModel wellnessdatamodel = new Gson().fromJson(jsonArray.get(i).toString(),
                            WellnessDataModel.class);
                    list.add(wellnessdatamodel);
                }
                header = jsonMessage.get("header").getAsString();
                subheader = jsonMessage.get("subheader").getAsString();
                primary_button_name = jsonMessage.get("primary_button_name").getAsString();
                secondary_button_name = jsonMessage.get("secondary_button_name").getAsString();
                url = jsonMessage.get("imageurl").getAsString();
                secondary_button_action = jsonMessage.get("secondary_button_action").getAsString();
                primary_button_action = jsonMessage.get("primary_button_action").getAsString();


                cardViewModel = new CardViewModel(header, subheader, primary_button_name,
                        secondary_button_name, url, 0, "", primary_button_action, secondary_button_action);
                IncomingCardWithListWellnessMessage incomingcardwithlistwellnessmessage = new IncomingCardWithListWellnessMessage(getActivity(),
                        list, this, cardViewModel);
                chatMessage.setMessageItem(incomingcardwithlistwellnessmessage);

            } else if (type.equalsIgnoreCase((MessageType.INCOMING_CARD_WITH_LIST_MEDICATION).toString())) {
                System.out.println("FragmentChat.createMessage meditation=======" + "medic");
                JsonArray jsonArray = jsonMessage.getAsJsonArray("medications");
                System.out.println("FragmentChat.createMessage meditation=======" + jsonArray.size());

                System.out.println("FragmentChat.createMessage INCOMING_CARD_WITH_LIST jsonArray=" + jsonArray.size());

                ArrayList<MedicationsChatModel> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    MedicationsChatModel medicationschatmodel = new Gson().fromJson(jsonArray.get(i).toString(),
                            MedicationsChatModel.class);
                    list.add(medicationschatmodel);
                }
                header = jsonMessage.get("header").getAsString();
                subheader = jsonMessage.get("subheader").getAsString();
                primary_button_name = jsonMessage.get("primary_button_name").getAsString();
                secondary_button_name = jsonMessage.get("secondary_button_name").getAsString();
                url = jsonMessage.get("imageurl").getAsString();
                secondary_button_action = jsonMessage.get("secondary_button_action").getAsString();
                primary_button_action = jsonMessage.get("primary_button_action").getAsString();

                cardViewModel = new CardViewModel(header, subheader, primary_button_name,
                        secondary_button_name, url, 0, "", primary_button_action, secondary_button_action);

                IncomingCardWithListMedicationsMessage incomingcardwithlistmedicationsmessage = new IncomingCardWithListMedicationsMessage(getActivity(),
                        list, this, cardViewModel, this, this);
                chatMessage.setMessageItem(incomingcardwithlistmedicationsmessage);

            } else if (type.equalsIgnoreCase((MessageType.INCOMING_CARD_LIST_WITH_GRAPH).toString())) {

                JsonArray jsonArray = jsonMessage.getAsJsonArray("lab_result_values");
                System.out.println("FragmentChat.createMessage INCOMING_CARD_WITH_LIST jsonArray=" + jsonArray.size());

                ArrayList<TrendsModelData> list = new ArrayList<>();
                List<String> xaxiesLablesDates = new ArrayList<String>();
                String[] mMonthsXValues;
                list.clear();
                xaxiesLablesDates.clear();

                for (int i = 0; i < jsonArray.size(); i++) {
                    TrendsModelData trendsmodeldata = new Gson().fromJson(jsonArray.get(i).toString(),
                            TrendsModelData.class);
                    list.add(trendsmodeldata);
                }

                if (list.size() > 0) {
                    for (int j = 0; j < list.size(); j++) {
                        xaxiesLablesDates.add(DateUtility.getconvertdate(list.get(j).getDate()).substring(0, 6));

                    }
                }

                mMonthsXValues = new String[xaxiesLablesDates.size()];

                mMonthsXValues = xaxiesLablesDates.toArray(mMonthsXValues);

                System.out.println("mychartValues=============" + Arrays.toString(mMonthsXValues));
                System.out.println("mychartValues=============" + Arrays.toString(xaxiesLablesDates.toArray()));

                header = jsonMessage.get("header").getAsString();
                subheader = jsonMessage.get("subheader").getAsString();
                primary_button_name = jsonMessage.get("primary_button_name").getAsString();
                secondary_button_name = jsonMessage.get("secondary_button_name").getAsString();
                url = jsonMessage.get("imageurl").getAsString();
                secondary_button_action = jsonMessage.get("secondary_button_action").getAsString();
                primary_button_action = jsonMessage.get("primary_button_action").getAsString();

                cardViewModel = new CardViewModel(header, subheader, primary_button_name,
                        secondary_button_name, url, 0, "", primary_button_action, secondary_button_action);

                IncomingCardGraphWithListMessage incomingcardgraphwithlistmessage = new IncomingCardGraphWithListMessage(getActivity(),
                        list, this, cardViewModel, mMonthsXValues);
                chatMessage.setMessageItem(incomingcardgraphwithlistmessage);

            } else if (type.equalsIgnoreCase((MessageType.INCOMING_CARD_WITH_LIST).toString())) {
                JsonArray jsonArray = jsonMessage.getAsJsonArray("lab_results");
                System.out.println("FragmentChat.createMessage INCOMING_CARD_WITH_LIST jsonArray=" + jsonArray.size());

                ArrayList<TestComponentData> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    TestComponentData testComponentData = new Gson().fromJson(jsonArray.get(i).toString(),
                            TestComponentData.class);
                    list.add(testComponentData);
                }

                header = jsonMessage.get("header").getAsString();
                subheader = jsonMessage.get("subheader").getAsString();
                primary_button_name = jsonMessage.get("primary_button_name").getAsString();
                secondary_button_name = jsonMessage.get("secondary_button_name").getAsString();
                url = jsonMessage.get("imageurl").getAsString();
                secondary_button_action = jsonMessage.get("secondary_button_action").getAsString();
                primary_button_action = jsonMessage.get("primary_button_action").getAsString();

                cardViewModel = new CardViewModel(header, subheader, primary_button_name,
                        secondary_button_name, url, 0, "", primary_button_action, secondary_button_action);

                IncomingCardWithListMessage incomingCardWithListMessage = new IncomingCardWithListMessage(getActivity(),
                        list, this, cardViewModel);
                chatMessage.setMessageItem(incomingCardWithListMessage);

            } else if (type.equalsIgnoreCase((MessageType.INCOMING_HORIZONTAL_DOCTOR_CARD_LIST).toString())) {
                JsonArray jsonArray = jsonMessage.getAsJsonArray("doctors");
                System.out.println("FragmentChat.createMessage INCOMING_HORIZONTAL_DOCTOR_CARD_LIST jsonArray=" + jsonArray.size());

                ArrayList<DoctorModelData> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    DoctorModelData doctorModelData = new Gson().fromJson(jsonArray.get(i).toString(),
                            DoctorModelData.class);
                    list.add(doctorModelData);
                }
                IncomingHorizontalDoctorCardListMessage incomingHorizontalDoctorCardListMessage = new IncomingHorizontalDoctorCardListMessage(getActivity(), list, this);

                chatMessage.setMessageItem(incomingHorizontalDoctorCardListMessage);
            } else if (type.equalsIgnoreCase((MessageType.INTRODUCTION_TEXT).toString())) {
                IntroductionMessage introductionMessage = new IntroductionMessage();
                chatMessage.setMessageItem(introductionMessage);
            } else if (type.equalsIgnoreCase((MessageType.INCOMING_PACKAGE).toString())) {
                JsonArray jsonArray = jsonMessage.getAsJsonArray("package");
                System.out.println("FragmentChat.createMessage INCOMING_PACKAGE jsonArray=" + jsonArray.size());

                List<PackageItem> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    PackageItem packageItem = new Gson().fromJson(jsonArray.get(i).toString(),
                            PackageItem.class);
                    list.add(packageItem);
                }

                System.out.println("FragmentChat.createMessage list=" + list.toString());
                IncomingHorizontalPackageCardListMessage incomingHorizontalPackageCardListMessage = new IncomingHorizontalPackageCardListMessage(list, getActivity());
                chatMessage.setMessageItem(incomingHorizontalPackageCardListMessage);
            } else if (type.equalsIgnoreCase((MessageType.INCOMING_CART_WITH_LIST).toString())) {
                JsonArray jsonArray = jsonMessage.getAsJsonArray("package");
                System.out.println("FragmentChat.createMessage INCOMING_PACKAGE jsonArray=" + jsonArray.size());

                List<PackageItem> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    PackageItem packageItem = new Gson().fromJson(jsonArray.get(i).toString(),
                            PackageItem.class);
                    list.add(packageItem);
                }

                System.out.println("FragmentChat.createMessage list=" + list.toString());
                IncomingCartCardWithListMessage incomingCartCardWithListMessage = new IncomingCartCardWithListMessage(getActivity(), list);
                chatMessage.setMessageItem(incomingCartCardWithListMessage);
            } else if (type.equalsIgnoreCase((MessageType.INCOMING_CARD_WITH_IMAGE).toString())) {
                JsonObject jsonObject = jsonMessage.get("doctor_profile").getAsJsonObject();
                header = jsonObject.get("name").getAsString();
                subheader = jsonObject.get("education").getAsString();
                primary_button_name = jsonObject.get("specialities").getAsString();
                secondary_button_name = jsonObject.get("language").getAsString();
                url = jsonObject.get("photo").getAsString();
                secondary_button_action = jsonObject.get("query_fee").getAsJsonObject().get("inr_val").getAsString();
                ;
                primary_button_action = jsonObject.get("consult_fee").getAsJsonObject().get("inr_val").getAsString();
                cardViewModel = new CardViewModel(header, subheader, primary_button_name, secondary_button_name, url, 0, "", primary_button_action, secondary_button_action);

                IncomingCardWithImageMessage incomingCardWithImageMessage = new IncomingCardWithImageMessage(cardViewModel, getActivity(), this);
                chatMessage.setMessageItem(incomingCardWithImageMessage);
            } else if (type.equalsIgnoreCase((MessageType.OUTGOING_DC_CARD).toString())) {

                Labs labs = new Gson().fromJson(jsonMessage.get("labs").getAsJsonObject(), Labs.class);
                OutgoingDcCardMessage outgoingDcCardMessage = new OutgoingDcCardMessage(labs, getActivity());
                outgoingDcCardMessage.setStatus(Config.MESSAGE_DEIVERED);
                chatMessage.setMessageItem(outgoingDcCardMessage);
            } else if (type.equalsIgnoreCase((MessageType.OUTGOING_MAP).toString())) {
                LatLng latLng = new LatLng(jsonMessage.get("lat").getAsDouble(), jsonMessage.get("long").getAsDouble());
                OutgoingMapMessage outgoingMapMessage = new OutgoingMapMessage(getActivity(), latLng);
                chatMessage.setMessageItem(outgoingMapMessage);
            } else if (type.equalsIgnoreCase((MessageType.OUTGOING_TEXT).toString())) {
                String text = "";
                try {
                    text = jsonMessage.get("message").getAsString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!text.isEmpty()) {
                    OutgoingTextMessage outgoingTextMessage = new OutgoingTextMessage(text, getActivity());
                    outgoingTextMessage.setStatus(Config.MESSAGE_DEIVERED);
                    chatMessage.setMessageItem(outgoingTextMessage);
                }
            } else if (type.equalsIgnoreCase((MessageType.INTRODUCTION_TEXT).toString())) {
                IntroductionMessage introductionMessage = new IntroductionMessage();
                chatMessage.setMessageItem(introductionMessage);
            } else if (type.equalsIgnoreCase((MessageType.OUTGOING_IMAGE).toString())) {
                String text = "";
                try {
                    text = jsonMessage.get("url").getAsString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!text.isEmpty()) {
                    byte[] decodedString = Base64.decode(text, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    OutgoingImageMessage outgoingImageMessage = new OutgoingImageMessage("", decodedByte);
                    outgoingImageMessage.showProgress(false);
                    outgoingImageMessage.setStatus(Config.MESSAGE_DEIVERED);
                    chatMessage.setMessageItem(outgoingImageMessage);
                }
            } else {
                Toast.makeText(getActivity(), "Payload error...", Toast.LENGTH_LONG).show();
                return null;
            }


        } catch (NullPointerException e) {
            IncomingTextMessage incomingTextMessage = new IncomingTextMessage("Something went wrong...", getActivity());
            chatMessage.setMessageItem(incomingTextMessage);

            Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_LONG).show();
        }

        return chatMessage;

    }


    InputFilter textFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; ++i) {
                if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789',?%&@#!* ]*").matcher(String.valueOf(source.charAt(i))).matches()) {
                    return "";
                }
            }

            return null;
        }
    };

    InputFilter numberFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; ++i) {
                if (!Pattern.compile("[1234567890]*").matcher(String.valueOf(source.charAt(i))).matches()) {
                    return "";
                }
            }

            return null;
        }
    };

    InputFilter emailFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; ++i) {
                if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789@.]*").matcher(String.valueOf(source.charAt(i))).matches()) {
                    return "";
                }
            }

            return null;
        }
    };

    public void startPayment(int price, String discription) {

        System.out.println("FragmentChat.startPayment========" + prefs.getPackageMrp() + price);
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "eKincare");
            options.put("description", discription);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://d1qb2nb5cznatu.cloudfront.net/startups/i/401619-8a6a4d1567e485ddb81b3ba782e16890-medium_jpg.jpg?buster=1409810898");
            options.put("currency", "INR");
            options.put("amount", price);

            JSONObject preFill = new JSONObject();
            preFill.put("email", customerManager.getCurrentCustomer().getEmail());
            preFill.put("contact", customerManager.getCurrentCustomer().getMobile_number());
            options.put("prefill", preFill);
            co.open(getActivity(), options);
        } catch (Exception e) {
            System.out.println("ErrorePayment============" + "Chat1");
            Toast.makeText(getActivity(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            // paymentFailuerServerCall(e.getMessage());
        }
    }


    public void onNotificationReceived(String title, String discription, String button1, String button1Action,String button2,String button2Action) {
        System.out.println("FragmentChat.onNotificationReceived discription=" + discription);
        final ChatMessage chatMessage = new ChatMessage();
        CardViewModel cardViewModel = new CardViewModel(title, discription, button1, button2, "", 0, "", button1Action, button2Action);

        IncomingCardWithTextAndButtonMessage incomingCardWithTextAndButtonMessage = new IncomingCardWithTextAndButtonMessage(cardViewModel, getActivity(), this, this);
        chatMessage.setMessageItem(incomingCardWithTextAndButtonMessage);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addMessageToQueue(chatMessage);
            }
        }, 3600);
    }


    public void showAlert(String message, Context mContext) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        mBuilder.setTitle("Enable location");
        mBuilder.setMessage(message);
        mBuilder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);

                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);

            }
        });
        mBuilder.create();
        mBuilder.setCancelable(true);
        mBuilder.show();
    }


    public void getLocationData() {
        if (NetworkCaller.isInternetOncheck(getActivity())) {
            String locationProviders = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (locationProviders == null || locationProviders.equals("")) {
                showAlert("Please Enable Location to use this app", getActivity());
            } else {
                try {
                    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    currentLocationLat = location.getLatitude();
                    currentLocationLang = location.getLongitude();
                    geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    prefs.setSaveLatitude(Double.toString(currentLocationLat));
                    prefs.setSavelongitude(Double.toString(currentLocationLang));

                    if (currentLocationLat > 0 || currentLocationLang > 0) {
                        System.out.println("length1============" + prefs.getLocationlatitude() +
                                currentLocationLat + "========" + currentLocationLang);

                        serverRequests.fetchCityName(location);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        } else {
            tempChatMessageQueue.add(tempChatMessageQueue.size(), createIncomingTextMessage("No internet"));
            if (!isQueueRunning) {
                showDelayed();
            }
        }


    }

    @Override
    public void onDoctorAutoString(String address, String packageId) {
        // System.out.println("FragmentChat.onDoctorAutoString======="+address+"======="+prefs.getPackageIDSearch());
        askQuerySpeciality = packageId;
        saveAndSendOutgoingMessage(new OutgoingTextMessage(address, getActivity()));
        enableRelativeLayoutSend(false, true, false, false);

        askQueryId = askQueryId + 1;
        startAskQueryToDoctor(askQueryId);
    }

    public void onDcSelected(Labs labs) {
        if (isBookAppointmentTrue) {
            selectedDclabs = labs;
            removeMessage(getRecyclerCount() - 1);

            OutgoingDcCardMessage outgoingDcCardMessage = new OutgoingDcCardMessage(labs, getActivity());
            outgoingDcCardMessage.setStatus(Config.MESSAGE_DEIVERED);

            saveAndSendOutgoingMessage(outgoingDcCardMessage);

            if (labs.getSlots().isJsonObject()) {
                Set<Entry<String, JsonElement>> ens = ((JsonObject) labs.getSlots()).entrySet();
                if (ens != null) {
                    for (Entry<String, JsonElement> en : ens) {
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = null;
                        try {
                            date = (Date) formatter.parse(en.getKey());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        DateFormat destDf = new SimpleDateFormat("EEE, d MMM yyyy");

                        selectedDcPackageDateList.add(destDf.format(date));

                        System.out.println(en.getKey() + " : ");
                        System.out.println(en.getValue() + " : ");
                    }
                }
            }

            try {
                if (labs.getHome_collection_enabled().equalsIgnoreCase("true") && isRequestPackageHomeCollectionEnabled()) {
                    System.out.println("FragmentChat.onDcSelected Home Collection enealbed");

                    SparseArray<String> stringSparseArray = new SparseArray<>();

                    for (int i = 0; i < requestPackages.size(); i++) {
                        if (requestPackages.get(i).getHome_collection_prices().isJsonObject()) {
                            Set<Entry<String, JsonElement>> ens = ((JsonObject) requestPackages.get(i).getHome_collection_prices()).entrySet();

                            if (ens != null) {
                                for (Entry<String, JsonElement> en : ens) {
                                    System.out.println("FragmentChat.onDcSelected Key=" + en.getKey().toString() + " labs.getEnterprise_id()=" + labs.getEnterprise_id());
                                    if (en.getKey().toString().equalsIgnoreCase(labs.getEnterprise_id())) {
                                        dc_collection_price = dc_collection_price + en.getValue().getAsInt();
                                        break;
                                    }

                                    stringSparseArray.append(Integer.parseInt(en.getKey().toString()), en.getValue().toString());

                                    System.out.println(en.getKey() + " : ");
                                    System.out.println(en.getValue() + " : ");
                                }
                                System.out.println("stringSparseArray : " + stringSparseArray.toString());
                            }
                        }
                    }


                    saveAndSendIncomingMessage(new IncomingTextMessage("Would you like the lab to collect samples at your home?", getActivity()));

                    if (dc_collection_price == 0) {
                        saveAndSendIncomingMessage(new IncomingTextMessage("Its free " + getEmojiByUnicode(0x1F609), getActivity()));
                    } else {
                        saveAndSendIncomingMessage(new IncomingTextMessage("Home collection would include delivery charges of " + getResources().getString(R.string.Rs) + dc_collection_price, getActivity()));
                    }

                    ChatMessage chatMessageOptions = new ChatMessage();
                    ArrayList<String> arrayListOptions = new ArrayList<>();
                    arrayListOptions.add("No");
                    arrayListOptions.add("Yeah, sure");
                    IncomingHorizontalListMessage incomingHorizontalOptionsListMessage = new
                            IncomingHorizontalListMessage(getActivity(), arrayListOptions, this);
                    chatMessageOptions.setMessageItem(incomingHorizontalOptionsListMessage);
                    addMessageToQueue(chatMessageOptions);
                } else {
                    bookAppointmentId = 1 + bookAppointmentId;
                    homeCollectionEnable="false";
                    startBookPackage(bookAppointmentId);
                }

            } catch (NullPointerException e) {
                bookAppointmentId = 1 + bookAppointmentId;
                startBookPackage(bookAppointmentId);
            }
        }
    }

    private boolean isRequestPackageHomeCollectionEnabled() {
        System.out.println("FragmentChat.isRequestPackageHomeCollectionEnabled");
        boolean flag = true;
        for (int i = 0; i < requestPackages.size(); i++) {
            System.out.println("FragmentChat.isRequestPackageHomeCollectionEnabled i=" + i + " Home_collection_enable=" + requestPackages.get(i).getHome_collection_enable());
            if (requestPackages.get(i).getHome_collection_enable().equalsIgnoreCase("false")) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    @Override
    public void onDcChangeLocation() {
        {
            System.out.println("FragmentChat.onDCPackagesResponse dcLabs=" + "NO ");
            removeMessage(getRecyclerCount() - 1);
            bookAppointmentId = 0;
            ChatMessage chatMessage = createIncomingTextMessage(getResources().getString(R.string.book_no_select_diagnostic_center));
            addMessageToQueue(chatMessage);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
            }, 4400);

        }
    }


    public void paymentFailuerServerCall(String failed) {
        isPaymentFailed = true;

        if (isaskQuaryDoctorTrue) {
            askQueryId = 7;
        }


        saveAndSendIncomingMessage(new IncomingTextMessage("Payment failed! Would you like to retry?", getActivity()));

        ArrayList<String> arrayListPayment = new ArrayList<>();
        arrayListPayment.add("Cancel");
        arrayListPayment.add("Yes");

        IncomingHorizontalListMessage incomingHorizontalPayment = new IncomingHorizontalListMessage(getActivity(), arrayListPayment, this);

        ChatMessage chatMessagePayment = new ChatMessage();
        chatMessagePayment.setMessageItem(incomingHorizontalPayment);
        addMessageToQueue(chatMessagePayment);


        /*enableRelativeLayoutSend(true,true,true,false);
        //removeMessage(getRecyclerCount()-1);
        if(isBookAppointmentTrue){
            ChatMessage chatMessage = createIncomingTextMessage(failed);
            addMessageToQueue(chatMessage);
            isBookAppointmentTrue = false;
            bookAppointmentId = -1;
            bookPackageServerTime = "";
            bookPackageServerDate = "";
            packageDate ="";
            packageIds = "";
            packageAppointmentTime = "";
            packageTime = "";
            packageDay = 0;
            packageYear = 0;
            packageMonth = 0;
            packageHour = 0;
            packageMin = 0;
            packagePrice = 0;
            packageTotalPrice = 0;
            walletMoney = 0;

        }else if(isaskQuaryDoctorTrue){
            removeMessage(getRecyclerCount()-1);
            askQueryId=-1;
            isaskQuaryDoctorTrue=false;
            askQueryDate="";
            askQueryMode = "";
            askQueryPackagePrice = 0;
            askQueryTotalPackagePrice = 0;
            walletMoney = 0;
            askQueryQuestion="";
            askQuerySpeciality = "";
            askQueryTime = "";
            ChatMessage chatMessage = createIncomingTextMessage(failed);
            addMessageToQueue(chatMessage);

        }*/

    }

    public void addFamilyTextNotification(String text) {
        System.out.println("FragmentChat.addFamilyTextNotification======" + text);
        //sendDataLogic(text);
        editTextMessage.setText(text);
    }

    public void paymentSuccessServerCall(String razorpayPaymentID) {
        System.out.println("FragmentChat.paymentSuccessServerCall razorpayPaymentID=" + razorpayPaymentID);
        if (isaskQuaryDoctorTrue) {
            removeMessage(getRecyclerCount() - 1);

            generateIncomingMessageAnimation();

            recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

            System.out.println("MainActivity.onPaymentSuccess=======" + askQueryMode);
            if (askQueryMode.equalsIgnoreCase("text")) {
                JSONObject object = new JSONObject();
                try {
                    object.put("package_id", askQuerySpeciality);
                    object.put("query", askQueryQuestion);
                    object.put("profile_url", askShareProfile);
                    object.put("amount", askQueryPackagePrice * 100);
                    object.put("total_amount", askQueryTotalPackagePrice * 100);
                    if (!razorpayPaymentID.isEmpty()) {
                        object.put("razorpay_payment_id", razorpayPaymentID);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                serverRequests.sendQueryRequest(object, TagValues.POST_QUERY);
            } else if (askQueryMode.equalsIgnoreCase("phone") || askQueryMode.equalsIgnoreCase("video") || askQueryMode.equalsIgnoreCase("audio")) {
                String time = "";
                if (askQueryTime.equalsIgnoreCase("08:00 AM") || askQueryTime.equalsIgnoreCase("09:00 AM")) {
                    time = "1";
                } else if (askQueryTime.equalsIgnoreCase("10:00 AM") || askQueryTime.equalsIgnoreCase("11:00 AM")) {
                    time = "2";
                } else if (askQueryTime.equalsIgnoreCase("12:00 AM") || askQueryTime.equalsIgnoreCase("01:00 PM")) {
                    time = "3";
                } else if (askQueryTime.equalsIgnoreCase("02:00 PM") || askQueryTime.equalsIgnoreCase("03:00 PM")) {
                    time = "4";
                } else if (askQueryTime.equalsIgnoreCase("04:00 PM") || askQueryTime.equalsIgnoreCase("05:00 PM")) {
                    time = "5";
                } else if (askQueryTime.equalsIgnoreCase("06:00 PM") || askQueryTime.equalsIgnoreCase("07:00 PM") || askQueryTime.equalsIgnoreCase("08:00 PM")) {
                    time = "6";
                } else if (askQueryTime.equalsIgnoreCase("08:00AM-10:00AM")) {
                    time = "1";
                } else if (askQueryTime.equalsIgnoreCase("10:00AM-12:00PM")) {
                    time = "2";
                } else if (askQueryTime.equalsIgnoreCase("12:00PM-02:00PM")) {
                    time = "3";
                } else if (askQueryTime.equalsIgnoreCase("02:00PM-04:00PM")) {
                    time = "4";
                } else if (askQueryTime.equalsIgnoreCase("04:00PM-06:00PM")) {
                    time = "5";
                } else if (askQueryTime.equalsIgnoreCase("06:00PM-08:00PM")) {
                    time = "6";
                }

                JSONObject object = new JSONObject();
                try {
                    object.put("package_id", askQuerySpeciality);
                    object.put("doctor_id", prefs.getPREF_DOCTORID());
                    object.put("query", askQueryQuestion);
                    if (askQueryMode.equalsIgnoreCase("phone") || askQueryMode.equalsIgnoreCase("audio")) {
                        object.put("consultation_type", "phone");
                    } else if (askQueryMode.equalsIgnoreCase("video")) {
                        object.put("consultation_type", "video");
                    }
                    object.put("consult_date", askQueryDate);
                    object.put("consult_time", time);
                    object.put("mobile_number", customerManager.getCurrentCustomer().getMobile_number());
                    object.put("profile_url", askShareProfile);
                    object.put("amount", askQueryPackagePrice * 100);
                    object.put("total_amount", askQueryTotalPackagePrice * 100);
                    if (!razorpayPaymentID.isEmpty()) {
                        object.put("razorpay_payment_id", razorpayPaymentID);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                System.out.println("FragmentChat.paymentSuccessServerCall==========" + object.toString());
                serverRequests.sendQueryRequest(object, TagValues.POST_QUERYDOCTOR);
            }
        } else if (isBookAppointmentTrue) {
            System.out.println("FragmentChat.paymentSuccessServerCall isBookAppointmentTrue");
            System.out.println(" sponsorIds====="+ sponsorIds);
            generateIncomingMessageAnimation();

            recyclerViewChat.smoothScrollToPosition(getRecyclerCount());

            JSONObject object = new JSONObject();
            try {
                object.put("amount", packagePrice * 100);
                if (!razorpayPaymentID.isEmpty()) {
                    object.put("razorpay_payment_id", razorpayPaymentID);
                }
                object.put("customer_id", bookPackageCustomerId);
                if(sponsorIds!=null){
                    object.put("sponsor_id", sponsorIds);
                }
                if(homeCollectionEnable.equalsIgnoreCase("")){
                    object.put("home_collection_selected", "false");
                }else{
                    object.put("home_collection_selected", homeCollectionEnable);
                    object.put("house_number", pacakageHouseNumber);
                    object.put("line1", packageAddressLine1);
                    object.put("line2", packageAddressLine2);
                    object.put("city", packageCity);
                    object.put("state", packageState);
                    object.put("country", packageCounty);
                    object.put("zipcode", packagePostalCode);
                    object.put("latitude", packageLatitude);
                    object.put("longitude", pacakageLangtude);

                }

                object.put("txnid", razorpayPaymentID);
                object.put("package_ids", packageIds);
                object.put("provider_id", selectedDclabs.getProvider_id());
                object.put("appointment_time", packageAppointmentTime);
                object.put("appointment_date", packageAppointmentTime);
                object.put("total_amount", packageTotalPrice * 100);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            serverRequests.sendBookRequest(object, TagValues.POST_BOOK_APPOINTMENT,strFamilyMemberKey);
        }
    }

    public void insertInDatabase() {
        System.out.println("FragmentChat.insertInDatabase");
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();

                String inputString = gson.toJson(chatMessageList);

                System.out.println("FragmentChat.insertInDatabase inputString= " + inputString);

                /*Type type = new TypeToken<ArrayList<ChatMessage>>() {}.getType();



                ArrayList<ChatMessage> finalOutputString = gson.fromJson(inputString, type);
                System.out.println("FragmentChat.insertInDatabase finalOutputString="+finalOutputString.size() + " " +
                        finalOutputString.get(0).getMessageItem().getMessageSource());*/
            }
        });


    }

    public void playIncomingSound() {
        System.out.println("FragmentChat.playIncomingSound");
        try {
            final MediaPlayer[] mediaPlayer = {MediaPlayer.create(getActivity(), R.raw.incoming)};
            mediaPlayer[0].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    mp.release();
                    mediaPlayer[0] = null;
                }

            });
            mediaPlayer[0].start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void playOutgoingSound() {
        System.out.println("FragmentChat.playOutgoingSound");
        try {
            final MediaPlayer[] mediaPlayer = {MediaPlayer.create(getActivity(), R.raw.outgoing)};
            mediaPlayer[0].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    mp.release();
                    mediaPlayer[0] = null;
                }

            });
            mediaPlayer[0].start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void expand(Fragment fragment, ArrayList<String> list1, ArrayList<String> list2, boolean isSingleList, String unit1, String unit2) {
        System.out.println("FragmentChat.expand");
        editTextMessage.setHint("Choose from below...");
        expandableLayoutContainer.setVisibility(View.VISIBLE);

        final Fragment f = fragment;
        try {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            if (isSingleList) {
                args.putStringArrayList("list", list1);
                args.putString("unit", unit1);
            } else {
                args.putStringArrayList("list1", list1);
                args.putStringArrayList("list2", list2);
                args.putString("unit1", unit1);
                args.putString("unit2", unit2);
            }
            f.setArguments(args);
            fragmentTransaction.replace(R.id.expandable, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAnimator.start();
        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
        if (Build.VERSION.SDK_INT >= 21) {
            relativeLayoutSend.setElevation(03.0f);
        } else {

        }


        //expandableLayoutContainer.startAnimation(slide_up);
    }

    private void expand(Fragment fragment) {
        System.out.println("FragmentChat.expand");
        editTextMessage.setHint("Choose from below...");
        expandableLayoutContainer.setVisibility(View.VISIBLE);

        final Fragment f = fragment;
        try {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.expandable, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAnimator.start();
        recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
        if (Build.VERSION.SDK_INT >= 21) {
            relativeLayoutSend.setElevation(03.0f);
        } else {

        }
    }

    private void setupFragment(Fragment fragment) {
        try {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.expandable, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void collapse() {
        System.out.println("FragmentChat.collapse");
        //expandableLayoutContainer.startAnimation(slide_down);

        int finalHeight = expandableLayoutContainer.getHeight();

        ValueAnimator valueAnimator = slideAnimator(finalHeight, 0);

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.expandable, new FragmentProgressView()).commit();
                expandableLayoutContainer.setVisibility(View.GONE);
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
            }

            @Override
            public void onAnimationStart(Animator animator) {
                recyclerViewChat.smoothScrollToPosition(getRecyclerCount());
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        valueAnimator.start();
    }

    private ValueAnimator slideAnimator(int start, int end) {
        System.out.println("FragmentChat.slideAnimator");
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                System.out.println("FragmentChat.onAnimationUpdate");
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = expandableLayoutContainer.getLayoutParams();
                layoutParams.height = value;
                expandableLayoutContainer.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    @Override
    public void onSchemaItemClick(String customerId, String tansitionScreen) {
        System.out.println("FragmentChat.onSchemaItemClick customerId=" + customerId + " tansitionScreen=" + tansitionScreen);
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
            mCustomer = mProfileManager.getLoggedinCustomer();
            if (!familyMembers.contains(mCustomer))
                familyMembers.add(0, mCustomer);
        }

        for (int i = 0; i < familyMembers.size(); i++) {
            customerManager.setCurrentFamilyMember(mProfileManager.getLoggedinCustomer());
            if (familyMembers.get(i).getIdentification_token().equalsIgnoreCase(customerId)) {
                customerManager.setCurrentFamilyMember(familyMembers.get(i));
                break;
            }
        }

        int pos = 0;

        if (tansitionScreen.equalsIgnoreCase("documents")) {
            pos = 3;
        } else if (tansitionScreen.equalsIgnoreCase("timeline")) {
            pos = 1;
        }
        if (tansitionScreen.equalsIgnoreCase("medications")) {
            pos = 2;
        }
        if (tansitionScreen.equalsIgnoreCase("vaccinations")) {
            pos = 4;
        }
        if (tansitionScreen.equalsIgnoreCase("profile")) {
            pos = 0;
        }
        if (tansitionScreen.equalsIgnoreCase("allergies")) {
            pos = 5;
        }
        if (tansitionScreen.equalsIgnoreCase("insights")) {
            pos = 13;
        }

        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            final MainActivity myactivity = (MainActivity) activity;

            final int finalPos = pos;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    myactivity.goToScreen(finalPos);
                }
            }, 300);
        }
    }

    public String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    public void removeGoogleFitMessage() {
        try {
            if (googleFitMessagePosition > 0) {
                removeMessage(googleFitMessagePosition);
                googleFitMessagePosition = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCameraError(String cameraFailure) {
        saveAndSendOutgoingMessage(new OutgoingTextMessage(cameraFailure, getActivity()));
    }

    public boolean isAnyProcessActive() {
        if (isAddFamilyTrue || isaskQuaryDoctorTrue || isHRATrue || isBookAppointmentTrue) {
            return true;
        }
        return false;
    }

    @Override
    public void onConsultDoctorSelected(String text) {
        if (isaskQuaryDoctorTrue) {
            switch (askQueryId) {
                case 2:
                    saveAndSendOutgoingMessage(new OutgoingTextMessage(text, getActivity()));
                    askQueryId = askQueryId + 1;
                    startAskQueryToDoctor(askQueryId);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDoctorProfileSelected(String text) {
        serverRequests.getDoctorProfile(text);
    }

    @Override
    public void onBottonFragmentItemClick(String text) {
        System.out.println("Date test=" + bookAppointmentId+"case pop");

        isBottomSheetFragmentClicked = true;
        System.out.println("FragmentChat.onBottonFragmentItemClick text=" + text);
        if (isAddFamilyTrue) {
            createAddFamilyMemberMessages(text);
        } else if (isHRATrue) {
            createHraOutgoingMessages(text);
        } else if (isaskQuaryDoctorTrue) {
            System.out.println("FragmentChat.onBottonFragmentItemClick isaskQuaryDoctorTrue=" + isaskQuaryDoctorTrue);
            createAskQueryDoctorMessages(text);
        } else if (isBookAppointmentTrue) {
            System.out.println("Date test=" + bookAppointmentId+"ppppp"+"test");

            System.out.println("onBottonFragmentItemClick isBookAppointmentTrue============" +"CLicking date");
            createBookPackageOutgoingMessages(text);
        }else if(isAgeGenderFlowNeed){
            System.out.println("FragmentChat.onBottonFragmentItemClick isAgeGenderFlowNeed text="+text);
            if (isBottomSheetFragmentClicked) {
                doneString = text;
            }
        }
    }

    private class GetChatThreadThread implements Runnable {
        @Override
        public void run() {
            getPreviousChatData();
        }
    }

    boolean isAppStart = true;
    boolean isPaginationEnd = false;
    int pageCount = 0;
    int totalDisplayItem = 100;
    boolean isLoading = false;
    ArrayList<ChatDatabaseModel> chatDatabaseModelList;


    private void getPreviousChatData() {
        if (isAppStart) {
            chatDatabaseModelList = dbHandler.getChatData();
            System.out.println("FragmentChat.getPreviousChatData chatDatabaseModelList=" + chatDatabaseModelList.size());
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (pageCount > 0) {
                    SpinnerItem spinnerItem = new SpinnerItem();
                    spinnerItem.showProgress(false);
                    outgoingChatMessage.setMessageItem(spinnerItem);
                    mRecyclerAdapter.update(0, outgoingChatMessage);
                }
            }
        });

        isLoading = false;

        if (chatDatabaseModelList.size() > 0) {
            int start;

            int end = chatDatabaseModelList.size() - (pageCount * totalDisplayItem);

            if (end < 0) {
                System.out.println("FragmentChat.getPreviousChatData end is <0 + end=" + end);
                isPaginationEnd = true;
                return;
            }

            if (end < totalDisplayItem) {
                start = 0;
            } else {
                start = end - totalDisplayItem;
            }

            System.out.println("FragmentChat.getPreviousChatData start=" + start + " end=" + end);

            for (int i = start; i < end; i++) {
                //System.out.println("FragmentChat.getPreviousChatData chatDatabaseModelList.get("+i+")"+chatDatabaseModelList.get(i).getChatJson());
                try {
                    JsonParser parser = new JsonParser();
                    final JsonObject o = parser.parse(chatDatabaseModelList.get(i).getChatJson()).getAsJsonObject();

                    ChatMessage chatMessage = createMessage(o, false);

                    if (chatMessage != null) {
                        addMessageInUiThread(i - start, chatMessage);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (isAppStart) {
            isAppStart = false;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AddNewMessageTask(chatMessageList, mRecyclerAdapter, recyclerViewChat, chatDatabaseModelList.size()).execute();

                    serverRequests.getWalletDataFirst();
                    serverRequests.notificationCountRequest();

                    setUpLoadingView();

                    botIntro(false);
                }
            });
        }
    }

    private void addMessageInUiThread(final int i, final ChatMessage chatMessage) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (chatMessage != null) {
                        mRecyclerAdapter.add(i, chatMessage);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
