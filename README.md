# Neonman Academy — Educational Platform Android Application
> Official MVP version submitted for the **President AI Award** competition.

**Neonman Academy** is the official Android app for an interactive learning platform built using modern Kotlin and Jetpack Compose technologies. The app enables students to take courses, complete quizzes, play educational games, manage class schedules, and utilize an AI drawing board.

---

## 📲 MVP & Download (Release)

You can download and test the MVP version of the application via the direct link below:

* 📥 **[Download Neonman Academy APK (v1.0.0-mvp)](https://github.com/neonman-dev/neonman-acadmy-android-en/releases/download/v1.0.0-mvp/Neonman.Academy.1.0.English.apk )**
* 📦 **[All Releases Page (GitHub Releases)](https://github.com/neonman-dev/neonman-academy-android/releases)**

---

## 📱 Package & Project Details

* **Package Name (Application ID):** `uz.neonman.academy`
* **Architecture:** Clean MVVM (Model-View-ViewModel) + Single Activity (Compose Navigation)
* **Programming Language:** Kotlin 100%
* **UI Framework:** Jetpack Compose (Material 3)
* **Minimum Android Version:** Android 8.0 (API 26)
* **Target Android Version:** Android 15 (API 36)

---

## ✨ Key Features & Modules

### 1. 🔐 Authorization & Profile (Auth)
* **Demo Login:** Guest mode to quickly explore the platform.
* **Telegram WebApp Integration:** Secure, one-step authentication via Telegram `initData`.
* **Profile Setup & Editing:** Update name, surname, and date of birth.

### 2. 📚 Courses & Lessons
* **Interactive Courses:** Browse lesson lists, read descriptions, and access study materials.
* **Quizzes & Assignments:** Take quizzes at the end of lessons with automatic grading.
* **Course Creation:** Ability for teachers to manually create new courses and lessons.

### 3. 🛠 Extra Platform Features
* **📖 Books:** Library of educational and fiction books with a reading interface.
* **✍️ Quizzes & Tests:** Subject-specific self-assessment tests.
* **🎮 Educational Games:** Interactive logic and educational mini-games.
* **📊 Presentations:** View lesson slides and presentation materials.
* **📅 Schedule:** Organize daily class schedules, set timings, and save notes.
* **🔖 Saved Items:** Bookmarks to keep essential study materials in one place.
* **🎨 AI Board (Interactive Canvas):** Interactive drawing canvas to sketch ideas, formulas, and diagrams.

### 4. 🎨 Themes & Design
* **Neon Dark Mode:** Cyberpunk-style dark interface with glowing neon-green accents.
* **Light Sketch Mode:** Clean, bright classic look styled after paper pencil sketches.

---

## 🏗 Project Architecture

```text
app/src/main/java/com/example/
├── data/
│   ├── api/            # Retrofit API services, models, and HTTP requests
│   ├── datastore/      # UserPreferences (Token and settings storage)
│   └── repository/     # Repositories for data handling and resources
├── ui/
│   ├── components/     # Reusable UI components (BottomNav, Card, Input)
│   ├── screens/        # App screens (Home, Courses, Auth, Board, Games, ...)
│   ├── theme/          # Material 3 color palettes, typography, and themes
│   └── viewmodels/     # MVVM ViewModels and StateFlow states
└── MainActivity.kt      # Main entry activity and screen navigation
```

---

## 🚀 Setup & Build

### Requirements:
* **Android Studio:** Ladybug (2024.2.1) or newer
* **JDK:** Java 17 or JDK 21
* **Gradle:** Gradle Wrapper (8.x+)

### Project Setup:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/neonman-dev/neonman-academy-android.git
   ```
2. Open the project in **Android Studio**.
3. Wait for Gradle dependencies to sync (`Sync Project with Gradle Files`).
4. Configure **.env** or backend server URLs:
   ```properties
   BASE_URL=https://your-api-domain.com/
   ```
5. Connect a device (Emulator or physical Android phone) and click `Run` (Shift + F10).

---

## 📦 Building APK & AAB (Release)

To build a release APK via CLI:

```bash
./gradlew assembleRelease
```

To build an Android App Bundle (AAB) for Google Play Store:

```bash
./gradlew bundleRelease
```

Built artifacts will be located in:
`app/build/outputs/apk/release/` and `app/build/outputs/bundle/release/`

---

## 🔒 Security & Privacy

* All authorization tokens are securely stored using Android `DataStore`.
* Server communications use encrypted HTTPS connections.
* Secret keys and server URLs are managed via `BuildConfig` and `.env` files.

---

© 2026 **NeonmanDev**. All rights reserved.
