package com.aziis98.corecompiler

// Copyright 2016 Antonio De Lucreziis
/*
class Scope() {
    val variables: HashMap<String, Variable> = HashMap()

    fun addVariable(variable: Variable) {
        variables.put(variable.name, variable)
    }

    fun getVariable(name: String): Variable {
        return variables[name] ?:
            throw VariableNotFoundException("The variable '$name' does not exists in scope ${this.toString()}!")
    }

    override fun toString() = variables.map { "${it.key}: ${it.value}" }.joinToString("\n  ", "{\n  ", "\n}")
}

data class Variable(val name: String, val type: VariableType, var value: Any) {
    override fun toString() = "Variable(name= $name, type= ${type.name}, value= $value)"
}

data class VariableType(val name: String)

data class TypeDefinition(val name: String, val extending: Set<TypeDefinition> = setOf()) {

    fun isSubOf(typeDefinition: TypeDefinition): Boolean {
        return typeDefinition == this || extending.any { it.isSubOf(typeDefinition) }
    }

    fun isSuperOf(typeDefinition: TypeDefinition): Boolean {
        return typeDefinition == this || typeDefinition.extending.any { it.isSuperOf(this) }
    }

}
*/

class DefinitionNotFoundException(message: String = "") : RuntimeException(message)

/*
class TypeDefTest() {
    @Test
    fun test1() {
        val typeGeneric = TypeDefinition("generic")

        val typeString = TypeDefinition("string", setOf(typeGeneric))
        val typeNumeric = TypeDefinition("numeric", setOf(typeGeneric))

        val typeInteger = TypeDefinition("integer", setOf(typeNumeric))
        val typeDecimal = TypeDefinition("decimal", setOf(typeNumeric))

        val typeA = TypeDefinition("A", setOf(typeGeneric))
        val typeB = TypeDefinition("B", setOf(typeGeneric))
        val typeC = TypeDefinition("C", setOf(typeA, typeB))

        Assert.assertTrue(typeA.isSuperOf(typeC))
        Assert.assertTrue(typeC.isSubOf(typeA))
    }
}

*/


























