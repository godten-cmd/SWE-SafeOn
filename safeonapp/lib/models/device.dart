class SafeOnDevice {
  const SafeOnDevice({
    required this.name,
    required this.location,
    required this.status,
    required this.connectionStrength,
    required this.isOnline,
    required this.icon,
  });

  final String name;
  final String location;
  final String status;
  final double connectionStrength;
  final bool isOnline;
  final String icon;
}
