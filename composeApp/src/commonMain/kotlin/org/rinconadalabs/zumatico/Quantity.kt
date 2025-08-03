package org.rinconadalabs.zumatico

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import zumatico.composeapp.generated.resources.Res
import zumatico.composeapp.generated.resources.unknown

class Quantity : Term() {
    override val image = mutableStateOf(Res.drawable.unknown)
    private var fruits = mutableSetOf<Fruit>()
    fun add(fruit: Fruit) {
        fruits.add(fruit)
        println("fruit added ${fruits.size}")
    }
    fun remove(fruit: Fruit) {
        fruits.remove(fruit)
        println("fruit remove ${fruits.size}")
    }

    fun isEmpty() = fruits.isEmpty()

    @Composable
    override fun Draw() {
        drawing(Modifier)
    }
}