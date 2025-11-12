import 'package:flutter/material.dart';

class SafeOnColors {
  SafeOnColors._();

  static const Color primary = Color(0xFF1F6FEB);
  static const Color primaryVariant = Color(0xFF0F3FA3);
  static const Color accent = Color(0xFFFFB74D);
  static const Color success = Color(0xFF2ECC71);
  static const Color warning = Color(0xFFF1C40F);
  static const Color danger = Color(0xFFE74C3C);
  static const Color scaffold = Color(0xFFF6F8FC);
  static const Color surface = Colors.white;
  static const Color textPrimary = Color(0xFF0F172A);
  static const Color textSecondary = Color(0xFF64748B);
}

class SafeOnTextStyles {
  SafeOnTextStyles._();

  static const TextStyle headlineLarge = TextStyle(
    fontSize: 32,
    fontWeight: FontWeight.w700,
    letterSpacing: -0.5,
    color: SafeOnColors.textPrimary,
  );

  static const TextStyle headlineMedium = TextStyle(
    fontSize: 24,
    fontWeight: FontWeight.w600,
    letterSpacing: -0.3,
    color: SafeOnColors.textPrimary,
  );

  static const TextStyle title = TextStyle(
    fontSize: 18,
    fontWeight: FontWeight.w600,
    color: SafeOnColors.textPrimary,
  );

  static const TextStyle body = TextStyle(
    fontSize: 16,
    fontWeight: FontWeight.w400,
    color: SafeOnColors.textSecondary,
  );

  static const TextStyle caption = TextStyle(
    fontSize: 13,
    fontWeight: FontWeight.w500,
    color: SafeOnColors.textSecondary,
  );
}

ThemeData buildSafeOnTheme() {
  final base = ThemeData.light(useMaterial3: true);
  return base.copyWith(
    colorScheme: ColorScheme.fromSeed(
      seedColor: SafeOnColors.primary,
      primary: SafeOnColors.primary,
      secondary: SafeOnColors.accent,
      surface: SafeOnColors.surface,
      // ignore: deprecated_member_use
      background: SafeOnColors.scaffold,
    ),
    scaffoldBackgroundColor: SafeOnColors.scaffold,
    textTheme: base.textTheme.copyWith(
      headlineLarge: SafeOnTextStyles.headlineLarge,
      headlineMedium: SafeOnTextStyles.headlineMedium,
      titleLarge: SafeOnTextStyles.title,
      bodyLarge: SafeOnTextStyles.body,
      bodyMedium: SafeOnTextStyles.body,
      labelMedium: SafeOnTextStyles.caption,
    ),
    appBarTheme: const AppBarTheme(
      backgroundColor: SafeOnColors.scaffold,
      elevation: 0,
      centerTitle: false,
      foregroundColor: SafeOnColors.textPrimary,
      titleTextStyle: SafeOnTextStyles.title,
    ),
    bottomNavigationBarTheme: const BottomNavigationBarThemeData(
      backgroundColor: SafeOnColors.surface,
      selectedItemColor: SafeOnColors.primary,
      unselectedItemColor: SafeOnColors.textSecondary,
      showUnselectedLabels: true,

      type: BottomNavigationBarType.fixed,
    ),
    cardTheme: CardThemeData(
      color: SafeOnColors.surface,
      elevation: 0,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
      margin: EdgeInsets.zero,
    ),
  );
}
