package com.multikick.viewer;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    WebView w1, w2, w3, w4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide action bar for fullscreen feel
        try { getSupportActionBar().hide(); } catch (Exception ignored) {}
        setContentView(R.layout.activity_main);

        w1 = findViewById(R.id.web1);
        w2 = findViewById(R.id.web2);
        w3 = findViewById(R.id.web3);
        w4 = findViewById(R.id.web4);

        setupWebView(w1);
        setupWebView(w2);
        setupWebView(w3);
        setupWebView(w4);

        // Default load MultiKick - user can navigate inside each quadrant to choose lives
        String url = "https://multikick.com";
        w1.loadUrl(url);
        w2.loadUrl(url);
        w3.loadUrl(url);
        w4.loadUrl(url);

        // make D-pad navigation easier on TV: focus order
        w1.setNextFocusRightId(R.id.web2);
        w1.setNextFocusDownId(R.id.web3);
        w2.setNextFocusLeftId(R.id.web1);
        w2.setNextFocusDownId(R.id.web4);
        w3.setNextFocusUpId(R.id.web1);
        w3.setNextFocusRightId(R.id.web4);
        w4.setNextFocusUpId(R.id.web2);
        w4.setNextFocusLeftId(R.id.web3);
    }

    private void setupWebView(WebView w) {
        WebSettings ws = w.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setMediaPlaybackRequiresUserGesture(false);
        w.setWebViewClient(new WebViewClient());
        w.setWebChromeClient(new WebChromeClient());
        w.setFocusable(true);
        w.setFocusableInTouchMode(true);
    }

    // Allow volume/up/down and D-pad to be sent to focused webview when needed
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        View v = getCurrentFocus();
        if (v instanceof WebView) {
            // let the webview handle it
            return v.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        // if focused webview can go back, navigate back inside it first
        WebView focused = null;
        View v = getCurrentFocus();
        if (v instanceof WebView) focused = (WebView) v;
        if (focused != null && focused.canGoBack()) {
            focused.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
