package com.swap.views.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.swap.R;
import com.swap.models.Users;
import com.swap.utilities.Constants;
import com.swap.views.fragments.ProfileFragment;
import com.swap.views.fragments.TwitterFragment;

public class SwapProfileActivity extends AppCompatActivity implements View.OnClickListener {

    FragmentManager fragmentManager;
    private ImageView imageViewProfile;
    private ImageView imageViewTwitter;
    private ImageView imageViewYoutube;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_profile);
        findViewById();
        getdataFromPreviousView();
        defaultFragment();
    }

    private void getdataFromPreviousView() {
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras() != null) {
                bundle = getIntent().getExtras();
            }
        }
    }

    private void findViewById() {
        imageViewProfile = (ImageView) findViewById(R.id.imageViewProfile);
        imageViewTwitter = (ImageView) findViewById(R.id.imageViewTwitter);
        imageViewYoutube = (ImageView) findViewById(R.id.imageViewYoutube);
        imageViewProfile.setOnClickListener(this);
        imageViewTwitter.setOnClickListener(this);
        imageViewYoutube.setOnClickListener(this);
    }

    private void defaultFragment() {
        ProfileFragment profileFragment = new ProfileFragment();
        if (bundle != null) {
            profileFragment.setArguments(bundle);
        }
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayoutContent, profileFragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewProfile:
                defaultFragment();
                break;
            case R.id.imageViewTwitter:
                TwitterFragment twitterFragment = new TwitterFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frameLayoutContent, twitterFragment).commit();
                break;
            case R.id.imageViewYoutube:
                twitterFragment = new TwitterFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frameLayoutContent, twitterFragment).commit();
                break;
        }
    }
}
