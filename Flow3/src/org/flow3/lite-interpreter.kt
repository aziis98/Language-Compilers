package org.flow3

import com.aziis98.corecompiler.DefinitionNotFoundException
import java.util.*

// Copyright 2016 Antonio De Lucreziis

interface Statement {
    fun execute(scope: Scope)
}

class StatementAssign(val varName: String, val value: Value) : Statement {
    override fun execute(scope: Scope) {
        scope.variables[varName] = value
    }
}

class StatementCall(val targetName: String, val funcName: String, val paramNames: List<String>) : Statement {
    override fun execute(scope: Scope) {

    }
}


data class FunctionDefinition(val name: String, val params: List<String>, val instructions: List<Statement>)

abstract class Value(val type: String)

class ValueObject(type: String, val properties: MutableMap<String, Value>) : Value(type)

abstract class ValuePrimitive<T>(type: String, val value: T) : Value(type)

class ValueBoolean(value: Boolean) : ValuePrimitive<Boolean>("Bool", value)

class ValueInt(value: Int) : ValuePrimitive<Int>("Integer", value)

class ValueDecimal(value: Double) : ValuePrimitive<Double>("Decimal", value)

class ValueString(value: String) : ValuePrimitive<String>("Text", value)

class Scope(val parent: Scope? = null) {

    val variables: HashMap<String, Value> = HashMap()
    val functions = HashMap<Pair<String, String>, FunctionDefinition>()

    fun getVariable(name: String): Value {
        return (variables[name]
            ?: parent?.getVariable(name))
            ?: throw DefinitionNotFoundException("The variable '$name' does not exists in scope ${this.toString()}!")
    }

    fun getFunction(type: String, name: String): FunctionDefinition {
        return (functions[Pair(type, name)]
            ?: parent?.getFunction(name))
            ?: throw DefinitionNotFoundException("The function '$name' does not exists in scope ${this.toString()}!")
    }

    override fun toString() = variables.map { "${it.key}: ${it.value}" }.joinToString("\n  ", "{\n  ", "\n}")

}
