package org.rinconadalabs.zumatico

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    val formula = Formula()
    val basket = Basket(formula.targets, { index, fruit -> formula.onAdded(index, fruit) }, { index, fruit -> formula.onRemoved(index, fruit) })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        formula.Draw()
        basket.Draw()
    }


}