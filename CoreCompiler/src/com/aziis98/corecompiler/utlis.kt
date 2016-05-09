package com.aziis98.corecompiler

// Copyright Antonio De Lucreziis 2016

infix fun String.nl(other: String): String = this + "\n" + other

fun <T> T.printExpr(label: String = ""): T {
    println(label + this)
    return this
}