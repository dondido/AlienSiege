package com.publicgamelibrary.aliensiege;

import android.annotation.SuppressLint;
import android.app.Activity;
//import android.graphics.Color;
//import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
//import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout;

import com.publicgamelibrary.aliensiege.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {

	private WebView webview;
	public WebAppInterface WebApp;
	//private AdView adView;
	private InterstitialAd interstitial;
	private AdRequest adRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 

		//adView = (AdView) this.findViewById(R.id.adView);
		adRequest = new AdRequest.Builder().addTestDevice(
				AdRequest.DEVICE_ID_EMULATOR).build();

		// Create the interstitial.
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId("xxxx");

		// Begin loading your interstitial.
		interstitial.loadAd(adRequest);

		// Set the AdListener.
		interstitial.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				displayInterstitial();
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				//((LinearLayout) adView.getParent()).removeView(adView);
				initGame();
			}

			@Override
			public void onAdOpened() {
				initGame();
				//initBanner();
			}
		});
	}

	public void initBanner() {
		// Look up the AdView as a resource and load a request.

		// Create the adView.
		// adView = new AdView(this);
		// adView.setAdUnitId("xxxxx");
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

	// Invoke displayInterstitial() when you are ready to display an
	// interstitial.
	public void displayInterstitial() {
		if (interstitial.isLoaded()) {
			interstitial.show();
		}
	}

}
