// Syfte: Första sidan där användaren anger namn, väljer mörkt läge och volym.
//        Lokalt banner (asset) visas. "Save & Preview" sparar och navigerar.
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../../../core/user_prefs.dart';
import '../../../core/state_scope.dart';

class ProfileSetupPage extends StatefulWidget {
  const ProfileSetupPage({super.key});

  @override
  State<ProfileSetupPage> createState() => _ProfileSetupPageState();
}

class _ProfileSetupPageState extends State<ProfileSetupPage> {
  final _formKey = GlobalKey<FormState>();
  late final TextEditingController _nameController;
  bool _canSave = false; // styr om knappen ska vara aktiv

  @override
  void initState() {
    super.initState();
    _nameController = TextEditingController();
    _nameController.addListener(() {
      final valid = (_nameController.text.trim().isNotEmpty);
      if (valid != _canSave) {
        setState(() => _canSave = valid);
      }
    });
  }

  @override
  void dispose() {
    _nameController.dispose();
    super.dispose();
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    final prefs = StateScope.of<UserPrefs>(context);
    if (_nameController.text != prefs.name) {
      _nameController.text = prefs.name;
    }
  }

  @override
  Widget build(BuildContext context) {
    final prefs = StateScope.of<UserPrefs>(context);

    Widget content = Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Text('Profil', style: Theme.of(context).textTheme.titleLarge),
            const SizedBox(height: 8),
            // Lokalt banner (asset) för tydligare uttryck
            ClipRRect(
              borderRadius: BorderRadius.circular(12),
              child: Image.asset(
                'assets/images/sample_banner.png',
                height: 150,
                fit: BoxFit.cover,
                semanticLabel: 'Lokal banner-bild',
              ),
            ),
            const SizedBox(height: 16),
            const Divider(),
            const SizedBox(height: 8),
            Form(
              key: _formKey,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  TextFormField(
                    controller: _nameController,
                    decoration: const InputDecoration(
                      labelText: 'Visningsnamn',
                      hintText: 'Ange ditt namn',
                    ),
                    validator: (v) => (v == null || v.trim().isEmpty)
                        ? 'Ange ett namn'
                        : null,
                  ),
                  const SizedBox(height: 12),
                  Text('Inställningar', style: Theme.of(context).textTheme.titleMedium),
                  const SizedBox(height: 4),
                  SwitchListTile(
                    title: const Text('Mörkt läge'),
                    value: prefs.darkMode,
                    onChanged: (val) => prefs.darkMode = val,
                  ),
                  const SizedBox(height: 4),
                  Row(
                    children: [
                      const Text('Volym'),
                      Expanded(
                        child: Slider(
                          value: prefs.volume,
                          onChanged: (v) => prefs.volume = v,
                        ),
                      ),
                      Text((prefs.volume * 100).round().toString()),
                    ],
                  ),
                  const SizedBox(height: 12),
                  ElevatedButton.icon(
                    icon: const Icon(Icons.save),
                    label: const Text('Spara & Förhandsvisa'),
                    onPressed: _canSave
                        ? () async {
                            if (_formKey.currentState!.validate()) {
                              prefs.name = _nameController.text.trim();
                              await prefs.save();
                              if (!context.mounted) return;
                              ScaffoldMessenger.of(context).showSnackBar(
                                const SnackBar(content: Text('Sparat!')),
                              );
                              context.go('/preview');
                            }
                          }
                        : null,
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );

    return Scaffold(
      appBar: AppBar(title: const Text('Reflector · Profil')),
      body: SafeArea(
        child: LayoutBuilder(
          builder: (context, constraints) {
            final isWide = constraints.maxWidth > 640;
            return SingleChildScrollView(
              padding: const EdgeInsets.all(16),
              child: Center(
                child: ConstrainedBox(
                  // Maxbredd ~560 på breda skärmar, full bredd på mobil
                  constraints: BoxConstraints(
                      maxWidth: isWide ? 560 : constraints.maxWidth),
                  child: content,
                ),
              ),
            );
          },
        ),
      ),
    );
  }
}
