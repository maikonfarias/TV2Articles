package com.maikonfarias.tv2articles;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity implements ShareActionProvider.OnShareTargetSelectedListener {
    private WebView webView;
    private ShareActionProvider mShareActionProvider;
    private Intent shareIntent = new Intent(Intent.ACTION_SEND);

    private String publicUrl;
    private String articleTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebViewClient());

        Bundle bundle = this.getIntent().getExtras();
        String url = bundle.getString("url");
        publicUrl = bundle.getString("public_url");
        articleTitle = bundle.getString("title");


        if (null != url) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        item.setEnabled(true);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setOnShareTargetSelectedListener(this);

        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, articleTitle);
        shareIntent.putExtra(Intent.EXTRA_TEXT, publicUrl);
        mShareActionProvider.setShareIntent(shareIntent);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_view:
                openUrl();
                return true;
            case android.R.id.home:
                WebViewActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
        return false;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void openUrl() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(publicUrl));
        startActivity(browserIntent);
    }

}
