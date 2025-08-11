package org.rinconadalabs.zumatico

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.plus
import org.jetbrains.compose.resources.painterResource
import zumatico.composeapp.generated.resources.Res
import zumatico.composeapp.generated.resources.basket

class Basket(val fruitDragged: (Fruit, Rect) -> Unit) {
    private val scale = 0.25f
    private val position = IntOffset(-50,30)
    private val rotation = 35f
    private val fruitOriginPosition = Offset(0f, -90f)

    private val fruits = mutableStateListOf<Fruit>().apply { addAll(initialFruits()) }

    fun get(fruit: Fruit) {
        //fruits.remove(fruit)
        fruits.add(createFruit(fruits.size))
    }

    fun initialFruits(): List<Fruit> {
        val fruits = mutableStateListOf<Fruit>()
        for (i in 0..6) {
            fruits.add(createFruit(i))
        }
        return fruits
    }

    private fun createFruit(i: Int): Fruit {
        val higher = (5 * (i % 2))
        val lower = -(3 * (i % 3))
        val fruitPosition = fruitOriginPosition.plus(IntOffset(18 * i + higher - lower, 15 * i - higher - lower))
        return Fruit(fruitPosition) { fruit, bounds -> fruitDragged(fruit, bounds) }
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
                Image(painter = painterResource(Res.drawable.basket),
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight(fraction = scale)
                        //.offset((-50).dp, 30.dp)
                        .offset { position }
                        .rotate(rotation)
                )
        }
    }
}