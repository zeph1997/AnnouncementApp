package com.example.zeph1.announcementapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);

        WebView webView = (WebView) findViewById(R.id.webViewer);

        Intent i = getIntent();
        String title = i.getStringExtra("ArticleTitle");
        String url = i.getStringExtra("ArticleUrl");

        ActionBar a = getSupportActionBar();
        a.setTitle(title);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(url);
    }
}
