import 'package:flutter_test/flutter_test.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:reflector/core/user_prefs.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  test('UserPrefs saves and loads correctly', () async {
    SharedPreferences.setMockInitialValues({});

    final prefs = await UserPrefs.load();
    expect(prefs.name, '');
    expect(prefs.darkMode, false);
    expect(prefs.volume, 0.5);

    prefs
      ..name = 'Alice'
      ..darkMode = true
      ..volume = 0.7;
    await prefs.save();

    final reloaded = await UserPrefs.load();
    expect(reloaded.name, 'Alice');
    expect(reloaded.darkMode, true);
    expect((reloaded.volume - 0.7).abs() < 1e-6, true);
  });
}
