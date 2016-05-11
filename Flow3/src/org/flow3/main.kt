package org.flow3

import com.aziis98.corecompiler.*
import java.io.File

// Copyright 2016 Antonio De Lucreziis

fun myGluer(a: Char, b: Char): Boolean {
    if (a == '/' && b == '/') return true
    if (a == '/' && b == '*') return true
    if (a == '*' && b == '/') return true

    return defaultGluer(a, b)
}

fun main(args: Array<String>) {
    val file = if (args.size > 0) File(args[0]) else throw IllegalArgumentException("You must pass a file name!")

    val source = file.readText()
    val bareSource = source
        .replace(Regex("//.*$", RegexOption.MULTILINE), "")
        .replace(Regex("""/\*(.*)\*/""", RegexOption.DOT_MATCHES_ALL), "")

    println("====================")
    println(bareSource)
    println("====================")

    val tokens = tokenize(bareSource, ::myGluer)

    println(tokens.map { "[${it.replace("\n", "\\n").replace("\t", "\\t")}]" }.joinToString(" "))


    Program(listOf(

    ))
}