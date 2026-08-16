// Syfte: Andra sidan som visar sparade värden (namn, mörkt läge, volym)
//        samt en nätbild. Tillbaka-knapp återgår till Profil-sidan.
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../../../core/user_prefs.dart';
import '../../../core/state_scope.dart';

class PreviewPage extends StatelessWidget {
  const PreviewPage({super.key});

  @override
  Widget build(BuildContext context) {
    final prefs = StateScope.of<UserPrefs>(context);

    return Scaffold(
      appBar: AppBar(title: const Text('Reflector · Förhandsvisning')),
      body: SafeArea(
        child: Center(
          child: SingleChildScrollView(
            padding: const EdgeInsets.all(16),
            child: ConstrainedBox(
              constraints: const BoxConstraints(maxWidth: 560),
              child: Card(
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      // Network image (semantiskt label för tillgänglighet)
                      ClipRRect(
                        borderRadius: BorderRadius.circular(12),
                        child: Image.network(
                          'https://picsum.photos/seed/reflector/900/400',
                          height: 180,
                          width: double.infinity,
                          fit: BoxFit.cover,
                          semanticLabel: 'Exempelbild från internet',
                          gaplessPlayback: true,
                          frameBuilder: (context, child, frame, wasSynchronouslyLoaded) {
                            if (wasSynchronouslyLoaded) return child;
                            return AnimatedOpacity(
                              duration: const Duration(milliseconds: 250),
                              opacity: frame == null ? 0 : 1,
                              child: child,
                            );
                          },
                        ),
                      ),
                      const SizedBox(height: 16),
                      AnimatedSwitcher(
                        duration: const Duration(milliseconds: 200),
                        child: Text(
                          prefs.name.isEmpty
                              ? 'Ingen data sparad'
                              : 'Hej, ${prefs.name}! 👋',
                          key: ValueKey(prefs.name),
                          style: Theme.of(context).textTheme.headlineSmall,
                          textAlign: TextAlign.center,
                        ),
                      ),
                      const SizedBox(height: 8),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          const Icon(Icons.dark_mode_outlined),
                          const SizedBox(width: 8),
                          Text(
                              prefs.darkMode ? 'Mörkt läge: På' : 'Mörkt läge: Av'),
                        ],
                      ),
                      const SizedBox(height: 8),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          const Icon(Icons.volume_up_outlined),
                          const SizedBox(width: 8),
                          Text('Volym: ${(prefs.volume * 100).round()}%'),
                        ],
                      ),
                      const SizedBox(height: 24),
                      ElevatedButton.icon(
                        onPressed: () => context.go('/'),
                        icon: const Icon(Icons.arrow_back),
                        label: const Text('Tillbaka till Profil'),
                      )
                    ],
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
