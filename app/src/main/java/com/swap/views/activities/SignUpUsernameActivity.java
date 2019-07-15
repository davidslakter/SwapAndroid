package com.swap.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.swap.R;
import com.swap.utilities.AppHelper;
import com.swap.utilities.Utils;

public class SignUpUsernameActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack;
    Button buttonNext;
    EditText editTextUsername;
    TextView textViewStatus;
    TextView textViewChooseYourUsername;
    String firstName;
    String lastName;
    String DOB;
    String email;
    Boolean isUserAvailable = false;
    View viewUserAvailability;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_username);
        findViewById();
    }

    private void findViewById() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("FirstName")) {
                firstName = extras.getString("FirstName");
                lastName = extras.getString("LastName");
                DOB = extras.getString("DOB");
                email = extras.getString("Email");
            }
        }
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(this);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);
        buttonNext.setAlpha(0.5F);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        textViewChooseYourUsername = (TextView) findViewById(R.id.textViewChooseYourUsername);
        viewUserAvailability = (View) findViewById(R.id.viewUserAvailability);
        viewUserAvailability.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed));
        textViewStatus.setVisibility(View.INVISIBLE);
        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                AppHelper.init(SignUpUsernameActivity.this);
                username = editTextUsername.getText().toString().trim();
                textViewStatus.setVisibility(View.VISIBLE);
                if (username.isEmpty()) {
                    viewUserAvailability.setBackgroundColor(ContextCompat.getColor(SignUpUsernameActivity.this, R.color.colorRed));
                    textViewStatus.setText(getString(R.string.yourUsernameShouldNotBeEmpty));
                    buttonNext.setAlpha(0.5F);
                } else if (username.length() < 1) {
                    viewUserAvailability.setBackgroundColor(ContextCompat.getColor(SignUpUsernameActivity.this, R.color.colorRed));
                    textViewStatus.setText(R.string.UsernamesMustBeAtLeastOneCharacter);
                    buttonNext.setAlpha(0.5F);
                } else if (username.length() > 18) {
                    viewUserAvailability.setBackgroundColor(ContextCompat.getColor(SignUpUsernameActivity.this, R.color.colorRed));
                    textViewStatus.setText(R.string.UsernamesMustBeLessThanCharacters);
                    buttonNext.setAlpha(0.5F);
                } else if (username.equals(" ")) {
                    viewUserAvailability.setBackgroundColor(ContextCompat.getColor(SignUpUsernameActivity.this, R.color.colorRed));
                    textViewStatus.setVisibility(View.VISIBLE);
                    textViewStatus.setText(R.string.usernameShouldNotBeAcceptSpace);
                    buttonNext.setAlpha(0.5F);
                } else {
                    if (Utils.isNetworkConnected(SignUpUsernameActivity.this)) {
                        getDetails(username);
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        setFontOnViews();
    }

    private void setFontOnViews() {
        buttonBack.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        buttonNext.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        textViewChooseYourUsername.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        textViewStatus.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        editTextUsername.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
    }

    private void getDetails(final String username) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                AppHelper.getPool().getUser(username).
                        confirmSignUp("0", false, mGenericHandler);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    GenericHandler mGenericHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            Log.e("getDetailsHandler", "onSuccess");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textViewStatus.setText("");
                    textViewStatus.setText(R.string.usernameIsTakenTryAnother);
                    viewUserAvailability.setBackgroundColor(ContextCompat.getColor(SignUpUsernameActivity.this, R.color.colorRed));
                    isUserAvailable = false;
                    buttonNext.setAlpha(0.5F);
                }
            });
        }

        @Override
        public void onFailure(Exception exception) {
            Log.e("getDetailsHandler", "onFailure " + exception.getMessage());
            final String error = exception.getMessage();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (error.contains("Invalid code provided")) {
                        textViewStatus.setText("");
                        textViewStatus.setText(R.string.usernameIsTakenTryAnother);
                        viewUserAvailability.setBackgroundColor(ContextCompat.getColor(SignUpUsernameActivity.this, R.color.colorRed));
                        isUserAvailable = false;
                        buttonNext.setAlpha(0.5F);
                    } else if (error.contains("user exceeds the limit for a requested")) {
                        textViewStatus.setText("");
                        textViewStatus.setText(R.string.internalServerIssue);
                        viewUserAvailability.setBackgroundColor(ContextCompat.getColor(SignUpUsernameActivity.this, R.color.colorRed));
                        isUserAvailable = false;
                        buttonNext.setAlpha(0.5F);
                    }else {
                        if (!username.isEmpty()) {
                            textViewStatus.setText("");
                            textViewStatus.setText(R.string.usernameAvailable);
                            viewUserAvailability.setBackgroundColor(ContextCompat.getColor(SignUpUsernameActivity.this, R.color.colorGreen));
                            isUserAvailable = true;
                            buttonNext.setAlpha(1F);
                        }
                    }
                }
            });

        }
    };


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
        String username = editTextUsername.getText().toString().trim();
        if (username.isEmpty()) {
            textViewStatus.setVisibility(View.VISIBLE);
            textViewStatus.setText(getString(R.string.yourUsernameShouldNotBeEmpty));
            // editTextUsername.setError(getString(R.string.yourUsernameShouldNotBeEmpty));
        } else if (username.length() < 1) {
            textViewStatus.setText(R.string.UsernamesMustBeAtLeastOneCharacter);
        } else if (username.length() > 18) {
            textViewStatus.setText(R.string.UsernamesMustBeLessThanCharacters);
        } else if (username.equals(" ")) {
            textViewStatus.setVisibility(View.VISIBLE);
            textViewStatus.setText(R.string.usernameShouldNotBeAcceptSpace);
        } else {
            if (isUserAvailable) {
                buttonNext.setAlpha(1F);
                Intent signUpUserName = new Intent(SignUpUsernameActivity.this, SignUpPasswordActivity.class);
                signUpUserName.putExtra("FirstName", firstName);
                signUpUserName.putExtra("LastName", lastName);
                signUpUserName.putExtra("DOB", DOB);
                signUpUserName.putExtra("Email", email);
                signUpUserName.putExtra("UserName", username);
                startActivity(signUpUserName);
            }


        }
    }

}
