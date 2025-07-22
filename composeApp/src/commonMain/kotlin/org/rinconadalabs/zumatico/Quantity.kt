package org.rinconadalabs.zumatico

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    private var fruits = mutableSetOf<Fruit>()

    fun add(fruit: Fruit) {
        fruits.add(fruit)
        println("fruit added ${fruits.size}")
    }

    fun remove(fruit: Fruit) {
        fruits.remove(fruit)
        println("fruit remove ${fruits.size}")
    }

    var target: Rect? = null

    @Composable
    override fun draw(): Rect? {
        val visible = remember {
            MutableTransitionState(false).apply {
                targetState = true
            }
        }

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
            Box {
                Image(
                    painter = painterResource(Res.drawable.unknown),
                    contentDescription = "Unknown",
                    colorFilter = ColorFilter.tint(Color.DarkGray)
                )
                Row { fruits.forEach { fruit -> fruit.Draw() } }
            }

        }
        return target
    }
}