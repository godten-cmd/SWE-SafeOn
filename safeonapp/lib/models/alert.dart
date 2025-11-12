import 'package:flutter/material.dart';

class SafeOnAlert {
  const SafeOnAlert({
    required this.title,
    required this.description,
    required this.timestamp,
    required this.severity,
  });

  final String title;
  final String description;
  final DateTime timestamp;
  final AlertSeverity severity;
}

enum AlertSeverity { low, medium, high }

extension AlertSeverityExtension on AlertSeverity {
  Color get color {
    switch (this) {
      case AlertSeverity.low:
        return const Color(0xFF2ECC71);
      case AlertSeverity.medium:
        return const Color(0xFFF1C40F);
      case AlertSeverity.high:
        return const Color(0xFFE74C3C);
    }
  }

  String get label {
    switch (this) {
      case AlertSeverity.low:
        return 'Low';
      case AlertSeverity.medium:
        return 'Medium';
      case AlertSeverity.high:
        return 'High';
    }
  }
}
