package com.swap.views.activities;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.jlubecki.soundcloud.webapi.android.SoundCloudAPI;
import com.jlubecki.soundcloud.webapi.android.auth.AuthenticationCallback;
import com.jlubecki.soundcloud.webapi.android.auth.AuthenticationStrategy;
import com.jlubecki.soundcloud.webapi.android.auth.SoundCloudAuthenticator;
import com.jlubecki.soundcloud.webapi.android.auth.browser.BrowserSoundCloudAuthenticator;
import com.jlubecki.soundcloud.webapi.android.auth.chrometabs.ChromeTabsSoundCloudAuthenticator;
import com.jlubecki.soundcloud.webapi.android.auth.models.AuthenticationResponse;
import com.jlubecki.soundcloud.webapi.android.auth.webview.WebViewSoundCloudAuthenticator;
import com.jlubecki.soundcloud.webapi.android.models.User;
import com.loopj.android.http.AsyncHttpClient;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.swap.Instagram.InstagramApp;
import com.swap.R;
import com.swap.models.SwapUser;
import com.swap.models.Users;
import com.swap.retrofitApi.APIService;
import com.swap.retrofitApi.ApiUtils;
import com.swap.utilities.Constants;
import com.swap.utilities.CustomVimeoClient;
import com.swap.utilities.Preferences;
import com.swap.utilities.SocialMediaUtil;
import com.swap.utilities.Utils;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.vimeo.networking.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.utils.URLEncodedUtils;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.UserPublic;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

import static com.swap.utilities.Constants.GITHUB_ACCESS_TOKEN_URL;
import static com.swap.utilities.Constants.GITHUB_CALLBACK;
import static com.swap.utilities.Constants.GITHUB_CLIENT_ID;
import static com.swap.utilities.Constants.GITHUB_CLIENT_SECRET;
import static com.swap.utilities.Constants.GITHUB_SCOPE;
import static com.swap.utilities.Constants.GITHUB_STATE;
import static com.swap.utilities.Constants.GITHUB_USER_DETAILS_URL;

public class ConnectSocialMediaActivity extends AppCompatActivity implements View.OnClickListener {

    private static AsyncHttpClient client = new AsyncHttpClient();
    //GoogleAccountCredential mCredential;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    //private static final String[] SCOPES = {"https://www.googleapis.com/auth/plus.circles.read https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/youtube"};

    String soundCloudUserId;
    private static final String SHOWCASE_ID = "1";
    private static final String TAG = "SocialMedia";
    private static final int SPOTIFY_REQUEST_CODE = 1990;
    private static final int VIMEO_REQUEST_CODE = 100;
    private ImageView imageViewSpotify;
    private ImageView imageViewPinterest;
    private ImageView imageViewVimeo;
    private ImageView imageViewYoutube;
    private LinearLayout ll;
    private ImageView imageViewInstagram;
    private ImageView imageViewRedit;
    private ImageView imageViewSoundCloud;
    private ImageView imageViewGithub;
    private ImageView imageViewTwitter;
    Button buttonNext;
    TextView textViewConnectSocialMedia;
    //pinInterest
    //private PDKClient pdkClient;
    WebView mWebview;
    RelativeLayout relativeLayoutWebView;
    // private InstagramApp mApp;
    String instaProfileImage = "";
    String pinterestProfileImage = "";
    String spotifyImageUrl = "";
    String soundCloudImageUrl = "";
    String gitHubImageUrl = "";
    String twitterImageUrl = "";
    private ImageView twitter;
    //twitter
    Twitter mTwitter;
    String twitterToken;
    String twitterSecret;
    TwitterAuthClient mTwitterAuthClient;
    boolean isFromSettings = false;

    //Soundcloud
    private final int SC_REQUEST_CODE_AUTHENTICATE = 1337;

    private SoundCloudAuthenticator mAuthenticator;
    CustomVimeoClient vimeoClient;
    Configuration.Builder configBuilder;
    private AuthenticationStrategy strategy;
    private AuthenticationCallback callback = new AuthenticationCallback() {
        @Override
        public void onReadyToAuthenticate(SoundCloudAuthenticator authenticator) {

            // Customize Chrome Tabs
            if (authenticator instanceof ChromeTabsSoundCloudAuthenticator) {
                int toolbarColor = ContextCompat.getColor(ConnectSocialMediaActivity.this, R.color.colorPrimary);
                int secondaryToolbarColor = ContextCompat.getColor(ConnectSocialMediaActivity.this, R.color.colorAccent);

                ChromeTabsSoundCloudAuthenticator tabsAuthenticator = (ChromeTabsSoundCloudAuthenticator) authenticator;
                CustomTabsIntent.Builder builder = tabsAuthenticator.newTabsIntentBuilder()
                        .setToolbarColor(toolbarColor)
                        .setSecondaryToolbarColor(secondaryToolbarColor);

                tabsAuthenticator.setTabsIntentBuilder(builder);
            }

            mAuthenticator = authenticator;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_social_media);

        findViewById();

        initSocialMediaResources();

        showInfo();
        Preferences.saveBooleanValue(this, Preferences.IsConnectSocialMedia, true);

        configBuilder = new Configuration.Builder(Constants.VIMEO_CLIENT_ID, Constants.VIMEO_CLIENT_SECRET, Constants.VIMEO_SCOPE).
                setCacheDirectory(this.getCacheDir()).setCodeGrantRedirectUri(Constants.VIMEO_CALLBACK);
        vimeoClient.initialize(configBuilder.build());

      /*  mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());*/
        SocialMediaUtil.mCredential = SocialMediaUtil.youtubeInitialization(this);
    }

    private void initSocialMediaResources() {
        //Instagram
        checkForInstagramData();

       /* //pinInterest
        pdkClient = PDKClient.configureInstance(this, Constants.PINTEREST_APP_ID);
        pdkClient.getInstance().onConnect(this);
        pdkClient.setDebugMode(true);
*/
        SocialMediaUtil.pdkClient = SocialMediaUtil.pinterestInitialization(this);
        //twitter
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(Constants.TWITTER_CONSUMER_KEY, Constants.TWITTER_CONSUMER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);
        mTwitterAuthClient = new TwitterAuthClient();

        checkAllSocialMediaLogins();
        twitterAuthentication();
        //authenticateSoundCloud();
    }

    private void checkAllSocialMediaLogins() {
        //twitter shared pref
        String isTwitter = Preferences.get(ConnectSocialMediaActivity.this, Preferences.twitter);
        if (isTwitter.equals("")) {
            twitter.setBackgroundResource(R.drawable.twitter_black);
        } else {
            twitter.setBackgroundResource(R.drawable.twitter_blue);
        }
        //reddit shared pref
        String isReddit = Preferences.get(ConnectSocialMediaActivity.this, Preferences.REDDIT_ACCESS_TOKEN);
        Log.d("isReddit:", isReddit);
        if (isReddit.equals("")) {
            imageViewRedit.setImageResource(R.drawable.redit_black);
        } else {
            imageViewRedit.setImageResource(R.drawable.redit_blue);
        }
        //SoundCloud shared pref
        String isSoundCloud = Preferences.get(ConnectSocialMediaActivity.this, Preferences.SOUND_CLOUD_ACCESS_TOKEN);
        if (isSoundCloud.equals("")) {
            imageViewSoundCloud.setImageResource(R.drawable.soundcloud_black);
        } else {
            imageViewSoundCloud.setImageResource(R.drawable.soundcloud_blue);
        }
        //Vimeo shared pref
        String isVimeo = Preferences.get(ConnectSocialMediaActivity.this, Preferences.VIMEO_ACCESS_TOKEN);
        if (isVimeo.equals("")) {
            imageViewVimeo.setImageResource(R.drawable.vimeo_black);
        } else {
            imageViewVimeo.setImageResource(R.drawable.vimeo_blue);
        }
        //Youtube shared pref
        boolean isYoutube = Preferences.getBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_YOUTUBE_LOGIN);
        if (!isYoutube) {
            imageViewYoutube.setImageResource(R.drawable.youtube_black);
        } else {
            imageViewYoutube.setImageResource(R.drawable.youtube_blue);
        }
        //Pinterest shared pref
        String isPinterest = Preferences.get(ConnectSocialMediaActivity.this, Preferences.PINTEREST_ACCESS_TOKEN);
        if (isPinterest.equals("")) {
            imageViewPinterest.setImageResource(R.drawable.pinterest_black);
        } else {
            imageViewPinterest.setImageResource(R.drawable.pinterest_blue);
        }
        //Github shared pref
        String isGithub = Preferences.get(ConnectSocialMediaActivity.this, Preferences.GITHUB_ACCESS_TOKEN);
        if (isGithub.equals("")) {
            imageViewGithub.setImageResource(R.drawable.github_black);
        } else {
            imageViewGithub.setImageResource(R.drawable.github_blue);
        }
        //Instagram shared pref
        String isInstagram = Preferences.get(ConnectSocialMediaActivity.this, Preferences.INSTAGRAM_ACCESS_TOKEN);
        if (isInstagram.equals("")) {
            imageViewInstagram.setImageResource(R.drawable.instagram_black);
        } else {
            imageViewInstagram.setImageResource(R.drawable.instagram_blue);
        }
        //Spotify shared pref
        String isSpotify = Preferences.get(ConnectSocialMediaActivity.this, Preferences.SPOTIFY_ACCESS_TOKEN);
        if (isSpotify.equals("")) {
            imageViewSpotify.setImageResource(R.drawable.spotify_black);
        } else {
            imageViewSpotify.setImageResource(R.drawable.spotify_blue);
        }
    }

    private void authenticateSoundCloud() {
        Utils.clearWebViewCookies(this);
        if (Preferences.get(ConnectSocialMediaActivity.this, Preferences.SOUND_CLOUD_ACCESS_TOKEN) != null && !Preferences.get(ConnectSocialMediaActivity.this, Preferences.SOUND_CLOUD_ACCESS_TOKEN).equals("")) {
            Preferences.save(ConnectSocialMediaActivity.this, Preferences.SOUND_CLOUD_ACCESS_TOKEN, "");
            Preferences.save(ConnectSocialMediaActivity.this, Preferences.SOUND_CLOUD_PROFILE_PICTURE, Constants.DEFAULT_PROFILE_PICTURE);
            Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_SOUND_CLOUD_LOGIN, false);
            imageViewSoundCloud.setImageResource(R.drawable.soundcloud_black);
        } else {
            // Prepare auth methods
            ChromeTabsSoundCloudAuthenticator tabsAuthenticator = new ChromeTabsSoundCloudAuthenticator(Constants.SOUNDCLOUD_CLIENT_ID, Constants.SOUNDCLOUD_REDIRECT, this);
            BrowserSoundCloudAuthenticator browserAuthenticator = new BrowserSoundCloudAuthenticator(Constants.SOUNDCLOUD_CLIENT_ID, Constants.SOUNDCLOUD_REDIRECT, this);
            WebViewSoundCloudAuthenticator webViewAuthenticator = new WebViewSoundCloudAuthenticator(Constants.SOUNDCLOUD_CLIENT_ID, Constants.SOUNDCLOUD_REDIRECT, this, SC_REQUEST_CODE_AUTHENTICATE);

            strategy = new AuthenticationStrategy.Builder(this)
                    //.addAuthenticator(tabsAuthenticator) // Tries this first
                    //.addAuthenticator(browserAuthenticator) // Then tries this
                    .addAuthenticator(webViewAuthenticator) // Finally tries this
                    .setCheckNetwork(true) // Makes sure the internet is connected first.
                    .build();

            //strategy.beginAuthentication(callback);
            strategy.beginAuthentication(new AuthenticationCallback() {
                @Override
                public void onReadyToAuthenticate(SoundCloudAuthenticator authenticator) {
                    authenticator.launchAuthenticationFlow(); // launch immediately
                    // ... or ...
                    //MyActivity.this.authenticator = authenticator; // save to launch when ready
                }
            });
        }
    }

    private void twitterAuthentication() {
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkConnected(ConnectSocialMediaActivity.this)) {
                    twitterLogin();
                } else {
                    Toast.makeText(ConnectSocialMediaActivity.this, R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void twitterLogin() {
        Utils.clearWebViewCookies(ConnectSocialMediaActivity.this);
        if (Preferences.get(ConnectSocialMediaActivity.this, Preferences.twitter) != null && !Preferences.get(ConnectSocialMediaActivity.this, Preferences.twitter).equals("")) {
            Preferences.save(ConnectSocialMediaActivity.this, Preferences.twitter, "");
            Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_TWITTER_LOGIN, false);
            twitter.setImageResource(R.drawable.twitter_black);
        } else {
            mTwitterAuthClient.authorize(ConnectSocialMediaActivity.this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> twitterSessionResult) {
                    // Success
                    // Do something with result, which provides a TwitterSession for making API
                    TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                    TwitterAuthToken authToken = session.getAuthToken();
                    twitterToken = authToken.token;
                    twitterSecret = authToken.secret;
                    final String twiterUserId = String.valueOf(session.getUserId());
                    getTwitterUserImage();
                    Preferences.save(ConnectSocialMediaActivity.this, Preferences.twitter, authToken.token);
                    twitter.setImageResource(R.drawable.twitter_blue);
                    if (twiterUserId != null) {
                        Log.d("id: ", twiterUserId);
                        Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_TWITTER_LOGIN, true);
                        Runnable runnable = new Runnable() {
                            public void run() {
                                try {
                                    //DynamoDB calls go here
                                    Users users = SwapUser.getUser(ConnectSocialMediaActivity.this, Preferences.get(ConnectSocialMediaActivity.this, Preferences.USERNAME));
                                    users.setTwitter_ID(twiterUserId);
                                    SwapUser.updateUsers(ConnectSocialMediaActivity.this, users);
                                    SwapUser.incrementPoints(ConnectSocialMediaActivity.this, users, 5);
                                } catch (Exception e) {
                                }
                            }
                        };
                        Thread mythread = new Thread(runnable);
                        mythread.start();
                    }
                }

                @Override
                public void failure(TwitterException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void getTwitterUserImage() {

        Call<com.twitter.sdk.android.core.models.User> user = TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(false, false, false);
        user.enqueue(new com.twitter.sdk.android.core.Callback<com.twitter.sdk.android.core.models.User>() {
            @Override
            public void success(Result<com.twitter.sdk.android.core.models.User> userResult) {

                twitterImageUrl = userResult.data.profileImageUrl;
                Preferences.save(ConnectSocialMediaActivity.this, Preferences.TWITTER_PROFILE_PICTURE, twitterImageUrl);
                String photoUrlBiggerSize = userResult.data.profileImageUrl.replace("_normal", "_bigger");
                String photoUrlMiniSize = userResult.data.profileImageUrl.replace("_normal", "_mini");
                String photoUrlOriginalSize = userResult.data.profileImageUrl.replace("_normal", "");
            }

            @Override
            public void failure(TwitterException exc) {
                Log.d("TwitterKit", "Verify Credentials Failure", exc);
            }
        });

    }


    private void findViewById() {
        relativeLayoutWebView = (RelativeLayout) findViewById(R.id.relativeLayoutWebView);
        mWebview = (WebView) findViewById(R.id.webViewLogin);
        relativeLayoutWebView.setVisibility(View.GONE);
        imageViewSpotify = (ImageView) findViewById(R.id.imageViewSpotify);
        imageViewPinterest = (ImageView) findViewById(R.id.imageViewPinterest);
        imageViewVimeo = (ImageView) findViewById(R.id.imageViewVimeo);
        imageViewYoutube = (ImageView) findViewById(R.id.imageViewYoutube);
        ll = (LinearLayout) findViewById(R.id.ll);
        imageViewInstagram = (ImageView) findViewById(R.id.imageViewInstagram);
        imageViewRedit = (ImageView) findViewById(R.id.imageViewRedit);
        imageViewSoundCloud = (ImageView) findViewById(R.id.imageViewSoundCloud);
        imageViewGithub = (ImageView) findViewById(R.id.imageViewGithub);
        twitter = (ImageView) findViewById(R.id.twitter_login_button);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        textViewConnectSocialMedia = (TextView) findViewById(R.id.textViewConnectSocialMedia);

        buttonNext.setOnClickListener(this);
        imageViewSpotify.setOnClickListener(this);
        imageViewPinterest.setOnClickListener(this);
        imageViewVimeo.setOnClickListener(this);
        imageViewYoutube.setOnClickListener(this);
        imageViewInstagram.setOnClickListener(this);
        imageViewRedit.setOnClickListener(this);
        imageViewSoundCloud.setOnClickListener(this);
        imageViewGithub.setOnClickListener(this);
        setFontOnViews();
    }

    private void setFontOnViews() {
        buttonNext.setTypeface(Utils.setFont(this, "lantinghei.ttf"));
        textViewConnectSocialMedia.setTypeface(Utils.setFont(this, "lantinghei.ttf"));
    }

    private void showInfo() {
        try {
            int showcaseId = 0;
            if (getIntent() != null) {
                showcaseId = getIntent().getIntExtra("ShowcaseKey", 0);
                if (getIntent().getExtras().containsKey("isFromSettings")) {
                    isFromSettings = getIntent().getExtras().getBoolean("isFromSettings");
                }
            }
            imageViewInstagram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signInWithInstagram();
                }
            });
            // single example
            int withDelay = 5;
            new MaterialShowcaseView.Builder(this)
                    .setTarget(imageViewInstagram)
                    .setDismissText("GOT IT")
                    .setContentText("Tap To Connect")
                    .setDelay(withDelay) // optional but starting animations immediately in onCreate can make them choppy
                    .singleUse(String.valueOf(showcaseId)) // provide a unique ID used to ensure it is only shown once
                    .setTargetTouchable(true)
                    .show();
        } catch (Exception e) {
        }
    }


    @Override
    public void onClick(View view) {
        final int id = view.getId();
        switch (id) {
            case R.id.buttonNext:

                if (Preferences.get(ConnectSocialMediaActivity.this, Preferences.INSTAGRAM_PROFILE_PICTURE).isEmpty()) {
                    Preferences.save(ConnectSocialMediaActivity.this, Preferences.INSTAGRAM_PROFILE_PICTURE, Constants.DEFAULT_PROFILE_PICTURE);
                }
                if (Preferences.get(ConnectSocialMediaActivity.this, Preferences.SOUND_CLOUD_PROFILE_PICTURE).isEmpty()) {
                    Preferences.save(ConnectSocialMediaActivity.this, Preferences.SOUND_CLOUD_PROFILE_PICTURE, Constants.DEFAULT_PROFILE_PICTURE);
                }
                if (Preferences.get(ConnectSocialMediaActivity.this, Preferences.SPOTIFY_PROFILE_PICTURE).isEmpty()) {
                    Preferences.save(ConnectSocialMediaActivity.this, Preferences.SPOTIFY_PROFILE_PICTURE, Constants.DEFAULT_PROFILE_PICTURE);
                }
                if (Preferences.get(ConnectSocialMediaActivity.this, Preferences.GITHUB_PROFILE_PICTURE).isEmpty()) {
                    Preferences.save(ConnectSocialMediaActivity.this, Preferences.GITHUB_PROFILE_PICTURE, Constants.DEFAULT_PROFILE_PICTURE);
                }

                if (pinterestProfileImage.isEmpty()) {
                    pinterestProfileImage = Constants.DEFAULT_PROFILE_PICTURE;
                }
                if (twitterImageUrl.isEmpty()) {
                    twitterImageUrl = Constants.DEFAULT_PROFILE_PICTURE;
                }
                Intent i = new Intent(ConnectSocialMediaActivity.this, SelectPictureActivity.class);
                i.putExtra("isFromSettings", isFromSettings);
                i.putExtra("pinterestProfileImage", pinterestProfileImage);
                i.putExtra("twitterImageUrl", twitterImageUrl);
                startActivity(i);
                break;

            case R.id.imageViewSpotify:
                if (Utils.isNetworkConnected(this)) {
                    spotifyLogin();
                } else {
                    Toast.makeText(this, R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.imageViewPinterest:
                if (Utils.isNetworkConnected(this)) {

                    onPinInterestLogin();
                } else {
                    Toast.makeText(this, R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.imageViewVimeo:
                if (Utils.isNetworkConnected(this)) {
                    vimeoLogin();
                } else {
                    Toast.makeText(this, R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.imageViewYoutube:
                if (Utils.isNetworkConnected(this)) {
                    authenticateYoutube();
                } else {
                    Toast.makeText(this, R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.imageViewInstagram:
                if (Utils.isNetworkConnected(this)) {
                    signInWithInstagram();
                } else {
                    Toast.makeText(this, R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.imageViewRedit:
                if (Utils.isNetworkConnected(this)) {
                    redditLogin();
                } else {
                    Toast.makeText(this, R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.imageViewSoundCloud:
                if (Utils.isNetworkConnected(this)) {
                    authenticateSoundCloud();
                } else {
                    Toast.makeText(this, R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.imageViewGithub:
                if (Utils.isNetworkConnected(this)) {
                    gitHubLogin();
                } else {
                    Toast.makeText(this, R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void pinInterestLoginAlert() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle(R.string.pintrestAlertTitle);
        builder.setMessage(R.string.pintrestAlertMessage);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                pintrestLoginProcessStart();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void pintrestLoginProcessStart() {
        List scopes = new ArrayList<String>();
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_RELATIONSHIPS);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_RELATIONSHIPS);
        try {
            SocialMediaUtil.pdkClient.login(this, scopes, new PDKCallback() {
                @Override
                public void onSuccess(PDKResponse response) {

                    Log.d(getClass().getName(), response.getData().toString());
                    if (response.getUser().getImageUrl() != null) {
                        pinterestProfileImage = response.getUser().getImageUrl();
                        Log.d("pinterestProfileImage", pinterestProfileImage);
                    }
                    JSONObject data = (JSONObject) response.getData();
                    //onLoginSuccess();
                    imageViewPinterest.setImageResource(R.drawable.pinterest_blue);
                    // Toast.makeText(ConnectSocialMediaActivity.this, "Pinterest login success", Toast.LENGTH_SHORT).show();

                    if (SocialMediaUtil.pdkClient.getAccessToken() != null) {
                        Preferences.save(ConnectSocialMediaActivity.this, Preferences.PINTEREST_ACCESS_TOKEN, SocialMediaUtil.pdkClient.getAccessToken());
                    }
                    try {
                        final String pinterestId = data.getString("id");
                        if (pinterestId != null) {
                            Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_PINTEREST_LOGIN, true);
                            Log.d("id: ", pinterestId);
                            Runnable runnable = new Runnable() {
                                public void run() {
                                    try {
                                        //DynamoDB calls go here
                                        Users users = SwapUser.getUser(ConnectSocialMediaActivity.this, Preferences.get(ConnectSocialMediaActivity.this, Preferences.USERNAME));
                                        users.setPinterest_ID(pinterestId);
                                        SwapUser.updateUsers(ConnectSocialMediaActivity.this, users);
                                    } catch (Exception e) {
                                    }
                                }
                            };
                            Thread mythread = new Thread(runnable);
                            mythread.start();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(PDKException exception) {
                    Log.e(getClass().getName(), exception.getDetailMessage());
                    Toast.makeText(ConnectSocialMediaActivity.this, "Pinterest login failure", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void authenticateYoutube() {
        Utils.clearWebViewCookies(this);
        if (Preferences.get(this, Preferences.YOUTUBE_ACCOUNT_NAME) != null && !Preferences.get(this, Preferences.YOUTUBE_ACCOUNT_NAME).equals("")) {
            Preferences.save(this, Preferences.YOUTUBE_ACCOUNT_NAME, "");
            Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_YOUTUBE_LOGIN, false);
            SocialMediaUtil.mCredential.setSelectedAccount(null);
            imageViewYoutube.setImageResource(R.drawable.youtube_black);
        } else {
            youtubeLogin();
        }
    }

    private void youtubeLogin() {

        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (SocialMediaUtil.mCredential.getSelectedAccountName() == null || SocialMediaUtil.mCredential.getSelectedAccountName().isEmpty()) {
            chooseAccount();
        } else if (!Utils.isNetworkConnected(this)) {
            Toast.makeText(this, R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
        } else {
            Log.d("youtube user", SocialMediaUtil.mCredential.getSelectedAccountName());
            new MakeRequestTask(SocialMediaUtil.mCredential).execute();
        }

    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.youtube.YouTube mService = null;
        private Exception mLastError = null;
        ProgressDialog pDialog = new ProgressDialog(ConnectSocialMediaActivity.this);
        String youtubeUser = Preferences.get(ConnectSocialMediaActivity.this, Preferences.YOUTUBE_ACCOUNT_NAME);

        MakeRequestTask(GoogleAccountCredential credential) {

          /*  mCredential = GoogleAccountCredential.usingOAuth2(
                    getApplicationContext(), Arrays.asList(SocialMediaUtil.SCOPES))
                    .setBackOff(new ExponentialBackOff());*/
            SocialMediaUtil.mCredential = SocialMediaUtil.youtubeInitialization(ConnectSocialMediaActivity.this);

            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Swap")
                    .build();

        }

        /**
         * Background task to call YouTube Data API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi(youtubeUser);
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch information about the "GoogleDevelopers" YouTube channel.
         *
         * @param youtubeUser
         * @return List of Strings containing information about the channel.
         * @throws IOException
         */
        private List<String> getDataFromApi(String youtubeUser) throws IOException {
            // Get a list of up to 10 files.
            List<String> channelInfo = new ArrayList<String>();
            ChannelListResponse result = mService.channels().list("snippet,contentDetails,statistics")
                    .setMine(true)
                    .execute();
            List<Channel> channels = result.getItems();
            if (channels != null) {

                final Channel channel = channels.get(0);
                channelInfo.add("This channel's ID is " + channel.getId() + ". " +
                        "Its title is '" + channel.getSnippet().getTitle() + ", " +
                        "and it has " + channel.getStatistics().getViewCount() + " views.");
                if (channel.getId() != null) {
                    Log.d("channel id: ", channel.getId());

                    Runnable runnable = new Runnable() {
                            public void run() {
                            try {
                                //DynamoDB calls go here
                                Users users = SwapUser.getUser(ConnectSocialMediaActivity.this, Preferences.get(ConnectSocialMediaActivity.this, Preferences.USERNAME));
                                users.setYoutube_ID(channel.getId());
                                SwapUser.updateUsers(ConnectSocialMediaActivity.this, users);
                                SwapUser.incrementPoints(ConnectSocialMediaActivity.this, users, 5);
                            } catch (Exception e) {
                            }
                        }
                    };

                    Thread mythread = new Thread(runnable);
                    mythread.start();
                }
            }
            return channelInfo;
        }


        @Override
        protected void onPreExecute() {
            //mOutputText.setText("");
            // mProgress.show();
            try {
                pDialog.setMessage(getString(R.string.loading));
                pDialog.setCancelable(false);
                pDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(List<String> output) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            /*if (output == null || output.size() == 0) {
                //  mOutputText.setText("No results returned.");
            } else {
                output.add(0, "Data retrieved using the YouTube Data API:");
                imageViewYoutube.setImageResource(R.drawable.youtube_blue);
                Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_YOUTUBE_LOGIN, true);
                //mOutputText.setText(TextUtils.join("\n", output));
            }*/

            if (output != null && output.size() > 0) {
                output.add(0, "Data retrieved using the YouTube Data API:");
                imageViewYoutube.setImageResource(R.drawable.youtube_blue);
                Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_YOUTUBE_LOGIN, true);
            }

        }

        @Override
        protected void onCancelled() {
            //  mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
                    /*mOutputText.setText("The following error occurred:\n"
                            + mLastError.getMessage());*/
                }
            } else {
                //mOutputText.setText("Request cancelled.");
            }
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    private void chooseAccount() {
        if (isPermissionGranted()) {
            String accountName = Preferences.get(this, Preferences.YOUTUBE_ACCOUNT_NAME);
            if (accountName != null && !accountName.equals("")) {
                SocialMediaUtil.mCredential.setSelectedAccountName(accountName);
                getYoutubeToken();
                youtubeLogin();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        SocialMediaUtil.mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            requestPermission();
        }

    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_PERMISSION_GET_ACCOUNTS);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_GET_ACCOUNTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    recreate();
                }
            } else {
                Toast.makeText(this, "This app needs to access your Google account (via Contacts).", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    private void vimeoLogin() {
        Utils.clearWebViewCookies(this);
        if (Preferences.get(this, Preferences.VIMEO_ACCESS_TOKEN) != null && !Preferences.get(this, Preferences.VIMEO_ACCESS_TOKEN).equals("")) {
            Preferences.save(this, Preferences.VIMEO_ACCESS_TOKEN, "");
            Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_VIMEO_LOGIN, false);
            imageViewVimeo.setImageResource(R.drawable.vimeo_black);
        } else {
            String uri = vimeoClient.getInstance().getCodeGrantAuthorizationURI();
            Log.d("vimeoURL:", uri);
            /*
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(browserIntent);*/
            OnSignUp_Click(uri);
        }
    }


    private void redditLogin() {
        Utils.clearWebViewCookies(this);
        if (Preferences.get(this, Preferences.REDDIT_ACCESS_TOKEN) != null && !Preferences.get(this, Preferences.REDDIT_ACCESS_TOKEN).equals("")) {
            imageViewRedit.setImageResource(R.drawable.redit_black);
            Preferences.save(this, Preferences.REDDIT_ACCESS_TOKEN, "");
            Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_REDDIT_LOGIN, false);
        } else {
            String url = String.format(Constants.REDDIT_AUTH_URL, Constants.REDDIT_CLIENT_ID, Constants.REDDIT_STATE, Constants.REDDIT_REDIRECT_URI);
            OnSignUp_Click(url);
        }
    }


    public void OnSignUp_Click(String urlLogin) {
        relativeLayoutWebView.setVisibility(View.VISIBLE);
        mWebview.clearCache(true);
        mWebview.clearHistory();
        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        mWebview.getSettings().setSupportMultipleWindows(true);
        mWebview.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cmsg) {
                /* process JSON */
                cmsg.message();
                return true;
            }
        });

        mWebview.setWebViewClient(new WebViewClient() {

            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                Log.e("Load Signup page", description);
                relativeLayoutWebView.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("response url", url);
                if (url != null) {
                    if (url.contains("code")) {
                        Uri uri = Uri.parse(url);
                        String code = uri.getQueryParameter("code");
                        if (code != null) {
                            relativeLayoutWebView.setVisibility(View.GONE);
                            String state = uri.getQueryParameter("state");
                            if (state.equals(Constants.REDDIT_STATE)) {
                                getRedditAccessToken(code);
                                Preferences.save(ConnectSocialMediaActivity.this, Preferences.REDDIT_CODE, code);
                                imageViewRedit.setImageResource(R.drawable.redit_blue);
                            } else if (state.equals(Constants.VIMEO_STATE)) {
                                getVimeoAccessToken(code);
                                imageViewVimeo.setImageResource(R.drawable.vimeo_blue);
                            } else if (state.equals(Constants.GITHUB_STATE)) {
                                getGitHubAccessToken(code);
                                imageViewGithub.setImageResource(R.drawable.github_blue);
                            }
                            view.loadUrl(null);
                            view.clearCache(true);
                            view.clearHistory();
                        }
                    }
                }
                // view.loadUrl("javascript:console.log(document.body.getElementsByTagName('pre')[0].innerHTML);");
            }
        });

        mWebview.loadUrl(urlLogin);

    }

    private void getVimeoAccessToken(String code) {
        Log.v("Reddit code: ", code);
        OkHttpClient client = new OkHttpClient();

        String authString = Constants.VIMEO_CLIENT_ID + ":" + Constants.VIMEO_CLIENT_SECRET;
        String encodedAuthString = Base64.encodeToString(authString.getBytes(),
                Base64.NO_WRAP);

        Request request = new Request.Builder()
                .addHeader("Authorization", "Basic " + encodedAuthString)
                .url(Constants.VIMEO_ACCESS_TOKEN_URL)
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
                        "grant_type=authorization_code&code=" + code +
                                "&redirect_uri=" + Constants.VIMEO_CALLBACK))
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e(TAG, "ERROR: " + e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String json = response.body().string();

                JSONObject data = null;
                try {
                    data = new JSONObject(json);
                    if (data != null) {
                        String accessToken = data.optString("access_token");
                        String refreshToken = data.optString("refresh_token");
                        JSONObject userObject = data.getJSONObject("user");
                        String userProfileUri = userObject.optString("uri");
                        String[] userProfileUriArray = userProfileUri.split("/");
                        String userId = userProfileUriArray[2];
                        Log.v("Vimeo User Id : ", userId);
                        Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_VIMEO_LOGIN, true);
                        JSONArray userPicturesArray = userObject.getJSONArray("pictures");
                        String imageUrl = "";
                        if (userPicturesArray.length() > 0)
                            imageUrl = userPicturesArray.getJSONObject(0).getString("link");
                        Log.v("Vimeo image url : ", imageUrl);
                        Preferences.save(ConnectSocialMediaActivity.this, Preferences.VIMEO_ACCESS_TOKEN, accessToken);
                        Log.d(TAG, "Vimeo Access Token = " + accessToken);
                        Log.d(TAG, "Vimeo Refresh Token = " + refreshToken);

                        //Save userId to DB
                        //DynamoDB calls go here
                        Users users = SwapUser.getUser(ConnectSocialMediaActivity.this, Preferences.get(ConnectSocialMediaActivity.this, Preferences.USERNAME));
                        users.setVimeoID(userId);
                        SwapUser.updateUsers(ConnectSocialMediaActivity.this, users);
                        SwapUser.incrementPoints(ConnectSocialMediaActivity.this, users, 5);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void gitHubLogin() {
        Utils.clearWebViewCookies(this);
        if (Preferences.get(this, Preferences.GITHUB_ACCESS_TOKEN) != null && !Preferences.get(this, Preferences.GITHUB_ACCESS_TOKEN).equals("")) {
            Preferences.save(this, Preferences.GITHUB_ACCESS_TOKEN, "");
            Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_GITHUB_LOGIN, false);
            Preferences.save(ConnectSocialMediaActivity.this, Preferences.GITHUB_PROFILE_PICTURE, Constants.DEFAULT_PROFILE_PICTURE);
            imageViewGithub.setImageResource(R.drawable.github_black);
        } else {
            String url = String.format(Constants.GITHUB_AUTH_URL, GITHUB_CLIENT_ID, GITHUB_CALLBACK, GITHUB_SCOPE, GITHUB_STATE);
            OnSignUp_Click(url);
        }
    }

    public void getGitHubAccessToken(String code) {
        try {
            APIService mAPIService = ApiUtils.get(this, "Accept", "application/json");
            mAPIService.getGitHubDeviceToken(GITHUB_ACCESS_TOKEN_URL, GITHUB_CLIENT_ID, GITHUB_CLIENT_SECRET, code, GITHUB_CALLBACK, GITHUB_STATE)
                    .enqueue(new retrofit2.Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    String json = response.body().string();
                                    JSONObject data = new JSONObject(json);
                                    String accessToken = data.optString("access_token");
                                    Log.d("git accessToken", accessToken);
                                    Preferences.save(ConnectSocialMediaActivity.this, Preferences.GITHUB_ACCESS_TOKEN, accessToken);
                                    if (accessToken != null) {
                                        try {
                                            getGitHubUser(accessToken);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
        } catch (Exception e) {
            Log.d("Exception ", e.getMessage());
        }
    }

    String githubUserName;

    private void getGitHubUser(String token) {
        try {
            APIService mAPIService = ApiUtils.get(this, "Accept", "application/json");
            String url = GITHUB_USER_DETAILS_URL + token;
            mAPIService.getGitHubUsersDetails(url)
                    .enqueue(new retrofit2.Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    String json = response.body().string();
                                    JSONObject data = new JSONObject(json);
                                    githubUserName = data.getString("login");
                                    gitHubImageUrl = data.getString("avatar_url");
                                    if (gitHubImageUrl != null) {
                                        Preferences.save(ConnectSocialMediaActivity.this, Preferences.GITHUB_PROFILE_PICTURE, gitHubImageUrl);
                                    }
                                    //final String id = data.getString("id");
                                    if (githubUserName != null) {
                                        Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_GITHUB_LOGIN, true);
                                        Runnable runnable = new Runnable() {
                                            public void run() {
                                                try {
                                                    Users users = SwapUser.getUser(ConnectSocialMediaActivity.this, Preferences.get(ConnectSocialMediaActivity.this, Preferences.USERNAME));
                                                    if (users != null) {
                                                        users.setGithubID(githubUserName);
                                                        SwapUser.updateUsers(ConnectSocialMediaActivity.this, users);
                                                        SwapUser.incrementPoints(ConnectSocialMediaActivity.this, users, 5);
                                                    }
                                                } catch (Exception e) {
                                                }
                                            }
                                        };
                                        Thread mythread = new Thread(runnable);
                                        mythread.start();
                                        imageViewGithub.setImageResource(R.drawable.github_blue);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
        } catch (Exception e) {
            Log.d("Exception ", e.getMessage());
        }
    }

    private void onPinInterestLogin() {
        Utils.clearWebViewCookies(this);
        if (Preferences.get(this, Preferences.PINTEREST_ACCESS_TOKEN) != null && !Preferences.get(this, Preferences.PINTEREST_ACCESS_TOKEN).equals("")) {
            SocialMediaUtil.pdkClient.getInstance().logout();
            Preferences.save(ConnectSocialMediaActivity.this, Preferences.PINTEREST_ACCESS_TOKEN, "");
            Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_PINTEREST_LOGIN, false);
            imageViewPinterest.setImageResource(R.drawable.pinterest_black);
        } else {
            pinInterestLoginAlert();
            /*List scopes = new ArrayList<String>();
            scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
            scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);
            scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_RELATIONSHIPS);
            scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_RELATIONSHIPS);
            try {
                SocialMediaUtil.pdkClient.login(this, scopes, new PDKCallback() {
                    @Override
                    public void onSuccess(PDKResponse response) {
            
                        Log.d(getClass().getName(), response.getData().toString());
                        pinterestProfileImage = response.getUser().getImageUrl();
                        Log.d("pinterestProfileImage",pinterestProfileImage);
                        JSONObject data = (JSONObject) response.getData();
                        //onLoginSuccess();
                        imageViewPinterest.setImageResource(R.drawable.pinterest_blue);
                        // Toast.makeText(ConnectSocialMediaActivity.this, "Pinterest login success", Toast.LENGTH_SHORT).show();

                        if (SocialMediaUtil.pdkClient.getAccessToken() != null) {
                            Preferences.save(ConnectSocialMediaActivity.this, Preferences.PINTEREST_ACCESS_TOKEN, SocialMediaUtil.pdkClient.getAccessToken());
                        }
                        try {
                            final String pinterestId = data.getString("id");
                            if (pinterestId != null) {
                                Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_PINTEREST_LOGIN, true);
                                Log.d("id: ", pinterestId);
                                Runnable runnable = new Runnable() {
                                    public void run() {
                                        try {
                                            //DynamoDB calls go here
                                            Users users = SwapUser.getUser(ConnectSocialMediaActivity.this, Preferences.get(ConnectSocialMediaActivity.this, Preferences.USERNAME));
                                            users.setPinterest_ID(pinterestId);
                                            SwapUser.updateUsers(ConnectSocialMediaActivity.this, users);
                                        } catch (Exception e) {
                                        }
                                    }
                                };
                                Thread mythread = new Thread(runnable);
                                mythread.start();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
            
                    }
        
                    @Override
                    public void onFailure(PDKException exception) {
                        Log.e(getClass().getName(), exception.getDetailMessage());
                        Toast.makeText(ConnectSocialMediaActivity.this, "Pinterest login failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }*/
        }
    }

    private void signInWithInstagram() {
        checkForInstagramData();
        SocialMediaUtil.mApp.resetAccessToken();
        Utils.clearWebViewCookies(this);
        if (Preferences.get(this, Preferences.INSTAGRAM_ACCESS_TOKEN) != null && !Preferences.get(this, Preferences.INSTAGRAM_ACCESS_TOKEN).equals("")) {
            Preferences.save(this, Preferences.INSTAGRAM_ACCESS_TOKEN, "");
            Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_INSTAGRAM_LOGIN, false);
            Preferences.save(this, Preferences.INSTAGRAM_PROFILE_PICTURE, Constants.DEFAULT_PROFILE_PICTURE);
            imageViewInstagram.setImageResource(R.drawable.instagram_black);
        } else {
            //checkForInstagramData();
            SocialMediaUtil.mApp.authorize();
        }
    }

    private void checkForInstagramData() {
        //Instagram
       /* mApp = new InstagramApp(this, Constants.INSTAGRAM_CLIENT_ID,
                Constants.INSTAGRAM_CLIENT_SECRET, Constants.INSTAGRAM_CALLBACK);*/
        SocialMediaUtil.mApp = SocialMediaUtil.instagramInitialization(this);
        SocialMediaUtil.mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
                if (SocialMediaUtil.mApp.getProfilePicture() != null) {
                    Log.v("profile_picture", SocialMediaUtil.mApp.getProfilePicture());
                    //instaProfileImage = SocialMediaUtil.mApp.getProfilePicture();
                    Preferences.save(ConnectSocialMediaActivity.this, Preferences.INSTAGRAM_PROFILE_PICTURE, SocialMediaUtil.mApp.getProfilePicture());
                }
                imageViewInstagram.setImageResource(R.drawable.instagram_blue);
                Preferences.save(ConnectSocialMediaActivity.this, Preferences.INSTAGRAM_ACCESS_TOKEN, SocialMediaUtil.mApp.getTOken());
                Runnable runnable = new Runnable() {
                    public void run() {
                        try {
                            //DynamoDB calls go here
                            Users users = SwapUser.getUser(ConnectSocialMediaActivity.this, Preferences.get(ConnectSocialMediaActivity.this, Preferences.USERNAME));
                            users.setInstagram_ID(SocialMediaUtil.mApp.getId());
                            Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_INSTAGRAM_LOGIN, true);
                            SwapUser.updateUsers(ConnectSocialMediaActivity.this, users);
                            SwapUser.incrementPoints(ConnectSocialMediaActivity.this, users, 5);
                        } catch (Exception e) {
                        }
                    }
                };
                Thread mythread = new Thread(runnable);
                mythread.start();
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(ConnectSocialMediaActivity.this, error, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        SocialMediaUtil.pdkClient.onOauthResponse(requestCode, resultCode, data);

        switch (requestCode) {
            case SC_REQUEST_CODE_AUTHENTICATE:
                handleAuthRequestCode(requestCode, resultCode, data);
                break;

            case SPOTIFY_REQUEST_CODE:
                com.spotify.sdk.android.authentication.AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);
                if (response.getType() == com.spotify.sdk.android.authentication.AuthenticationResponse.Type.CODE) {
                    getSpotifyTokens(response);
                }
                break;
            case VIMEO_REQUEST_CODE:
                Log.i(TAG, " result: " + requestCode);
                if (data != null) {
                    Log.d("data", String.valueOf(data));
                }
                break;
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(getApplicationContext(),
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.", Toast.LENGTH_LONG).show();

                } else {
                    youtubeLogin();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        Preferences.save(ConnectSocialMediaActivity.this, Preferences.YOUTUBE_ACCOUNT_NAME, accountName);
                        SocialMediaUtil.mCredential.setSelectedAccountName(accountName);
                        getYoutubeToken();

                        youtubeLogin();
                    }
                }
                break;

            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    youtubeLogin();
                }
                break;

            default:
                Log.i(TAG, "Other activity result: " + requestCode);
                break;
        }

    }

    private void getYoutubeToken() {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    if (SocialMediaUtil.mCredential != null && SocialMediaUtil.mCredential.getToken() != null) {
                        if (SocialMediaUtil.mCredential.getToken() != null) {
                            Log.d("youtubeToken", SocialMediaUtil.mCredential.getToken());
                            Preferences.save(ConnectSocialMediaActivity.this, Preferences.YOUTUBE_ACCESS_TOKEN, SocialMediaUtil.mCredential.getToken());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAllSocialMediaLogins();
        if (getIntent() != null && getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_VIEW)) {
            Uri uri = getIntent().getData();
            if (uri.getQueryParameter("error") != null) {
                String error = uri.getQueryParameter("error");
                Log.e(TAG, "An error has occurred : " + error);
            } else {
                if (uri.toString().contains("pdk")) {
                    getPdkValue(uri);
                }
                String state = uri.getQueryParameter("state");
                if (state != null) {
                    getTokenFromIntent(getIntent());
                }
            }
        }

    }


    private void getPdkValue(Uri uri) {
        StringTokenizer tokens = new StringTokenizer(uri.toString(), "?");
        String first = tokens.nextToken();
        String second = tokens.nextToken();
        HashMap<String, String> parameters = new HashMap<String, String>();
        List<NameValuePair> params = URLEncodedUtils.parse(second, Charset.defaultCharset());
        for (NameValuePair nameValuePair : params) {
            parameters.put(nameValuePair.getName(), nameValuePair.getValue());
            if (nameValuePair.getName() != null && nameValuePair.getName().equals("access_token") && nameValuePair.getValue() != null) {
                imageViewPinterest.setImageResource(R.drawable.pinterest_blue);
            }
        }
    }

    private void getRedditAccessToken(String code) {
        Log.v("Reddit code: ", code);
        OkHttpClient client = new OkHttpClient();

        String authString = Constants.REDDIT_CLIENT_ID + ":";
        String encodedAuthString = Base64.encodeToString(authString.getBytes(),
                Base64.NO_WRAP);

        Request request = new Request.Builder()
                .addHeader("User-Agent", "Swap")
                .addHeader("Authorization", "Basic " + encodedAuthString)
                .url(Constants.REDDIT_ACCESS_TOKEN_URL)
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
                        "grant_type=authorization_code&code=" + code +
                                "&redirect_uri=" + Constants.REDDIT_REDIRECT_URI))
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e(TAG, "ERROR: " + e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String json = response.body().string();

                JSONObject data = null;
                try {
                    data = new JSONObject(json);
                    String accessToken = data.optString("access_token");
                    String refreshToken = data.optString("refresh_token");
                    Preferences.save(ConnectSocialMediaActivity.this, Preferences.REDDIT_ACCESS_TOKEN, accessToken);
                    if (accessToken != null) {
                        try {
                            getRedditUser();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getRedditUser() {
        String requestUrl = "https://oauth.reddit.com/api/v1/me.json";
        requestUrl = requestUrl.replace(" ", "%20");
        APIService mAPIService = ApiUtils.get(this);

        mAPIService.getRedditUsersDetails(requestUrl).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String json = null;
                JSONObject jsonObject = null;
                try {
                    json = response.body().string();
                    jsonObject = new JSONObject(json);

                    if (jsonObject.has("name")) {
                        final String name = jsonObject.getString("name");
                        Log.d("name", String.valueOf(name));
                        if (name != null) {
                            Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_REDDIT_LOGIN, true);
                            Log.d("id: ", name);
                            Runnable runnable = new Runnable() {
                                public void run() {
                                    try {
                                        //DynamoDB calls go here
                                        Users users = SwapUser.getUser(ConnectSocialMediaActivity.this, Preferences.get(ConnectSocialMediaActivity.this, Preferences.USERNAME));
                                        users.setRedditID(name);
                                        SwapUser.updateUsers(ConnectSocialMediaActivity.this, users);
                                        SwapUser.incrementPoints(ConnectSocialMediaActivity.this, users, 5);
                                    } catch (Exception e) {
                                    }
                                }
                            };
                            Thread mythread = new Thread(runnable);
                            mythread.start();
                            imageViewRedit.setImageResource(R.drawable.redit_blue);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "ERROR: " + "ERROR");
            }
        });

    }


    @Override
    public void onDestroy() {
        if (mAuthenticator != null) {
            mAuthenticator.release(); // Need to call this if using Chrome Tabs.
        }

        super.onDestroy();
    }

    void getTokenFromIntent(Intent intent) {
        String intentInfo = intent != null ? intent.getDataString() : "Null";
        Log.i(TAG, "Trying to get token from intent data: " + intentInfo);

        if (strategy != null && strategy.canAuthenticate(intent)) {
            strategy.getToken(intent, Constants.SOUNDCLOUD_CLIENT_SECRET, new AuthenticationStrategy.ResponseCallback() {
                @Override
                public void onAuthenticationResponse(AuthenticationResponse response) {
                    switch (response.getType()) {
                        case TOKEN:
                            saveToken(response.access_token);
                            //openPlayer();
                            break;

                        default:
                            Log.e(TAG, response.toString());
                            break;
                    }
                }

                @Override
                public void onAuthenticationFailed(Throwable throwable) {
                    Log.e(TAG, throwable.getMessage());
                }
            });
        }
    }

    private void handleAuthRequestCode(int requestCode, int resultCode, Intent data) {
        if (requestCode != SC_REQUEST_CODE_AUTHENTICATE) {
            String error = "Code: " + requestCode + " should be: " + SC_REQUEST_CODE_AUTHENTICATE;
            Log.e(TAG, error);
            return;
        }

        if (resultCode == RESULT_OK) {
            getTokenFromIntent(data);
        } else if (resultCode == RESULT_CANCELED) {
            Log.w(TAG, "Authentication was canceled.");
        } else {
            Log.w(TAG, "Unhandled result code: " + resultCode);
        }
    }

    private void saveToken(String token) {
        Log.i(TAG, "Token SoundCloud -  " + token);
        Preferences.save(ConnectSocialMediaActivity.this, Preferences.SOUND_CLOUD_ACCESS_TOKEN, token);
        imageViewSoundCloud.setImageResource(R.drawable.soundcloud_blue);
        new GetSoundCloudUser_Async(token).execute();
    }

    public class GetSoundCloudUser_Async extends AsyncTask<Void, Void, Void> {

        String scToken;

        public GetSoundCloudUser_Async(String scToken) {
            this.scToken = scToken;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SoundCloudAPI api = new SoundCloudAPI(Constants.SOUNDCLOUD_CLIENT_ID);
            api.setToken(scToken);
            Call<User> user = api.getService().getMe();

            //Test code
            if (user != null) {
                try {
                    User execute = user.execute().body();
                    soundCloudUserId = execute.id;
                    Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_SOUND_CLOUD_LOGIN, true);
                    if (execute.avatar_url != null) {
                        soundCloudImageUrl = execute.avatar_url;
                        Preferences.save(ConnectSocialMediaActivity.this, Preferences.SOUND_CLOUD_PROFILE_PICTURE, soundCloudImageUrl);
                    }
                    Log.v("Soundcloud id : ", soundCloudUserId);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (soundCloudUserId != null) {
                Runnable runnable = new Runnable() {
                    public void run() {
                        try {
                            //DynamoDB calls go here
                            Users users = SwapUser.getUser(ConnectSocialMediaActivity.this, Preferences.get(ConnectSocialMediaActivity.this, Preferences.USERNAME));
                            users.setSoundcloud_ID(soundCloudUserId);
                            SwapUser.updateUsers(ConnectSocialMediaActivity.this, users);
                            SwapUser.incrementPoints(ConnectSocialMediaActivity.this, users, 5);
                        } catch (Exception e) {
                        }
                    }
                };
                Thread mythread = new Thread(runnable);
                mythread.start();
            }
        }
    }

    public void spotifyLogin() {
        Utils.clearWebViewCookies(this);
        if (Preferences.get(this, Preferences.SPOTIFY_ACCESS_TOKEN) != null && !Preferences.get(this, Preferences.SPOTIFY_ACCESS_TOKEN).equals("")) {
            //AuthenticationClient.clearCookies(getApplication());
            Preferences.save(ConnectSocialMediaActivity.this, Preferences.SPOTIFY_PROFILE_PICTURE, Constants.DEFAULT_PROFILE_PICTURE);
            Preferences.save(this, Preferences.SPOTIFY_ACCESS_TOKEN, "");
            Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_SPOTIFY_LOGIN, false);
            imageViewSpotify.setImageResource(R.drawable.spotify_black);
        } else {
            AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(Constants.SPOTIFY_CLIENT_ID,
                    com.spotify.sdk.android.authentication.AuthenticationResponse.Type.CODE,
                    Constants.SPOTIFY_CALLBACK);
            builder.setScopes(new String[]{"user-read-private", "streaming"});
            AuthenticationRequest request = builder.build();
            AuthenticationClient.openLoginActivity(this, SPOTIFY_REQUEST_CODE, request);
        }

    }

    private class GetSpotifyUser_Async extends AsyncTask<Object, Object, UserPublic> {

        String spotifyToken;

        public GetSpotifyUser_Async(String accessToken) {
            this.spotifyToken = accessToken;
        }

        @Override
        protected UserPublic doInBackground(Object... voids) {
            SpotifyApi spotifyApi = new SpotifyApi();
            spotifyApi.setAccessToken(spotifyToken);
            SpotifyService spotify = spotifyApi.getService();
            UserPublic user = spotify.getMe();
            return user;
        }

        @Override
        protected void onPostExecute(final UserPublic userPublic) {
            super.onPostExecute(userPublic);
            if (userPublic != null) {
                final String spotifyUserId = userPublic.id;
                List<Image> images = userPublic.images;
                if (images.size() != 0) {
                    try {
                        if (images.get(1).url != null) {
                            spotifyImageUrl = images.get(1).url;
                            Preferences.save(ConnectSocialMediaActivity.this, Preferences.SPOTIFY_PROFILE_PICTURE, spotifyImageUrl);
                            Log.d("uri:", spotifyImageUrl);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (spotifyUserId != null) {
                    Log.d("id: ", userPublic.id);
                    Preferences.saveBooleanValue(ConnectSocialMediaActivity.this, Preferences.IS_SPOTIFY_LOGIN, true);
                    Runnable runnable = new Runnable() {
                        public void run() {
                            try {
                                //DynamoDB calls go here
                                Users users = SwapUser.getUser(ConnectSocialMediaActivity.this, Preferences.get(ConnectSocialMediaActivity.this, Preferences.USERNAME));
                                users.setSpotify_ID(spotifyUserId);
                                SwapUser.updateUsers(ConnectSocialMediaActivity.this, users);
                                SwapUser.incrementPoints(ConnectSocialMediaActivity.this, users, 5);
                            } catch (Exception e) {
                            }
                        }
                    };
                    Thread mythread = new Thread(runnable);
                    mythread.start();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        boolean isConnectSocialMediaFromSplash = Preferences.getBooleanValue(ConnectSocialMediaActivity.this, Preferences.IsConnectSocialMediaFromSplash);

        if (relativeLayoutWebView.getVisibility() == View.VISIBLE) {
            relativeLayoutWebView.setVisibility(View.GONE);
            Utils.clearWebViewCookies(this);
        } else {
            if (isConnectSocialMediaFromSplash) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //super.onBackPressed();
                                finishAffinity();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                finish();
            }
        }
    }

    private void getSpotifyTokens(com.spotify.sdk.android.authentication.AuthenticationResponse response) {

        String requestUrl = "https://accounts.spotify.com/api/token";
        APIService mAPIService = ApiUtils.getAPIService();

        mAPIService.getSpotifyTokens(requestUrl, "authorization_code", response.getCode(), Constants.SPOTIFY_CALLBACK, Constants.SPOTIFY_CLIENT_ID, Constants.SPOTIFY_CLIENT_SECRET).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                JSONObject jsonObject = null;
                try {
                    String json = response.body().string();
                    if (json != null)
                        jsonObject = new JSONObject(json);
                    imageViewSpotify.setImageResource(R.drawable.spotify_blue);
                    if (jsonObject.has("access_token") && jsonObject.has("refresh_token")) {
                        String accessToken = jsonObject.getString("access_token");
                        String refreshToken = jsonObject.getString("refresh_token");
                        int expiresIn = Integer.parseInt(jsonObject.getString("expires_in"));
                        Log.d("refresh_token", String.valueOf(refreshToken));
                        if (accessToken != null && refreshToken != null) {
                            //Save to preferences
                            new GetSpotifyUser_Async(accessToken).execute();
                            Preferences.save(ConnectSocialMediaActivity.this, Preferences.SPOTIFY_ACCESS_TOKEN, accessToken);
                            Preferences.save(ConnectSocialMediaActivity.this, Preferences.SPOTIFY_REFRESH_TOKEN, refreshToken);
                            Preferences.saveInt(ConnectSocialMediaActivity.this, Preferences.SPOTIFY_EXPIRES_IN_DURATION, expiresIn);
                            Preferences.saveLong(ConnectSocialMediaActivity.this, Preferences.SPOTIFY_ACCESS_TOKEN_TIME, System.currentTimeMillis());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "ERROR: " + "ERROR");
            }
        });
    }


}
