package org.cloudchallenge.codex.security.model

import java.util.*

data class Auth(var id: String,
                var login: String?,
                val firstName: String?,
                val lastName: String?,
                val token: String?,
                val locale: Locale) {

    constructor(authenticable: Authenticable, token: String? = null) : this(authenticable.identifier, authenticable.login,
            authenticable.firstName, authenticable.lastName,token, authenticable.locale)

    override fun toString(): String {
        return "Auth('$id', '$login')"
    }
}