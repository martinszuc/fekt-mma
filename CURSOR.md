# XPC-MMA — Scientific Calculator App (Android)

## Context
University course task: build a calculator Android app with explicit on-screen buttons (no system keyboard).
Full Kotlin + Jetpack Compose, MVVM, minimal dark UI.

---

## Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM — `CalculatorViewModel` holds all state and logic
- **Min SDK**: 26
- **UI toolkit**: Material3 (`androidx.compose.material3`)
- **No other external libraries needed**

---

## Project Structure

```
app/src/main/java/com/example/calculator/
├── MainActivity.kt
├── ui/
│   ├── CalculatorScreen.kt       # root composable, assembles display + grid
│   ├── CalculatorDisplay.kt      # expression + result text area
│   └── CalculatorButtonGrid.kt  # all button rows
├── viewmodel/
│   └── CalculatorViewModel.kt   # all calculator state and logic
└── model/
    └── CalculatorButton.kt      # sealed class/enum for button types
```

---

## Features

### Basic (required for grading)
- Digits 0–9, each as an explicit Button
- Operators: `+` `-` `×` `÷`
- Equals `=`, clear `C`, backspace `⌫`
- Decimal point `.`

### Scientific (extend above)
- `sin` `cos` `tan` — in degrees
- `ln` `log`
- `√` (square root)
- `x²` (square)
- `xʸ` (power)
- `( )` parentheses — single button that toggles open/close
- `±` sign toggle
- `π` `e` constants

---

## UI Design

**Material3 dark theme** — force dark, no dynamic color (consistent look across devices).

Use Material3 `darkColorScheme` with a custom palette:
```
Background:       #121212   → mapped to background/surface
Button (digit):   #1E1E1E   → surfaceVariant
Button (op):      #2A2A2A   → surface
Button (action):  #333333   → secondaryContainer  (C, backspace)
Button (equals):  #BB86FC   → primary accent
Text:             #FFFFFF   → onBackground
Secondary text:   #9E9E9E   → onSurfaceVariant  (expression preview)
```

Wrap app in a custom `MaterialTheme` with the above `darkColorScheme`. No `dynamicDarkColorScheme`.

Layout:
- Full screen, no top bar
- Display area at top (~30% height): expression in small text above, current result large below
- Button grid fills remaining space
- Buttons equal-width in rows, slightly rounded corners, no borders
- Scientific functions in a scrollable row or compact top section above main grid

---

## ViewModel Responsibilities

```kotlin
// state to expose to UI
val expression: StateFlow<String>   // e.g. "sin(45) + 3 × "
val displayResult: StateFlow<String> // live preview result or final answer
val isError: StateFlow<Boolean>

// inputs
fun onButtonPressed(button: CalculatorButton)

// internal
private fun evaluate(expression: String): Double  // use expression parser
private fun formatResult(value: Double): String   // trim trailing .0
```

**Expression evaluation**: implement a simple recursive descent parser or use `Double` stack-based eval for `+ - × ÷ ^ ()`. Do NOT use `javax.script` (not available on Android) and do NOT import any eval library — implement it.

---

## CalculatorButton Model

```kotlin
sealed class CalculatorButton {
    data class Digit(val value: Int) : CalculatorButton()
    data class Operator(val symbol: String) : CalculatorButton()
    object Equals : CalculatorButton()
    object Clear : CalculatorButton()
    object Backspace : CalculatorButton()
    object Decimal : CalculatorButton()
    object ToggleSign : CalculatorButton()
    data class ScientificFn(val name: String) : CalculatorButton()  // "sin", "cos" etc.
    data class Constant(val name: String, val value: Double) : CalculatorButton()
    object OpenParen : CalculatorButton()
    object CloseParen : CalculatorButton()
}
```

---

## Coding Rules
- Production-quality, clean Kotlin idioms
- No magic numbers — named constants for all colors and sizes
- Comments only where logic is non-obvious (parser, edge cases)
- Each composable does one thing
- ViewModel has zero Android imports — pure Kotlin logic only
- Handle edge cases: division by zero, invalid expression, `sqrt` of negative, `log` of non-positive

---

## What NOT to do
- No XML layouts
- No Java
- No `eval()` or scripting engine
- No third-party math libraries
- No system keyboard input
- No light theme, no dynamic color (`dynamicDarkColorScheme`)