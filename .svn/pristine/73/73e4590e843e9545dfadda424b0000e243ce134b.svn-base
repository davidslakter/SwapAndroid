package com.swap.views.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.swap.R;
import com.swap.utilities.Constants;
import com.swap.utilities.Utils;

public class PrivacyPolicyActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        inItToolBar();

    }

    private void inItToolBar() {
        View view = findViewById(R.id.layout_toolbar);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarView);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.textViewTitle);
        mTitle.setText(R.string.privacyPolicy);
        mTitle.setTypeface(Utils.setFont(this, "avenir-next-regular.ttf"));
        toolbar.setNavigationIcon(R.drawable.back_arrow_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView = (WebView)findViewById(R.id.privacyWebView) ;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if(Utils.isNetworkConnected(this)){
           startWebView(Constants.PRIVACY_POLICY_URL, webView);
        } else {
            Toast.makeText(getApplicationContext(), R.string.pleaseCheckInternetConnection, Toast.LENGTH_LONG).show();
        }

        //Set font on view//
        mTitle.setTypeface(Utils.setFont(this, "lantinghei.ttf"));
    }

    public void startWebView(String url, WebView webView) {
        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog = null;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


            //Show loader on url load
            public void onLoadResource (WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(PrivacyPolicyActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try{
                    if (progressDialog!=null) {
                        progressDialog.dismiss();
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }
        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);

        //Load url in webview
        webView.loadUrl(url);
    }


}
