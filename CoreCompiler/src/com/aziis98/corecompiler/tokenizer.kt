package com.aziis98.corecompiler

import java.util.*

// Copyright 2016 Antonio De Lucreziis

fun otherGluer(a: Char, b: Char) : Boolean {
    return defaultGluer(a, b) || a == b || a == '<' && b == '-' || a == '-' && b == '>'
}

fun defaultGluer(a: Char, b: Char): Boolean {
    if (a.isJavaIdentifierPart() && b.isJavaIdentifierPart()) return true
    if (a == '.' || b == '.') return true
    if (a == '\n' || b == '\n') return false
    if (a.isWhitespace() && b.isWhitespace()) return true

    return false
}

class TokenList : LinkedList<String>()

fun tokenize(source: String, gluer: (Char, Char) -> Boolean = ::defaultGluer): TokenList {
    val cToken = StringBuilder()
    val list = TokenList()

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

fun TokenList.popIf(predicate: (String) -> Boolean): Boolean {
    if (isEmpty()) return false

    if (predicate(peek())) {
        pop()
        return true
    }
    return false
}

fun TokenList.popIfBlack(): Boolean {
    return popIf { it.isBlank() }
}

open class CompilationException(message: String) : RuntimeException(message)

open class ParsingException(val tokens: TokenList, message: String) : CompilationException(message) {
    override val message: String
        get() {
            return "${super.message}" nl
                    "Compilation Stack: " nl
                    tokens.joinToString(limit = 15)
        }
}


















