package com.swap.views.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.swap.R;
import com.swap.utilities.AmazonClientManager;
import com.swap.utilities.AppHelper;
import com.swap.utilities.EmojiExcludeFilter;
import com.swap.utilities.GPSTracker;
import com.swap.utilities.Preferences;
import com.swap.utilities.Utils;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback {

    private TextView textViewForgotPassword;
    TextView textViewOr;
    TextView textViewAgreePrivacyPolicy;
    Button buttonSignIn;
    Button buttonSignUp;
    EditText editTextUsername;
    EditText editTextPassword;
    private AlertDialog userDialog;
    private ProgressDialog waitDialog;
    String userName;
    String password;
    private MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation;
    private final int PERMISSION_REQUEST = 1;
    GoogleApiClient mGoogleApiClient;
    LocationRequest locationRequest;
    public static int REQUEST_CHECK_SETTINGS = 100;
    GPSTracker gpsTracker;
    String Tag = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Preferences.saveBooleanValue(LoginActivity.this, Preferences.IsConnectSocialMediaFromSplash,false);
        findViewById();
        AskPermission();

        // CognitoDevice thisDevice = AppHelper.getPool().getUser("priya").thisDevice();
        // AppHelper.setThisDevice(thisDevice);
    }


    // To remember the device
    private void AskPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
                }
            } else {
                gpsTracker = new GPSTracker(LoginActivity.this);
                if (!gpsTracker.canGetLocation()) {
                    initGoogleApiClient();
                }
            }
        } else {
            gpsTracker = new GPSTracker(LoginActivity.this);
            if (!gpsTracker.canGetLocation()) {
                initGoogleApiClient();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    gpsTracker = new GPSTracker(LoginActivity.this);
                    if (!gpsTracker.canGetLocation()) {
                        initGoogleApiClient();
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(LoginActivity.this, "Permissions denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void initGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this).addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
    }

    private void findViewById() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("FromForgotPwd")) {
                boolean FromForgotPwd = extras.getBoolean("FromForgotPwd");
                showDialogMessage("Password successfully changed!", "");
            } else if (extras.containsKey("EXIT")) {
                // boolean Exit = extras.getBoolean("Exit");
                if (extras.getBoolean("EXIT")) {
                    finish();
                }
            }
        }
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(this);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextUsername.setFilters(new InputFilter[]{new EmojiExcludeFilter(this)});
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword.setFilters(new InputFilter[]{new EmojiExcludeFilter(this)});
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(this);
        textViewForgotPassword = (TextView) findViewById(R.id.textViewForgotPassword);
        textViewForgotPassword.setOnClickListener(this);
        textViewOr = (TextView) findViewById(R.id.textViewOr);
        textViewAgreePrivacyPolicy = (TextView) findViewById(R.id.textViewAgreePrivacyPolicy);

        setFontOnViews();
    }

    private void setFontOnViews() {
        buttonSignIn.setTypeface(Utils.setFont(LoginActivity.this, "lantinghei.ttf"));
        editTextUsername.setTypeface(Utils.setFont(LoginActivity.this, "lantinghei.ttf"));
        editTextPassword.setTypeface(Utils.setFont(LoginActivity.this, "lantinghei.ttf"));
        buttonSignUp.setTypeface(Utils.setFont(LoginActivity.this, "lantinghei.ttf"));
        textViewForgotPassword.setTypeface(Utils.setFont(LoginActivity.this, "lantinghei.ttf"));
        textViewOr.setTypeface(Utils.setFont(LoginActivity.this, "lantinghei.ttf"));
        textViewAgreePrivacyPolicy.setTypeface(Utils.setFont(LoginActivity.this, "lantinghei.ttf"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignIn:
                Utils.hideSoftKeyboard(LoginActivity.this);
                validations();
                break;
            case R.id.buttonSignUp:

                Utils.hideSoftKeyboard(LoginActivity.this);
                Intent i = new Intent(LoginActivity.this, SignUpNameActivity.class);
                startActivity(i);
                break;
            case R.id.textViewForgotPassword:
                Utils.hideSoftKeyboard(LoginActivity.this);
                Intent ii = new Intent(this, ForgotPasswordActivity.class);
                startActivity(ii);
                break;
        }
    }

    private void validations() {
        if (Utils.isNetworkConnected(LoginActivity.this)) {
            userName = editTextUsername.getText().toString().trim();
            password = editTextPassword.getText().toString().trim();

            if (!userName.isEmpty() && !password.isEmpty()) {
                AppHelper.setUser(userName);
                showWaitDialog("Signing in...");
                AppHelper.init(getApplicationContext());
                AppHelper.getPool().getUser(userName).getSessionInBackground(authenticationHandler);
            } else {
                if (userName.isEmpty()) {
                    editTextUsername.requestFocus();
                    editTextUsername.setError(getString(R.string.usernameShouldBeNotEmpty));
                } else if (password.isEmpty()) {
                    editTextPassword.requestFocus();
                    editTextPassword.setError(getString(R.string.passwordShouldNotBeEmpty));
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
        }
    }




    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
        if (username != null) {
            this.userName = username;
            AppHelper.setUser(username);
        }
        if (this.password == null) {
            editTextUsername.setText(username);
            password = editTextPassword.getText().toString();
        }
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(this.userName, password, null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }

    private void showWaitDialog(String message) {
        closeWaitDialog();
        waitDialog = new ProgressDialog(this);
        waitDialog.setTitle(message);
        waitDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {

            } else {
                Toast.makeText(LoginActivity.this, "GPS is not enabled", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                } catch (Exception e) {
                    //
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private void closeWaitDialog() {
        try {
            if (waitDialog.isShowing()) {
                waitDialog.dismiss();
            }
        } catch (Exception e) {
            //
        }
    }

    private void launchUser() {
        Preferences.save(this, Preferences.USERNAME, userName);
        Preferences.saveBooleanValue(this, Preferences.IsLoginOrSignup, true);
        AmazonClientManager.getInstance(this).ddb();
        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
        i.putExtra("FromLogin", true);
        startActivity(i);
        finish();
    }

    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            Log.d(Tag,"Login onSuccess"+"Auth Success");
            AppHelper.setCurrSession(cognitoUserSession);
            AppHelper.newDevice(device);
            closeWaitDialog();
            launchUser();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
            Log.d(Tag,"Login AuthenticationDetails"+"Auth Details");
            Locale.setDefault(Locale.US);
            getUserAuthentication(authenticationContinuation, username);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
            Log.d(Tag,"Login MFACode"+"MFACode");
            //closeWaitDialog();
            //mfaAuth(multiFactorAuthenticationContinuation);
        }

        @Override
        public void onFailure(Exception e) {
            closeWaitDialog();
            Log.d(Tag,"Login onFailure"+"onFailure");
            showDialogMessage("Sign-in failed", AppHelper.formatException(e));
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            Log.d(Tag,"Login Challenge"+"Challenge");
            /**
             * For Custom authentication challenge, implement your logic to present challenge to the
             * user and pass the user's responses to the continuation.
             */
            if ("NEW_PASSWORD_REQUIRED".equals(continuation.getChallengeName())) {
                // This is the first sign-in attempt for an admin created user

                Log.e("tag", "ChallengeREQUIRED");

            }
        }
    };



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build()
        );

        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Result result) {
        final Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                // NO need to show the dialog;
                //Intent i = new Intent(getActivity(), LocationByGoogleApiClient.class);
                //getActivity().startService(i);


                //checkCountryByLatLng();

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog

                try {
                    status.startResolutionForResult(LoginActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }
}
