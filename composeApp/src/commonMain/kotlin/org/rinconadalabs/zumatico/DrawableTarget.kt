package org.rinconadalabs.zumatico

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect

interface DrawableTarget {
    @Composable
    fun draw() : Rect?
}