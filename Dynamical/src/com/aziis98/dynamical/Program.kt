package com.aziis98.dynamical

import com.aziis98.corecompiler.Scope
import java.util.*

// Copyright Antonio De Lucreziis 2016

class Program {
    val statements = LinkedList<Statement>()
    val globalScope = Scope()

    fun executeProgram() {
        var cScope = globalScope

        for (statement in statements) {
            statement.execute(this, cScope)
        }
    }

    override fun toString() = "Program( $globalScope )"
}