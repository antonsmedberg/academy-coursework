// Syfte: Central konfiguration för GoRouter (2 rutter: "/" och "/preview").
//        Håller navigationslogik samlat och webbadresser stabila.
import 'package:flutter/widgets.dart';
import 'package:go_router/go_router.dart';
import '../features/profile/pages/profile_setup_page.dart';
import '../features/profile/pages/preview_page.dart';

GoRouter createRouter() {
  return GoRouter(
    initialLocation: '/',
    routes: [
      GoRoute(
        path: '/',
        name: 'profile_setup',
        pageBuilder: (context, state) => const NoTransitionPage(
          child: ProfileSetupPage(),
        ),
      ),
      GoRoute(
        path: '/preview',
        name: 'preview',
        pageBuilder: (context, state) {
          return CustomTransitionPage(
            child: const PreviewPage(),
            transitionDuration: const Duration(milliseconds: 220),
            reverseTransitionDuration: const Duration(milliseconds: 200),
            transitionsBuilder:
                (context, animation, secondaryAnimation, child) {
              final tween =
                  Tween<Offset>(begin: const Offset(1, 0), end: Offset.zero)
                      .chain(CurveTween(curve: Curves.easeOutCubic));
              final reverseTween =
                  Tween<Offset>(begin: const Offset(-0.2, 0), end: Offset.zero)
                      .chain(CurveTween(curve: Curves.easeOutCubic));
              return SlideTransition(
                position: animation.status == AnimationStatus.reverse
                    ? animation.drive(reverseTween)
                    : animation.drive(tween),
                child: child,
              );
            },
          );
        },
      ),
    ],
    // Simple URL strategy works by default in dev; for production you can add
    // a custom route information provider if needed.
    observers: <NavigatorObserver>[],
  );
}
