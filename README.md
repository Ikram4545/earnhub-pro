# EarnHub Pro — Production PWA & WebView Platform

**EarnHub Pro** is a mobile-first Progressive Web App (PWA) and WebView-ready earning ecosystem combining Multi-Network Ad-Watching (PTC / Rewarded Video) and a CPI App-Install Task Offerwall System based on a sustainable **70% Admin / 30% User** revenue-sharing model.

---

## 🚀 Live Local Server Endpoints

| Resource | Local URL |
|---|---|
| **EarnHub Pro (Full Production App)** | `http://localhost:8080/` |
| **Initial HTML Prototype** | `http://localhost:8080/prototype.html` |
| **Simulated Postback Webhook Endpoint** | `http://localhost:8080/api/postback?user_id=USER-8819&reward=1200&secret=ehp_live_99a` |

---

## 📱 Architecture & Features Overview

### 1. Visual Design & Mobile-First UX
- **Container**: `max-w-md mx-auto min-h-screen` styled with a dark fintech theme (`#090d16` background, `#131b2e` glassmorphism panels, `#10b981` emerald green accents, and `#f59e0b` gold coin highlights).
- **Sticky Top Header**: App logo, user badge, and dynamic Coin Balance + Local Currency (`PKR` / `USD`) converter pill (100 Coins = Rs 2.00 PKR).
- **Sticky Banner Ad Containers**: Top & Bottom 320x50 slots ready for Adsterra/Monetag banner script injection via Admin Panel.
- **Bottom Navigation**: 5 distinct tabs (Watch Ads, Install Apps, Refer & Earn, Wallet, Admin Panel).

### 2. Multi-Network Ad Watch Engine (Tab 1)
- **Daily Quota Tracker**: Progress bar tracking `0 / 150 Ads Watched Today` with bonus milestones at 50 (+100 coins) and 100 (+250 coins).
- **8 Active Ad Servers**: Adsterra Direct, Monetag SmartLink, Unity Video, Start.io, HilltopAds, TimeWall, PropellerAds, AdMob.
- **Anti-Cheat Verification**:
  - 15-second countdown modal with circular spinner.
  - Claim button stays strictly disabled until timer reaches 0.
  - Per-server daily cap (25 views/day per server) with live remaining counter.
  - 10-second anti-bot cooldown between consecutive ad views.

### 3. CPI App Downloading & Task System (Tab 2)
- High-reward tasks (+500 to +2,000 Coins) for apps like Alibaba, Binance, TikTok Lite, Opera GX, Ludo Star 2.
- **Two Verification Modes**:
  - **Auto-Timer Mode**: 20-second background verification timer.
  - **Screenshot Upload Mode**: User submits registration/install screenshot proof into Admin queue.
- **Offerwall Iframe Integration**: Dedicated tabs for CPALead, Monlix, and TimeWall iframes.
- **Postback URL Handler**: Server-to-server callback simulation with live test webhook tool.

### 4. Referral System (Tab 3)
- Unique referral link & code (`EARN-9821`).
- 1-Click Copy, WhatsApp, Telegram, and Native OS share.
- 10% lifetime commission on all referral earnings.
- Real-time referral earnings ledger and simulation trigger.

### 5. User Wallet & Payout System (Tab 4)
- Live dual currency display (PKR & USDT).
- Withdrawal form supporting:
  - **Easypaisa** (03XX-XXXXXXX)
  - **JazzCash** (03XX-XXXXXXX)
  - **SadaPay / NayaPay**
  - **Binance USDT** (TRC-20 / BEP-20)
- Configurable minimum cashout threshold (default: 25,000 Coins = Rs 500 PKR).
- Withdrawal status ledger (Pending, Approved, Rejected) synced with Admin panel.

### 6. Superuser Admin Panel (Tab 5)
- **70/30 Economy Calculator**: Live projected CPM revenue vs Admin profit vs User payout.
- **Ad Network Manager (CRUD)**: Add, edit, pause, and delete ad servers dynamically.
- **Live Banner Injector**: Paste raw 320x50 HTML/JS script tags directly into the UI.
- **App Task Manager (CRUD)**: Add and delete app install tasks.
- **Screenshot Verification Desk**: 1-Click Approve/Reject for user-submitted proofs.
- **Fraud Control & Payouts**: 1-Click Approve/Reject payouts, IP address logging, and 1-Click Ban User for multi-account abuse.

---

## 🛠️ How to Convert to an Android APK (WebView)

### Option A: Using Android Studio WebView (Recommended)
1. Open Android Studio -> New Project -> **Empty Views Activity**.
2. In `app/src/main/AndroidManifest.xml`, add internet permission:
   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   ```
3. In `activity_main.xml`:
   ```xml
   <WebView
       android:id="@+id/webView"
       android:layout_width="match_parent"
       android:layout_height="match_parent" />
   ```
4. In `MainActivity.java`:
   ```java
   WebView webView = findViewById(R.id.webView);
   webView.getSettings().setJavaScriptEnabled(true);
   webView.getSettings().setDomStorageEnabled(true);
   webView.loadUrl("https://your-domain.com/"); // or your local IP:8080
   ```

### Option B: Using Capacitor (1-Command PWA to APK)
```bash
npm install @capacitor/core @capacitor/cli @capacitor/android
npx cap init "EarnHub Pro" "com.earnhub.pro" --web-dir "."
npx cap add android
npx cap open android
```
Build your release signed APK directly from Android Studio!
