# 🚀 EarnHub Pro — Master Developer Prompt & Ad Network Integration Specification

> **Instructions for the Project Owner:** Copy the entire prompt below and send it directly to your hired developer. Everything they need to know—from account creation to link extraction and postback configuration—is documented here in exact detail.

---

```markdown
# 📋 DEVELOPER BRIEF: Ad Network & CPA Offerwall Setup for "EarnHub Pro"

Dear Developer,

We are launching **EarnHub Pro**, a high-converting, mobile-first Rewarded Web App (PWA) and Android WebView platform that combines a **Multi-Network Ad-Watch Engine (PTC)** and a **CPI App-Install Offerwall System** with a 70% Admin / 30% User revenue-sharing economy.

The frontend application, anti-cheat tab-focus engine, and server-side postback handlers are **already built and live** in the GitHub repository:
👉 **Repository:** https://github.com/Ikram4545/earnhub-pro

Your primary objective is to **create publisher/affiliate accounts** on our designated ad networks, configure the payout wallets, extract the direct tracking links/scripts, and connect the automated Server-to-Server (S2S) Postback webhooks.

---

## 🎯 YOUR DELIVERABLES (CHECKLIST)

Please complete the following 4 milestones:

- [ ] **Milestone 1:** Create and verify Publisher accounts on the 10 networks listed below.
- [ ] **Milestone 2:** Generate and test all Direct SmartLinks, Banner HTML tags, and Offerwall Iframes.
- [ ] **Milestone 3:** Configure the S2S Postback Webhook URL inside CPALead, Monlix, and TimeWall dashboards.
- [ ] **Milestone 4:** Deliver the completed `ad_networks_config.json` file back to us.

---

## 🌐 REQUIRED AD NETWORKS & EXACT LINK SPECIFICATIONS

### 1️⃣ Adsterra (https://publishers.adsterra.com/)
* **Account Type:** Publisher (Category: Non-Adult / Mainstream).
* **Required Links & Units:**
  1. **Direct Link #1:** Dashboard -> Direct Links -> Add New -> Mainstream -> Copy URL.
  2. **Direct Link #2:** Repeat to create a second distinct direct link.
  3. **320x50 Banner Script:** Dashboard -> Websites -> Add Website -> Ad Unit: 320x50 -> Copy JS Code.
  4. **Popunder Code (OnClick):** Ad Unit: Popunder -> Copy JS Code.

### 2️⃣ Monetag (https://monetag.com/)
* **Account Type:** Publisher.
* **Required Links & Units:**
  1. **SmartLink #1:** Direct Links -> Create Direct Link -> Copy URL.
  2. **SmartLink #2:** Create a second SmartLink for rotation.
  3. **In-Page Push / Rewarded Interstitial:** Sites -> Add Site -> In-Page Push -> Get Tag.

### 3️⃣ CPALead (https://www.cpalead.com/)
* **Account Type:** Affiliate / Publisher.
* **Required Links & Units:**
  1. **Hosted Offerwall Link:** Offerwalls -> Create Offerwall -> Name: `EarnHub Offerwall` -> Currency: `Coins` -> Conversion: `100 Coins = $0.05` -> Check SubID parameter as `subid`. Copy Hosted URL.
  2. **3 High-Paying CPI Direct Offers:** Offers -> Filter: Android, Pakistan/Worldwide -> Select 3 high-paying apps (e.g. Binance, TikTok, Alibaba) -> Copy Affiliate Tracking Links.
  3. **Global Postback Configuration:** See Postback specification below.

### 4️⃣ Monlix (https://monlix.com/)
* **Account Type:** Offerwall Publisher.
* **Required Links & Units:**
  1. **Iframe Embed URL:** Apps -> Create App -> Type: Web/PWA -> Copy Iframe link containing `userId={{USER_ID}}`.
  2. **Secret Key & App ID:** Copy App ID and Secret Token for webhook signature validation.

### 5️⃣ HilltopAds (https://hilltopads.com/)
* **Account Type:** Publisher.
* **Required Links:**
  1. **Direct Link:** Manage Sites & Zones -> Add Direct Link -> Mainstream.

### 6️⃣ TimeWall (https://timewall.io/)
* **Account Type:** Publisher Widget.
* **Required Links:**
  1. **Micro-Task Wall URL:** Settings -> Widget URL with `{subid}` parameter.

### 7️⃣ Torox / OfferToro (https://torox.io/)
* **Account Type:** Offerwall Publisher (Mobile Gaming CPI).
* **Required Links:**
  1. **Gaming Offerwall URL:** Copy Web/PWA integration link.

### 8️⃣ MyLead (https://mylead.global/)
* **Account Type:** Publisher.
* **Required Links:**
  1. **SmartLink (CPI/CPA):** Campaigns -> SmartLinks -> Mobile Downloads.

### 9️⃣ Start.io (https://www.start.io/)
* **Account Type:** Developer / Publisher.
* **Required Links:**
  1. **Rewarded Web Link / App ID:** For mobile interstitial triggers.

### 🔟 PropellerAds (https://propellerads.com/)
* **Account Type:** Publisher.
* **Required Links:**
  1. **Direct SmartLink:** Sites -> Add Zone -> Direct Link.

---

## 🔗 SERVER-TO-SERVER (S2S) POSTBACK SPECIFICATION

Inside CPALead, Monlix, and TimeWall dashboards, you must configure our Postback Webhook endpoint so users get credited automatically when an app install or survey completes.

### Postback URL Format:
```
https://earnhub.omniwiremedia.com/api/postback.php?user_id={subid}&reward={payout}&secret=ehp_live_99a&offer_name={campaign_name}&tx_id={txid}
```

### Parameter Mapping:
* `{subid}` or `{uid}` -> User's internal ID (e.g. `Ali_Pak786`).
* `{payout}` or `{amount}` -> Amount in Coins to credit.
* `secret=ehp_live_99a` -> Security token to prevent spoofed calls.
* `{txid}` -> Unique transaction ID from network (for duplicate prevention).

---

## 📦 FINAL OUTPUT FORMAT FOR DELIVERY

Please deliver your completed work as a clean JSON file structured as follows:

```json
{
  "adsterra": {
    "direct_link_1": "https://www.highcpmgate.com/...",
    "direct_link_2": "https://www.highcpmgate.com/...",
    "banner_320x50_code": "<script ...></script>",
    "popunder_code": "<script ...></script>"
  },
  "monetag": {
    "smartlink_1": "https://otergulu.net/...",
    "smartlink_2": "https://otergulu.net/...",
    "inpage_push_code": "<script ...></script>"
  },
  "cpalead": {
    "offerwall_url": "https://fasttracklink.com/offerwall/...",
    "direct_app_1": "https://cpalead.com/offer/...",
    "direct_app_2": "https://cpalead.com/offer/...",
    "postback_configured": true
  },
  "monlix": {
    "app_id": "YOUR_APP_ID",
    "secret_key": "YOUR_SECRET_KEY",
    "iframe_url": "https://monlix.com/offerwall/..."
  },
  "hilltopads": {
    "direct_link": "https://hilltopads.com/..."
  },
  "timewall": {
    "widget_url": "https://timewall.io/..."
  },
  "torox": {
    "offerwall_url": "https://torox.io/..."
  },
  "mylead": {
    "smartlink": "https://mylead.global/..."
  }
}
```

---

## 🛡️ ANTI-BAN & SECURITY GUIDELINES

1. **IP Cleanliness:** When creating publisher accounts, do NOT use datacenter proxies, Tor, or free VPNs. Use a clean residential IP address.
2. **Payment Method:** In all accounts, set the payment payout method to **Binance USDT (TRC-20)** or **Bank Wire** with identical beneficiary names to avoid KYC verification holds.
3. **Traffic Quality:** All ad links will receive genuine user traffic with anti-cheat tab-focus verification (no automated bot clicks).
```
