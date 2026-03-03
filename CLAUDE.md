# XPC-MMA — AI Assistant Context

Course: Mobile and Multi-platform Applications (XPC-MMA)
Institution: Brno University of Technology, 2024/25
Developer: Red Hat software engineer, professional Kotlin/Android experience

---

## Repository Structure

Branch-per-exercise monorepo. Each branch is a self-contained project.

| Branch | Platform | Description |
|--------|----------|-------------|
| `cv1`  | Android / Kotlin + Compose | Scientific calculator, MVVM |
| `cv3`  | Flutter / Dart | Two-screen swipe navigation |

---

## Coding Philosophy

### Core Rules
- **Production-ready**: clean, maintainable code from the start
- **Clarity over cleverness**: no over-engineering, no premature abstractions
- **Single responsibility**: each function/composable/widget does one thing
- **Self-documenting names**: `userInputText`, `onSendButtonClicked()` — not `txt`, `click()`
- **No magic numbers**: named constants for all sizes, colors, durations

### Comments — lowercase, explain WHY not WHAT
```kotlin
// using binary search here — dataset can reach 10M+ records
// prevents race condition under concurrent state updates
```
```dart
// toast must fire after frame renders, hence post-frame callback
```
**Comment when**: complex algorithms, non-obvious decisions, edge cases, workarounds
**Skip comments for**: loops, assignments, obvious conditionals, getters/setters

### Error Handling
- Handle at the appropriate level, never silently swallow exceptions
- Descriptive messages with context, not just the exception type

---

## Android / Kotlin + Jetpack Compose

### Stack
- **Language**: Kotlin (no Java)
- **UI**: Jetpack Compose + Material3 (no XML layouts)
- **Architecture**: MVVM — ViewModel holds all state and logic
- **DI**: Hilt where scope justifies it
- **Async**: Coroutines + StateFlow

### Rules
- ViewModel has **zero Android framework imports** — pure Kotlin logic only
- No `eval()`, no scripting engines, no third-party math libs
- No system keyboard for input-heavy UIs — explicit on-screen buttons
- No light theme, no dynamic color unless specified
- Orientation changes handled via `LocalConfiguration` — do not recreate activity state

### Project Layout
```
app/src/main/java/com/example/<project>/
├── MainActivity.kt
├── ui/
│   └── *.kt          # composables, one per screen or logical component
├── viewmodel/
│   └── *ViewModel.kt
└── model/
    └── *.kt          # data classes, sealed classes
```

---

## Flutter / Dart

### Stack
- **Language**: Dart
- **UI**: Flutter widgets + Material3
- **IDE**: Android Studio with Flutter plugin
- **State**: `setState` for simple local state; Provider or Riverpod for shared state
- **Navigation**: `PageView` for swipe-based, `Navigator` for push-based

### Rules
- Prefer `StatelessWidget` — only use `StatefulWidget` when local mutable state is needed
- One screen per file; keep `main.dart` as entry point only
- Use `pubspec.yaml` for all dependencies — no manual jar/aar imports
- Run `flutter pub get` after any `pubspec.yaml` change
- Toast notifications via `fluttertoast` package or `SnackBar` — specify which in task notes

### Project Layout
```
lib/
├── main.dart           # MaterialApp entry point only
├── screens/
│   ├── screen_one.dart
│   └── screen_two.dart
└── widgets/            # shared reusable widgets (if any)
```

---

## Git

- Commit messages: brief, what changed + why
- Never push unless explicitly requested
- Screenshots go on `main` in `screenshots/<cvN>/`
- Each branch is standalone — no cross-branch dependencies

---

## What NOT to Do (Either Platform)

- No copy-paste from Stack Overflow without understanding
- No unused imports or dead code left in
- No hardcoded strings that belong in constants or resources
- No `print()` / `Log.d()` left in production paths