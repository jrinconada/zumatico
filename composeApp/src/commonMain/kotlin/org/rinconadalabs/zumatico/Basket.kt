package org.rinconadalabs.zumatico

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import org.jetbrains.compose.resources.painterResource
import zumatico.composeapp.generated.resources.Res
import zumatico.composeapp.generated.resources.basket

class Basket(val fruitDragged: (Fruit, Rect) -> Unit) {
    private var fruits = mutableStateSetOf<Fruit>()

    init {
        fruits.add(Fruit { fruit, bounds -> fruitDragged(fruit, bounds) })
    }

    fun get() {
        fruits.add(Fruit { fruit, bounds -> fruitDragged(fruit, bounds) })
    }

    fun put(fruit: Fruit) {
        fruit.backToBasket()
    }

    @Composable
    fun Draw() {
        BoxWithConstraints(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier.fillMaxHeight()) {
                fruits.forEach { fruit -> fruit.Draw(maxHeight.value) }
                Image(painter = painterResource(Res.drawable.basket), contentDescription = null)
        }
    }
}