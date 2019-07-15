package com.swap.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.swap.R;
import com.swap.utilities.EmojiExcludeFilter;
import com.swap.utilities.Utils;

public class SignUpPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonBack;
    Button buttonNext;
    TextView textViewShowPassword;
    TextView textViewSingUp;
    EditText editTextPassword;
    int passwordNotVisible = 1;
    String code;
    String firstName;
    String lastName;
    String DOB;
    String email;
    String userName;

    boolean IsFromForgotPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_password);

        findViewById();

    }

    private void findViewById() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("code")) {
                code = extras.getString("code");
                IsFromForgotPassword = extras.getBoolean("IsFromForgotPassword");
            } else if (extras.containsKey("FirstName")) {
                firstName = extras.getString("FirstName");
                lastName = extras.getString("LastName");
                DOB = extras.getString("DOB");
                email = extras.getString("Email");
                userName = extras.getString("UserName");
            }
        }

        textViewShowPassword = (TextView) findViewById(R.id.textViewShowPassword);
        textViewSingUp = (TextView) findViewById(R.id.textViewSingUp);
        textViewShowPassword.setOnClickListener(this);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(this);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        editTextPassword.setFilters(new InputFilter[]{filter});
        editTextPassword.setFilters(new InputFilter[]{new EmojiExcludeFilter(this)});
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setFontOnViews();
    }

    private void setFontOnViews() {
        buttonBack.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        buttonNext.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        textViewShowPassword.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        textViewSingUp.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        editTextPassword.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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
            case R.id.textViewShowPassword:
                showPassword();
                break;
        }
    }

    private void showPassword() {
        if (passwordNotVisible == 1) {
            editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordNotVisible = 0;
        } else {
            editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordNotVisible = 1;
        }
        editTextPassword.setSelection(editTextPassword.length());
    }

    private void validation() {
        String password = editTextPassword.getText().toString().trim();
        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.signUpPasswordShouldNotBeEmpty));
        } else if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.signUpPasswordShouldBeMin));
        } else {
            if (IsFromForgotPassword) {
                exit(password, code);
            } else {
                Intent signUpPassword = new Intent(SignUpPasswordActivity.this, SignUpPhoneNumberActivity.class);
                signUpPassword.putExtra("FirstName", firstName);
                signUpPassword.putExtra("LastName", lastName);
                signUpPassword.putExtra("DOB", DOB);
                signUpPassword.putExtra("Email", email);
                signUpPassword.putExtra("UserName", userName);
                signUpPassword.putExtra("Password", password);
                startActivity(signUpPassword);
            }
        }
    }

    private void exit(String newPass, String code) {
        ForgotPasswordActivity.forgotPasswordContinuation.setPassword(newPass);
        ForgotPasswordActivity.forgotPasswordContinuation.setVerificationCode(code);
        ForgotPasswordActivity.forgotPasswordContinuation.continueTask();
        Intent signUpPassword = new Intent(SignUpPasswordActivity.this, LoginActivity.class);
        startActivity(signUpPassword);
        finish();
    }


}
