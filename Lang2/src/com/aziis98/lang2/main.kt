package com.aziis98.lang2

import com.aziis98.corecompiler.defaultGluer

// Copyright 2016 Antonio De Lucreziis

fun myGluer(a: Char, b: Char): Boolean {
    if (a == '/' && b == '/') return true
    if (a == '/' && b == '*') return true
    if (a == '*' && b == '/') return true

    return defaultGluer(a, b)
}

fun main(args: Array<String>) {
    /*
    val file = if (args.size > 0) File(args[0]) else throw IllegalArgumentException("You must pass a file name!")

    val tokens = tokenize(file.readText(), ::myGluer)

    println(tokens.map { "[${it.replace("\n", "\\n").replace(" ", "\\s").replace("\t", "\\t")}]" }.joinToString(" "))
    */

    val program = BlockStatement(listOf(
        FunctionCall("println", listOf(
            Constant("The Answer to Life And Everything :", CoreTypes.STRING)
        )),
        FunctionCall("println", listOf(
            FunctionCall(
                "-",
                listOf(
                    Constant(55, CoreTypes.INT),
                    Constant(13, CoreTypes.INT))
                )
        ))
    ))

    runAst(program)
}
