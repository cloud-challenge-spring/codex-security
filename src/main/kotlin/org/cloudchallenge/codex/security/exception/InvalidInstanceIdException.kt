package org.cloudchallenge.codex.security.exception

import org.cloudchallenge.codex.core.exception.FunctionalException

class InvalidInstanceIdException(message: String, code:String= InvalidCredentialsException.ERROR_CODE): FunctionalException(code, message){
    companion object {
        const val ERROR_CODE = "INVALID_INSTANCE_ID"
    }
}