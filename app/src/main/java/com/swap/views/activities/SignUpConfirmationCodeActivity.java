package com.swap.views.activities;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.swap.R;
import com.swap.models.SwapUser;
import com.swap.models.Users;
import com.swap.utilities.AmazonClientManager;
import com.swap.utilities.AppHelper;
import com.swap.utilities.Constants;
import com.swap.utilities.Preferences;
import com.swap.utilities.Utils;

import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class SignUpConfirmationCodeActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {
    EditText editTextDigitOne;
    EditText editTextDigitTwo;
    EditText editTextDigitThree;
    EditText editTextDigitFour;
    EditText editTextDigitFive;
    EditText editTextDigitSix;
    EditText editTextHiddenCode;
    Button buttonSendCodeAgain;
    Button buttonNext;
    boolean IsFromForgotPassword = false;
    String firstName;
    String lastName;
    String dob;
    String email;
    String userName;
    String phoneNo;
    String password;
    TextView tvHeading;
    private AlertDialog userDialog;
    private ProgressDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainLayout(this, null));
        findViewById();
    }

    private void findViewById() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("FromForgotPwd")) {
                IsFromForgotPassword = extras.getBoolean("FromForgotPwd");
            } else if (extras.containsKey("name")) {
                userName = extras.getString("name");
                firstName = extras.getString("FirstName");
                lastName = extras.getString("LastName");
                dob = extras.getString("DOB");
                email = extras.getString("Email");
                phoneNo = extras.getString("PhoneNo");
                password = extras.getString("Password");
            }
        }
        editTextDigitOne = (EditText) findViewById(R.id.editTextDigitOne);
        editTextDigitTwo = (EditText) findViewById(R.id.editTextDigitTwo);
        editTextDigitThree = (EditText) findViewById(R.id.editTextDigitThree);
        editTextDigitFour = (EditText) findViewById(R.id.editTextDigitFour);
        editTextDigitFive = (EditText) findViewById(R.id.editTextDigitFive);
        editTextDigitSix = (EditText) findViewById(R.id.editTextDigitSix);

        editTextHiddenCode = (EditText) findViewById(R.id.editTextHiddenCode);
        tvHeading = (TextView) findViewById(R.id.tvHeading);

        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);
        buttonSendCodeAgain = (Button) findViewById(R.id.buttonSendCodeAgain);
        buttonSendCodeAgain.setOnClickListener(this);
        setFontOnViews();
        digitsListener();
    }

    private void setFontOnViews() {
        editTextDigitOne.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        editTextDigitTwo.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        editTextDigitThree.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        editTextDigitFour.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        editTextDigitFive.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        editTextDigitSix.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        editTextHiddenCode.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        tvHeading.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        buttonNext.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        buttonSendCodeAgain.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
    }

    private void digitsListener() {
        editTextHiddenCode.addTextChangedListener(this);

        editTextDigitOne.setOnFocusChangeListener(this);
        editTextDigitTwo.setOnFocusChangeListener(this);
        editTextDigitThree.setOnFocusChangeListener(this);
        editTextDigitFour.setOnFocusChangeListener(this);
        editTextDigitFive.setOnFocusChangeListener(this);
        editTextDigitSix.setOnFocusChangeListener(this);

        editTextDigitOne.setOnKeyListener(this);
        editTextDigitTwo.setOnKeyListener(this);
        editTextDigitThree.setOnKeyListener(this);
        editTextDigitFour.setOnKeyListener(this);
        editTextDigitFive.setOnKeyListener(this);
        editTextDigitSix.setOnKeyListener(this);

        editTextHiddenCode.setOnKeyListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSendCodeAgain:
                //TODO implement
                resendCode();
                break;
            case R.id.buttonNext:
                validation();
                break;
        }
    }

    private void resendCode() {
        if (Utils.isNetworkConnected(SignUpConfirmationCodeActivity.this)) {
            if (!userName.isEmpty()) {
                showWaitDialog("Resend code...");
                AppHelper.getPool().getUser(userName).resendConfirmationCodeInBackground(resendConfCodeHandler);
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
        }
    }

    private void validation() {
        if (Utils.isNetworkConnected(SignUpConfirmationCodeActivity.this)) {
            String code = editTextHiddenCode.getText().toString().trim();
            if (code.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.pleaseAddConfirmationCode, Toast.LENGTH_LONG).show();
            } else if (code.length() < 6) {
                Toast.makeText(getApplicationContext(), R.string.pleaseAddCompleatConfirmationCode, Toast.LENGTH_LONG).show();
            } else {
                if (IsFromForgotPassword) {
                    Intent i = new Intent(SignUpConfirmationCodeActivity.this, SignUpPasswordActivity.class);
                    i.putExtra("code", code);
                    i.putExtra("IsFromForgotPassword", IsFromForgotPassword);
                    startActivity(i);
                    //IsFromFrogotPassword=false;
                    finish();
                } else {
                    showWaitDialog("Confirmation in progress...");
                    AppHelper.getPool().getUser(userName).confirmSignUpInBackground(code, true, confHandler);

                }
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();

        }
    }

    GenericHandler confHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            closeWaitDialog();
            //showDialogMessage("Success!", userName + " has been confirmed!", true);
            AppHelper.init(getApplicationContext());
            AppHelper.getPool().getUser(userName).getSessionInBackground(authenticationHandler);

        }

        @Override
        public void onFailure(Exception exception) {
            closeWaitDialog();
            showDialogMessage("Confirmation failed", AppHelper.formatException(exception), false);
        }
    };


    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            Log.e("tag", "Auth Success");
            AppHelper.setCurrSession(cognitoUserSession);
            AppHelper.newDevice(device);
            showDialogMessage("Success!", userName + " has been confirmed!", true);
            closeWaitDialog();
            //launchUser();
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
            //closeWaitDialog();
            //mfaAuth(multiFactorAuthenticationContinuation);
        }

        @Override
        public void onFailure(Exception e) {
            closeWaitDialog();
            Log.e("tag", "onFailure");
            //showDialogMessage("Sign-in failed", AppHelper.formatException(e));
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


    private Users saveUserDataInDB() {
        Users users = new Users();
        users.setUsername(userName);
        users.setFirstname(firstName);
        users.setLastname(lastName);
        users.setProfile_picture_url(Constants.DEFAULT_PROFILE_PICTURE);
        Date date = new Date(dob);
        long dateDOB = date.getTime();
        users.setBirthday(dateDOB);
        users.setEmail(email);
        users.setPhonenumber(phoneNo);
        users.setBio("");
        users.setProfile_picture_url("");
        users.setPrivate(false);
        users.setCompany("");
        users.setPoints(0);
        users.setSwapped(0);
        users.setSwaps(0);
        users.setIsVerified(false);
        users.setGithubID("");
        users.setSpotify_ID("");
        users.setInstagram_ID("");
        users.setPinterest_ID("");
        users.setRedditID("");
        users.setYoutube_ID("");
        users.setSoundcloud_ID("");
        users.setNotification_id_one_signal("");
        users.setTwitter_ID("");
        users.setVimeoID("");
        users.setVine_ID("");
        users.setWebsite("");
        users.setWillShareEmail(false);
        users.setWillShareInstagram(false);
        users.setWillSharePhone(false);
        users.setWillSharePinterest(false);
        users.setWillShareReddit(false);
        users.setWillShareSoundCloud(false);
        users.setWillShareSpotify(false);
        users.setWillShareTwitter(false);
        users.setWillShareVine(false);
        users.setWillShareVimeo(false);
        users.setWillShareYouTube(false);
        users.setWillShareGitHub(false);
        users.setGender("");
        return users;
        //return SwapUser.updateUsers(SignUpConfirmationCodeActivity.this, users);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


        if (s.length() == 0) {
            editTextDigitOne.setText("");
            setValueHiddenCode();
        } else if (s.length() == 1) {
            editTextDigitOne.setText(s.charAt(0) + "");
            editTextDigitTwo.setText("");
            editTextDigitThree.setText("");
            editTextDigitFour.setText("");
            editTextDigitFive.setText("");
            editTextDigitSix.setText("");
            setValueHiddenCode();
        } else if (s.length() == 2) {
            editTextDigitTwo.setText(s.charAt(1) + "");
            editTextDigitThree.setText("");
            editTextDigitFour.setText("");
            editTextDigitFive.setText("");
            editTextDigitSix.setText("");
            setValueHiddenCode();
        } else if (s.length() == 3) {
            editTextDigitThree.setText(s.charAt(2) + "");
            editTextDigitFour.setText("");
            editTextDigitFive.setText("");
            editTextDigitSix.setText("");
            setValueHiddenCode();
        } else if (s.length() == 4) {
            editTextDigitFour.setText(s.charAt(3) + "");
            editTextDigitFive.setText("");
            editTextDigitSix.setText("");
            setValueHiddenCode();
        } else if (s.length() == 5) {
            editTextDigitFive.setText(s.charAt(4) + "");
            editTextDigitSix.setText("");
            setValueHiddenCode();
        } else if (s.length() == 6) {
            editTextDigitSix.setText(s.charAt(5) + "");
            hideSoftKeyboard(editTextDigitSix);
            setValueHiddenCode();
        }
    }

    private void setValueHiddenCode() {
        int pos = editTextHiddenCode.getText().length();
        editTextHiddenCode.setSelection(pos);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        final int id = view.getId();
        switch (id) {
            case R.id.editTextDigitOne:
                if (hasFocus) {
                    setFocus(editTextHiddenCode);
                    showSoftKeyboard(editTextHiddenCode);
                }
                break;

            case R.id.editTextDigitTwo:
                if (hasFocus) {
                    setFocus(editTextHiddenCode);
                    showSoftKeyboard(editTextHiddenCode);
                }
                break;

            case R.id.editTextDigitThree:
                if (hasFocus) {
                    setFocus(editTextHiddenCode);
                    showSoftKeyboard(editTextHiddenCode);
                }
                break;

            case R.id.editTextDigitFour:
                if (hasFocus) {
                    setFocus(editTextHiddenCode);
                    showSoftKeyboard(editTextHiddenCode);
                }
                break;

            case R.id.editTextDigitFive:
                if (hasFocus) {
                    setFocus(editTextHiddenCode);
                    showSoftKeyboard(editTextHiddenCode);
                }
                break;
            case R.id.editTextDigitSix:
                if (hasFocus) {
                    setFocus(editTextHiddenCode);
                    showSoftKeyboard(editTextHiddenCode);
                }
                break;
            default:
                break;
        }

    }

    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = view.getId();
            switch (id) {
                case R.id.editTextHiddenCode:
                    String hiddenCode = editTextHiddenCode.getText().toString();
                    Log.d("editTextHiddenCode: ", hiddenCode);
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (editTextHiddenCode.getText().length() == 6)
                            editTextDigitSix.setText("");
                        else if (editTextHiddenCode.getText().length() == 5)
                            editTextDigitFive.setText("");
                        else if (editTextHiddenCode.getText().length() == 4)
                            editTextDigitFour.setText("");
                        else if (editTextHiddenCode.getText().length() == 3)
                            editTextDigitThree.setText("");
                        else if (editTextHiddenCode.getText().length() == 2)
                            editTextDigitTwo.setText("");
                        else if (editTextHiddenCode.getText().length() == 1)
                            editTextDigitOne.setText("");
                        if (editTextHiddenCode.length() > 0)
                            editTextHiddenCode.setText(editTextHiddenCode.getText().subSequence(0, editTextHiddenCode.length() - 1));
                        return true;
                    }

                    break;

                default:
                    return false;
            }
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        showDialogMessageExit("Exit!", "You are really want to exit?");

    }

    private void showDialogMessageExit(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(title).setMessage(body)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            userDialog.dismiss();
                            Intent intent = new Intent(SignUpConfirmationCodeActivity.this, LoginActivity.class);
                            intent.putExtra("EXIT", true);
                            startActivity(intent);
                            finishAffinity();
                        } catch (Exception e) {
                            //
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userDialog.dismiss();
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    public void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public class MainLayout extends LinearLayout {

        public MainLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.activity_sign_up_confirmation_code, this);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int proposedHeight = MeasureSpec.getSize(heightMeasureSpec);
            final int actualHeight = getHeight();

            Log.d("TAG", "proposed: " + proposedHeight + ", actual: " + actualHeight);

            if (actualHeight >= proposedHeight) {
                // Keyboard is shown
                /*if (editTextHiddenCode.length() == 0)
                    setFocusedPinBackground(mPinFirstDigitEditText);
                else
                    setDefaultPinBackground(mPinFirstDigitEditText);*/
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
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

    VerificationHandler resendConfCodeHandler = new VerificationHandler() {
        @Override
        public void onSuccess(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            closeWaitDialog();
            showDialogMessage("Confirmation code sent.", "Code sent to " + cognitoUserCodeDeliveryDetails.getDestination() + " via " + cognitoUserCodeDeliveryDetails.getDeliveryMedium() + ".", false);
        }

        @Override
        public void onFailure(Exception exception) {
            closeWaitDialog();
            showDialogMessage("Confirmation code request has failed", AppHelper.formatException(exception), false);
        }
    };

    private void showDialogMessage(String title, String body, final boolean exitActivity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                    if (exitActivity) {
                        exit();
                    }
                } catch (Exception e) {
                    exit();
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private void exit() {
        Preferences.save(this, Preferences.USERNAME, userName);
        Log.d("userName", Preferences.get(this, Preferences.USERNAME));
        if (Utils.isNetworkConnected(this)) {
            Users users = saveUserDataInDB();
            AmazonClientManager.getInstance(SignUpConfirmationCodeActivity.this).ddb();
            if (users!=null) {
                new SaveUserInDB(users).execute();
            }else {
                Toast.makeText(getApplicationContext(), "User data is null", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
        }
    }

    private class SaveUserInDB extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog pDialog = new ProgressDialog(SignUpConfirmationCodeActivity.this);
        Users mUsers;
        public SaveUserInDB(Users users) {
            this.mUsers=users;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.loading));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.d("savedata", "Starting..");

           // SwapUser.updateUsers(SignUpConfirmationCodeActivity.this, mUsers);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return SwapUser.updateUsers(SignUpConfirmationCodeActivity.this, mUsers);
        }

        @Override
        protected void onPostExecute(Boolean results) {
            super.onPostExecute(results);
            Log.d("savedata", String.valueOf(results));
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (results) {
                //users= results;
                Preferences.saveBooleanValue(SignUpConfirmationCodeActivity.this, Preferences.IsLoginOrSignup, true);
                AmazonClientManager.getInstance(SignUpConfirmationCodeActivity.this).ddb();
                Intent intent = new Intent(SignUpConfirmationCodeActivity.this, ConnectSocialMediaActivity.class);
                intent.putExtra("ShowcaseKey", new Random().nextInt());
                intent.putExtra("isFromSettings", false);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }else
            {
                Preferences.saveBooleanValue(SignUpConfirmationCodeActivity.this, Preferences.IsLoginOrSignup, false);
                Toast.makeText(SignUpConfirmationCodeActivity.this, "Please try again, user creation failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
