import 'package:flutter/material.dart';

import '../theme/app_theme.dart';

class ScreenTwo extends StatefulWidget {
  const ScreenTwo({
    super.key,
    required this.receivedText,
  });

  final String receivedText;

  @override
  State<ScreenTwo> createState() => _ScreenTwoState();
}

class _ScreenTwoState extends State<ScreenTwo> {
  late TextEditingController _controller;

  @override
  void initState() {
    super.initState();
    _controller = TextEditingController(text: widget.receivedText);
  }

  @override
  void didUpdateWidget(covariant ScreenTwo oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.receivedText != widget.receivedText) {
      _controller.text = widget.receivedText;
    }
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
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
              Text('Screen 2', style: AppTheme.headingLarge),
              const SizedBox(height: 8),
              Text('Page 2 / 2', style: AppTheme.labelMuted),
              const SizedBox(height: 32),
              TextField(
                controller: _controller,
                style: AppTheme.bodyRegular,
                decoration: const InputDecoration(
                  hintText: 'Text received from Screen 1',
                  hintStyle: AppTheme.labelMuted,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
