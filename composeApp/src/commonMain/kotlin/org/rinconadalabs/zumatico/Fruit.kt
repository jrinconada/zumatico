package org.rinconadalabs.zumatico

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import org.jetbrains.compose.resources.painterResource
import zumatico.composeapp.generated.resources.Res
import zumatico.composeapp.generated.resources.apple
import kotlin.math.roundToInt
import kotlin.plus

class Fruit(val targets: MutableList<Rect?>, num: Int = 0) : DrawableDraggable {
    @Composable
    override fun draw() {
        var position by remember { mutableStateOf(Offset.Zero) }
        val dragAnimation by animateOffsetAsState(targetValue = position, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        var size by remember { mutableStateOf(IntSize.Zero) }
        var isDragging by remember { mutableStateOf(false) }

        fun snapToTarget() {
            isDragging = false
            var targetHit : Rect? = null
            targets.forEach { target ->
                target?.let { targetBounds ->
                    val draggableRect = Rect(
                        offset = position,
                        size = Size(
                            size.width.toFloat(),
                            size.height.toFloat()
                        )
                    )
                    println("fruit $draggableRect target $targetBounds")
                    if (draggableRect.overlaps(targetBounds)) {
                        targetHit = targetBounds
                    }
                }
            }
            targetHit?.let {
                val targetCenter = it.center
                val snapX = targetCenter.x - size.width / 2f
                val snapY = targetCenter.y - size.height / 2f
                position = Offset(snapX, snapY)
            } ?: run {
                position = Offset.Zero
            }
        }

        Image(
            painter = painterResource(Res.drawable.apple),
            contentDescription = "Apple",
            modifier = Modifier
                //.align(Alignment.BottomStart)
                .offset {
                    val offsetToUse = if (isDragging) position else dragAnimation
                    IntOffset(
                        offsetToUse.x.roundToInt(),
                        offsetToUse.y.roundToInt()
                    )
                }
                .fillMaxSize(fraction = 0.15f)
                .onGloballyPositioned { coordinates ->
                    size = coordinates.size
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            isDragging = true
                            position = dragAnimation
                        },
                        onDragEnd = {
                            snapToTarget()
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