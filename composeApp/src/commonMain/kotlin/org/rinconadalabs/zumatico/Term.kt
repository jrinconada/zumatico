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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import zumatico.composeapp.generated.resources.Res
import zumatico.composeapp.generated.resources.unknown

abstract class Term {
    var bounds: Rect? = null
    abstract val image: DrawableResource

    @Composable
    fun Draw() {
        val visible = remember {
            MutableTransitionState(false).apply {
                targetState = true
            }
        }

        AnimatedVisibility (visible,
            enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
            exit = scaleOut(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
            modifier = Modifier
                .fillMaxSize(fraction = 0.3f)
                .onGloballyPositioned { coordinates ->
                    bounds = coordinates.boundsInRoot()
                }
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null
            )
        }
    }
}