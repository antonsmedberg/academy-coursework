# Reflector (Android + Web)

Reflector är ett litet experiment byggt i Flutter som undersöker hur ett användargränssnitt kan kännas lika självklart i mobilen som i webbläsaren. Appen låter dig ange namn, välja mörkt läge och justera volym. Resultatet visas direkt i en förhandsvisning med en konsekvent känsla på båda plattformar.

Målet har varit enkelhet: ett lugnt, responsivt gränssnitt utan onödiga distraktioner.

---

## Funktioner

- **Profilsidan**: Ange visningsnamn, växla mörkt läge och justera volym.  
  “Save & Preview” sparar inställningarna och öppnar förhandsvisningen.  
- **Förhandsvisningen**: Visar de sparade valen samt en bild hämtad från nätet.  
- **Lokala preferenser**: Inställningar sparas med `shared_preferences` och finns kvar även efter att appen startats om.

---

## Varför den här strukturen?

Projektet bygger på två sidor, tydliga interaktioner och navigering som fungerar likadant på Android och webb. Inga överflödiga funktioner, bara det som behövs för en stabil och smidig upplevelse.

---

## Projektstruktur

```
lib/
  main.dart
  app/        (router, tema)
  core/       (UserPrefs + Scope)
  features/profile/pages/
    profile_setup_page.dart
    preview_page.dart
web/index.html (brandat, favicon)
test/user_prefs_test.dart
```

---

## Designval

- Material 3-tema ger lugn visuell bas.  
- Maxbredd (~560 px) på webben gör text och innehåll mer lättläst.  
- Spara-knappen är inaktiverad tills formuläret är giltigt; en SnackBar bekräftar sparning.  
- Navigering på webben använder `go('/')` i stället för `pop` för att undvika tom historik.  
- Profilbilden laddas lokalt, medan förhandsvisningen använder en nätbild.

---

## Plattformsspecifikt

- **Webb**: Navigering och historik styrs via `go_router` och URL-hantering.  
- **Android**: Backstack hanteras av systemet. Appen är låst i porträttläge för konsekvent upplevelse.

---

## Tekniskt

- **Navigering**: `go_router` med två rutter (`/`, `/preview`).  
- **Tillstånd & lagring**: `UserPrefs` + `shared_preferences` (namn, mörkt läge, volym).  
- **UI**: Material 3, semantiska etiketter och formulärvalidering.  
- **Ikoner**: Android-ikoner via `flutter_launcher_icons`, favicon för webben.  
- **Utmaningar**: CORS för nätbilder på webben samt varningar om saknade ikoner (ofarligt).

---

## Kom igång

1) `flutter pub get`
2) `flutter run -d chrome` eller `flutter run -d android`
3) `flutter test`

## Bygga

- APK: `flutter build apk --release` → `build/app/outputs/flutter-apk/app-release.apk`
- Web: `flutter build web --release` → `build/web/`

---

