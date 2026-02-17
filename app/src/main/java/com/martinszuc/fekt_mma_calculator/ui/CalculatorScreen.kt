package com.martinszuc.fekt_mma_calculator.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.martinszuc.fekt_mma_calculator.viewmodel.CalculatorViewModel

object LayoutConstants {
    val DisplayHeightWeight = 0.3f
}

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val expression by viewModel.expression.collectAsState()
    val displayResult by viewModel.displayResult.collectAsState()
    val isError by viewModel.isError.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        CalculatorDisplay(
            expression = expression,
            displayResult = displayResult,
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .height((LayoutConstants.DisplayHeightWeight * LocalConfiguration.current.screenHeightDp).dp)
        )
        CalculatorButtonGrid(
            onButtonPressed = viewModel::onButtonPressed,
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        )
    }
}
