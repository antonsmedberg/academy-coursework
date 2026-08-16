// Syfte: Enkel wrapper kring InheritedNotifier för att exponera ett ChangeNotifier
//        (t.ex. UserPrefs) i widgetträdet utan externa paket.
// Användning: StateScope<UserPrefs>(notifier: prefs, child: ...);
import 'package:flutter/widgets.dart';

/// A tiny dependency-free state scope built on InheritedNotifier
/// to expose a ChangeNotifier down the tree.
class StateScope<T extends Listenable> extends InheritedNotifier<T> {
  const StateScope({super.key, required super.notifier, required super.child});

  static N of<N extends Listenable>(BuildContext context) {
    final widget = context.dependOnInheritedWidgetOfExactType<StateScope<N>>();
    assert(widget != null, 'StateScope<$N> not found in context');
    return widget!.notifier as N;
  }
}
