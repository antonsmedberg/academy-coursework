// Syfte: Startpunkt som initierar UserPrefs (persistens), injicerar tillstånd
//        via StateScope och startar MaterialApp.router med GoRouter och tema.
import 'package:flutter/material.dart';
import 'app/router.dart';
import 'app/theme.dart';
import 'core/state_scope.dart';
import 'core/user_prefs.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  final prefs = await UserPrefs.load();
  runApp(ReflectorApp(prefs: prefs));
}

class ReflectorApp extends StatelessWidget {
  final UserPrefs prefs;
  const ReflectorApp({super.key, required this.prefs});

  @override
  Widget build(BuildContext context) {
    final router = createRouter();
    // Rebuild MaterialApp when prefs change (theme, etc).
    return StateScope<UserPrefs>(
      notifier: prefs,
      child: AnimatedBuilder(
        animation: prefs,
        builder: (context, _) {
          return MaterialApp.router(
            title: 'Reflector',
            debugShowCheckedModeBanner: false,
            theme: buildTheme(prefs),
            routerConfig: router,
          );
        },
      ),
    );
  }
}
