// Syfte: Skapar Material 3-tema baserat på användarens preferenser (mörkt läge)
//        och en seed-färg för konsekvent färgsättning.
import 'package:flutter/material.dart';
import '../core/user_prefs.dart';

ThemeData buildTheme(UserPrefs prefs) {
  final seed = Colors.indigo;
  final base = ColorScheme.fromSeed(
    seedColor: seed,
    brightness: prefs.darkMode ? Brightness.dark : Brightness.light,
  );
  final border = OutlineInputBorder(
    borderRadius: BorderRadius.circular(12),
    borderSide: BorderSide(color: base.outlineVariant),
  );
  return ThemeData(
    colorScheme: base,
    useMaterial3: true,
    visualDensity: VisualDensity.adaptivePlatformDensity,
    appBarTheme: AppBarTheme(
      backgroundColor: base.surface,
      foregroundColor: base.onSurface,
      elevation: 0,
      centerTitle: false,
      titleTextStyle: TextStyle(
        color: base.onSurface,
        fontWeight: FontWeight.w600,
        fontSize: 18,
      ),
    ),
    inputDecorationTheme: InputDecorationTheme(
      filled: true,
      fillColor: base.surfaceContainerHighest,
      border: border,
      enabledBorder: border,
      focusedBorder: border.copyWith(
        borderSide: BorderSide(color: base.primary, width: 2),
      ),
      contentPadding: const EdgeInsets.symmetric(horizontal: 12, vertical: 12),
    ),
    cardTheme: CardThemeData(
      elevation: 0,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      margin: const EdgeInsets.all(0),
      color: base.surfaceContainerHigh,
    ),
    elevatedButtonTheme: ElevatedButtonThemeData(
      style: ElevatedButton.styleFrom(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
      ),
    ),
    dividerTheme: DividerThemeData(
      color: base.outlineVariant,
      thickness: 1,
      space: 16,
    ),
    textTheme: Typography.material2021(platform: TargetPlatform.android)
        .black
        .apply(
          bodyColor: base.onSurface,
          displayColor: base.onSurface,
        )
        .copyWith(
          titleLarge: const TextStyle(fontWeight: FontWeight.w600),
          titleMedium: const TextStyle(fontWeight: FontWeight.w600),
          labelLarge: const TextStyle(fontWeight: FontWeight.w600),
        ),
  );
}
