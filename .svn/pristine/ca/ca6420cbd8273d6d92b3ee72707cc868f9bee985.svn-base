package com.swap.views.activities;

import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.swap.R;
import com.swap.utilities.AppHelper;
import com.swap.utilities.Utils;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    public static ForgotPasswordContinuation forgotPasswordContinuation;
    String userName;
    TextView textViewForgotPassword;
    Button buttonBack;
    Button buttonNext;
    private String code;
    private String newPassword;
    private AlertDialog userDialog;
    private ProgressDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        findViewById();
    }

    private void findViewById() {
        textViewForgotPassword= (TextView)findViewById(R.id.textViewForgotPassword);
        buttonBack= (Button) findViewById(R.id.buttonBack);
        buttonNext= (Button) findViewById(R.id.buttonNext);
        buttonBack.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        setFontOnViews();
    }

    private void setFontOnViews() {
        textViewForgotPassword.setTypeface(Utils.setFont(this, "lantinghei.ttf"));
        buttonNext.setTypeface(Utils.setFont(this, "lantinghei.ttf"));
        buttonBack.setTypeface(Utils.setFont(this, "lantinghei.ttf"));
        getEditTextUserName().setTypeface(Utils.setFont(this, "lantinghei.ttf"));
    }

    private EditText getEditTextUserName() {
        return (EditText) findViewById(R.id.editTextFirstName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonBack:
                finish();
                break;
            case R.id.buttonNext:
                validations();
                break;
        }
    }

    private void validations() {
        if (Utils.isNetworkConnected(ForgotPasswordActivity.this)) {
            userName = getEditTextUserName().getText().toString().trim();
            if (!userName.isEmpty() && userName.length() > 0) {
                showWaitDialog("Processing...");
                AppHelper.init(getApplicationContext());
                AppHelper.getPool().getUser(userName).forgotPasswordInBackground(forgotPasswordHandler);
            } else if (getEditTextUserName().getText().toString().isEmpty()) {
                getEditTextUserName().setError(getString(R.string.usernameShouldBeNotEmpty));
            } else if (getEditTextUserName().getText().toString().contains(" ")) {
                getEditTextUserName().setError(getString(R.string.spacesAreNotAllowed));
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();

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

    public void showAlertDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent intent = new Intent(ForgotPasswordActivity.this, SignUpConfirmationCodeActivity.class);
                // intent.putExtra("destination",forgotPasswordContinuation.getParameters().getDestination());
                // intent.putExtra("deliveryMed", forgotPasswordContinuation.getParameters().getDeliveryMedium());
                intent.putExtra("FromForgotPwd", true);
                startActivityForResult(intent, 3);
                finish();

            }
        });
        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

    }

    // Callbacks
    ForgotPasswordHandler forgotPasswordHandler = new ForgotPasswordHandler() {
        @Override
        public void onSuccess() {
            Log.d("forgotPassword:", "forgotPassword");
            closeWaitDialog();
            Intent signUpPassword = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            signUpPassword.putExtra("FromForgotPwd", true);
            startActivity(signUpPassword);
            finish();
        }

        @Override
        public void getResetCode(ForgotPasswordContinuation forgotPasswordContinuation) {
            closeWaitDialog();
            getForgotPasswordCode(forgotPasswordContinuation);
        }

        @Override
        public void onFailure(Exception e) {
            closeWaitDialog();
            showDialogMessage("Forgot password failed", AppHelper.formatException(e));
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

    private void getForgotPasswordCode(ForgotPasswordContinuation forgotPasswordContinuation) {
        this.forgotPasswordContinuation = forgotPasswordContinuation;

        String dest = forgotPasswordContinuation.getParameters().getDestination();
        String delMed = forgotPasswordContinuation.getParameters().getDeliveryMedium();
        String textToDisplay = "Code to set a new password was sent to " + dest + " via " + delMed;
        showAlertDialog(getString(R.string.reset_password), textToDisplay);
    }


}
