package com.swap.utilities;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.pinterest.android.pdk.PDKClient;
import com.swap.Instagram.InstagramApp;



import java.util.Arrays;

/**
 * Created by anjali on 14-09-2017.
 */

public class SocialMediaUtil {
    private static final String YOUTUBE_SCOPE = "https://www.googleapis.com/auth/youtube";
    //Youtube//
    public static GoogleAccountCredential mCredential;
    public static final String[] SCOPES = {"https://www.googleapis.com/auth/plus.circles.read https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/youtube"};

    //pinterest//
    public static PDKClient pdkClient;

    //Instagram//
    public static InstagramApp mApp;

    //Youtube//
    public static GoogleAccountCredential youtubeInitialization(Context mContext){
        mCredential =GoogleAccountCredential.usingOAuth2(mContext, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        return mCredential;
    }

    //Pinterest//
    public static PDKClient pinterestInitialization(Activity mContext){
        pdkClient = PDKClient.configureInstance(mContext, Constants.PINTEREST_APP_ID);
        pdkClient.getInstance().onConnect(mContext);
        return pdkClient;
    }

    //Instagram//
    public static InstagramApp instagramInitialization(Context mContext){
        mApp = new InstagramApp(mContext, Constants.INSTAGRAM_CLIENT_ID,
                Constants.INSTAGRAM_CLIENT_SECRET, Constants.INSTAGRAM_CALLBACK);
        return mApp;
    }

    // Configure sign-in to request the user's ID, email address, basic profile,
    // and readonly access to contacts.
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(new Scope(YOUTUBE_SCOPE))
            .requestEmail()
            .build();
}
