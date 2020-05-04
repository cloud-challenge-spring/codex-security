package org.cloudchallenge.codex.security.exception

import org.cloudchallenge.codex.core.exception.FunctionalException

class DisabledUserException (message: String, code:String=ERROR_CODE): FunctionalException(code, message){
    companion object {
        const val ERROR_CODE = "DISABLED_USER"
    }
}