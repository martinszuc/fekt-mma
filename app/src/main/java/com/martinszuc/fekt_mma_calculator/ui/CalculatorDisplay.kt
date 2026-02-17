package com.martinszuc.fekt_mma_calculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object DisplayConstants {
    val ExpressionTextSize = 18
    val ResultTextSize = 42
    val DisplayPadding = 24
    val DisplayPaddingBottom = 16
}

@Composable
fun CalculatorDisplay(
    expression: String,
    displayResult: String,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(DisplayConstants.DisplayPadding.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = expression.ifEmpty { " " },
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = DisplayConstants.ExpressionTextSize.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isError) "Error" else displayResult,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = DisplayConstants.ResultTextSize.sp
            ),
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End
        )
    }
}
