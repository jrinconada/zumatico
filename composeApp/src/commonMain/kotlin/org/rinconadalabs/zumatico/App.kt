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
    var formula = mutableListOf(Quantity(), Quantity())
    var targets = mutableListOf<Rect?>()
    var fruits = mutableListOf(Fruit(targets), Fruit(targets))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Row { formula.forEach { term -> targets.add(term.draw()) } }
        fruits.forEach { fruit -> fruit.draw() }
    }


}