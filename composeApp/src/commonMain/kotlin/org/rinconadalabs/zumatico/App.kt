package org.rinconadalabs.zumatico

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    val formula = Formula()

    formula.Draw()
}