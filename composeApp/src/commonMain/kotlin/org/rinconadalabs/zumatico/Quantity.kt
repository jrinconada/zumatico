package org.rinconadalabs.zumatico

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import org.jetbrains.compose.resources.painterResource
import zumatico.composeapp.generated.resources.Res
import zumatico.composeapp.generated.resources.unknown

class Quantity : Term() {
    @Composable
    override fun draw(): Rect? {
        val visible = remember {
            MutableTransitionState(false).apply {
                targetState = true
            }
        }
        var target by remember { mutableStateOf<Rect?>(null) }

        AnimatedVisibility (visible,
            enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
            exit = scaleOut(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
            modifier = Modifier
                //.align(Alignment.Center)
                .fillMaxSize(fraction = 0.3f)
                .onGloballyPositioned { coordinates ->
                    target = coordinates.boundsInParent()
                }
        ) {
            Image(
                painter = painterResource(Res.drawable.unknown),
                contentDescription = "Unknown",
                colorFilter = ColorFilter.tint(Color.DarkGray)
            )
        }
        return target
    }
}