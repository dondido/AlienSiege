package com.publicgamelibrary.aliensiege;

import android.annotation.SuppressLint;
import android.app.Activity;
//import android.graphics.Color;
//import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
//import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout;

import com.publicgamelibrary.aliensiege.R;
import com.google.analytics.tracking.android.EasyTracker;

import com.amazon.device.ads.*;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity implements AdListener {

	private WebView webview;
	public WebAppInterface WebApp;
	private AdLayout adView; // The ad view used to load and display the ad.
    private static final String APP_KEY = "sample-app-v1_pub-2"; // Sample Application Key. Replace this variable with your Application Key
    private static final String LOG_TAG = "SimpleAdSample"; // Tag used to prefix all log messages

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		
		// For debugging purposes enable logging, but disable for production builds
        AdRegistration.enableLogging(true);
        // For debugging purposes flag all ad requests as tests, but set to false for production builds
        AdRegistration.enableTesting(true);
        
        adView = (AdLayout)findViewById(R.id.ad_view);
        adView.setListener(this);
        
        /**/try {
            AdRegistration.setAppKey(APP_KEY);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception thrown: " + e.toString());
            return;
        }
        
        // Load the ad with the appropriate ad targeting options.
        AdTargetingOptions adOptions = new AdTargetingOptions();
        adView.loadAd(adOptions);
        
        initGame();
		
	}

	public void initBanner() {
		// Look up the AdView as a resource and load a request.

		// Create the adView.
		// adView = new AdView(this);
		// adView.setAdUnitId("ca-app-pub-9226577604414587/9326174087");
		// adView.setAdSize(AdSize.SMART_BANNER);

		// Lookup your LinearLayout assuming it's been given
		// the attribute android:id="@+id/mainLayout".
		// LinearLayout layout = (LinearLayout)findViewById(R.id.linearlayout);

		// Add the adView to it.
		// layout.addView(adView);

		// Load the adView with the ad request.
		// adView.setBackgroundColor(Color.parseColor("#232927")); 
		// adView.invalidate();
		// adView.loadAd(adRequest);
	}

	public void initGame() {
		webview = (WebView) findViewById(R.id.webview);
		// webview.setWebViewClient(new CustomWebViewClient(this));
		// create instance of object
		WebApp = new WebAppInterface(this);
		// call instance method using object

		webview.addJavascriptInterface(WebApp, "Android");

		WebSettings wSettings = webview.getSettings();
		wSettings.setJavaScriptEnabled(true);
		wSettings.setDatabaseEnabled(true);
		wSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		wSettings.setDomStorageEnabled(true);
		webview.setWebChromeClient(new WebChromeClient());
		webview.loadUrl("file:///android_asset/www/index.html");
	}

	@Override
	public void onStart() {
		super.onStart();
		// The rest of your onStart() code.
		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
		// The rest of your onStop() code.
		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

	@Override
	public void onResume() {
		super.onResume();
		if (webview != null) {
			System.out.println("Resuming webview state");
			WebApp.resumeSnd();
			// webview.resumeTimers();
		}
		/*if (adView != null) {
			adView.resume();
		}*/
	}

	@Override
	public void onPause() {
		super.onPause();
		if (webview != null) {
			WebApp.muteAll();
			System.out.println("Saving webview state");
			// webview.pauseTimers();
		}
		/*if (adView != null) {
			adView.pause();
		}*/
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (webview != null) {
			WebApp.stopSnd();
			webview.destroy();
		}
		/*if (adView != null) {
			adView.destroy();
		}*/
	}

	/**
     * This event is called after a rich media ads has collapsed from an expanded state.
     */
    public void onAdCollapsed(AdLayout view) {
        Log.d(LOG_TAG, "Ad collapsed.");
    }

    /**
     * This event is called if an ad fails to load.
     */
    public void onAdFailedToLoad(AdLayout view, AdError error) {
        Log.w(LOG_TAG, "Ad failed to load. Code: " + error.getCode() + ", Message: " + error.getMessage());
    }

    /**
     * This event is called once an ad loads successfully.
     */
    public void onAdLoaded(AdLayout view, AdProperties adProperties) {
        Log.d(LOG_TAG, adProperties.getAdType().toString() + " Ad loaded successfully.");
    }

    /**
     * This event is called after a rich media ad expands.
     */
    public void onAdExpanded(AdLayout view) {
        Log.d(LOG_TAG, "Ad expanded.");
    }

}
