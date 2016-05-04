package com.aziis98.corecompiler

import java.util.*

// Copyright 2016 Antonio De Lucreziis

fun otherGluer(a: Char, b: Char) : Boolean {
    return defaultGluer(a, b) || a == b || a == '<' && b == '-' || a == '-' && b == '>'
}

fun defaultGluer(a: Char, b: Char): Boolean {
    return ((a.isJavaIdentifierPart() || a == '.') && (b.isJavaIdentifierPart() || b == '.')) ||
        (a.isWhitespace() && b.isWhitespace())
}

fun tokenize(source: String, gluer: (Char, Char) -> Boolean = ::defaultGluer): List<String> {
    var cToken = StringBuilder()
    var list = LinkedList<String>()

    for (i in 0 .. source.length - 2) {
        cToken.append(source[i])
        if (gluer(source[i], source[i + 1]) == false) {
            list.add(cToken.toString())
            cToken.setLength(0)
        }
    }

    if (gluer(source[source.lastIndex - 1], source.last())) {
        cToken.append(source.last())
    } else {
        cToken.append(source.last())
    }

    list.add(cToken.toString())

    return list
}
