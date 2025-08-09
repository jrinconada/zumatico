package org.rinconadalabs.zumatico

import androidx.compose.runtime.snapshots.SnapshotStateList
import org.rinconadalabs.zumatico.Symbol.Symbols.Div
import org.rinconadalabs.zumatico.Symbol.Symbols.Equal
import org.rinconadalabs.zumatico.Symbol.Symbols.Greater
import org.rinconadalabs.zumatico.Symbol.Symbols.Lower
import org.rinconadalabs.zumatico.Symbol.Symbols.Minus
import org.rinconadalabs.zumatico.Symbol.Symbols.Mult
import org.rinconadalabs.zumatico.Symbol.Symbols.Plus

class Validator {
    companion object {
        fun isValid(terms: SnapshotStateList<Term>): Boolean {
            var a = -1
            var b: Int
            var valid = false
            for (i in terms.indices) {
                if (terms[i] is Symbol) continue // Symbol term
                if (a == -1) { // First term
                    a = (terms[i] as Quantity).count
                } else if (terms[i-1].isComparison()) { // Last term of comparison
                    b = (terms[i] as Quantity).count
                    valid = when (terms[i-1].image.value) {
                        Lower.resource -> a < b
                        Greater.resource -> a > b
                        Equal.resource -> a == b
                        else -> false
                    }
                    a = -1
                } else { // Second term of operation
                    b = (terms[i] as Quantity).count
                    when (terms[i-1].image.value) {
                        Div.resource -> a = a / b
                        Minus.resource -> a = a - b
                        Plus.resource -> a = a + b
                        Mult.resource -> a = a * b
                    }
                }
            }
            return valid
        }
    }
}