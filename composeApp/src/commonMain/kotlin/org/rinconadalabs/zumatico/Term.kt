package org.rinconadalabs.zumatico

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.rinconadalabs.zumatico.Symbol.Symbols

abstract class Term {
    abstract val image: MutableState<DrawableResource>
    abstract val filter: MutableState<ColorFilter?>

    fun isOperation() : Boolean {
        return !isComparison()
    }

    fun isComparison() : Boolean {
        return image.value == Symbols.Equal.resource ||
                image.value == Symbols.Greater.resource ||
                image.value == Symbols.Lower.resource
    }

    val drawing: @Composable (modifier: Modifier, image: DrawableResource) -> Unit = { modifier, image ->
        val visible = remember {
            MutableTransitionState(false).apply {
                targetState = true
            }
        }

        AnimatedVisibility (visible,
            enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
            exit = scaleOut(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
            ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                colorFilter = filter.value,
                modifier = modifier.fillMaxHeight()
            )
        }
    }

    @Composable
    abstract fun Draw()
}