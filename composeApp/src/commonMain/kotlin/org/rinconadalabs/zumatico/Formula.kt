package org.rinconadalabs.zumatico

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color

class Formula () {

    fun fruitDragged(fruit: Fruit, bounds: Rect) {
        terms.forEachIndexed { index, term ->
            term.bounds?.let { targetBounds ->
                if (bounds.overlaps(targetBounds)) {
                    val snapX = term.bounds!!.center.x - bounds.size.width / 2f
                    val snapY = term.bounds!!.center.y - bounds.size.height / 2f
                    fruit.goTo(Offset(snapX, snapY))
                    onAdded(term, fruit)
                    return
                }
            }
        }
        onRemoved(fruit)
        fruit.backToBasket()
    }

    fun onAdded(quantity: Quantity, fruit: Fruit) {
        quantity.add(fruit)
        fruits.add(Fruit({ fruit, bounds -> fruitDragged(fruit, bounds) }))
    }

    fun onRemoved(fruit: Fruit) {
        terms.forEach { quantity -> quantity.remove(fruit) }
    }

    var fruits = mutableStateSetOf(Fruit({ fruit, bounds -> fruitDragged(fruit, bounds) }))
    val terms = mutableListOf(Quantity())

    @Composable
    fun Draw() {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
        ) {
            Row(modifier = Modifier.align(Alignment.Center)) { terms.forEach { term -> term.Draw() } }
            Row { fruits.forEach { fruit -> fruit.Draw() } }
        }
    }
}