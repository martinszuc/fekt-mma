import 'package:flutter_test/flutter_test.dart';

import 'package:cv3/main.dart';

void main() {
  testWidgets('App loads and shows Screen 1', (WidgetTester tester) async {
    await tester.pumpWidget(const Cv3App());

    expect(find.text('Screen 1'), findsOneWidget);
    expect(find.text('Page 1 / 2'), findsOneWidget);
    expect(find.text('Send'), findsOneWidget);
  });
}
