package org.rinconadalabs.zumatico

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import zumatico.composeapp.generated.resources.Res
import zumatico.composeapp.generated.resources.unknown

class Quantity : Term() {
    var bounds: Rect? = null
    override val image = mutableStateOf(Res.drawable.unknown)
    override var filter: MutableState<ColorFilter?> = mutableStateOf(null)
    private val activeColor = Color(0xCC777777)
    private val termScale = 0.3f
    private val margin = 0.1f
    private val max = 16
    private var fruits = mutableSetOf<Fruit>()
    fun isEmpty() = fruits.isEmpty()
    fun isFull() = fruits.size == max
    val count get() = fruits.size
    fun add(fruit: Fruit): Boolean {
        if (isFull()) return false
        val added = fruits.add(fruit)
        updateFruits()
        return added
    }
    fun remove(fruit: Fruit): Boolean {
        val removed = fruits.remove(fruit)
        updateFruits()
        return removed
    }
    private fun updateFruits() {
        if (isFull()) filter.value = ColorFilter.tint(Color.Transparent)
        else if (isEmpty()) filter.value = null
        else filter.value = ColorFilter.tint(activeColor)
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
            in 7..8, 11, 12, 16 -> 0.05f
            9 -> 0.06f
            in 10..15 -> 0.04f
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
    private fun getXOffset(i: Int, count: Int = fruits.size) : Float {
        val size = getSize()
        return when(count) {
            1,2 -> 0f
            3,7 -> if (i == 0) 0f else horizontalAlignmentOffset(i, size)
            5,10 -> if ((i + 1) % 5 == 0) 0f else horizontalAlignmentOffset(i, size) * 1.6f
            9 -> (if (i % 3 == 0) -size  else if (i % 3 == 1) 0f else size) * 1.1f
            11,12 -> if (i < count - 8) getXOffset(i, count - 8)
                else if (i < count - 4) getXOffset(i - (count - 8), 4) - size * 1.2f
                else getXOffset(i - 7, 4) + size * 1.2f
            13,14,15 -> if (i < count - 10) getXOffset(i, count - 10)
                else if (i < count - 5) getXOffset(i - (count - 10), 5) - size * 1.5f
                else getXOffset(i - (count - 5), 5) + size * 1.5f
            16 -> if (i in 0..3 || i in 8..11) getXOffset(i % 4, 4) - size * 1.1f
                else getXOffset(i % 4, 4) + size * 1.1f
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
            7,8,11,12 -> if (i < -(count % 2) + 4) getYOffset(i, -(count % 2) + 4) - size * 1.15f
                else getYOffset(i % 4, 4) + size * 1.15f
            9 -> if (i in 3..5) 0f else verticalAlignmentOffset(i, size, count) * 2.2f
            10 -> if (i < 5) getYOffset(i, 5) - size * 1.6f
                else getYOffset(i - 5, 5) + size * 1.6f
            13,14,15 -> if (i < count - 10) getYOffset(i, count - 10) - size * 1.5f
                else getYOffset((i - (count - 10)) % 5, 5) + size * 1.3f
            16 -> if (i < 8) getYOffset(i % 4, 4) - size * 1.1f
                else getYOffset(i % 4, 4) + size * 1.1f
            else -> verticalAlignmentOffset(i, size, count) // 2 and 4
        }
    }
    @Composable
    override fun Draw() {
        drawing(Modifier.onGloballyPositioned { coordinates ->
            bounds = coordinates.boundsInRoot()
            updateFruits()
        }, image.value)
    }
}