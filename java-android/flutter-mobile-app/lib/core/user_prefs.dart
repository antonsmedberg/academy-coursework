// Syfte: Enkel central tillståndsmodell för appen (namn, mörkt läge, volym)
//        med persistens via SharedPreferences. Används av hela appen.
// Invarians: volume ∈ [0.0, 1.0]. NotifyListeners vid ändringar.
import 'package:flutter/foundation.dart';
import 'package:shared_preferences/shared_preferences.dart';

class UserPrefs extends ChangeNotifier {
  static const _kName = 'name';
  static const _kDark = 'darkMode';
  static const _kVolume = 'volume';

  String _name;
  bool _darkMode;
  double _volume; // 0.0 - 1.0

  UserPrefs({String name = '', bool darkMode = false, double volume = 0.5})
      : _name = name,
        _darkMode = darkMode,
        _volume = volume.clamp(0.0, 1.0);

  String get name => _name;
  bool get darkMode => _darkMode;
  double get volume => _volume;

  set name(String v) {
    if (v != _name) {
      _name = v;
      notifyListeners();
    }
  }

  set darkMode(bool v) {
    if (v != _darkMode) {
      _darkMode = v;
      notifyListeners();
    }
  }

  set volume(double v) {
    final nv = v.clamp(0.0, 1.0);
    if (nv != _volume) {
      _volume = nv;
      notifyListeners();
    }
  }

  static Future<UserPrefs> load() async {
    final prefs = await SharedPreferences.getInstance();
    return UserPrefs(
      name: prefs.getString(_kName) ?? '',
      darkMode: prefs.getBool(_kDark) ?? false,
      volume: prefs.getDouble(_kVolume) ?? 0.5,
    );
  }

  Future<void> save() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_kName, _name.trim());
    await prefs.setBool(_kDark, _darkMode);
    await prefs.setDouble(_kVolume, _volume);
  }
}
