package com.aziis98.lang2

import java.util.*

// Copyright 2016 Antonio De Lucreziis

class Scope(val parent: Scope? = null) {

    val types = HashMap<String, Type>()

    private val definitions = HashMap<String, MutableList<FunctionDefinition>>()

    private val variables = HashMap<String, VariableDefinition>()

    fun getFunction(name: String, paramTypes: List<Type>): FunctionDefinition {
        return definitions[name]?.first { def ->
            def.params.forEachIndexed { i, functionParam ->
                if (functionParam.type != paramTypes[i]) {
                    return@first false
                }
            }
            return@first true
        } ?: throw NoSuchElementException("$name(${paramTypes.joinToString(",")})")
    }

    fun getVariable(name: String): VariableDefinition {
        return variables.getOrElse(name) { parent?.getVariable(name) ?: throw NoSuchElementException(name) }
    }

    fun addFunction(functionDef: FunctionDefinition) {
        definitions.putIfAbsent(functionDef.name, mutableListOf())
        definitions[functionDef.name]!!.add(functionDef)
    }

    fun addVariable(variableDef: VariableDefinition) {
        variables.put(variableDef.name, variableDef)
    }
}


interface Node {
    fun getType(scope: Scope) : Type
}

interface Statement : Node

interface Expression : Statement

class BlockStatement(val statements: List<Statement>) : Statement {
    override fun getType(scope: Scope) = statements.last().getType(scope)
}

class NativeStatement(val type: Type, val procedure: (Scope) -> Expression) : Statement {
    override fun getType(scope: Scope) = type
}

class VariableDefinition(val name: String, val expression: Expression) : Expression {
    override fun getType(scope: Scope) = expression.getType(scope)
}

class Variable(val name: String) : Expression {
    override fun getType(scope: Scope) = scope.getVariable(name).getType(scope)
}

class Constant(val value: Any, val type: Type) : Expression {
    override fun getType(scope: Scope) = type
}

class SimpleExpression(val expression: Expression) : Expression {
    override fun getType(scope: Scope) = expression.getType(scope)
}

class FunctionCall(val name: String, val params: List<Expression>) : Expression {
    override fun getType(scope: Scope): Type {
        return scope.getFunction(name, params.map { it.getType(scope) }).returnType
    }
}

data class FunctionParam(val name: String, val type: Type)

class FunctionDefinition(val name: String, val params: List<FunctionParam>, val returnType: Type, val blockStatement: BlockStatement) : Expression {
    override fun getType(scope: Scope): Type {
        return Type(params.map { it.type }.joinToString(",", "(", ")") + "->" + returnType)
    }
}
