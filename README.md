# Parent App 📱

<div align="center">

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.2.21-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-BOM-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Clean-FF6F00?style=for-the-badge)
![License](https://img.shields.io/badge/License-Apache%202.0-blue?style=for-the-badge)

**Parent App** — an offline-first Android WebView app (package **`com.pw.parent`**) built with
Jetpack Compose, Material 3, Room Database and Firebase Cloud Messaging on top of a reusable
WebView container template.

[Configuration](#-configuration) • [Features](#-key-features) • [Tech Stack](#-tech-stack) • [Getting Started](#-getting-started) • [CI/CD & Releases](#-cicd--signing-secrets)

</div>

---

## 📱 Overview

**Parent App ships the Parent web app as a native Android app.** It wraps the web app in a
production-shaped container: native Compose chrome around a hardened WebView, offline caching
and a retryable offline screen, scoped camera/mic/geolocation, a Room database for local data,
and FCM push wired to a JavaScript bridge.

### App identity

| | |
| :--- | :--- |
| App name (launcher label) | **Parent App** |
| Application ID / package | **`com.pw.parent`** |
| Kotlin namespace & source root | `com.pw.parent` → `app/src/main/java/com/pw/parent/` |
| JavaScript bridge | `window.ParentApp.*` |
| Notification channel | `parent_app_notifications` |
| Room database file | `parent_app_database` |

The identity above comes from the `app.*` block in `gradle.properties`; the web URL the app loads
comes from `.env` (`TARGET_URL`). **`.env.example` currently contains a placeholder URL
(`https://parent-app.example.com`) — set the real Parent App deployment URL before building.**
See **[Configuration](#-configuration)** for details.

---

## ✨ Key Features

- 🎨 **Material 3 & Edge-to-Edge**: Modern UI design following the latest Material Design 3 guidelines, dynamic theming with dark mode support, and seamless edge-to-edge drawing.
- ⚡ **Offline-First Reliability**: Integrated Room Database along with WebView ServiceWorker caching ensuring fast load times and uninterrupted offline experience.
- 🔔 **Push Notifications**: Firebase Cloud Messaging (FCM) plumbing with a configurable notification channel and a JavaScript bridge the web app can call — activate it by adding `google-services.json` (see below).
- 🔄 **Modern State Management**: MVVM architecture utilizing Kotlin Coroutines, `StateFlow`, and `collectAsStateWithLifecycle`.
- 🧩 **Config-Driven Rebranding**: App name, application ID, version, JS bridge name, notification channel and target URL all come from `gradle.properties` / `.env`.
- 🔒 **Scoped Permissions**: camera, microphone and geolocation are granted only to the configured origin, requested lazily when the page needs them.
- 🛡️ **Automated CI/CD Workflows**: GitHub Actions build a signed Release APK, Debug APK and Play Store AAB on every push, publish GitHub Releases on tags, and run a build check on pull requests with strict secret validation.

---

## 🛠 Tech Stack

| Layer | Technologies |
| :--- | :--- |
| **Language** | Kotlin 2.x |
| **UI Framework** | Jetpack Compose (BOM), Material 3, Accompanist |
| **Architecture** | MVVM (Model-View-ViewModel) + Repository Pattern |
| **Local Storage** | Room Database + SQLite, Android Keystore |
| **Networking & API**| Retrofit, OkHttp 4, Moshi (Kotlin codegen) |
| **Push Notifications** | Firebase Cloud Messaging (FCM) |
| **Build System** | Gradle 9.3.1 (Kotlin DSL), Android Gradle Plugin (AGP) |
| **Testing** | Robolectric, Roborazzi, JUnit 4, AndroidX Test |

---

## 📂 Project Structure

```text
pwparentapp/                    # Parent App repository root
├── .github/
│   └── workflows/
│       ├── build.yml          # Build & Sign APK / AAB on push to main
│       ├── release.yml        # Build & Publish to GitHub Releases on tag (v*)
│       └── ci.yml             # Build check on pull requests
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── assets/        # App assets & graphics
│   │   │   ├── java/com/pw/parent/
│   │   │   │   ├── MainActivity.kt          # Compose host + ParentAppScreen (WebView)
│   │   │   │   ├── data/      # ParentAppDatabase, ParentAppDao, ParentAppRepository (Room)
│   │   │   │   ├── ui/        # ParentAppViewModel, ParentAppJavascriptInterface, theme/
│   │   │   │   └── utils/     # MyFirebaseMessagingService, ParentAppNotificationHelper
│   │   │   ├── res/           # Layouts, mipmaps, drawables, strings
│   │   │   └── AndroidManifest.xml
│   │   └── test/              # Local JVM and Robolectric unit tests
│   ├── build.gradle.kts       # App module configuration & dependencies
│   └── proguard-rules.pro     # ProGuard / R8 rules
├── gradle/
│   ├── libs.versions.toml     # Version catalog
│   └── wrapper/               # Gradle wrapper executable & properties
├── gradle.properties          # ⭐ App configuration (app.name=Parent App, app.id=com.pw.parent, ...)
├── .env.example               # ⭐ TARGET_URL for the Parent web app (copy to .env)
├── build.gradle.kts           # Root build configuration
├── settings.gradle.kts        # Project settings & plugin resolution
└── README.md                  # Documentation
```

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Ladybug (2024.2.1+) or newer recommended.
- **JDK**: Java 17 or Java 21 (Temurin / Eclipse Adoptium recommended).
- **Android SDK**: API Level 35 (compileSdk & targetSdk), Minimum API Level 23.

### Local Installation & Build

1. **Clone the repository:**
   ```bash
   git clone https://github.com/gothwadtech/pwparentapp.git
   cd pwparentapp
   ```

2. **Setup environment variables:**
   ```bash
   cp .env.example .env
   ```

3. **Build the Debug APK:**
   ```bash
   chmod +x ./gradlew
   ./gradlew assembleDebug
   ```
   The debug APK will be generated at:
   `app/build/outputs/apk/debug/app-debug.apk`

4. **Run Unit Tests:**
   ```bash
   ./gradlew testDebugUnitTest
   ```
   The Roborazzi screenshot test records its golden image with
   `./gradlew recordRoborazziDebug` (written to `app/src/test/screenshots/`).

---

## 🧩 Configuration

Parent App is built on a **reusable WebView container**, so its branding lives in one config
block instead of being scattered through the code. Nothing else in the project hardcodes the
branding.

### 1. The config block (`gradle.properties`)

| Key | What it controls |
| :--- | :--- |
| `app.name` | Launcher label, notification fallback title, JS bridge fallback token, CI artifact names |
| `app.id` | `applicationId` (Play Store / package identity) |
| `app.versionCode` / `app.versionName` | Local build version — CI overrides these with `-PversionCode` / `-PversionName` and auto-increments per release |
| `app.jsBridgeName` | The injected bridge name; the web app calls `window.<name>.postNotification(...)`, `.getPushToken()`, `.isDeviceOnline()`, `.saveOfflineDraft()`, `.showToast()`, `.setTheme()` |
| `app.notificationChannelId` / `Name` / `Description` | Android notification channel |
| `app.prefsName` | SharedPreferences file that caches the FCM token |

### 2. The web target (`.env`)

```bash
cp .env.example .env     # then set TARGET_URL to your web app
```

`TARGET_URL` is the **only** allowed origin: camera, microphone and geolocation requests from
any other origin are denied, and it drives the WebView's initial load. Change it and the
session/theme/caching behaviour follows automatically.

### 3. Checklist before the first release

1. Review the `app.*` block in `gradle.properties` (already set to **Parent App** / `com.pw.parent`).
2. Set `TARGET_URL` in `.env` to the real Parent App web deployment (the checked-in
   `.env.example` value is a placeholder).
3. Replace the launcher icons in `app/src/main/res/mipmap-*` and the splash logo in
   `app/src/main/res/drawable/ic_splash_logo.xml` with the final Parent App artwork.
4. Add `google-services.json` + the `google-services` plugin if the app needs push (see below).

> **Source layout:** the Kotlin `namespace`, the `applicationId` and the source tree all use
> `com.pw.parent` (`app/src/main/java/com/pw/parent/`). Internal classes carry the `ParentApp`
> prefix (`ParentAppDatabase`, `ParentAppDao`, `ParentAppRepository`, `ParentAppViewModel`,
> `ParentAppJavascriptInterface`, `ParentAppNotificationHelper`), the Compose theme is
> `ParentAppTheme` / `Theme.ParentApp`, and the Room file is `parent_app_database`.

### 4. What the template already handles

- Offline-first: `LOAD_CACHE_ELSE_NETWORK` + ServiceWorker cache when the device is offline, a
  retryable offline panel over a still-mounted WebView (page state survives a network blip).
- Permission scoping: camera/mic/geolocation only for `TARGET_URL`'s origin, asked lazily when
  the page needs them; no permission spam on first launch.
- HTML ↔ native theme sync (MutationObserver + luma fallback), dark/light, edge-to-edge insets.
- Room database for offline drafts + a notification log.
- FCM service + notification channel + `ParentApp`-style JS bridge.

---

## 🔔 Push Notifications (FCM) — setup required

The app ships with FCM code, but **push notifications stay disabled until Firebase is wired up**.
Both of these are required:

1. Add your `google-services.json` to the `app/` directory.
2. Apply the Google Services plugin — uncomment in `app/build.gradle.kts`:
   ```kotlin
   plugins {
     // ...
     alias(libs.plugins.google.services)
   }
   ```

Without them, `FirebaseApp.getApps()` is empty, the app logs
`Firebase is not configured ... Push notifications are DISABLED.`, and
`window.ParentApp.getPushToken()` returns a locally generated placeholder token.
(Previously the app silently initialised Firebase with a fake API key, so token
retrieval failed forever while every piece of the notification stack *looked* connected.)

---

## 🔐 CI/CD & Signing Secrets

The repository includes pre-configured GitHub Actions workflows for continuous integration and automated release deployments.

### Required GitHub Secrets

To build and sign Release APKs & Play Store AAB bundles automatically, add the following secrets to your GitHub repository under **Settings > Secrets and variables > Actions**:

| Secret Name | Description | Required |
| :--- | :--- | :---: |
| `RELEASE_KEYSTORE_BASE64` | Base64-encoded release `.jks` or `.keystore` file | **Yes** |
| `KEYSTORE_PASSWORD` | Password for your release keystore | **Yes** |
| `KEY_ALIAS` | Key alias name inside the keystore | Optional |
| `KEY_PASSWORD` | Password for the key alias | Optional |

> **Note**: For security, if `RELEASE_KEYSTORE_BASE64` or `KEYSTORE_PASSWORD` is not configured, the release build step will automatically abort to prevent deploying unverified or improperly signed builds.

### Generating `RELEASE_KEYSTORE_BASE64`

You can convert your local `.jks` or `.keystore` file into Base64 using:

**Linux / macOS:**
```bash
base64 -i my-release-key.jks | tr -d '\n' > keystore_base64.txt
```

**Windows (PowerShell):**
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("my-release-key.jks")) | Set-Content keystore_base64.txt
```
Copy the contents of `keystore_base64.txt` and paste it into GitHub Secrets as `RELEASE_KEYSTORE_BASE64`.

---

## 🏷️ Triggering a Release

To trigger an official GitHub Release:

1. Create a version tag locally:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```
2. The `Release to GitHub Releases` workflow will automatically:
   - Validate signing secrets.
   - Self-heal Gradle wrapper if needed.
   - Build signed Release APK, Debug APK, and Play Store AAB.
   - Publish a new GitHub Release with generated release notes and downloadable assets.

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

Distributed under the Apache License 2.0. See `LICENSE` for more information.
