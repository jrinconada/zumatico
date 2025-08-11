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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import org.jetbrains.compose.resources.painterResource
import zumatico.composeapp.generated.resources.Res
import zumatico.composeapp.generated.resources.apple
import kotlin.math.roundToInt

class Fruit(val dragStopped: (Fruit, Rect) -> Unit) {
    private val originalScale = 0.15f
    private val randomShake: Offset = randomPosition()
    private var relativePosition by mutableStateOf(Offset.Zero)
    private var scale by mutableStateOf(originalScale)
    private var origin: Offset by mutableStateOf(Offset.Zero)
    private var size: IntSize by mutableStateOf(IntSize.Zero)
    private var absolutePosition: Offset? = null
    private var inBasket = true

    fun backToBasket() {
        inBasket = true
        absolutePosition = origin
        scale = originalScale
        relativePosition = getRelativeOffset()
    }

    fun goTo(position: Offset, scale: Float) {
        inBasket = false
        this.scale = scale
        absolutePosition = position
        relativePosition = getRelativeOffset()
    }

    private fun randomPosition() : Offset {
        val range = 30
        val x = (-range..range).random()
        val y = (-range..range).random()
        return Offset(x.toFloat(), y.toFloat())
    }

    private fun getAbsolutePosition(): Offset {
        return relativePosition + origin - randomShake
    }

    private fun getRelativeOffset(): Offset {
        val shake = if (inBasket) randomShake else Offset.Zero
        return (absolutePosition?.let { it - origin } ?: Offset.Zero) + shake
    }

    @Composable
    fun Draw(height: Float) {
        val moveAnimation by animateOffsetAsState(targetValue = relativePosition, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        val scaleAnimation by animateFloatAsState(targetValue = scale, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        var isDragging by remember { mutableStateOf(false) }

        fun onDragEnd() {
            isDragging = false
            val bounds = Rect(
                offset = getAbsolutePosition(),
                size = Size(size.width.toFloat(), size.height.toFloat())
            )
            dragStopped(this, bounds)
        }

        Image(
            painter = painterResource(Res.drawable.apple),
            contentDescription = null,
            modifier = Modifier
                .offset {
                    val offsetToUse = if (isDragging) relativePosition else moveAnimation
                    println("offset: $offsetToUse Absolute position: ${getAbsolutePosition()}")
                    IntOffset(offsetToUse.x.roundToInt(), offsetToUse.y.roundToInt())
                }
                .fillMaxHeight(fraction = scaleAnimation)
                .onSizeChanged { newSize ->
                    size = newSize
                    origin = Offset(0f, height - size.height)
                    relativePosition = getRelativeOffset()
                    println("onSizeChanged: $newSize relativePosition: $relativePosition")
                }
                .onGloballyPositioned { coordinates ->
                    size = coordinates.size
                    origin = Offset(0f, height - size.height)
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            isDragging = true
                            relativePosition = moveAnimation
                        },
                        onDragEnd = {
                            onDragEnd()
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            relativePosition = relativePosition.plus(dragAmount)
                        }
                    )
                }
        )
    }
}