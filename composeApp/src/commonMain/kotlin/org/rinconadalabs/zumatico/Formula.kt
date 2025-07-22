package org.rinconadalabs.zumatico

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect

class Formula () {

    fun onAdded(index: Int, fruit: Fruit) {
        terms[index].add(fruit)
    }

    fun onRemoved(index: Int, fruit: Fruit) {
        terms[index].remove(fruit)
    }

    var terms = mutableListOf(Quantity())
    val targets : MutableList<Rect?> get() {
        return terms.map { term -> term.target }.toMutableList()
    }

    @Composable
    fun Draw() {
        Row { terms.forEach { term -> term.draw() } }
    }
}