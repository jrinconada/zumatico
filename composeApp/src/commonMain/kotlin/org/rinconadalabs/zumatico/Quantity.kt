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
    val margin = 0.1f
    override val image = mutableStateOf(Res.drawable.unknown)
    private var fruits = mutableSetOf<Fruit>()

    val count get() = fruits.size
    fun add(fruit: Fruit) {
        fruits.add(fruit)
        updateFruits()
    }
    fun remove(fruit: Fruit) {
        fruits.remove(fruit)
        updateFruits()
    }

    private fun updateFruits() {
        fruits.forEachIndexed { i, fruit -> move(i, fruit) }
    }

    private fun move(i: Int, fruit: Fruit) {
        val fruitScale = getFruitScale()
        bounds?.let {
            val snapX = it.center.x - centerCorrectionOffset() + getXOffset(i)
            val snapY = it.center.y - centerCorrectionOffset() + getYOffset(i)
            fruit.goTo(Offset(snapX, snapY), fruitScale)
        }
    }

    private fun getFruitScale() : Float {
        return when(fruits.size) {
            in 2..4 -> 0.1f
            5 -> 0.08f
            in 6..9 -> 0.07f
            in 10..16 -> 0.05f
            else -> 0.2f // 1
        }
    }

    private fun centerCorrectionOffset() : Float {
        val fruitScale = getFruitScale()
        val ratio = fruitScale / termScale
        bounds?.let { return it.width * ratio / 2f }
        return 0f
    }

    private fun getSize() : Float {
        val ratio = getFruitScale() / termScale
        val containerSize = bounds?.width ?: return 0f
        println("containerSize: $containerSize size ratio: ${containerSize * ratio + (containerSize * ratio) * margin}")
        return containerSize * ratio + (containerSize * ratio) * margin
    }

    private fun horizontalAlignment(i: Int, size: Float) : Float {
        return if (i % 2 == 0) -size / 2f else size / 2f
    }
    private fun getXOffset(i: Int) : Float {
        val size = getSize()
        return when(fruits.size) {
            1,2 -> 0f
            3 -> if (i == 0) 0f else horizontalAlignment(i, size)
            5 -> if (i == fruits.size - 1) 0f else horizontalAlignment(i, size)
            4 -> horizontalAlignment(i, size)
            else -> horizontalAlignment(i, size)
        }
    }
    private fun verticalAlignment(i: Int, size: Float) : Float {
        return if (i < fruits.size / 2) -size / 2f else size / 2f
    }
    private fun getYOffset(i: Int) : Float {
        val size = getSize()
        return when(fruits.size) {
            1 -> 0f
            3 -> if (i == 0) -size / 2f else size / 2f
            5 -> if (i == fruits.size - 1) 0f else verticalAlignment(i, size)
            6 -> if (i == 2 || i == 3) 0f else verticalAlignment(i, size)
            else -> verticalAlignment(i, size)
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