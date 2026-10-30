# 🚀 EarnHub Pro - Production Rewarded Ads & Offerwall Platform

[![GitHub Pages](https://img.shields.io/badge/Live%20Demo-GitHub%20Pages-brightgreen?style=for-the-badge&logo=github)](https://ikram4545.github.io/earnhub-pro/)
[![cPanel Deployment](https://img.shields.io/badge/cPanel%20Subdomain-earnup.drivewithbilal.com-blue?style=for-the-badge&logo=cpanel)](https://earnup.drivewithbilal.com/)
[![License](https://img.shields.io/badge/License-Proprietary-red?style=for-the-badge)]()

EarnHub Pro is an enterprise-grade Rewarded Ad-Watching (PTC), CPA Offerwall, and Interactive Game Monetization Platform designed for mobile-first PWA and web browsers.

---

## 🌐 Live Demos
- 🔗 **GitHub Pages (Instant Preview):** [https://ikram4545.github.io/earnhub-pro/](https://ikram4545.github.io/earnhub-pro/)
- 🔗 **Production Subdomain (cPanel):** [https://earnup.drivewithbilal.com/](https://earnup.drivewithbilal.com/)

---

## 📸 Platform Showcase

### 1. Marketing Homepage & Earning Calculator
![Homepage](landing_page_design.png)

### 2. Mobile App View & 10 Ad Server Grid
![Mobile App](mobile_app_design.png)

### 3. SEO Articles Hub (Urdu & English Guides)
![Articles Hub](articles_hub_design.png)

### 4. Interactive Lucky Spin & Win Game
![Lucky Spin](lucky_spin_design.png)

### 5. Desktop Web Dashboard
![Desktop Web](desktop_web_design.png)

---

## 🛠️ Architecture & Features

1. **3-Pillar Ecosystem:**
   - **Marketing Homepage:** High-converting hero section, live verified payouts ticker, dynamic earning slider, and trust badges.
   - **Earning App Dashboard:** 10 Independent Ad Network Servers, Anti-Cheat Tab Focus Blur detection, Canvas Lucky Spin Wheel, Scratch Card, CPI App Downloads, and Multi-Gateway Cashout (Easypaisa, JazzCash, SadaPay, Binance USDT).
   - **SEO Articles Hub:** High-traffic guides in Urdu/English with built-in banner monetization to drive free Google organic traffic.

2. **Revenue Economics:**
   - **70% Admin / 30% User Revenue Split** built into reward calculation engine.
   - Built-in anti-fraud prevention (daily limits, tab blur freeze).

3. **Multi-Network Support:**
   - Adsterra (Direct Smartlinks + Social Bar)
   - Monetag (Smartlink + Direct URL)
   - HilltopAds (Direct URL)
   - CPALead & Monlix (Offerwalls + S2S Postbacks)
   - TimeWall & Torox

---

## 👨‍💻 Developer Quickstart

For ad networks publisher account creation and link integration, see:
👉 **[DEVELOPER_PROMPT.md](DEVELOPER_PROMPT.md)**

### Local Run:
```bash
python server.py
# Open http://localhost:8080/
```

### S2S Postback Endpoint:
`https://earnup.drivewithbilal.com/api/postback.php?network=monlix&user_id={subId}&payout={reward_amount}&tx_id={transId}&secret=EarnHubSecret2026`
