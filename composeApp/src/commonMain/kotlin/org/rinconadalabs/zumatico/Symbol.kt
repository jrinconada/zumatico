package org.rinconadalabs.zumatico

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import zumatico.composeapp.generated.resources.Res
import zumatico.composeapp.generated.resources.div
import zumatico.composeapp.generated.resources.eq
import zumatico.composeapp.generated.resources.greater
import zumatico.composeapp.generated.resources.lower
import zumatico.composeapp.generated.resources.minus
import zumatico.composeapp.generated.resources.mult
import zumatico.composeapp.generated.resources.plus
import kotlin.math.abs

class Symbol (which: Symbols, val onSwipe: () -> Unit) : Term() {
    enum class Symbols (val resource: DrawableResource) {
        Lower(Res.drawable.lower),
        Equal(Res.drawable.eq),
        Greater(Res.drawable.greater),
        Mult(Res.drawable.mult),
        Plus(Res.drawable.plus),
        Minus(Res.drawable.minus),
        Div(Res.drawable.div)
    }
    override var image = mutableStateOf(which.resource)
    override var filter: MutableState<ColorFilter?> = mutableStateOf(null)
    private var swipeDirection = Offset.Zero
    private var currentSymbol = which.ordinal

    fun changeOperation() {
        if (currentSymbol in Symbols.Lower.ordinal..Symbols.Greater.ordinal) {
            currentSymbol = Symbols.Plus.ordinal
        } else if (swipeDirection.y < 0 && currentSymbol != Symbols.Div.ordinal) {
            currentSymbol++
        } else if (swipeDirection.y > 0 && currentSymbol != Symbols.Mult.ordinal){
            currentSymbol--
        } else return
        image.value = Symbols.entries[currentSymbol].resource
        onSwipe()
    }

    fun changeComparison() {
        if (currentSymbol in Symbols.Mult.ordinal..Symbols.Div.ordinal) {
            currentSymbol = Symbols.Equal.ordinal
        } else if (swipeDirection.x < 0 && currentSymbol != Symbols.Greater.ordinal) {
            currentSymbol++
        } else if (swipeDirection.x > 0 && currentSymbol != Symbols.Lower.ordinal){
            currentSymbol--
        } else return
        image.value = Symbols.entries[currentSymbol].resource
        onSwipe()
    }

    fun isVertical() : Boolean {
        return abs(swipeDirection.y) > abs(swipeDirection.x)
    }

    @Composable
    override fun Draw() {
        AnimatedContent(
            targetState = image.value,
            transitionSpec = {
                if (isVertical()) {
                    if (swipeDirection.y < 0f) {
                        slideInVertically { height -> height } + scaleIn() togetherWith
                                slideOutVertically { height -> -height } + scaleOut()
                    } else {
                        slideInVertically { height -> -height } + scaleIn() togetherWith
                                slideOutVertically { height -> height } + scaleOut()
                    }.using(
                        SizeTransform(clip = false)
                    )
                } else {
                    if (swipeDirection.x < 0f) {
                        slideInHorizontally { width -> width } + scaleIn() togetherWith
                                slideOutHorizontally { width -> -width } + scaleOut()
                    } else {
                        slideInHorizontally { width -> -width } + scaleIn() togetherWith
                                slideOutHorizontally { width -> width } + scaleOut()
                    }.using(
                        SizeTransform(clip = false)
                    )
                }
            }
        ) { content ->
            drawing(Modifier.pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (isVertical())
                            changeOperation()
                        else
                            changeComparison()
                    }, onDrag = { change, dragAmount ->
                        change.consume()
                        swipeDirection = dragAmount
                    }
                )
            }.padding(20.dp), content)
        }

    }
}