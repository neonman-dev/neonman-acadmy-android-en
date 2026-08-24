# Neonman Academy — Ta'lim Platformasi Android Ilovasi

**Neonman Academy** — bu zamonaviy Kotlin va Jetpack Compose texnologiyalarida yaratilgan interaktiv ta'lim platformasining rasmiy Android ilovasi. Ilova o'quvchilarga kurslarni o'rganish, testlar topshirish, ta'limiy o'yinlar o'ynash, dars jadvalini yuritish va AI chizmachilik doskasidan foydalanish imkoniyatini beradi.

---

## 📱 Paket va Loyiha Ma'lumotlari

* **Paket nomi (Application ID):** `uz.neonman.academy`
* **Arxitektura:** Clean MVVM (Model-View-ViewModel) + Single Activity (Compose Navigation)
* **Dasturlash tili:** Kotlin 100%
* **UI karkasi:** Jetpack Compose (Material 3)
* **Minimal Android versiyasi:** Android 8.0 (API 26)
* **Maqsadli Android versiyasi:** Android 15 (API 36)

---

## ✨ Asosiy Imkoniyatlar va Bo'limlar

### 1. 🔐 Avtorizatsiya va Profil (Auth)
* **Demo Kirish:** Platformani tezkor ko'rib chiqish uchun sinov rejimida kirish.
* **Telegram WebApp Integratsiyasi:** Telegram `initData` orqali xavfsiz va bir bosqichli avtorizatsiya.
* **Profil to'ldirish va tahrirlash:** Ism, familiya va tug'ilgan sanani o'zgartirish.

### 2. 📚 Kurslar va Darslar (Courses & Lessons)
* **Interaktiv Kurslar:** Darslar ro'yxatini ko'rish, tavsifi va dars materiallari bilan tanishish.
* **Test va Topshiriqlar:** Dars yakunida test topshirish va natijalarni avtomatik hisoblash.
* **Kurs Yaratish:** O'qituvchilar uchun yangi kurs va darslarni qo'lda yaratish imkoniyati.

### 3. 🛠 Platforma Imkoniyatlari (Extras Features)
* **📖 Kitoblar (Books):** O'quv va badiiy kitoblar kutubxonasi, mutolaa paneli.
* **✍️ Testlar (Tests):** Fanlar bo'yicha mustaqil bilimni sinash testlari.
* **🎮 O'yinlar (Educational Games):** Mantiqiy va ta'limiy interaktiv mini-o'yinlar.
* **📊 Taqdimotlar (Presentations):** Dars slaydlar va taqdimot materiallarini ko'rish.
* **📅 Dars Jadvali (Schedule):** Kunlar bo'yicha dars va topshiriqlar jadvalini tuzish, vaqt va izohlarni saqlash.
* **🔖 Saqlanganlar (Saved Items):** Sevimli va kerakli dars materiallarini bitta joyga saqlash.
* **🎨 AI Doska (Interactive Canvas Board):** Interaktiv chizmachilik doskasi — g'oyalar, formula va chizmalarni saqlab borish.

### 4. 🎨 Mavzular va Dizayn (Themes)
* **Neon Dark Mode:** Kiberpank uslubidagi neom-yashil va to'q rangli interfeys.
* **Light Sketch Mode:** Yorug', toza va qalam eskiz uslubidagi klassik ko'rinish.

---

## 🏗 Loyiha Strukturasi (Project Architecture)

```text
app/src/main/java/com/example/
├── data/
│   ├── api/             # Retrofit API servislari, modellari va HTTP so'rovlar
│   ├── datastore/       # UserPreferences (Token va sozlamalarni saqlash)
│   └── repository/      # Ma'lumotlar ombori va resurslar bilan ishlash
├── ui/
│   ├── components/      # Qayta ishlatiluvchi UI komponentlar (BottomNav, Card, Input)
│   ├── screens/         # Ilova ekranlari (Home, Courses, Auth, Board, Games, ...)
│   ├── theme/           # Material 3 ranglar palitrasi, shriftlar va mavzular
│   └── viewmodels/      # MVVM ViewModel va StateFlow holatlari
└── MainActivity.kt      # Asosiy ishga tushirish faoliyati va ekranlar navigatsiyasi
```

---

## 🚀 Ishga Tushirish va Yig'ish (Setup & Build)

### Talab qilinadigan muhit:
* **Android Studio:** Ladybug (2024.2.1) yoki undan yangi versiya
* **JDK:** Java 17 yoki JDK 21
* **Gradle:** Gradle Wrapper (8.x +)

### Loyihani sozlash va ishga tushirish:

1. **Repozitoriyani klon qiling yoki zip formatida yuklab oling.**
2. **Android Studio** dasturida loyihani oching.
3. Gradle bog'liqliklari avtomatik sinxronlanishini kuting (`Sync Project with Gradle Files`).
4. **.env** yoki backend server manzillarini sozlang:
   ```properties
   BASE_URL=https://your-api-domain.com/
   ```
5. Qurilma (Emulator yoki real Android telefon) ni ulang va `Run` (Shift + F10) tugmasini bosing.

---

## 📦 APK va AAB Fayl Yaratish (Release Build)

Android Studio buyruqlar satri orqali release APK yig'ish uchun:

```bash
./gradlew assembleRelease
```

Google Play Store uchun Android App Bundle (AAB) yig'ish uchun:

```bash
./gradlew bundleRelease
```

Tayyor tayyorlangan fayllar ushbu katalogda joylashadi:
`app/build/outputs/apk/release/` hamda `app/build/outputs/bundle/release/`

---

## 🔒 Xavfsizlik va Maxfiylik

* Barcha authorization tokenlar Android `DataStore` tizimida xavfsiz saqlanadi.
* Server bilan ma'lumot almashinuvi HTTPS shifrlangan tarmoq orqali amalga oshiriladi.
* Maxfiy kalitlar va server URL manzillari `BuildConfig` va `.env` fayllari orqali boshqariladi.

---

© 2026 **Neonman Academy**. Barcha huquqlar himoyalangan.
