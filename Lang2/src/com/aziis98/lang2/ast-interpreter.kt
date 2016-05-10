package com.aziis98.lang2

// Copyright 2016 Antonio De Lucreziis

val coreScope = Scope().apply {
    addFunction(
        FunctionDefinition("plus", listOf(
            FunctionParam("a", CoreTypes.NUMERIC),
            FunctionParam("b", CoreTypes.NUMERIC)
        ), CoreTypes.NUMERIC, NativeStatement(CoreTypes.NUMERIC) { scope ->
            val a = scope.getVariable("a").expression as Int
            val b = scope.getVariable("a").expression as Int

            return@NativeStatement a + b
        })
    )
}

fun runAst(program: Node) {
    runRecursive(program, Scope())
}

private fun runRecursive(node: Node, scope: Scope): Any {
    when (node) {
        is BlockStatement -> {
            val blockScope = Scope(parent = scope)

            node.statements.forEach {
                runRecursive(it, blockScope)
            }
        }
        is FunctionDefinition -> {
            scope.addFunction(Definition())
        }
    }
}

private fun evalExpression(expression: Expression, scope: Scope) : Any {
    when (expression) {
        is FunctionCall -> {
            evalFunctionCall(scope.getFunction(expression.name, expression.params.map { it.getType(scope) }), expression, scope)
        }
    }
}

private fun evalFunctionCall(functionDefinition: FunctionDefinition, scope: Scope): Any {
    return runRecursive(functionDefinition.blockStatement, scope)
}