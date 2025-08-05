package org.rinconadalabs.zumatico

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
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
        println("fruit added ${fruits.size}")
    }
    fun remove(fruit: Fruit) {
        fruits.remove(fruit)
        println("fruit remove ${fruits.size}")
    }

    private fun moveFruits() {
        fruits.forEach { fruit -> move(fruit) }
    }

    private fun move(fruit: Fruit) {
        bounds?.let {
            val snapX = it.center.x - it.width * (0.1f / 2f)
            val snapY = it.center.y - it.height * (0.1f / 2f)
            fruit.goTo(Offset(snapX, snapY), 0.1f)
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