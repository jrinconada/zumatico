package org.rinconadalabs.zumatico

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color

class Formula () {

    fun fruitDragged(fruit: Fruit, bounds: Rect) {
        terms.forEachIndexed { index, term ->
            if (term is Quantity) {
                term.bounds?.let { targetBounds ->
                    if (bounds.overlaps(targetBounds)) {
                        onAdded(fruit, term)
                        return
                    }
                }
            }
        }
        onRemoved(fruit)
        fruit.backToBasket()
    }

    fun onAdded(fruit: Fruit, to: Quantity) {
        to.add(fruit)
        fruits.add(Fruit { fruit, bounds -> fruitDragged(fruit, bounds) })
        if (terms.size == 1) addSum()
    }

    fun addSum() {
        terms.add(Symbol(Symbol.Plus, onSwipe = { symbolChange() }))
        terms.add(Quantity())
        terms.add(Symbol(Symbol.Equal, onSwipe = { symbolChange() }))
        terms.add(Quantity())
    }

    fun symbolChange() {
        addEqual()
        removeConsecutiveEmptyEqual() // First call prevents double zeroes (0 = 0)
        removeConsecutiveEmptyEqual() // Second call prevents triple zeroes (0 = 0 = 0)
    }

    fun addEqual() {
        if (terms[terms.size - 2].image.value != Symbol.Equal) {
            terms.add(Symbol(Symbol.Equal, onSwipe = { symbolChange() }))
            terms.add(Quantity())
        }
    }

    fun removeConsecutiveEmptyEqual() {
        var toRemove = -1
        for (i in 0..terms.size - 5 step 2) {
            if (terms[i+1].image.value != Symbol.Equal
                || terms[i+3].image.value != Symbol.Equal) continue
            if ((terms[i] as Quantity).count == 0) {
                toRemove = i
            }
            if ((terms[i+2] as Quantity).count == 0) {
                toRemove = i + 2
            }
            if ((terms[i+4] as Quantity).count == 0) {
                toRemove = i + 4
            }
            if (toRemove == -1) continue
            else {
                terms.removeAt(toRemove)
                if (toRemove == i) {
                    terms.removeAt(toRemove)
                } else {
                    terms.removeAt(toRemove - 1)
                }
                return
            }
        }
    }

    fun onRemoved(fruit: Fruit) {
        terms.forEach { quantity -> if (quantity is Quantity) quantity.remove(fruit) }
        if (terms.filterIsInstance<Quantity>().all { it.isEmpty() }) {
            terms.clear()
            terms.add(Quantity())
        } else {
            removeConsecutiveEmptyEqual()
        }
    }

    var fruits = mutableStateSetOf(Fruit { fruit, bounds -> fruitDragged(fruit, bounds) })
    val terms = mutableStateListOf<Term>(Quantity())

    @Composable
    fun Draw() {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
        ) {
            Row(modifier = Modifier.align(Alignment.Center).fillMaxHeight(fraction = 0.3f)) {
                terms.forEach { term -> term.Draw() } }
            Box { fruits.forEach { fruit -> fruit.Draw() } }
        }
    }
}