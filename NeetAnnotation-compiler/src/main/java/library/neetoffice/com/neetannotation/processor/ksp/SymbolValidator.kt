package library.neetoffice.com.neetannotation.processor.ksp

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate

interface SymbolValidator {
    fun isValid(symbol: KSAnnotated): Boolean
}

class NActivityValidator() : SymbolValidator {
    override fun isValid(symbol: KSAnnotated): Boolean {
        return symbol is KSClassDeclaration &&
                symbol.validate() &&
                symbol.isActivity()
    }
}