package com.earnhub.pro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkInitializationConfiguration;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EarnHubPro";

    private WebView webView;
    private FrameLayout topBannerContainer;
    private FrameLayout bottomBannerContainer;

    // Google AdMob Components
    private AdView admobTopBanner;
    private AdView admobBottomBanner;
    private RewardedAd admobRewardedAd;
    private boolean isRewardedLoading = false;

    // AppLovin MAX Components
    private MaxAdView maxTopBanner;
    private MaxAdView maxBottomBanner;
    private MaxRewardedAd maxRewardedAd;

    // Pending reward callback state
    private String pendingSourceTag = "video";
    private int pendingRewardCoins = 30;
    private boolean rewardEarnedInCurrentAd = false;

    // 30-Second Auto-Refresh Engine (Only runs when screen is visible/active)
    private final Handler refreshHandler = new Handler(Looper.getMainLooper());
    private int bannerRefreshCount = 0;
    private boolean isActivityVisible = false;

    private final Runnable bannerRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            if (isActivityVisible) {
                refreshNativeBanners();
                refreshHandler.postDelayed(this, AdConfig.BANNER_REFRESH_INTERVAL_MS);
            }
        }
    };

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topBannerContainer = findViewById(R.id.topBannerContainer);
        bottomBannerContainer = findViewById(R.id.bottomBannerContainer);
        webView = findViewById(R.id.webView);

        setupWebView();
        initializeAdNetworks();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new EarnHubNativeBridge(), "EarnHubNative");
        webView.loadUrl("file:///android_asset/www/index.html");
    }

    private void initializeAdNetworks() {
        // 1. Initialize Google Mobile Ads SDK (AdMob)
        MobileAds.initialize(this, initializationStatus -> {
            Log.d(TAG, "AdMob SDK Initialized");
            runOnUiThread(() -> {
                if (!AdConfig.ENABLE_APPLOVIN_MAX) {
                    setupAdMobBanners();
                    preloadAdMobRewardedVideo();
                }
                notifyWebViewSdkReady("AdMob SDK Live");
            });
        });

        // 2. Initialize AppLovin MAX SDK if enabled
        if (AdConfig.ENABLE_APPLOVIN_MAX && !AdConfig.APPLOVIN_SDK_KEY.startsWith("REPLACE_")) {
            AppLovinSdkInitializationConfiguration initConfig =
                    AppLovinSdkInitializationConfiguration.builder(AdConfig.APPLOVIN_SDK_KEY, this)
                            .setMediationProvider(AppLovinMediationProvider.MAX)
                            .build();

            AppLovinSdk.getInstance(this).initialize(initConfig, sdkConfig -> {
                Log.d(TAG, "AppLovin MAX SDK Initialized");
                runOnUiThread(() -> {
                    setupAppLovinBanners();
                    preloadAppLovinRewardedVideo();
                    notifyWebViewSdkReady("AppLovin MAX + AdMob Live");
                });
            });
        }
    }

    // =========================================================================
    // TOP & BOTTOM 50dp STICKY BANNERS (NO CLOSE BUTTON - 100% ADMIN REVENUE)
    // =========================================================================
    private void setupAdMobBanners() {
        // Top 50dp Banner
        admobTopBanner = new AdView(this);
        admobTopBanner.setAdUnitId(AdConfig.ADMOB_TOP_BANNER_ID);
        admobTopBanner.setAdSize(AdSize.BANNER);
        admobTopBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TAG, "Top 50dp Banner Loaded");
            }
        });
        FrameLayout.LayoutParams topParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        );
        topBannerContainer.removeAllViews();
        topBannerContainer.addView(admobTopBanner, topParams);
        admobTopBanner.loadAd(new AdRequest.Builder().build());

        // Bottom 50dp Banner
        admobBottomBanner = new AdView(this);
        admobBottomBanner.setAdUnitId(AdConfig.ADMOB_BOTTOM_BANNER_ID);
        admobBottomBanner.setAdSize(AdSize.BANNER);
        admobBottomBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TAG, "Bottom 50dp Banner Loaded");
            }
        });
        FrameLayout.LayoutParams bottomParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        );
        bottomBannerContainer.removeAllViews();
        bottomBannerContainer.addView(admobBottomBanner, bottomParams);
        admobBottomBanner.loadAd(new AdRequest.Builder().build());
    }

    private void setupAppLovinBanners() {
        maxTopBanner = new MaxAdView(AdConfig.APPLOVIN_TOP_BANNER_ID, this);
        int heightPx = (int) (50 * getResources().getDisplayMetrics().density);
        maxTopBanner.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, heightPx, Gravity.CENTER));
        topBannerContainer.removeAllViews();
        topBannerContainer.addView(maxTopBanner);
        maxTopBanner.loadAd();
        maxTopBanner.startAutoRefresh();

        maxBottomBanner = new MaxAdView(AdConfig.APPLOVIN_BOT_BANNER_ID, this);
        maxBottomBanner.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, heightPx, Gravity.CENTER));
        bottomBannerContainer.removeAllViews();
        bottomBannerContainer.addView(maxBottomBanner);
        maxBottomBanner.loadAd();
        maxBottomBanner.startAutoRefresh();
    }

    private void refreshNativeBanners() {
        bannerRefreshCount++;
        if (!AdConfig.ENABLE_APPLOVIN_MAX) {
            if (admobTopBanner != null) {
                admobTopBanner.loadAd(new AdRequest.Builder().build());
            }
            if (admobBottomBanner != null) {
                admobBottomBanner.loadAd(new AdRequest.Builder().build());
            }
        }
        evalJs("if(window.onBannerRefreshed){window.onBannerRefreshed(" + bannerRefreshCount + ");}");
    }

    // =========================================================================
    // REWARDED VIDEO ADS (70% ADMIN / 30% USER - OFFICIAL COMPLETION CALLBACK)
    // =========================================================================
    private void preloadAdMobRewardedVideo() {
        if (isRewardedLoading || admobRewardedAd != null) return;
        isRewardedLoading = true;
        evalJs("if(window.onAdStateChanged){window.onAdStateChanged('loading');}");

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, AdConfig.ADMOB_REWARDED_VIDEO_ID, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                admobRewardedAd = rewardedAd;
                isRewardedLoading = false;
                Log.d(TAG, "AdMob Rewarded Video Ready!");
                evalJs("if(window.onAdStateChanged){window.onAdStateChanged('ready');}");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                admobRewardedAd = null;
                isRewardedLoading = false;
                Log.e(TAG, "AdMob Rewarded failed: " + loadAdError.getMessage());
                evalJs("if(window.onAdStateChanged){window.onAdStateChanged('retry');}");
                // Auto-retry in 5 seconds
                refreshHandler.postDelayed(() -> preloadAdMobRewardedVideo(), 5000);
            }
        });
    }

    private void preloadAppLovinRewardedVideo() {
        maxRewardedAd = MaxRewardedAd.getInstance(AdConfig.APPLOVIN_REWARDED_ID, this);
        maxRewardedAd.setListener(new MaxRewardedAdListener() {
            @Override
            public void onUserRewarded(@NonNull MaxAd maxAd, @NonNull MaxReward maxReward) {
                rewardEarnedInCurrentAd = true;
                deliverRewardToWebView(pendingSourceTag, pendingRewardCoins);
            }
            @Override
            public void onAdLoaded(@NonNull MaxAd maxAd) {
                evalJs("if(window.onAdStateChanged){window.onAdStateChanged('ready');}");
            }
            @Override
            public void onAdDisplayed(@NonNull MaxAd maxAd) {}
            @Override
            public void onAdHidden(@NonNull MaxAd maxAd) {
                if (!rewardEarnedInCurrentAd) {
                    evalJs("if(window.onNativeAdCancelled){window.onNativeAdCancelled('Ad poori nahi dekhi! Coins nahi mile.');}");
                }
                maxRewardedAd.loadAd();
            }
            @Override
            public void onAdClicked(@NonNull MaxAd maxAd) {}
            @Override
            public void onAdLoadFailed(@NonNull String s, @NonNull MaxError maxError) {
                refreshHandler.postDelayed(() -> maxRewardedAd.loadAd(), 5000);
            }
            @Override
            public void onAdDisplayFailed(@NonNull MaxAd maxAd, @NonNull MaxError maxError) {
                maxRewardedAd.loadAd();
            }
        });
        maxRewardedAd.loadAd();
    }

    private void launchNativeRewardedAd(String sourceTag, int coins) {
        pendingSourceTag = sourceTag;
        pendingRewardCoins = coins;
        rewardEarnedInCurrentAd = false;

        if (AdConfig.ENABLE_APPLOVIN_MAX && maxRewardedAd != null && maxRewardedAd.isReady()) {
            maxRewardedAd.showAd(this);
            return;
        }

        if (admobRewardedAd != null) {
            admobRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    admobRewardedAd = null;
                    if (!rewardEarnedInCurrentAd) {
                        evalJs("if(window.onNativeAdCancelled){window.onNativeAdCancelled('Ad beech mein band kar di! Poori ad dekhne par hi coins milenge.');}");
                    }
                    preloadAdMobRewardedVideo();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    admobRewardedAd = null;
                    evalJs("if(window.onNativeAdCancelled){window.onNativeAdCancelled('Ad load nahi hui. Dobara koshish karein.');}");
                    preloadAdMobRewardedVideo();
                }
            });

            admobRewardedAd.show(this, rewardItem -> {
                rewardEarnedInCurrentAd = true;
                deliverRewardToWebView(pendingSourceTag, pendingRewardCoins);
            });
        } else {
            Toast.makeText(this, "Ad load ho rahi hai, 3 second mein dobara click karein...", Toast.LENGTH_SHORT).show();
            preloadAdMobRewardedVideo();
            evalJs("if(window.onNativeAdCancelled){window.onNativeAdCancelled('Ad network se load ho rahi hai, 3 second baad dobara tap karein.');}");
        }
    }

    private void deliverRewardToWebView(String sourceTag, int coins) {
        String safeTag = sourceTag.replace("'", "");
        evalJs("if(window.onNativeAdRewarded){window.onNativeAdRewarded('" + safeTag + "', " + coins + ");}");
    }

    private void notifyWebViewSdkReady(String label) {
        evalJs("if(window.onNativeSdkReady){window.onNativeSdkReady('" + label + "');}");
    }

    private void evalJs(String js) {
        runOnUiThread(() -> {
            if (webView != null) {
                webView.evaluateJavascript(js, null);
            }
        });
    }

    // =========================================================================
    // JAVASCRIPT BRIDGE (WEBVIEW <-> NATIVE ANDROID SDK)
    // =========================================================================
    public class EarnHubNativeBridge {
        @JavascriptInterface
        public void showRewardedVideo(String sourceTag, int coins) {
            runOnUiThread(() -> launchNativeRewardedAd(sourceTag, coins));
        }

        @JavascriptInterface
        public boolean isRewardedReady() {
            if (AdConfig.ENABLE_APPLOVIN_MAX && maxRewardedAd != null) {
                return maxRewardedAd.isReady();
            }
            return admobRewardedAd != null;
        }

        @JavascriptInterface
        public void openExternalUrl(String url) {
            runOnUiThread(() -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Link open nahi ho saka", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @JavascriptInterface
        public String getSdkInfo() {
            return AdConfig.ENABLE_APPLOVIN_MAX
                    ? "AppLovin MAX + AdMob SDK (Live)"
                    : "Google AdMob SDK Active (Test Ads Ready)";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityVisible = true;
        if (admobTopBanner != null) admobTopBanner.resume();
        if (admobBottomBanner != null) admobBottomBanner.resume();
        refreshHandler.removeCallbacks(bannerRefreshRunnable);
        refreshHandler.postDelayed(bannerRefreshRunnable, AdConfig.BANNER_REFRESH_INTERVAL_MS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityVisible = false;
        refreshHandler.removeCallbacks(bannerRefreshRunnable);
        if (admobTopBanner != null) admobTopBanner.pause();
        if (admobBottomBanner != null) admobBottomBanner.pause();
    }

    @Override
    protected void onDestroy() {
        refreshHandler.removeCallbacks(bannerRefreshRunnable);
        if (admobTopBanner != null) admobTopBanner.destroy();
        if (admobBottomBanner != null) admobBottomBanner.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
