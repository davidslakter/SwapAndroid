package com.swap.views.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.swap.R;
import com.swap.models.SwapRequest;
import com.swap.models.SwapUser;
import com.swap.models.Users;
import com.swap.utilities.Preferences;
import com.swap.utilities.RoundedImageView;
import com.swap.utilities.Utils;
import com.swap.views.activities.SwapTutorialActivity;

import java.util.ArrayList;
import java.util.List;

import static com.swap.utilities.Constants.KEY_FROM_SWAP_HISTORY;
import static com.swap.utilities.Constants.KEY_USER_NAME;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    Users users;
    private RoundedImageView imageViewProfilePicture;
    private TextView tvName;
    private TextView textViewBio;
    private LinearLayout linearLayoutSocialMedia;
    private ImageView imageViewRedit;
    private ImageView imageViewSpotify;
    private ImageView imageViewCall;
    private ImageView imageViewEmail;
    private ImageView imageViewInstagram;
    private ImageView imageViewGithub;
    private ImageView imageViewVimeo;
    private ImageView imageViewTwitter;
    private ImageView imageViewYoutube;
    private ImageView imageViewSoundCloud;
    private ImageView imageViewPinterest;
    private TextView tvPointsCount;
    private TextView tvSwapsCount;
    private TextView tvSwappedCount;
    Button btnSwap;
    List<String> socialMediaEnableList;
    ImageView imgViewVerified;
    private static final int PERMISSIONS_REQUEST = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        socialMediaEnableList = new ArrayList<>();
        getDataFromPreviousView();
        inItToolBar(view);
        inItUI(view);
        setDataOnView();
        loadProfile();

        return view;
    }

    private void setDataOnView() {
        if (users != null) {
            if (users.getFirstname() != null && users.getLastname() != null)
                tvName.setText(users.getFirstname() + " " + users.getLastname());
            if (users.getBio() != null)
                textViewBio.setText(users.getBio());
            tvPointsCount.setText(String.valueOf(users.getPoints()) + "\n " + getString(R.string.points));
            tvSwapsCount.setText(String.valueOf(users.getSwaps()) + "\n " + getString(R.string.swaps));
            tvSwappedCount.setText(String.valueOf(users.getSwapped()) + "\n " + getString(R.string.swapped));
            if (users.getWillShareEmail()) {
              //  imageViewEmail.setImageResource(R.drawable.ic_email_icon);
                imageViewEmail.setVisibility(View.VISIBLE);
                socialMediaEnableList.add("ShareEmail");
            }
            if (users.getWillShareGitHub()) {
               // imageViewGithub.setImageResource(R.drawable.ic_github_icon);
                imageViewGithub.setVisibility(View.VISIBLE);
                socialMediaEnableList.add("ShareGitHub");
            }
            if (users.getWillShareInstagram()) {
               // imageViewInstagram.setImageResource(R.drawable.ic_instagram_icon);
                imageViewInstagram.setVisibility(View.VISIBLE);
                socialMediaEnableList.add("ShareInstagram");
            }
            if (users.getWillSharePhone()) {
                //imageViewCall.setImageResource(R.drawable.ic_contact_icon);
                imageViewCall.setVisibility(View.VISIBLE);
                socialMediaEnableList.add("SharePhone");
            }
            if (users.getWillSharePinterest()) {
                //imageViewPinterest.setImageResource(R.drawable.ic_pinterest_icon);
                imageViewPinterest.setVisibility(View.VISIBLE);
                socialMediaEnableList.add("SharePinterest");
            }
            if (users.getWillShareReddit()) {
                //imageViewRedit.setImageResource(R.drawable.ic_reddit_icon);
                imageViewRedit.setVisibility(View.VISIBLE);
                socialMediaEnableList.add("ShareReddit");
            }
            if (users.getWillShareSoundCloud()) {
                //imageViewSoundCloud.setImageResource(R.drawable.ic_soundcloud_icon);
                imageViewSoundCloud.setVisibility(View.VISIBLE);
                socialMediaEnableList.add("SoundCloud");
            }
            if (users.getWillShareSpotify()) {
                //imageViewSpotify.setImageResource(R.drawable.ic_spotify_icon);
                imageViewSpotify.setVisibility(View.VISIBLE);
                socialMediaEnableList.add("ShareSpotify");
            }
            if (users.getWillShareTwitter()) {
                //imageViewTwitter.setImageResource(R.drawable.ic_twitter_icon);
                imageViewTwitter.setVisibility(View.VISIBLE);
                socialMediaEnableList.add("ShareTwitter");
            }
            if (users.getWillShareVimeo()) {
               // imageViewVimeo.setImageResource(R.drawable.ic_vimeo_icon);
                imageViewVimeo.setVisibility(View.VISIBLE);
                socialMediaEnableList.add("ShareReddit");
            }
            if (users.getWillShareYouTube()) {
                //imageViewYoutube.setImageResource(R.drawable.ic_youtube_icon);
                imageViewYoutube.setVisibility(View.VISIBLE);
                socialMediaEnableList.add("ShareYouTube");
            }
            if (users.getProfile_picture_url() != null) {
                Picasso.with(getActivity()).load(users.getProfile_picture_url()).error(R.drawable.profile_picture_default_icon).into(imageViewProfilePicture);
            }
            if (socialMediaEnableList.size() == 0) {
                disableSwapButton();
            }
            if (users.getIsVerified()) {
                imgViewVerified.setVisibility(View.VISIBLE);
            } else {
                imgViewVerified.setVisibility(View.GONE);
            }

        }
    }

    private void inItUI(View view) {
        imageViewProfilePicture = (RoundedImageView) view.findViewById(R.id.imageViewProfilePicture);
        tvName = (TextView) view.findViewById(R.id.tvName);
        imageViewProfilePicture = (RoundedImageView) view.findViewById(R.id.imageViewProfilePicture);
        tvName = (TextView) view.findViewById(R.id.tvName);
        textViewBio = (TextView) view.findViewById(R.id.textViewBio);
        linearLayoutSocialMedia = (LinearLayout) view.findViewById(R.id.linearLayoutSocialMedia);
        imageViewRedit = (ImageView) view.findViewById(R.id.imageViewRedit);
        imageViewSpotify = (ImageView) view.findViewById(R.id.imageViewSpotify);
        imageViewCall = (ImageView) view.findViewById(R.id.imageViewCall);
        imageViewEmail = (ImageView) view.findViewById(R.id.imageViewEmail);
        imageViewInstagram = (ImageView) view.findViewById(R.id.imageViewInstagram);
        imageViewGithub = (ImageView) view.findViewById(R.id.imageViewGithub);
        imageViewVimeo = (ImageView) view.findViewById(R.id.imageViewVimeo);
        imageViewTwitter = (ImageView) view.findViewById(R.id.imageViewTwitter);
        imageViewYoutube = (ImageView) view.findViewById(R.id.imageViewYoutube);
        imageViewSoundCloud = (ImageView) view.findViewById(R.id.imageViewSoundCloud);
        imageViewPinterest = (ImageView) view.findViewById(R.id.imageViewPinterest);
        tvPointsCount = (TextView) view.findViewById(R.id.tvPointsCount);
        tvSwapsCount = (TextView) view.findViewById(R.id.tvSwapsCount);
        tvSwappedCount = (TextView) view.findViewById(R.id.tvSwappedCount);
        btnSwap = (Button) view.findViewById(R.id.buttonSwap);
        imgViewVerified = (ImageView) view.findViewById(R.id.imgViewVerified);
        btnSwap.setOnClickListener(this);
        setFontOnViews();
    }

    private void setFontOnViews() {
        tvName.setTypeface(Utils.setFont(getActivity(), "lantinghei.ttf"));
        textViewBio.setTypeface(Utils.setFont(getActivity(), "lantinghei.ttf"));
        tvPointsCount.setTypeface(Utils.setFont(getActivity(), "lantinghei.ttf"));
        tvSwapsCount.setTypeface(Utils.setFont(getActivity(), "lantinghei.ttf"));
        tvSwappedCount.setTypeface(Utils.setFont(getActivity(), "lantinghei.ttf"));
        btnSwap.setTypeface(Utils.setFont(getActivity(), "lantinghei.ttf"));
    }

    private void getDataFromPreviousView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            users = (Users) bundle.getSerializable(KEY_USER_NAME);
        }
    }

    private void inItToolBar(View view) {

        View viewToolbar = view.findViewById(R.id.layout_toolbar);
        Toolbar toolbar = (Toolbar) viewToolbar.findViewById(R.id.toolbarView);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.textViewTitle);
        if (users != null)
            mTitle.setText(users.getUsername());

        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSwap:
                if (Utils.isNetworkConnected(getActivity())) {
                    if (isPermissionGranted()) {
                        new SwapRequestAsyncTask(false).execute();
                    } else {
                        requestPermission();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
                }
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, PERMISSIONS_REQUEST);

    }

    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    recreate();
                }*/
            } else {
                promptForPermissions();
            }
        }
    }

    private void promptForPermissions() {

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle(R.string.contactPermissions);
        alertDialogBuilder.setMessage(R.string.contactPermissionsMessege).setCancelable(false).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                requestPermission();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    ArrayList<SwapRequest> swapRequestList;

    private class SwapRequestAsyncTask extends AsyncTask<Void, Void, String> {

        boolean isPendingSwapRequest = false;
        boolean swapResponse;

        public SwapRequestAsyncTask(boolean isPendingSwapRequest) {
            this.isPendingSwapRequest = isPendingSwapRequest;
        }

        ProgressDialog pDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.loading));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {
            if (isPendingSwapRequest) {
                swapRequestList = SwapUser.getPendingSentSwapRequests(getActivity(), users.getUsername());
            } else {
                swapResponse = SwapUser.swap(getActivity(), users.getUsername(), false);
                if (swapResponse && !users.isPrivate()) {
                    Users swap = SwapUser.getUser(getActivity(), Preferences.get(getActivity(), Preferences.USERNAME));
                    Users swapped = SwapUser.getUser(getActivity(), users.getUsername());
                    SwapUser.giveSwapPointsToUsersWhoSwapped(getActivity(), swap, swapped);
                }
            }
            return "";
        }

        protected void onPostExecute(String str) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.cancel();
            }
            if (isPendingSwapRequest) {
                if (swapRequestList != null && swapRequestList.size() > 0) {
                    for (SwapRequest swapRequest : swapRequestList) {
                        if (swapRequest.getRequested().equals(Preferences.get(getActivity(), Preferences.USERNAME))) {
                            makeSwapButtonRequested();
                        }
                    }
                }
            }
            if (swapResponse) {
                if (users.isPrivate()) {
                    makeSwapButtonRequested();
                } else {
                    disableSwapButton();
                }
                showToast();
            }
        }
    }

    public void showToast() {
        Activity activityObj = this.getActivity();
        Toast toast = new Toast(activityObj);
        View view = LayoutInflater.from(activityObj).inflate(R.layout.success_message, null);
        /*TextView textView = (TextView) view.findViewById(R.id.custom_toast_text);
        textView.setText("");*/
        toast.setView(view);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    private void makeSwapButtonRequested() {
        btnSwap.setText(R.string.requested);
        btnSwap.setPadding(0, 0, 4, (int) getResources().getDimension(R.dimen.swap_button_locked_padding_bottom));
        btnSwap.setEnabled(false);
        btnSwap.setAlpha(0.4f);
    }

    private void loadProfile() {
        if (users != null) {
            if (users.getUsername().equals(Preferences.get(getActivity(), Preferences.USERNAME))) {
                disableSwapButton();
            }
            if (users.isPrivate()) {
                btnSwap.setBackgroundResource(R.drawable.privateswapbutton);
                btnSwap.setPadding(0, 0, 20, (int) getResources().getDimension(R.dimen.swap_button_locked_padding_bottom));

            } else {
                btnSwap.setBackgroundResource(R.drawable.publicswapbutton);
                btnSwap.setPadding(0, 0, 0, (int) getResources().getDimension(R.dimen.swap_button_locked_padding_bottom));
            }
            if (isPermissionGranted()) {
                new SwapRequestAsyncTask(true).execute();
            } else {
                requestPermission();
            }
        }
        if (KEY_FROM_SWAP_HISTORY) {
            disableSwapButton();
            KEY_FROM_SWAP_HISTORY = false;
        }
    }

    private void disableSwapButton() {
        btnSwap.setEnabled(false);
        btnSwap.setAlpha(0.4f);
    }
}
