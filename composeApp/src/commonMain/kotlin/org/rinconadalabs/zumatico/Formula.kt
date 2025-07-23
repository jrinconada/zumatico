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
                if (term is Quantity && bounds.overlaps(targetBounds)) {
                    val snapX = term.bounds!!.center.x - bounds.size.width / 2f
                    val snapY = term.bounds!!.center.y - bounds.size.height / 2f
                    fruit.goTo(Offset(snapX, snapY), 0.1f)
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
        if (terms.size == 1) addSum()
    }

    fun addSum() {
        terms.add(Symbol())
        terms.add(Quantity())
        terms.add(Symbol(Symbol.Equal))
        terms.add(Quantity())
    }

    fun onRemoved(fruit: Fruit) {
        terms.forEach { quantity -> if (quantity is Quantity) quantity.remove(fruit) }
        if (terms.filterIsInstance<Quantity>().all { it.isEmpty() }) {
            terms.clear()
            terms.add(Quantity())
        }
    }

    var fruits = mutableStateSetOf(Fruit({ fruit, bounds -> fruitDragged(fruit, bounds) }))
    val terms = mutableStateListOf<Term>(Quantity())

    @Composable
    fun Draw() {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
        ) {
            Row(modifier = Modifier.align(Alignment.Center)) { terms.forEach { term -> term.Draw() } }
            Box { fruits.forEach { fruit -> fruit.Draw() } }
        }
    }
}