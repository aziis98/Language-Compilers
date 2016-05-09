package com.aziis98.dynamical

import com.aziis98.corecompiler.*
import java.io.File

// Copyright Antonio De Lucreziis 2016

fun main(args: Array<String>) {
    val file = if (args.size > 0) File(args[0]) else throw IllegalArgumentException("You must pass a file name!")

    runCode(file.readText())
}

fun removeComments(source: String): String {
    return source.replace(Regex("""//.*$""", RegexOption.MULTILINE), "")
}

fun runCode(source: String) {
    val tokens = tokenize(removeComments(source))
    val program = Program()

    while (tokens.isNotEmpty()) {
        while (tokens.peek().isBlank()) {
            tokens.popIfBlack()
        }

        val statement = parseStatement(tokens)
        if (statement != null) program.statements.add(statement)
        tokens.popIfBlack()
    }

    program.executeProgram()

    println(program)
}

interface Element

abstract class Statement : Element {
    abstract fun execute(program: Program, scope: Scope)
}

open class DeclareStatement(val varName: String, val value: Value<*>, val isConstant: Boolean) : Statement() {
    override fun execute(program: Program, scope: Scope) {
        scope.addVariable(Variable(varName, value.type, value))
    }
}

interface Value<T> : Element {
    val type: VariableType
    val value: T
}

abstract class ValueLitteral<T> : Value<T> {
    override fun toString() = "Litteral(type = $type, value = $value)"
}

class ValueLitteralBoolean(override val value: Boolean) : ValueLitteral<Boolean>() {
    override val type: VariableType
        get() = VariableType("Boolean")
}

class ValueLitteralInteger(override val value: Int) : ValueLitteral<Int>() {
    override val type: VariableType
        get() = VariableType("Integer")
}

class ValueLitteralDecimal(override val value: Double) : ValueLitteral<Double>() {
    override val type: VariableType
        get() = VariableType("Decimal")
}

class ValueLitteralString(override val value: String) : ValueLitteral<String>() {
    override val type: VariableType
        get() = VariableType("String")
}

class ValueLitteralObject(val typeName: String, override val value: Scope) : ValueLitteral<Scope>() {
    override val type: VariableType
        get() = VariableType(typeName)
}


fun parseStatement(tokens: TokenList): Statement? {
    if (tokens.popIf { it.isBlank() }) {
        return null
    }

    if (tokens.peek() == "val" || tokens.peek() == "var") {
        return parseDeclaration(tokens)
    }

    throw ParsingException(tokens, "Unable to parse statement!")
}

fun parseDeclaration(tokens: TokenList): DeclareStatement {
    // tokens.popIfBlack()
    // var|val
    val declType = tokens.pop()
    tokens.pop()
    // <var-name>
    val varName = tokens.pop()
    tokens.popIfBlack()
    // =
    tokens.pop()
    tokens.popIfBlack()
    val varValue = parseExpression(tokens)

    return DeclareStatement(varName, varValue, declType == "val")
}

fun parseExpression(tokens: TokenList): Value<*> {
    if (isValidIdentifier(tokens.peek())) {
        if (tokens.indexOfFirst { it == "{" }.printExpr("indexof: ") <= 2) {
            return parseObject(tokens)
        }
    }

    return parseLitteral(tokens)
}

val PATTERN_DOUBLE = """[+-]?(\d)*\.(\d)+""".toRegex()
val PATTERN_INTEGER = """[+-]?(\d)+""".toRegex()

fun isValidIdentifier(string: String): Boolean {
    val list = string.toCharArray().toList()
    return list.first().isJavaIdentifierStart() && list.takeLast(list.size - 1).all { it.isJavaIdentifierPart() }
}


fun parseLitteral(tokens: TokenList): ValueLitteral<*> {
    val nextToken = tokens.peek()

    if (nextToken == "\"") {
        tokens.pop()
        val str = StringBuilder()
        while (tokens.peek() != "\"") {
            str.append(tokens.pop())
        }
        tokens.pop()
        return ValueLitteralString(str.toString())
    }
    else if (PATTERN_DOUBLE.matches(nextToken)) {
        val value = java.lang.Double.parseDouble(tokens.pop())
        return ValueLitteralDecimal(value)
    }
    else if (PATTERN_INTEGER.matches(nextToken)) {
        val value = Integer.parseInt(tokens.pop())
        return ValueLitteralInteger(value)
    }
    else if (nextToken == "true" || nextToken == "false") {
        return ValueLitteralBoolean(java.lang.Boolean.parseBoolean(nextToken))
    }

    throw ParsingException(tokens, "Unable to parse Litteral")
}

fun parseObject(tokens: TokenList): ValueLitteralObject {
    // <type>
    val objType = tokens.pop()
    tokens.popIfBlack()
    // {
    tokens.pop()
    tokens.popIfBlack()

    var listIndex = 0
    val objData = Scope()


    while (tokens.peek() != "}") {
        var (propName, propValue) = parseObjectKeyValue(tokens, objType == "List")
        if (propName == null) {
            propName = (listIndex++).toString()
        }
        objData.addVariable(Variable(propName, VariableType(objType), propValue))
        tokens.popIfBlack()
    }

    // }
    tokens.pop()

    return ValueLitteralObject(objType, objData)
}

// <name> = <value> | <value>
fun parseObjectKeyValue(tokens: TokenList, isList: Boolean = false): Pair<String?, Value<*>> {
    while (tokens.peek().isBlank()) {
        tokens.popIfBlack()
    }

    if (isList) {
        val propValue = parseExpression(tokens)
        tokens.popIfBlack()
        return null to propValue
    }

    // <name>
    val propName = tokens.pop()
    tokens.popIfBlack()
    // =
    tokens.pop()
    tokens.popIfBlack()
    // <value>
    val propValue = parseExpression(tokens)
    tokens.popIfBlack()

    return propName to propValue
}































