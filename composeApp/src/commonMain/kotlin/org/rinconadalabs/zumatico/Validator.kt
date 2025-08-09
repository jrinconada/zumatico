package org.rinconadalabs.zumatico

import androidx.compose.runtime.snapshots.SnapshotStateList

class Validator {
    companion object {
        fun isValid(terms: SnapshotStateList<Term>): Boolean {
            return terms.size > 1
        }
    }
}