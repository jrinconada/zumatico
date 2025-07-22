package org.rinconadalabs.zumatico

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect

class Basket (val targets: MutableList<Rect?>, val onOutOfBasket: (Int, Fruit) -> Unit, val onBackToBasket: (Int, Fruit) -> Unit){

    fun goingOut(index: Int, fruit: Fruit) {
        fruits.remove(fruit)
        onOutOfBasket(index, fruit)
        fruits.add(Fruit(targets, { index, fruit -> goingOut(index, fruit) }, { index, fruit -> onBackToBasket(index, fruit) }))
    }

    val fruits = mutableListOf(
        Fruit(targets, { index, fruit -> goingOut(index, fruit) }, { index, fruit -> onBackToBasket(index, fruit) })
    )

    @Composable
    fun Draw() {
        fruits.forEach { fruit -> fruit.Draw() }
    }
}