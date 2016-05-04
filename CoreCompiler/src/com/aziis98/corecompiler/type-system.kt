package com.aziis98.corecompiler

import java.util.*

// Copyright 2016 Antonio De Lucreziis

class Scope() {
    val variables: HashMap<String, Variable> = HashMap()

    fun addVariable(variable: Variable) {
        variables.put(variable.name, variable)
    }

    fun getVariable(name: String): Variable {
        return variables[name] ?: throw VariableNotFoundException("The variable \"$name\" does not exists!")
    }
}

class Variable(val name: String, val type: VariableType, var value: VaraibleValue)

class VariableType(val name: String)

class VaraibleValue()

class VariableNotFoundException(message: String = "") : RuntimeException(message)