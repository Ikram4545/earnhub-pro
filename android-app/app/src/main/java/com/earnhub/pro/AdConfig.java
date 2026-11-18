package com.earnhub.pro;

/**
 * SINGLE MASTER CONFIGURATION FILE FOR ALL AD NETWORKS
 * ----------------------------------------------------
 * Step 1: Test the APK right now with Official Google Test IDs (Safe - Zero Ban Risk).
 * Step 2: After Play Store 14-day approval, paste your REAL AdMob & AppLovin IDs below and rebuild!
 */
public class AdConfig {

    // =========================================================================
    // 1. GOOGLE ADMOB OFFICIAL IDS (Currently Official Google Test IDs)
    // =========================================================================
    public static final String ADMOB_TOP_BANNER_ID    = "ca-app-pub-3940256099942544/6300978111";
    public static final String ADMOB_BOTTOM_BANNER_ID = "ca-app-pub-3940256099942544/6300978111";
    public static final String ADMOB_REWARDED_VIDEO_ID = "ca-app-pub-3940256099942544/5224354917";

    // =========================================================================
    // 2. APPLOVIN MAX SDK IDS (Enable once Play Store URL & SDK Key are active)
    // =========================================================================
    public static final boolean ENABLE_APPLOVIN_MAX    = false;
    public static final String APPLOVIN_SDK_KEY        = "REPLACE_WITH_YOUR_APPLOVIN_SDK_KEY";
    public static final String APPLOVIN_TOP_BANNER_ID  = "REPLACE_WITH_TOP_BANNER_UNIT_ID";
    public static final String APPLOVIN_BOT_BANNER_ID  = "REPLACE_WITH_BOTTOM_BANNER_UNIT_ID";
    public static final String APPLOVIN_REWARDED_ID    = "REPLACE_WITH_REWARDED_UNIT_ID";

    // =========================================================================
    // 3. AUTO-REFRESH & ECONOMY SETTINGS
    // =========================================================================
    public static final long BANNER_REFRESH_INTERVAL_MS = 30000L; // 30 Seconds Official Standard
}
