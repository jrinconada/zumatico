package org.rinconadalabs.zumatico

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import org.jetbrains.compose.resources.DrawableResource
import zumatico.composeapp.generated.resources.Res
import zumatico.composeapp.generated.resources.eq
import zumatico.composeapp.generated.resources.plus

class Symbol (which: DrawableResource = Plus) : Term() {
    companion object {
        val Equal = Res.drawable.eq
        val Plus = Res.drawable.plus
    }
    override var image = mutableStateOf(which)

    var swipeDirection = 0f

    @Composable
    override fun Draw() {
        drawing(Modifier.pointerInput(Unit) {
            detectVerticalDragGestures(
                onDragEnd = {
                    if (swipeDirection < 0) {
                        image.value = Equal
                    } else {
                        image.value = Plus
                    }
                },
                onVerticalDrag = { change, dragAmount ->
                    change.consume()
                    swipeDirection = dragAmount
                }
            )
        })
    }
}