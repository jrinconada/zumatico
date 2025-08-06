package org.rinconadalabs.zumatico

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import zumatico.composeapp.generated.resources.Res
import zumatico.composeapp.generated.resources.unknown

class Quantity : Term() {
    var bounds: Rect? = null
    override val image = mutableStateOf(Res.drawable.unknown)
    private var fruits = mutableSetOf<Fruit>()

    val count get() = fruits.size
    fun add(fruit: Fruit) {
        move(fruit)
        fruits.add(fruit)
    }
    fun remove(fruit: Fruit) {
        fruits.remove(fruit)
    }

    private fun moveFruits() {
        fruits.forEach { fruit -> move(fruit) }
    }

    private fun move(fruit: Fruit) {
        val termScale = 0.3f
        val fruitScale = 0.2f
        val ratio = fruitScale / termScale
        bounds?.let {
            val snapX = it.center.x - it.width * ratio / 2f
            val snapY = it.center.y - it.height * ratio / 2f
            fruit.goTo(Offset(snapX, snapY), fruitScale)
        }
    }

    fun isEmpty() = fruits.isEmpty()

    @Composable
    override fun Draw() {
        drawing(Modifier.onGloballyPositioned { coordinates ->
            bounds = coordinates.boundsInRoot()
            moveFruits()
        }, image.value)
    }
}