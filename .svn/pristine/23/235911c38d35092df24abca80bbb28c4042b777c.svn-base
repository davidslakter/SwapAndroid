package com.swap.views.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.services.cognitoidentityprovider.model.GetUserResult;
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
import com.rilixtech.CountryCodePicker;
import com.swap.R;
import com.swap.utilities.AppHelper;
import com.swap.utilities.GPSTracker;
import com.swap.utilities.Utils;

import java.util.Locale;

public class SignUpPhoneNumberActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback {
    Button buttonBack;
    Button buttonNext;
    EditText editTextPhoneNumber;
    CountryCodePicker countryCodePicker;
    TextView textViewSingUp;
    String firstName;
    String lastName;
    String DOB;
    String email;
    String userName;
    String password;
    private AlertDialog userDialog;
    private ProgressDialog waitDialog;
    GetUserResult getUserResult;
    GoogleApiClient mGoogleApiClient;
    LocationRequest locationRequest;
    public static int REQUEST_CHECK_SETTINGS = 100;
    GPSTracker gpsTracker;
    private final int PERMISSION_REQUEST = 1;
    String phoneNumber;
    String Tag = "SignUpPhone";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_phone_number);

        findViewById();
        AskPermission();
    }

    private void AskPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
                }
            } else {
                gpsTracker = new GPSTracker(SignUpPhoneNumberActivity.this);
                if (!gpsTracker.canGetLocation()) {
                    initGoogleApiClient();
                }
            }
        } else {
            gpsTracker = new GPSTracker(SignUpPhoneNumberActivity.this);
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
                    gpsTracker = new GPSTracker(SignUpPhoneNumberActivity.this);
                    if (!gpsTracker.canGetLocation()) {
                        initGoogleApiClient();
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(SignUpPhoneNumberActivity.this, "Permissions denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void findViewById() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("FirstName")) {
                firstName = extras.getString("FirstName");
                lastName = extras.getString("LastName");
                DOB = extras.getString("DOB");
                email = extras.getString("Email");
                userName = extras.getString("UserName");
                password = extras.getString("Password");
            }
        }

        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(this);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
        editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        countryCodePicker.registerPhoneNumberTextView(editTextPhoneNumber);
        textViewSingUp = (TextView) findViewById(R.id.textViewSingUp);
        try {

            gpsTracker = new GPSTracker(SignUpPhoneNumberActivity.this);
            if (!gpsTracker.canGetLocation()) {
                initGoogleApiClient();
            } else {
                if (!Utils.getCountryName(SignUpPhoneNumberActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude()).isEmpty()) {
                    String countryName = Utils.getCountryName(SignUpPhoneNumberActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude());
                    countryCodePicker.resetToDefaultCountry();
                    countryCodePicker.setDefaultCountryUsingNameCode(countryName);
                    String code = countryCodePicker.getDefaultCountryCode().toString();
                    countryCodePicker.resetToDefaultCountry();
                    Log.d("code ", code);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        setFontOnViews();
    }

    private void setFontOnViews() {
        buttonBack.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        buttonNext.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        editTextPhoneNumber.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        textViewSingUp.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
    }

    public void initGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(SignUpPhoneNumberActivity.this).addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonBack:
                onBackPressed();
                break;
            case R.id.buttonNext:
                validation();
                break;
        }
    }


    private void validation() {
        if (Utils.isNetworkConnected(SignUpPhoneNumberActivity.this)) {
            CognitoUserAttributes userAttributes = new CognitoUserAttributes();
            AppHelper.setUser(userName);
            AppHelper.init(getApplicationContext());
            phoneNumber = countryCodePicker.getFullNumberWithPlus().trim();
            String phoneNumberValue = editTextPhoneNumber.getText().toString().trim();
            phoneNumber = phoneNumber.replace("(", "");
            phoneNumber = phoneNumber.replace(")", "");
            phoneNumber = phoneNumber.replace(".", "");
            phoneNumber = phoneNumber.replace(" ", "");
            phoneNumber = phoneNumber.replace("-", "");
            Log.d("testPhone", phoneNumber);
            if (phoneNumber.isEmpty()) {
                editTextPhoneNumber.setError(getString(R.string.phoneNumberShouldNotBeEmpty));
            } else if (phoneNumberValue.isEmpty()) {
                editTextPhoneNumber.setError(getString(R.string.phoneNumberShouldNotBeEmpty));
            }/*else if (phoneNumberValue.contains(" ")){
                editTextPhoneNumber.setError(getString(R.string.spacesAreNotAllowed));
            }*/ else {
                String userInput = "IS_NOT_VERIFIED";
                if (userInput != null) {
                    if (userInput.length() > 0) {
                        userAttributes.addAttribute(AppHelper.getSignUpFieldsC2O().get("Profile").toString(), userInput);
                    }
                }
                userInput = "https://s3.amazonaws.com/swap-userfiles-mobilehub-1081613436/default/DefaultProfilePicSwap.png";
                if (userInput != null) {
                    if (userInput.length() > 0) {
                        userAttributes.addAttribute(AppHelper.getSignUpFieldsC2O().get("Picture").toString(), userInput);
                    }
                }

                userInput = email;
                if (userInput != null) {
                    if (userInput.length() > 0) {
                        userAttributes.addAttribute(AppHelper.getSignUpFieldsC2O().get("Email").toString(), userInput);
                    }
                }

                userInput = phoneNumber;
                if (userInput != null) {
                    if (userInput.length() > 0) {
                        userAttributes.addAttribute(AppHelper.getSignUpFieldsC2O().get("Phone number").toString(), userInput);
                    }
                }
                /*showWaitDialog("Start sign up process...");
                AppHelper.getPool().signUpInBackground(userName, password, userAttributes, null, signUpHandler);*/
                showAlertDialogSendSMS("Send confirmation code \nto " + phoneNumber + " ?", userAttributes);
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();

        }
    }

    private void showAlertDialogSendSMS(String body, final CognitoUserAttributes userAttributes) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(body)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            //deleteExitUser();
                            userDialog.dismiss();
                            showWaitDialog("Start sign up process...");
                            AppHelper.getPool().signUpInBackground(userName, password, userAttributes, null, signUpHandler);
                        } catch (Exception e) {
                            //
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userDialog.dismiss();
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private void deleteExitUser() {
        String username = AppHelper.getCurrUser();
        CognitoUser user = AppHelper.getPool().getUser(username);
        user.deleteUser(handler);
    }

    private void showWaitDialog(String message) {
        closeWaitDialog();
        waitDialog = new ProgressDialog(this);
        waitDialog.setTitle(message);
        waitDialog.show();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                String name = null;
                if (data.hasExtra("name")) {
                    name = data.getStringExtra("name");
                }
            }
        } else if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                if (!gpsTracker.canGetLocation()) {
                    if (!Utils.getCountryName(SignUpPhoneNumberActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude()).isEmpty()) {
                        String countryName = Utils.getCountryName(SignUpPhoneNumberActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude());
                        countryCodePicker.resetToDefaultCountry();
                        countryCodePicker.setDefaultCountryUsingNameCode(countryName);
                        String code = countryCodePicker.getDefaultCountryCode().toString();
                        countryCodePicker.resetToDefaultCountry();
                        Log.d("code ", code);
                    }
                }

            } else {
                Toast.makeText(SignUpPhoneNumberActivity.this, "GPS is not enabled", Toast.LENGTH_LONG).show();

            }
        }
    }

    SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState,
                              CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            // Check signUpConfirmationState to see if the user is already confirmed
            Log.d(Tag,"SignUp onSuccess"+"On success");
            if (signUpConfirmationState) {
                Log.d(Tag,"SignUp onSuccess"+"Confirmation");
                // User is already confirmed
                showDialogMessageBool("Sign up successful!", userName + " has been Confirmed", true);
            } else {
                // User is not confirmed
                Log.d(Tag,"SignUp onSuccess"+"Not confirmation");
                confirmSignUp(cognitoUserCodeDeliveryDetails);
            }
            closeWaitDialog();
        }

        @Override
        public void onFailure(Exception exception) {
            closeWaitDialog();
            showDialogMessageBool("Sign up failed", AppHelper.formatException(exception), false);

        }
    };

    private void showDialogMessageBool(String title, String body, final boolean Isexit) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                    if (Isexit) {
                        exit(userName, password);
                    }
                } catch (Exception e) {
                    if (Isexit) {
                        exit(userName, password);
                    }
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private void exit(String userName, String password) {
        if (!userName.isEmpty() && !userName.isEmpty()) {
            // We have the user details, so sign in!
            showWaitDialog("SignUp...");
            AppHelper.init(getApplicationContext());
            AppHelper.getPool().getUser(userName).getSessionInBackground(authenticationHandler);
        }
    }

    private void confirmSignUp(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
        String dest = cognitoUserCodeDeliveryDetails.getDestination();
        String delMed = cognitoUserCodeDeliveryDetails.getDeliveryMedium();
        String textToDisplay = "Code for registration sent to " + dest + " via " + delMed;
        showAlertDialog("Confirmation code", textToDisplay);
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent intent = new Intent(SignUpPhoneNumberActivity.this, SignUpConfirmationCodeActivity.class);
                intent.putExtra("source", "signup");
                intent.putExtra("name", userName);
                intent.putExtra("FirstName", firstName);
                intent.putExtra("LastName", lastName);
                intent.putExtra("DOB", DOB);
                intent.putExtra("Email", email);
                intent.putExtra("PhoneNo", phoneNumber);
                intent.putExtra("Password", password);
                startActivityForResult(intent, 10);
                finishAffinity();
            }
        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }



    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            Log.e("tag", "Auth Success");
            AppHelper.setCurrSession(cognitoUserSession);
            AppHelper.newDevice(device);
            closeWaitDialog();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
            //closeWaitDialog();
            Log.e("tag", "Auth Details");
            Locale.setDefault(Locale.US);
            getUserAuthentication(authenticationContinuation, username);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
            Log.e("tag", "MFACode");
            // closeWaitDialog();
            // mfaAuth(multiFactorAuthenticationContinuation);
        }

        @Override
        public void onFailure(Exception e) {
            closeWaitDialog();
            Log.e("tag", "onFailure");
            showDialogMessage("Sign-in failed", AppHelper.formatException(e));
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            Log.e("tag", "Challenge");
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

    private void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
        if (username != null) {
            this.userName = username;
            AppHelper.setUser(username);
        }
        if (this.password == null) {


        }
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(this.userName, password, null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }

    GenericHandler handler = new GenericHandler() {

        @Override
        public void onSuccess() {
            // Delete was successful!
            Log.e("deletUser", "onSuccess");
        }

        @Override
        public void onFailure(Exception exception) {
            // Delete failed, probe exception for details
            Log.e("deletUser", "onFailure");
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

                gpsTracker = new GPSTracker(SignUpPhoneNumberActivity.this);
                if (!Utils.getCountryName(SignUpPhoneNumberActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude()).isEmpty()) {
                    String countryName = Utils.getCountryName(SignUpPhoneNumberActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude());
                    countryCodePicker.resetToDefaultCountry();
                    countryCodePicker.setDefaultCountryUsingNameCode(countryName);
                    String code = countryCodePicker.getDefaultCountryCode().toString();
                    countryCodePicker.resetToDefaultCountry();
                    Log.d("code ", code);
                }

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog

                try {
                    status.startResolutionForResult(SignUpPhoneNumberActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(message);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(message,
                new IntentFilter("send"));
    }

    private BroadcastReceiver message = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            gpsTracker = new GPSTracker(SignUpPhoneNumberActivity.this);
            if (!Utils.getCountryName(SignUpPhoneNumberActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude()).isEmpty()) {
                String countryName = Utils.getCountryName(SignUpPhoneNumberActivity.this, gpsTracker.getLatitude(), gpsTracker.getLongitude());
                countryCodePicker.resetToDefaultCountry();
                countryCodePicker.setDefaultCountryUsingNameCode(countryName);
                String code = countryCodePicker.getDefaultCountryCode().toString();
                countryCodePicker.resetToDefaultCountry();
                Log.d("code ", code);
            }
        }
    };
}
