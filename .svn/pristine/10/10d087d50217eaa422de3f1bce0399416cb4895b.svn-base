package com.swap.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.swap.R;
import com.swap.utilities.Utils;

public class MoreInfoActivity extends AppCompatActivity {
    EditText editTextMiddleName;
    EditText editTextCompany;
    EditText editTextWebsite;

    static String website;
    static String company;
    static String middleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        findViewById();
        inItToolBar();
    }

    private void findViewById() {

        editTextMiddleName = (EditText) findViewById(R.id.editTextMiddleName);
        editTextCompany = (EditText) findViewById(R.id.editTextCompany);
        editTextWebsite = (EditText) findViewById(R.id.editTextWebsite);

        setFontOnViews();
        setData();
    }
    private void setFontOnViews() {
        editTextMiddleName.setTypeface(Utils.setFont(this, "lantinghei.ttf"));
        editTextCompany.setTypeface(Utils.setFont(this, "lantinghei.ttf"));
        editTextWebsite.setTypeface(Utils.setFont(this, "lantinghei.ttf"));
        editTextWebsite.setTypeface(Utils.setFont(this, "lantinghei.ttf"));
    }
    private void setData() {
        if (middleName != null) {
            editTextMiddleName.setText(middleName);
        }
        if (company != null) {
            editTextCompany.setText(company);
        }
        if (website != null) {
            editTextWebsite.setText(website);
        }
    }

    private void inItToolBar() {
        View view = findViewById(R.id.layout_toolbar);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarView);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.textViewTitle);
        toolbar.setNavigationIcon(R.drawable.back_arrow_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                if (website!=null&&!website.equals("") && !(Patterns.WEB_URL.matcher(website).matches())){
                    editTextWebsite.setError(getResources().getString(R.string.invalidURL));
                }else {
                    finish();
                }
            }
        });

    }

    private void getData() {
        middleName = editTextMiddleName.getText().toString().trim();
        company = editTextCompany.getText().toString().trim();
        website = editTextWebsite.getText().toString().trim();
    }
}
