package com.swap.utilities;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler;
import com.swap.views.activities.EditProfileActivity;

import java.util.List;

/**
 * Created by anjali on 23-10-2017.
 */

public class UpdateAttributes {


    private static String username;
    // Update attributes
    public static void updateAttribute(Context context, String attributeType, String attributeValue) {
        username = Preferences.get(context, Preferences.USERNAME);
        if (attributeType == null || attributeType.length() < 1) {
            return;
        }
        CognitoUserAttributes updatedUserAttributes = new CognitoUserAttributes();
        updatedUserAttributes.addAttribute(attributeType, attributeValue);
        //Toast.makeText(getApplicationContext(), attributeType + ": " + attributeValue, Toast.LENGTH_LONG);
        //showWaitDialog("Updating...");
        AppHelper.getPool().getUser(username).updateAttributesInBackground(updatedUserAttributes, updateHandler);
    }

    static UpdateAttributesHandler updateHandler = new UpdateAttributesHandler() {
        @Override
        public void onSuccess(List<CognitoUserCodeDeliveryDetails> attributesVerificationList) {
            // Update successful
            if (attributesVerificationList.size() > 0) {
                Log.d("Updated", "Updated");
                // showDialogMessage("Updated", "The updated attributes has to be verified",  false);
            }
            getDetails();
        }

        @Override
        public void onFailure(Exception exception) {
            // Update failed
            try {
                Log.e("Updated", String.valueOf(exception));
            }catch (Exception e){

            }

            // showDialogMessage("Update failed", AppHelper.formatException(exception), false);
        }
    };
    // Get user details from CIP service
    private static void getDetails() {
        AppHelper.getPool().getUser(username).getDetailsInBackground(detailsHandler);
    }

    static GetDetailsHandler detailsHandler = new GetDetailsHandler() {
        @Override
        public void onSuccess(CognitoUserDetails cognitoUserDetails) {

            // Store details in the AppHandler
            AppHelper.setUserDetails(cognitoUserDetails);

        }

        @Override
        public void onFailure(Exception exception) {

            // showDialogMessage("Could not fetch user details!", AppHelper.formatException(exception), true);
        }
    };


}
