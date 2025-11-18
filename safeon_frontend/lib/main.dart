import 'package:flutter/material.dart';

import 'screens/dashboard_screen.dart';
import 'screens/login_screen.dart';
import 'screens/onboarding_screen.dart';
import 'theme/app_theme.dart';

void main() {
  runApp(const SafeOnApp());
}

class SafeOnApp extends StatefulWidget {
  const SafeOnApp({super.key});

  @override
  State<SafeOnApp> createState() => _SafeOnAppState();
}

class _SafeOnAppState extends State<SafeOnApp> {
  bool _completedOnboarding = false;
  bool _authenticated = false;
  String _userEmail = 'Godten@example.com';
  String _userNickname = 'Young';
  String _userPassword = 'Secur3Pass!';

  void _completeOnboarding() {
    setState(() => _completedOnboarding = true);
  }

  void _handleLoginSuccess(String email, String password, String nickname) {
    setState(() {
      _authenticated = true;
      _userEmail = email;
      _userPassword = password;
      _userNickname = nickname;
    });
  }

  void _handleProfileUpdated(String email, String password, String nickname) {
    setState(() {
      _userEmail = email;
      _userPassword = password;
      _userNickname = nickname;
    });
  }

  void _handleLogout() {
    setState(() {
      _authenticated = false;
      _completedOnboarding = true;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'SafeOn',
      debugShowCheckedModeBanner: false,
      theme: buildSafeOnTheme(),
      home: _completedOnboarding
          ? _authenticated
              ? DashboardScreen(
                  onLogout: _handleLogout,
                  onProfileUpdated: _handleProfileUpdated,
                  userEmail: _userEmail,
                  userNickname: _userNickname,
                  userPassword: _userPassword,
                )
              : LoginScreen(
                  onLoginSuccess: _handleLoginSuccess,
                  initialEmail: _userEmail,
                  initialPassword: _userPassword,
                  initialNickname: _userNickname,
                )
          : OnboardingScreen(onContinue: _completeOnboarding),
    );
  }
}
