import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';

import 'theme/app_theme.dart';
import 'screens/screen_one.dart';
import 'screens/screen_two.dart';

void main() {
  runApp(const Cv3App());
}

class Cv3App extends StatelessWidget {
  const Cv3App({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'cv3 Swipe Navigation',
      theme: AppTheme.darkTheme,
      home: const _SwipeNavigationHost(),
    );
  }
}

class _SwipeNavigationHost extends StatefulWidget {
  const _SwipeNavigationHost();

  @override
  State<_SwipeNavigationHost> createState() => _SwipeNavigationHostState();
}

class _SwipeNavigationHostState extends State<_SwipeNavigationHost> {
  final PageController _pageController = PageController();
  String _textForScreenTwo = '';

  void _onTextSent(String text) {
    setState(() {
      _textForScreenTwo = text;
    });
  }

  void _onPageChanged(int index) {
    final pageNumber = index + 1;
    Fluttertoast.showToast(msg: 'Page $pageNumber');
  }

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return PageView(
      controller: _pageController,
      onPageChanged: _onPageChanged,
      children: [
        ScreenOne(onTextSent: _onTextSent),
        ScreenTwo(receivedText: _textForScreenTwo),
      ],
    );
  }
}
