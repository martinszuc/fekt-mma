import 'package:flutter/material.dart';

import '../theme/app_theme.dart';

class ScreenOne extends StatefulWidget {
  const ScreenOne({
    super.key,
    required this.onTextSent,
  });

  final void Function(String text) onTextSent;

  @override
  State<ScreenOne> createState() => _ScreenOneState();
}

class _ScreenOneState extends State<ScreenOne> {
  final TextEditingController _controller = TextEditingController();

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  void _onSendPressed() {
    final text = _controller.text.trim();
    if (text.isEmpty) return;

    widget.onTextSent(text);
    _controller.clear();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppTheme.backgroundDark,
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(24),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Text('Screen 1', style: AppTheme.headingLarge),
              const SizedBox(height: 8),
              Text('Page 1 / 2', style: AppTheme.labelMuted),
              const SizedBox(height: 32),
              TextField(
                controller: _controller,
                style: AppTheme.bodyRegular,
                decoration: const InputDecoration(
                  hintText: 'Enter text to send...',
                  hintStyle: AppTheme.labelMuted,
                ),
              ),
              const SizedBox(height: 24),
              ElevatedButton(
                onPressed: _onSendPressed,
                child: const Text('Send'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
