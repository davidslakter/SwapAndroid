/*
 * Copyright 2010-2012 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 * 
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.swap.utilities;

import android.content.Context;
import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to get clients to the various AWS services. Before
 * accessing a client the credentials should be checked to ensure validity.
 */
public class AmazonClientManager {

    private static final String LOG_TAG = "AmazonClientManager";
    private AmazonDynamoDBClient ddb;
    private Context context;
    private static AmazonClientManager mInstance;
    CognitoCachingCredentialsProvider credentials;

    public AmazonClientManager(Context context) {
        this.context = context;
    }

    public static AmazonClientManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AmazonClientManager(context);
            Log.d("mInstance", "done");
        } else {
            Log.d("mInstance", "noDone");
        }
        return mInstance;
    }

    public AmazonDynamoDBClient ddb() {
        validateCredentials();
        return ddb;
    }

    public boolean hasCredentials() {
        return (!(Constants.IDENTITY_POOL_ID.equalsIgnoreCase("us-east-1:d0d2cc0d-a727-4228-91b7-f1e7aba06e5c")
                || Constants.TABLE_USERS.equalsIgnoreCase("swap-mobilehub-1081613436-Users")));
    }

    public void validateCredentials() {
        if (ddb == null) {
            //initClients();
            Log.d("validateCredentials", "done");
            getCredentials();
        }else {
            Log.d("validateCredentials", "notDone");
        }
    }

    public boolean wipeCredentialsOnAuthError(AmazonServiceException ex) {
        Log.e(LOG_TAG, "Error, wipeCredentialsOnAuthError called" + ex);
        if (
            // STS
            // http://docs.amazonwebservices.com/STS/latest/APIReference/CommonErrors.html
                ex.getErrorCode().equals("IncompleteSignature")
                        || ex.getErrorCode().equals("InternalFailure")
                        || ex.getErrorCode().equals("InvalidClientTokenId")
                        || ex.getErrorCode().equals("OptInRequired")
                        || ex.getErrorCode().equals("RequestExpired")
                        || ex.getErrorCode().equals("ServiceUnavailable")

                        // DynamoDB
                        // http://docs.amazonwebservices.com/amazondynamodb/latest/developerguide/ErrorHandling.html#APIErrorTypes
                        || ex.getErrorCode().equals("AccessDeniedException")
                        || ex.getErrorCode().equals("IncompleteSignatureException")
                        || ex.getErrorCode().equals(
                        "MissingAuthenticationTokenException")
                        || ex.getErrorCode().equals("ValidationException")
                        || ex.getErrorCode().equals("InternalFailure")
                        || ex.getErrorCode().equals("InternalServerError")) {

            return true;
        }
        return false;
    }

    private void getCredentials() {
        AppHelper.init(context);
        CognitoUser user;
        String username = Preferences.get(context, Preferences.USERNAME);
        Log.d("usernameCredentials", username);
        user = AppHelper.getPool().getUser(username);
        if (user != null) {
            Log.d("getCredentials", "done");
            user.getSessionInBackground(new AuthenticationHandler() {
                @Override
                public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                    Log.d("onSuccess", String.valueOf(newDevice));
                    credentials = new CognitoCachingCredentialsProvider(
                            context,
                            Constants.IDENTITY_POOL_ID,
                            Regions.US_EAST_1);
                    String idToken = userSession.getIdToken().getJWTToken();
                    Log.d("idToken", idToken);
                    Map<String, String> logins = new HashMap<String, String>();
                    logins.put("cognito-idp.us-east-1.amazonaws.com/" + AppHelper.userPoolId, idToken);
                    Log.d("logins", String.valueOf(logins));
                    credentials.setLogins(logins);
                    Log.d("credentials", String.valueOf(credentials));
                    ddb = new AmazonDynamoDBClient(credentials);
                }

                @Override
                public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String UserId) {
                    Log.d("getAuthenticationDetail", String.valueOf(authenticationContinuation));
                }

                @Override
                public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                    Log.d("getMFACode", String.valueOf(continuation));
                }

                @Override
                public void authenticationChallenge(ChallengeContinuation continuation) {
                    Log.d("authenticationChallenge", String.valueOf(continuation));
                }

                @Override
                public void onFailure(Exception exception) {
                    Log.d("onFailure", String.valueOf(exception));
                }
            });
        }else {
            Log.d("getCredentials", "notDone");
        }
    }
}
