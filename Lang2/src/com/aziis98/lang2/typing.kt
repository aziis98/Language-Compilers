package com.aziis98.lang2


import org.junit.*

// Copyright 2016 Antonio De Lucreziis

object CoreTypes {
    val GENERIC  = Type("Generic")
    val FUNCTION = Type("Function")
    val STRING   = Type("String", listOf(GENERIC))

    val NUMERIC  = Type("Numeric", listOf(GENERIC))
    val INT      = Type("Int", listOf(NUMERIC))
    val DECIMAL  = Type("Decimal", listOf(NUMERIC))
}

open class Type(val name: String, val parents: List<Type> = emptyList()) {

    open fun isSubOf(other: Type): Boolean {
        return other == this || parents.any { it.isSubOf(other) }
    }

    open fun isSuperOf(other: Type): Boolean {
        return other == this || other.parents.any { it.isSuperOf(this) }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Type) return false

        return this.name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode() * 31
    }

}

class FunctionType(val inputs: List<Type>, val output: Type) : Type("", listOf(CoreTypes.FUNCTION)) {
    override fun isSuperOf(other: Type): Boolean {
        if (other !is FunctionType) return false

        inputs.forEachIndexed { i, inTypes ->
            if (!inTypes.isSuperOf(other.inputs[i])) {
                return false
            }
        }

        return output.isSuperOf(other.output)
    }
}

class FunctionTypeTest {
    @Test
    fun test1() {
        val addition = FunctionType(listOf(CoreTypes.INT, CoreTypes.INT), CoreTypes.INT)

        val genericAddition = FunctionType(listOf(CoreTypes.NUMERIC, CoreTypes.NUMERIC), CoreTypes.NUMERIC)

        Assert.assertTrue("$addition  is sub type of $genericAddition", genericAddition.isSuperOf(addition))
    }
}