package com.swap.views.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.swap.R;
import com.swap.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SignUpDOBActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonBack;
    Button buttonNext;
    EditText editTextDOB;
    String firstName;
    String lastName;
    TextView textViewWillNotBeSwapWithoutPermission;
    TextView textViewWhenYourBirthday;

    //SingleDateAndTimePicker singleDateAndTimePicker;
    //SimpleDateFormat simpleDateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_dob);

        findViewById();
    }

    private void findViewById() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("FirstName")) {
                firstName = extras.getString("FirstName");
                lastName = extras.getString("LastName");
            }
        }
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(this);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);
        editTextDOB = (EditText) findViewById(R.id.editTextDOB);
        editTextDOB.setOnClickListener(this);
        textViewWillNotBeSwapWithoutPermission = (TextView)findViewById(R.id.textViewWillNotBeSwapWithoutPermission);
        textViewWhenYourBirthday = (TextView)findViewById(R.id.textViewWhenYourBirthday);

        setFontOnViews();
    }

    private void setFontOnViews() {
        buttonBack.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        buttonNext.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        editTextDOB.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        textViewWillNotBeSwapWithoutPermission.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
        textViewWhenYourBirthday.setTypeface(Utils.setFont(this, "avenir-light.ttf"));
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
            case R.id.editTextDOB:
                DatePickerFragment mDatePicker = new DatePickerFragment();
                mDatePicker.show(getFragmentManager(), "Select date");
                break;
        }
    }

    private void validation() {
        String dob = editTextDOB.getText().toString().trim();
        if (dob.isEmpty()) {
            showAlertDialog(getString(R.string.sorry), getString(R.string.pleaseAddYourDOB));
        } else {
            Intent signUpDob = new Intent(SignUpDOBActivity.this, SignUpEmailActivity.class);
            signUpDob.putExtra("FirstName", firstName);
            signUpDob.putExtra("LastName", lastName);
            signUpDob.putExtra("DOB", dob);
            startActivity(signUpDob);
        }

    }

    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR) - 13;
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Calendar minAdultAge = new GregorianCalendar();
            minAdultAge.add(Calendar.YEAR, -13);
            if (minAdultAge.before(calendar)) {
                editTextDOB.setText("");
                showAlertDialog(getString(R.string.sorry), getString(R.string.ageValidationAlert));
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                String dateString = dateFormat.format(calendar.getTime());
                //textViewDate.setText( String.valueOf(day)+" " + String.valueOf(month) +" "+String.valueOf(year));
                editTextDOB.setText(dateString);
            }
        }
    }

    public void showAlertDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUpDOBActivity.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }


}
