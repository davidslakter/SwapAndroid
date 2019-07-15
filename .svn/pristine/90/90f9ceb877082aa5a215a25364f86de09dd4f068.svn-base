package com.swap.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.swap.R;
import com.swap.utilities.AppHelper;

public class MainActivity extends AppCompatActivity {
    private CognitoUser user;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppHelper.init(getApplicationContext());
        username = AppHelper.getCurrUser();
        if (username!=null&&!username.isEmpty()) {
            user = AppHelper.getPool().getUser(username);
            user.getDetails(detailsHandler);
        }
    }

    GetDetailsHandler detailsHandler = new GetDetailsHandler() {
        @Override
        public void onSuccess(CognitoUserDetails cognitoUserDetails) {
            Log.e("tag", " onSuccess");
            // closeWaitDialog();
            // Store details in the AppHandler
            AppHelper.setUserDetails(cognitoUserDetails);
            // Trusted devices?
            handleTrustedDevice();
        }

        @Override
        public void onFailure(Exception exception) {
            Log.e("tag", " onFailure");
            //closeWaitDialog();
            // showDialogMessage("Could not fetch users details!", AppHelper.formatException(exception), true);
        }
    };
    private void handleTrustedDevice() {
        CognitoDevice newDevice = AppHelper.getNewDevice();
        if (newDevice != null) {
            AppHelper.newDevice(null);
            // trustedDeviceDialog(newDevice);
        }
    }
}
