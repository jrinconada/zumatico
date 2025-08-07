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
    val termScale = 0.3f
    override val image = mutableStateOf(Res.drawable.unknown)
    private var fruits = mutableSetOf<Fruit>()

    val count get() = fruits.size
    fun add(fruit: Fruit) {
        fruits.add(fruit)
        updateFruits()
    }
    fun remove(fruit: Fruit) {
        fruits.remove(fruit)
    }

    private fun updateFruits() {
        fruits.forEachIndexed { i, fruit -> move(i, fruit) }
    }

    private fun move(i: Int, fruit: Fruit) {
        val fruitScale = getFruitScale()
        bounds?.let {
            val snapX = it.center.x + it.width * getXOffset(i)
            val snapY = it.center.y + it.height * getYOffset(i)
            fruit.goTo(Offset(snapX, snapY), fruitScale)
        }
    }

    private fun getFruitScale() : Float {
        return when(fruits.size) {
            in 2..4 -> 0.11f
            in 5..9 -> 0.1f
            in 10..16 -> 0.05f
            else -> 0.2f // 1
        }
    }

    private fun getXOffset(i: Int) : Float {
        val fruitScale = getFruitScale()
        val ratio = fruitScale / termScale
        return when(fruits.size) {
            1,2 -> -ratio / 2f
            3,7 -> if (i == 0) -ratio / 2f else if (i == 1) -ratio else ratio / 2f
            in 4..9 -> 0f
            else -> -ratio / 2f
        }
    }

    private fun getYOffset(i: Int) : Float {
        val fruitScale = getFruitScale()
        val ratio = fruitScale / termScale
        return when(fruits.size) {
            1 -> -ratio / 2f
            in 2..4 -> if (i == 0) -ratio else 0f
            else -> -ratio / 2f
        }
    }

    fun isEmpty() = fruits.isEmpty()

    @Composable
    override fun Draw() {
        drawing(Modifier.onGloballyPositioned { coordinates ->
            bounds = coordinates.boundsInRoot()
            updateFruits()
        }, image.value)
    }
}