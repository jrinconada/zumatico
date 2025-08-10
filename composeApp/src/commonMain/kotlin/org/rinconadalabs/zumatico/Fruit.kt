package org.rinconadalabs.zumatico

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.plus
import org.jetbrains.compose.resources.painterResource
import zumatico.composeapp.generated.resources.Res
import zumatico.composeapp.generated.resources.apple
import kotlin.math.roundToInt

class Fruit(val dragStopped: (Fruit, Rect) -> Unit) {
    private val originalScale = 0.15f
    private val randomShake: Offset = randomPosition()
    private var position by mutableStateOf(randomShake)
    private var scale by mutableStateOf(originalScale)

    fun backToBasket() {
        position = randomShake
        scale = originalScale
    }

    fun goTo(position: Offset, scale: Float) {
        this.position = position
        this.scale = scale
    }

    private fun randomPosition() : Offset {
        val range = 30
        val x = (-range..range).random()
        val y = (-range..range).random()
        return Offset(x.toFloat(), y.toFloat())
    }

    @Composable
    fun Draw() {
        val moveAnimation by animateOffsetAsState(targetValue = position, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        val scaleAnimation by animateFloatAsState(targetValue = scale, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        var size by remember { mutableStateOf(IntSize.Zero) }
        var isDragging by remember { mutableStateOf(false) }

        fun onDragEnd() {
            isDragging = false
            val bounds = Rect(
                offset = position.minus(randomShake),
                size = Size(size.width.toFloat(), size.height.toFloat())
            )
            dragStopped(this, bounds)
        }

        Image(
            painter = painterResource(Res.drawable.apple),
            contentDescription = null,
            modifier = Modifier
                .offset {
                    val offsetToUse = if (isDragging) position else moveAnimation
                    IntOffset(offsetToUse.x.roundToInt(), offsetToUse.y.roundToInt())
                }
                .fillMaxHeight(fraction = scaleAnimation)
                .onGloballyPositioned { coordinates ->
                    size = coordinates.size
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            isDragging = true
                            position = moveAnimation
                        },
                        onDragEnd = {
                            onDragEnd()
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            position = position.plus(dragAmount)
                        }
                    )
                }
        )
    }
}