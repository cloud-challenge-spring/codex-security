package org.cloudchallenge.codex.security.service

import org.cloudchallenge.codex.security.model.Auth
import org.cloudchallenge.codex.security.model.UserLogin

interface AuthenticationService {

    fun rank(): Int

    fun accept(userLogin: UserLogin): Boolean

    fun authenticate(userLogin: UserLogin): Auth
}