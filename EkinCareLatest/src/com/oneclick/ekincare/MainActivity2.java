package com.oneclick.ekincare;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ekincare.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Ajay on 03-02-2017.
 */

public class MainActivity2 extends AppCompatActivity {
    private PreferenceHelper prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dummy_transparent_layout);
        prefs = new PreferenceHelper(this);
        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }
        setUpGoogleFitCard();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("MainActivity2.onActivityResult 1");
        if (requestCode == REQUEST_OAUTH) {
            System.out.println("MainActivity2.onActivityResult REQUEST_OAUTH");
            authInProgress = false;
            if (resultCode == RESULT_OK) {
                System.out.println("MainActivity2.onActivityResult RESULT_OK");
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
                    System.out.println("MainActivity2.onActivityResult");
                    mGoogleApiClient.connect();
                    System.out.println("MainActivity2.onActivityResult 3");
                }
            }else if( resultCode == RESULT_CANCELED ) {
                Log.e( "GoogleFit", "RESULT_CANCELED" );
            }
        } else {
            Log.e("GoogleFit", "requestCode NOT request_oauth");
        }
    }
    private GoogleApiClient mGoogleApiClient;
    private int googleApiClientId = 0;
    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();

    }
;
    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(MainActivity2.this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(MainActivity2.this);
            mGoogleApiClient.disconnect();
        }
    }


    public void setUpGoogleFitCard()
    {
        System.out.println("MainActivity.setUpGoogleFitCard");

        if (NetworkCaller.isInternetOncheck(MainActivity2.this)) {
            try {
                mGoogleApiClient = new GoogleApiClient.Builder(MainActivity2.this)
                        .addApi(Fitness.HISTORY_API)
                        .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                        .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(@Nullable Bundle bundle) {
                                System.out.println("MainActivity2.onConnected");
                                prefs.setIsGoogleFitConnected(true);
                                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                intent.putExtra("GOOGLE_FIT","GOOGLE_FIT");
                                intent.putExtra("isGFitConnected",true);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                System.out.println("MainActivity2.onConnectionSuspended");
                                prefs.setIsGoogleFitConnected(false);
                                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                intent.putExtra("GOOGLE_FIT","GOOGLE_FIT");
                                intent.putExtra("isGFitConnected",false);
                                startActivity(intent);

                                finish();
                            }
                        })
                        .enableAutoManage(MainActivity2.this, googleApiClientId, new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                System.out.println("MainActivity2.onConnectionFailed");
                                prefs.setIsGoogleFitConnected(false);
                                String text = getString(R.string.google_fit_not_connected_body_text) + " " + getString(R.string.google_fit_image_body_text);

                                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                intent.putExtra("GOOGLE_FIT","GOOGLE_FIT");
                                intent.putExtra("isGFitConnected",false);
                                startActivity(intent);
                                finish();
                                // The failure has a resolution. Resolve it.
                                // Called typically when the app is not yet authorized, and an
                                // authorization dialog is displayed to the user.
                                if (!authInProgress) {
                                    try {
                                        authInProgress = true;
                                        connectionResult.startResolutionForResult(MainActivity2.this, REQUEST_OAUTH);
                                    } catch (IntentSender.SendIntentException e) {
                                        System.out.println("MainActivity2 Exception while starting resolution activity" + e);
                                    }
                                }
                            }
                        })
                        .build();

            } catch (IllegalStateException e) {
                e.printStackTrace();
                System.out.println("MainActivity2 mGoogleApiClient==========" + "wrong");
            }
        }
    }
}

