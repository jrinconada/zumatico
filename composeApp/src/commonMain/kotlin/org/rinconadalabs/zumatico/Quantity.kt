package org.rinconadalabs.zumatico

import zumatico.composeapp.generated.resources.Res
import zumatico.composeapp.generated.resources.unknown

class Quantity : Term() {
    override val image = Res.drawable.unknown
    private var fruits = mutableSetOf<Fruit>()
    fun add(fruit: Fruit) {
        fruits.add(fruit)
        println("fruit added ${fruits.size}")
    }
    fun remove(fruit: Fruit) {
        fruits.remove(fruit)
        println("fruit remove ${fruits.size}")
    }

    fun isEmpty() = fruits.isEmpty()
}