package com.swap.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.swap.R;
import com.swap.adapters.SelectPictureAdapter;
import com.swap.models.SwapUser;
import com.swap.models.Users;
import com.swap.utilities.Preferences;
import com.swap.utilities.Utils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Arrays;
import java.util.List;

public class SelectPictureActivity extends AppCompatActivity implements View.OnClickListener {

    String[] sliderSelectPictureArray;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    Button buttonSetPicture;
    Button buttonSkip;
    boolean isFromSettings = false;
    String instaProfileImage = "";
    String spotifyProfileImage = "";
    String soundCloudProfileImage = "";
    String gitHubImageUrl = "";
    String pinterestProfileImage = "";
    String twitterImageUrl = "";
    int currentPosition;
    String image;
    Users mUsers;
    TextView textViewSelectPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture);
        findViewById();
    }

    private void findViewById() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("isFromSettings")) {
                isFromSettings = extras.getBoolean("isFromSettings");
            }
            if (extras.containsKey("pinterestProfileImage")) {
                pinterestProfileImage = extras.getString("pinterestProfileImage");
            }
            if (extras.containsKey("twitterImageUrl")) {
                twitterImageUrl = extras.getString("twitterImageUrl");
            }
        }
        sliderSelectPictureArray = new String[]{
                Preferences.get(this,Preferences.INSTAGRAM_PROFILE_PICTURE),
                Preferences.get(this,Preferences.TWITTER_PROFILE_PICTURE),
                Preferences.get(this,Preferences.SOUND_CLOUD_PROFILE_PICTURE),
                Preferences.get(this,Preferences.GITHUB_PROFILE_PICTURE)};
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new SelectPictureAdapter(sliderSelectPictureArray, this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(4);

        CirclePageIndicator titleIndicator = (CirclePageIndicator) findViewById(R.id.circlePageIndicator);
        titleIndicator.setViewPager(viewPager);
        buttonSetPicture = (Button) findViewById(R.id.buttonSetPicture);
        buttonSetPicture.setOnClickListener(this);
        buttonSkip = (Button) findViewById(R.id.buttonSkip);
        buttonSkip.setOnClickListener(this);
        textViewSelectPicture = (TextView) findViewById(R.id.textViewSelectPicture);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                getImage();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (Utils.isNetworkConnected(SelectPictureActivity.this)) {
            new GetUserInfoTask(false).execute();
        }
        setFontOnViews();
        getImage();
    }

    private void setFontOnViews() {
        textViewSelectPicture.setTypeface(Utils.setFont(this, "lantinghei.ttf"));
        buttonSkip.setTypeface(Utils.setFont(this, "lantinghei.ttf"));
        buttonSetPicture.setTypeface(Utils.setFont(this, "lantinghei.ttf"));
    }

    private void getImage() {
        List<String> pic = Arrays.asList(sliderSelectPictureArray);
        for (int i = 0; i <= pic.size(); i++) {
            if (currentPosition == i) {
                image = pic.get(i);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSetPicture:
                navigateToHome(false);
                break;
            case R.id.buttonSkip:
                navigateToHome(true);
                break;
        }
    }

    private void navigateToHome(boolean isSkip) {
        if (isSkip) {
            if (isFromSettings) {
                Intent i = new Intent(SelectPictureActivity.this, HomeActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(SelectPictureActivity.this, SwapTutorialActivity.class);
                startActivity(i);
            }
        } else {
            if (Utils.isNetworkConnected(SelectPictureActivity.this)) {
                if (Utils.isNetworkConnected(SelectPictureActivity.this)) {

                    if (mUsers != null && image != null && !image.isEmpty()) {
                        mUsers.setProfile_picture_url(image);
                        new GetUserInfoTask(true).execute();
                    }
                }
            }
        }
    }


    private class GetUserInfoTask extends AsyncTask<Void, Void, Users> {

        boolean isUpdate;
        ProgressDialog pDialog = new ProgressDialog(SelectPictureActivity.this);

        public GetUserInfoTask(boolean isUpdate) {
            this.isUpdate = isUpdate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isUpdate) {
                pDialog.setMessage(getString(R.string.loading));
                pDialog.setCancelable(false);
                pDialog.show();
            }
        }

        protected Users doInBackground(Void... params) {
            if (isUpdate) {
                SwapUser.updateUsers(SelectPictureActivity.this, mUsers);
            } else {
                //SwapUser.swap(getActivity(),"sipls",false);
                return SwapUser.getUser(SelectPictureActivity.this, Preferences.get(SelectPictureActivity.this, Preferences.USERNAME));
            }
            return null;
        }

        protected void onPostExecute(Users user) {


            if (user != null)
                mUsers = user;

            if (isUpdate) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.cancel();
                }

                if (isFromSettings) {
                    Intent i = new Intent(SelectPictureActivity.this, HomeActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(SelectPictureActivity.this, SwapTutorialActivity.class);
                    startActivity(i);
                }
            }
        }
    }
}
