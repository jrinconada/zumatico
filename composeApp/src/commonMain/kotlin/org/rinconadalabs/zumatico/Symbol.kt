package org.rinconadalabs.zumatico

import org.jetbrains.compose.resources.DrawableResource
import zumatico.composeapp.generated.resources.Res
import zumatico.composeapp.generated.resources.eq
import zumatico.composeapp.generated.resources.plus
import zumatico.composeapp.generated.resources.unknown

class Symbol (which: DrawableResource = Plus) : Term() {
    companion object {
        val Equal = Res.drawable.eq
        val Plus = Res.drawable.plus
    }
    override val image = which
}