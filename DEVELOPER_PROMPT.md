# 🚀 EarnHub Pro — Complete Master Developer & Production Specification

**Version:** 1.0.0 Production Architecture  
**Platform:** Native Android (APK/AAB) + Web PWA / Landing Distribution  
**Primary Ad SDKs:** Google Mobile Ads (AdMob v23.6.0) + AppLovin MAX SDK (v13.0.1)  
**Mediation Support:** Yandex Mobile Ads, Start.io, Unity Ads  
**Economics:** 70% Platform Pool (Admin) / 30% User Points Split  
**Minimum Cashout:** Rs 3,000 (100,000 Coins) & Rs 5,000 (165,000 Coins)  
**Gateways:** Easypaisa, JazzCash, SadaPay, Binance USDT TRC20  

---

## 🎯 1. Project Overview & Architecture

EarnHub Pro is an enterprise-grade mobile rewarded monetization platform designed to maximize publisher eCPM through high-fill rewarded ads, continuous auto-refresh banners, and Cost-Per-Install (CPI) app tasks, while maintaining strict anti-fraud protection and Google Play Developer compliance.

### 🏛️ The 3-Tier Monetization Model:
1. **Tier 1: Sticky 50dp Banners (Top Header & Bottom Footer) — 100% Admin Revenue**
   - **Top 50dp Header:** Native `AdView` (Google AdMob / AppLovin).
   - **Bottom 50dp Footer:** Native `AdView` (Google AdMob / AppLovin).
   - **Auto-Refresh Engine:** Officially refreshes every 30 seconds when the app is active and visible.
   - **User Revenue:** 0% (Pure platform passive income).
   - **No Close Button:** Un-dismissible, persistent mobile placement.

2. **Tier 2: Rewarded Video Ads (Middle Section) — 70/30 Revenue Split**
   - **Daily Limit:** 150 Short Video Ads per user per day.
   - **Ad Duration:** 10 to 15 seconds short rewarded interstitial / video clips.
   - **Anti-Cheat Callback:** Official `OnUserEarnedRewardListener`. If the user skips or closes the ad early, 0 coins are awarded. Full completion rewards **+30 Coins** per video.
   - **Casual Games Gateways:**
     - 🎡 **Lucky Spin & Win Wheel:** Spin to win 30 to 150 coins, claim via 10s video ad.
     - 🎫 **Scratch & Win Card:** Scratch to reveal prize (40-120 coins), claim via 10s video ad.
     - 🧠 **Quick Math Quiz:** Solve 2+2 question, claim +45 coins via 10s video ad.

3. **Tier 3: Cost-Per-Install (CPI) App Tasks — High Payouts**
   - User installs sponsor APKs (utility VPNs, casual games, fintech/crypto apps).
   - Admin receives: $0.50 to $2.50 USD per install.
   - User receives: 1,300 to 7,000 Coins per verified install (30% share).
   - Supported Offerwalls: CPALead, Monlix, Torox via server-to-server (`postback.php`) webhooks.

---

## 🔑 2. Ad Configuration & Credentials Handover

All ad IDs are centralized in a single configuration file:  
`android-app/app/src/main/java/com/earnhub/pro/AdConfig.java`

### Current Test State (Zero Ban Risk):
- The app compiles and runs out-of-the-box using **Google's Official Sample Test Ad Units**:
  - `ADMOB_TOP_BANNER_ID` = `ca-app-pub-3940256099942544/6300978111`
  - `ADMOB_BOTTOM_BANNER_ID` = `ca-app-pub-3940256099942544/6300978111`
  - `ADMOB_REWARDED_VIDEO_ID` = `ca-app-pub-3940256099942544/5224354917`

### Production Deployment Checklist:
When your Google Play Console / Google AdMob / AppLovin accounts are ready:
1. Open `AdConfig.java` and replace:
   ```java
   public static final String ADMOB_TOP_BANNER_ID    = "ca-app-pub-YOUR_PUBLISHER_ID/YOUR_TOP_BANNER_SLOT";
   public static final String ADMOB_BOTTOM_BANNER_ID = "ca-app-pub-YOUR_PUBLISHER_ID/YOUR_BOT_BANNER_SLOT";
   public static final String ADMOB_REWARDED_VIDEO_ID = "ca-app-pub-YOUR_PUBLISHER_ID/YOUR_REWARDED_SLOT";

   // If using AppLovin MAX Mediation:
   public static final boolean ENABLE_APPLOVIN_MAX    = true;
   public static final String APPLOVIN_SDK_KEY        = "YOUR_APPLOVIN_SDK_KEY";
   public static final String APPLOVIN_TOP_BANNER_ID  = "YOUR_APPLOVIN_TOP_BANNER_ID";
   public static final String APPLOVIN_BOT_BANNER_ID  = "YOUR_APPLOVIN_BOT_BANNER_ID";
   public static final String APPLOVIN_REWARDED_ID    = "YOUR_APPLOVIN_REWARDED_ID";
   ```
2. Open `AndroidManifest.xml` and replace:
   ```xml
   <meta-data
       android:name="com.google.android.gms.ads.APPLICATION_ID"
       android:value="ca-app-pub-YOUR_PUBLISHER_ID~YOUR_APP_ID" />
   <meta-data
       android:name="applovin.sdk.key"
       android:value="YOUR_APPLOVIN_SDK_KEY" />
   ```

---

## 🛠️ 3. How to Build & Review the Codebase

### Repository Structure:
```
earnhub-pro/
├── android-app/                       # Native Android Project
│   ├── app/
│   │   ├── build.gradle               # AdMob & AppLovin dependencies
│   │   └── src/main/
│   │       ├── AndroidManifest.xml    # Permissions, AdMob App ID & AppLovin Key
│   │       ├── java/com/earnhub/pro/
│   │       │   ├── AdConfig.java      # Master ad IDs & refresh timings
│   │       │   └── MainActivity.java  # Native SDK lifecycle, 30s auto-refresh & JS bridge
│   │       ├── res/
│   │       │   ├── layout/activity_main.xml # 50dp Top Banner + WebView + 50dp Bottom Banner
│   │       │   └── values/{colors,strings,styles}.xml # Clean light theme styling
│   │       └── assets/www/index.html  # Light theme UI, Spin, Scratch, Quiz & Wallet
│   ├── build.gradle
│   ├── settings.gradle
│   ├── gradle.properties
│   └── gradlew.bat
├── api/
│   └── postback.php                   # S2S postback listener for CPALead/Monlix
├── download.html                      # Clean web APK download landing page
├── EarnHubPro-v1.0.apk                # Ready-to-install Android APK (14.28 MB)
├── DEVELOPER_PROMPT.md                # This comprehensive master prompt
└── README.md                          # Public repository documentation
```

### Local Build Command:
```powershell
cd android-app
.\gradlew.bat assembleDebug
# Output APK: android-app\app\build\outputs\apk\debug\app-debug.apk
```

---

## ⚖️ 4. Google Play Console & Policy Safeguards
1. **14-Day Closed Testing Rule:**
   - Keep ads on Test Ad Units or limit requests during the initial 20-tester 14-day trial to protect AdMob accounts from invalid traffic flags.
2. **Ad Stacking Compliance:**
   - Top banner (Header) and Bottom banner (Footer) are cleanly separated by the main content view. No ads overlap clickable UI buttons.
3. **No Unrealistic Claims on Store Listing:**
   - Store title should be: *EarnHub Pro — Rewards & Mini-Games* (avoids aggressive get-rich-quick trigger keywords in Google Play review algorithms).
