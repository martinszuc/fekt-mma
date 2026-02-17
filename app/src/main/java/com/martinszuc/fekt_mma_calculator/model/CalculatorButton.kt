package com.martinszuc.fekt_mma_calculator.model

sealed class CalculatorButton {
    data class Digit(val value: Int) : CalculatorButton()
    data class Operator(val symbol: String) : CalculatorButton()
    object Equals : CalculatorButton()
    object Clear : CalculatorButton()
    object Backspace : CalculatorButton()
    object Decimal : CalculatorButton()
    object ToggleSign : CalculatorButton()
    data class ScientificFn(val name: String) : CalculatorButton()
    data class Constant(val name: String, val value: Double) : CalculatorButton()
    object OpenParen : CalculatorButton()
    object CloseParen : CalculatorButton()
    object ToggleParen : CalculatorButton()  // single button that toggles ( or )
}
