package org.cloudchallenge.codex.security.controller

import org.cloudchallenge.codex.core.exception.FunctionalException
import org.cloudchallenge.codex.security.exception.InvalidCredentialsException
import org.cloudchallenge.codex.security.model.Auth
import org.cloudchallenge.codex.security.model.UserLogin
import org.cloudchallenge.codex.security.service.AuthenticationService
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@ConditionalOnClass(RestController::class)
class AuthenticationController(private val authenticationServices: List<AuthenticationService>) {

    companion object {
        private val logger = LoggerFactory.getLogger(AuthenticationController::class.java)
    }

    @PostMapping("/identity/authenticate")
    fun authenticate(@RequestBody userLogin: UserLogin): Auth {
        try {
            authenticationServices.sortedByDescending { it.rank() }.filter { it.accept(userLogin) }.forEach {
                try {
                    return it.authenticate(userLogin)
                } catch (e: Exception) {
                    logger.info("${it.javaClass.simpleName} can't authenticate user login", e.message)
                }
            }
            logger.error("No authentication service can authenticate user login")
            throw InvalidCredentialsException("Invalid login")

        } catch (e: FunctionalException) {
            throw InvalidCredentialsException("Invalid login")
        }
    }
}