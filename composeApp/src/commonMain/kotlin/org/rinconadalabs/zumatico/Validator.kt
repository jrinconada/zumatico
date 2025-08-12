package org.rinconadalabs.zumatico

import androidx.compose.runtime.snapshots.SnapshotStateList
import org.rinconadalabs.zumatico.Symbol.Symbols.*

/**
 * Validate a formula with PEDMAS order of operations
 * - Parenthesis and Exponents are not supported
 * - First operate with Divisions and Multiplications with the same priority and left to right
 * - Then operate Addition and Substraction have with the same priority and left to right
 * - Finally evaluate the comparators
 */
class Validator {
    companion object {
        fun isValid(terms: SnapshotStateList<Term>): Boolean {
            if (terms.size < 3) return false
            var formula = toList(terms)
            formula = operate(formula, true)
            formula = operate(formula, false)
            return evaluate(formula)
        }

        fun toList(terms: SnapshotStateList<Term>): MutableList<Int> {
            val list = mutableListOf<Int>()
            terms.forEach { term ->
                if (term is Symbol) {
                    list.add(term.image.value.hashCode())
                } else {
                    list.add((term as Quantity).count)
                }
            }
            return list
        }

        fun replace(i: Int, formula: MutableList<Int>, result: Int) {
            formula.removeAt(i - 1) // Remove first term
            formula.removeAt(i - 1) // Remove operation
            formula[i - 1] = result // Replace second term with result
        }

        fun operate(formula: MutableList<Int>, divAndMult: Boolean): MutableList<Int> {
            for (i in formula.indices) {
                if (divAndMult) {
                    if (formula[i] == Mult.resource.hashCode()) {
                        replace(i, formula, formula[i - 1] * formula[i + 1])
                        return operate(formula, divAndMult)
                    } else if (formula[i] == Div.resource.hashCode()) {
                        replace(i, formula, if (formula[i + 1] == 0) Int.MAX_VALUE else formula[i - 1] / formula[i + 1])
                        return operate(formula, divAndMult)
                    }
                } else { // Addition & substraction
                    if (formula[i] == Plus.resource.hashCode()) {
                        replace(i, formula, formula[i - 1] + formula[i + 1])
                        return operate(formula, divAndMult)
                    } else if (formula[i] == Minus.resource.hashCode()) {
                        replace(i, formula, formula[i - 1] - formula[i + 1])
                        return operate(formula, divAndMult)
                    }
                }
            }
            return formula
        }

        fun evaluate(formula: MutableList<Int>): Boolean {
            for (i in formula.indices) {
                val valid = when (formula[i]) {
                    Lower.resource.hashCode() -> formula[i - 1] < formula[i + 1]
                    Equal.resource.hashCode() -> formula[i - 1] == formula[i + 1]
                    Greater.resource.hashCode() -> formula[i - 1] > formula[i + 1]
                    else -> true // Quantity
                }
                if (!valid) return false
            }
            return true
        }
    }
}