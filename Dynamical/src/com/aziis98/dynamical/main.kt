package com.aziis98.dynamical

import com.aziis98.corecompiler.*
import java.io.File
import java.util.*

// Copyright Antonio De Lucreziis 2016

fun main(args: Array<String>) {
    val file = if (args.size > 0) File(args[0]) else throw IllegalArgumentException("You must pass a file name!")

    runCode(file.readText())
}

fun runCode(source: String) {
    val tokens = tokenize(source)
    val program = Program()

    while (tokens.isNotEmpty()) {
        program.statements.add(parseStatement(tokens))
    }

    program.executeProgram()
}

interface Element

abstract class Statement : Element

open class DeclareStatement(val varName: String, val value: Value, val isConstant: Boolean) : Statement()

interface Value : Element

abstract class ValueLitteral : Value

class ValueLitteralBoolean(val value: Boolean) : ValueLitteral()

class ValueLitteralInteger(val value: Int) : ValueLitteral()

class ValueLitteralDecimal(val value: Double) : ValueLitteral()

class ValueLitteralString(val value: String) : ValueLitteral()


fun parseStatement(tokens: TokenList) : Statement {
    if (tokens.peek() == "val" || tokens.peek() == "var") {
        return parseDeclaration(tokens)
    }
}

fun parseDeclaration(tokens: TokenList): DeclareStatement {
    // tokens.popIfBlack()
    // var|val
    var declType = tokens.pop()
    tokens.pop()
    // <var-name>
    val varName = tokens.pop()
    tokens.popIfBlack()
    // =
    tokens.pop()
    tokens.popIfBlack()
    var varValue = parseExpression(tokens)

    return DeclareStatement(varName, varValue, declType == "val")
}

fun parseExpression(tokens: TokenList): Value {
    return parseLitteral(tokens)
}

val PATTERN_DOUBLE = """[+-](\d)*\.(\d)+""".toRegex()
val PATTERN_INTEGER = """[+-](\d)""".toRegex()
fun parseLitteral(tokens: TokenList): ValueLitteral {
    val nextToken = tokens.peek()

    if (nextToken == "\"") {
        tokens.pop()
        val str = StringBuilder()
        while (nextToken != "\"") {
            str.append(tokens.pop())
        }
        return ValueLitteralString(str.toString())
    }
    else if (PATTERN_DOUBLE.matches(nextToken)) {
        val value = java.lang.Double.parseDouble(tokens.pop())
        return ValueLitteralDecimal(value)
    }
    else if (PATTERN_INTEGER.matches(nextToken)) {
        val value = Integer.parseInt(tokens.pop())
        return ValueLitteralInteger(value)
    } else if (nextToken == "true" || nextToken == "false") {
        return ValueLitteralBoolean(java.lang.Boolean.parseBoolean(nextToken))
    }

    throw ParsingException(tokens, "Unable to parse Litteral")
}


































