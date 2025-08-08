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
            6 -> 0.07f
            in 7..8 -> 0.05f
            9 -> 0.06f
            in 10..16 -> 0.04f
            else -> 0.17f // 1
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
        return containerSize * ratio + (containerSize * ratio) * margin
    }

    private fun horizontalAlignmentOffset(i: Int, size: Float) : Float {
        return if (i % 2 == 0) -size / 2f else size / 2f
    }
    private fun getXOffset(i: Int) : Float {
        val size = getSize()
        return when(fruits.size) {
            1,2 -> 0f
            3,7 -> if (i == 0) 0f else horizontalAlignmentOffset(i, size)
            5,10 -> if ((i + 1) % 5 == 0) 0f else horizontalAlignmentOffset(i, size) * 1.6f
            9 -> (if (i % 3 == 0) -size  else if (i % 3 == 1) 0f else size) * 1.1f
            else -> horizontalAlignmentOffset(i, size) // 4,6,8
        }
    }
    private fun verticalAlignmentOffset(i: Int, size: Float, count: Int) : Float {
        return if (i < count / 2) -size / 2f else size / 2f
    }
    private fun getYOffset(i: Int, count: Int = fruits.size) : Float {
        val size = getSize()
        return when(count) {
            1 -> 0f
            3 -> if (i == 0) -size / 2f else size / 2f
            5 -> if (i == count - 1) 0f else verticalAlignmentOffset(i, size, count) * 1.6f
            6 -> if (i == 2 || i == 3) 0f else verticalAlignmentOffset(i, size, count) * 2.1f
            7 -> if (i < 3) getYOffset(i, 3) - size * 1.1f
            else getYOffset(i - 3, 4) + size * 1.1f
            8 -> if (i < 4) getYOffset(i, 4) - size * 1.1f
            else getYOffset(i - 4, 4) + size * 1.1f
            9 -> if (i in 3..5) 0f else verticalAlignmentOffset(i, size, count) * 2.2f
            10 -> if (i < 5) getYOffset(i, 5) - size * 1.6f
            else getYOffset(i - 5, 5) + size * 1.6f
            else -> verticalAlignmentOffset(i, size, count) // 2 and 4
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