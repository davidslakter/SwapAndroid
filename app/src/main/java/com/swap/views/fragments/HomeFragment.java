package com.swap.views.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import com.swap.R;
import com.swap.models.SwapUser;
import com.swap.models.Users;
import com.swap.utilities.AppHelper;
import com.swap.utilities.Preferences;
import com.swap.utilities.RoundedImageView;
import com.swap.utilities.UpdateAttributes;
import com.swap.utilities.Utils;
import com.swap.views.activities.EditProfileActivity;
import com.swap.views.activities.SwapHistoryActivity;
import com.swap.views.activities.SwapTutorialActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final int PERMISSION_REQUEST = 101;
    private RoundedImageView imageViewUser;
    private RelativeLayout rlCount;
    private TextView tvPointsCount;
    private TextView tvPointLable;
    private TextView tvSwapsCount;
    private TextView tvSwapsLable;
    private TextView tvSwappedCount;
    private TextView tvSwappedLable;
    private TextView tvUsername;
    private EditText etEditBio;
    private ImageView imageViewTutorial;
    private ImageView imageViewQr;
    private ImageView imageViewRedit;
    private ImageView imageViewSpotify;
    private ImageView imageViewCall;
    private ImageView imageViewEmail;
    private ImageView imageViewInstagram;
    private ImageView imageViewGithub;
    private LinearLayout ll;
    private ImageView imageViewVimeo;
    private ImageView imageViewTwitter;
    private ImageView imageViewYoutube;
    private ImageView imageViewSoundCloud;
    private ImageView imageViewPinterest;
    File uploadToS3;
    private TransferUtility transferUtility;
    ImageView imgViewVerified;

    //private String qrCodeUrl = "https://dashboard.unitag.io/qreator/generate?setting=%7B%22LAYOUT%22%3A%7B%22COLORBG%22%3A%22transparent%22%2C%22COLOR1%22%3A%22262626%22%7D%2C%22EYES%22%3A%7B%22EYE_TYPE%22%3A%22Grid%22%7D%2C%22BODY_TYPE%22%3A5%2C%22E%22%3A%22H%22%2C%22LOGO%22%3A%7B%22L_NAME%22%3A%22https%3A%2F%2Fstatic-unitag.com%2Ffile%2Ffreeqr%2F38436a5c234f2c0817f2e83903d33287.png%22%2C%22EXCAVATE%22%3Atrue%7D%7D&data=%7B%22TYPE%22%3A%22text%22%2C%22DATA%22%3A%7B%22TEXT%22%3A%22http%3A%2F%2Fgetswap.me%2F" + Preferences.get(getActivity(), Preferences.USERNAME) + "%22%2C%22URL%22%3A%22%22%7D%7D";
    private String qrCodeUrl;
    private String userChoosenTask;
    private int CAMERA_REQUEST = 1888;
    private Uri outPutFileUri;
    private int SELECT_FILE = 1;
    Users mUsers;
    Bitmap bitmap;
    private ProgressDialog waitDialog;
    private int checkedIndex;
    private List<TransferObserver> observers;
    private static final int INDEX_NOT_CHECKED = -1;
    private ArrayList<HashMap<String, Object>> transferRecordMaps;
    String filename = "";
    boolean fromLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        qrCodeUrl = "https://dashboard.unitag.io/qreator/generate?setting=%7B%22LAYOUT%22%3A%7B%22COLORBG%22%3A%22transparent%22%2C%22COLOR1%22%3A%22262626%22%7D%2C%22EYES%22%3A%7B%22EYE_TYPE%22%3A%22Grid%22%7D%2C%22BODY_TYPE%22%3A5%2C%22E%22%3A%22H%22%2C%22LOGO%22%3A%7B%22L_NAME%22%3A%22https%3A%2F%2Fstatic-unitag.com%2Ffile%2Ffreeqr%2F38436a5c234f2c0817f2e83903d33287.png%22%2C%22EXCAVATE%22%3Atrue%7D%7D&data=%7B%22TYPE%22%3A%22text%22%2C%22DATA%22%3A%7B%22TEXT%22%3A%22http%3A%2F%2Fgetswap.me%2F" + Preferences.get(getActivity(), Preferences.USERNAME) + "%22%2C%22URL%22%3A%22%22%7D%7D";
        if (getArguments() != null) {
            fromLogin = getArguments().getBoolean("FromLogin", false);
            Log.d("fromLogin", String.valueOf(fromLogin));
        }
        inItUi(view);
        setHasOptionsMenu(true);
        transferUtility = Utils.getTransferUtility(getActivity());
        checkedIndex = INDEX_NOT_CHECKED;
        transferRecordMaps = new ArrayList<HashMap<String, Object>>();
        updateBio();
        setFontOnViews();
        return view;
    }

    private void askForTutorialDialog() {
        if (fromLogin) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setTitle(R.string.welcome_swap);
            alertDialogBuilder.setMessage(R.string.short_tutorial).setCancelable(false).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    startActivity(new Intent(getActivity(), SwapTutorialActivity.class));
                }
            });
            alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            fromLogin = false;
        }

    }

    private void inItUi(View view) {
        imageViewUser = (RoundedImageView) view.findViewById(R.id.imageViewUser);
        rlCount = (RelativeLayout) view.findViewById(R.id.rlCount);
        tvPointsCount = (TextView) view.findViewById(R.id.tvPointsCount);
        tvPointLable = (TextView) view.findViewById(R.id.tvPointLable);
        tvSwapsCount = (TextView) view.findViewById(R.id.tvSwapsCount);
        tvSwapsLable = (TextView) view.findViewById(R.id.tvSwapsLable);
        tvSwappedCount = (TextView) view.findViewById(R.id.tvSwappedCount);
        tvSwappedLable = (TextView) view.findViewById(R.id.tvSwappedLable);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        etEditBio = (EditText) view.findViewById(R.id.tvEditBio);
        imageViewTutorial = (ImageView) view.findViewById(R.id.imageViewTutorial);
        imageViewQr = (ImageView) view.findViewById(R.id.imageViewQr);
        imageViewRedit = (ImageView) view.findViewById(R.id.imageViewRedit);
        imageViewSpotify = (ImageView) view.findViewById(R.id.imageViewSpotify);
        imageViewCall = (ImageView) view.findViewById(R.id.imageViewCall);
        imageViewEmail = (ImageView) view.findViewById(R.id.imageViewEmail);
        imageViewInstagram = (ImageView) view.findViewById(R.id.imageViewInstagram);
        imageViewGithub = (ImageView) view.findViewById(R.id.imageViewGithub);
        ll = (LinearLayout) view.findViewById(R.id.ll);
        imageViewVimeo = (ImageView) view.findViewById(R.id.imageViewVimeo);
        imageViewTwitter = (ImageView) view.findViewById(R.id.imageViewTwitter);
        imageViewYoutube = (ImageView) view.findViewById(R.id.imageViewYoutube);
        imageViewSoundCloud = (ImageView) view.findViewById(R.id.imageViewSoundCloud);
        imageViewPinterest = (ImageView) view.findViewById(R.id.imageViewPinterest);
        imgViewVerified = (ImageView) view.findViewById(R.id.imgViewVerified);
        imageViewUser.setOnClickListener(this);
        imageViewTutorial.setOnClickListener(this);
        tvSwapsCount.setOnClickListener(this);
        tvSwappedCount.setOnClickListener(this);
        tvSwapsLable.setOnClickListener(this);
        tvSwappedLable.setOnClickListener(this);
        imageViewEmail.setOnClickListener(this);
        imageViewCall.setOnClickListener(this);
        imageViewInstagram.setOnClickListener(this);
        imageViewRedit.setOnClickListener(this);
        imageViewSpotify.setOnClickListener(this);
        imageViewGithub.setOnClickListener(this);
        imageViewVimeo.setOnClickListener(this);
        imageViewTwitter.setOnClickListener(this);
        imageViewYoutube.setOnClickListener(this);
        imageViewSoundCloud.setOnClickListener(this);
        imageViewPinterest.setOnClickListener(this);

    }

    private void setFontOnViews() {
        tvUsername.setTypeface(Utils.setFont(getActivity(), "avenir-next-condensed-medium.ttf"));
        tvPointsCount.setTypeface(Utils.setFont(getActivity(), "avenir-next-condensed-medium.ttf"));
        tvSwapsCount.setTypeface(Utils.setFont(getActivity(), "avenir-next-condensed-medium.ttf"));
        tvSwappedCount.setTypeface(Utils.setFont(getActivity(), "avenir-next-condensed-medium.ttf"));
        etEditBio.setTypeface(Utils.setFont(getActivity(), "avenir-next-regular.ttf"));
        tvPointLable.setTypeface(Utils.setFont(getActivity(), "avenir-next-regular.ttf"));
        tvSwapsLable.setTypeface(Utils.setFont(getActivity(), "avenir-next-regular.ttf"));
        tvSwappedLable.setTypeface(Utils.setFont(getActivity(), "avenir-next-regular.ttf"));
    }

    private void setQrCode() {
        Picasso.with(getActivity()).load(qrCodeUrl).into(imageViewQr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewTutorial:
                startActivity(new Intent(getActivity(), SwapTutorialActivity.class));
                break;
            case R.id.tvSwapsCount:
                Intent intent = new Intent(getActivity(), SwapHistoryActivity.class);
                intent.putExtra("Swaps", "Swaps");
                intent.putExtra("users", mUsers);
                getActivity().startActivity(intent);
                getActivity().finish();
                break;
            case R.id.tvSwappedCount:
                Intent intentSwappedCount = new Intent(getActivity(), SwapHistoryActivity.class);
                intentSwappedCount.putExtra("Swaps", "Swapped");
                getActivity().startActivity(intentSwappedCount);
                getActivity().finish();
                break;
            case R.id.tvSwapsLable:
                Intent intentSwaps = new Intent(getActivity(), SwapHistoryActivity.class);
                intentSwaps.putExtra("Swaps", "Swaps");
                getActivity().startActivity(intentSwaps);
                getActivity().finish();
                break;
            case R.id.tvSwappedLable:
                Intent intentSwapped = new Intent(getActivity(), SwapHistoryActivity.class);
                intentSwapped.putExtra("Swaps", "Swapped");
                getActivity().startActivity(intentSwapped);
                getActivity().finish();
                break;
            case R.id.imageViewUser:
                if (Utils.checkImagePermissions(getActivity()))
                    //selectImage();
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(getContext(),this);
                else requestImagePermissions();
                break;
            case R.id.imageViewCall:
                if (Utils.isNetworkConnected(getActivity())) {
                    if (mUsers != null) {
                        if (mUsers.getWillSharePhone()) {
                            imageViewCall.setImageResource(R.drawable.phone_black);
                            mUsers.setWillSharePhone(false);
                            new GetUserInfoTask(true).execute();
                        } else {
                            imageViewCall.setImageResource(R.drawable.phone_blue);
                            mUsers.setWillSharePhone(true);
                            new GetUserInfoTask(true).execute();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageViewEmail:
                if (Utils.isNetworkConnected(getActivity())) {
                    if (mUsers != null) {
                        if (mUsers.getWillShareEmail()) {
                            imageViewEmail.setImageResource(R.drawable.email_black);
                            mUsers.setWillShareEmail(false);
                            new GetUserInfoTask(true).execute();
                        } else {
                            imageViewEmail.setImageResource(R.drawable.email_blue);
                            mUsers.setWillShareEmail(true);
                            new GetUserInfoTask(true).execute();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageViewInstagram:
                if (Utils.isNetworkConnected(getActivity())) {
                    if (Preferences.getBooleanValue(getActivity(), Preferences.IS_INSTAGRAM_LOGIN)) {
                        imageViewInstagram.setAlpha(1.0f);
                        if (mUsers != null) {
                            if (mUsers.getWillShareInstagram()) {
                                imageViewInstagram.setImageResource(R.drawable.instagram_black);
                                mUsers.setWillShareInstagram(false);
                                new GetUserInfoTask(true).execute();
                            } else {
                                imageViewInstagram.setImageResource(R.drawable.instagram_blue);
                                mUsers.setWillShareInstagram(true);
                                new GetUserInfoTask(true).execute();
                            }
                        }
                    } else {
                        imageViewInstagram.setAlpha(0.5f);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageViewSpotify:
                if (Utils.isNetworkConnected(getActivity())) {
                    if (Preferences.getBooleanValue(getActivity(), Preferences.IS_SPOTIFY_LOGIN)) {
                        imageViewSpotify.setAlpha(1.0f);
                        if (mUsers != null) {
                            if (mUsers.getWillShareSpotify()) {
                                imageViewSpotify.setImageResource(R.drawable.spotify_black);
                                mUsers.setWillShareSpotify(false);
                                new GetUserInfoTask(true).execute();
                            } else {
                                imageViewSpotify.setImageResource(R.drawable.spotify_blue);
                                mUsers.setWillShareSpotify(true);
                                new GetUserInfoTask(true).execute();
                            }
                        }
                    } else {
                        imageViewSpotify.setAlpha(0.5f);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageViewRedit:
                if (Utils.isNetworkConnected(getActivity())) {
                    if (Preferences.getBooleanValue(getActivity(), Preferences.IS_REDDIT_LOGIN)) {
                        imageViewRedit.setAlpha(1.0f);
                        if (mUsers != null) {
                            if (mUsers.getWillShareReddit()) {
                                imageViewRedit.setImageResource(R.drawable.redit_black);
                                mUsers.setWillShareReddit(false);
                                new GetUserInfoTask(true).execute();
                            } else {
                                imageViewRedit.setImageResource(R.drawable.redit_blue);
                                mUsers.setWillShareReddit(true);
                                new GetUserInfoTask(true).execute();
                            }
                        }
                    } else {
                        imageViewRedit.setAlpha(0.5f);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageViewGithub:
                if (Utils.isNetworkConnected(getActivity())) {
                    if (Preferences.getBooleanValue(getActivity(), Preferences.IS_GITHUB_LOGIN)) {
                        imageViewGithub.setAlpha(1.0f);
                        if (mUsers != null) {
                            if (mUsers.getWillShareGitHub()) {
                                imageViewGithub.setImageResource(R.drawable.github_black);
                                mUsers.setWillShareGitHub(false);
                                new GetUserInfoTask(true).execute();
                            } else {
                                imageViewGithub.setImageResource(R.drawable.github_blue);
                                mUsers.setWillShareGitHub(true);
                                new GetUserInfoTask(true).execute();
                            }
                        }
                    } else {
                        imageViewGithub.setAlpha(0.5f);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageViewVimeo:
                if (Utils.isNetworkConnected(getActivity())) {
                    if (Preferences.getBooleanValue(getActivity(), Preferences.IS_VIMEO_LOGIN)) {
                        imageViewVimeo.setAlpha(1.0f);
                        if (mUsers != null) {
                            if (mUsers.getWillShareVimeo()) {
                                imageViewVimeo.setImageResource(R.drawable.vimeo_black);
                                mUsers.setWillShareVimeo(false);
                                new GetUserInfoTask(true).execute();
                            } else {
                                imageViewVimeo.setImageResource(R.drawable.vimeo_blue);
                                mUsers.setWillShareVimeo(true);
                                new GetUserInfoTask(true).execute();
                            }
                        }
                    } else {
                        imageViewVimeo.setAlpha(0.5f);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageViewTwitter:
                if (Utils.isNetworkConnected(getActivity())) {
                    if (Preferences.getBooleanValue(getActivity(), Preferences.IS_TWITTER_LOGIN)) {
                        imageViewTwitter.setAlpha(1.0f);
                        if (mUsers != null) {
                            if (mUsers.getWillShareTwitter()) {
                                imageViewTwitter.setImageResource(R.drawable.twitter_black);
                                mUsers.setWillShareTwitter(false);
                                new GetUserInfoTask(true).execute();
                            } else {
                                imageViewTwitter.setImageResource(R.drawable.twitter_blue);
                                mUsers.setWillShareTwitter(true);
                                new GetUserInfoTask(true).execute();
                            }
                        }
                    } else {
                        imageViewTwitter.setAlpha(0.5f);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageViewYoutube:
                if (Utils.isNetworkConnected(getActivity())) {
                    if (Preferences.getBooleanValue(getActivity(), Preferences.IS_YOUTUBE_LOGIN)) {
                        imageViewYoutube.setAlpha(1.0f);
                        if (mUsers != null) {
                            if (mUsers.getWillShareYouTube()) {
                                imageViewYoutube.setImageResource(R.drawable.youtube_black);
                                mUsers.setWillShareYouTube(false);
                                new GetUserInfoTask(true).execute();
                            } else {
                                imageViewYoutube.setImageResource(R.drawable.youtube_blue);
                                mUsers.setWillShareYouTube(true);
                                new GetUserInfoTask(true).execute();
                            }
                        }
                    } else {
                        imageViewYoutube.setAlpha(0.5f);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageViewSoundCloud:
                if (Utils.isNetworkConnected(getActivity())) {
                    if (Preferences.getBooleanValue(getActivity(), Preferences.IS_SOUND_CLOUD_LOGIN)) {
                        imageViewSoundCloud.setAlpha(1.0f);
                        if (mUsers != null) {
                            if (mUsers.getWillShareSoundCloud()) {
                                imageViewSoundCloud.setImageResource(R.drawable.soundcloud_black);
                                mUsers.setWillShareSoundCloud(false);
                                new GetUserInfoTask(true).execute();
                            } else {
                                imageViewSoundCloud.setImageResource(R.drawable.soundcloud_blue);
                                mUsers.setWillShareSoundCloud(true);
                                new GetUserInfoTask(true).execute();
                            }
                        }
                    } else {
                        imageViewSoundCloud.setAlpha(0.5f);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageViewPinterest:
                if (Utils.isNetworkConnected(getActivity())) {
                    if (Preferences.getBooleanValue(getActivity(), Preferences.IS_PINTEREST_LOGIN)) {
                        imageViewPinterest.setAlpha(1.0f);
                        if (mUsers != null) {
                            if (mUsers.getWillSharePinterest()) {
                                imageViewPinterest.setImageResource(R.drawable.pinterest_black);
                                mUsers.setWillSharePinterest(false);
                                new GetUserInfoTask(true).execute();
                            } else {
                                imageViewPinterest.setImageResource(R.drawable.pinterest_blue);
                                mUsers.setWillSharePinterest(true);
                                new GetUserInfoTask(true).execute();
                            }
                        }
                    } else {
                        imageViewPinterest.setAlpha(0.5f);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    private void requestImagePermissions() {
        requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    selectImage();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission required for changing user image", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void selectImage() {
        final CharSequence[] items = {getResources().getString(R.string.takePhoto), getResources().getString(R.string.choooseFromLibrary),
                getResources().getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.uvsnap));
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                // boolean result = Utils.checkPermission(getActivity());
                if (items[item].equals(getResources().getString(R.string.takePhoto))) {
                    userChoosenTask = getResources().getString(R.string.takePhoto);
                    //if (result)
                    cameraIntent();
                } else if (items[item].equals(getResources().getString(R.string.choooseFromLibrary))) {
                    userChoosenTask = getResources().getString(R.string.choooseFromLibrary);
                    // if (result)
                    galleryIntent();

                } else if (items[item].equals(getResources().getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), System.currentTimeMillis() + ".jpg");
        System.out.println("destination" + destination);
        outPutFileUri = Uri.fromFile(destination);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutFileUri);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private class GetUserInfoTask extends AsyncTask<Void, Void, Users> {
        ProgressDialog pDialog = new ProgressDialog(getActivity());
        boolean isUpdate;


        public GetUserInfoTask(boolean isUpdate) {
            this.isUpdate = isUpdate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isUpdate) {
                pDialog.setMessage(getString(R.string.loading));
                pDialog.setCancelable(false);
                pDialog.show();
                pDialog.setCanceledOnTouchOutside(false);
            }
        }

        protected Users doInBackground(Void... params) {
            if (isUpdate) {
                SwapUser.updateUsers(getActivity(), mUsers);
            } else {
                //SwapUser.swap(getActivity(),"sipls",false);
                return SwapUser.getUser(getActivity(), Preferences.get(getActivity(), Preferences.USERNAME));
            }
            return null;
        }

        protected void onPostExecute(Users user) {

            if (!isUpdate && user != null) {
                if (Utils.isNetworkConnected(getActivity())) {
                    OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
                    String userId = status.getSubscriptionStatus().getUserId();
                    mUsers = user;
                    Utils.saveUserDateInPreferences(getActivity(), mUsers, Preferences.UserDate);
                    if (mUsers != null && userId != null && !userId.equals("")) {
                        mUsers.setNotification_id_one_signal(userId);
                        new GetUserInfoTask(true).execute();
                    }
                }
                mUsers = user;
                updateView(user);
            }
            if (!isUpdate) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.cancel();
                }
                askForTutorialDialog();
            }
        }
    }

    private void updateView(Users users) {

        if (users != null) {
            tvUsername.setText(users.getFirstname() + " " + users.getLastname());
            Picasso.with(getActivity()).load(users.getProfile_picture_url()).error(R.drawable.profile_picture_default_icon).into(imageViewUser);
            if (users.getBio() != null)
                etEditBio.setText(users.getBio());
            tvPointsCount.setText(String.valueOf(users.getPoints()));
            tvSwapsCount.setText(String.valueOf(users.getSwaps()));
            tvSwappedCount.setText(String.valueOf(users.getSwapped()));
            if (users.getWillShareEmail()) {
                imageViewEmail.setImageResource(R.drawable.email_blue);
            } else {
                imageViewEmail.setImageResource(R.drawable.email_black);
            }
            if (Preferences.getBooleanValue(getActivity(), Preferences.IS_GITHUB_LOGIN)) {
                imageViewGithub.setAlpha(1.0f);
                if (users.getWillShareGitHub()) {
                    imageViewGithub.setImageResource(R.drawable.github_blue);
                } else {
                    imageViewGithub.setImageResource(R.drawable.github_black);
                }
            } else {
                imageViewGithub.setAlpha(0.5f);
            }
            if (Preferences.getBooleanValue(getActivity(), Preferences.IS_INSTAGRAM_LOGIN)) {
                imageViewInstagram.setAlpha(1.0f);
                if (users.getWillShareInstagram()) {
                    imageViewInstagram.setImageResource(R.drawable.instagram_blue);
                } else {
                    imageViewInstagram.setImageResource(R.drawable.instagram_black);
                }
            } else {
                imageViewInstagram.setAlpha(0.5f);
            }
            if (users.getWillSharePhone()) {
                imageViewCall.setImageResource(R.drawable.phone_blue);
            } else {
                imageViewCall.setImageResource(R.drawable.phone_black);
            }
            if (Preferences.getBooleanValue(getActivity(), Preferences.IS_PINTEREST_LOGIN)) {
                imageViewPinterest.setAlpha(1.0f);
                if (users.getWillSharePinterest()) {
                    imageViewPinterest.setImageResource(R.drawable.pinterest_blue);
                } else {
                    imageViewPinterest.setImageResource(R.drawable.pinterest_black);

                }
            } else {
                imageViewPinterest.setAlpha(0.5f);
            }
            if (Preferences.getBooleanValue(getActivity(), Preferences.IS_REDDIT_LOGIN)) {
                imageViewRedit.setAlpha(1.0f);
                if (users.getWillShareReddit()) {
                    imageViewRedit.setImageResource(R.drawable.redit_blue);
                } else {
                    imageViewRedit.setImageResource(R.drawable.redit_black);
                }
            } else {
                imageViewRedit.setAlpha(0.5f);
            }
            if (Preferences.getBooleanValue(getActivity(), Preferences.IS_SOUND_CLOUD_LOGIN)) {
                imageViewSoundCloud.setAlpha(1.0f);
                if (users.getWillShareSoundCloud()) {
                    imageViewSoundCloud.setImageResource(R.drawable.soundcloud_blue);
                } else {
                    imageViewSoundCloud.setImageResource(R.drawable.soundcloud_black);

                }
            } else {
                imageViewSoundCloud.setAlpha(0.5f);
            }
            if (Preferences.getBooleanValue(getActivity(), Preferences.IS_SPOTIFY_LOGIN)) {
                imageViewSpotify.setAlpha(1.0f);
                if (users.getWillShareSpotify()) {
                    imageViewSpotify.setImageResource(R.drawable.spotify_blue);
                } else {
                    imageViewSpotify.setImageResource(R.drawable.spotify_black);
                }
            } else {
                imageViewSpotify.setAlpha(0.5f);
            }
            if (Preferences.getBooleanValue(getActivity(), Preferences.IS_TWITTER_LOGIN)) {
                imageViewTwitter.setAlpha(1.0f);
                if (users.getWillShareTwitter()) {
                    imageViewTwitter.setImageResource(R.drawable.twitter_blue);
                } else {
                    imageViewTwitter.setImageResource(R.drawable.twitter_black);
                }
            } else {
                imageViewTwitter.setAlpha(0.5f);
            }
            if (Preferences.getBooleanValue(getActivity(), Preferences.IS_VIMEO_LOGIN)) {
                imageViewVimeo.setAlpha(1.0f);
                if (users.getWillShareVimeo()) {
                    imageViewVimeo.setImageResource(R.drawable.vimeo_blue);
                } else {
                    imageViewVimeo.setImageResource(R.drawable.vimeo_black);
                }
            } else {
                imageViewVimeo.setAlpha(0.5f);
            }
            if (Preferences.getBooleanValue(getActivity(), Preferences.IS_YOUTUBE_LOGIN)) {
                imageViewYoutube.setAlpha(1.0f);
                if (users.getWillShareYouTube()) {
                    imageViewYoutube.setImageResource(R.drawable.youtube_blue);
                } else {
                    imageViewYoutube.setImageResource(R.drawable.youtube_black);
                }
            } else {
                imageViewYoutube.setAlpha(0.5f);
            }
            if (users.getIsVerified()) {
                imgViewVerified.setVisibility(View.VISIBLE);
            } else {
                imgViewVerified.setVisibility(View.GONE);
            }
        }
    }

    String bioStr = "";

    private void updateBio() {
        etEditBio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bioStr = s.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isNetworkConnected(getActivity())) {
                    if (mUsers != null && !bioStr.equals("")) {
                        mUsers.setBio(bioStr);
                        new GetUserInfoTask(true).execute();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = null;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                if (data != null) {
                    bitmap = Utils.onSelectFromGalleryResult(getActivity(), data);
                }
            } else if (requestCode == CAMERA_REQUEST) {
                if (outPutFileUri != null)
                    bitmap = Utils.onCaptureImageResult(getActivity(), outPutFileUri);
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == Activity.RESULT_OK) {

                    bitmap = Utils.onCaptureImageResult(getActivity(),result.getUri());
                    //((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
                    //Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
                    Log.d("Cropping successful: " , String.valueOf(result.getSampleSize()));
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Log.d("Cropping failed: ", String.valueOf(result.getError()));
                   // Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                }
            }
        }


        if (bitmap != null) {
            imageViewUser.setImageBitmap(bitmap);
        }
        if (Utils.realPath != null) {
            if (Utils.isNetworkConnected(getActivity())) {
                new UserProfilePicUpdate().execute();
            } else {
                Toast.makeText(getActivity(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class UserProfilePicUpdate extends AsyncTask<Void, Void, Users> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //if (!isUpdate) {
            showWaitDialog("Loading..");
            //}
        }

        protected Users doInBackground(Void... params) {

            if (Utils.realPath != null) {
                uploadFileToS3(Utils.realPath);
            }
            return null;
        }

        protected void onPostExecute(Users user) {


        }
    }

    private void uploadFileToS3(String uri) {
        if (uri != null) {
            uploadToS3 = new File(uri);
            filename = uri.substring(uri.lastIndexOf("/") + 1).replace("_", "");
            TransferObserver transferObserver = transferUtility.upload(
                    com.swap.utilities.Constants.BUCKET_NAME,
                    Preferences.get(getActivity(), Preferences.USERNAME) + "/profile_picture-" + filename,
                    uploadToS3
            );
            transferObserverListener(transferObserver);
        }
    }

    public void transferObserverListener(TransferObserver transferObserver) {

        transferObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.d("State Change", String.valueOf(state));
                if (state.equals(TransferState.COMPLETED)) {
                    Utils.realPath = "";
                    if (mUsers != null && bitmap != null && !filename.isEmpty()) {
                        String pictureUrl = com.swap.utilities.Constants.PROFILE_PICTURE + Preferences.get(getActivity(), Preferences.USERNAME) + "/profile_picture-" + filename;
                        Log.d("pictureUrl_home: ", pictureUrl);
                        Picasso.with(getActivity()).load(pictureUrl).error(R.drawable.profile_picture_default_icon).into(imageViewUser);
                        mUsers.setProfile_picture_url(pictureUrl);
                        Utils.saveUserDateInPreferences(getActivity(), mUsers, Preferences.UserDate);
                        UpdateAttributes.updateAttribute(getActivity(), AppHelper.getSignUpFieldsC2O().get("Picture"), pictureUrl);
                        new GetUserInfoTask(true).execute();
                    }
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    closeWaitDialog();
                }

                if (state.equals(TransferState.FAILED)) {
                    closeWaitDialog();
                    Utils.realPath = "";
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                try {
                    int percentage = (int) (bytesCurrent / bytesTotal * 100);
                    Log.d("Progress in %", String.valueOf(percentage));
                } catch (ArithmeticException ae) {
                    ae.printStackTrace();
                }
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error", "error");
            }

        });
    }

    private void showWaitDialog(String message) {
        closeWaitDialog();
        waitDialog = new ProgressDialog(getActivity());
        waitDialog.setCanceledOnTouchOutside(false);
        waitDialog.setCancelable(false);
        waitDialog.setTitle(message);
        waitDialog.show();
    }

    private void closeWaitDialog() {
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.isNetworkConnected(getActivity())) {
            new GetUserInfoTask(false).execute();
            setQrCode();
            initData();
        } else {
            Toast.makeText(getActivity(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
        }
    }


    private void initData() {
        transferRecordMaps.clear();
        // Use TransferUtility to get all upload transfers.
        observers = transferUtility.getTransfersWithType(TransferType.UPLOAD);
        TransferListener listener = new UploadListener();
        for (TransferObserver observer : observers) {

            // For each transfer we will will create an entry in
            // transferRecordMaps which will display
            // as a single row in the UI
            HashMap<String, Object> map = new HashMap<String, Object>();
            Utils.fillMap(map, observer, false);
            transferRecordMaps.add(map);

            // Sets listeners to in progress transfers
            if (TransferState.WAITING.equals(observer.getState())
                    || TransferState.WAITING_FOR_NETWORK.equals(observer.getState())
                    || TransferState.IN_PROGRESS.equals(observer.getState())) {
                observer.setTransferListener(listener);
            }
        }
    }

    private class UploadListener implements TransferListener {

        // Simply updates the UI list when notified.
        @Override
        public void onError(int id, Exception e) {
            Log.e("Error during upload:", "Error during upload: " + id, e);
            //updateList();
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d("onProgressChanged:", String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
            //updateList();
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            Log.d("onStateChanged", "onStateChanged: " + id + ", " + newState);
            // updateList();
        }
    }
}
