package com.martinszuc.fekt_mma_calculator.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.martinszuc.fekt_mma_calculator.model.CalculatorButton

object ButtonGridConstants {
    val ScientificRowHeight = 48.dp
    val ScientificButtonWidth = 56.dp
    val GridPadding = 12.dp
    val ButtonSpacing = 8.dp
}

@Composable
fun CalculatorButtonGrid(
    onButtonPressed: (CalculatorButton) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(ButtonGridConstants.GridPadding),
        verticalArrangement = Arrangement.spacedBy(ButtonGridConstants.ButtonSpacing)
    ) {
        // Scientific functions — scrollable row with fixed-height buttons
        Row(
            modifier = Modifier
                .height(ButtonGridConstants.ScientificRowHeight)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(ButtonGridConstants.ButtonSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(
                CalculatorButton.ScientificFn("sin"),
                CalculatorButton.ScientificFn("cos"),
                CalculatorButton.ScientificFn("tan"),
                CalculatorButton.ScientificFn("ln"),
                CalculatorButton.ScientificFn("log"),
                CalculatorButton.ScientificFn("sqrt"),
                CalculatorButton.ScientificFn("²"),
                CalculatorButton.Operator("^"),
                CalculatorButton.ToggleParen,
                CalculatorButton.ToggleSign,
                CalculatorButton.Constant("π", Math.PI),
                CalculatorButton.Constant("e", Math.E)
            ).forEach { button ->
                CalcButton(
                    label = buttonToLabel(button),
                    onClick = { onButtonPressed(button) },
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .width(ButtonGridConstants.ScientificButtonWidth)
                        .height(ButtonGridConstants.ScientificRowHeight)
                )
            }
        }

        // Main grid rows — share remaining space equally
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(ButtonGridConstants.ButtonSpacing)
        ) {
        // Row: C ⌫ ( ) ÷
        CalcButtonRow(
            listOf(
                CalculatorButton.Clear to "C",
                CalculatorButton.Backspace to "⌫",
                CalculatorButton.OpenParen to "(",
                CalculatorButton.CloseParen to ")",
                CalculatorButton.Operator("÷") to "÷"
            ),
            onButtonPressed,
            primaryColor = MaterialTheme.colorScheme.secondaryContainer
        )

        // Row: 7 8 9 ×
        CalcButtonRow(
            listOf(
                CalculatorButton.Digit(7) to "7",
                CalculatorButton.Digit(8) to "8",
                CalculatorButton.Digit(9) to "9",
                CalculatorButton.Operator("×") to "×"
            ),
            onButtonPressed
        )

        // Row: 4 5 6 -
        CalcButtonRow(
            listOf(
                CalculatorButton.Digit(4) to "4",
                CalculatorButton.Digit(5) to "5",
                CalculatorButton.Digit(6) to "6",
                CalculatorButton.Operator("-") to "-"
            ),
            onButtonPressed
        )

        // Row: 1 2 3 +
        CalcButtonRow(
            listOf(
                CalculatorButton.Digit(1) to "1",
                CalculatorButton.Digit(2) to "2",
                CalculatorButton.Digit(3) to "3",
                CalculatorButton.Operator("+") to "+"
            ),
            onButtonPressed
        )

        // Row: 0 . =
        CalcButtonRow(
            listOf(
                CalculatorButton.Digit(0) to "0",
                CalculatorButton.Decimal to ".",
                CalculatorButton.Equals to "="
            ),
            onButtonPressed,
            equalsColor = MaterialTheme.colorScheme.primary
        )
        }
    }
}

@Composable
private fun ColumnScope.CalcButtonRow(
    buttons: List<Pair<CalculatorButton, String>>,
    onButtonPressed: (CalculatorButton) -> Unit,
    modifier: Modifier = Modifier,
    primaryColor: androidx.compose.ui.graphics.Color? = null,
    equalsColor: androidx.compose.ui.graphics.Color? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .weight(1f),
        horizontalArrangement = Arrangement.spacedBy(ButtonGridConstants.ButtonSpacing)
    ) {
        buttons.forEach { (button, label) ->
            val color = when {
                equalsColor != null && button is CalculatorButton.Equals -> equalsColor
                primaryColor != null && (button is CalculatorButton.Clear || button is CalculatorButton.Backspace) -> primaryColor
                button is CalculatorButton.Digit || button is CalculatorButton.Decimal -> MaterialTheme.colorScheme.surfaceVariant
                else -> MaterialTheme.colorScheme.surface
            }
            CalcButton(
                label = label,
                onClick = { onButtonPressed(button) },
                color = color,
                modifier = Modifier.weight(1f).aspectRatio(1.8f)
            )
        }
    }
}

@Composable
private fun CalcButton(
    label: String,
    onClick: () -> Unit,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = color
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

private fun buttonToLabel(button: CalculatorButton): String = when (button) {
    is CalculatorButton.Digit -> button.value.toString()
    is CalculatorButton.Operator -> button.symbol
    CalculatorButton.Equals -> "="
    CalculatorButton.Clear -> "C"
    CalculatorButton.Backspace -> "⌫"
    CalculatorButton.Decimal -> "."
    CalculatorButton.ToggleSign -> "±"
    is CalculatorButton.ScientificFn -> button.name
    is CalculatorButton.Constant -> button.name
    CalculatorButton.OpenParen -> "("
    CalculatorButton.CloseParen -> ")"
    CalculatorButton.ToggleParen -> "( )"
}
