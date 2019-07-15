package com.swap.views.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.swap.R;
import com.swap.models.SwapUser;
import com.swap.models.Users;
import com.swap.utilities.AppHelper;
import com.swap.utilities.CameraSourcePreview;
import com.swap.utilities.Constants;
import com.swap.utilities.Preferences;
import com.swap.utilities.RoundedImageView;
import com.swap.utilities.Utils;
import com.swap.views.fragments.ExploreToolFragment;
import com.swap.views.fragments.NotificationFragment;
import com.swap.views.fragments.SearchFragment;
import com.swap.views.fragments.SkipFragment;
import com.swap.views.fragments.SwapLinkFragment;
import com.swap.views.fragments.SwappedFragment;
import com.swap.views.fragments.SwapsFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SwapHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    TabLayout tabLayout;
    ViewPager viewPager;

    private static final String TAG = "MainActivity";
    private static final int PERMISSIONS_REQUEST = 100;

    private BarcodeDetector mBarcodeDetector;
    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    Button buttonGallery;
    SlidingUpPanelLayout slidingLayout;
    ImageButton imgSwapCircle;
    private LinearLayout layoutUserProfile;
    private LinearLayout layoutUserNavigation;
    private RoundedImageView imageViewUser;
    private TextView tvUserName;
    private TextView tvUserBio;
    boolean islayoutProfileVisible;
    private int SELECT_FILE = 1;
    Bitmap bitmap;
    Toolbar toolbar;
    TextView mTitle;
    int[] icons = {
            R.drawable.tab_home,
            R.drawable.tab_search,
            R.drawable.ic_skip,
            R.drawable.tab_notification,
            R.drawable.tab_explore
    };
    String swapNavigation = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppHelper.init(getApplicationContext());
        inItToolBar();
        findViewById();
        setFontOnViews();
        Preferences.saveBooleanValue(this, Preferences.IsConnectSocialMedia, false);
    }

    private void inItToolBar() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("Swaps")) {
                swapNavigation = extras.getString("Swaps");
            }
            if (extras.containsKey("users")) {

            }
        }
        View view = findViewById(R.id.layout_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbarView);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTitle = (TextView) toolbar.findViewById(R.id.textViewTitle);
        if (swapNavigation.equalsIgnoreCase("Swaps")) {
            mTitle.setText("Swaps");
        } else if (swapNavigation.equalsIgnoreCase("Swapped")) {
            mTitle.setText("Swapped");
        } else {
            mTitle.setText("Swap Link");
        }

        toolbar.setNavigationIcon(R.drawable.back_arrow_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SwapHistoryActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    private void findViewById() {
        Preferences.saveBooleanValue(this, Preferences.IsConnectSocialMedia, false);
        Preferences.saveBooleanValue(this, Preferences.IsConnectSocialMediaFromSplash, false);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.main_tab_content);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < icons.length; i++) {
            tabLayout.getTabAt(i).setIcon(icons[i]);
        }
        tabLayout.getTabAt(0).select();

        layoutUserProfile = (LinearLayout) findViewById(R.id.layoutUserProfile);
        layoutUserNavigation = (LinearLayout) findViewById(R.id.layoutUserNavigation);
        imageViewUser = (RoundedImageView) findViewById(R.id.imageViewUser);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserBio = (TextView) findViewById(R.id.tvUserBio);
        imgSwapCircle = (ImageButton) findViewById(R.id.imgSwapCircle);
        layoutUserProfile.setOnClickListener(this);
        layoutUserNavigation.setOnClickListener(this);

        imgSwapCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canScanQR = 0;
                if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                } else {
                    slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    slidingLayout.setAnchorPoint(40);
                }
            }
        });

        mPreview = (CameraSourcePreview) findViewById(R.id.cameraSourcePreview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isPermissionGranted()) {
                setupBarcodeDetector();
                setupCameraSource();
            } else {
                requestPermission();
            }
        } else {
            setupBarcodeDetector();
            setupCameraSource();
        }
        slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingLayout.setPanelSlideListener(onSlideListener());
        buttonGallery = (Button) findViewById(R.id.buttonGallery);
        buttonGallery.setOnClickListener(this);

    }

    private void setFontOnViews() {
        buttonGallery.setTypeface(Utils.setFont(this, "lantinghei.ttf"));
        tvUserName.setTypeface(Utils.setFont(this, "lantinghei.ttf"));
        tvUserBio.setTypeface(Utils.setFont(this, "lantinghei.ttf"));
        mTitle.setTypeface(Utils.setFont(this, "avenir-next-regular.ttf"));
    }

    int previousTab = 0;

    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (swapNavigation.equalsIgnoreCase("Swaps")) {
            adapter.insertNewFragment(new SwapsFragment());

        } else if (swapNavigation.equalsIgnoreCase("Swapped")) {
            adapter.insertNewFragment(new SwappedFragment());

        } else {
            adapter.insertNewFragment(new SwapLinkFragment());

        }
        adapter.insertNewFragment(new SearchFragment());
        //fake center fragment, so that it creates place for raised center tab.
        adapter.insertNewFragment(new SkipFragment());
        adapter.insertNewFragment(new NotificationFragment());
        adapter.insertNewFragment(new ExploreToolFragment());

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 2) {
                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if (previousTab == 1) {
                                        tabLayout.getTabAt(3).select();
                                        previousTab = 3;
                                    } else {
                                        tabLayout.getTabAt(1).select();
                                        previousTab = 1;
                                    }
                                }
                            }, 10);
                } else {
                    previousTab = i;
                }

                if (i == 0) {
                    getSupportActionBar().show();
                    if (swapNavigation.equalsIgnoreCase("Swaps")) {
                        mTitle.setText("Swaps");
                    } else if (swapNavigation.equalsIgnoreCase("Swapped")) {
                        mTitle.setText("Swapped");
                    } else {
                        mTitle.setText("Swap Link");
                    }
                    toolbar.setNavigationIcon(R.drawable.back_arrow_white);
                } else if (i == 1) {
                    getSupportActionBar().hide();
                } else if (i == 3) {
                    getSupportActionBar().show();
                    toolbar.setNavigationIcon(null);
                    mTitle.setText(R.string.swapRequests);
                } else if (i == 4) {
                    getSupportActionBar().hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonGallery:
                galleryIntent();
                break;
            case R.id.layoutUserProfile:
                layoutUserProfile.setVisibility(View.GONE);
                canScanQR = 0;
                islayoutProfileVisible = false;
                break;

            case R.id.layoutUserNavigation:
                navigateToProfile();

        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void navigateToProfile() {
        Intent intent = new Intent(SwapHistoryActivity.this, SwapProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_USER_NAME, scannedUser);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void insertNewFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.action_settings:
                Intent settingsIntent = new Intent(this, LoginActivity.class);
                startActivity(settingsIntent);*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    recreate();
                }*/
            } else {
                Toast.makeText(this, R.string.camera_permission, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    int canScanQR = 0;
    String userName = "";

    private void setupBarcodeDetector() {
        mBarcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0 && canScanQR == 0) {
                    canScanQR = canScanQR + 1;
                    String data = barcodes.valueAt(0).displayValue;

                    if (!data.equals("")) {
                        userName = data.substring(data.lastIndexOf("/") + 1);
                        if (Utils.isNetworkConnected(SwapHistoryActivity.this)) {
                            new SwapHistoryActivity.GetUserInfoTask().execute(userName);
                        }
                    }
                    Log.d(TAG, "Barcode detected: " + data);

                    //slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            }
        });
    }

    private void setupCameraSource() {
        mCameraSource = new CameraSource.Builder(getApplicationContext(), mBarcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(15.0f)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true)
                .build();
    }

    private void startCameraSource() {
        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource);
            } catch (IOException e) {
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    private void playBeep() {
        ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME);
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }

    private SlidingUpPanelLayout.PanelSlideListener onSlideListener() {
        return new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                tabLayout.setVisibility(View.GONE);
                if (islayoutProfileVisible)
                    layoutUserProfile.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPanelCollapsed(View view) {

                tabLayout.setVisibility(View.VISIBLE);
                layoutUserProfile.setVisibility(View.GONE);
            }

            @Override
            public void onPanelExpanded(View view) {

            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPermissionGranted())
            startCameraSource();
    }

    Users scannedUser;

    private class GetUserInfoTask extends AsyncTask<String, Void, Users> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Users doInBackground(String... params) {
            scannedUser = SwapUser.getUser(SwapHistoryActivity.this, params[0]);
            return scannedUser;
        }

        protected void onPostExecute(Users user) {
            layoutUserProfile.setVisibility(View.VISIBLE);
            islayoutProfileVisible = true;
            if (user != null) {
                if (user.getProfile_picture_url() != null) {
                    Picasso.with(SwapHistoryActivity.this).load(user.getProfile_picture_url()).into(imageViewUser);
                }
                if (user.getFirstname() != null && user.getLastname() != null) {
                    tvUserName.setText(user.getFirstname() + " " + user.getLastname());
                }
                if (user.getBio() != null) {
                    tvUserBio.setText(user.getBio());
                }
            }
        }
    }

    ProgressDialog pDialog;

    private void showProgressDialog() {
        pDialog = new ProgressDialog(SwapHistoryActivity.this);
        pDialog.setMessage(getString(R.string.loading));
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = null;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                if (data != null) {
                    bitmap = Utils.onSelectFromGalleryResult(this, data);
                    if (bitmap != null) {

                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }
}
