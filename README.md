# Learning Dashboard

## 1. Architecture
I used MVVM + Clean Architecture (UI → ViewModel → Repository → API/Room). This keeps UI, business logic, and data access separate, so each layer can be tested and changed on its own without breaking the others. The Repository pattern also hides whether data comes from network or cache, which made offline support straightforward to add later.

## 2. Offline Support
Room is the single source of truth. On a successful fetch, course and lesson data is saved to Room. The UI always reads from Room (via a Flow), not directly from the network. If a refresh fails (e.g. no internet), the app simply keeps showing whatever was last cached, so the dashboard still works offline after the first successful load.

## 3. Security
In a production app, I would not store auth tokens in plain SharedPreferences. I'd use EncryptedSharedPreferences or the Android Keystore-backed storage (e.g. via Jetpack Security library) for access/refresh tokens, use short-lived access tokens with refresh-token rotation, and never log tokens or put them in plain Room tables.

## 4. Scale
For 1M+ users and hundreds of courses, I would:
- Add pagination/lazy-loading for the course list instead of fetching everything at once.
- Introduce a proper sync mechanism (WorkManager + ETags/timestamps) instead of full refresh-and-replace.
- Add a real backend with caching/CDN for course content, and server-side search/filtering.
- Add analytics/crash reporting (e.g. Firebase) to monitor real usage and failures.
- Introduce modularization (feature modules) so build times and team ownership stay manageable.

## 5. Second Platform (iOS/macOS)
I built this on Android. To bring it to iOS/macOS, I'd keep the same architecture: SwiftUI for the UI layer (mirroring Compose), ViewModels as ObservableObject classes, the same Repository pattern, Core Data or SwiftData instead of Room for local caching, and URLSession (or a mock service) in place of the Android mock API. The domain/business logic (progress calculation, lesson completion) is plain logic with no Android dependency, so it ports over almost as it is in concept, just rewritten in Swift.

## Run

```
./gradlew assembleDebug   # build
./gradlew installDebug    # install on connected device/emulator
```

Login: `test@learning.com` / `password123`
