package org.rinconadalabs.zumatico

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import org.rinconadalabs.zumatico.Symbol.Symbols.*

class Formula () {
    private val normalColor = Color(0xFF606060)
    private val validColor = Color(0xFF8bb592)
    val terms = mutableStateListOf<Term>(Quantity())
    val valid = mutableStateOf(false)
    val basket = Basket(fruitDragged = { fruit, bounds -> fruitDragged(fruit, bounds) })

    fun fruitDragged(fruit: Fruit, bounds: Rect) {
        terms.forEachIndexed { index, term ->
            if (term is Quantity) {
                term.bounds?.let { targetBounds ->
                    if (bounds.overlaps(targetBounds) && !term.isFull()) {
                        onAdded(fruit, term)
                        return
                    }
                }
            }
        }
        onRemoved(fruit)
    }

    fun onAdded(fruit: Fruit, to: Quantity) {
        to.add(fruit)
        basket.get(fruit)
        if (terms.size == 1) addSum()
        valid.value = Validator.isValid(terms)
    }

    fun addSum() {
        terms.add(Symbol(Plus, onSwipe = { symbolChange() }))
        terms.add(Quantity())
        terms.add(Symbol(Equal, onSwipe = { symbolChange() }))
        terms.add(Quantity())
    }

    fun symbolChange() {
        addEqual()
        removeConsecutiveEmptyEqual() // First call prevents double zeroes (0 = 0)
        removeConsecutiveEmptyEqual() // Second call prevents triple zeroes (0 = 0 = 0)
        valid.value = Validator.isValid(terms)
    }

    fun addEqual() {
        if (terms[terms.size - 2].isOperation()) {
            terms.add(Symbol(Equal, onSwipe = { symbolChange() }))
            terms.add(Quantity())
        }
    }

    fun removeConsecutiveEmptyEqual() {
        var toRemove = -1
        for (i in 0..terms.size - 5 step 2) {
            if (terms[i+1].isOperation()
                || terms[i+3].isOperation()) continue
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

    fun remove(fruit: Fruit) {
        var removed = false
        for (i in 0..terms.size - 1) {
            if (terms[i] is Quantity) {
                removed = (terms[i] as Quantity).remove(fruit)
                if (removed) {
                    basket.putBack(fruit)
                    break
                }
            }
        }
        println("Fruit removed $removed")
        if (!removed) {
            basket.release(fruit)
        }
    }

    fun updateFormulaAfterFruitRemoved() {
        if (terms.filterIsInstance<Quantity>().all { it.isEmpty() }) {
            terms.clear()
            terms.add(Quantity())

        } else {
            removeConsecutiveEmptyEqual()
        }
        valid.value = Validator.isValid(terms)
    }
    fun onRemoved(fruit: Fruit) {
        remove(fruit)
        updateFormulaAfterFruitRemoved()
    }
    @Composable
    fun Draw() {
        val backgroundColor: Color by animateColorAsState(
            if (valid.value) validColor else normalColor)

        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Row(modifier = Modifier.align(Alignment.Center).fillMaxHeight(fraction = 0.3f)) {
                terms.forEach { term -> term.Draw() } }
            basket.Draw()
        }
    }
}