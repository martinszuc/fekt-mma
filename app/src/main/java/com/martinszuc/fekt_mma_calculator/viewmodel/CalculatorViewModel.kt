package com.martinszuc.fekt_mma_calculator.viewmodel

import androidx.lifecycle.ViewModel
import com.martinszuc.fekt_mma_calculator.model.CalculatorButton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.*

class CalculatorViewModel : ViewModel() {

    private val _expression = MutableStateFlow("")
    val expression: StateFlow<String> = _expression.asStateFlow()

    private val _displayResult = MutableStateFlow("0")
    val displayResult: StateFlow<String> = _displayResult.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

    companion object {
        private const val PI_CONST = "π"
        private const val E_CONST = "e"
        private const val PI_VALUE = 3.141592653589793
        private const val E_VALUE = 2.718281828459045
    }

    fun onButtonPressed(button: CalculatorButton) {
        _isError.value = false
        val current = _expression.value

        when (button) {
            is CalculatorButton.Digit -> append(current, button.value.toString())
            is CalculatorButton.Operator -> append(current, " ${button.symbol} ")
            CalculatorButton.Equals -> evaluateAndReplace()
            CalculatorButton.Clear -> {
                _expression.value = ""
                _displayResult.value = "0"
            }
            CalculatorButton.Backspace -> {
                _expression.value = if (current.isNotEmpty()) current.dropLast(1).trimEnd() else ""
                updatePreview()
            }
            CalculatorButton.Decimal -> append(current, ".")
            CalculatorButton.ToggleSign -> toggleSign(current)
            is CalculatorButton.ScientificFn -> appendFunction(current, button.name)
            is CalculatorButton.Constant -> append(current, button.name)
            CalculatorButton.OpenParen -> append(current, "(")
            CalculatorButton.CloseParen -> {
                val closeCount = current.count { it == ')' }
                val openCount = current.count { it == '(' }
                if (closeCount < openCount) append(current, ")")
            }
            CalculatorButton.ToggleParen -> {
                val openCount = current.count { it == '(' }
                val closeCount = current.count { it == ')' }
                append(current, if (closeCount < openCount) ")" else "(")
            }
        }
    }

    private fun append(current: String, token: String) {
        _expression.value = current + token
        updatePreview()
    }

    private fun appendFunction(current: String, name: String) {
        _expression.value = when (name) {
            "²" -> "$current^2"
            else -> "$current$name("
        }
        updatePreview()
    }


    private fun toggleSign(current: String) {
        val trimmed = current.trimEnd()
        if (trimmed.isEmpty()) {
            append(current, "(-")
            return
        }
        val lastSpace = trimmed.lastIndexOf(' ')
        val lastToken = if (lastSpace >= 0) trimmed.substring(lastSpace + 1) else trimmed
        when {
            lastToken == "(-" -> {
                _expression.value = trimmed.dropLast(2)
            }
            lastToken.startsWith("-") && lastToken.length > 1 ->
                _expression.value = trimmed.dropLast(lastToken.length) + lastToken.drop(1)
            lastToken.firstOrNull()?.isDigit() == true || lastToken.contains(".") ->
                _expression.value = if (lastSpace >= 0) {
                    trimmed.substring(0, lastSpace + 1) + "(-" + lastToken
                } else "(-$lastToken"
            else -> append(current, "(-")
        }
        updatePreview()
    }

    private fun evaluateAndReplace() {
        val result = evaluate(_expression.value)
        if (_isError.value) return
        _expression.value = formatResult(result)
        _displayResult.value = formatResult(result)
    }

    private fun updatePreview() {
        val expr = _expression.value
        if (expr.isBlank()) {
            _displayResult.value = "0"
            return
        }
        val result = evaluate(expr)
        if (_isError.value) return
        _displayResult.value = formatResult(result)
    }

    fun formatResult(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "Error"
        return when {
            value == value.toLong().toDouble() -> value.toLong().toString()
            else -> value.toString().trimEnd('0').trimEnd('.')
        }
    }

    fun evaluate(expression: String): Double {
        _isError.value = false
        val normalized = expression
            .replace(PI_CONST, PI_VALUE.toString())
            .replace(E_CONST, E_VALUE.toString())
            .replace("×", "*")
            .replace("÷", "/")
            .replace("±", "-")
            .replace(Regex("\\s+"), " ")
            .trim()

        if (normalized.isBlank()) return 0.0

        return try {
            Parser(normalized).parse()
        } catch (e: Exception) {
            _isError.value = true
            Double.NaN
        }
    }

    private class Parser(private val input: String) {
        private var pos = 0

        private fun peek(): Char? = input.getOrNull(pos)
        private fun advance(): Char? = input.getOrNull(pos).also { pos++ }
        private fun skipSpaces() { while (peek()?.isWhitespace() == true) advance() }

        fun parse(): Double {
            skipSpaces()
            val result = parseTerm()
            skipSpaces()
            if (pos < input.length) throw IllegalArgumentException("Unexpected character")
            return result
        }

        private fun parseTerm(): Double {
            var result = parseFactor()
            while (true) {
                skipSpaces()
                when (peek()) {
                    '+' -> { advance(); result += parseFactor() }
                    '-' -> { advance(); result -= parseFactor() }
                    else -> return result
                }
            }
        }

        private fun parseFactor(): Double {
            var result = parsePower()
            while (true) {
                skipSpaces()
                when (peek()) {
                    '*' -> { advance(); result *= parsePower() }
                    '/' -> {
                        advance()
                        val right = parsePower()
                        if (right == 0.0) throw ArithmeticException("Division by zero")
                        result /= right
                    }
                    else -> return result
                }
            }
        }

        private fun parsePower(): Double {
            val base = parseUnary()
            skipSpaces()
            if (peek() == '^') {
                advance()
                val exp = parsePower()
                val result = when {
                    base < 0 && exp != exp.toLong().toDouble() -> Double.NaN
                    base == 0.0 && exp < 0 -> Double.NaN
                    else -> base.pow(exp)
                }
                if (result.isNaN()) throw ArithmeticException("Invalid power")
                return result
            }
            return base
        }

        private fun parseUnary(): Double {
            skipSpaces()
            when (peek()) {
                '-' -> { advance(); return -parseUnary() }
                '+' -> { advance(); return parseUnary() }
            }
            return parsePrimary()
        }

        private fun parsePrimary(): Double {
            skipSpaces()
            when (peek()) {
                '(' -> {
                    advance()
                    val result = parseTerm()
                    skipSpaces()
                    if (peek() != ')') throw IllegalArgumentException("Expected ')'")
                    advance()
                    return result
                }
            }

            val func = readIdentifier()
            if (func != null) {
                skipSpaces()
                if (peek() == '(') {
                    advance()
                    val arg = parseTerm()
                    skipSpaces()
                    if (peek() != ')') throw IllegalArgumentException("Expected ')'")
                    advance()
                    return evalFunction(func, arg)
                }
            }

            return parseNumber()
        }

        private fun readIdentifier(): String? {
            val start = pos
            while (peek()?.let { it.isLetter() || it == '_' } == true) advance()
            return if (pos > start) input.substring(start, pos) else null
        }

        private fun parseNumber(): Double {
            skipSpaces()
            val start = pos
            if (peek() == '-') advance()
            while (peek()?.isDigit() == true) advance()
            if (peek() == '.') {
                advance()
                while (peek()?.isDigit() == true) advance()
            }
            if (pos == start) throw IllegalArgumentException("Expected number")
            return input.substring(start, pos).toDouble()
        }

        private fun evalFunction(name: String, arg: Double): Double {
            return when (name.lowercase()) {
                "sin" -> sin(Math.toRadians(arg))
                "cos" -> cos(Math.toRadians(arg))
                "tan" -> {
                    val r = Math.toRadians(arg)
                    if (cos(r).absoluteValue < 1e-10) Double.NaN else tan(r)
                }
                "ln" -> if (arg <= 0) Double.NaN else ln(arg)
                "log" -> if (arg <= 0) Double.NaN else log10(arg)
                "sqrt" -> if (arg < 0) Double.NaN else sqrt(arg)
                else -> throw IllegalArgumentException("Unknown function: $name")
            }
        }
    }
}
