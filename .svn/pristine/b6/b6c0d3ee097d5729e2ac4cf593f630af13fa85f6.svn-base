package com.swap.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.swap.R;
import com.swap.utilities.Preferences;
import com.swap.utilities.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class SplashActivity extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById();
        viewNavigation();


    }

    private void findViewById() {
        TextView textViewCopyRight = (TextView)findViewById(R.id.textViewCopyRight);
        textViewCopyRight.setTypeface(Utils.setFont(SplashActivity.this, "lantinghei.ttf"));
    }

    private void viewNavigation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                boolean isLogin = Preferences.getBooleanValue(SplashActivity.this, Preferences.IsLoginOrSignup);
                if (isLogin) {
                    boolean isConnectSocial = Preferences.getBooleanValue(SplashActivity.this, Preferences.IsConnectSocialMedia);
                    if (isConnectSocial) {
                        Preferences.saveBooleanValue(SplashActivity.this, Preferences.IsConnectSocialMediaFromSplash,true);
                        Intent i = new Intent(SplashActivity.this, ConnectSocialMediaActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();

        // If NOT using automatic session management
        // Branch branch = Branch.getInstance(getApplicationContext());

        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the users clicked before showing up
                    // params will be empty if no data found
                    String pictureID = referringParams.optString("picture_id", "");
                    if (pictureID.equals("")) {
                        //startActivity(new Intent(this, HomeActivity.class));
                    } else {
                       /* Intent i = new Intent(this, ViewerActivity.class);
                        i.putExtra("picture_id", pictureID);
                        startActivity(i);*/
                    }
                } else {
                    Log.e("MyApp", error.getMessage());
                }
            }


        }, this.getIntent().getData(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

    public void OnClickTestText(View view) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sku", "Swap");
        } catch (JSONException e) {
        }
        Branch.getInstance(getApplicationContext()).userCompletedAction("Swap", jsonObject);
       /* CommerceEvent commerceEvent = new CommerceEvent();
        commerceEvent.setRevenue(0.0);
        commerceEvent.setCurrencyType(CurrencyType.USD);*/
    }
}
