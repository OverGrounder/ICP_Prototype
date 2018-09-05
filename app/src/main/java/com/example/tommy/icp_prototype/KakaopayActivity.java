package com.example.tommy.icp_prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class KakaopayActivity extends Activity {
    private static final String TAG = SignInActivity.class.getName();

    private WebView mainWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakaopay);


        mainWebView = (WebView) findViewById(R.id.mainWebView);
        mainWebView.setWebViewClient(new KakaoWebViewClient(this));
        WebSettings settings = mainWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);

        Intent intent =getIntent();
        String url = intent.getStringExtra("next_redirect_app_url");
        mainWebView.loadUrl(url);

    }

}
