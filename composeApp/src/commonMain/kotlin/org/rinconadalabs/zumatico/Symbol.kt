package org.rinconadalabs.zumatico

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import org.jetbrains.compose.resources.DrawableResource
import zumatico.composeapp.generated.resources.Res
import zumatico.composeapp.generated.resources.eq
import zumatico.composeapp.generated.resources.plus

class Symbol (which: DrawableResource, val onSwipe: () -> Unit) : Term() {
    companion object {
        val Equal = Res.drawable.eq
        val Plus = Res.drawable.plus
    }
    override var image = mutableStateOf(which)

    var swipeDirection = 0f

    @Composable
    override fun Draw() {
        AnimatedContent(
            targetState = image.value,
            transitionSpec = {
                if (swipeDirection < 0f) {
                    slideInVertically { height -> height } + scaleIn() togetherWith
                            slideOutVertically { height -> -height } + scaleOut()
                } else {
                    slideInVertically { height -> -height } + scaleIn() togetherWith
                            slideOutVertically { height -> height } + scaleOut()
                }.using(
                    SizeTransform(clip = false)
                )
            }
        ) { content ->
            drawing(Modifier.pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        if (swipeDirection < 0) {
                            image.value = Equal
                            onSwipe()
                        } else {
                            image.value = Plus
                            onSwipe()
                        }
                    },
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        swipeDirection = dragAmount
                    }
                )
            }, content)
        }

    }
}