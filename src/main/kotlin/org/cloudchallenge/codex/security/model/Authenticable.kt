package org.cloudchallenge.codex.security.model

import java.util.*

data class Authenticable(
    var identifier: String,
    var login: String?,
    var password: String?,
    var firstName: String?,
    var lastName: String?,
    var active: Boolean,
    var locale: Locale,
    var authType: UserLoginType
)