package com.Maths.mathematicalreasoning;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class PrivacyPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);
        WebView privacyPolicy =findViewById(R.id.privacyPolicy);
        final ProgressBar progressBar =findViewById(R.id.progressBar);
        privacyPolicy.loadUrl("https://docs.google.com/document/d/e/2PACX-1vTA3GOaLINLamZYQ5onefyK7FqDoFfnx0h_SKd3d15nJzQtks1u0W6YQFZxK_imXlKrVnWHbn-YQo3S/pub");
        privacyPolicy.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
              progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}